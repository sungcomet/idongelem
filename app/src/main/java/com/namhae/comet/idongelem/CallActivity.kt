package com.namhae.comet.idongelem

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout

/**
 * Created by comet on 4/3/2017.
 */

class CallActivity : DrawerActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val framelayout = findViewById(R.id.activity_frame) as FrameLayout
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val activityView = inflater.inflate(R.layout.activity_call, null, false)
        framelayout.addView(activityView)
        supportActionBar!!.setTitle("주요 연락처")


        val btncall1 = findViewById(R.id.btncall1) as Button
        val btncall2 = findViewById(R.id.btncall2) as Button
        val type = Typeface.createFromAsset(assets, "fonts/NanumPen.ttf")

        btncall1.typeface = type
        btncall2.typeface = type

        btncall1.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:055-862-1597"))
            startActivity(intent)
        }
        btncall2.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:055-862-5009"))
            startActivity(intent)
        }
    }

}
