package com.example.market_ceo.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.market_ceo.MainActivity
import com.example.market_ceo.R
import com.example.market_ceo.databinding.FragmentMainBinding
import com.example.market_ceo.main.dialog.NewOrderDialog

class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    lateinit var mainActivity:MainActivity

    companion object {
        @JvmStatic
        fun newInstance() =
            MainFragment().apply {

            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        mainActivity = context as MainActivity

        binding.tvAllOrderList.setOnClickListener(mClick)
        binding.tvLogout.setOnClickListener(mClick)
        binding.tvGoodsManage.setOnClickListener(mClick)

        return binding.root
    }

    private val mClick = View.OnClickListener {
        when(it.id){
            R.id.tv_all_order_list -> {
                mainActivity.setFragment(OrderListFragment.newInstance())
            }
            R.id.tv_logout -> {
                context?.let { it1 -> NewOrderDialog(it1).newOrderDialogShow() }
            }
            R.id.tv_goods_manage -> {
                mainActivity.setFragment(ManageFragment.newInstance())
            }
        }
    }
}