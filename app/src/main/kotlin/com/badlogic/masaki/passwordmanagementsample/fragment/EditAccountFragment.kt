package com.badlogic.masaki.passwordmanagementsample.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.badlogic.masaki.passwordmanagementsample.GlideApp
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_ACCOUNT
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CATEGORY_ID
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CREATE_FLAG
import com.badlogic.masaki.passwordmanagementsample.constants.Image.THUMBNAIL_HEIGHT
import com.badlogic.masaki.passwordmanagementsample.constants.Image.THUMBNAIL_WIDTH
import com.badlogic.masaki.passwordmanagementsample.constants.RequestCode.CODE_CAMERA
import com.badlogic.masaki.passwordmanagementsample.constants.RequestCode.CODE_GALLERY
import com.badlogic.masaki.passwordmanagementsample.contract.EditAccountFragmentContract
import com.badlogic.masaki.passwordmanagementsample.presenter.EditAccountFragmentPresenter
import com.badlogic.masaki.passwordmanagementsample.proxy.CameraLaunchProxy
import com.badlogic.masaki.passwordmanagementsample.proxy.GalleryLaunchProxy
import com.badlogic.masaki.passwordmanagementsample.proxy.PermissionProxy
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage
import com.badlogic.masaki.passwordmanagementsample.util.LogWrapperD
import com.badlogic.masaki.passwordmanagementsample.util.toEditable
import com.badlogic.masaki.passwordmanagementsample.dialog.ChoosePhotoDialogFragment
import com.badlogic.masaki.passwordmanagementsample.util.showValidationError
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty

/**
 * Created by masaki on 2017/11/21.
 */
