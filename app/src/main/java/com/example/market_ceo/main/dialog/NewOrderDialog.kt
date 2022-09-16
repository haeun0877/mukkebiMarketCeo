package com.example.market_ceo.main.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.market_ceo.R
import com.example.market_ceo.main.ManageFragment
import com.example.market_ceo.main.OrderListFragment
import com.example.market_ceo.main.adapter.DialogPriceAdapter
import com.example.market_ceo.main.item.OrderItem
import com.example.market_ceo.main.utils.VerticalItemDecorator

class NewOrderDialog(context : Context) {
    private val dialog = Dialog(context)
    private val context = context

    private lateinit var rvPrice: RecyclerView
    private val prices = mutableListOf<Int>()
    private var dialogAdapter:DialogPriceAdapter? = null

    private var ivClose: ImageView? = null

    private var origin: String = ""
    private var fresh: String = ""
    private var price: String = ""

    fun newOrderDialogShow(){

        setDialogSetting()

        dialog.show();
    }

    private fun setDialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_new_order)

        val window = dialog.window
        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        ivClose = dialog.findViewById(R.id.iv_close)
        ivClose?.setOnClickListener(mClick)

        rvPrice = dialog.findViewById(R.id.rv_price)

        val gridLM = GridLayoutManager(context,3)
        rvPrice.layoutManager = gridLM

        dialogAdapter = context?.let { DialogPriceAdapter(it, object: DialogPriceAdapter.PriceAdapterListener{
            override fun setPrice(price:String){
                setInputPrice(price)
            }
        })}

        dialog.findViewById<TextView>(R.id.tv_origin_korea).setOnClickListener(mClick)
        dialog.findViewById<TextView>(R.id.tv_origin_america).setOnClickListener(mClick)
        dialog.findViewById<TextView>(R.id.tv_origin_australia).setOnClickListener(mClick)

        dialog.findViewById<TextView>(R.id.tv_fresh_bad).setOnClickListener(mClick)
        dialog.findViewById<TextView>(R.id.tv_fresh_best).setOnClickListener(mClick)
        dialog.findViewById<TextView>(R.id.tv_fresh_good).setOnClickListener(mClick)

        setPrices()
    }

    private val mClick = View.OnClickListener {
        when(it.id){
            R.id.iv_close ->{
                dialog.dismiss()
            }
            R.id.tv_origin_korea,
            R.id.tv_origin_america,
            R.id.tv_origin_australia ->{
                setOriginClickInit()
                setOriginClick(it.id)
                origin = dialog.findViewById<TextView>(it.id).text.toString()
                checkAllInput()
            }
            R.id.tv_fresh_bad,
            R.id.tv_fresh_good,
            R.id.tv_fresh_best ->{
                setFreshClickInit()
                setFreshClick(it.id)
                fresh = dialog.findViewById<TextView>(it.id).text.toString()
                checkAllInput()
            }
        }
    }

    private fun checkAllInput(){
        if(price!=""&&origin!=""&&fresh!=""){
            dialog.findViewById<TextView>(R.id.tv_price_request).background =
                ContextCompat.getDrawable(context, R.drawable.box_round_10_0537c8)
        }else{
            dialog.findViewById<TextView>(R.id.tv_price_request).background =
                ContextCompat.getDrawable(context, R.drawable.box_round_10_d8d8d8)
        }
    }

    fun setInputPrice(inputPrice: String){
        dialog.findViewById<TextView>(R.id.tv_goods_price).setText(inputPrice)
        price = inputPrice
        checkAllInput()
    }

    private fun setFreshClick(id:Int){
        when(id){
            R.id.tv_fresh_bad->{
                dialog.findViewById<TextView>(R.id.tv_fresh_bad).setTextColor(Color.parseColor("#FFFFFF"))
                dialog.findViewById<TextView>(R.id.tv_fresh_bad).background=
                    ContextCompat.getDrawable(context, R.drawable.box_round_24_0537c8)
            }
            R.id.tv_fresh_good->{
                dialog.findViewById<TextView>(R.id.tv_fresh_good).setTextColor(Color.parseColor("#FFFFFF"))
                dialog.findViewById<TextView>(R.id.tv_fresh_good).background=
                    ContextCompat.getDrawable(context, R.drawable.box_round_24_0537c8)
            }
            R.id.tv_fresh_best->{
                dialog.findViewById<TextView>(R.id.tv_fresh_best).setTextColor(Color.parseColor("#FFFFFF"))
                dialog.findViewById<TextView>(R.id.tv_fresh_best).background=
                    ContextCompat.getDrawable(context, R.drawable.box_round_24_0537c8)
            }
        }
    }

    private fun setFreshClickInit(){
        dialog.findViewById<TextView>(R.id.tv_fresh_best).setTextColor(Color.parseColor("#0537c8"))
        dialog.findViewById<TextView>(R.id.tv_fresh_best).background=
            ContextCompat.getDrawable(context, R.drawable.box_round_24_f7f8fe)
        dialog.findViewById<TextView>(R.id.tv_fresh_good).setTextColor(Color.parseColor("#0537c8"))
        dialog.findViewById<TextView>(R.id.tv_fresh_good).background=
            ContextCompat.getDrawable(context, R.drawable.box_round_24_f7f8fe)
        dialog.findViewById<TextView>(R.id.tv_fresh_bad).setTextColor(Color.parseColor("#0537c8"))
        dialog.findViewById<TextView>(R.id.tv_fresh_bad).background=
            ContextCompat.getDrawable(context, R.drawable.box_round_24_f7f8fe)
    }

    private fun setOriginClick(id:Int){
        when(id){
            R.id.tv_origin_korea->{
                dialog.findViewById<TextView>(R.id.tv_origin_korea).setTextColor(Color.parseColor("#FFFFFF"))
                dialog.findViewById<TextView>(R.id.tv_origin_korea).background=
                    ContextCompat.getDrawable(context, R.drawable.box_round_24_0537c8)
            }
            R.id.tv_origin_america->{
                dialog.findViewById<TextView>(R.id.tv_origin_america).setTextColor(Color.parseColor("#FFFFFF"))
                dialog.findViewById<TextView>(R.id.tv_origin_america).background=
                    ContextCompat.getDrawable(context, R.drawable.box_round_24_0537c8)
            }
            R.id.tv_origin_australia->{
                dialog.findViewById<TextView>(R.id.tv_origin_australia).setTextColor(Color.parseColor("#FFFFFF"))
                dialog.findViewById<TextView>(R.id.tv_origin_australia).background=
                    ContextCompat.getDrawable(context, R.drawable.box_round_24_0537c8)
            }
        }
    }

    private fun setOriginClickInit(){
        dialog.findViewById<TextView>(R.id.tv_origin_korea).setTextColor(Color.parseColor("#0537c8"))
        dialog.findViewById<TextView>(R.id.tv_origin_korea).background=
            ContextCompat.getDrawable(context, R.drawable.box_round_24_f7f8fe)
        dialog.findViewById<TextView>(R.id.tv_origin_australia).setTextColor(Color.parseColor("#0537c8"))
        dialog.findViewById<TextView>(R.id.tv_origin_australia).background=
            ContextCompat.getDrawable(context, R.drawable.box_round_24_f7f8fe)
        dialog.findViewById<TextView>(R.id.tv_origin_america).setTextColor(Color.parseColor("#0537c8"))
        dialog.findViewById<TextView>(R.id.tv_origin_america).background=
            ContextCompat.getDrawable(context, R.drawable.box_round_24_f7f8fe)
    }

    private fun setPrices(){
        var startPrice = 15000

        for(i: Int in 1..6){
            prices?.apply {
                add(startPrice)
            }
            startPrice+=2000
        }

        dialogAdapter?.items = prices
        dialogAdapter?.notifyDataSetChanged()
        rvPrice.adapter = dialogAdapter
        rvPrice.addItemDecoration(VerticalItemDecorator(8))

    }
}