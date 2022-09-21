package com.example.market_ceo.main.order_list_page

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.market_ceo.databinding.FragmentAllOrderListBinding
import com.example.market_ceo.adapter.OrderListAdapter
import com.example.market_ceo.main.item.OrderItem
import com.example.market_ceo.utils.VerticalItemDecorator

class AllOrderListFragment : Fragment() {
    lateinit var binding: FragmentAllOrderListBinding

    lateinit var orderListAdapter: OrderListAdapter
    private val items = mutableListOf<OrderItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllOrderListBinding.inflate(inflater, container, false)

        orderListAdapter = OrderListAdapter(requireContext())
        binding.rvOrderList.isNestedScrollingEnabled = false

        setOrderList()

        return binding.root
    }

    private fun setOrderList(){
        items.clear()
        items.apply {
            add(OrderItem("제안/취소", "", "서울식 순대국밥 곱배기(1인분)", "10,000원", "050-1234-5678"))
            add(OrderItem("판매완료", "", "서울식 순대국밥(1인분)", "8,000원", "050-1234-5678"))
            add(OrderItem("판매완료", "", "대구식 순대국밥(3인분)", "1,000원", "050-1111-5678"))
            add(OrderItem("제안/취소", "", "평양식 순대국밥(1인분)", "18,000원", "050-1234-5555"))
            add(OrderItem("판매완료", "", "서울식 순대국밥(2인분)", "16,000원", "050-1234-5678"))

            orderListAdapter.items = items
            orderListAdapter.notifyDataSetChanged()
            binding.rvOrderList.adapter = orderListAdapter
            binding.rvOrderList.addItemDecoration(VerticalItemDecorator(20))
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AllOrderListFragment().apply {
            }
    }
}