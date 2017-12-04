package com.badlogic.masaki.passwordmanagementsample.proxy

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.util.checkGrantResults

/**
 * Created by masaki on 2017/10/27.
 */
open abstract class PhotoLauncher {
    open abstract fun tryToLaunch(activity: AppCompatActivity, grantResults: IntArray)
    open abstract fun tryToLaunch(fragment: Fragment, grantResults: IntArray)
    open abstract fun launch(activity: AppCompatActivity)
    open abstract fun launch(fragment: Fragment)
}