package com.example.market_ceo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.LayoutRes
import com.example.market_ceo.databinding.ItemSpinnerBinding

class YearSpinnerAdapter(
    context: Context,
    @LayoutRes private val resId: Int,
    private val values: ArrayList<Int>
) : ArrayAdapter<Int>(context, resId, values) {

    override fun getCount() = values.size

    override fun getItem(position: Int) = values[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = values[position]
        try {
            binding.txtName.text = model.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemSpinnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val model = values[position]
        try {
            binding.txtName.text = model.toString()
            binding.imgSpinner.visibility = View.GONE
            binding.clRoot.setBackgroundColor(Color.parseColor("#FFFFFF"))

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return binding.root
    }



}