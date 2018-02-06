package com.namhae.comet.idongelem

/**
 * Created by comet on 4/10/2017.
 */


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import java.util.ArrayList
import java.util.HashMap

class ListViewAdapter(// Declare Variables
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
        val title: TextView
        val writer: TextView
        val date: TextView
        val number: TextView

        val inflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val itemView = inflater.inflate(R.layout.boardlistview_item, parent, false)
        // Get the position
        resultp = data[position]

        // Locate the TextViews in listview_item.xml
        title = itemView.findViewById(R.id.title) as TextView
        writer = itemView.findViewById(R.id.writer) as TextView
        date = itemView.findViewById(R.id.date) as TextView
        number = itemView.findViewById(R.id.number) as TextView
        // Locate the ImageView in listview_item.xml


        // Capture position and set results to the TextViews
        title.text = resultp[BoardActivity.TITLE]
        writer.text = resultp[BoardActivity.WRITER]
        date.text = resultp[BoardActivity.DATE]
        number.text = resultp[BoardActivity.NUMBER]
        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        // Capture ListView item click


        return itemView
    }
}
