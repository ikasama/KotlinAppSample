package com.badlogic.masaki.passwordmanagementsample.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.masaki.passwordmanagementsample.R

/**
 * Created by masaki on 2017/12/03.
 */
class AboutOpenSourceFragment : BaseFragment() {

    companion object {
        val TAG: String = AboutOpenSourceFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): AboutOpenSourceFragment = AboutOpenSourceFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_about_open_source, container, false)
    }
}