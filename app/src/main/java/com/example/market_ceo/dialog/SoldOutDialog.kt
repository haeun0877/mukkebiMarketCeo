package com.example.market_ceo.dialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import com.example.market_ceo.R
import com.example.market_ceo.adapter.ProductListAdapter
import com.example.market_ceo.main.item.ProductItem

class SoldOutDialog(context : Context) {
    private val dialog = Dialog(context)
    private val context = context

    lateinit var productListAdapter: ProductListAdapter
    lateinit var productItem: ProductItem
    private var position = 0

    fun soldOutDialogShow(adapter: ProductListAdapter, item: ProductItem, pos: Int){
        productListAdapter = adapter
        productItem = item
        position = pos

        setDialogSetting()

        dialog.show();
    }

    private fun setDialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_sold_out)

        val window = dialog.window
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val tvShowKeep = dialog.findViewById<TextView>(R.id.tv_show_keep)
        val tvShowOnOff = dialog.findViewById<TextView>(R.id.tv_show_on_off)
        tvShowKeep.setOnClickListener (mClick)
        tvShowOnOff.setOnClickListener(mClick)
    }

    private val mClick = View.OnClickListener {
        when(it.id){
            R.id.tv_show_keep -> {
                dialog.dismiss()
            }
            R.id.tv_show_on_off -> {
                productListAdapter.setShowOnOff(position)
                dialog.dismiss()
            }
        }
    }
}