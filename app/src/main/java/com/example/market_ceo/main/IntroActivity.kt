package com.example.market_ceo.main

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.market_ceo.MainActivity
import com.example.market_ceo.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.util.*

class IntroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        setPermission()
    }

    private fun startMain(){
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }, 1000)
    }

    private fun setPermission() {
        val permissionListener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() {
                startMain()
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                try {
                    Toast.makeText(this@IntroActivity, "[설정] > [권한] 에서 권한을 허용을 하셔야 합니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        .setData(Uri.parse("package:" + application.packageName))
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this@IntroActivity, "[설정] > [권한] 에서 권한을 허용을 하셔야 합니다.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                        .setData(Uri.parse("package:" + application.packageName))
                    startActivity(intent)
                }
                finish()
            }

        }
        TedPermission.with(this)
            .setPermissionListener(permissionListener)
            .setPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.SYSTEM_ALERT_WINDOW
            )
            .check()

    }
}