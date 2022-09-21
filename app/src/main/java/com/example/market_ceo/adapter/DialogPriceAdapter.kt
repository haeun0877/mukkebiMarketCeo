package com.example.market_ceo.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.market_ceo.R
import com.example.market_ceo.utils.Utils

class DialogPriceAdapter (private val context: Context, priceAdapterListener: PriceAdapterListener) : RecyclerView.Adapter<DialogPriceAdapter.ViewHolder>() {

    var items = mutableListOf<Int>()

    var selectedPrice: String = ""

    var selectPos = -1

    val listener = priceAdapterListener

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tvPrice: TextView = view.findViewById<TextView>(R.id.tv_price)
        val clPrice: ConstraintLayout = view.findViewById<ConstraintLayout>(R.id.cl_price)

        fun bind(item: Int, position: Int){
            tvPrice.text = Utils.makePriceComma(item.toString())

            clPrice.background=ContextCompat.getDrawable(context, R.drawable.box_round_10_f7f7f7)
            tvPrice.setTextColor(Color.parseColor("#cccccc"))

            if(selectPos == position) {
                selectedPrice = item.toString()
                if(listener!=null){
                    listener.setPrice(selectedPrice)
                }

                clPrice.background=ContextCompat.getDrawable(context, R.drawable.box_round_10_0537c8)
                tvPrice.setTextColor(Color.parseColor("#FFFFFF"))
            } else {
                clPrice.background=ContextCompat.getDrawable(context, R.drawable.box_round_10_f7f7f7)
                tvPrice.setTextColor(Color.parseColor("#cccccc"))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogPriceAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_price_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DialogPriceAdapter.ViewHolder, position: Int) {
        holder.bind(items[position], position)

        holder.clPrice.setOnClickListener{
            var beforePos = selectPos
            selectPos = position

            notifyItemChanged(beforePos)
            notifyItemChanged(selectPos)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface PriceAdapterListener{
        fun setPrice(price:String)
    }

}