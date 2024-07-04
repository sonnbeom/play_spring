# 📝 프로젝트 소개


<br/>

> 혼자보다는 같이 하고 싶어하는 이들을 위한 매칭 서비스입니다.
<br/> <br/>
> 개발 인원 : 1명 <br/>
> 개발 기간 : 2024.03.16 ~ 2023.06.16

## 기술 스택

* 서비스: Spring Boot, Java, JPA, QueryDSL, Spring Security, Swagger
* 데이터베이스: MySQL, Redis
* 인프라: Jenkins, AWS(ec2)
<br/>

<br/>


## 목차

1. 시스템 아키텍처
2. API 문서 (스웨거)
3. 폴더 구조
4. ERD
5. 통합 테스트
6. 리팩토링
7. 트러블 슈팅
<br/>

<br/>





# 🔨 시스템 아키텍처

![image](https://github.com/SesacAcademy/SesacAnimal/assets/55624470/1ffa4dcf-22c2-4451-a8e0-f7458b4f53b1)

<br/>

# 🗒️ ERD 설계

- [ERD 링크](https://www.erdcloud.com/d/PfonDH9Lehu9mzAMT)

![image](https://github.com/sonnbeom/play_spring/assets/127067296/2a1990a1-cd88-401d-89df-7e3228e6b22f)

<br/>

# 📜  API 문서(스웨거)

![image](https://github.com/sonnbeom/play_spring/assets/127067296/a52e9db8-55da-49b6-ba81-f436947b80c9)

![image](https://github.com/sonnbeom/play_spring/assets/127067296/4da8c6fa-487b-46e4-8d66-ae6976b953cd)

# 📁 디렉토리 구조

- [디렉토리 구조 링크](https://github.com/sonnbeom/play_spring/wiki/%F0%9F%93%81-%EB%94%94%EB%A0%89%ED%86%A0%EB%A6%AC-%EA%B5%AC%EC%A1%B0)


![image](https://github.com/sonnbeom/play_spring/assets/127067296/b1e79dde-06c5-4fec-9c18-9212ab2fbefc)

# 📁 도메인별 단위 테스트

- 📌 Code - [[테스트 코드]](https://github.com/sonnbeom/play_spring/tree/master/src/test/java/com/example/play)



![image](https://github.com/sonnbeom/play_spring/assets/127067296/960e2810-28ba-4884-904b-4be29d83de42)

![image](https://github.com/sonnbeom/play_spring/assets/127067296/0731a3d6-20f8-4782-8865-b2d09155f637)

<br/>

# 💡 주요 업무

#### 👩‍👧 회원 (박성수)

- (Spring Security 활용) JWT 토큰 기반 방식의 로그인 기능
  - 📌 Code - [[로그인_컨트롤러]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/auth/controller/AuthController.java#L42) 
  [[JWT_발급_서비스]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/jwt/service/JwtService.java#L43) [[JWT_필터]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/jwt/filter/JwtTokenFilter.java#L26)[[JWT_예외_핸들러]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/jwt/exceptionHandler/JwtExceptionHandler.java#L16)


- 소셜 로그인 기능(구글, 네이버)
  - Code - 📌 [[소셜로그인_서비스]](https://github.com/SesacAcademy/SesacAnimal/blob/dev/src/main/java/com/project/animal/member/controller/LoginController.java#L86)

- 웹소켓을 활용한 실시간 채팅 구현
  - 📌 Code - [[웹소켓 핸들러]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/chat/handler/WebSocketHandler.java#L24) [[커스텀_핸드셰이크_인터셉터]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/chat/interceptor/CustomHandshakeInterceptor.java#L15) [[채팅 레포지토리]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/chat/repository/CustomChatMessageRepositoryImpl.java#L31)

- 채팅방 활용 채팅 관리
    - 📌 Code - [[채팅방_컨트롤러]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/chatroom/controller/ChatRoomController.java#L23) [[채팅방_서비스]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/chatroom/service/ChatRoomServiceImpl.java#L40) [[채팅방_레포지토리]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/chatroom/repository/CustomChatRoomRepositoryImpl.java#L26)

- 동적쿼리를 활용 계층구조의 댓글, 대댓글 구현
  - 📌 Code - [[댓글_서비스]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/comment/service/CommentServiceImpl.java#L56) [[댓글 레포지토리]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/comment/repository/CustomCommentRepositoryImpl.java#L26)

- 친구 요청(수락, 삭제) 및 조회 기능
    - 📌 Code - [[친구요청_컨트롤러]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/friendship/controller/FriendshipController.java#L31) [[친구요청_서비스]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/friendship/service/FriendshipServiceImpl.java#L85) [[친구요청_레포지토리]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/friendship/repository/FriendshipCustomRepositoryImpl.java#L28)
- 오브젝트스토리지를 활용한 도메인별 이미지 업로드
    - 📌 Code - [[멤버_프로필_서비스]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/image/service/MemberImgServiceImpl.java#L34) [[포스트_이미지_서비스]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/image/service/PostImgServiceImpl.java#L36) [[미니오_서비스]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/global/common/provider/MinioServiceProvider.java#L34)
- 동적쿼리를 사용하여 게시글 검색, 정렬 기능 구현
    - 📌 Code -  [[게시글 레포지토리]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/post/repository/CustomPostRepositoryImpl.java#L36)
- Redis 활용 Refresh Token을 통한 Access Token, Refresh Token 재발급, 만료시간 설정 -> 자동삭제 
    - 📌 Code -  [[어세스,리프레시_토큰_재발급]](https://github.com/sonnbeom/play_spring/blob/master/src/main/java/com/example/play/auth/service/AuthService.java#L38)
        

- aws ec2 활용한 인프라 구축

- Jenkins CI/CD Pipeline 구축



<br/>

# 🌟 프로젝트 진행 중 고려사항

### JWT의 무상태성의 이점과 보안에서의 단점을 보완
### Spring Security + JWT 3번의 로직 개선, Redis 사용 이유

jwt 기반 로그인을 구현하면서 무상태성의 이점과 보안에서의 단점을 보완하기 위해 총 3번의 모델 변화가 있었습니다.

1차 모델
![image](https://github.com/sonnbeom/play_spring/assets/127067296/f2a17167-1cce-4916-a1e6-6016a1f26bfb)
### 1차 모델 장점
- Access Token의 만료 기한을 짧게 설정하여 보안성 증가

### 1차 모델 단점
1. Refresh Token을 발급하고 인증할 때 유저 정보를 DB에서 조회 → 네트워크 사용량 증가 및 응답 속도 저하 -> JWT의 이점인 무상태성을 활용할 수 없음.
2. Access Token과 Refresh Token을 동시에 탈취시 보안상 위험 존재

![image](https://github.com/sonnbeom/play_spring/assets/127067296/aebc50d8-0b08-4dc1-b588-7fb8089b56b4)

### 2차 모델 장점
1. DB를 조회하던 방식에 비해 성능상 이점 존재
2. Refresh Token 만료 시간이 지나면 자동으로 Redis에서 삭제
3. 블랙리스트 유저의 강제 로그아웃 기능

### 2차 모델 단점
1. Access Token과 Refresh Token을 동시에 탈취시 보안상 위험 존재 Refresh Token 만료기간 전까지 재발급 가능

- #### Refresh Token을 DB에서 조회한다면 JWT의 무상태성의 이점을 살릴 수 없다고 판단했습니다.
- #### 이를 해결하기 위해 Refresh Token을 Redis에 저장하며 응답 속도를 개선시키고자 했습니.
- #### RDB에 저장하게 된다면 만료시간에 대한 로직을 구현해야 했지만 Redis를 사용 시에는 이러한 로직을 직접 구현할 필요가 없었습니다.

![image](https://github.com/sonnbeom/play_spring/assets/127067296/800b09d7-8541-4804-9910-1c39d6c18dd0)
- #### Access Token, Refresh Token 동시에 탈취되었을 때 문제방지를 위해 Access Token이 재발급될 때마다 Refresh Token도 재발급하는 방법을 택했습니다.




## 👩‍💻 리팩토링



