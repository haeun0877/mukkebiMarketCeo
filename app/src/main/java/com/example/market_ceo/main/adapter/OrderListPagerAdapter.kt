package com.example.market_ceo.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.market_ceo.main.order_list_page.AllOrderListFragment
import com.example.market_ceo.main.order_list_page.CancelOrderListFragment
import com.example.market_ceo.main.order_list_page.SuccessOrderListFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrderListPagerAdapter : FragmentStateAdapter, TabLayoutMediator.TabConfigurationStrategy {
    private val fragmentLaunchers: List<() -> Fragment> = listOf(
        { AllOrderListFragment() },
        { SuccessOrderListFragment() },
        { CancelOrderListFragment() }
    )
    private val titles = listOf(
        "전체",
        "판매완료",
        "제안/취소"
    )

    constructor(fragmentActivity: FragmentActivity) : super(fragmentActivity)
    constructor(fragment: Fragment) : super(fragment)
    constructor(fragmentManager: FragmentManager, lifecycle: Lifecycle) : super(
        fragmentManager,
        lifecycle
    )

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        tab.text = titles[position]
    }

    override fun getItemCount() = fragmentLaunchers.size

    override fun createFragment(position: Int): Fragment {
        return fragmentLaunchers[position].invoke()
    }

}
