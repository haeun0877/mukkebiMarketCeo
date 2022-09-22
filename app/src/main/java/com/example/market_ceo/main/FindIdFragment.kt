package com.example.market_ceo.main

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.market_ceo.MainActivity
import com.example.market_ceo.databinding.FragmentFindIdBinding
import com.example.market_ceo.http.request.RequestFindId
import org.json.JSONObject

class FindIdFragment : Fragment() {
    lateinit var binding : FragmentFindIdBinding

    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindIdBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

        binding.tvSearchId.setOnClickListener { doFindID() }
        binding.tvLogin.setOnClickListener { mainActivity.setMainFragment(LoginFragment.newInstance()) }
        binding.tvSearchPw.setOnClickListener { mainActivity.setFragment(FindPWFragment.newInstance()) }
        binding.ivBack.setOnClickListener { mainActivity.onBackPressed() }

        return binding.root
    }

    private fun doFindID() {
        val strName: String = binding.etName.text.toString().trim { it <= ' ' }
        val strEMail: String = binding.etEmail.text.toString().trim { it <= ' ' }
        if (!TextUtils.isEmpty(strName) && !TextUtils.isEmpty(strEMail)) {
            val request = RequestFindId(activity)
            request.mb_name = strName
            request.mb_email = strEMail
            request.setOnHttpResponseListener { _, response ->
                val strOK = response.optString("result")
                if (strOK == "ok") {
                    val strID = response.optString("result_mb_id")
                    binding.tvResultId.text = strID
                    binding.clCompleteId.visibility = View.VISIBLE
                    binding.tvSearchId.visibility = View.GONE
                    binding.etName.visibility = View.GONE
                    binding.etEmail.visibility = View.GONE
                } else{
                    Toast.makeText(context, response.optString("result_text"), Toast.LENGTH_SHORT).show()
                }
            }.setOnHttpResponseErrorListener { _, error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }.send(context)
        } else {
            Toast.makeText(context, "이름 또는 이메일을 확인하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FindIdFragment().apply {
            }
    }
}