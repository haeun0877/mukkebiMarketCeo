package com.example.market_ceo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.market_ceo.main.MainFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainFrag = MainFragment.newInstance()

        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.layout_fragment,
                mainFrag,
                mainFrag.tag
            )
            .commitAllowingStateLoss()
    }

    fun setFragment(newFragment: Fragment){
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.layout_fragment,
                newFragment,
                newFragment.tag
            )
            .addToBackStack(newFragment.tag)
            .commitAllowingStateLoss()
    }

    fun removeAllFragments(){
        supportFragmentManager
            .popBackStack(0, 0)
    }

    fun setFragmentRemoveAll(newFragment: Fragment){
        removeAllFragments()
        setFragment(newFragment)
    }
}