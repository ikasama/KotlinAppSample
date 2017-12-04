package com.badlogic.masaki.passwordmanagementsample.fragment

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.activity.AccountListActivity
import com.badlogic.masaki.passwordmanagementsample.activity.EditAccountActivity
import com.badlogic.masaki.passwordmanagementsample.activity.EditAccountActivity.Companion.FLAG_UPDATE
import com.badlogic.masaki.passwordmanagementsample.adapter.AccountListAdapter
import com.badlogic.masaki.passwordmanagementsample.constants.ErrorType
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_ACCOUNT
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CATEGORY
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CATEGORY_ID
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CREATE_FLAG
import com.badlogic.masaki.passwordmanagementsample.constants.RequestCode.CODE_DELETE_ACCOUNT
import com.badlogic.masaki.passwordmanagementsample.contract.AccountListFragmentContract
import com.badlogic.masaki.passwordmanagementsample.listener.OnAccountItemClickListener
import com.badlogic.masaki.passwordmanagementsample.presenter.AccountListFragmentPresenter
import com.badlogic.masaki.passwordmanagementsample.room.Category
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage
import com.badlogic.masaki.passwordmanagementsample.util.bind
import com.badlogic.masaki.passwordmanagementsample.dialog.DeleteAccountDialogFragment

/**
 * Created by masaki on 2017/10/28.
 */
class AccountListFragment: BaseFragment(), AccountListFragmentContract.View {

    private val mPresenter: AccountListFragmentContract.Actions = AccountListFragmentPresenter(this)

    private val mAccountClickListener: OnAccountItemClickListener by lazy { getAttachedContext() }

    companion object {
        val TAG: String = AccountListFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(category: Category?): AccountListFragment {
            val f = AccountListFragment()
            val args = Bundle()
            args.putParcelable(EXTRA_CATEGORY, category)
            f.arguments = args
            return f
        }
    }

    private val mCategory: Category? by lazy { getCategory() }

    private fun getCategory(): Category? {
        return arguments?.getParcelable(EXTRA_CATEGORY)
    }

    private var mRecyclerView: RecyclerView? = null
    private val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
    private val mDataSet: MutableList<AccountImage> = mutableListOf()
    private val mAdapter: AccountListAdapter by lazy { initAdapter() }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getAttachedContext()
    }

    private fun getAttachedContext(): OnAccountItemClickListener {
        when (context) {
            is AccountListActivity -> return context as OnAccountItemClickListener
            else -> throw RuntimeException("context must be AccountListActivity.")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? = inflater?.inflate(R.layout.fragment_account_list, container, false)
        initViews(v)
        return v
    }

    private fun initAdapter(): AccountListAdapter {
        return AccountListAdapter(
                mContext = activity,
                mDataSet = mDataSet,
                mSurfaceClick = { account ->
                    showAccountDetailScreen(account)
                },
                mEditClick = { account ->
                    startEditAccountActivity(account, mCategory?.categoryId)
                },
                mDeleteClick = { account ->
                    showDeleteDialog(account)
                })

    }

    private fun initViews(v: View?) {
        mRecyclerView = v?.findViewById(R.id.listView)
        mRecyclerView?.apply {
            addItemDecoration(DividerItemDecoration(activity, (mLayoutManager as LinearLayoutManager).orientation))
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mAdapter
        }
    }

    override fun showDeleteDialog(account: AccountImage) {
        DeleteAccountDialogFragment.newInstance(this, account, CODE_DELETE_ACCOUNT)
                .show(activity.supportFragmentManager, "deleteAccount")
    }

    override fun onResume() {
        super.onResume()
        /*
        starts loading account data
         */
        mPresenter.findAccount(rxActivity(), mCategory?.categoryId)
    }

    override fun updateListView(accountList: MutableList<AccountImage>) {
        mAdapter.apply {
            mDataSet.clear().bind { mDataSet.addAll(accountList) }
            notifyDataSetChanged()
        }
    }

    override fun showErrorMessage(errorType: ErrorType) {
        when (errorType) {
            ErrorType.DELETE -> {
                Toast.makeText(activity.applicationContext, getString(R.string.fail_delete_account), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun notifyAccountDeleted(account: AccountImage) {
        val pos = mAdapter.getItemPosition(account)
        mDataSet.remove(account)
        mAdapter.notifyItemRemoved(pos)
    }

    override fun showAccountDetailScreen(account: AccountImage) {
        //tells the parent activity that an account item has been clicked.
        mAccountClickListener.onAccountItemClick(account)
    }

    override fun startEditAccountActivity(account: AccountImage, categoryId: Int?) {
        val i = Intent(activity, EditAccountActivity::class.java)
                .putExtra(EXTRA_ACCOUNT, account)
                .putExtra(EXTRA_CREATE_FLAG, FLAG_UPDATE)
                .putExtra(EXTRA_CATEGORY_ID, categoryId)
        startActivity(i)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
        /*
         * called when the positive button on DeleteAccountDialogFragment is clicked.
         */
            CODE_DELETE_ACCOUNT -> {
                if(resultCode == DialogInterface.BUTTON_POSITIVE) {
                    data?.let {
                        mPresenter.deleteAccount(rxActivity(),
                                it.getParcelableExtra(EXTRA_ACCOUNT))
                    }
                }
            }
        }
    }
}