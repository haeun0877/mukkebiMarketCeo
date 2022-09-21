package com.example.market_ceo.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.market_ceo.MainActivity
import com.example.market_ceo.R
import com.example.market_ceo.constant.Constant
import com.example.market_ceo.data.ShareData
import com.example.market_ceo.databinding.FragmentMainBinding
import com.example.market_ceo.member.MemberManager

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

        binding.mainSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.clTop.setBackgroundColor(Color.parseColor("#0537c8"))
                binding.tvTitle.visibility=View.VISIBLE
                binding.tvTitleOff.visibility=View.INVISIBLE
            }else{
                binding.clTop.setBackgroundColor(Color.parseColor("#363940"))
                binding.tvTitle.visibility=View.INVISIBLE
                binding.tvTitleOff.visibility=View.VISIBLE
            }
        }

        return binding.root
    }

    private val mClick = View.OnClickListener {
        when(it.id){
            R.id.tv_all_order_list -> {
                mainActivity.setFragment(OrderListFragment.newInstance())
            }
            R.id.tv_logout -> {
                val strUUID: String = ShareData.shared().getPfData(Constant.PREF_UUID)
                MemberManager.getInstance().doUuidLogout(context, strUUID, true, null)


//                context?.let { it1 -> NewOrderPopupDialog(it1).newOrderPopupDialogShow() }
            }
            R.id.tv_goods_manage -> {
                Toast.makeText(context, "준비중입니다.", Toast.LENGTH_SHORT).show()
//                mainActivity.setFragment(ManageFragment.newInstance())
            }
        }
    }
}