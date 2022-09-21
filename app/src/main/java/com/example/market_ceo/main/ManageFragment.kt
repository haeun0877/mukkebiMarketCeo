package com.example.market_ceo.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.market_ceo.MainActivity
import com.example.market_ceo.databinding.FragmentManageBinding
import com.example.market_ceo.adapter.ProductListAdapter
import com.example.market_ceo.main.item.ProductItem
import com.example.market_ceo.utils.ItemTouchHelperCallback

class ManageFragment : Fragment() {
    lateinit var binding: FragmentManageBinding
    lateinit var adapter: ProductListAdapter

    lateinit var mainActivity: MainActivity

    private val items = mutableListOf<ProductItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

        binding.clProductInput.setOnClickListener {
            mainActivity.setFragment(AddProductFragment.newInstance())
        }

        adapter = ProductListAdapter(requireContext())
        val callback = ItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(binding.rvBestProduct)
        adapter.startDrag(object : ProductListAdapter.OnStartDragListener {
            override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                touchHelper.startDrag(viewHolder)
            }
        })

        setList()

        return binding.root
    }

    private fun setList(){
        items.clear()
        items.apply {
            add(ProductItem("서울식 순대국밥 곱배기(1인분)", "8,000", "", true, false))
            add(ProductItem("병천식 순대국밥(1인분)", "3,000", "", true, false))
            add(ProductItem("오징어 순대(2인분)", "15,000", "", true, false))
            add(ProductItem("미니 순대(1인분)", "5,000", "", true, false))
        }

        adapter.items = items
        adapter.notifyDataSetChanged()
        binding.rvBestProduct.adapter = adapter

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ManageFragment().apply {
            }
    }
}