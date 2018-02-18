package com.namhae.comet.idongelem

/**
 * Created by comet on 4/10/2017.
 */


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.util.ArrayList
import java.util.HashMap

interface ItemClickListener
{fun onItemClickListenser(view:View, pos: Int)}

class ListViewAdapter (// Declare Variables
        internal var context: Context,
        internal var data: ArrayList<HashMap<String, String>>
) : RecyclerView.Adapter<ListViewAdapter.ViewHolder>() {
    internal var resultp = HashMap<String, String>()

    override fun getItemCount(): Int {
        return data.size
    }



    override fun getItemId(position: Int): Long {
        return 0

    }


    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListViewAdapter.ViewHolder {
        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v=  inflater.inflate(R.layout.boardlistview_item, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        resultp = data[position]
        holder.title.text = resultp[BoardActivity.TITLE]
        holder.writer.text = resultp[BoardActivity.WRITER]
        holder.date.text = resultp[BoardActivity.DATE]
        holder.number.text = resultp[BoardActivity.NUMBER]
        holder.url=resultp[BoardActivity.URL]
        holder.setOnitemClickListener(object : ItemClickListener{
            override fun onItemClickListenser(view: View, pos: Int) {
                val myIntent = Intent(Intent.ACTION_VIEW, Uri.parse(holder.url))
                startActivity(context, myIntent, null)
            }
        })

    }

    class ViewHolder(view:View): RecyclerView.ViewHolder(view), View.OnClickListener{
        var title : TextView
        var writer : TextView
        var date : TextView
        var number : TextView

        init {
            title = view.findViewById<TextView>(R.id.title)
            writer = view.findViewById<TextView>(R.id.writer)
            date = view.findViewById<TextView>(R.id.date)
            number = view.findViewById<TextView>(R.id.number)
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
