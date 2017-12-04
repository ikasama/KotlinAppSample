package com.badlogic.masaki.passwordmanagementsample.proxy

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.badlogic.masaki.passwordmanagementsample.R
import com.badlogic.masaki.passwordmanagementsample.constants.RequestCode.CODE_CAMERA
import com.badlogic.masaki.passwordmanagementsample.util.checkGrantResults

/**
 * Created by masaki on 2017/10/27.
 */
class CameraLaunchProxy: PhotoLauncher() {
    override fun launch(activity: AppCompatActivity) {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(activity.packageManager)?.let {
                activity.startActivityForResult(intent, CODE_CAMERA)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun launch(fragment: Fragment) {
        try {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.resolveActivity(fragment.activity.packageManager)?.let {
                fragment.startActivityForResult(intent, CODE_CAMERA)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun tryToLaunch(activity: AppCompatActivity, grantResults: IntArray) {
        if (!checkGrantResults(grantResults)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                Toast.makeText(activity.applicationContext, activity.getString(R.string.cannot_use_camera_without_perm), Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                activity.startActivity(intent)
            }
        } else {
            launch(activity)
        }
    }

    override fun tryToLaunch(fragment: Fragment, grantResults: IntArray) {
        if (!checkGrantResults(grantResults)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragment.activity, Manifest.permission.CAMERA)) {
                Toast.makeText(fragment.activity.applicationContext, fragment.activity.getString(R.string.cannot_use_camera_without_perm), Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", fragment.activity.packageName, null) //Fragmentの場合はgetContext().getPackageName()
                intent.data = uri
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                fragment.startActivity(intent)
            }
        } else {
            launch(fragment)
        }
    }
}