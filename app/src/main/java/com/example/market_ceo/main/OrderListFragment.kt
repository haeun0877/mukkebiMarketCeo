package com.example.market_ceo.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.market_ceo.R
import com.example.market_ceo.databinding.FragmentOrderListBinding
import com.example.market_ceo.main.adapter.OrderListPagerAdapter
import com.example.market_ceo.main.order_list_page.AllOrderListFragment
import com.example.market_ceo.main.order_list_page.CancelOrderListFragment
import com.example.market_ceo.main.order_list_page.SuccessOrderListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrderListFragment : Fragment() {
    lateinit var binding: FragmentOrderListBinding
//    lateinit var pagerAdapter: OrderListPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderListBinding.inflate(inflater, container, false)

        var pagerAdapter = activity?.supportFragmentManager?.let { OrderListPagerAdapter(it, lifecycle) }
        binding.viewPager.adapter = pagerAdapter
        binding.tabLayout.setTabTextColors(Color.parseColor("#8c93a8"), Color.parseColor("#262630"))

        TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager,
            true,
            true,
            pagerAdapter!!
        ).attach()

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            OrderListFragment().apply {
            }
    }
}