package com.example.market_ceo.main

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.market_ceo.MainActivity
import com.example.market_ceo.databinding.FragmentFindPWBinding
import com.example.market_ceo.http.request.RequestFindPw
import org.json.JSONObject

class FindPWFragment : Fragment() {
    lateinit var binding: FragmentFindPWBinding
    lateinit var mainActivity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFindPWBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

        binding.tvSearchPw.setOnClickListener { doFindPW() }
        binding.tvLogin.setOnClickListener { mainActivity.setMainFragment(LoginFragment.newInstance()) }
        binding.ivBack.setOnClickListener { mainActivity.onBackPressed() }

        return binding.root
    }

    private fun doFindPW() {
        val strID: String = binding.etId.text.toString().trim { it <= ' ' }
        val strName: String = binding.etName.text.toString().trim { it <= ' ' }
        val strEMail: String = binding.etEmail.text.toString().trim { it <= ' ' }
        if (!TextUtils.isEmpty(strID) && !TextUtils.isEmpty(strName) && !TextUtils.isEmpty(strEMail)) {
            val request = RequestFindPw(activity)
            request.mb_id = strID
            request.mb_name = strName
            request.mb_email = strEMail
            request.setOnHttpResponseListener { _, response ->
                val strOK = response.optString("result")
                if (strOK == "ok") {
                    binding.clCompleteId.visibility=View.VISIBLE
                    binding.tvSearchPw.visibility=View.GONE
                    binding.etEmail.visibility=View.GONE
                    binding.etId.visibility=View.GONE
                    binding.etName.visibility=View.GONE
                } else {
                    Toast.makeText(context, response.optString("result_text"), Toast.LENGTH_SHORT).show()
                }
            }.setOnHttpResponseErrorListener { _, error ->
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }.send(context)
        } else {
            Toast.makeText(context, "빈칸을 모두 기입하여 주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FindPWFragment().apply {
            }
    }
}