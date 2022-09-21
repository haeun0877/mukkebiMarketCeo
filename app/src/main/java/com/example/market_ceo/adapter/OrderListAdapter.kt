package com.example.market_ceo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.market_ceo.R
import com.example.market_ceo.main.item.OrderItem

class OrderListAdapter(private val context:Context) : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    var items = mutableListOf<OrderItem>()

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tvOrderStatus = view.findViewById<TextView>(R.id.tv_order_status)
        private val ivImage = view.findViewById<ImageView>(R.id.iv_image)
        private val tvTitle = view.findViewById<TextView>(R.id.tv_title)
        private val tvPrice = view.findViewById<TextView>(R.id.tv_price)
        private val tvPhone = view.findViewById<TextView>(R.id.tv_phone)

        fun bind(item: OrderItem){
            tvOrderStatus.text = item.status
            tvTitle.text = item.title
            tvPrice.text = item.price
            tvPhone.text = item.phone

            if(item.status == "판매완료") {

            }else{

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.order_list_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderListAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

}