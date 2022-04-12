package com.n.library.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.n.library.util.ActivityManager
import com.n.library.util.WaterMarkUtil

class MainActivity : AppCompatActivity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WaterMarkUtil.getInstance()
            .setRotation(-15.0F)
            .setText("自定义水印~~~")
            .setTextAlpha(15)
            .setBottomMargin(0)
            .show(this)


        ActivityManager.instance.addForeBackgroundCallback(object : ActivityManager.ForeBackgroundCallback{
            override fun onChange(fore: Boolean) {
                Toast.makeText(applicationContext, "当前是否处于前台：$fore",Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tv_test->{
                startActivity(Intent(this,TestActivity::class.java))
            }
        }
    }
}