package com.example.market_ceo.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.market_ceo.R
import com.example.market_ceo.adapter.YearSpinnerAdapter
import java.text.SimpleDateFormat
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

    lateinit var tvStartDate: TextView
    lateinit var tvEndDate: TextView

    lateinit var root:LinearLayout

    lateinit var years: ArrayList<Int>
    private var year: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    fun dateSelectDialog(){

        setDialogSetting()

        dialog.show();
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
            dialog.dismiss()
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
        tvStartDate = dialog.findViewById(R.id.tv_start_date)
        tvEndDate = dialog.findViewById(R.id.tv_end_date)

        setRadioList()
        setSpinnerYear()
        setDateInitial()
    }

    private fun setSpinnerYear(){
        val spinner:Spinner = dialog.findViewById<Spinner>(R.id.spinner_year)
        years = ArrayList<Int>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var now = LocalDate.now()
            for(year:Int in now.year downTo 2020){
                years.add(year)
            }
        }

        var spinnerAdapterYear = YearSpinnerAdapter(context, R.layout.support_simple_spinner_dropdown_item, years)
        spinner.adapter = spinnerAdapterYear

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                year = years[p2]

                setYears()
                setRadioView(radioBranchData)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
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

    @SuppressLint("SimpleDateFormat")
    private fun setRadioList(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // TAB: 일/주
            radioWeekData = ArrayList<DateItem>()
            //오늘
            var now = LocalDate.now()
            year = now.year
            var strNow = now.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (E)"))

            //어제
            var yesterday = now.minusDays(1)
            var strYesterday = yesterday.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (E)"))

            var startWeek = ""
            var endWeek = ""

            //이번 주
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            startWeek = getWeekDay(calendar,1)
            endWeek = getWeekDay(calendar,7)
            var strThisWeek = "$startWeek ~ $endWeek"

            //지난 주
            calendar.add(Calendar.WEEK_OF_MONTH, -1)
            startWeek = getWeekDay(calendar,1)
            endWeek = getWeekDay(calendar,7)
            var strLastWeek = "$startWeek ~ $endWeek"

            radioWeekData.add(DateItem("오늘", strNow))
            radioWeekData.add(DateItem("어제", strYesterday))
            radioWeekData.add(DateItem("이번 주", strThisWeek))
            radioWeekData.add(DateItem("지난 주", strLastWeek))


            // TAB: 월
            radioMonthData = ArrayList<DateItem>()
            //이번 달
            var thisMonth = getMonthDay(now)

            //지난 달
            var lastMonth = getMonthDay(now.minusMonths(1))

            //지난 3개월
            var last3Month = getMonthBeforeDay(now, 3)

            //지난 6개월
            var last6Month = getMonthBeforeDay(now, 6)

            radioMonthData.add(DateItem("이번 달", thisMonth))
            radioMonthData.add(DateItem("지난 달", lastMonth))
            radioMonthData.add(DateItem("지난 3개월", last3Month))
            radioMonthData.add(DateItem("지난 6개월", last6Month))

            // TAB: 분기
            setYears()

            setRadioView(radioWeekData)
        }

    }

    private fun setYears(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            radioBranchData = ArrayList<DateItem>()
            //1/4분기
            var oneBranch = getRadioBranchDay(1, 3)

            //2/4분기
            var twoBranch = getRadioBranchDay(2, 6)

            //3/4분기
            var threeBranch = getRadioBranchDay(7, 9)

            //4/4분기
            var fourBranch = getRadioBranchDay(10, 12)

            radioBranchData.add(DateItem("1/4분기",oneBranch))
            radioBranchData.add(DateItem("2/4분기",twoBranch))
            radioBranchData.add(DateItem("3/4분기",threeBranch))
            radioBranchData.add(DateItem("4/4분기",fourBranch))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getRadioBranchDay(start:Int, end: Int) : String {
        var localDate:LocalDate = LocalDate.now()
        var startDate:LocalDate = localDate.withYear(year).withMonth(start)
        var endLocalDate:LocalDate = localDate.withYear(year).withMonth(end)

        var firstDate:LocalDate = startDate.withDayOfMonth(1)
        var endDate:LocalDate = endLocalDate.withDayOfMonth(endLocalDate.lengthOfMonth())

        var strFirstDate: String = firstDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (E)"))
        var strEndDate: String = endDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (E)"))

        return "$strFirstDate ~ $strEndDate"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMonthBeforeDay(now:LocalDate, minus: Int): String {
        var localDate: LocalDate = now

        var strFirstDate: String = localDate.minusMonths(minus.toLong()).format(DateTimeFormatter.ofPattern("yyyy.MM.dd (E)"))
        var nowDate: String = localDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (E)"))

        return "$strFirstDate ~ $nowDate"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMonthDay(now:LocalDate): String {
        var localDate: LocalDate = now

        var firstDate:LocalDate = localDate.withDayOfMonth(1)
        var endDate:LocalDate = localDate.withDayOfMonth(localDate.lengthOfMonth())

        var strFirstDate: String = firstDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (E)"))
        var strEndDate: String = endDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd (E)"))

        return "$strFirstDate ~ $strEndDate"
    }

    private fun getWeekDay(calendar: Calendar ,minus: Int): String {
        calendar.add(Calendar.DAY_OF_MONTH, (minus-calendar.get(Calendar.DAY_OF_WEEK)))
        val date = calendar.time

        val dayFormat = SimpleDateFormat("yyyy.MM.dd (E)", Locale.getDefault())
        var day = dayFormat.format(date)

        return day
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
        dialog.findViewById<Spinner>(R.id.spinner_year).visibility=View.GONE
        dialog.findViewById<ConstraintLayout>(R.id.cl_date_select).visibility=View.GONE
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
                dialog.findViewById<Spinner>(R.id.spinner_year).visibility=View.VISIBLE
            }
            R.id.tab_date->{
                tabDate.setBackgroundResource(R.drawable.box_c0caeb_gray)
                tabDate.setTextColor(Color.parseColor("#0537c8"))
                root.removeAllViews()
                dialog.findViewById<ConstraintLayout>(R.id.cl_date_select).visibility=View.VISIBLE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDateInitial(){
        tvStartDate.text = "적용 시작일"
        tvEndDate.text = "적용 종료일"

        tvStartDate.setTextColor(Color.parseColor("#8c93a8"))
        tvEndDate.setTextColor(Color.parseColor("#8c93a8"))

        tvStartDate.setOnClickListener {
            CalendarDialog(context, object:CalendarDialog.DateListener{
                override fun setDate(date: String) {
                    tvStartDate.text = date
                }
            }).newOrderDialogShow()
        }

        tvEndDate.setOnClickListener{
            CalendarDialog(context, object:CalendarDialog.DateListener{
                override fun setDate(date: String) {
                    tvEndDate.text = date
                }
            }).newOrderDialogShow()
        }
    }

    data class DateItem(
        val title:String,
        val subTitle: String
    )
}