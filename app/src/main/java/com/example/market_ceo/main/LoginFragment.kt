package com.example.market_ceo.main

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.market_ceo.MainActivity
import com.example.market_ceo.databinding.FragmentLoginBinding
import com.example.market_ceo.http.request.RequestHPVerify
import com.example.market_ceo.http.request.RequestHPVerifySendSms
import com.example.market_ceo.member.MemberManager
import com.google.android.material.tabs.TabLayout

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    lateinit var mainActivity:MainActivity

    private var strID:String = ""
    private var strPW:String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity
        tabInit()
        listenerInit()

        return binding.root
    }

    private fun tabInit(){
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("휴대폰 로그인"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("아이디 로그인"))

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab!!.position){
                    0 -> {
                        binding.clLoginPhone.visibility = View.VISIBLE
                        binding.clLoginId.visibility = View.GONE
                    }
                    1 -> {
                        binding.clLoginPhone.visibility = View.GONE
                        binding.clLoginId.visibility = View.VISIBLE
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.tvPhoneLogin.setOnClickListener {
            mainActivity.setFragmentRemoveAll(MainFragment.newInstance())
        }
        binding.tvIdLogin.setOnClickListener {
            mainActivity.setFragmentRemoveAll(MainFragment.newInstance())
        }
    }

    private fun listenerInit(){
        binding.tvSubmit.setOnClickListener { doVerifySendSms()}
        binding.tvPhoneLogin.setOnClickListener { doVerify() }
        binding.tvIdLogin.setOnClickListener { doIdLogin() }
    }

    private fun doIdLogin(){
        if (isAvail()) {
            MemberManager.getInstance()
                .doLogin(activity, "1", strID, strPW, null) //일단 무조건 자동로그인되도록
        } else {
            Toast.makeText(context, "아이디 혹은 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isAvail(): Boolean {
        var isAvail = false
        strID = binding.etInputId.text.toString().trim { it <= ' ' }
        strPW = binding.etInputPass.text.toString().trim { it <= ' ' }
        //        m_strAuto = m_ckAuto.isChecked() ? "1" : "0";
        if (!TextUtils.isEmpty(strID) && !TextUtils.isEmpty(strPW)) {
            isAvail = true
        }
        return isAvail
    }

    //로그인 인증번호 발송
    private fun doVerifySendSms() {
        try {
            binding.etInputPhone.visibility = View.VISIBLE
            val phoneNumber: String = binding.etPhone.text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(phoneNumber)) {
                val request = RequestHPVerifySendSms(activity)
                request.mb_hp = phoneNumber
                request.type = "1" //1인경우: 등록된 핸드폰번호를 인증하기 위한 인증번호 발송
                request.setOnHttpResponseListener { _, response ->
                    val strOK = response.optString("result")
                    if (strOK == "ok") {
                        binding.tvPhoneLogin.visibility = View.VISIBLE
                        Toast.makeText(context, "인증번호가 발송되었습니다.", Toast.LENGTH_SHORT).show()
                    } else {
                        val strMessage = response.optString("result_text")
                        Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show()
                    }
                }.setOnHttpResponseErrorListener { _, error ->
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }.send(activity)
            } else {
                Toast.makeText(context, "휴대폰번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "네트워크연결이 원활하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    //인증번호 확인
    private fun doVerify() {
        try {
            val phoneNumber: String =
                binding.etPhone.text.toString().trim { it <= ' ' } //휴대폰번호
            val verifyNum: String =
                binding.etInputPhone.text.toString().trim { it <= ' ' } //인증번호
            if (!TextUtils.isEmpty(phoneNumber) && !TextUtils.isEmpty(verifyNum)) {
                val request = RequestHPVerify(activity)
                request.mb_hp = phoneNumber
                request.mb_auth_no = verifyNum
                request.setOnHttpResponseListener { _, response ->
                    val strOK = response.optString("result")
                    if (strOK == "ok") {
                        MemberManager.getInstance()
                            .doLoginPhone(activity, "1", phoneNumber, null) //일단 무조건 자동로그인되도록

//                            Utils.showToast("본인인증이 완료되었습니다.");
                    } else {
                        val strMessage = response.optString("result_text")
                        Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show()
                    }
                }.setOnHttpResponseErrorListener { request, error ->
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }.send(context)

            } else {
                Toast.makeText(context,"인증번호를 확인해 주세요.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
            }
    }
}