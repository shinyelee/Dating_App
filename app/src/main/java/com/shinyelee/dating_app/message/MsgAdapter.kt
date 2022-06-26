package com.shinyelee.dating_app.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.shinyelee.dating_app.R

class MsgAdapter(val context : Context, val items : MutableList<MsgModel>) : BaseAdapter() {

    // 리스트 전체 개수
    override fun getCount(): Int = items.size

    // 리스트를 하나씩 가져옴
    override fun getItem(position: Int): Any = items[position]

    // 리스트의 ID를 가져옴
    override fun getItemId(position: Int): Long = position.toLong()

    // 뷰를 꾸며줌
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null) {
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.list_view_item, parent, false)
        }
        // 별명 불러와서 좋아요 리스트에 넣어줌
        val nickname = convertView!!.findViewById<TextView>(R.id.listViewItemNickname)
//        nickname.text = items[position].nickname
        return convertView!!
    }

}