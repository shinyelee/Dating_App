package com.shinyelee.dating_app.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.auth.UserDataModel

class ListViewAdapter(val context : Context, val items : MutableList<UserDataModel>) : BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.list_view_item, parent, false)
        }
        // 별명 불러와서 좋아요 리스트에 넣어줌
        val nickname = convertView!!.findViewById<TextView>(R.id.listViewItemNickname)
        nickname.text = items[position].nickname
        return convertView!!
    }

}