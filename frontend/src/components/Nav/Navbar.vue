<template>
  <div style="font-family: GmarketSansMedium; position: relative" v-if="login">
    <div v-if="alertShow" class="myAlerts">
      <MyAlerts
        @modalOff="myAlerts"
        :user_id="user_id"
      />
    </div>
    <nav class="sticky_nav">
      <ul class="nav_menu">
        <div class="nav_menu_div_logo"><li style="width: 100%"><img @click="moveToFeedMain" class="logo-image" src="@/assets/images/logo-image-cutout.png"></li></div>
        <!-- <div class="search-logo-container">
          <div class="search-logo">
            <button>
              <i class="fas fa-search"></i>
            </button>
          </div>
        </div> -->
        <div class="nav_menu_div">
          <li>
            <img @click="moveToMap" class="icons" src="@/assets/images/icons/map.png">
          </li>
          <li>
            <img @click="moveToDiary" class="icons" src="@/assets/images/icons/diary.png">
          </li>
          <li id="whoareyou" style="position: relative;">
            <img @click="profileModal" class="icons" src="@/assets/images/icons/profile.png">
            <div class="red-dot" v-if="0 < followList.length || 0 < alertCnt || 0 < chatCnt"></div>
            <div class="profileModal" v-if="profileShow">
              <NavSelectModal
                :contents="contents"
                @modalOff="profileModal"
                @myProfile="myProfile"
                @myAlerts="myAlerts"
                @myMessages="myMessages"
                @signOut="signOut"
              />
            </div>
          </li>
        </div>

      </ul>
    </nav>
    <!-- <nav class="sticky_nav2">
        <div style="color: black; margin-right: 3vw; width: 90%; display: flex; justify-content: flex-end; font-size: 1vw;">
          {{ userEmail }}
        </div>
    </nav> -->
            
  </div>
</template>

<script>
import axios from 'axios'
import VueJwtDecode from "vue-jwt-decode"
import NavSelectModal from "../common/NavSelectModal"
import MyAlerts from "../alert/MyAlerts"


