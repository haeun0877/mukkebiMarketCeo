package com.example.market_ceo.main

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.market_ceo.databinding.FragmentLeaveBinding
import com.example.market_ceo.http.request.RequestCheckPw
import com.example.market_ceo.http.request.RequestLeave
import com.example.market_ceo.member.MemberManager
import org.json.JSONObject

class LeaveFragment : Fragment() {
    lateinit var binding: FragmentLeaveBinding
    lateinit var imm: InputMethodManager

    var inputPw: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeaveBinding.inflate(inflater, container, false)

        imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        binding.tvCheckPw.setOnClickListener { checkPassword(binding.etPass.text.toString()) }
        binding.ivBack.setOnClickListener { activity?.onBackPressed() }
        binding.tvLeave.setOnClickListener { doLeave() }

        return binding.root
    }

    private fun checkPassword(password: String) {
        val request = RequestCheckPw(activity)
        request.mb_pass = password
        request.setOnHttpResponseListener { _, response ->
            val result = response.optString("result")
            val resultText = response.optString("result_text")
            if (TextUtils.equals(result, "ok")) {
                binding.clInputLeave.visibility=View.VISIBLE
                binding.etId.visibility=View.GONE
                binding.etPass.visibility=View.GONE
                binding.tvCheckPw.visibility=View.GONE
                binding.tvSubTitle.text="탈퇴 사유를 작성해 주세요"

                imm.hideSoftInputFromWindow(view?.windowToken, 0)
                inputPw = password
            } else {
                Toast.makeText(context, resultText, Toast.LENGTH_SHORT).show()
            }
        }.setOnHttpResponseErrorListener { _, error ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }.send(context)
    }

    fun doLeave() {
        val request = RequestLeave(activity)
        request.mb_out_reason = binding.etReasonLeave.getText().toString().trim { it <= ' ' }
        request.mb_pass = inputPw
        request.setOnHttpResponseListener { _, response ->
            val strOK = response.optString("result")
            if (strOK == "ok") {
                Toast.makeText(context,"이용해주셔서 감사합니다.",Toast.LENGTH_SHORT).show()
                MemberManager.getInstance().doLogout(context, true, null)
                //MainActivity.setFragmentClear(MainFragment.newInstance());
            } else {
                val strMessage = response.optString("result_text")
                Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show()
            }
        }.setOnHttpResponseErrorListener { _, error ->
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
        }.send(activity)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LeaveFragment().apply {

            }
    }
}