package com.namhae.comet.idongelem

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jsoup.Jsoup
import java.util.ArrayList
import java.util.HashMap


class StudentClassActivity : DrawerActivity() {
    internal var arraylist = ArrayList<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val framelayout = findViewById<FrameLayout>(R.id.activity_frame)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val activityView = inflater.inflate(R.layout.activity_student_class, null, false)
        framelayout.addView(activityView)
        supportActionBar!!.setTitle("우리반홈페이지")

        getClass().execute()
        val yourListView = findViewById<ListView>(R.id.listview_class)
        yourListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val url = arraylist[position][URL]
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(myIntent)

        }
    }

    inner class getClass : AsyncTask<Void, Void, Void>()
    {
        override fun onPreExecute() {
            super.onPreExecute()
        }
        override fun doInBackground(vararg void : Void) : Void?{
            arraylist = ArrayList()
            try {
                val doc = Jsoup.connect("http://idong-p.gne.go.kr/?SCODE=S0000000294&mnu=M001009002").get()
                val divs = doc.select("div#m_ban_list")
                val uls = divs.select("ul")
                for (i in 0..5)
                {   val text = "ddd"
                    val map = HashMap<String, String>()
                    map.put("grade",uls.select("li.school_year")[i].text())
                    map.put("class_name", uls.select("li.school_ban")[i].text())
                    map.put("url", uls.select("a[href]")[i].attr("abs:href"))
                    arraylist.add(map)}
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            val listview = findViewById<ListView>(R.id.listview_class)
            val adapter = ClassListViewAdapter(this@StudentClassActivity, arraylist)
            listview.adapter = adapter
        }
    }
    companion object {

        internal var GRADE = "grade"
        internal var CLASS = "class_name"
        internal var URL = "url"
    }
    inner class ClassListViewAdapter(// Declare Variables
            internal var context: Context,
            internal var data: ArrayList<HashMap<String, String>>) : BaseAdapter() {
        internal var resultp = HashMap<String, String>()

        override fun getCount(): Int {
            return data.size
        }

        override fun getItem(position: Int): Any? {
            return null
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            // Declare Variables
            val grade: TextView
            val class_name: TextView


            val inflater = context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val itemView = inflater.inflate(R.layout.class_listview_item, parent, false)
            // Get the position
            resultp = data[position]

            // Locate the TextViews in listview_item.xml
            val type = Typeface.createFromAsset(assets, "fonts/NanumPen.ttf")
            grade = itemView.findViewById(R.id.grade)
            class_name = itemView.findViewById(R.id.class_name)
            grade.typeface = type
            class_name.typeface =type
            // Locate the ImageView in listview_item.xml


            // Capture position and set results to the TextViews
            grade.text = resultp[StudentClassActivity.GRADE]
            class_name.text = resultp[StudentClassActivity.CLASS]
            // Capture position and set results to the ImageView
            // Passes flag images URL into ImageLoader.class
            // Capture ListView item click


            return itemView
        }
    }


}
