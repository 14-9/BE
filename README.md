# 🍷 We Luv Wine

> 항해99 14기 9조 주특기 mini 프로젝트 <br>
> 와인을 좋아하는 사람들 모두 모여라~ 내게 좋은 추억을 전해준 와인을 너에게도 알려줄게!<br>
> 와인에 대한 이야기를 나눌 수 있는 공간입니다. 

# ⚙️ Tech Stack
<br>
<div align=center> 
<img src="https://img.shields.io/badge/JAVA-E34F26?style=for-the-badge&logo=JAVA&logoColor=white">
<img src="https://img.shields.io/badge/PYTHON-3776AB?style=for-the-badge&logo=PYTHON&logoColor=white">
<img src="https://img.shields.io/badge/SPRING BOOT-6DB33F?style=for-the-badge&logo=SPRING BOOT&logoColor=white">
<img src="https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white">
<img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
<img src="https://img.shields.io/badge/redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
<img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white">
<img src="https://img.shields.io/badge/amazonrds-527FFF?style=for-the-badge&logo=amazonrds&logoColor=white">
<img src="https://img.shields.io/badge/amazonec2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white">
<img src="https://img.shields.io/badge/amazonaws-232F3E?style=for-the-badge&logo=amazonaws&logoColor=white">
<img src="https://img.shields.io/badge/junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white">
</div>

<br>

## 💻 9조의 S.A가 궁금하다면?
https://kaput-birth-81b.notion.site/S-A-b5c42c60c43c4298bd123ecd03e12de1
<br>

## 📖 ERD
<div align="center">

![image](https://user-images.githubusercontent.com/102853354/236878245-8fe36ff4-9e56-470a-a5f5-be1004e04489.png)

</div>
  <br>

## 🧑‍🤝‍🧑 회원 관리 관련 기능
  1. 회원 가입 API<br>
    - memberId, password, nickname을 Client에서 전달받습니다.<br>
    - memberId는 `5~20자의 영문 소문자, 숫자와 특수기호(_),(-)`만 사용 가능합니다.<br>
    - password는 `8~16자 영문 대 소문자, 숫자, 특수문자`만 가능합니다.<br>
    - nickname은 `2~8자 이내`로 사용가능합니다.<br>
    - 중복된 Id, nickname은 사용할 수 없습니다.<br>
  <br>
    
  2. 로그인 / 로그아웃 API<br>
    - 로그인 성공 시, Access Token과 Refresh Token을 발급하고 헤더에 토큰을 추가합니다.<br>
    - Redis를 적용하여 로그아웃을 진행합니다.<br>
  <br>
  
  3. 마이페이지 API<br>
    - 마이페이지에서 내가 쓴 글의 목록을 조회할 수 있습니다.
  <br>
   
  
## 🏆 와인 리뷰 기능
  1. 와인 검색 API<br>
    - 와인 이름을 키워드로 조회할 수 있습니다.<br>
    - Full name을 검색하지 않아도 됩니다. 일부 키워드로 검색이 가능합니다.
  <br>
  
  2. 리뷰 작성 API<br>
    - 내가 좋아하는 와인에 리뷰를 업로드할 수 있습니다. 
  <br>
    
  3. 리뷰 수정 API<br>
    - 내가 작성한 리뷰에 한하여 수정할 수 있습니다.
  <br>
    
  4. 리뷰 삭제 API<br>
    - 내가 작성한 리뷰에 한하여 삭제할 수 있습니다.
  <br>
    
  5. 와인 좋아요 API<br>
    - 내가 좋아하는 와인에 좋아요 버튼을 눌러 추천할 수 있습니다.<br>
    - 이미 누른 좋아요 버튼을 다시 누른다면 추천은 취소됩니다.
  <br>
  
  6. 추천 와인 조회 API<br>
    - 추천된 와인 중 추천수가 가장 많은 와인은 `와인의 전당`에 올라갑니다.<br>
    - `와인의 전당`에 오른 와인의 추천수가 같다면 고유 식별 코드를 기준으로 정렬되어 올라갑니다.
  <br>
  
 ## 예외처리
 ```
 - 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 때는 "토큰이 유효하지 않습니다." 라는 에러메시지와 statusCode: 400을 반환합니다.
- 토큰이 있고, 유효한 토큰이지만 해당 사용자가 작성한 게시글/댓글이 아닌 경우에는 “작성자만 삭제/수정할 수 있습니다.”라는 에러메시지와 statusCode: 400을 반환합니다.
- DB에 이미 존재하는 memberId로 회원가입을 요청한 경우 "중복된 memberId 입니다." 라는 에러메시지와 statusCode: 400을 반환합니다.
- 로그인 시, 전달된 memberId로 password 중 맞지 않는 정보가 있다면 "회원을 찾을 수 없습니다."라는 에러메시지와 statusCode: 400을 반환합니다.
- 회원가입 시 memberId와 password의 구성이 알맞지 않으면 에러메시지와 statusCode: 400을 반환합니다.
 ```


