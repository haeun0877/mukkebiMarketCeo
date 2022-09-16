package com.example.market_ceo.main.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.CalendarView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import com.example.market_ceo.R
import com.example.market_ceo.main.adapter.DialogPriceAdapter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarDialog (context : Context, listener: DateListener) {
    private val dialog = Dialog(context)
    private val context = context
    private val dateListener = listener

    private lateinit var calendarView: CalendarView

    @RequiresApi(Build.VERSION_CODES.O)
    fun newOrderDialogShow(){

        setDialogSetting()

        dialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun setDialogSetting(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_calendar)

        val format = SimpleDateFormat("yyyy.MM.dd (E)")
        val calendar = Calendar.getInstance()

        val window = dialog.window
        window?.setGravity(Gravity.CENTER)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        calendarView = dialog.findViewById(R.id.calendar_view)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            calendar.set(year,month,dayOfMonth)
        }

        dialog.findViewById<TextView>(R.id.tv_date_ok).setOnClickListener {
            try{
                var date:String = format.format(calendar.time)

                dateListener.setDate(date)
                dialog.dismiss()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    interface DateListener{
        fun setDate(date:String)
    }
}