# 📝 프로젝트 소개


<br/>

> 혼자보다는 같이 하고 싶어하는 이들을 위한 매칭 서비스입니다.
<br/> <br/>
> 개발 인원 : 1명 <br/>
> 개발 기간 : 2024.03.16 ~ 2024.06.16

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

![image (1)](https://github.com/user-attachments/assets/3ac5876d-c5c4-415c-beeb-2b6f713350ca)
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

## <code> JWT 3번의 로직 개선, Redis 사용 이유 </code>
### JWT의 무상태성의 이점과 보안에서의 단점 보완 


jwt 기반 로그인을 구현하면서 무상태성의 이점과 보안에서의 단점을 보완하기 위해 총 3번의 모델 변화가 있었습니다.

### 1차 모델
![image](https://github.com/sonnbeom/play_spring/assets/127067296/f2a17167-1cce-4916-a1e6-6016a1f26bfb)
### 1차 모델 장점
- Access Token의 만료 기한을 짧게 설정하여 보안성 증가

### 1차 모델 단점
>
1. Refresh Token을 발급하고 인증할 때 유저 정보를 DB에서 조회 → 네트워크 사용량 증가 및 응답 속도 저하 -> JWT의 이점인 무상태성을 활용할 수 없음.
2. Access Token과 Refresh Token을 동시에 탈취시 보안상 위험 존재

### 2차 모델
![image](https://github.com/sonnbeom/play_spring/assets/127067296/aebc50d8-0b08-4dc1-b588-7fb8089b56b4)

### 2차 모델 장점
-  DB를 조회하던 방식에 비해 성능상 이점 존재
-  Refresh Token 만료 시간이 지나면 자동으로 Redis에서 삭제
-  블랙리스트 유저의 강제 로그아웃 기능

### 2차 모델 단점
- Access Token과 Refresh Token을 동시에 탈취시 보안상 위험 존재 Refresh Token 만료기간 전까지 재발급 가능

-  Refresh Token을 DB에서 조회한다면 JWT의 무상태성의 이점을 살릴 수 없다고 판단
-  -> Refresh Token을 1차 캐시인 Redis에 저장하여 응답 속도를 개선
-  RDB에 저장하게 된다면 만료시간에 대한 로직을 구현해야 했지만 Redis를 사용 시에는 이러한 로직을 직접 구현할 필요가 없음

![image](https://github.com/sonnbeom/play_spring/assets/127067296/800b09d7-8541-4804-9910-1c39d6c18dd0)
- #### Access Token, Refresh Token 동시에 탈취되었을 때 문제방지를 위해 Access Token이 재발급될 때마다 Refresh Token도 재발급하는 방법 선택



### Access Token과 Refresh Token을 어디에 저장해야 하는지에 대한 사고 과정
## <code> JWT Access Token과 Refresh Token을 저장 위치 이슈 </code>
JWT는 무상태성이라는 이점이 있지만 보안 방식에서 단점이 있다고 생각했습니다.

그렇기에 JWT를 어디에 보관해야하는지에 대한 고민했고 다음과 같은 내용을 기반으로 블로그에 기술했습니다.
   [블로그](https://velog.io/@devson_42/JWT-Access-Token%EA%B3%BC-Refresh-Token%EC%9D%84-%EC%96%B4%EB%94%94%EC%97%90-%EC%A0%80%EC%9E%A5%ED%95%B4%EC%95%BC-%ED%95%A0%EA%B9%8C)

1. Access Token과 Refresh Token의 저장소가 같으면 안된다.
2. 각 저장 위치에 저장했을 시에 어떠한 장단점 작성
3. CSRF, XSS 공격 취약성 고려

## <code> 결합도와 캡슐화 고려하여 코드 작성 </code>
도메인 내 getter, setter를 사용하게 된다면 캡슐화를 약화시키고 높은 결합도를 야기할 수 있다고 생각합니다.

getter, setter를 사용하게 되면 왜 결합도가 높아지고 캡슐화는 약해지는지, 어떠한 방법으로 코드를 작성해야 하는지 등을 블로그에 기술하였습니다.


   [getter, setter를 왜 지양해야하는가](https://velog.io/@devson_42/setter-getter%EB%A5%BC-%EC%99%9C-%EC%A7%80%EC%96%91%ED%95%B4%EC%95%BC-%ED%95%98%EB%82%98%EC%9A%94) <br> [해결방법](https://velog.io/@devson_42/%EB%8B%98%EC%95%84-%EA%B7%B8-getter-setter%EB%A5%BC-%EC%93%B0%EC%A7%80-%EB%A7%88%EC%98%A4...%EC%BA%A1%EC%8A%90%ED%99%94%EC%99%80-%EA%B2%B0%ED%95%A9%EB%8F%84%EB%A5%BC-%EA%B3%A0%EB%A0%A4%ED%95%B4%EB%B3%B4%EC%9E%90-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81-%ED%95%98%EA%B8%B0)




<table>
  	<tr>
  		<td align="center">
      			Before
    		</td>
		<td>
      			도메인 내의 정보가 다른 계층에서 은닉되지 않아 객체 간의 결합도가 높아져 모듈화와 재사용성이 저하되는 상황
    		</td>
  	</tr>
	<tr>
		<td align="center">
			After
		</td>
		<td>
   			도메인 내의 정보를 다른 계층에서 활용 못하게 도메인 내에 DTO로 변환하는 코드 작성
    		</td>
	</tr>
</table>

<pre>
<code>
 [Before]
    @Override
    public List<ChatMessageResponseDto> findByRoom(ChatRoom chatRoom) {
        List<ChatMessage> chatMessage = customChatMessageRepository.findByRoomNumber(chatRoom);
        List<ChatMessageResponseDto> dtoList = new ArrayList<>();
        for (ChatMessage c : chatMessage){
        ChatMessageResponseDto dto = ChatMessageResponseDto.builder()
        	.id(c.getId())
            .msg(c.getMessage())
            ..생략
 			.build();
            dtoList.add(dto);
        }
        return dtoList;
    }}
</code>
</pre>

<pre>
<code>
 [After]
   @Override
    public List<ChatMessageResponseDto> findByRoom(ChatRoom chatRoom) {
        List<ChatMessage> chatMessage = customChatMessageRepository.findByRoomNumber(chatRoom);
        List<ChatMessageResponseDto> dtoList = new ArrayList<>();
        for (ChatMessage c : chatMessage){
            ChatMessageResponseDto chatDto = c.toDto();
            dtoList.add(chatDto);
        }
        return dtoList;
    }
    
    //도메인 내 메소드
    public ChatMessageResponseDto toDto(){
        return ChatMessageResponseDto.builder()
                .id(id)
                .msg(msg)
                .dateTime(getCreatedAt())
                .build();
    }
 
</code>
</pre>

<hr/>

<table>
  	<tr>
  		<td align="center">
      			Before
    		</td>
		<td>
      			도메인 내 setter를 사용함으로써 무결성이 위반되는 상황
    		</td>
  	</tr>
	<tr>
		<td align="center">
			After
		</td>
		<td>
   			도메인 내 메소드를 만들어 다른 계층에서 임의로 도메인 내의 정보를 변경할 수 없음
    		</td>
	</tr>
</table>

<pre>
<code>
 [Before]
    @Override
    public ResponseComment update(RequestCommentUpdate commentUpdate, String email) {
        Member member = memberService.findByEmail(email);
        Comment comment = findById(commentUpdate.getCommentId());
        //업데이트 권한 확인
        comment.checkUpdateAuthorization(member);
        comment.setContent(commentUpdate.getContent());
        return comment.toDto();
    }
</code>
</pre>

<pre>
<code>
 [After]
    @Override
    public ResponseComment update(RequestCommentUpdate commentUpdate, String email) {
        Member member = memberService.findByEmail(email);
        Comment comment = findById(commentUpdate.getCommentId());
        //업데이트 권한 확인
        comment.checkUpdateAuthorization(member);
        comment.update(commentUpdate);
        return comment.toDto();
    }
    
 //comment 도메인 내 메소드
    public void update(RequestCommentUpdate commentUpdate) {
        this.content = commentUpdate.getContent();
    }
 
</code>
</pre>



<details>
<summary>코드 확인:  권한 체크 로직 구현시 결합도 증가 개선 </summary>
<table>
  	<tr>
  		<td align="center">
      			Before
    		</td>
		<td>
      			권한이 있는지 체크하는 로직 구현시, 도메인 내의 정보를 다른 계층에서 호출하여 유지보수 및 수정의 어려움이 있다고 판단
    		</td>
  	</tr>
	<tr>
		<td align="center">
			After
		</td>
		<td>
   			다른 계층에서 도메인 내의 정보를 알지 못하게 도메인 내 로직 구현  
    		</td>
	</tr>
</table>

<pre>
<code>
 [Before]
  @Override
  public void delete(RequestCommentDelete commentDelete, String email) {
      Member member = memberService.findByEmail(email);
      Comment comment = findById(commentDelete.getCommentId());
      if (comment.getMember().equals(member)){
          comment.checkDeleteAuthorization(member);
          commentRespository.delete(comment);
      }
  }
</code>
</pre>

<pre>
<code>
 [After]
  @Override
  public void delete(RequestCommentDelete commentDelete, String email) {
      Member member = memberService.findByEmail(email);
      Comment comment = findById(commentDelete.getCommentId());
      comment.checkDeleteAuthorization(member);
      commentRespository.delete(comment);
  }

//댓글 도메인 내에 메소드
   public void checkDeleteAuthorization(Member member) {
        if (!this.member.equals(member)){
            throw new CommentDeleteAuthorizationException("권한이 없는 멤버가 댓글 삭제를 시도합니다.", HttpStatus.FORBIDDEN);
        }
    }

</code>
</pre>

</details>





<details>
<summary>코드 확인하기: 다른 객체들로 책임이 분산되어 코드 리팩토링</summary>

<table>
  	<tr>
  		<td align="center">
      			Before
    		</td>
		<td>
      		다른 객체들로 책임이 분산되어 객체지향의 적합하지 않은 코드 작성
    </td>
  	</tr>
	<tr>
		<td align="center">
			After
		</td>
		<td>
   			도메인 내 로직 작성
    		</td>
	</tr>
</table>

<pre>
<code>
 [Before]
  for(ChatMessage chatMessage : chatList){
        if (map.containsKey(chatMessage.getChatRoom())){
            ChatRoomsWithChatsDto chatRoomsWithChatsDto = map.get(chatMessage.getChatRoom());
        }
      }
</code>
</pre>

<pre>
<code>
 [After]
  private List<ResponseFriendshipWithImg> mappingFriendshipWithImg(List<Friendship> friendshipList, Map<Member, MemberImage> map){
        List<ResponseFriendshipWithImg> friendshipWithImgList = new ArrayList<>();
        //이미지가 존재하는 멤버라면 이미지를 포함해서 dto로 변환 이미지가 없다면 이미지 디폴트 값으로 dto로 변환
        for (Friendship friendship : friendshipList){

            Optional<ResponseMemberImg> img =  friendship.isExistSenderImg(map);
            
 //도메인 내에 메소드
     public Optional<ResponseMemberImg> isExistSenderImg(Map<Member, MemberImage> map) {
        if (map.containsKey(sender)){
            return Optional.of(map.get(sender).toDto());
        }
        else return Optional.empty();
    }
 
</code>
</pre>

</details>





<hr/>




