package com.n.library.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.n.library.util.WaterMarkUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WaterMarkUtil.getInstance()
            .setRotation(-15.0F)
            .setText("自定义水印~~~")
            .setTextAlpha(15)
            .setBottomMargin(0)
            .show(this)
    }
}