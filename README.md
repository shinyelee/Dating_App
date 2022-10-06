# 데이트 DAY2

> 데이트/매칭 앱 프로젝트

![day2_cover](https://user-images.githubusercontent.com/68595933/193454768-3bb5a2d3-6d1a-4605-82d0-6c94e54c6130.png)

## 시작

- FCM을 이용한 데이트/매칭 프로젝트입니다.

---

## 개발

### 기간

- 22. 5. 30. ~ 22. 7. 14.

### 목표

- 코틀린으로 주요 기능을 구현합니다.
- FCM을 사용합니다.

### 사용

- Kotlin
- Jetpack
- Firebase

---

## 기능

### 1. 인증(Firebase Auth)

![auth](https://user-images.githubusercontent.com/68595933/194291726-c0896dd5-237f-4c01-a103-f3ea28ebc9e2.PNG)

- 1.1. 로그인/로그아웃
  - 유효성 검사 후 로그인합니다.
  - 비회원으로 로그인하면 회원 가입 절차 없이 로그인하는 대신, 한 번 로그아웃하면 같은 계정으로 다시 로그인할 수 없습니다.
  - 로그아웃하면 IntroActivity로 이동합니다.

```kotlin
// LoginActivity.kt

        // 로그인 버튼 클릭하면
        binding.loginBtn.setOnClickListener {

            // 로그인조건 확인
            var emailCheck = true
            var pwCheck = true

            // 이메일, 비밀번호
            val emailTxt = binding.email.text.toString()
            val pwTxt = binding.pw.text.toString()

            // 이메일 정규식
            val emailPattern = Patterns.EMAIL_ADDRESS

            // 이메일 검사
            if(emailTxt.isEmpty()) {
                emailCheck = false
                binding.emailArea.error = "이메일주소를 입력하세요"
            } else if(!emailPattern.matcher(emailTxt).matches()) {
                emailCheck = false
                binding.emailArea.error = "이메일 형식이 잘못되었습니다"
            } else {
                emailCheck = true
                binding.emailArea.error = null
            }

            // 비밀번호 검사
            if(pwTxt.isEmpty()) {
                pwCheck = false
                binding.pwArea.error = "비밀번호를 입력해 주세요"
            } else if (pwTxt.length<6) {
                pwCheck = false
                binding.pwArea.error = "최소 6자 이상 입력하세요"
            } else if (pwTxt.length>20) {
                pwCheck = false
                binding.pwArea.error = "20자 이하로 입력하세요"
            } else {
                pwCheck = true
                binding.pwArea.error = null
            }

            // 로그인 조건을 모두 만족하면
            if(emailCheck and pwCheck) {

                // 로그인
                auth.signInWithEmailAndPassword(emailTxt, pwTxt)
                    .addOnCompleteListener(this) { task ->

                        // 성공하면
                        if (task.isSuccessful) {

                            // 명시적 인텐트 -> 다른 액티비티 호출
                            val intent = Intent(this, MainActivity::class.java)

                            // 메인 액티비티 시작
                            startActivity(intent)

                            // 로그인 액티비티 종료
                            finish()

                        // 오류 등 -> 로그인 실패
                        } else { Toast.makeText(this, "이메일과 비밀번호를 다시 확인하세요", Toast.LENGTH_SHORT).show() }

                    }

            // 조건 불만족하면 로그인 실패
            } else { Toast.makeText(this, "이메일과 비밀번호를 다시 확인하세요", Toast.LENGTH_SHORT).show() }

        }
```
```kotlin
// MyPageActivity.kt

        // 로그아웃 버튼
        binding.logoutBtn.setOnClickListener {

            // 로그아웃 실행
            Firebase.auth.signOut()

            // 로그아웃 확인 메시지지
            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()

            // '새 작업(task) 시작' 또는 '시작하려는 액티비티보다 상위에 존재하는 액티비티 삭제'
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // 명시적 인텐트 -> 다른 액티비티 호출
            val intent = Intent(this, IntroActivity::class.java)

            // 인트로 액티비티 시작
            startActivity(intent)

            // 내 정보 액티비티 종료
            finish()

        }
```

- 1.2. 회원가입
  - 유효성 검사 후 가입합니다.

```kotlin
// JoinActivity.kt

        // 회원가입 버튼
        binding.joinBtn.setOnClickListener {

            // 가입조건 확인
            var emailCheck = true
            var pwCheck = true
            var nicknameCheck = true
            var genderCheck = true
            var cityCheck = true
            var ageCheck = true

            // 모든 가입조건
            var allCheck = emailCheck and pwCheck and nicknameCheck and genderCheck and cityCheck and ageCheck

            // 이메일주소, 비밀번호
            val emailTxt = binding.email.text.toString()
            val pwTxt = binding.pw.text.toString()

            // 별명, 성별, 지역, 생년
            nickname = binding.nickname.text.toString()
            gender = binding.gender.text.toString()
            city = binding.city.text.toString()
            age = binding.age.text.toString()

            // 빈 칸 검사
            if(emailTxt.isEmpty() || pwTxt.isEmpty() || nickname.isEmpty() || gender.isEmpty() || city.isEmpty() || age.isEmpty()) {
                allCheck = false
                Toast.makeText(this, "입력란을 모두 작성하세요", Toast.LENGTH_SHORT).show()
            }

            // 이메일주소 정규식
            val emailPattern = Patterns.EMAIL_ADDRESS

            // 이메일주소 검사
            if(emailTxt.isEmpty()) {
                emailCheck = false
                binding.emailArea.error = "이메일주소를 입력하세요"
            } else if(!emailPattern.matcher(emailTxt).matches()) {
                emailCheck = false
                binding.emailArea.error = "이메일 형식이 잘못되었습니다"
            } else {
                emailCheck = true
                binding.emailArea.error = null
            }

            // 비밀번호 검사
            if(pwTxt.isEmpty()) {
                pwCheck = false
                binding.pwArea.error = "비밀번호를 입력하세요"
            } else if (pwTxt.length<6) {
                pwCheck = false
                binding.pwArea.error = "최소 6자 이상 입력하세요"
            } else if (pwTxt.length>20) {
                pwCheck = false
                binding.pwArea.error = "20자 이하로 입력하세요"
            } else {
                pwCheck = true
                binding.pwArea.error = null
            }

            // 별명 검사
            if(nickname.isEmpty()) {
                nicknameCheck = false
                binding.nicknameArea.error = "별명을 입력하세요"
            } else if(nickname.length>20) {
                nicknameCheck = false
                binding.nicknameArea.error = "10자 이하로 입력하세요"
            } else {
                nicknameCheck = true
                binding.nicknameArea.error = null
            }

            // 성별 검사
            if(gender.isEmpty()) {
                genderCheck = false
                binding.genderArea.error = "성별을 입력하세요"
            } else {
                genderCheck = true
                binding.genderArea.error = null
            }

            // 지역 검사
            if(city.isEmpty()) {
                cityCheck = false
                binding.cityArea.error = "지역을 입력하세요"
            } else {
                cityCheck = true
                binding.cityArea.error = null
            }

            // 생년 검사
            if(age.isEmpty()) {
                ageCheck = false
                binding.ageArea.error = "생년을 입력하세요"
            } else {
                ageCheck = true
                binding.ageArea.error = null
            }

            // 가입조건 모두 만족하면
            if(allCheck) {

                // 계정 생성
                auth.createUserWithEmailAndPassword(emailTxt, pwTxt)
                    .addOnCompleteListener(this) { task ->

                        // 회원가입 성공
                        if (task.isSuccessful) {

                            // UID 정의
                            val user = auth.currentUser
                            uid = user?.uid.toString()

                            // 토큰
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->

                                    // 실패시 로그
                                    if (!task.isSuccessful) {
                                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                        return@OnCompleteListener
                                    }

                                    // 새 FCM 등록 토큰
                                    val token = task.result.toString()
                                    Log.e(TAG, "토큰(user token value) - $token")

                                    // 작성한 내용과 토큰값을 데이터 클래스 형태로 만들어
                                    val userModel = UserDataModel(
                                        uid,
                                        nickname,
                                        gender,
                                        city,
                                        age,
                                        token
                                    )

                                    // 파이어베이스에 회원정보 하위값으로 넣고
                                    FirebaseRef.userInfoRef.child(uid).setValue(userModel)

                                    // 프사 업로드 후
                                    uploadImage(uid)

                                    // 명시적 인텐트 -> 다른 액티비티 호출
                                    val intent = Intent(this, MainActivity::class.java)

                                    // 메인 액티비티 시작
                                    startActivity(intent)

                                    // 조인 액티비티 종료
                                    finish()

                                })

                        // 오류, 중복 계정 등 -> 회원가입 실패
                        } else { Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show() }

                    }

            // 조건 불만족하면 회원가입 실패
            } else { Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show() }

        }
```

### 2. 메인(Card Stack View)

![main](https://user-images.githubusercontent.com/68595933/194291695-f901b0b2-c53e-48b2-a533-778b8e205605.PNG)

- 현재 사용자와 다른 성별인 사용자의 프로필만 카드 더미 형식으로 구현합니다.
- 카드를 오른쪽으로 넘기면 하트 애니메이션이 활성화되며 좋아요 목록에 해당 사용자를 추가합니다.
- 카드를 왼쪽으로 넘기면 좋아요를 취소합니다. - 22. 10. 6. 업데이트
- 모든 프로필을 확인하면 자동으로 새로고침합니다.

```kotlin
// MainActivity.kt

        // 카드스택뷰
        manager = CardStackLayoutManager(baseContext, object: CardStackListener {

            // 카드 넘기기
            override fun onCardSwiped(direction: Direction?) {
            
                // 왼쪽(관심없음)
                if(direction == Direction.Left) {

                    // 해당 카드(사용자) 좋아요 삭제
                    userLikeDelete(uid, usersDataList[userCount].uid.toString())

                }

                // 오른쪽(좋아요)
                if(direction == Direction.Right) {

                    // 하트 애니메이션 및 토스트 메시지
                    binding.ltAnimation.playAnimation()
                    Toast.makeText(this@MainActivity, "좋아요", Toast.LENGTH_SHORT).show()

                    // 해당 카드(사용자) 좋아요 처리
                    userLikeOther(uid, usersDataList[userCount].uid.toString())

                }

                // 넘긴 프로필의 수를 셈
                userCount += 1

                // 프로필 전부 다 봤을 때
                if(userCount == usersDataList.count()) {

                    // 자동으로 새로고침
                    getUserDataList(currentUserGender)
                    Toast.makeText(this@MainActivity, "모든 프로필을 확인했습니다", Toast.LENGTH_SHORT).show()

                }
            }

            // 중략

        })

        // 카드스택어댑터에 데이터 넘김
        cardStackAdapter = CardStackAdapter(baseContext, usersDataList)
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = cardStackAdapter

        // 현재 사용자 정보
        getMyUserData()

    }

    // 현재 사용자 정보
    private fun getMyUserData() {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터스냅샷 내 사용자 데이터 출력
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 프사 제외한 나머지 정보
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                // 현재 사용자의 성별
                currentUserGender = data?.gender.toString()

                // 현재 사용자의 닉네임
                MyInfo.myNickname = data?.nickname.toString()

                // 현재 사용자와 성별이 반대인 사용자 목록
                getUserDataList(currentUserGender)

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getMyUserData - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)

    }

    // 전체 사용자 정보
    private fun getUserDataList(currentUserGender : String) {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터스냅샷 내 사용자 데이터 출력
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 데이터스냅샷 내 사용자 데이터 출력
                for(dataModel in dataSnapshot.children) {

                    // 다른 사용자들 정보 가져옴
                    val user = dataModel.getValue(UserDataModel::class.java)

                    // 현재 사용자와 같은 성별인 사용자 -> 패스
                    if(user!!.gender.toString() == currentUserGender) {}

                    // 현재 사용자와 다른 성별인 사용자만 불러옴
                    else { usersDataList.add(user) }

                }

                // 동기화(새로고침) -> 리스트 크기 및 아이템 변화를 어댑터에 알림
                cardStackAdapter.notifyDataSetChanged()

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getUserDataList - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userInfoRef.addValueEventListener(postListener)

    }

    // 카드 좋아요 하기
    private fun userLikeOther(myUid : String, otherUid : String) {

        // (카드 오른쪽으로 넘기면) 좋아요 값 true로 설정
        FirebaseRef.userLikeRef.child(myUid).child(otherUid).setValue("true")

        // 좋아요 목록
        getMyLikeList(otherUid)
        
    }
```
```kotlin
// CardStackAdapter.kt

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
```

### 3. 좋아요 및 매칭(Firebase Cloud Messaging)

![my_like](https://user-images.githubusercontent.com/68595933/193457538-67f80dfb-51c7-40e9-b330-67252af42a72.PNG)

- 3.1. 좋아요
  - 좋아요 목록에서 내가 좋아하는 사용자를 확인합니다.
  - 현재 사용자와 상대방이 서로 좋아요 상태면 메시지를 주고받을 수 있습니다.
  - 나만 좋아요 상태면 메시지를 보낼 수 없습니다.

```kotlin
// MyLikeActivity.kt

    // 현재 사용자의 좋아요 리스트
    private fun getMyLikeList() {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 데이터스냅샷 내 사용자 데이터 출력 -> 현재 사용자가 좋아하는 사용자들의 UID를 myLikeList에 넣음
                for(dataModel in dataSnapshot.children) { myLikeListUid.add(dataModel.key.toString()) }

                // 전체 사용자 정보 받아옴
                getUserDataList()

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getMyLikeList - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userLikeRef.child(uid).addValueEventListener(postListener)

    }

    // 전체 사용자 정보
    private fun getUserDataList() {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 데이터스냅샷 내 사용자 데이터 출력
                for(dataModel in dataSnapshot.children) {

                    // 다른 사용자 정보 받아옴
                    val user = dataModel.getValue(UserDataModel::class.java)

                    // 전체 사용자 중
                    if(myLikeListUid.contains(user?.uid)) {

                        // 현재 사용자가 좋아하는 사용자의 정보만 추가
                        myLikeList.add(user!!)

                    }

                }

                // 동기화(새로고침) -> 리스트 크기 및 아이템 변화를 어댑터에 알림
                listviewAdapter.notifyDataSetChanged()
                Log.d(TAG, myLikeList.toString())

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getUserDataList - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userInfoRef.addValueEventListener(postListener)

    }
```
```kotlin
// ListViewAdapter.kt

// 좋아요 목록
class ListViewAdapter(val context : Context, private val items : MutableList<UserDataModel>) : BaseAdapter() {

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

        // 현재 사용자가 좋아하는 사용자의 별명을
        val nickname = convertView!!.findViewById<TextView>(R.id.lvNick)

        // 좋아요 목록에 넣어줌
        nickname.text = items[position].nickname

        return convertView

    }

}
```

![match](https://user-images.githubusercontent.com/68595933/193455842-c8b83ef5-13d2-42aa-bd11-33d02372e0d7.PNG)

- 3.2. 매칭
  - 현재 사용자와 상대방이 서로 좋아요한 상태면 매칭을 알리는 푸시 메시지가 전송됩니다.
  - 매칭된 사용자는 서로 메시지를 주고받을 수 있습니다.

```kotlin
// MainActivity.kt

    // 현재 사용자의 좋아요 목록
    private fun getMyLikeList(otherUid: String) {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터스냅샷 내 사용자 데이터 출력
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // "모든" 사용자의 좋아요 리스트 (x)
                // "현재 사용자가 좋아하는" 사용자의 좋아요 리스트 (O)
                for(dataModel in dataSnapshot.children) {

                    // 다른 사용자가 좋아요 한 사용자 목록에
                    val likeUserKey = dataModel.key.toString()

                    // 현재 사용자가 포함돼 있으면
                    if(likeUserKey == uid) {

                        // 알림 채널 시스템에 등록
                        createNotificationChannel()

                        // 알림 보내기
                        sendNotification()

                    }

                }

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getMyLikeList - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(postListener)

    }

    // 알림 채널 시스템에 등록
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "name"
            val descriptionText = "descriptionText"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply { description = descriptionText }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)

        }

    }

    // 푸시 알림(매칭)
    private fun sendNotification() {

        var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_baseline_local_fire_department_24)
            .setContentTitle("매칭 완료")
            .setContentText("상대방도 나에게 호감이 있어요! 메시지를 보내볼까요?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) { notify(123, builder.build()) }

    }
```
```kotlin
// MyLikeActivity.kt

        // 좋아요 목록 길게 클릭하면
        binding.myLikeListView.setOnItemLongClickListener { parent, view, position, id ->

            // 매칭된 상태인 경우 메시지 보낼 수 있음
            checkMatching(myLikeList[position].uid.toString())
            getterUid = myLikeList[position].uid.toString()
            getterToken = myLikeList[position].token.toString()

            return@setOnItemLongClickListener(true)

        }
```

### 4. 메시지 보내기(Firebase Cloud Messaging)

![push_msg1](https://user-images.githubusercontent.com/68595933/193456208-7b78b73b-0174-42b7-a262-1d8d61c3d5a1.PNG)
![push_msg2](https://user-images.githubusercontent.com/68595933/193456209-a6b02f97-b181-4603-9ef0-b4eca9abb2f7.PNG)
![push_msg3](https://user-images.githubusercontent.com/68595933/193456211-0ec8986e-db0f-42ad-ac38-b0a5ebe5df96.PNG)

- 메시지를 보내면 해당 내용이 푸시 메시지로 전송됩니다.
- 받은 모든 메시지는 내 쪽지함에서 확인할 수 있습니다.

```kotlin
// MyLikeActivity.kt

    // 현재 사용자와 상대방이 서로 좋아요 했는지 확인
    private fun checkMatching(otherUid : String) {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 다른 사용자 정보 로그로 출력
                Log.d(TAG, otherUid)
                Log.e(TAG, dataSnapshot.toString())

                // 서로 좋아요 한 상태 아니면 메시지 못 보냄
                if(dataSnapshot.children.count() == 0) { Toast.makeText(this@MyLikeActivity, "메시지를 보낼 수 없습니다", Toast.LENGTH_LONG).show() }

                // 서로 좋아요 된 상태면 메시지 보냄
                else {

                    // 데이터스냅샷 내 사용자 데이터 출력
                    for (dataModel in dataSnapshot.children) {

                        // 다른 사용자가 좋아요 한 사용자 목록에
                        val likeUserKey = dataModel.key.toString()

                        // 현재 사용자가 포함돼 있으면
                        if(likeUserKey == uid) {

                            // 메시지 입력창 띄움
                            showDialog()

                        }

                    }

                }

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "checkMatching - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(postListener)

    }

    // 현재 사용자의 좋아요 리스트
    private fun getMyLikeList() {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 데이터스냅샷 내 사용자 데이터 출력 -> 현재 사용자가 좋아하는 사용자들의 UID를 myLikeList에 넣음
                for(dataModel in dataSnapshot.children) { myLikeListUid.add(dataModel.key.toString()) }

                // 전체 사용자 정보 받아옴
                getUserDataList()

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getMyLikeList - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userLikeRef.child(uid).addValueEventListener(postListener)

    }

    // 전체 사용자 정보
    private fun getUserDataList() {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 데이터스냅샷 내 사용자 데이터 출력
                for(dataModel in dataSnapshot.children) {

                    // 다른 사용자 정보 받아옴
                    val user = dataModel.getValue(UserDataModel::class.java)

                    // 전체 사용자 중
                    if(myLikeListUid.contains(user?.uid)) {

                        // 현재 사용자가 좋아하는 사용자의 정보만 추가
                        myLikeList.add(user!!)

                    }

                }

                // 동기화(새로고침) -> 리스트 크기 및 아이템 변화를 어댑터에 알림
                listviewAdapter.notifyDataSetChanged()
                Log.d(TAG, myLikeList.toString())

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getUserDataList - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userInfoRef.addValueEventListener(postListener)

    }

    // 푸시 메시지
    private fun testPush(notification : PushNotification) = CoroutineScope(Dispatchers.IO).launch {

        // 레트로핏 API 이용
        RetrofitInstance.api.postNotification(notification)

    }

    // 메시지 보내는 대화창
    private fun showDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("메시지 보내기")
        val mAlertDialog = mBuilder.show()

        val btn = mAlertDialog.findViewById<Button>(R.id.sendBtn)
        val text = mAlertDialog.findViewById<EditText>(R.id.sendText)

        // 메시지 보내기 버튼을 클릭하면 푸시 메시지 발송
        btn?.setOnClickListener {

            val msgText = text!!.text.toString()
            val msgModel = MsgModel(MyInfo.myNickname, msgText)

            // 파이어베이스에 메시지 업로드
            FirebaseRef.userMsgRef.child(getterUid).push().setValue(msgModel)

            val notiModel = NotiModel(MyInfo.myNickname, msgText)
            val pushModel = PushNotification(notiModel, getterToken)

            // 푸시 메시지
            testPush(pushModel)

            // 대화창
            mAlertDialog.dismiss()

        }

    }
```
```kotlin
// MyMsgActivity.kt

    // 내 메시지 불러오기
    private fun getMyMsg() {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 중복 출력 방지 위해 메시지 목록 비워줌
                msgList.clear()

                // 데이터스냅샷 내 사용자 데이터 출력
                for(dataModel in dataSnapshot.children) {

                    // 메시지 모델에서
                    val msg = dataModel.getValue(MsgModel::class.java)
                    msgList.add(msg!!)
                    Log.d(TAG, msg.toString())

                }

                // 역순 정렬
                msgList.reverse()

                // 동기화(새로고침) -> 리스트 크기 및 아이템 변화를 어댑터에 알림
                listviewAdapter.notifyDataSetChanged()

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getMyMsg - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userMsgRef.child(FirebaseAuthUtils.getUid()).addValueEventListener(postListener)

    }
```
```kotlin
// MsgAdapter.kt

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
```
```kotlin
// FirebaseService.kt

    // 새 토큰
    override fun onNewToken(token: String) { super.onNewToken(token) }

    // 메시지 받기
    override fun onMessageReceived(message: RemoteMessage) {

        super.onMessageReceived(message)

        val title = message.data["title"].toString()
        val body = message.data["content"].toString()

        // 알림 채널 시스템에 등록
        createNotificationChannel()

        // 알림 보내기
        sendNotification(title, body)

    }

    // 알림 채널 시스템에 등록
    private fun createNotificationChannel() {

        // API 26 이상에서만 NotificationChannel을 생성(API 25 이하는 지원하지 않음)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply { description = descriptionText }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)

        }

    }

    // 푸시 알림(메시지)
    private fun sendNotification(title : String, body : String) {

        var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_baseline_local_fire_department_24)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) { notify(123, builder.build()) }

    }
```
```kotlin
// RetrofitInstance.kt

// 레트로핏 인스턴스
class RetrofitInstance {

    companion object {

        // (레트로핏) 객체 지연초기화
        private val retrofit by lazy {

            // 빌더
            Retrofit.Builder()

                // 통신할 서버 URL
                .baseUrl(BASE_URL)

                // 서버로부터 받아온 데이터(컨텐츠)를 원하는 타입으로 바꿈
                .addConverterFactory(GsonConverterFactory.create())

                // 빌드
                .build()

        }

        // (레트로핏) 객체 생성
        val api = retrofit.create(NotiAPI::class.java)

    }

}
```

---

## DB 설계(Firebase Realtime Database)

![db_all](https://user-images.githubusercontent.com/68595933/193630716-0453af4d-3594-403f-afc1-4cf27f1fe755.png)

- DB 전체

![info_table](https://user-images.githubusercontent.com/68595933/193632032-1c93954b-f1d6-449c-b267-b582e26d13bc.png)

- userInfo(회원정보)

![like_table](https://user-images.githubusercontent.com/68595933/193631821-ab4a9337-5068-451d-9956-a69e6445b5db.png)

- userLike(좋아요)

![msg_table](https://user-images.githubusercontent.com/68595933/193632246-7886ac2f-3dbc-410b-821b-567284962d79.png)

- userMsg(메시지)

---

## 피드백

### 문제점

1. 메인 페이지를 제외한 나머지 화면에 액션바가 없어 현재 페이지의 기능을 알기 어려움.
2. 좋아요 설정한 사용자를 삭제할 수 없음.


### 개선점

1. 모든 페이지에 액션바 및 뒤로가기 버튼 추가. - 22. 10. 05 업데이트
2. 메인 페이지에 좋아요 취소 기능 추가. - 22. 10. 06 업데이트

---

## 저작권

이 프로젝트는 MIT 라이센스에 따라 라이센스가 부여됩니다. 자세한 내용은 [LICENSE.md](LICENSE.md) 파일을 참조하십시오.

---

## 레퍼런스

- [참고 강의 바로가기][참고]

<!-- 링크 -->

[참고]: https://www.inflearn.com/course/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%BD%94%ED%8B%80%EB%A6%B0-%EB%8D%B0%EC%9D%B4%ED%8C%85%EC%95%B1/dashboard
