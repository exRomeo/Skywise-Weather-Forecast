package com.example.skywise.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.skywise.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object PermissionsUtil {
    const val LOCATION_PERMISSIONS_REQUEST_CODE = 101
    const val NOTIFICATIONS_PERMISSION_REQUEST_CODE = 102


    fun requestLocationPermissions(activity: Activity) {

        if (ContextCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                activity.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSIONS_REQUEST_CODE
            )
        }

    }

    fun requestNotificationsPermission(activity: Activity) {
        if (ActivityCompat.checkSelfPermission(
                activity.applicationContext, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATIONS_PERMISSION_REQUEST_CODE
            )
        }
    }


    fun checkOverlayPermission(activity: Activity, context: Context) {
        if (!Settings.canDrawOverlays(context)) {
            val alertDialogBuilder = MaterialAlertDialogBuilder(context)
            alertDialogBuilder.setTitle(R.string.draw_over_other_apps)
                .setMessage(R.string.draw_over_other_apps_explaination)
                .setPositiveButton(R.string.ok) { dialog: DialogInterface, _ ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package: " + activity.applicationContext.packageName)
                    )
                    activity.startActivityForResult(intent, 1)
                    dialog.dismiss()
                }
                .setNegativeButton(R.string.cancel) { dialog: DialogInterface, _ -> dialog.dismiss() }
                .show()

        }
    }


}