const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: "Navbar",
  components: {
    NavSelectModal,
    MyAlerts
  },
  data: function () {
    return {
      // login: false,
      messages: [],
      userEmail: this.$store.getters.getUserEmail,
      user_id: 0,
      token: this.$store.getters.getAccessToken,
      pets: [],
      profileShow: false,
      alertShow:false,
      contents: [
        "프로필",
        "내 알림",
        "메시지",
        "로그아웃",
      ],
      chatList:[], //가장 최근 온 채팅 내용들 불러오기
      followList:[], //팔로우 알람 리스트
      feedList:[], //피드 알람 리스트
      myNickname:"",
      unreadMessageCnt:0,
      chatCnt:0,
      alertCnt:0,
    }
  },
  methods: {
    getMyInfo(){ //내 닉네임 가져오기
      axios.post(`${SERVER_URL}/member/getsinglemember/${this.$store.getters.getUserId}`)
        .then(({data}) => {
          this.myNickname=data.user_nickname
          //console.log("내 닉네임 : "+this.myNickname)
          this.getMyList(); //채팅 최근목록 가져오기
          this.getAlertList(); //알람 목록 가져오기
        })
    },
    getMyList(){ //내 채팅목록(가장 최근 내용만) 가져오기
      const jwt = localStorage.getItem("jwt")
      // console.log(jwt)
      if (jwt) {
        axios.post(`${SERVER_URL}/chat/mychatlist/${this.$store.getters.getUserId}`)
          .then(({data}) => {
            this.chatList=data
            for(var i = 0;i<this.chatList.length;i++){
              /*누구와의 채팅방인지 : 
                roomNames를 따로만든건... 마지막 내용의 보낸이가 "나"일수도 "남"일수도 있기 때문!! 인덱스는 chatList와 같다
                하지만 화면에 보이는 건 ~~님과의 채팅방 이니까 결국 "남"이 나와야 한다 
              */
              if(this.chatList[i].send_id==this.$store.getters.getUserId){ //마지막으로 보낸사람이 나면 -> send_nickname receive_nickname 해서 출력해야댐
                continue;
              }else if(!this.chatList[i].isread){
                //console.log(this.chatList[i])
                this.unreadMessageCnt++;
              }
            //버튼식으로 클릭해서 사람 프로필에 갈때는 chatList.send_id를 user_id처럼해서 보내면 된다!!!!!!!
          }
          this.chatCnt=this.unreadMessageCnt
          this.sendUnreadMessages() //최근 메시지 내용 전체를 mychatroom에 전달
          this.sendUnreadMessagesCnt() //최근 메시지 중 안읽은 메시지 개수를 navselectModal에 전달
          this.unreadMessageCnt=0
        })
      }
    },
    getAlertList: function(){ //내 알람 리스트
      const jwt = localStorage.getItem("jwt")
      // console.log("나도 있음")
      if (jwt) {
        axios.post(`${SERVER_URL}/alert/${this.$store.getters.getUserId}`)
        .then(({data}) => {
          this.followList=data
        // console.log("팔로우 알람 아래")
        // console.log(this.followList)
  
          axios.post(`${SERVER_URL}/alert/feed/${this.$store.getters.getUserId}`)
          .then(({data}) => {
            this.feedList=data
          // console.log("피드 알람 아래")
          // console.log(this.feedList)
          this.alertCnt=this.feedList.length
            this.sendUnreadAlertsFollow()
            this.sendUnreadAlertsFeed()
          })
        })
      }


    },
    parentsMethod: function(message) {
      this.alertCnt=message
    },
    moveToFeedMain: function () {
      if (this.$route.path == '/feed/main') {
        this.$router.go(this.$router.currentRoute)
      } else {
        this.$router.push({ name: 'FeedMain'})
      }
    },
    signOut: function () {
      this.$store.dispatch("logout")
      this.$router.push({ name: 'Login'})
    },
    goal: function () {
      this.$store.dispatch("goalgoal")
    },
    myProfile: function () {
      if (this.$route.path == `/user/profile/${this.$store.getters.getUserId}`) {
        this.$router.go(this.$router.currentRoute)
      } else {
      this.$router.push({ name: 'UserProfile', params: { user_id: `${this.$store.getters.getUserId}` } })}
    },
    myAlerts: function () {
      this.alertShow = !this.alertShow
      this.profileShow =false
    },
    myMessages: function(){
      this.$router.push({ name: "MyChatRoom", params: { user_id: `${this.$store.getters.getUserId}`}})
    },

    moveToDiary: function () {
      this.$router.push({ name: 'Calendar' })
    },
    getUserPets: function(){
      axios.get(`${SERVER_URL}/pet/selectAll/${this.user_Id}`)
        .then(({data}) => {
          this.pets = data
        })
        .catch( (err) => {
          console.log(err)
        })
    },
    profileModal: function () {
      this.profileShow = !this.profileShow
    },
    moveToMap: function () {
      if (this.$route.path == `/map/searchmap`) {
        this.$router.go(this.$router.currentRoute)
      } else {
      this.$router.push({ name: 'SearchMap' })
      }
    },
    sendUnreadMessages(){ //mychatroom 가장 최근 채팅 리스트를 보내줌...
      this.$store.dispatch('setMessageUnread', this.chatList);
    },
    sendUnreadMessagesCnt(){ //navselectmodal에 안읽은 채팅 개수를 보내줌...
      this.$store.dispatch('setMessageUnreadCnt', this.unreadMessageCnt);
    },
    sendUnreadAlertsFollow(){ //navselectmodal에 안 읽은 알람 리스트를 보내줌...
      this.$store.dispatch('setAlertUnreadFollow', this.followList);
    },
    sendUnreadAlertsFeed(){ //navselectmodal에 안 읽은 알람 리스트를 보내줌...
      this.$store.dispatch('setAlertUnreadFeed', this.feedList);
    },

  },
  created: function () {
    const token = localStorage.getItem('jwt')
    if (token) {
      this.$store.state.accessToken = token
      const info = VueJwtDecode.decode(token)
      this.$store.state.userId = info.user.user_id
      this.$store.state.userNickname = info.user.user_nickname
      // console.log(info.user.user_nickname)
      // console.log(info.user.user_email)
      this.userEmail = `반갑습니다, ${info.user.user_email}님`
      this.user_id=this.$store.getters.getUserId;
    } else {
      this.$router.push({ name: 'Login' })
    }
    this.getMyInfo();
    setTimeout(setInterval(()=>this.getMyList(),3000), 5000);
    setTimeout(setInterval(()=>this.getAlertList(),60000), 5000);
  },
  computed: {
    login: function () {
      return this.$store.getters.getAccessToken
    },
  },
  watch: {
    token: function () {
      if (this.token) {
        this.userEmail = `반갑습니다, ${this.$store.state.getUserEmail}님`
      } else {
        this.userEmail = '아직 로그인하지 않았습니다.'
      }
    }
  },
  // beforeDestroy() {
  // },
  // destroyed() {
  //   console.log("파괴")
  // },
}
</script>

