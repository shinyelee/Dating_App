package com.shinyelee.dating_app.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.auth.UserDataModel

class CardStackAdapter(val context: Context, val items: List<UserDataModel>): RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

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

        // 카드스택뷰의 프사, 별명, 지역, 나이
        val image = itemView.findViewById<ImageView>(R.id.profileImageArea)
        val nickname = itemView.findViewById<TextView>(R.id.itemNickname)
        val city = itemView.findViewById<TextView>(R.id.itemCity)
        val age = itemView.findViewById<TextView>(R.id.itemAge)

        fun binding(data: UserDataModel) {

            val storageRef = Firebase.storage.reference.child(data.uid + ".png")

            storageRef.downloadUrl.addOnCompleteListener( OnCompleteListener { task ->

                if(task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .into(image)
                }

            })

            // UserDataModel의 데이터와 매칭
            nickname.text = data.nickname
            city.text = data.city
            age.text = data.age

        }

    }

}

// 설명 https://brunch.co.kr/@mystoryg/140