package com.namhae.comet.idongelem
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.*
import android.widget.*
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import org.jsoup.Jsoup
import java.sql.Types
import java.util.ArrayList
import java.util.HashMap
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext

class AlbumActivity : DrawerActivity(){

    private var pages = ArrayList<String>()
    internal var lastnum = Types.NULL


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val framelayout = findViewById<FrameLayout>(R.id.activity_frame)
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val activityView = inflater.inflate(R.layout.activity_album, null, false)
        framelayout.addView(activityView)
        supportActionBar!!.setTitle("우리학교앨범")

        val Pagenum = findViewById<TextView>(R.id.albumpage)
        val Pagenumber = Pagenum.text.toString()


        try {
            lastnum = getlastnum().execute().get() / 10 + 1
        } catch (e: Exception) {

            e.printStackTrace()
        }

        for (i in 1..lastnum) {
            pages.add(Integer.toString(i))
        }

        doAlbumparsing(gridview,this@AlbumActivity, Pagenumber)
        val adapter =GridViewAdapter(this, AlbumActivity.arraylist)
        gridview.adapter = adapter
        AlbumActivity.gridLayoutManager = GridLayoutManager(this, 3)
        gridview.layoutManager = AlbumActivity.gridLayoutManager

        buttonforward.setOnClickListener {
            var Pagenumber = Pagenum.text.toString()

            if (Integer.parseInt(Pagenumber) < lastnum) {
                Pagenumber = Integer.toString(Integer.parseInt(Pagenumber) + 1)
                Pagenum.text = Pagenumber
                doAlbumparsing(gridview,this@AlbumActivity, Pagenumber)            }
        }
        buttonbackward.setOnClickListener {
            var Pagenumber = Pagenum.text.toString()

            if (Integer.parseInt(Pagenumber) > 1) {
                Pagenumber = Integer.toString(Integer.parseInt(Pagenumber) - 1)
                Pagenum.text = Pagenumber
                doAlbumparsing(gridview,this@AlbumActivity, Pagenumber)            }
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
                val buider = AlertDialog.Builder(this)//AlertDialog.Builder 객체 생성 
                val adapter = ArrayAdapter(this, android.R.layout.select_dialog_singlechoice, pages)
                buider.setTitle("페이지를 선택하세요")//Dialog 제목

                buider.setAdapter(adapter) { dialog, which ->
                    doAlbumparsing(gridview,this@AlbumActivity, Integer.toString(which + 1))
                    albumpage.text = java.lang.String.format("%s", which + 1)
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



     class getlastnum : AsyncTask<Void, Void, Int>() {


        override fun doInBackground(vararg what: Void): Int? {
            var strArr: Array<String>? = null
            try {
                val doc = Jsoup.connect("http://idong-p.gne.go.kr/index.jsp?SCODE=S0000000294&mnu=M001006007").get()
                val total = doc.select("dl")
                val tot = total.select("dd")[0]
                val texttest = tot.text()
                strArr = texttest.split("건".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()


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
        lateinit var gridLayoutManager: GridLayoutManager
        internal var arraylist = ArrayList<HashMap<String, String>>()

        internal var ALBUMTITLE = "albumtitle"
        internal var PICTURE = "picture"
        internal var URL = "url"
    }

}

fun doAlbumparsing(listView: RecyclerView, context: Context, page_number: String)
{
    async {
            AlbumActivity.arraylist = ArrayList()


            val doc = Jsoup.connect("http://idong-p.gne.go.kr/index.jsp?mnu=M001006007&SCODE=S0000000294&frame=&search_field=&search_word=&category1=&category2=&category3=&search_year=&cmd=list&page=$page_number&nPage=1").get()
            val dates = doc.select("table")

            for (row in dates.select("td")) {
                val links = row.select("a[href]")
                val divs = row.select("div.image img[src]")
                val map = HashMap<String, String>()
                map.put("albumtitle", divs[0].attr("alt"))
                map.put("picture", divs[0].attr("abs:src"))
                map.put("url", links[0].attr("abs:href"))
                AlbumActivity.arraylist.add(map)




        }

        withContext(UI) {
            val adapter = GridViewAdapter(context, AlbumActivity.arraylist)
            listView.adapter=adapter

        }
    }
}


class GridViewAdapter(
        var context: Context,
        var data: ArrayList<HashMap<String, String>>) : RecyclerView.Adapter<GridViewAdapter.ViewHolder>() {

    var resultp = HashMap<String, String>()

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return 0
    }
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): GridViewAdapter.ViewHolder {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v=  inflater.inflate(R.layout.albumlistview_item, parent, false)

        return ViewHolder(v)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        resultp = data[position]
        holder.albumtitle.text = resultp[AlbumActivity.ALBUMTITLE]
        holder.albumtitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15.0f)
        holder.url = resultp[AlbumActivity.URL]
        Picasso.with(context).load(resultp[AlbumActivity.PICTURE]).memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .fit().into(holder.picture)
        holder.setOnitemClickListener(object : ItemClickListener{
            override fun onItemClickListenser(view: View, pos: Int) {
                val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(holder.url))
                ContextCompat.startActivity(context, myIntent, null)
            }
        })

    }
    class ViewHolder(view:View): RecyclerView.ViewHolder(view), View.OnClickListener{
        var albumtitle : TextView
        var picture : ImageView
        init {
            albumtitle = view.findViewById<TextView>(R.id.albumtitle)
            picture = view.findViewById<ImageView>(R.id.picture)
            view.setOnClickListener(this)
        }
        var url: String?=null
        var mlistener : ItemClickListener?=null
        fun setOnitemClickListener(itemClickListener: ItemClickListener)
        {
            this.mlistener = itemClickListener
        }

        override fun onClick(p0: View?) {
            this.mlistener!!.onItemClickListenser(p0!!, adapterPosition)
        }
    }


}

