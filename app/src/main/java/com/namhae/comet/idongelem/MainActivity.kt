package com.namhae.comet.idongelem

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_main.*
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.Toast


class MainActivity : DrawerActivity() {
    var lastTimeBackPressed  : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val framelayout = findViewById(R.id.activity_frame) as FrameLayout
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val activityView = inflater.inflate(R.layout.activity_main, null, false)
        framelayout.addView(activityView)
        supportActionBar!!.setTitle("이동초등학교")

        val type = Typeface.createFromAsset(assets, "fonts/NanumPen.ttf")
        btn_meal.typeface = type
        btn_tel.typeface = type
        btn_notice.typeface = type
        btn_house.typeface = type
        btn_album.typeface = type
        btn_class.typeface = type

        btn_meal.setOnClickListener {
            val intent = Intent(this@MainActivity, MealActivity::class.java)
            startActivity(intent)
        }
        btn_tel.setOnClickListener {
            val intent = Intent(this@MainActivity, CallActivity::class.java)
            startActivity(intent)
        }
        btn_notice.setOnClickListener {
            val intent = Intent(this@MainActivity, BoardActivity::class.java)
            intent.putExtra("value", "1")
            startActivity(intent)
        }
        btn_house.setOnClickListener {
            val intent = Intent(this@MainActivity, BoardActivity::class.java)
            intent.putExtra("value", "4")
            startActivity(intent)
        }
        btn_album.setOnClickListener {
            val intent = Intent(this@MainActivity, AlbumActivity::class.java)
            startActivity(intent)
        }
        btn_class.setOnClickListener {
            val intent = Intent(this@MainActivity, StudentClassActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        if(System.currentTimeMillis()-lastTimeBackPressed<1500)
        {
            android.os.Process.killProcess(android.os.Process.myPid())
        }
        Toast.makeText(this, "뒤로가기를 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show()
        lastTimeBackPressed = System.currentTimeMillis()
    }
}
