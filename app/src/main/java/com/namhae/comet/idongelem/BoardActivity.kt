package com.namhae.comet.idongelem

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import kotlinx.android.synthetic.main.activity_board.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext

import org.jsoup.Jsoup

import java.util.ArrayList
import java.util.HashMap

import java.sql.Types.NULL


class BoardActivity : DrawerActivity() {

    internal var pages = ArrayList<String>()

    internal var lastnum = NULL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val framelayout = findViewById<FrameLayout>(R.id.activity_frame)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val activityView = inflater.inflate(R.layout.activity_board, null, false)
        framelayout.addView(activityView)

        var Pagenumber = editText.text.toString()
        val intent = intent
        val boardtype = intent.getStringExtra("value")

        supportActionBar!!.setTitle(TITLENAME[Integer.parseInt(boardtype)])


        async {
            lastnum = getlastnum(boardtype) / 10
            for (i in 1..lastnum) {
                pages.add(Integer.toString(i))
            }
        }


        doparsing(recycler_view,this@BoardActivity, boardtype, Pagenumber)

        val adapter =ListViewAdapter(this, BoardActivity.arraylist)
        recycler_view.adapter = adapter
        BoardActivity.linearLayoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = BoardActivity.linearLayoutManager


        buttonforward.setOnClickListener {
            Pagenumber = editText.text.toString()
            if (Integer.parseInt(Pagenumber) < lastnum) {
                Pagenumber = Integer.toString(Integer.parseInt(Pagenumber) + 1)
                editText.text = Pagenumber
                doparsing(recycler_view,this@BoardActivity, boardtype, Pagenumber)
            }
        }
        buttonbackward.setOnClickListener {
            Pagenumber = editText.text.toString()
            if (Integer.parseInt(Pagenumber) > 1) {
                Pagenumber = Integer.toString(Integer.parseInt(Pagenumber) - 1)
                editText.text = Pagenumber
                doparsing(recycler_view,this@BoardActivity, boardtype, Pagenumber)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        menu.findItem(R.id.action_date).isVisible=false

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                val boardtype = intent.getStringExtra("value")
                val builder = AlertDialog.Builder(this)//AlertDialog.Builder 객체 생성 
                val adapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice, pages)
                builder.setTitle("페이지를 선택하세요")//Dialog 제목

                builder.setAdapter(adapter) { dialog, which ->
                    val page ="${which + 1}"
                    doparsing(recycler_view,this@BoardActivity, boardtype, page)
                    editText.text = page

                }
                val a = builder.create()
                a.show()
            }
        }
        return true
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }


    companion object {
        lateinit var linearLayoutManager: LinearLayoutManager
        internal var arraylist = ArrayList<HashMap<String, String>>()
        internal var TITLE = "title"
        internal var WRITER = "writer"
        internal var DATE = "date"
        internal var NUMBER = "number"
        internal var URL = "url"
        internal var TITLENAME = arrayOf("", "공지사항", "", "", "가정통신문")
    }
}
fun doparsing(listView: RecyclerView, context: Context, board_type: String, page_number: String)
{
    async {
        BoardActivity.arraylist = ArrayList()

        val doc = Jsoup.connect("http://idong-p.gne.go.kr/index.jsp?mnu=M00100600$board_type&SCODE=S0000000294&frame=&search_field=&search_word=&category1=&category2=&category3=&cmd=list&page=$page_number&nPage=1").get()
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
        BoardActivity.arraylist.add(map1)
        for (row in dates.select("tr:gt(0)")) {
            val links = row.select("a[href]")
            val map = HashMap<String, String>()
            map.put("title", row.select("a").text())
            map.put("writer", "작성자 : " + row.select("td")[2].text())
            map.put("date", "작성일 : " + row.select("td")[3].text())
            map.put("number", row.select("td")[0].text())
            map.put("url", links[0].attr("abs:href"))
            BoardActivity.arraylist.add(map)

        }

        withContext(UI) {
            val adapter = ListViewAdapter(context, BoardActivity.arraylist)
            listView.adapter=adapter

        }
    }
}

fun getlastnum(board_type: String) : Int{
    val strArr: Array<String>
    val doc = Jsoup.connect("http://idong-p.gne.go.kr/index.jsp?SCODE=S0000000294&mnu=M00100600$board_type").get()
    val total = doc.select("dl")
    val tot = total.select("dd")[0]
    val texttest = tot.text()
    strArr = texttest.split("건".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    return Integer.parseInt(strArr[0])}
