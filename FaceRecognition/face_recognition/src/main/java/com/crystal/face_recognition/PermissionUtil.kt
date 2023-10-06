package com.crystal.face_recognition

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionUtil {

    fun checkPermission(context: Context, permissionList: List<String>): Boolean {
        permissionList.forEach {
            if (ContextCompat.checkSelfPermission(
                    context,
                    it
            ) == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    fun requestPermission(activity: Activity, permissionList: List<String>) {
        // 실제 할때는 requestCode도 상황처에 맞게끔 세팅
        ActivityCompat.requestPermissions(activity, permissionList.toTypedArray(), 1)
    }
}