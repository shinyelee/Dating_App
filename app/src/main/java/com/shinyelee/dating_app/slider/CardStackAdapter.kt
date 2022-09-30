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

// 카드스택
class CardStackAdapter(val context: Context, private val items: List<UserDataModel>): RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    // ViewHolder : (자식뷰를 포함한) 레이아웃 단위의 뷰를 하나의 뷰홀더로 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackAdapter.ViewHolder {

        // 뷰홀더 생성
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

        // (카드의) 프사
        private val image = itemView.findViewById<ImageView>(R.id.profileImageArea)

        // 별명
        private val nickname = itemView.findViewById<TextView>(R.id.itemNickname)

        // 지역
        private val city = itemView.findViewById<TextView>(R.id.itemCity)

        // 생년
        private val age = itemView.findViewById<TextView>(R.id.itemAge)

        fun binding(data: UserDataModel) {

            // 프사 저장된 위치
            val storageRef = Firebase.storage.reference.child(data.uid + ".png")

            // 프사 다운로드
            storageRef.downloadUrl.addOnCompleteListener( OnCompleteListener { task ->

                // 수행
                if(task.isSuccessful) {

                    // 글라이드로 불러옴
                    Glide.with(context)
                        .load(task.result)
                        .into(image)

                }

            })

            // 불러온 별명, 지역, 나이 정보를 해당 영역에 매칭
            nickname.text = data.nickname
            city.text = data.city
            age.text = data.age

        }

    }

}