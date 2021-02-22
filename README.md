# Welcome to PETMILY

![](images/README/logologo.png)

> 펫밀리 (PETMILY) 웹사이트

🏠 [Homepage](https://i4a403.p.ssafy.io/)

> 펫밀리 시연영상

🏠 [Petmily_Video](https://www.youtube.com/watch?v=b6qmTilhApQ)



## 👨‍👩‍👦 Team members

### 신상훈(Shin Sanghun)

> [@github](https://github.com/sw0817)

- Team Leader
- Frontend
- Presentation

### 신윤식(Shin Yoonsik)

> [@github](https://github.com/)

- Backend
- Server

### 이도건(Lee Dogun)

> [@github](https://github.com/)

- Frontend
- UCC

### 박다윤(Park Dayoon)

> [@github](https://github.com/)

- Backend
- Database

### 박서영(Park Suyoung)

> [@github](https://github.com/)

- Backend
- Frontend



## Project Intro

- 진행 기간 : 2021.01.11 ~ 2021.02.19

- 목표 / 프로젝트 선정 배경

  - 나의 반려동물들을 한 계정에서 쉽게 관리 & 공유하고,
  - 다른 사람의 반려동물을 쉽게 구경하고,
  - 반려동물들의 일상을 한 눈에 파악할 수 있는 다이어리를 작성할 수 있는

  - 신개념 반려동물 전용 커뮤니티 만들기

- 웹 서비스 이름 : Petmily (펫밀리)

  - 반려동물과의 일상이야기, 신개념 반려동물 SNS 서비스



## Tech Stack

[![StackShare](http://img.shields.io/badge/tech-stack-0690fa.svg?style=flat)](https://stackshare.io/sw0817/petmily)

<details>
    <summary>Front 자세히 살펴보기</summary>
    <ul>
        <li>Vue: 2.6.12</li>
        <li>Vue / CLI: 4.5.8</li>
        <li>Vuex: 3.4.0</li>
        <li>npm: 6.14.10</li>
        <li>axios: 0.21.1</li>
    </ul>
</details>

<details>
    <summary>Back 자세히 살펴보기</summary>
    <ul>
        <li>Swagger</li>
        <li>Spring Boot</li>
        <li>Spring Security</li>
        <li>MySQL</li>
        <li>NGINX</li>
        <li>E2C</li>
    </ul>
</details>

<details>
    <summary>ETC 자세히 살펴보기</summary>
    <ul>
        <li>Json Web Token</li>
        <li>GitLab</li>
        <li>Jira</li>
        <li>Webex</li>
    </ul>
</details>



## ⚙️ Install and Usage

### Frontend

- frontend 폴더로 들어와 필요한 패키지를 설치합니다.

  - ```bash
    $ npm i
    ```

- frontend 폴더 (vue 프로젝트)를 실행합니다.

  - ```bash
    $ npm run serve
    ```

### Backend

- Java (Open JDK 14)를 설치합니다.
- Maven을 설치합니다.
- VS Code 및 관련 Spring Boot Extension Pack 설치합니다.

- DB 테이블을 생성합니다.



## ⭐️ 주요 기능

### Feed

> 유저들의 반려동물 일상 공유 공간



### 👀 페이지 소개





## 배포

> 펫밀리 (PETMILY) 웹사이트

🏠 [Homepage](https://i4a403.p.ssafy.io/)

AWS(EC2) 서버에 배포하였다.



## 호환성

- 크롬(Chrome) & 안드로이드 완벽 구동
- IE 구동 불가능
- iOS 이미지(로고) 크기가 변경되는 버그
- iOS에서 실행 시 일대일 대화 시간이 NaN으로 나오는 문제



## 알려진 버그

**☑️** 가로크기 1100px 이상에서 피드 작성 시, 사진에 특정 필터링 적용 시 사진 크기가 늘어나는 버그

**☑️** 피드 작성에서 동영상 업로드 시, 필터링 Preview 사진이 작은 네모칸으로 출력되는 버그

**☑️** 피드 태그 및 댓글 알람 모두지우기 실행 시, 해당 알람 내용이 다시 나타나는 버그



##### *Copyright* © 2021 SSAFY 4기 Team PETMILY
