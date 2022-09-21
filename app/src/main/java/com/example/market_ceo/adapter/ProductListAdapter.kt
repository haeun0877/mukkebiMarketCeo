package com.example.market_ceo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.market_ceo.R
import com.example.market_ceo.dialog.SoldOutDialog
import com.example.market_ceo.main.item.ProductItem
import com.example.market_ceo.utils.ItemTouchHelperCallback
import java.util.*

class ProductListAdapter (private val context: Context) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>(), ItemTouchHelperCallback.OnItemMoveListener {

    var items = mutableListOf<ProductItem>()

    private lateinit var dragListener: OnStartDragListener

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tvName = view.findViewById<TextView>(R.id.tv_product_name)
        private val tvPrice = view.findViewById<TextView>(R.id.tv_price)
        private val ivMove = view.findViewById<ImageView>(R.id.iv_move)
        private val switchSoldOut = view.findViewById<Switch>(R.id.switch_sold_out)
        private val switchShow = view.findViewById<Switch>(R.id.switch_show)

        @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
        fun bind(item: ProductItem){
            tvName.text = item.name
            tvPrice.text = "기준가격: "+item.price+"원"

            switchShow.isChecked = item.show
            switchSoldOut.isChecked = item.sold_out

            ivMove.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    dragListener.onStartDrag(this)
                }
                return@setOnTouchListener false
            }

            switchSoldOut.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    if(switchShow.isChecked){
                        SoldOutDialog(context).soldOutDialogShow(this@ProductListAdapter, item, position)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductListAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.product_list_row,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductListAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface OnStartDragListener {
        fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
    }

    fun startDrag(listener: OnStartDragListener) {
        this.dragListener = listener
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemSwiped(position: Int) {
        TODO("Not yet implemented")
    }

    fun setShowOnOff(position: Int){
        items[position].show = false
        notifyItemChanged(position)
    }

}