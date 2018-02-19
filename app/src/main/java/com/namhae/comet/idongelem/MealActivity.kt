package com.namhae.comet.idongelem

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.android.synthetic.main.activity_meal.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext

import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*


class MealActivity : DrawerActivity() {
    val cal = Calendar.getInstance()
    lateinit var mDateSetListener : DatePickerDialog.OnDateSetListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val framelayout = findViewById<FrameLayout>(R.id.activity_frame)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val activityView = inflater.inflate(R.layout.activity_meal, null, false)
        framelayout.addView(activityView)
        supportActionBar!!.setTitle("급식정보")


        mDateSetListener= object: DatePickerDialog.OnDateSetListener{
            override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()

            }
        }
        val type = Typeface.createFromAsset(assets, "fonts/NanumPen.ttf")
        meal_list.typeface = type
        updateDateInView()

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_settings).isVisible=false
        return true

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_date -> {
                DatePickerDialog(this@MealActivity, mDateSetListener,
                        cal.get(Calendar.YEAR),cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
             }
        }
        return true
    }


fun doMealparsing(date:String,dayofweek:Int, mealtext: TextView)
{
    var words = ""
    var menu = ""
    async {
        val calendar = Calendar.getInstance()
        val doc = Jsoup.connect("http://stu.gne.go.kr/sts_sci_md01_001.do?schYmd=$date&schulCode=S100001310&schulCrseScCode=2&schulKndScCode=02&schMmealScCode=2").get()
        val dates = doc.select("table.tbl_type3>thead>tr>th")
        menu = doc.select("table.tbl_type3>tbody>tr:eq(1)>td:eq($dayofweek)").text()
        when (menu == "") {true -> menu = "식단정보가 없습니다."
        }
        words += (dates[dayofweek].text() + "\n \n "
                + menu + "\n\n")


        withContext(UI) {
            mealtext.text = words

        }
    }
    }
    private fun updateDateInView(){
        val myFormat="yyyy.MM.dd"
        val sdf=SimpleDateFormat(myFormat, Locale.KOREA)
        doMealparsing(sdf.format(cal.time),cal.get(Calendar.DAY_OF_WEEK), meal_list)    }
}
