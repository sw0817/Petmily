<template>
  <div class="feed">
  <Navbar
    style="position:sticky; top:0; z-index:15; width: 100%;"
  >
  </Navbar>
    <button class="write-btn" @click="createFeed">
      <div class="write-container">
        <i class="fas fa-edit writing"></i>
      </div>
    </button>

    <div class="wrapB container">
      <div v-if="searchKeyword != ''">
        <div v-for="(feed) in filteredFeeds"
        :key="feed.feed_id"
        style="display: flex; align-items: center; justify-content: center;">
        <FeedItem2
          v-if="!followingCheck || followingCheck && followingFeed(feed)"
          :feed="feed"
          :myId="myId"
          :followings="followings"
          @searchingTag="searchingTag"
          @searchingFriendTag="searchingFriendTag"
          @deleteFeed="deleteFeed"
          @reportFeed="reportFeed"
          @readFeed="readFeed"
          @updateFeed="updateFeed"
          @follow="follow"
          @unfollow="unfollow"
        />
        </div>
      </div>

      <div v-else>
        <div v-for="(feed) in feeds"
          :key="feed.feed_id"
          style="display: flex; align-items: center; justify-content: center;">
          <FeedItem2
            v-if="!followingCheck || followingCheck && followingFeed(feed)"
            :feed="feed"
            :myId="myId"
            :followings="followings"
            @searchingTag="searchingTag"
            @searchingFriendTag="searchingFriendTag"
            @deleteFeed="deleteFeed"
            @reportFeed="reportFeed"
            @readFeed="readFeed"
            @updateFeed="updateFeed"
            @follow="follow"
            @unfollow="unfollow"
          />
        </div>
      </div>
    </div>
    <div class="feed-search-container-div">
      <div class="feed-search-container">
        <div class="feed-search-logo">
          <img :src="followingImg" class="friend-search-mode-img" @click="abOn">
          <div class="feed-search-btn-div" @click="friendModal">
            <button class="feed-search-btn">
              <i class="fas fa-search"></i>
            </button>
            <div class="feed-search-keyword">
              {{ searchKeyword }}
            </div>
            <button v-if="searchKeyword" class="feed-search-keyword-cancle" @click="cancleSearchKeyword">
              <i class="fas fa-times"></i>
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="text-modal" v-if="simpleShow">
      <div class="text-modal-wrapper">
        <SimpleModal
          :contents="simpleModalContents"
          @modalOff="simpleOff"
        />
      </div>
    </div>
    <!--  -->
    <div v-if="friendShow" class="friend-modal">
      <div class="modal-wrapper">
        <div class="tag-table-container">
          <div class="tag-table-header">
            <div class="tag-table-pre">
              <button class="tag-table-pre-btn" @click="friendModal">
                <i class="fas fa-arrow-left"></i>
              </button>
            </div>
            <div class="friend-tag-title">
              <tabs
                :tabs="tabs"
                :currentTab="currentTab"
                :wrapper-class="'default-tabs'"
                :tab-class="'default-tabs__item'"
                :tab-active-class="'default-tabs__item_active'"
                :line-class="'default-tabs__active-line'"
                @onClick="handleClick"
              />
            </div>
            <div class="tag-table-done">
              <button class="tag-table-done-btn" @click="friendModal">
                완료
              </button>
            </div>
          </div>


          <div class="content">
            <div v-if="currentTab==='friend'">
              <div class="friend-tag-myFriend-title">
                <!-- 나의 친구 -->
                <div class="friend-tag-search-logo">
                  <i class="fas fa-search"></i>
                </div>
                <input
                  type="text"
                  placeholder="친구 검색"
                  class="friend-tag-search-input"
                  v-model="friendSearchInput"
                >
              </div>
              <div class="myFriend-list">
                <div
                  v-for="(friend, idx) in filteredFriends"
                  :key="idx"
                  class="friend-tag-table"
                >
                  <span class="friend-tag-friendName" @click="searchingFriendTag(friend.user_id)">
                    {{ friend.user_nickname }}
                  </span>
                </div>
              </div>
            </div>
            <div v-if="currentTab=== 'tag'">
              <div class="friend-tag-myFriend-title">
                <div class="friend-tag-search-logo">
                  <i class="fas fa-search"></i>
                </div>
                <input
                  type="text"
                  placeholder="태그 검색"
                  class="friend-tag-search-input"
                  v-model="tagSearchInput"
                >

              </div>
              <div class="myFriend-list">
                <div
                  v-for="(tag, idx) in filteredTags"
                  :key="idx"
                  class="friend-tag-table"
                >
                  <span class="search-tag-friendName" @click="searchWithTag(tag.tag_content)">
                    #{{ tag.tag_content}} <span class="search-tag-friendName-count">게시물 {{tag.count}}개</span>
                  </span>
                </div>
              </div>
            </div>
          </div>
          <div class="friend-modal-layer"></div>
        </div>
      
      </div>
    </div>
    <!--  -->
    <div class="text-modal" v-if="abShow">
      <div class="text-modal-wrapper">
        <AorBModal
          :a="'전체 피드 보기'"
          :b="'팔로잉 피드 보기'"
          @selectA="selectA"
          @selectB="selectB"
          @modalOff="AorBmodalOff"
        />
      </div>
    </div>

    <div class="text-modal" v-if="yesNoShow">
      <div class="text-modal-wrapper">
        <YesNoModal
          :contents="yesNoModalContents"
          @modalOff="yesNoOff"
          @deleteDiary="reportFeedOn"
        />
      </div>
    </div>
  </div>
