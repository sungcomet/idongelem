package com.namhae.comet.idongelem

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_meal.*

import org.jsoup.Jsoup
import java.util.Calendar

/**
 * Created by comet on 4/3/2017.
 */

class MealActivity : DrawerActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val framelayout = findViewById(R.id.activity_frame) as FrameLayout
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val activityView = inflater.inflate(R.layout.activity_meal, null, false)
        framelayout.addView(activityView)
        supportActionBar!!.setTitle("급식정보")

        val type = Typeface.createFromAsset(assets, "fonts/NanumPen.ttf")
        meal_list.typeface = type
        doit().execute()

    }

    inner class doit : AsyncTask<Void, Void, Void>() {
        internal var words = ""

        override fun doInBackground(vararg params: Void): Void? {
            try {
                val doc = Jsoup.connect("http://stu.gne.go.kr/sts_sci_md01_001.do?schulCode=S100001310&schulCrseScCode=2&schulKndScCode=02&schMmealScCode=2").get()
                val dates = doc.select("table.tbl_type3>thead>tr>th")
                val meals = doc.select("table.tbl_type3>tbody>tr:eq(1)>td")
                val calendar = Calendar.getInstance()

                words += (dates[calendar.get(Calendar.DAY_OF_WEEK)].text() + "\n \n "
                        + meals[calendar.get(Calendar.DAY_OF_WEEK) - 1].text() + "\n\n")

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            meal_list.text = words
        }
    }
}
