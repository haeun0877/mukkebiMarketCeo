package com.example.market_ceo.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.example.market_ceo.MainActivity
import com.example.market_ceo.R
import com.example.market_ceo.main.NewOrderFragment

class NewOrderPopupDialog(context : Context) {
    private val dialog = Dialog(context)
    private val context = context

    lateinit var mainActivity:MainActivity

    fun newOrderPopupDialogShow(){

        setDialogSetting()

        dialog.show();
    }

    private fun setDialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_new_order_popup)

        mainActivity = context as MainActivity

        val window = dialog.window
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val tvOk = dialog.findViewById<TextView>(R.id.tv_ok)
        val tvClose = dialog.findViewById<TextView>(R.id.tv_close)
        tvOk.setOnClickListener(mClick)
        tvClose.setOnClickListener(mClick)
    }

    private val mClick = View.OnClickListener {
        when(it.id){
            R.id.tv_ok ->{
                mainActivity.setFragment(NewOrderFragment.newInstance())
                dialog.dismiss()
            }
            R.id.tv_close ->{
                dialog.dismiss()
            }
        }
    }
}