</template>

<script>
import SimpleModal from "../../components/common/SimpleModal"
import YesNoModal from "../../components/common/YesNoModal"
import AorBModal from "../../components/common/AorBModal"
import FeedItem2 from "../../components/feed/FeedItem2.vue";
import Navbar from "../../components/Nav/Navbar"
import VueJwtDecode from "vue-jwt-decode"
import axios from "axios";
import Tabs from 'vue-tabs-with-active-line'; 
const SERVER_URL = process.env.VUE_APP_SERVER_URL;
const TABS = [{
    title: '친구',
    value: 'friend',
  }, {
    title: '태그',
    value: 'tag',
  }];

export default {
  name: "FeedMain",
  props: {
    searchedTag: {
      type: String,
    },
  },
  data: function () {
    return {
      newFeeds:[],
      feeds: [],
      tags: [],
      tag: "",
      tag2: "",
      tagInput: "",
      followingCheck: false,
      followings: [],
      allFeedData:[],
      allFileData:[],
      allPetTagData:[],
      allFriendTagData:[],
      allLikeData:[],
      allTagData:[],
      usersInfo: [],
      myId: "",
      myNickname: "",
      yourNickname: "",
      f: 100,
      simpleModalContents:0,
      simpleShow:false,
      yesNoShow: false,
      yesNoModalContents: "이 게시글을 신고하시겠습니까?",

      friendShow: false,
      friends: [],
      filteredFriends: [],
      filteredTags: [],
      friendSearchInput: "",
      tagSearchInput: "",
      tabs:TABS,
      currentTab: 'friend',
      searchKeyword: "",
      allTagForSearch: [],
      abShow: false,
      followingImg: "http://i4a403.p.ssafy.io/images/wholefeed.jpg",
      // followingImg: require("../../assets/images/wholefeed.jpg"),
    }
  },
  computed: {
    filteredFeeds: function () {
      if (this.searchKeyword) {
        return this.feeds.filter( x => {
          var match = false
          for (let tag of x.tags) {
            // console.log(tag)
            if (tag == this.searchKeyword) {
              match = true
              break
            }
          }
          return match
        })
      } else {
        return this.feeds
      }
    },
  },
  methods: {
    AorBmodalOff:function(){
      this.abShow = !this.abShow
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
            this.followings.push(userId)
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
        this.followings.splice(this.followings.indexOf(userId), 1)
      })
      .catch( (err ) => {
        console.log(err)
        const msg = "문제가 발생했습니다."
        alert(msg)
      });
    },
    followingFeed: function (feed) {
      for (var user_id of this.followings) {
        if (user_id == feed.user_id) {
          return true
        }
      }
      return false
    },
    selectA: function () {
      this.followingCheck = false
      this.abShow = !this.abShow
      this.followingImg = "http://i4a403.p.ssafy.io/images/wholefeed.jpg"
      // this.followingImg = require("../../assets/images/wholefeed.jpg")
    },
    selectB: function () {
      this.followingCheck = true
      this.abShow = !this.abShow
      this.followingImg = "http://i4a403.p.ssafy.io/images/following.jpg"
      // this.followingImg = require("../../assets/images/following.jpg")
    },
    abOn: function () {
      this.abShow = !this.abShow
    },
    cancleSearchKeyword: function () {
      this.searchKeyword = ""
    },
    searchWithTag: function (tag) {
      this.searchKeyword = tag
      this.friendShow = !this.friendShow
    },
    friendModal: function () {
      this.friendShow = !this.friendShow
    },
    handleClick(newTab) {
      this.currentTab = newTab;
    },
    // addTag: function () {
    //   this.tags.push(this.tagInput)
    //   this.tagInput = ""
    //   // console.log(this.feeds[0])
    // },
    // cancelTag: function () {
    //   this.tag2 = ""
    //   this.tags = []
    // },
    getUserInfo: function () {
      axios.get(`${SERVER_URL}/member/getallmember`)
      .then(({data}) => {
        // console.log({data})
        this.usersInfo = data
        // console.log(this.usersInfo)
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getFriendTags: function () {
      axios.get(`${SERVER_URL}/feed/allFriendTag`)
      .then(({data}) =>{
        this.allFriendTagData = data;
        // console.log(this.allFriendTagData);
        this.getSum();
      })
      .catch( (err) => {
        // console.log(err)
      })
    },
    getLikeList: function () {
      axios.get(`${SERVER_URL}/like/list`)
      .then( (res) => {
        // console.log(res)
        this.allLikeData = res.data
        this.getFriendTags()
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
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getTags: function(){
      axios.get(`${SERVER_URL}/feed/allTag`)
      .then(({data}) =>{
        this.allTagData = data;
        this.getPetTags();
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getFiles: function(){
      axios.get(`${SERVER_URL}/feed/allFile`)
      .then(({data}) =>{
        this.allFileData = data;
        this.getTags();
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getFeeds: function () {
      axios.get(`${SERVER_URL}/feed/allFeed`)
      .then(({data}) =>{
        this.allFeedData = data;
        this.getFiles();
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    followingCheck2: function () {
      if (this.followingCheck) {
        this.followingCheck = false
      } else {
        this.followingCheck = true
      }
    },
    createFeed: function () {
      this.$router.push({ name: 'CreateFeed'})
    },
    searchingTag: function (taged) {
      this.searchKeyword = taged
    },
    searchingFriendTag: function (user_id) {
      this.$router.push({ name: 'UserProfile', params: { user_id: user_id } })
    },
    getSum: function(){
      // console.log(this.allFeedData)
      // console.log(this.allFileData)
      for(let i=0; i<this.allFeedData.length; i++){
        var temp = {};
        temp["feed_id"] = this.allFeedData[i].feed_id;
        temp["user_id"] = this.allFeedData[i].user_id;
        temp["feed_date"] = this.allFeedData[i].feed_date;
        for (let userInfo of this.usersInfo) {
          if (this.allFeedData[i].user_id == userInfo.user_id) {
            temp["nickname"] = userInfo.user_nickname
            break
          }
        }
        temp["feed_content"] = this.allFeedData[i].feed_content;
        temp["file_name"] = [];
        temp["file_type"] = []
        temp["pets"] = []
        temp["tags"] = []
        temp["mentions"] = []
        temp["like"] = []
        temp["friends"] = []
        temp["friends_id"] = []
        // temp["like_nickname"] = []
        temp["image_filters"] = []
        for(let j = 0; j < this.allFileData.length; j++){
          if(temp["feed_id"] == this.allFileData[j].feed_id){
            temp["file_name"].push(this.allFileData[j].file_name);
            temp["file_type"].push(this.allFileData[j].file_type)
            temp["image_filters"].push(this.allFileData[j].filter_name)
            // console.log("필터이름:  " + this.allFileData[j].filter_name)
          } 
        }
        for(let j = 0; j < this.allPetTagData.length; j++){
          if(temp["feed_id"] == this.allPetTagData[j].feed_id){
            temp["pets"].push(this.allPetTagData[j].pet_name);
          }
        }
        for(let j = 0; j < this.allLikeData.length; j++){
          if(temp["feed_id"] == this.allLikeData[j].feed_id){
            temp["like"].push(this.allLikeData[j].user_id)
            // temp["like_nickname"].push(this.allLikeData[j].user_nickname)
          }
        }
        for(let j = 0; j < this.allTagData.length; j++){
          if(temp["feed_id"] == this.allTagData[j].feed_id){
            temp["tags"].push(this.allTagData[j].tag_content);
          }
        }
        for(let j = 0; j < this.allFriendTagData.length; j++){
          if(temp["feed_id"] == this.allFriendTagData[j].feed_id){
            temp["friends"].push(this.allFriendTagData[j].friend_nickname);
            temp["friends_id"].push(this.allFriendTagData[j].friend_id);
          }
        }
        if (this.feeds.length < 1) {
          this.feeds.push(temp);
        } else {
          this.feeds.splice(0, 0, temp)
        }
        
        // console.log(temp)
        // console.log(this.allTagData)
      }
      // console.log(this.feeds);
      // for(let i=0; i<this.feeds.length; i++){
      //   console.log((i + 1) +" 합쳐진 객체: " + this.feeds[i].feed_id);
      // }
    },
    getMyId: function () {
      const token = localStorage.getItem('jwt')
      if (token) {
        this.myId = Number(VueJwtDecode.decode(token).user.user_id)
        // console.log(`내 아이디 ${this.myId}`)
      }
      this.getMyFollowing()
    },
    deleteFeed: function (feed_id) {
      // console.log(`지워 ${feed_id}`)
      axios.delete(`${SERVER_URL}/feed/deletion/${feed_id}`)
      .then( (res) => {
        // console.log(res)
        for (let i = 0; i < this.feeds.length; i++) {
          if (this.feeds[i].feed_id == feed_id) {
            this.feeds.splice(i, 1)
            break
          }
        }
        this.f += 100
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    readFeed: function (feed_id) {
      this.$router.push({ name: "ReadFeed", params: {feed_id: feed_id} })
    },
    reportFeed: function (feed_id) {
      // console.log(`신고 ${feed_id}`)
      this.yesNoShow = !this.yesNoShow
    },
    yesNoOff: function () {
      this.yesNoShow = !this.yesNoShow
    },
    reportFeedOn: function () {
      this.yesNoShow = !this.yesNoShow
      this.simpleShow=true
      this.simpleModalContents="신고가 접수되었습니다."
    },
    updateFeed: function (feed_id) {
      this.$router.push({ name: "ModifyFeed", params: {feed_id: feed_id} })
    },
    simpleOff: function () {
      this.simpleShow=false
    },
    getAllTag: function () {
      axios.get(`${SERVER_URL}/feed/allCount`)
      .then( (res) => {
        // console.log(res)
        // console.log("모든 태그 ㅋ")
        this.allTagForSearch = res.data
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getMyFollowing: function () {
      axios.post(`${SERVER_URL}/member/following/${this.myId}`)
        .then(({data}) => {
          this.followings.push(this.myId)
          for (let following of data) {
            this.followings.push(
              following.to_id
            )
          }
        })
        .catch( (err) => {
          console.log(err)
        })
    },
    
  },
  created: function () {
    this.getUserInfo()
    this.getFeeds();
    this.getMyId()
    this.getAllTag()
    // console.log("찍히나 ?")
    // console.log(this.feeds)
  },
  watch: {
    searchedTag: function () {
      if (this.searchedTag) {
      // console.log(this.searchedTag)
      }
    },
    friendSearchInput: function () {
      if (this.friendSearchInput.length < 1) {
        this.filteredFriends = "";
      } else {
        this.filteredFriends = this.usersInfo.filter(friend => {
          return friend.user_nickname.match(this.friendSearchInput)
        })
      }
      // console.log(this.filteredFriends)
    },
    tagSearchInput: function () {
      // console.log(this.allTagForSearch)
      if (this.tagSearchInput.length < 1) {
        this.filteredTags = "";
      } else {
        this.filteredTags = this.allTagForSearch.filter(tag => {
          return tag.tag_content.match(this.tagSearchInput)
        })
      }
      //console.log(this.filteredTags)
    },
  },

  components: {
    FeedItem2,
    SimpleModal,
    YesNoModal,
    AorBModal,
    Navbar,
    Tabs,
  },
};
</script>

<style>

.feed-search-container-div {
  width: 100%;
  display: flex;
  justify-content: center;
}

.feed-search-container {
  position: fixed;
  width: 40vw;
  height: 10vh;
  top: 0vh;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 99;
}

.feed-search-logo {
  display: flex;
  align-items: center;
  width: 40vw;
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
  height: 6vh;
  padding: 1vh;
}

.feed-search-logo:hover {
  cursor: pointer;
}

.feed-search-btn-div {
  display: flex;
  align-items: center;
  width: 30vw;
}

.feed-search-btn-div:hover {
  display: flex;
  align-items: center;
  width: 30vw;
}

.feed-search-btn {
  font-size: 2vh;
  /* background-color: white; */
}

.friend-search-mode-img {
  width: 3vh;
  height: 3vh;
  margin-right: 1vh;
  border-radius: 70%;
}

.friend-search-mode:hover {
  cursor: pointer;
}

.feed-search-keyword {
  font-size: 2vh;
  margin-left: 1vh;
  color: gray;
}

.feed-search-keyword-cancle {
  font-size: 2vh;
  margin-left: 0.5vh;
}

.feed {
  background-color: white;
  /* overflow: scroll; */
  /* overflow: hidden; */
}

.feedModal {
  background-color: white;
  /* overflow: hidden;
  height: 100%;
  min-height: 100%; */
}

/* 
.wrapB {
  overflow: hidden;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
} */

.writing {
  font-size: 8vw;
  z-index: 10;
}

.write-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 8vw;
  height: 8vw;
  z-index: 10;
}

.write-btn {
  border: 0.5vw solid rgb(248, 248, 248);
  background-color: rgb(248, 248, 248);
  border-radius: 70%;
  padding: 2vw;
  position: fixed;
  top: 73%;
  left: 80%;
  color: blue;
  box-shadow: 0.5vw 0.5vw gray;
  z-index: 10;
  opacity: 0.5;
}

.write-btn:hover {
  opacity: 1;
}

.tabs {
  position: relative;
  margin: 0 auto;
}

.tabs__item {
  display: inline-block;
  margin: 0 5px;
  padding: 10px;
  padding-bottom: 8px;
  font-size: 16px;
  letter-spacing: 0.8px;
  color: gray;
  text-decoration: none;
  border: none;
  background-color: transparent;
  border-bottom: 2px solid transparent;
  cursor: pointer;
  transition: all 0.25s;
}

.tabs__item_active {
  color: black;
}

.tabs__item:hover {
  border-bottom: 2px solid gray;
  color: black;
}

.tabs__item:focus {
  outline: none;
  border-bottom: 2px solid gray;
  color: black;
}

.tabs__item:first-child {
  margin-left: 0;
}

.tabs__item:last-child {
  margin-right: 0;
}

.tabs__active-line {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 2px;
  background-color: black;
  transition: transform 0.4s ease, width 0.4s ease;
}
.search{
    position : relative;
    width: 100%;
    height: 8vw;
  }
.tag-friends{
   position : absolute;
   left:50%;
   top:50%;
   transform: translate(-50%,-50%);

}

.search-tag-friendName {
  margin-left: 5%;
  margin-top: 1vw;
  width: 80%;
  font-size: 1.5vw;
  font-weight: bold;
}

.search-tag-friendName-count {
  margin-top: 1vw;
  font-size: 1.3vw;
  color: gray;
}

.search-tag-friendName-count:hover {
  cursor: pointer;
}

.search-tag-friendName:hover {
  cursor: pointer;
}

@media ( max-width: 540px) {

  .writing {
    font-size: 8vw;
  }

  .container {
    width: 100%;
  }

  .write-btn {
  position: fixed;
  top: 85%;
  left: 75%;
  }

  .search-tag-friendName {
    margin-left: 5%;
    margin-top: 1vw;
    font-size: 5vw;
    font-weight: bold;
  }

  .search-tag-friendName-count {
    font-size: 4vw;
  }

}

</style>
