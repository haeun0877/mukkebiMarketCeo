package com.example.market_ceo.main.order_list_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.market_ceo.R
import com.example.market_ceo.databinding.FragmentCancelOrderListBinding
import com.example.market_ceo.main.adapter.OrderListAdapter
import com.example.market_ceo.main.item.OrderItem
import com.example.market_ceo.main.utils.VerticalItemDecorator

class CancelOrderListFragment : Fragment() {
    lateinit var binding: FragmentCancelOrderListBinding

    lateinit var orderListAdapter: OrderListAdapter
    private val items = mutableListOf<OrderItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCancelOrderListBinding.inflate(inflater, container, false)

        orderListAdapter = OrderListAdapter(requireContext())

        setOrderList()

        return binding.root
    }

    private fun setOrderList(){
        items.clear()
        items.apply {
            add(OrderItem("제안/취소", "", "평양식 순대국밥(1인분)", "18,000원", "050-1234-5555"))
            add(OrderItem("제안/취소", "", "서울식 순대국밥(1인분)", "8,000원", "050-1234-5678"))

            orderListAdapter.items = items
            orderListAdapter.notifyDataSetChanged()
            binding.rvOrderList.adapter = orderListAdapter
            binding.rvOrderList.addItemDecoration(VerticalItemDecorator(20))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            CancelOrderListFragment().apply {
            }
    }
}