package com.namhae.comet.idongelem

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import kotlinx.android.synthetic.main.activity_album.*

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.util.ArrayList
import java.util.HashMap

import java.sql.Types.NULL


class BoardActivity : DrawerActivity() {

    internal var arraylist = ArrayList<HashMap<String, String>>()
    internal var pages = ArrayList<String>()

    internal var lastnum = NULL


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val framelayout = findViewById(R.id.activity_frame) as FrameLayout
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val activityView = inflater.inflate(R.layout.activity_board, null, false)
        framelayout.addView(activityView)

        val intent = intent
        val boardtype = intent.getStringExtra("value")

        supportActionBar!!.setTitle(TITLENAME[Integer.parseInt(boardtype)])

        val Pagenum = findViewById(R.id.editText) as TextView
        val Forwardbtn = findViewById(R.id.buttonforward) as Button
        val Backwardbutton = findViewById(R.id.buttonbackward) as Button

        val Pagenumber = Pagenum.text.toString()

        try {
            lastnum = getlastnum().execute(boardtype).get() / 10 + 1
        } catch (e: Exception) {

            e.printStackTrace()

        }

        for (i in 1..lastnum) {
            pages.add(Integer.toString(i))
        }





        doit().execute(Pagenumber, boardtype)
        val yourListView = findViewById(R.id.listview) as ListView
        yourListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val url = arraylist[position][URL]
            val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(myIntent)
        }
        Forwardbtn.setOnClickListener {
            var Pagenumber = Pagenum.text.toString()

            if (Integer.parseInt(Pagenumber) < lastnum) {
                Pagenumber = Integer.toString(Integer.parseInt(Pagenumber) + 1)
                Pagenum.text = Pagenumber
                doit().execute(Pagenumber, boardtype)
            }
        }
        Backwardbutton.setOnClickListener {
            var Pagenumber = Pagenum.text.toString()

            if (Integer.parseInt(Pagenumber) > 1) {
                Pagenumber = Integer.toString(Integer.parseInt(Pagenumber) - 1)
                Pagenum.text = Pagenumber
                doit().execute(Pagenumber, boardtype)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val buider = AlertDialog.Builder(this)//AlertDialog.Builder 객체 생성 
                val adapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, pages)
                buider.setTitle("페이지를 선택하세요")//Dialog 제목

                buider.setAdapter(adapter) { dialog, which ->
                    doit().execute(Integer.toString(which + 1),
                            "1")
                    val Pagenum = findViewById(R.id.editText) as TextView
                    Pagenum.text = Integer.toString(which + 1)
                }
                val a = buider.create()
                a.show()
            }
        }
        return true
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    internal inner class doit : AsyncTask<String, Void, Void>() {
        private var pDialog: ProgressDialog? = null


        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = ProgressDialog(this@BoardActivity)
            pDialog!!.setMessage("불러오는 중입니다.")
            pDialog!!.show()
        }


        override fun doInBackground(vararg what: String): Void? {
            arraylist = ArrayList()

            try {
                val doc = Jsoup.connect("http://idong-p.gne.go.kr/index.jsp?mnu=M00100600" + what[1] + "&SCODE=S0000000294&frame=&search_field=&search_word=&category1=&category2=&category3=&cmd=list&page=" + what[0] + "&nPage=1").get()
                val dates = doc.select("tbody")
                val row1 = dates.select("tr")[0]
                val as1 = row1.select("a")
                val tds1 = row1.select("td")
                val links1 = row1.select("a[href]")
                val map1 = HashMap<String, String>()
                map1.put("title", as1.text())
                map1.put("writer", "작성자 : " + tds1[2].text())
                map1.put("date", "작성일 : " + tds1[3].text())
                map1.put("number", tds1[0].text())
                map1.put("url", links1[0].attr("abs:href"))
                arraylist.add(map1)
                for (row in dates.select("tr:gt(0)")) {
                    val `as` = row.select("a")
                    val tds = row.select("td")
                    val links = row.select("a[href]")
                    val map = HashMap<String, String>()
                    map.put("title", `as`.text())
                    map.put("writer", "작성자 : " + tds[2].text())
                    map.put("date", "작성일 : " + tds[3].text())
                    map.put("number", tds[0].text())
                    map.put("url", links[0].attr("abs:href"))
                    arraylist.add(map)

                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            super.onPostExecute(aVoid)
            val listview = findViewById(R.id.listview) as ListView
            val adapter = ListViewAdapter(this@BoardActivity, arraylist)
            // Pass the results into ListViewAdapter.java
            // Set the adapter to the ListView
            listview.adapter = adapter
            pDialog!!.dismiss()
        }
    }


    inner class getlastnum : AsyncTask<String, Void, Int>() {

        override fun onPreExecute() {
            super.onPreExecute()

        }


        override fun doInBackground(vararg what: String): Int? {
            var strArr: Array<String>? = null
            try {
                val doc = Jsoup.connect("http://idong-p.gne.go.kr/index.jsp?SCODE=S0000000294&mnu=M00100600" + what[0]).get()
                val total = doc.select("dl")
                val tot = total.select("dd")[0]
                val texttest = tot.text()
                strArr = texttest.split("건".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()


            } catch (e: Exception) {
                e.printStackTrace()
            }

            return Integer.parseInt(strArr!![0])
        }

        override fun onPostExecute(aVoid: Int?) {
            super.onPostExecute(aVoid)


        }
    }

    companion object {
        internal var TITLE = "title"
        internal var WRITER = "writer"
        internal var DATE = "date"
        internal var NUMBER = "number"
        internal var URL = "url"
        internal var TITLENAME = arrayOf("", "공지사항", "", "", "가정통신문")
    }
}