<style>

@font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}

.red-dot {
  background-color: red;
  color: red;
  z-index: 3;
  position: absolute;
  top: 50px;
  left: 50px;
  width: 1vw;
  height: 1vw;
  border-radius: 70%;
  padding: 0 0.5vw;
}

.myAlerts {
  position: fixed;
  top: 10vh;
  right: 0;
  width: 50vw;
  max-width: 400px;
  height: auto;
  margin: 0 auto;
}

.sticky_nav {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: white;
  width: 100%;
  height: 10vh;
  border-bottom: 1px solid  rgba(175, 175, 175, 0.5);
}

.sticky_nav2 {
  display: flex;
  justify-content: center;
  align-items: center;
  background-color:white;
  width: 100%;
  padding-bottom: 1vw;
  border-bottom: 0.1vw solid  rgba(175, 175, 175, 0.5);
}

.nav_menu {
  list-style: none;
  display: flex;
  color: white;
  justify-content: space-between;
  align-items: center;
  width: 95%;
  margin: 10px 5px 0 5px;
}

.search-logo-container {
  display: flex;
  /* justify-content: center; */
  align-items: center;
  width: 30%;
  height: 60%;
  border: 1px solid rgba(175, 175, 175, 0.5);
}

.search-logo-container:hover {
  cursor: pointer;
}

.search-logo {
  color: black;
  font-size: 30px;
  margin-left: 20px;
}

.icons {
  width: 60px;
  height: 60px;
}

.nav_menu_div {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  list-style: none;
  width: 25%;
  /* padding-right: 30px; */
  display: flex;
  color: rgb(93, 26, 0);
  /* width: 30%; */
}

.nav_menu_div li:hover {
  cursor: pointer;
}

.nav_menu_div_logo {
  list-style: none;
  display: flex;
  color: white;
  width: 25%;
  max-width: 235px;
}

.logo-image {
  cursor: pointer;
  margin-top: 0vh;
  width: 100%;
}

.nav-diary-btn {
  font-size: 45px;
}

.nav-profile-btn {
  font-size: 45px;
}

ul div li {
  padding: 10px;
  display: flex;
  align-items: center;
}

li i {
  font-size: 500px;
}

.fa-user-circle:hover {
  cursor: pointer;
}

.abc span {
  color: red;
}

.profileModal {
  position: absolute;
  top: 75px;
  left: -10vw;
  z-index: 20;
  width: 17vw;
  transition-duration: 0.8s;
  /* font-size: 1vw; */
}

.alertModal {
  position: absolute;
  top: 75px;
  left: -10vw;
  z-index: 20;
  width: 20vw;
  transition-duration: 0.8s;
  /* font-size: 1vw; */
}

