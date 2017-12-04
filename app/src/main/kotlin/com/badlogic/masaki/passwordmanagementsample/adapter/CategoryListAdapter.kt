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
import com.badlogic.masaki.passwordmanagementsample.room.pojo.CategoryImage
import com.daimajia.swipe.SwipeLayout

/**
 * Created by masaki on 2017/10/26.
 */
class CategoryListAdapter(private val mContext: Context,
                          private val mDataSet: MutableList<CategoryImage>,
                          private val mClickEvent: (CategoryImage?) -> Unit,
                          private val mDeleteClick: (CategoryImage) -> Unit,
                          private val mEditClick: (CategoryImage) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val TAG: String = CategoryListAdapter::class.java.simpleName
    }

    fun getItemPosition(category: CategoryImage): Int {
        return mDataSet.indexOf(category) + 1
    }

    override fun getItemCount(): Int {
        return mDataSet.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ViewType.HEADER.type
            else -> ViewType.BODY.type
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        return when(holder) {
            is HeaderViewHolder -> holder.bind()
            is CategoryListViewHolder ->  holder.bind(mContext, mDataSet[position - 1])
            else -> throw RuntimeException("unexpected exception.")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            ViewType.HEADER.type -> {
                val v: View = LayoutInflater.from(parent?.context).inflate(R.layout.header_category_list, parent, false)
                HeaderViewHolder(v, mClickEvent)
            }
            else -> {
                val v: View = LayoutInflater.from(parent?.context).inflate(R.layout.row_category_list, parent, false)
                CategoryListViewHolder(v, mClickEvent, mDeleteClick, mEditClick)
            }
        }
    }



    class CategoryListViewHolder(private val mItemView: View,
                                 private val mSurfaceClick: (CategoryImage) -> Unit,
                                 private val mDeleteClick: (CategoryImage) -> Unit,
                                 private val mEditClick: (CategoryImage) -> Unit)
        : RecyclerView.ViewHolder(mItemView) {
        private var mSwipeLayout: SwipeLayout? = null
        private var mSurfaceWrapper: ConstraintLayout? = null
        private var mFrameDelete: ConstraintLayout? = null
        private var mFrameEdit: ConstraintLayout? = null
        private var mIcon: ImageView? = null
        private var mTitle: TextView? = null

        init {
            mSwipeLayout = mItemView.findViewById(R.id.swipe_layout)
            mSwipeLayout?.showMode = SwipeLayout.ShowMode.PullOut

            mSurfaceWrapper = mItemView.findViewById(R.id.surface_wrapper)
            mFrameDelete = mItemView.findViewById(R.id.frame_button_delete)
            mFrameEdit = mItemView.findViewById(R.id.frame_button_edit)

            mIcon = mItemView.findViewById(R.id.imageView)
            mTitle = mItemView.findViewById(R.id.textView)
        }

        fun bind(context: Context, category: CategoryImage) {
            GlideApp.with(context).load(category.url)
                    .error(R.drawable.icon).override(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT).circleCrop()
                    .into(mIcon)

            mTitle?.text = category.categoryName
            mSurfaceWrapper?.setOnClickListener { mSurfaceClick(category) }
            mFrameDelete?.setOnClickListener { mDeleteClick(category) }
            mFrameEdit?.setOnClickListener { mEditClick(category) }
        }
    }

    class HeaderViewHolder(mItemView: View,
                           private val mFrameClick: (CategoryImage?) -> Unit)
        : RecyclerView.ViewHolder(mItemView) {
        private var mContentFrame: ConstraintLayout? = null

        init {
            mContentFrame = mItemView.findViewById(R.id.frame_header_category_list)
        }

        fun bind() {
            mContentFrame?.setOnClickListener({mFrameClick(null)})
        }
    }
}