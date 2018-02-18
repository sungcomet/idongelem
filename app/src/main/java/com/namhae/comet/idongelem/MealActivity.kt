package com.namhae.comet.idongelem

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.widget.FrameLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_meal.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext

import org.jsoup.Jsoup
import java.util.Calendar


class MealActivity : DrawerActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val framelayout = findViewById<FrameLayout>(R.id.activity_frame)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val activityView = inflater.inflate(R.layout.activity_meal, null, false)
        framelayout.addView(activityView)
        supportActionBar!!.setTitle("급식정보")

        val type = Typeface.createFromAsset(assets, "fonts/NanumPen.ttf")
        meal_list.typeface = type
        doMealparsing(meal_list)

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_settings).isVisible=false
        return true

    }


fun doMealparsing(mealtext: TextView)
{
    var words = ""
    var menu = ""
    async {
        val calendar = Calendar.getInstance()
        val doc = Jsoup.connect("http://stu.gne.go.kr/sts_sci_md01_001.do?schulCode=S100001310&schulCrseScCode=2&schulKndScCode=02&schMmealScCode=2").get()
        val dates = doc.select("table.tbl_type3>thead>tr>th")
        menu = doc.select("table.tbl_type3>tbody>tr:eq(1)>td:eq(${calendar.get(Calendar.DAY_OF_WEEK)})").text()
        when (menu == "") {true -> menu = "식단정보가 없습니다."
        }
        words += (dates[calendar.get(Calendar.DAY_OF_WEEK)].text() + "\n \n "
                + menu + "\n\n")


        withContext(UI) {
            mealtext.text = words

        }
    }
    }
}
