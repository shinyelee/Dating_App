package com.shinyelee.dating_app.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shinyelee.dating_app.R

class CardStackAdapter(val context: Context, val items: List<String>): RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    // ViewHolder : (자식뷰를 포함한) 레이아웃 단위의 뷰를 하나의 뷰홀더로 설정

    // 뷰홀더 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    // 각 뷰홀더에 데이터 연결
    override fun onBindViewHolder(holder: CardStackAdapter.ViewHolder, position: Int) = holder.binding(items[position])

    // 전체 뷰홀더(아이템) 개수
    override fun getItemCount(): Int = items.size

    // 자식뷰를 포함한 레이아웃 단위의 뷰를 하나의 뷰홀더로 설정
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun binding(data: String) {

        }

    }

}

// 설명 https://brunch.co.kr/@mystoryg/140