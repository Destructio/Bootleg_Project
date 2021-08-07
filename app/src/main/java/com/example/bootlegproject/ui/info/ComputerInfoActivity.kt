package com.example.bootlegproject.ui.info

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.bootlegproject.R

class ComputerInfoActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_computer_info)
        val pager: ViewPager = findViewById(R.id.pager)
        val json: String? = intent.getStringExtra("computer")
        val email: String? = intent.getStringExtra("email")
        val pagerAdapter: PagerAdapter = MyFragmentPagerAdapter(supportFragmentManager, json!!, email!! )
        pager.adapter = pagerAdapter
    }
}