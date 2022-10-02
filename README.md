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

```javascript
// 코드1
```

- 1.2 회원가입
  - 회원가입 버튼을 클릭하면 유효성 검사 후 회원가입합니다.

```javascript
// 코드1
```

### 2. 메인(Card Stack View)

![main](https://user-images.githubusercontent.com/68595933/193457384-01def4a5-3c8f-4bcd-951e-22be19d7c28e.PNG)

- 현재 사용자와 다른 성별인 사용자의 프로필만 카드 더미 형식으로 구현합니다.
- 카드는 양쪽으로 넘길 수 있으며, 왼쪽으로 넘기면 아무런 변화 없이 다음 프로필로 넘어갑니다.
- 카드를 오른쪽으로 넘기면 하트 애니메이션이 활성화되며 좋아요 설정됩니다.
- 모든 프로필을 확인하면 자동으로 새로고침 됩니다.

```javascript
// 코드3
```

### 3. 좋아요 및 매칭(Firebase Cloud Messaging)

![my_like](https://user-images.githubusercontent.com/68595933/193457538-67f80dfb-51c7-40e9-b330-67252af42a72.PNG)

- 3.1 좋아요
  - 좋아요 목록에서 내가 좋아요 한 사용자를 확인합니다.
  - 좋아요를 취소할 수 있습니다.

```javascript
// 코드3
```

![match](https://user-images.githubusercontent.com/68595933/193455842-c8b83ef5-13d2-42aa-bd11-33d02372e0d7.PNG)

- 3.2 매칭
  - 현재 사용자와 상대방이 서로 좋아요한 상태면 매칭을 알리는 푸시 메시지가 전송됩니다.
  - 매칭된 사용자는 서로 메시지를 주고받을 수 있습니다.

```javascript
// 코드3
```

### 4. 메시지 보내기(Firebase Cloud Messaging)

![push_msg1](https://user-images.githubusercontent.com/68595933/193456208-7b78b73b-0174-42b7-a262-1d8d61c3d5a1.PNG)
![push_msg2](https://user-images.githubusercontent.com/68595933/193456209-a6b02f97-b181-4603-9ef0-b4eca9abb2f7.PNG)
![push_msg3](https://user-images.githubusercontent.com/68595933/193456211-0ec8986e-db0f-42ad-ac38-b0a5ebe5df96.PNG)

- 메시지를 보내면 해당 내용이 푸시 메시지로 전송됩니다.
- 받은 모든 메시지는 내 쪽지함에서 확인할 수 있습니다.

```javascript
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

[참고]: https://www.inflearn.com/course/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%BB%A4%EB%AE%A4%EB%8B%88%ED%8B%B0%EC%95%B1/dashboard
