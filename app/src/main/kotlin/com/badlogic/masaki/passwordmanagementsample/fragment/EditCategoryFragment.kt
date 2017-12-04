package com.badlogic.masaki.passwordmanagementsample.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.badlogic.masaki.passwordmanagementsample.GlideApp
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.constants.RequestCode.CODE_CAMERA
import com.badlogic.masaki.passwordmanagementsample.constants.RequestCode.CODE_GALLERY
import com.badlogic.masaki.passwordmanagementsample.contract.EditCategoryFragmentContract
import com.badlogic.masaki.passwordmanagementsample.presenter.EditCategoryFragmentPresenter
import com.badlogic.masaki.passwordmanagementsample.proxy.CameraLaunchProxy
import com.badlogic.masaki.passwordmanagementsample.proxy.GalleryLaunchProxy
import com.badlogic.masaki.passwordmanagementsample.proxy.PermissionProxy
import com.badlogic.masaki.passwordmanagementsample.room.pojo.CategoryImage
import com.badlogic.masaki.passwordmanagementsample.util.LogWrapperD
import com.badlogic.masaki.passwordmanagementsample.util.toEditable
import com.badlogic.masaki.passwordmanagementsample.dialog.ChoosePhotoDialogFragment
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Length
import com.mobsandgeeks.saripaar.annotation.NotEmpty

/**
 * Created by masaki on 2017/10/31.
 */
class EditCategoryFragment: BasePermissionFragment(),
                            ChoosePhotoDialogFragment.Callback,
                            View.OnClickListener,
                            EditCategoryFragmentContract.View,
                            Validator.ValidationListener {

    companion object {
        val TAG: String = EditCategoryFragment::class.java.simpleName
        fun newInstance(category: CategoryImage?): EditCategoryFragment {
            val f = EditCategoryFragment()
            category?.let {
                val b = Bundle()
                b.putParcelable("Category", it)
                f.arguments = b
            }
            return f
        }
    }

    @NotEmpty
    @Length(min = 1, max = 64)
    private var mEditName: EditText? = null
    private var mButtonRegister: Button? = null
    private var mThumbnailImage: ImageView? = null
    private var mCategory: CategoryImage? = null

    private val mPresenter: EditCategoryFragmentContract.Actions
            = EditCategoryFragmentPresenter(this)

    private val mValidator = Validator(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? = inflater?.inflate(R.layout.fragment_edit_category, container, false)
        initViews(v)
        return v
    }

    override fun initViews(v: View?) {
        v?.run {
            findViewById<Button>(R.id.buttonRegister).setOnClickListener(this@EditCategoryFragment)
            findViewById<Button>(R.id.buttonIcon).setOnClickListener(this@EditCategoryFragment)
            mEditName = findViewById(R.id.editName)
            mThumbnailImage = findViewById(R.id.imageIcon)
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

    private fun registerCategory(category: CategoryImage) {
        mPresenter.registerCategory(rxActivity(), category)
    }

    override fun showPickPhotoDialog() {
        ChoosePhotoDialogFragment.newInstance(this as ChoosePhotoDialogFragment.Callback)
                .show(activity.supportFragmentManager, "pickPhoto")
    }

    override fun onBootGalleryFromDialog() {
        mPermissionProxy.verifyPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                CODE_GALLERY, this)
    }

    override fun onBootCameraFromDialog() {
        /**
         * Cameraを使えるアプリがあるかどうかを確認してから起動
         */
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
        mEditName?.text?.let { outState?.putString("name", it.toString()) }
        mCategory?.let { outState?.putParcelable("category", it)}
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        restoreViews(savedInstanceState)
    }

    override fun restoreViews(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            mCategory = arguments?.getParcelable("Category") ?: CategoryImage()
            val categoryName: Editable? = if (!TextUtils.isEmpty(mCategory?.categoryName)) {
                mCategory?.categoryName?.toEditable()
            } else {
                null
            }
            mEditName?.text = categoryName
            mCategory?.url?.let {
                GlideApp.with(this@EditCategoryFragment).load(it)
                        .override(128, 128)
                        .circleCrop().into(mThumbnailImage)
            }
        } else {
            mCategory = savedInstanceState.getParcelable("category")
            mEditName?.text = savedInstanceState.getString("name").toEditable()
            if (mCategory?.bitmap != null) {
                GlideApp.with(this@EditCategoryFragment).load(mCategory?.bitmap?.copy(Bitmap.Config.RGB_565, true))
                        .override(128, 128).circleCrop().into(mThumbnailImage)
            } else {
                GlideApp.with(this@EditCategoryFragment).load(mCategory?.url)
                        .override(128, 128).circleCrop().into(mThumbnailImage)

            }

        }
    }

    override fun onValidationFailed(errors: MutableList<ValidationError>?) {
        showValidationErrors(errors)
    }

    override fun showValidationErrors(errors: MutableList<ValidationError>?) {
        errors?.let {
            mEditName?.error = getString(R.string.categoryName_validation_error)
        }
    }

    override fun onValidationSucceeded() {
        mCategory?.let {
            it.categoryName = mEditName?.text.toString()
            registerCategory(it)
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
                Toast.makeText(activity.applicationContext, "failed. resId = " + resId, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLoadDataFinished(requestCode: Int, bitmapPair: Pair<Bitmap?, Bitmap?>) {
        mCategory?.bitmap?.apply {
            recycle()
            null
        }
        mCategory?.bitmap = bitmapPair.second
        mCategory?.bitmap?.let { mCategory?.updatesIcon = true }
        GlideApp.with(this)
                .load(bitmapPair.first)
                .override(128, 128)
                .circleCrop()
                .into(mThumbnailImage)
    }

    override fun onLoadDataFailed(e: Throwable) {
        Toast.makeText(activity?.applicationContext, getString(R.string.fail_load_data),
                Toast.LENGTH_SHORT).show()
    }
}