class EditAccountFragment : BasePermissionFragment(),
        ChoosePhotoDialogFragment.Callback,
        View.OnClickListener,
        EditAccountFragmentContract.View,
        Validator.ValidationListener {
    @NotEmpty
    @Length(min = 1, max = 64)
    private var mEditAccountName: EditText? = null
    private var mEditPassword: EditText? = null
    private var mEditComment: EditText? = null
    private var mThumbnailImage: ImageView? = null

    private val mValidator: Validator = Validator(this)

    private var mAccount: AccountImage? = null
    private var mCategoryId: Int? = null

    private val mPresenter: EditAccountFragmentContract.Actions
            = EditAccountFragmentPresenter(this)

    companion object {
        val TAG: String = EditAccountFragment::class.java.simpleName
        private val KEY_NAME = "name"
        private val KEY_PASSWORD = "password"
        private val KEY_COMMENT = "comment"

        fun newInstance(createFlag: Int, account: AccountImage?): EditAccountFragment {
            val fragment = EditAccountFragment()
            val args = Bundle()
            account?.let {
                args.putParcelable(EXTRA_ACCOUNT, it)
            }
            args.putInt(EXTRA_CREATE_FLAG, createFlag)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        arguments?.let {
            mCategoryId = it.getInt(EXTRA_CATEGORY_ID)
            mAccount = it.getParcelable(EXTRA_ACCOUNT) ?: AccountImage()
        }
        val view: View? = inflater?.inflate(R.layout.fragment_edit_account, container, false)
        initViews(view)
        return view
    }

    override fun initViews(v: View?) {
        v?.run {
            mEditAccountName = findViewById(R.id.editAccountName)
            mEditPassword = findViewById(R.id.editPassword)
            mEditComment = findViewById(R.id.editComment)
            mThumbnailImage = findViewById(R.id.imageIcon)
            findViewById<Button>(R.id.buttonIcon)?.setOnClickListener(this@EditAccountFragment)
            findViewById<Button>(R.id.buttonRegister)?.setOnClickListener(this@EditAccountFragment)
        }
        mValidator.setValidationListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.buttonIcon -> showPickPhotoDialog()
            R.id.buttonRegister -> mValidator.validate()
            else -> return
        }
    }

    private fun registerAccount(account: AccountImage) {
        mPresenter.registerAccount(rxActivity(), account)
    }

    override fun showPickPhotoDialog() {
        ChoosePhotoDialogFragment.newInstance(this as ChoosePhotoDialogFragment.Callback)
                .show(activity.supportFragmentManager, null)
    }

    override fun onBootGalleryFromDialog() {
        mPermissionProxy.verifyPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                CODE_GALLERY, this)
    }

    override fun onBootCameraFromDialog() {
        mPermissionProxy.verifyPermission(Manifest.permission.CAMERA,
                CODE_CAMERA, this as PermissionProxy.Callback)
    }

    override fun onVerifyPermissionGranted(permission: String, requestCode: Int) {
        when {
            requestCode === CODE_CAMERA -> CameraLaunchProxy().launch(this)
            requestCode === CODE_GALLERY -> GalleryLaunchProxy().launch(this)
            else -> return
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when {
            grantResults.isEmpty() -> return
            requestCode === CODE_CAMERA -> CameraLaunchProxy().tryToLaunch(this, grantResults)
            requestCode === CODE_GALLERY -> GalleryLaunchProxy().tryToLaunch(this, grantResults)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode){
            Activity.RESULT_OK -> mPresenter.loadData(rxActivity(), requestCode, data)
            else -> return
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mEditAccountName?.text?.let { outState?.putString(KEY_NAME, it.toString()) }
        mEditPassword?.text?.let { outState?.putString(KEY_PASSWORD, it.toString()) }
        mEditComment?.text?.let { outState?.putString(KEY_COMMENT, it.toString()) }
        mAccount?.let { outState?.putParcelable(EXTRA_ACCOUNT, it)}
        mCategoryId?.let { outState?.putInt(EXTRA_CATEGORY_ID, it) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        restoreViews(savedInstanceState)
    }

    override fun restoreViews(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            mAccount = arguments?.getParcelable(EXTRA_ACCOUNT) ?: AccountImage()
            mCategoryId = arguments?.getInt(EXTRA_CATEGORY_ID)
            mEditAccountName?.text = mAccount?.accountName?.toEditable()
            mEditPassword?.text = mAccount?.password?.toEditable()
            mEditComment?.text = mAccount?.comment?.toEditable()
            mAccount?.url?.let {
                GlideApp.with(this@EditAccountFragment).load(it)
                        .override(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
                        .circleCrop().into(mThumbnailImage)
            }
        } else {
            mAccount = savedInstanceState.getParcelable(EXTRA_ACCOUNT)
            mCategoryId = savedInstanceState.getInt(EXTRA_CATEGORY_ID)
            mEditAccountName?.text = savedInstanceState.getString(KEY_NAME).toEditable()
            mEditPassword?.text = savedInstanceState.getString(KEY_PASSWORD).toEditable()
            mEditComment?.text = savedInstanceState.getString(KEY_COMMENT).toEditable()
            if (mAccount?.bitmap != null) {
                GlideApp.with(this@EditAccountFragment)
                        .load(mAccount?.bitmap?.copy(Bitmap.Config.RGB_565, true))
                        .override(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT).circleCrop().into(mThumbnailImage)
            } else {
                GlideApp.with(this@EditAccountFragment).load(mAccount?.url)
                        .override(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT).circleCrop().into(mThumbnailImage)
            }
        }
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        showValidationErrors(errors)
    }


    override fun showValidationErrors(errors: MutableList<ValidationError>?) {
        errors?.let {
            showValidationError(activity, errors)
        }
    }

    override fun onValidationSucceeded() {
        mAccount?.let {
            it.accountName = mEditAccountName?.text.toString()
            it.password = mEditPassword?.text.toString()
            it.comment = mEditComment?.text.toString()
            registerAccount(it)
        }
    }

    override fun showRegistrationError(msg: String) {
        Toast.makeText(activity?.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onRegistrationFinished(resId: Long) {
        when {
            resId > 0 -> {
                LogWrapperD(TAG, "succeeded. resId = " + resId + "¥ntid = " + Thread.currentThread().id)
                activity.finish()
            }
            else -> {
                LogWrapperD(TAG, "failed. resId = " + resId + "¥ntid = " + Thread.currentThread().id)
                Toast.makeText(activity.applicationContext, getString(R.string.fail_save_account), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLoadDataFinished(requestCode: Int, bitmapPair: Pair<Bitmap?, Bitmap?>) {
        mAccount?.bitmap?.apply {
            recycle()
            null
        }
        mAccount?.bitmap = bitmapPair.second
        mAccount?.bitmap?.let { mAccount?.updatesIcon = true }
        GlideApp.with(this).load(bitmapPair.first).override(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT)
                .circleCrop().into(mThumbnailImage)
    }

    override fun onLoadDataFailed(e: Throwable) {
        Toast.makeText(activity?.applicationContext, getString(R.string.fail_load_data),
                Toast.LENGTH_SHORT).show()
    }
}