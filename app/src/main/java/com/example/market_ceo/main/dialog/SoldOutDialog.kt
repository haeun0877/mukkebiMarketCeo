package com.example.market_ceo.main.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.market_ceo.R
import com.example.market_ceo.main.adapter.DialogPriceAdapter

class SoldOutDialog(context : Context) {
    private val dialog = Dialog(context)
    private val context = context

    fun soldOutDialogShow(){
        setDialogSetting()

        dialog.show();
    }

    private fun setDialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_sold_out)

        val window = dialog.window
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }
}