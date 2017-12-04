package com.badlogic.masaki.passwordmanagementsample.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.badlogic.masaki.passwordmanagementsample.GlideApp
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.constants.Image.THUMBNAIL_HEIGHT
import com.badlogic.masaki.passwordmanagementsample.constants.Image.THUMBNAIL_WIDTH
import com.badlogic.masaki.passwordmanagementsample.room.pojo.AccountImage
import com.badlogic.masaki.passwordmanagementsample.util.dateFormat
import com.daimajia.swipe.SwipeLayout

/**
 * Created by masaki on 2017/10/26.
 */
class AccountListAdapter(val mContext: Context,
                         val mDataSet: MutableList<AccountImage>,
                         val mSurfaceClick: (AccountImage) -> Unit,
                         val mEditClick: (AccountImage) -> Unit,
                         val mDeleteClick: (AccountImage) -> Unit) : RecyclerView.Adapter<AccountListAdapter.ViewHolder>() {

    fun getItemPosition(account: AccountImage): Int {
        return mDataSet.indexOf(account)
    }

    override fun getItemCount(): Int = mDataSet.size

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bind(mContext, mDataSet[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent?.context).inflate(R.layout.row_account_list, parent, false)
        return ViewHolder(v, mSurfaceClick, mEditClick, mDeleteClick)
    }


    class ViewHolder(mItemView: View,
                     private val mSurfaceClick: (AccountImage) -> Unit,
                     private val mEditClick: (AccountImage) -> Unit,
                     private val mDeleteClick: (AccountImage) -> Unit)
        : RecyclerView.ViewHolder(mItemView) {
        private var mSwipeLayout: SwipeLayout? = null
        private var mSurfaceWrapper: ConstraintLayout? = null
        private var mFrameDelete: ConstraintLayout? = null
        private var mFrameEdit: ConstraintLayout? = null
        private var mIcon: ImageView? = null
        private var mTitle: TextView? = null
        private var mUpdatedAt: TextView? = null

        init {
            mSwipeLayout = mItemView.findViewById(R.id.swipe_layout)
            mSwipeLayout?.showMode = SwipeLayout.ShowMode.PullOut

            mSurfaceWrapper = mItemView.findViewById(R.id.surface_wrapper)
            mFrameDelete = mItemView.findViewById(R.id.frame_button_delete)
            mFrameEdit = mItemView.findViewById(R.id.frame_button_edit)

            mIcon = mItemView.findViewById(R.id.imageView)
            mTitle = mItemView.findViewById(R.id.textView)
            mUpdatedAt = itemView.findViewById(R.id.textUpdatedAt)
        }

        fun bind(context: Context, account: AccountImage) {
            GlideApp.with(context).load(account.url)
                    .error(R.drawable.icon).override(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT).circleCrop()
                    .into(mIcon)

            mTitle?.text = account.accountName
            mUpdatedAt?.text = dateFormat(account.updatedAt)
            mSurfaceWrapper?.setOnClickListener {
                mSurfaceClick(account)
            }
            mFrameDelete?.setOnClickListener {
                mDeleteClick(account)
            }
            mFrameEdit?.setOnClickListener {
                mEditClick(account)
            }
        }
    }
}