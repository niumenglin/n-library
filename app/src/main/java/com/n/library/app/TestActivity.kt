package com.n.library.app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.n.library.util.ActivityManager

/**
 * 测试页面
 */
class TestActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.tv_test)
        val topActivity = ActivityManager.instance.topActivity
        topActivity?.let {
            textView.text = topActivity.localClassName
        }
    }
}