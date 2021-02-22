<template>
  <div style="font-family: GmarketSansMedium;" class="top-profile-container">
    <Navbar
      style="position:sticky; top:0; z-index:30; width: 100%;"
    >
    </Navbar>
    <div class="profile-container">
      <!-- 회원 개인 정보 -->
      <header class="userInfo">
        <div class="orderPetImg">
          <div class="orderPetImg-title">
            <div class="userInfo-username" :data-tooltip-text-ys= "user.user_content">
              {{user.user_nickname}}
            </div>
            <div v-if="user.user_id == myId">
              <button class="userInfo-edit" @click="abShowOn">
                <i class="fas fa-cog"></i>
              </button>
            </div>
          </div>
        </div>
        <section class="userInfo-section">
          <ul class="userInfo-menu">
            <div class="userInfo-div">
              <li>
                <div>게시물</div>
                <div>{{feeds.length}}</div>
              </li>
              <li class="follow" @click="followShowOff2">
                <div>팔로워</div>
                <div>{{followersList.length}}</div>
              </li>
              <li class="follow" @click="followShowOff">
                <div>팔로우</div>
                <div>{{followingsList.length}}</div>
              </li>
            </div>
            <div v-if="user.user_id != myId" class="userInfo-follow">
              <button class="userInfo-follow-btn" @click="sendmessages">
                메시지
              </button>
              <button class="userInfo-follow-btn-following" v-if="myFollowings.includes(userId)" @click="unfollow(userId)">언팔로우</button>
              <button class="userInfo-follow-btn" v-else-if="userId != myId" @click="follow(userId)">팔로우</button>
            </div>
          </ul>
          <!-- 한 줄 소개 -->
          <!-- <div class="orderPetImg-content">
            {{user.user_content}}
          </div> -->
          <!-- 한 줄 소개라는 표시 지울거 지금은 안 보이니깐 -->
        </section>
      </header>

      <hr>
      <!--  -->
      <!-- 나의 반려 동물 -->
      <div class="petInfo-title">
        <div>
          나의 반려동물
        </div>
        <button
          v-if="user.user_id == myId && pets.length < 3"
          @click="addPet"
        >
          <i class="fas fa-plus-circle"></i>
        </button>
      </div>
      <div v-if="0 < pets.length">
        <AllPet
          v-if="petOrder == 0"
          :pets="pets"
          :userId="userId"
          :myId="myId"
          @orderNext="orderNext"
          @orderPre="orderPre"
        />
        <div
          class="petInfo-list"
          v-for="(pet, idx) in pets"
          :key="idx"
        >
          <PetInfo
            v-if="petOrder == idx+1"
            :number="idx+1"
            :cnt="pets.length"
            :pet="pet"
            :userId="userId"
            :myId="myId"
            @orderNext="orderNext"
            @orderPre="orderPre"
          />
        </div>
      </div>
      <div v-else>
        <NoPet />
      </div>
      <hr>
      <div class="my-pets-feeds-container">
        <!-- <div class="my-pets-feeds-title">
          피드
        </div> -->
        <div class="my-pets-feeds-contents" v-if="petOrder == 0">
          <div
            class="my-pets-feeds-content"
            v-for="(feed, idx) in feeds"
            :key="idx+'d'"
          >
            <div @click="moveToReadFeed(feed.feed_id)">
              <FeedItem
                :feed="feed"
                :myId="user_id"
              />
            </div>
          </div>
        </div>
        <div class="my-pets-feeds-contents" v-else>
          <div
            class="my-pets-feeds-content"
            v-for="(feed, idx) in filteredFeeds"
            :key="idx+f"
          >
            <div @click="moveToReadFeed(feed.feed_id)">
              <FeedItem
                :feed="feed"
                :myId="user_id"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 팔로우 리스트 -->
    <div class="text-modal" v-if="followShow">
      <div class="text-modal-wrapper">
        <FollowModal
          :myFollowings="myFollowings"
          :followList="followingsList"
          :title="'팔로우'"
          :myId="myId"
          :userId="userId"
          @modalOff="followShowOff"
          @unfollow="unfollow"
          @follow="follow"
        />
      </div>
    </div>
    <!-- 팔로워 리스트 -->
    <div class="text-modal" v-if="followShow2">
      <div class="text-modal-wrapper">
        <FollowModal
          :myFollowings="myFollowings"
          :followList="followersList"
          :title="'팔로워'"
          :myId="myId"
          :userId="userId"
          @modalOff="followShowOff2"
          @unfollow="unfollow"
          @follow="follow"
          @followCancle="followCancle"
        />
      </div>
    </div>
    <!-- 정보 수정 -->
    <div class="text-modal" v-if="abShow">
      <div class="text-modal-wrapper">
        <AorBModal
          :a="'회원 정보 수정'"
          :b="'비밀번호 변경'"
          @selectA="modifyUser"
          @selectB="clickChangePW"
          @modalOff="AorBmodalOff"
        />
      </div>
    </div>
  </div>
