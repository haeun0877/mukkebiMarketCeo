package com.example.market_ceo.main.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.GridLayoutManager
import com.example.market_ceo.R
import com.example.market_ceo.main.adapter.DialogPriceAdapter
import org.json.JSONObject
import java.io.DataInput
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class DateSelectDialog(context : Context) {
    private val dialog = Dialog(context)
    private val context = context

    lateinit var radioWeekData: ArrayList<DateItem>
    lateinit var radioMonthData: ArrayList<DateItem>
    lateinit var radioBranchData: ArrayList<DateItem>

    lateinit var radioButtons: ArrayList<RadioButton>

    lateinit var tabDate: TextView
    lateinit var tabBranching: TextView
    lateinit var tabMonth: TextView
    lateinit var tabWeek: TextView

    lateinit var root:LinearLayout

    fun dateSelectDialog(){

        setDialogSetting()

        dialog.show();
    }

    private fun setDialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_data_select)

        val window = dialog.window
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        dialog.findViewById<ImageView>(R.id.iv_close).setOnClickListener{
            dialog.dismiss()
        }

        dialog.findViewById<TextView>(R.id.tv_ok).setOnClickListener {

        }

        root = dialog.findViewById(R.id.ll_radio_list)

        tabDate = dialog.findViewById<TextView>(R.id.tab_date)
        tabDate.setOnClickListener(tabClickListener)
        tabBranching = dialog.findViewById<TextView>(R.id.tab_branching)
        tabBranching.setOnClickListener(tabClickListener)
        tabMonth = dialog.findViewById<TextView>(R.id.tab_month)
        tabMonth.setOnClickListener(tabClickListener)
        tabWeek = dialog.findViewById<TextView>(R.id.tab_week)
        tabWeek.setOnClickListener(tabClickListener)

        setRadioList()
    }

    private fun setRadioView(radioType: ArrayList<DateItem>){
        root.removeAllViews()

        radioButtons = ArrayList<RadioButton>()

        var thisRadioType = radioType

        for(i: Int in 0 until thisRadioType.size){
            var section: View = LayoutInflater.from(context).inflate(R.layout.radio_list_row, root, false)

            var radio: RadioButton = section.findViewById(R.id.radio)
            var radioTitle: TextView = section.findViewById(R.id.tv_radio_title)
            var radioSubTitle: TextView = section.findViewById(R.id.tv_radio_sub_title)

            radioButtons.add(radio)
            if(i==0){
                radioButtons[i].isChecked = true
            }

            section.setOnClickListener {
                setInitialRadio()
                radioButtons[i].isChecked = true
            }
            radio.setOnClickListener {
                setInitialRadio()
                radioButtons[i].isChecked = true
            }

            radioTitle.text = thisRadioType[i].title
            radioSubTitle.text =  thisRadioType[i].subTitle

            root.addView(section)
        }
    }

    private fun setRadioList(){
        // TAB: 일/주
        radioWeekData = ArrayList<DateItem>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //오늘
            var now = LocalDate.now()
            var strNow = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (E)"))

            //어제
            var yesterday = now.minusDays(1)
            var strYesterday = yesterday.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (E)"))

            //이번 주
            var strThisWeek = ""

            //지난 주
            var strLastWeek = ""

            radioWeekData.add(DateItem("오늘", strNow))
            radioWeekData.add(DateItem("어제", strYesterday))
            radioWeekData.add(DateItem("이번 주", strThisWeek))
            radioWeekData.add(DateItem("지난 주", strLastWeek))
        }


        // TAB: 월
        radioMonthData = ArrayList<DateItem>()


        // TAB: 분기
        radioBranchData = ArrayList<DateItem>()

        setRadioView(radioWeekData)
    }

    private fun setInitialRadio(){
        for(i: Int in 0 until radioButtons.size){
            radioButtons[i].isChecked = false
        }
    }

    private fun setInitialTab(){
        tabDate.setBackgroundResource(R.drawable.box_white_gray)
        tabDate.setTextColor(Color.parseColor("#8c93a8"))
        tabBranching.setBackgroundResource(R.drawable.box_white_gray)
        tabBranching.setTextColor(Color.parseColor("#8c93a8"))
        tabMonth.setBackgroundResource(R.drawable.box_white_gray)
        tabMonth.setTextColor(Color.parseColor("#8c93a8"))
        tabWeek.setBackgroundResource(R.drawable.box_white_gray)
        tabWeek.setTextColor(Color.parseColor("#8c93a8"))
    }

    private val tabClickListener = View.OnClickListener {
        setInitialTab()

        when(it.id){
            R.id.tab_week->{
                tabWeek.setBackgroundResource(R.drawable.box_c0caeb_gray)
                tabWeek.setTextColor(Color.parseColor("#0537c8"))
                setRadioView(radioWeekData)
            }
            R.id.tab_month->{
                tabMonth.setBackgroundResource(R.drawable.box_c0caeb_gray)
                tabMonth.setTextColor(Color.parseColor("#0537c8"))
                setRadioView(radioMonthData)
            }
            R.id.tab_branching->{
                tabBranching.setBackgroundResource(R.drawable.box_c0caeb_gray)
                tabBranching.setTextColor(Color.parseColor("#0537c8"))
                setRadioView(radioBranchData)
            }
            R.id.tab_date->{
                tabDate.setBackgroundResource(R.drawable.box_c0caeb_gray)
                tabDate.setTextColor(Color.parseColor("#0537c8"))
            }
        }
    }

    data class DateItem(
        val title:String,
        val subTitle: String
    )
}