@media ( max-width: 1100px) {

  .icons {
    width: 5vw;
    height: 5vw;
  }

  .red-dot {
    background-color: red;
    color: red;
    z-index: 3;
    position: absolute;
    top: 4.5vw;
    left: 4.5vw;
    width: 1vw;
    height: 1vw;
    border-radius: 70%;
    padding: 0 0.5vw;
  }

  .sticky_nav {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: white;
    width: 100%;
    border-bottom: 0.1vw solid  rgba(175, 175, 175, 0.5);
  }

  .sticky_nav2 {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color:white;
    width: 100%;
    padding-bottom: 1vw;
    border-bottom: 0.1vw solid  rgba(175, 175, 175, 0.5);
  }

  .nav_menu {
    list-style: none;
    display: flex;
    color: white;
    justify-content: space-between;
    align-items: center;
    width: 95%;
    margin: 1vw 0.5vw 0vw 0.5vw;
  }

  .search-logo-container {
    display: flex;
    /* justify-content: center; */
    align-items: center;
    width: 30%;
    height: 60%;
    border: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .search-logo-container:hover {
    cursor: pointer;
  }

  .search-logo {
    color: black;
    font-size: 3vw;
    margin-left: 2vw;
  }

  .nav_menu_div {
    /* padding-right: 3vw; */
    list-style: none;
    display: flex;
    color: rgb(93, 26, 0);
    /* width: 30%; */
  }

  .nav_menu_div li:hover {
    cursor: pointer;
  }

  .nav-diary-btn {
    font-size: 4.5vw;
  }

  .nav-profile-btn {
    font-size: 4.5vw;
  }

  .nav_menu_div_logo {
    list-style: none;
    display: flex;
    color: white;
    width: 25%;
  }

  .logo-image {
    cursor: pointer;
  }

  ul div li {
    padding: 1vw;
    display: flex;
    align-items: center;
  }

  li i {
    font-size: 50vw;
  }

  .fa-user-circle:hover {
    cursor: pointer;
  }

  .abc span {
    color: red;
  }

  .profileModal {
    position: absolute;
    top: 7vw;
    left: -10vw;
    z-index: 20;
    width: 20vw;
    transition-duration: 0.8s;
    /* font-size: 1vw; */
  }

}

@media ( max-width: 540px) {

  .red-dot {
    background-color: red;
    color: red;
    z-index: 3;
    position: absolute;
    top: 5.5vw;
    left: 5.5vw;
    width: 1.5vw;
    height: 1.5vw;
    border-radius: 70%;
    padding: 0 0.5vw;
  }

  .myAlerts {
    position: absolute;
    top: 10vh;
    right: 0vw;
    width: 70vw;
    height: auto;
    margin: 0 auto;
    z-index: 9999;
  }

  .sticky_nav {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: white;
    width: 100%;
    border-bottom: 0.1vw solid  rgba(175, 175, 175, 0.5);
  }

  .sticky_nav2 {
    display: flex;
    justify-content: center;
    align-items: center;
    background-color:white;
    width: 100%;
    padding-bottom: 1vw;
    border-bottom: 0.1vw solid  rgba(175, 175, 175, 0.5);
  }

  .nav_menu {
    list-style: none;
    display: flex;
    color: white;
    justify-content: space-between;
    align-items: center;
    width: 95%;
    margin: 1vw 0.5vw 0vw 0.5vw;
  }

  .search-logo-container {
    display: flex;
    /* justify-content: center; */
    align-items: center;
    width: 30%;
    height: 60%;
    border: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .search-logo-container:hover {
    cursor: pointer;
  }

  .search-logo {
    color: black;
    font-size: 5vw;
    margin-left: 2vw;
  }

  .nav_menu_div {
    /* padding-right: 3vw; */
    list-style: none;
    display: flex;
    color: rgb(93, 26, 0);
    /* width: 30%; */
  }

  .nav_menu_div li:hover {
    cursor: pointer;
  }

  .nav-diary-btn {
    font-size: 6vw;
  }

  .nav-profile-btn {
    font-size: 6vw;
  }

  .nav_menu_div_logo {
    list-style: none;
    display: flex;
    color: white;
    width: 25%;
  }

  .logo-image {
    cursor: pointer;
  }

  ul div li {
    padding: 2vw;
    display: flex;
    align-items: center;
  }

  li i {
    font-size: 50vw;
  }

  .fa-user-circle:hover {
    cursor: pointer;
  }

  .abc span {
    color: red;
  }

  .profileModal {
    position: absolute;
    top: 15vw;
    left: -17.5vw;
    z-index: 20;
    width: 30vw;
    transition-duration: 0.8s;
    /* font-size: 1vw; */
  }

}


</style>
