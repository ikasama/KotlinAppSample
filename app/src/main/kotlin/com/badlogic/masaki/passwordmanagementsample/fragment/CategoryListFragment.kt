package com.badlogic.masaki.passwordmanagementsample.fragment

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
import com.badlogic.masaki.passwordmanagementsample.activity.EditCategoryActivity
import com.badlogic.masaki.passwordmanagementsample.adapter.CategoryListAdapter
import com.badlogic.masaki.passwordmanagementsample.constants.ErrorType
import com.badlogic.masaki.passwordmanagementsample.constants.ExtraCode.EXTRA_CATEGORY
import com.badlogic.masaki.passwordmanagementsample.constants.PrefKeys.USER_ID
import com.badlogic.masaki.passwordmanagementsample.constants.RequestCode.CODE_DELETE_CATEGORY
import com.badlogic.masaki.passwordmanagementsample.contract.CategoryListFragmentContract
import com.badlogic.masaki.passwordmanagementsample.presenter.CategoryListFragmentPresenter
import com.badlogic.masaki.passwordmanagementsample.room.pojo.CategoryImage
import com.badlogic.masaki.passwordmanagementsample.util.bind
import com.badlogic.masaki.passwordmanagementsample.storage.AppSettings
import com.badlogic.masaki.passwordmanagementsample.util.toCategory
import com.badlogic.masaki.passwordmanagementsample.dialog.DeleteCategoryDialogFragment

/**
 * Created by masaki on 2017/10/28.
 */
class CategoryListFragment: BaseFragment(), CategoryListFragmentContract.View {

    companion object {
        val TAG: String = CategoryListFragment::class.java.simpleName

        fun newInstance() = CategoryListFragment()
    }

    private var mRecyclerView: RecyclerView? = null
    private val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
    private val mDataSet: MutableList<CategoryImage> = mutableListOf()
    private val mAdapter: CategoryListAdapter by lazy { initAdapter() }
    private val mPresenter: CategoryListFragmentContract.Actions = CategoryListFragmentPresenter(this)

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? = inflater?.inflate(R.layout.fragment_category_list, container, false)
        initViews(v)
        return v
    }

    private fun initAdapter(): CategoryListAdapter {
        return CategoryListAdapter(
                mContext = activity,
                mDataSet =  mDataSet,
                mClickEvent = { c -> startActivity(Intent(activity, AccountListActivity::class.java).putExtra(EXTRA_CATEGORY, c?.toCategory())) },
                mDeleteClick = { c -> showDeleteDialog(c) },
                mEditClick = { c -> startEditCategoryActivity(c) }
        )
    }

    override fun showDeleteDialog(category: CategoryImage) {
        DeleteCategoryDialogFragment.newInstance(this, category, CODE_DELETE_CATEGORY)
                .show(activity.supportFragmentManager, null)
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

    override fun onResume() {
        super.onResume()
        /*
        starts loading category data
         */
        mPresenter.findCategory(rxActivity(), AppSettings.getString(context, USER_ID, ""))
    }

    override fun updateListView(categoryList: List<CategoryImage>) {
        mAdapter.apply {
            mDataSet.clear().bind { mDataSet.addAll(categoryList) }
            notifyDataSetChanged()
        }
    }

    override fun showErrorMessage(errorType: ErrorType) {
        when (errorType) {
            ErrorType.DELETE -> {
                Toast.makeText(activity.applicationContext, getString(R.string.fail_delete_category), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun notifyCategoryDeleted(category: CategoryImage) {
        val pos = mAdapter.getItemPosition(category)
        mDataSet.remove(category)
        mAdapter.notifyItemRemoved(pos)
    }

    override fun startEditCategoryActivity(category: CategoryImage) {
        startActivity(Intent(activity, EditCategoryActivity::class.java).putExtra(EXTRA_CATEGORY, category))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
        /*
         * called when the positive button is clicked on DeleteCategoryDialogFragment
         */
            CODE_DELETE_CATEGORY -> {
                if(resultCode == DialogInterface.BUTTON_POSITIVE) {
                    data?.let {
                        mPresenter.deleteCategory(rxActivity(),
                                it.getParcelableExtra(EXTRA_CATEGORY))
                    }
                }
            }
        }
    }

}