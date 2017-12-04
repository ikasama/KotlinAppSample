package com.badlogic.masaki.passwordmanagementsample.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.badlogic.masaki.passwordmanagementsample.R
import android.content.pm.PackageManager

/**
 * Created by masaki on 2017/12/03.
 */
class AboutAppFragment : BaseFragment() {

    companion object {
        val TAG: String = AboutAppFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(): AboutAppFragment = AboutAppFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View? =  inflater?.inflate(R.layout.fragment_about_app_top, container, false)
        initViews(v)
        return v
    }

    private fun initViews(v: View?) {
        v?.findViewById<View>(R.id.card_license)?.setOnClickListener { replaceFragment() }
        v?.findViewById<View>(R.id.card_github)?.setOnClickListener { launchBrowser() }
        v?.findViewById<TextView>(R.id.textVersion)?.let {
            try {
                val packageInfo = context.packageManager
                        .getPackageInfo(context.packageName, 0)
                it.text = packageInfo.versionName ?: ""
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun replaceFragment() {
        activity.supportFragmentManager.beginTransaction()
                .replace(R.id.content_container, AboutOpenSourceFragment.newInstance(), "f")
                .addToBackStack("f")
                .commit()
    }

    private fun launchBrowser() {
        //not implemented yet
    }
}