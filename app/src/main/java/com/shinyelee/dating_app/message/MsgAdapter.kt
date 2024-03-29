package com.shinyelee.dating_app.message

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.shinyelee.dating_app.R

// 메시지 목록
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

        // 메시지 보낸 사람의 별명과 내용을
        val nicknameArea = convertView!!.findViewById<TextView>(R.id.lvNickArea)
        val textArea = convertView.findViewById<TextView>(R.id.lvNick)

        // 받은 메시지에 넣어줌
        nicknameArea.text = items[position].senderInfo
        textArea.text = items[position].sendText

        return convertView

    }

}