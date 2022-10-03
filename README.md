# 데이트 DAY2

> 데이트/매칭 앱 프로젝트

![day2_cover](https://user-images.githubusercontent.com/68595933/193454768-3bb5a2d3-6d1a-4605-82d0-6c94e54c6130.png)

## 시작

- FCM을 이용한 데이트/매칭 프로젝트입니다.

---

## 개발

### 기간

- 22.05.30.~22.07.14.

### 목표

- 코틀린으로 주요 기능을 구현해 봅니다.
- FCM을 사용해 봅니다.

### 사용

- Kotlin
- Jetpack
- Firebase

---

## 기능

### 1. 인증(Firebase Auth)

![auth](https://user-images.githubusercontent.com/68595933/193455141-2077c40d-ea35-4e70-92fe-7bc9084b77bf.PNG)

- 1.1 로그인/로그아웃
  - 로그인 버튼을 클릭하면 유효성 검사 후 로그인합니다.
  - 비회원로그인 버튼을 클릭하면 회원가입 없이 임의로 로그인이 가능하나, 한 번 로그아웃하면 같은 계정으로 재로그인할 수 없습니다.
  - 로그아웃 버튼을 클릭하면 로그아웃 후 IntroActivity로 이동합니다.

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

- 1.2 회원가입
  - 회원가입 버튼을 클릭하면 유효성 검사 후 회원가입합니다.

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

![main](https://user-images.githubusercontent.com/68595933/193457384-01def4a5-3c8f-4bcd-951e-22be19d7c28e.PNG)

- 현재 사용자와 다른 성별인 사용자의 프로필만 카드 더미 형식으로 구현합니다.
- 카드는 양쪽으로 넘길 수 있으며, 왼쪽으로 넘기면 아무런 변화 없이 다음 프로필로 넘어갑니다.
- 카드를 오른쪽으로 넘기면 하트 애니메이션이 활성화되며 좋아요 설정됩니다.
- 모든 프로필을 확인하면 자동으로 새로고침 됩니다.

```kotlin
// MainActivity.kt

        // 카드스택뷰
        manager = CardStackLayoutManager(baseContext, object: CardStackListener {

            // 카드 넘기기
            override fun onCardSwiped(direction: Direction?) {

                // 왼쪽(관심없음)
//                if(direction == Direction.Left) {}

                // 오른쪽(좋아요)
                if(direction == Direction.Right) {

                    // 하트 애니메이션
                    binding.ltAnimation.playAnimation()

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

            override fun onCardDragging(direction: Direction?, ratio: Float) {}
            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View?, position: Int) {}
            override fun onCardDisappeared(view: View?, position: Int) {}

        })

        // 카드스택어댑터에 데이터 넘김
        cardStackAdapter = CardStackAdapter(baseContext, usersDataList)
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = cardStackAdapter
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

- 3.1 좋아요
  - 좋아요 목록에서 내가 좋아요 한 사용자를 확인합니다.
  - 좋아요를 취소할 수 있습니다.

```kotlin
// 코드3
```

![match](https://user-images.githubusercontent.com/68595933/193455842-c8b83ef5-13d2-42aa-bd11-33d02372e0d7.PNG)

- 3.2 매칭
  - 현재 사용자와 상대방이 서로 좋아요한 상태면 매칭을 알리는 푸시 메시지가 전송됩니다.
  - 매칭된 사용자는 서로 메시지를 주고받을 수 있습니다.

```kotlin
// 코드3
```

### 4. 메시지 보내기(Firebase Cloud Messaging)

![push_msg1](https://user-images.githubusercontent.com/68595933/193456208-7b78b73b-0174-42b7-a262-1d8d61c3d5a1.PNG)
![push_msg2](https://user-images.githubusercontent.com/68595933/193456209-a6b02f97-b181-4603-9ef0-b4eca9abb2f7.PNG)
![push_msg3](https://user-images.githubusercontent.com/68595933/193456211-0ec8986e-db0f-42ad-ac38-b0a5ebe5df96.PNG)

- 메시지를 보내면 해당 내용이 푸시 메시지로 전송됩니다.
- 받은 모든 메시지는 내 쪽지함에서 확인할 수 있습니다.

```kotlin
// 코드4
```

---

## 피드백

### 문제점

1. ~함.

### 개선점

1. ~할 것.

---

## 저작권

이 프로젝트는 MIT 라이센스에 따라 라이센스가 부여됩니다. 자세한 내용은 [LICENSE.md](LICENSE.md) 파일을 참조하십시오.

---

## 레퍼런스

- [참고 강의 바로가기][참고]

<!-- 링크 -->

[참고]: https://www.inflearn.com/course/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%BD%94%ED%8B%80%EB%A6%B0-%EB%8D%B0%EC%9D%B4%ED%8C%85%EC%95%B1/dashboard
