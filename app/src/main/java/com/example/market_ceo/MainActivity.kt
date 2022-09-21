package com.example.market_ceo

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.market_ceo.constant.Constant
import com.example.market_ceo.data.ShareData
import com.example.market_ceo.main.LoginFragment
import com.example.market_ceo.main.MainFragment
import com.example.market_ceo.member.MemberManager
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ShareData.shared().init(this)

        //UUID 확인
        checkDeviceUUID()

        MemberManager.getInstance().setMainActivity(this)
        if (MemberManager.getInstance().autoLogin) {
            //AccessToken 없을 경우 로그인 하지 않음
            if (TextUtils.isEmpty(MemberManager.getInstance().accessId) ||
                TextUtils.isEmpty(MemberManager.getInstance().accessToken)
            ) {
                setMainFragment(LoginFragment.newInstance())
            } else {
                MemberManager.getInstance().getUserInfo(this@MainActivity, null)
            }
        } else {
            setMainFragment(LoginFragment.newInstance())
        }
    }

    private fun checkDeviceUUID() {
        if (getUUIDFromPrefs()?.isEmpty() == true) {
            val uuid: String = getDeviceUUID()
            if (!TextUtils.isEmpty(uuid)) {
                saveUUIDToPrefs(uuid)
            }
        }
    }

    private fun saveUUIDToPrefs(uuid: String) {
        // Access Shared Preferences
        val preferences = getSharedPreferences("MUKKEBI.CEO", MODE_PRIVATE)
        val editor = preferences.edit()

        // Save to SharedPreferences
        editor.putString(Constant.PREF_UUID, uuid)
        editor.apply()
    }

    @SuppressLint("HardwareIds")
    private fun getDeviceUUID(): String {
        //DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(this);
        //return String.valueOf(deviceUuidFactory.getDeviceUuid());
        val tm =
            getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        var tmDevice = ""
        var tmSerial = ""
        var androidId = ""
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return ""
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                tmDevice = Settings.Secure.getString(
                    this.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
                tmSerial = Build.MODEL
            } else {
                tmDevice = "" + tm.deviceId
                tmSerial = "" + tm.simSerialNumber
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        androidId = "" + Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val deviceUuid = UUID(
            androidId.hashCode().toLong(),
            tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode()
                .toLong()
        )
        return deviceUuid.toString()
    }

    private fun getUUIDFromPrefs(): String? {
        val preferences = getSharedPreferences("MUKKEBI.CEO", MODE_PRIVATE)
        return preferences.getString(Constant.PREF_UUID, "")
    }

    fun setMainFragment(newFragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.layout_fragment,
                newFragment,
                newFragment.tag
            )
            .commitAllowingStateLoss()
    }

    fun setFragment(newFragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.layout_fragment,
                newFragment,
                newFragment.tag
            )
            .addToBackStack(newFragment.tag)
            .commitAllowingStateLoss()
    }

    private fun removeAllFragments(){
        supportFragmentManager
            .popBackStack(0, 0)
    }

    fun setFragmentRemoveAll(newFragment: Fragment){
        removeAllFragments()
        setFragment(newFragment)
    }
}