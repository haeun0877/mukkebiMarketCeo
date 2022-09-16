package com.example.market_ceo.main

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.market_ceo.R
import com.example.market_ceo.databinding.FragmentManageBinding
import com.example.market_ceo.databinding.FragmentNewOrderBinding
import com.example.market_ceo.main.adapter.DialogPriceAdapter
import com.example.market_ceo.main.utils.VerticalItemDecorator

class NewOrderFragment : Fragment() {
    lateinit var binding: FragmentNewOrderBinding

    private val prices = mutableListOf<Int>()
    private var dialogAdapter: DialogPriceAdapter? = null

    private var origin: String = ""
    private var fresh: String = ""
    private var price: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewOrderBinding.inflate(inflater, container, false)

        val gridLM = GridLayoutManager(context,3)
        binding.rvPrice.layoutManager = gridLM

        dialogAdapter = context?.let { DialogPriceAdapter(it, object: DialogPriceAdapter.PriceAdapterListener{
            override fun setPrice(price:String){
                setInputPrice(price)
            }
        })}

        binding.ivBack.setOnClickListener(mClick)

        binding.tvOriginAmerica.setOnClickListener(mClick)
        binding.tvOriginAustralia.setOnClickListener(mClick)
        binding.tvOriginKorea.setOnClickListener(mClick)

        binding.tvFreshBad.setOnClickListener(mClick)
        binding.tvFreshBest.setOnClickListener(mClick)
        binding.tvFreshGood.setOnClickListener(mClick)

        setPrices()

        return binding.root
    }

    private val mClick = View.OnClickListener {
        when(it.id){
            R.id.iv_back ->{
                activity?.onBackPressed()
            }
            R.id.tv_origin_korea,
            R.id.tv_origin_america,
            R.id.tv_origin_australia ->{
                setOriginClickInit()
                setOriginClick(it.id)
                origin = binding.root.findViewById<TextView>(it.id).text.toString()
                checkAllInput()
            }
            R.id.tv_fresh_bad,
            R.id.tv_fresh_good,
            R.id.tv_fresh_best ->{
                setFreshClickInit()
                setFreshClick(it.id)
                fresh = binding.root.findViewById<TextView>(it.id).text.toString()
                checkAllInput()
            }
        }
    }

    private fun checkAllInput(){
        if(price!=""&&origin!=""&&fresh!=""){
            binding.tvPriceRequest.background = context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_10_0537c8) }
        }else{
            binding.tvPriceRequest.background = context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_10_0537c8) }
        }
    }

    fun setInputPrice(inputPrice: String){
        binding.tvGoodsPrice.setText(inputPrice)
        price = inputPrice
        checkAllInput()
    }

    private fun setFreshClick(id:Int){
        when(id){
            R.id.tv_fresh_bad->{
                binding.tvFreshBad.setTextColor(Color.parseColor("#FFFFFF"))
                binding.tvFreshBad.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_0537c8) }
            }
            R.id.tv_fresh_good->{
                binding.tvFreshGood.setTextColor(Color.parseColor("#FFFFFF"))
                binding.tvFreshGood.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_0537c8) }
            }
            R.id.tv_fresh_best->{
                binding.tvFreshBest.setTextColor(Color.parseColor("#FFFFFF"))
                binding.tvFreshBest.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_0537c8) }
            }
        }
    }

    private fun setFreshClickInit(){
        binding.tvFreshBest.setTextColor(Color.parseColor("#cccccc"))
        binding.tvFreshBest.background=
            context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_f7f7f7) }
        binding.tvFreshGood.setTextColor(Color.parseColor("#cccccc"))
        binding.tvFreshGood.background=
            context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_f7f7f7) }
        binding.tvFreshBad.setTextColor(Color.parseColor("#cccccc"))
        binding.tvFreshBad.background=
            context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_f7f7f7) }
    }

    private fun setOriginClick(id:Int){
        when(id){
            R.id.tv_origin_korea->{
                binding.tvOriginKorea.setTextColor(Color.parseColor("#FFFFFF"))
                binding.tvOriginKorea.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_0537c8) }
            }
            R.id.tv_origin_america->{
                binding.tvOriginAmerica.setTextColor(Color.parseColor("#FFFFFF"))
                binding.tvOriginAmerica.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_0537c8) }
            }
            R.id.tv_origin_australia->{
                binding.tvOriginAustralia.setTextColor(Color.parseColor("#FFFFFF"))
                binding.tvOriginAustralia.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_0537c8) }
            }
        }
    }

    private fun setOriginClickInit(){
        binding.tvOriginKorea.setTextColor(Color.parseColor("#cccccc"))
        binding.tvOriginKorea.background=
            context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_f7f7f7) }
        binding.tvOriginAustralia.setTextColor(Color.parseColor("#cccccc"))
        binding.tvOriginAustralia.background=
            context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_f7f7f7) }
        binding.tvOriginAmerica.setTextColor(Color.parseColor("#cccccc"))
        binding.tvOriginAmerica.background=
            context?.let { ContextCompat.getDrawable(it, R.drawable.box_round_24_f7f7f7) }
    }

    private fun setPrices(){
        var startPrice = 15000

        for(i: Int in 1..6){
            prices?.apply {
                add(startPrice)
            }
            startPrice+=2000
        }

        dialogAdapter?.items = prices
        dialogAdapter?.notifyDataSetChanged()
        binding.rvPrice.adapter = dialogAdapter
        binding.rvPrice.addItemDecoration(VerticalItemDecorator(8))

    }

    companion object {
        fun newInstance() =
            NewOrderFragment().apply {
            }
    }
}