</template>

<script>
import AorBModal from "../../components/common/AorBModal"
import FollowModal from '@/components/user/FollowModal.vue'
import PetInfo from "@/components/pet/PetInfo.vue"
import AllPet from "@/components/pet/AllPet.vue"
import Navbar from "../../components/Nav/Navbar"
import FeedItem from "../../components/feed/FeedItem"
import NoPet from "@/components/pet/NoPet.vue"
import VueJwtDecode from "vue-jwt-decode"
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "UserProfile",
  props: {
    user_id: {
      type: Number,
    },
  },
  components: {
    PetInfo,
    AllPet,
    NoPet,
    Navbar,
    FeedItem,
    FollowModal,
    AorBModal,
  },
  data: function () {
    return {
      userId: Number(this.$route.params.user_id), //지금보고있는 마이페이지의 ID
      user: "",
      pets: [],
      feeds: [],
      petOrder: 0,
      myId: 0, //로그인한 ID
      myNickname:"", //로그인한 닉네임
      myMessages:0, //보류?
      followingModal:false, //모달...
      followerModal:false, //모달...
      allFeedData: [],
      allPetTagData: [],
      allFileData: [],
      allTagData: [],
      filteredFeeds: [],
      likeList: [],
      f: 0,
      //
      followingsList: [], //userId로 following리스트 구하기
      followersList: [], //userId로 follower리스트 구하기
      myFollowings: [], //myId로 following리스트 구하기
      myFollowers: [], //myId로 follower리스트 구하기
      followShow: false,
      followShow2: false,
      abShow: false,
    }
  },
  created: function () {
    // console.log(window.location)
    this.getMyInfos() //유저 프로필에 들어오면 유저에 관련된 모든 정보들을 다 가져옴!!!
    this.getFeeds();
  },
  mounted: function () {
  },
  computed: {

  },
  watch: {
  },
  methods: {
    AorBmodalOff:function(){
      this.abShow = !this.abShow
    },
    clickChangePW: function () { //비밀번호 변경 화면으로 이동
      this.$router.push({ name: "ChangePW", params: { user_id: this.myId}})
    },
    abShowOn: function () {
      this.abShow = !this.abShow
    },
    getMyInfos(){//유저 프로필에 들어왔을 때, 유저의 기본적인 정보들 + 펫 등록 정보들 + 게시글 정보 + 팔로워/팔로잉 가져오기
      this.getUserInfo() //유저의 정보들 가져옴!!!
      this.getMyId()
      this.getUserPets() //펫 정보 가져옴!!!
    },
    followingList(){ //팔로우 수 클릭하면 팔로우 리스트 보여줌
      this.followingModal = !this.followingModal
    },
    followerList(){ //팔로잉 수 클릭하면 팔로잉 리스트 보여줌
      this.followerModal = !this.followerModal
    },
    getUserInfo: function () {
      axios.get(`${SERVER_URL}/member/getallmember`)
      .then(({data}) => {
        // console.log({data})
        this.user = {data}.data.filter(x => x.user_id == this.userId)[0]
        // console.log(this.user)
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getMyId: function () {
      const token = localStorage.getItem('jwt')
      if (token) {
        this.myId = VueJwtDecode.decode(token).user.user_id
        this.myNickname = VueJwtDecode.decode(token).user.user_nickname
        axios.post(`${SERVER_URL}/member/follower/${this.myId}`)
        .then(({data}) => {
          for (let follower of data) {
            this.myFollowers.push(
              follower.from_id
            )
          }
        })
        .catch( (err) => {
          console.log(err)
        })
        axios.post(`${SERVER_URL}/member/following/${this.myId}`)
        .then(({data}) => {
          for (let following of data) {
            this.myFollowings.push(
              following.to_id
            )
          }
          this.getUserFollowers() //팔로우 정보
          this.getUserFollowings() //팔로잉 정보
        })
        .catch( (err) => {
          console.log(err)
        })
      }
    },
    getUserPets: function(){
      axios.get(`${SERVER_URL}/pet/selectAll/${this.userId}`)
        .then(({data}) => {
          this.pets = data
        })
        .catch( (err) => {
          console.log(err)
        })
    },
    getUserFollowers: function () {
      if (this.userId==this.myId) {
        this.followersList = this.myFollowers
      } else {
        axios.post(`${SERVER_URL}/member/follower/${this.userId}`)
          .then(({data}) => {
            for (let following of data) {
            this.followersList.push(
              following.from_id
            )
          }
          })
          .catch( (err) => {
            console.log(err)
          })
      }
    },
    getUserFollowings: function () {
      if (this.userId == this.myId) {
        this.followingsList = this.myFollowings
      } else {
      axios.post(`${SERVER_URL}/member/following/${this.userId}`)
        .then(({data}) => {
          for (let following of data) {
            this.followingsList.push(
              following.to_id
            )
          }
        })
        .catch( (err) => {
          console.log(err)
        })
      }
    },
    signOut: function () {
      this.$store.dispatch("logout")
      this.$router.push({ name: 'Login'})
    },
    follow: function (userId) {
      axios.post(`${SERVER_URL}/member/getsinglemember/${this.myId}`)
      .then( (data) => {
        this.myNickname = data.data.user_nickname
        axios.post(`${SERVER_URL}/member/getsinglemember/${userId}`)
        .then( (data) => {
          this.yourNickname=data.data.user_nickname
          const credentials = {
            from_id: this.myId,
            from_nickname:this.myNickname,
            to_id: userId,
            to_nickname:this.yourNickname
          }
          axios.post(`${SERVER_URL}/member/follow`, credentials)
          .then( (res) => {
            // console.log(res)
            this.myFollowings.push(userId)
            if (this.userId == userId) {
              this.followersList.push(this.myId)
            }
          })
          .catch( (err ) => {
            console.log(err)
            const msg = "문제가 발생했습니다."
            alert(msg)
          })
      })

      })
    },
    unfollow: function (userId) {
      const credentials = {
        from_id: this.myId,
        to_id: userId,
      }
      axios.post(`${SERVER_URL}/member/unfollow`, credentials)
      .then( (res) => {
        // console.log(res)
        this.myFollowings.splice(this.myFollowings.indexOf(userId), 1)
        if (this.userId == userId) {
          this.followersList.splice(this.followersList.indexOf(this.myId), 1)
        }
      })
      .catch( (err ) => {
        console.log(err)
        const msg = "문제가 발생했습니다."
        alert(msg)
      });
    },
    followCancle: function (userId) {
      const credentials = {
        from_id: userId,
        to_id: this.myId,
      }
      axios.post(`${SERVER_URL}/member/unfollow`, credentials)
      .then( (res) => {
        this.followersList.splice(this.followersList.indexOf(userId), 1)
        // console.log(this.followersList)
      })
      .catch( (err ) => {
        console.log(err)
        const msg = "문제가 발생했습니다."
        alert(msg)
      });
    },
    messagesList:function(){ //내 메시지함
      this.$router.push({ name: "MyChatRoom", params: { user_id: this.myId}})
    },

    sendmessages: function () { //메시지 보내기
      this.$router.push({ name: "Chat", params: { user_id: this.userId}})
    },
    addPet: function () {
      this.$router.push({ name: "AddPet", params: { user_id: this.$store.getters.getUserId}})
    },
    //회원정보 수정
    modifyUser: function(){
      this.$router.push({ name: "ModifyUser", params: { user_id: this.$store.getters.getUserId}})
    },
    orderNext: function () {
      if (this.petOrder < this.pets.length) {
        this.petOrder += 1
        this.filteredFeeds = []
        let list = [...this.feeds]
        if (this.petOrder) {
          this.filteredFeeds = list.filter( x => {
            var match = false
            for (var pet_name of x.pets) {
              if (pet_name == this.pets[this.petOrder-1].pet_name) {
                match = true
                break
              }
            }
            return match
          })
        } else {
          this.filteredFeeds = list
        }
      }
      this.f += 100
    },
    orderPre: function () {
      if (0 < this.petOrder) {
        this.petOrder -= 1
        this.filteredFeeds = []
        let list = [...this.feeds]
        if (this.petOrder) {
          this.filteredFeeds = list.filter( x => {
            var match = false
            for (var pet_name of x.pets) {
              if (pet_name == this.pets[this.petOrder-1].pet_name) {
                match = true
                break
              }
            }
            return match
          })
        } else {
          this.filteredFeeds = list
        }
      }
      this.f += 100
    },
    getLikeList: function () {
      axios.get(`${SERVER_URL}/like/list`)
      .then( (res) => {
        // console.log(res)
        this.likeList = res.data
        this.getSum();
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getPetTags: function(){
       axios.get(`${SERVER_URL}/feed/allPet`)
      .then(({data}) =>{
        this.allPetTagData = data;
        this.getLikeList()
        // console.log("좋아요")
      })
    },
    getTags: function(){
      axios.get(`${SERVER_URL}/feed/allTag`)
      .then(({data}) =>{
        this.allTagData = data;
        this.getPetTags();
        // console.log("태그");
        // console.log(this.allTagData);
      })
    },
    getFiles: function(){
      axios.get(`${SERVER_URL}/feed/allFile`)
      .then(({data}) =>{
        this.allFileData = data;
        this.getTags();
      })
    },
    getFeeds: function () {
      axios.get(`${SERVER_URL}/feed/allFeed`)
      .then(({data}) =>{
        this.allFeedData = data;
        this.getFiles();
      })
    },
    getSum: function () {
      for(let i=0; i<this.allFeedData.length; i++){
        var temp = {};
        if (this.$route.params.user_id == this.allFeedData[i].user_id) {
          temp["feed_id"] = this.allFeedData[i].feed_id
          temp["user_id"] = this.allFeedData[i].user_id
          temp["feed_date"] = this.allFeedData[i].feed_date
          temp["feed_content"] = this.allFeedData[i].feed_content
          temp["file_name"] = []
          temp["file_type"] = []
          temp["pets"] = []
          temp["tags"] = []
          temp["mentions"] = []
          temp["like"] = []
          // temp["like_nickname"] = []
          temp["image_filters"] = []
        } else {
          continue
        }
        for(let j = 0; j < this.allFileData.length; j++){
          if(temp["feed_id"] == this.allFileData[j].feed_id){
            temp["file_name"].push(this.allFileData[j].file_name);
            temp["file_type"].push(this.allFileData[j].file_type)
            temp["image_filters"].push(this.allFileData[j].filter_name)
          } 
        }
        for(let j = 0; j < this.allPetTagData.length; j++){
          if(temp["feed_id"] == this.allPetTagData[j].feed_id){
            temp["pets"].push(this.allPetTagData[j].pet_name);
          }
        }
        for(let j = 0; j < this.likeList.length; j++){
          if(temp["feed_id"] == this.likeList[j].feed_id){
            temp["like"].push(this.likeList[j].user_id);
            // temp["like_nickname"].push(this.likeList[j].user_nickname);
          }
        }
        for(let j = 0; j < this.allTagData.length; j++){
          if(temp["feed_id"] == this.allTagData[j].feed_id){
            temp["tags"].push(this.allTagData[j].tag_content);
          }
        }
        if (this.feeds.length < 1) {
          this.feeds.push(temp);
        } else {
          this.feeds.splice(0, 0, temp)
        }
      }
      // console.log(this.feeds)
    },
    moveToReadFeed: function (feed_id) {
      this.$router.push({ name: "ReadFeed", params: {feed_id: feed_id} })
    },
    // 팔로우 관련 상훈
    followShowOff: function () {
      this.followShow = !this.followShow
    },
    followShowOff2: function () {
      this.followShow2 = !this.followShow2
    },
  },
}
</script>

<style>
@font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
[data-tooltip-text-ys]:hover {
   position: relative;
}

[data-tooltip-text-ys]:after {
  -webkit-transition: bottom .3s ease-in-out, opacity .3s ease-in-out;
  -moz-transition: bottom .3s ease-in-out, opacity .3s ease-in-out;
  transition: bottom .3s ease-in-out, opacity .3s ease-in-out;

  background-color: rgba(0, 0, 0, 0.8);

  -webkit-box-shadow: 0px 0px 3px 1px rgba(50, 50, 50, 0.4);
  -moz-box-shadow: 0px 0px 3px 1px rgba(50, 50, 50, 0.4);
  box-shadow: 0px 0px 3px 1px rgba(50, 50, 50, 0.4);
   
  -webkit-border-radius: 5px;
  -moz-border-radius: 5px;
  border-radius: 5px;
   
  color: #FFFFFF;
  font-size: 1.5vw;
  margin-bottom: 10px;
  padding: 7px 12px;
  position: absolute;
  white-space: pre;
  width: auto;
  max-width: 30vw;
  word-wrap: break-word;

  z-index: 9999;

  opacity: 0;
  /* left:10vw; */
  /* left: 5999px;*/
  top: 100%;
  left: 0;
   
  content: attr(data-tooltip-text-ys);
}

[data-tooltip-text-ys]:hover:after {
  top: 100%;
  left: 0;
  opacity: 1;
}

.top-profile-container {
  min-height: 100vh;
}

.profile-container {
  padding: 15px;
  margin: 0 auto;
}

.main-container {
  width: 80%;
  /* height: 400px; */
  margin: 0 auto;
}
        
.img-upload-info-container {
  margin-top: 4vw;
  color: #222222;
  border: 0.1vw solid #dddddd;
}

.upload-title {
  text-align: center;
  font-size: 2vw;
  line-height: 5vw;
  border-bottom: 0.1vw solid #dddddd;
}

.img-upload-example {
  height: 100%;
}

.img-notice {
  margin: 1.5vw;
  padding: 1.5vw 3vw;
  border: 0.1vw solid #dddddd;
}

.img-preview-container {
  height: 100%;
}

.img-upload-before {
  margin: 1.5vw;
  border: 0.1vw solid #dddddd;
  background-color: #f4f4f4;
  min-height: 30vw;
  font-size: 1.3vw;
  color: #888888;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.img-upload-example-container {
  display: flex;
  align-items: center;
  justify-content: center;
  /* height: 100%;
  width: 100%; */
}

.image-upload-title {
  text-align: center;
}

.image-upload-notice {
  margin-top: 0.5vw;
  text-align: center;
}

.image-upload-notice-red {
  color: #ef4351;
}

.image-box {
  margin-top: 2.5vw;
  padding-bottom: 1.3vw;
  text-align: center;
}

.image-box input[type='file'] {
  position: absolute;
  width: 0;
  height: 0;
  padding: 0;
  overflow: hidden;
  border: 0;
}

.image-box label {
  display: inline-block;
  padding: 0.8vw 1.5vw;
  background-color: #232d4a;
  color: #fff;
  vertical-align: middle;
  font-size: 1.3vw;
  cursor: pointer;
  border-radius: 0.5vw;
}

.img-preview-wrapper {
  padding: 0.8vw;
  position: relative;
}

.img-preview-wrapper>img {
  position: relative;
  width: 20vw;
  height: 20vw;
  z-index: 1;
}

.img-close-btn {
  position: absolute;
  /* align-items: center; */
  line-height: 1.4vw;
  z-index: 2;
  font-size: 1.5vw;
  right: 0.7vw;
  top: 0.7vw;
  color: #fff;
  font-weight: bold;
  background-color: #666666;
  width: 1.7vw;
  height: 1.7vw;
  text-align: center;
  cursor: pointer;
}

.img-preview-container {
  height: 100%;
  display: flex;
  flex-wrap: wrap;
}

.userInfo {
  display: flex;
  justify-content: center;
  align-items: center;
}

.userInfo-section {
  width: 75%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.userInfo-menu {
  font-size: 20px;
  list-style: none;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 0;
}

.userInfo-div {
  list-style: none;
  display: flex;
  align-items: center;
}

.userInfo-div li {
  padding: 1vw;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.userInfo-follow {
  display: flex;
  /* margin-left: 1vw; */
}

.userInfo-follow-btn {
  margin-left: 1vw;
  width: 110px;
  padding: 10px;
  border-radius: 10px;
  border: 3px solid;
  border-color: mediumslateblue;
  background-color: white;
  color:mediumslateblue;
  font-size: 15px;
  font-weight: bold;
  transition-duration: 0.5s;
}

.userInfo-follow-btn:hover {
  background-color:mediumslateblue;
  color:white;
}

.userInfo-follow-btn-following {
  margin-left: 1vw;
  width: 110px;
  padding: 10px;
  border-radius: 10px;
  border: 3px solid;
  border-color: gray;
  background-color: white;
  color:gray;
  font-size: 15px;
  font-weight: bold;
  transition-duration: 0.5s;
}

.userInfo-follow-btn-following:hover {
  background-color:gray;
  color:white;
}

.userInfo-signout {
  padding: 1vw;
  border-radius: 1vw;
  border: 0.3vw solid;
  border-color: gray;
  background-color: white;
  color:gray;
  font-size: 1.5vw;
  font-weight: bold;
  transition-duration: 0.5s;
}

.userInfo-signout:hover {
  background-color:gray;
  color:white;
  cursor: pointer;
}

/*  */

.orderPetImg {
  width: 35%;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.orderPetImg-title {
  display: flex;
  justify-content: center;
  align-items: center;
}

.orderPetImg-content {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 2vw;
}

.userInfo-username {
  font-size: 4vw;
}

.userInfo-edit {
  font-size: 4vw;
  margin-left: 1vw;
}

.userInfo-edit:hover {
  cursor: pointer;
}

.follow:hover {
  cursor: pointer;
}

.message:hover {
  cursor: pointer;
}

.petInfo-container {
  display: flex;
  justify-content: center;
}

.petInfo-title {
  display: flex;
  /* justify-content: space-between; */
  margin-top: 2vh;
}

.petInfo-title div {
  font-size: 2vw;
  font-weight: bold;
  line-height: 2;
}

.petInfo-title button {
  margin-left: 1vw;
  font-size: 2vw;
  font-weight: bold;
}

.petInfo-title button:hover {
  cursor: pointer;
}

.petInfo {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 80%;
}

.petInfo img {
  border-radius: 70%;
  width: 12vw;
  height: 12vw;
}

.petInfo-next-btn {
  width: 5%;
  display: flex;
  justify-content: center;
}

.petInfo-img {
  width: 25%;
  margin-left: 5%;
}

.petInfo-content {
  width: 50%;
  margin-left: 5%;
  margin-right: 5%;
}

.petInfo-title-container {
  display: flex;
  align-items: center;
}

.petInfo-content-name {
  font-size: 4vw;
}

.petInfo-content-edit {
  font-size: 3vw;
  margin-left: 0.5vw;
}

.petInfo-content-edit:hover {
  cursor: pointer;
}

.my-pets-feeds-container {
  width: 100%;
}

.my-pets-feeds-title {
  font-size: 2vw;
  font-weight: bold;
}

.my-pets-feeds-contents {
  width: 100%;
  display: flex;
  flex-wrap: wrap;
  /* justify-content: space-between; */
  /* align-items: center; */
}

.my-pets-feeds-content {
  width: 30%;
  margin: 0 auto;
  /* margin-left: 1.4vw;
  margin-right: 1.4vw; */
}

.profile-feed-img-div {
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
}

/* .petInfo-list {
  display: flex;
} */

@media ( max-width: 1100px) {

  .profile-container {
    padding: 15px 10px;
    margin: 0 auto;
  }

  .userInfo-follow-btn {
    width: 10vw;
    padding: 1vw;
    border-radius: 1vw;
    border: 0.3vw solid;
    font-size: 1.5vw;
  }

  .userInfo-follow-btn-following {
    margin-left: 1vw;
    width: 10vw;
    padding: 1vw;
    border-radius: 1vw;
    border: 0.3vw solid;
    font-size: 1.5vw;
  }

  .userInfo-menu {
    font-size: 2vw;
    list-style: none;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 0;
  }

  .petInfo-content-name2 {
    font-size: 2vw;
  }

}

@media ( max-width: 540px) {

  .userInfo-follow {
    display: flex;
    flex-direction: column;
  }

  .profile-container {
    padding: 15px 5px;
    margin: 0 auto;
  }

  .userInfo-menu {
    font-size: 3.5vw;
  }

  .userInfo-follow-btn {
    width: 15vw;
    padding: 1vw;
    border-radius: 1vw;
    border: 0.3vw solid;
    font-size: 1.5vw;
    margin-bottom: 1vw;
  }

  .userInfo-follow-btn-following {
    margin-left: 1vw;
    width: 15vw;
    padding: 1vw;
    border-radius: 1vw;
    border: 0.3vw solid;
    font-size: 1.5vw;
    margin-bottom: 1vw;
  }

  .orderPetImg-content {
    font-size: 5vw;
  }

  .userInfo-username {
    font-size: 4vw;
  }

  /* 펫 인포 */

  .petInfo-title div {
    font-size: 5vw;
    font-weight: bold;
    line-height: 2;
  }

  .petInfo-title button {
    margin-left: 1vw;
    font-size: 5vw;
    font-weight: bold;
  }

  .petInfo-title button:hover {
    cursor: pointer;
  }

  .petInfo {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 80%;
  }

  .petInfo img {
    border-radius: 70%;
    width: 15vw;
    height: 15vw;
  }

  .petInfo-next-btn {
    width: 5%;
    display: flex;
    justify-content: center;
  }

  .petInfo-img {
    width: 25%;
    margin-left: 5%;
  }

  .petInfo-content {
    width: 50%;
    margin-left: 5%;
    margin-right: 5%;
  }

  .petInfo-title-container {
    display: flex;
    align-items: center;
  }

  .petInfo-content-name {
    font-size: 5vw;
  }

  .petInfo-content-name2 {
    font-size: 4vw;
  }

  .petInfo-content-edit {
    font-size: 3vw;
    margin-left: 0.5vw;
  }

  .petInfo-content-edit:hover {
    cursor: pointer;
  }

  .my-pets-feeds-container {
    width: 100%;
  }

  .my-pets-feeds-title {
    font-size: 5vw;
    font-weight: bold;
  }

  [data-tooltip-text-ys]:after {
    color: #FFFFFF;
    font-size: 3vw;
    margin-bottom: 10px;
    padding: 7px 12px;
    position: absolute;
    white-space: pre;
    width: auto;
    max-width: 40vw;
    word-wrap: break-word;

    z-index: 9999;

    opacity: 0;
    /* left:10vw; */
    /* left: 5999px;
    top: 100vw; */
    
    content: attr(data-tooltip-text-ys);
  }

  [data-tooltip-text-ys]:hover:after {
    top: 100%;
    left: 0;
    opacity: 1;
  }

}

</style>
