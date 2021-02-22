<template>
  <div class="read-feed-item">
    <Navbar
      style="position:sticky; top:0; z-index:15; width: 100%;"
    >
    </Navbar>
    <div class="read-top-container">
      <div class="read-feed-header">
        <div class="read-feed-header-name">
          <span class="read-feed-user" @click="moveToUserProfile" style="font-family: GmarketSansMedium;">{{ feed.nickname }}</span>
          <span class="read-feed-pet" v-for="(pet, idx) in feed.pets" :key="idx" style="font-family: GmarketSansMedium;">
            <span> {{ pet }}</span>
          </span>
        </div>
        <div class="read-feed-header-dots">
          <button v-if="myId == feed.user_id" class="read-my-feed-dots" @click="simpleOn2">
            <i class="fas fa-ellipsis-h"></i>
          </button>
          <button v-else class="read-others-feed-dots" @click="simpleOn">
            <i class="fas fa-ellipsis-h"></i>
          </button>
          <div class="selectModal" v-if="simpleShow">
            <SelectModal
              :contents="contents"
              @modalOff="simpleOff"
              @report="reportFeed"
            />
          </div>
          <div class="selectModal" v-if="simpleShow2">
            <SelectModal
              :contents="contents2"
              @modalOff="simpleOff2"
              @delete="yesNoOff"
              @update="updateFeed"
            />
          </div>
        </div>
      </div>
      <div class="read-feed-row">
        <div class="read-feed-container">
          <div class="read-feed-img-container">
            <div v-for="(image, idx) in imgs" :key="'A'+idx">
              <div class="read-feed-img-div" v-if="idx == imgIdx && feed.file_type[idx] == 'image'">
                <div class="read-feed-img-div-center">
                  <div :class="feed.image_filters[idx]">
                    <img
                      :class="{'feed-img': imgState, 'feed-img-none': !imgState}"
                      :src="image"
                    >
                  </div>
                </div>
              </div>
              <div class="read-feed-img-div" v-else-if="idx == imgIdx && feed.file_type[idx] == 'video'">
                <div class="read-feed-img-div-center">
                  <div :class="feed.image_filters[idx]">
                    <video
                      :class="{'feed-img': imgState, 'feed-img-none': !imgState}"
                      :src="image"
                      autoplay
                      muted
                      playsinline
                      loop
                      controls
                    ></video>
                  </div>
                </div>
              </div>
            </div>
            <button
              v-if="0 < imgIdx"
              class="read-pre-btn"
              @click="preImg"
            >
              <i class="fas fa-angle-left"></i>
            </button>
            <button
              v-if="imgIdx < imgs.length-1"
              class="read-next-btn"
              @click="nextImg"
            >
              <i class="fas fa-angle-right"></i>
            </button>
            <div class="read-feed-function-container">
              <section class="read-heart-chat-container">
                <div class="read-heart">
                  <button class="read-empty-heart" v-if="!isLiking" @click="giveHeart">
                    <i class="fas fa-heart"></i>
                  </button>
                  <button class="read-full-heart" v-else @click="deleteHeart">
                    <i class="fas fa-heart"></i>
                  </button>
                </div>
                <div class="read-chat" @click="focusChat">
                  <i class="far fa-comment"></i>
                </div>
              </section>
              <div class="read-dot-container">
                <span
                  v-for="(dot, idx) in imgs.length"
                  :key="'C'+idx"
                  class="read-idx-dot"
                >
                  <div @click="changeImgIdx(idx)">
                    <button v-if="idx != imgIdx" class="read-idx-btn-off">
                    </button>
                    <button v-else class="read-idx-btn-on">
                    </button>
                  </div>
                </span>
              </div>
            </div>
          </div>
          <div class="read-feed-content-container">
            <div class="read-feed-like-section">
              <div class="read-feed-like" style="font-family: GmarketSansMedium;" @click="ReadLike">
                <!-- 좋아요 목록으로 받아서 length로 하는게 맞다. -->
                좋아요 {{ like.length }}개
              </div>
              <div class="read-feed-time" style="font-family: GmarketSansMedium;">
                {{ returnedTime }}
              </div>
            </div>
          </div>
        </div>
        <div class="read-feed-content-section-container">
          <div class="read-feed-content-section">
            <section class="read-feed-content-content">
              <span class="read-feed-content-nickname" style="font-family: GmarketSansMedium;">
                {{feed.nickname}}
              </span>
              <span v-if="feed.feed_content.length < 30" class="read-feed-content" style="font-family: GmarketSansMedium;">
                {{feed.feed_content}}
              </span>
              <span v-else-if="feed.feed_content.length >= 30 && contentAdd" class="read-feed-content" style="font-family: GmarketSansMedium;">
                <span>
                  {{feed.feed_content}}
                </span>
                <span @click="readContentAdd" class="read-feed-content-add">
                  줄이기
                </span>
              </span>
              <span v-else-if="feed.feed_content.length >= 30" class="read-feed-content" style="font-family: GmarketSansMedium;">
                <span>
                  {{feed.feed_content.slice(0, 30)}} ...
                </span>
                <span @click="readContentAdd" class="read-feed-content-add">
                  더 보기
                </span>
              </span>
            </section>
          </div>
          <span
            class="read-feed-tag-section"
            v-for="(tag, idx) in feed.tags" 
            :key="'B'+idx">
            <span
              class="read-feed-tag"
              style="font-family: GmarketSansMedium;"
              @click="searchTag(tag)"
            >#{{tag}} </span>
          </span>
          <div><span
            class="read-feed-tag-section"
            v-for="(tag, idx) in feed.friends"
            :key="'C'+idx">
            <span
              class="read-feed-friend-tag"
              style="font-family: GmarketSansMedium;"
              @click="searchFriendTag(feed.friends_id[idx])"
            >@{{tag}} </span>
          </span></div>

          <memo-write
            v-bind:feed_id="article.feed_id"
            :reloadMemos="reloadMemos"
            ref="memoInput"
          />
          <div class="feed-comment-section">
            <div v-if="0 < memos.length" class="feed-comment">
              <memo-row
                v-if="isShow"
                :memo="memos[0]"
                :feed_id="article.feed_id"
                :feed_nickname="article.feed_nickname"
                :reloadMemos="reloadMemos"
              />
              <span class="feed-comment-notice" style="font-family: GmarketSansMedium;" v-if="isShow"  @click="showEvent">
                댓글 모두 보기
              </span>
              <span class="feed-comment-notice" style="font-family: GmarketSansMedium;" v-if="isClosed" @click="showEvent">
                댓글 감추기
              </span>
              <div v-if="isClosed">
                <memo-row
                  v-for="(memo, index) in memos"
                  :key="index"
                  :memo="memo"
                  :feed_id="article.feed_id"
                  :feed_nickname="article.feed_nickname"
                  :reloadMemos="reloadMemos"
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- 삭제 -->
    <div class="text-modal" v-if="yesNoShow">
      <div class="text-modal-wrapper">
        <YesNoModal
          :contents="yesNoModalContents"
          @modalOff="yesNoOff"
          @deleteDiary="deleteFeed"
        />
      </div>
    </div>
    <!-- 신고 -->
    <div class="text-modal" v-if="yesNoShow2">
      <div class="text-modal-wrapper">
        <YesNoModal
          :contents="yesNoModalContents2"
          @modalOff="yesNoOff2"
          @deleteDiary="reportFeedOn"
        />
      </div>
    </div>
    <!-- 신고접수 -->
    <div class="text-modal" v-if="reportShow">
      <div class="text-modal-wrapper">
        <SimpleModal
          :contents="reportModalContents"
          @modalOff="reportOff"
        />
      </div>
    </div>
    <!-- 좋아요 리스트 -->
    <div class="text-modal" v-if="likeShow">
      <div class="text-modal-wrapper">
        <FollowModal
          :myFollowings="followings"
          :followList="like"
          :title="'좋아요'"
          :myId="myId"
          :userId="myId"
          @modalOff="ReadLike"
          @follow="follow"
          @unfollow="unfollow"
        />
      </div>
    </div>
  </div>
</template>

<script>

import YesNoModal from "../../components/common/YesNoModal"
import SelectModal from "../../components/common/SelectModal"
import FollowModal from "../../components/user/FollowModal"
import SimpleModal from "../../components/common/SimpleModal"
import Navbar from "../../components/Nav/Navbar"
import VueJwtDecode from "vue-jwt-decode"
import MemoWrite from "../../components/feed/MemoWrite";
import MemoRow from "../../components/feed/MemoRow";
import { listMemo } from "@/api/memo.js";
import axios from 'axios'
import moment from 'moment/moment'

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "ReadFeed",
  components: {
    YesNoModal,
    FollowModal,
    SelectModal,
    SimpleModal,
    MemoWrite,
    MemoRow,
    Navbar,
  },
  props: {
    feed_id: {
      type: Number,
    }
  },
  data: function () {
    return {
      myId: 0,
      imgs: [],
      imgIdx: 0,
      pre: "<",
      next: ">",
      imgState: true,
      isLiking: false,
      contents: [
        "신고",
      ],
      contents2: [
        "삭제",
        "수정",
      ],
      simpleShow: false,
      simpleShow2: false,
      yesNoShow: false,
      yesNoModalContents: "정말 삭제하시겠습니까 ?",
      yesNoShow2: false,
      yesNoModalContents2: "이 게시글을 신고하시겠습니까?",
      reportShow: false,
      reportModalContents: "",
      contentAdd: false,
      like: [],
      preFeed: [],
      feed: {
        "feed_id": "",
        "user_id": "",
        "feed_date": new Date(),
        "nickname": "",
        "feed_content": "",
        "file_name": "",
        "file_type": "",
        "pets": "",
        "tags": "",
        "friend_tags": "",
        "mentions": "",
        "like": "",
        "image_filters": "",
      },
      users: [],
      isModalViewed:false,
      memos: [],
      article: {
        feed_id: 0,
        feed_nickname:"",
        content: "",
        userid: "",
        // hit: "", // 조회수
        regtime: "" // 작성일
      },
      isShow: true,
      isClosed:false,
      likeShow: false,
      followings: [],
    }
  },
  methods: {
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
    ReadLike: function () {
      this.likeShow = !this.likeShow
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
    focusChat: function () {
      this.$refs.memoInput.$refs.memosinput.focus()
    },
    showEvent: function () {
      this.isShow = !this.isShow;
      this.isClosed = !this.isClosed;
    },
    listArticle: function() {
      this.$router.push({ name: "board-list" });
    },
    moveModifyArticle: function() {
      this.$router.replace({
        name: "board-modify",
        params: { feed_id: this.article.feed_id }
      });
      //   this.$router.push({ path: `/board/modify/${this.article.feed_id}` });
    },
    reloadMemos: function (memolist) {
      // JS배열의 filter함수 arr.filter(콜백함수); 조건을 만족하는 원소만 담는다
      this.memos = memolist.filter((memo) => {
        return memo.feed_id == this.article.feed_id;
      });
      listMemo(
        this.feed.feed_id,
        (response) => {
          this.memos = response.data;
        },
        (error) => {
          console.error(error);
        }
      );
    },
    getUsersInfo: function () {
      axios.get(`${SERVER_URL}/member/getallmember`)
      .then( (res) => {
        this.users = res.data
        this.getMyId()
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getFeed: function () {
      axios.get(`${SERVER_URL}/feed/one/${this.$route.params.feed_id}`)
      .then( (res) => {
        //  console.log(res+" 피드!!!")
        this.preFeed = res.data
        this.getSum()
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getSum: function(){
      var temp = {};
      // console.log(this.preFeed)
      temp["feed_id"] = this.preFeed.feed.feed_id;
      temp["user_id"] = this.preFeed.feed.user_id;
      temp["feed_date"] = this.preFeed.feed.feed_date;
      for (let userInfo of this.users) {
        if (this.preFeed.feed.user_id == userInfo.user_id) {
          temp["nickname"] = userInfo.user_nickname
          break
        }
      }
      temp["feed_content"] = this.preFeed.feed.feed_content;
      temp["file_name"] = [];
      temp["file_type"] = []
      temp["pets"] = []
      temp["tags"] = []
      temp["friend_tags"] = []
      temp["mentions"] = []
      temp["like"] = []
      temp["friends"] = []
      temp["friends_id"] = []
      // temp["like_nickname"] = []
      temp["image_filters"] = []
      for (let j = 0; j < this.preFeed.files.length; j++) {
        // console.log("여긴오녀ㅑ ?")
        temp["file_name"].push(this.preFeed.files[j].file_name)
        temp["file_type"].push(this.preFeed.files[j].file_type)
        temp["image_filters"].push(this.preFeed.files[j].filter_name)
      }
      // console.log("여긴오녀ㅑ ?")
      for (let j = 0; j < this.preFeed.petTag.length; j++) {
          temp["pets"].push(this.preFeed.petTag[j].pet_name)
      }
      for (let j = 0; j < this.preFeed.friendTag.length; j++) {
          temp["friend_tags"].push(this.preFeed.friendTag[j].friend_nickname)
      }
      for (let j = 0; j < this.preFeed.likes.length; j++) {
          temp["like"].push(this.preFeed.likes[j].user_id)
        }
      for (let j = 0; j < this.preFeed.Tag.length; j++) {
          temp["tags"].push(this.preFeed.Tag[j].tag_content);
      }
      for (let j = 0; j < this.preFeed.friendTag.length; j++) {
          temp["friends"].push(this.preFeed.friendTag[j].friend_nickname);
          temp["friends_id"].push(this.preFeed.friendTag[j].friend_id);
      }
      this.feed = temp
      for (let img of this.feed.file_name) {
        this.imgs.push("http://i4a403.p.ssafy.io/images/"+img)
        // this.imgs.push(require("../../assets/images/FeedImg/"+img))
      }
      // this.getComments()
      this.checkLiking()
      this.getMyFollowing()
      // 메모
      this.article.feed_id = this.feed.feed_id;
      this.article.content = this.feed.feed_content;
      this.article.user_id = this.myId;
      this.article.feed_nickname = this.feed.nickname;
      listMemo(
        this.feed.feed_id,
        (response) => {
          this.memos = response.data;
        },
        (error) => {
          console.error(error);
        }
      )
    },
    getMyId: function () {
      const token = localStorage.getItem('jwt')
      if (token) {
        this.myId = VueJwtDecode.decode(token).user.user_id
      }
      this.getFeed()
    },
    searchTag: function (tag) {
      this.$emit('searchingTag', tag)
    },
    searchFriendTag: function (user_id) {
      this.$router.push({ name: 'UserProfile', params: { user_id: user_id } })
    },
    preImg: function () {
      if (0 < this.imgIdx) {
        setTimeout(() => {
          this.imgState = false
        }, 0);
        setTimeout(() => {
          this.imgIdx -= 1
          this.imgState = true
        }, 500);
      }
    },
    nextImg: function () {
      if (this.imgIdx < this.imgs.length) {
        setTimeout(() => {
          this.imgState = false
        }, 0);
        setTimeout(() => {
          this.imgIdx += 1
          this.imgState = true
        }, 500);
      }
    },
    changeImgIdx: function (idx) {
      setTimeout(() => {
          this.imgState = false
        }, 0);
        setTimeout(() => {
          this.imgIdx = idx
          this.imgState = true
        }, 500);
    },
    getComments: function () {
      // axios로 이 글 id에 달린 댓글 다 가져오기.
      // 아래는 예비
      this.comments = [
        {
          user_id: 1,
          content: "개 이쁘당"
        },
        {
          user_id: 2,
          content: "졸귀"
        },
      ]
    },
    moveToUserProfile: function () {
      this.$router.push({ name: 'UserProfile', params: { user_id: `${this.feed.user_id}` } })
    },
    giveHeart: function () {
      const credentials = {
        "user_id": this.myId,
        "feed_id": this.feed.feed_id,
      }
      axios.post(`${SERVER_URL}/like/new`, credentials)
      .then( (res) => {
        // console.log(res)
        this.isLiking = true
        this.like.push(this.myId)
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    deleteHeart: function () {
      axios.delete(`${SERVER_URL}/like/delete/`,{
        data : {
          user_id : this.feed.user_id,
          feed_id : this.feed.feed_id
        }
      })
      .then( (res) => {
        // console.log(res)
        this.isLiking = false
        this.like.splice(this.like.indexOf(this.myId, 1))
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    simpleOn: function () {
      this.simpleShow = !this.simpleShow
    },
    simpleOff: function () {
      this.simpleShow = !this.simpleShow
    },
    yesNoOff: function () {
      this.yesNoShow = !this.yesNoShow
    },
    simpleOn2: function () {
      this.simpleShow2 = !this.simpleShow2
    },
    simpleOff2: function () {
      this.simpleShow2 = !this.simpleShow2
    },
    reportFeed: function () {
      this.yesNoShow2 = !this.yesNoShow2
    },
    reportFeedOn: function () {
      this.yesNoShow2 = !this.yesNoShow2
      this.reportShow = true
      this.reportModalContents="신고가 접수되었습니다."
    },
    reportOff: function () {
      this.reportShow = false
    },
    yesNoOff2: function () {
      this.yesNoShow2 = !this.yesNoShow2
    },
    deleteFeed: function () {
      axios.delete(`${SERVER_URL}/feed/deletion/${this.$route.params.feed_id}`)
      .then( (res) => {
        // console.log(res)
        this.$router.push({ name: "FeedMain" })
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    updateFeed: function () {
      this.$router.push({ name: "ModifyFeed", params: {feed_id: this.$route.params.feed_id} })
    },
    readContentAdd: function () {
      this.contentAdd = !this.contentAdd
    },
    checkLiking: function () {
      this.like = this.feed.like
      if (this.like.includes(this.myId)) {
        this.isLiking = true
      }
    },
  },
  computed: {
    returnedTime: function () {
      const today = moment(new Date(),'YYYY.MM.DD HH:mm:ss').toDate();
      const timeValue = moment((this.feed.feed_date),'YYYY.MM.DD HH:mm:ss').toDate();
      const betweenTime = Math.floor((today-timeValue) / 1000 / 60); 
      if (betweenTime < 1) return '방금전';
      if (betweenTime < 60) {
          return `${betweenTime}분전`;
      }

      const betweenTimeHour = Math.floor(betweenTime / 60);
      if (betweenTimeHour < 24) {
          return `${betweenTimeHour}시간전`;
      }

      const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
      if (betweenTimeDay < 365) {
          return `${betweenTimeDay}일전`;
      }

      return `${Math.floor(betweenTimeDay / 365)}년전`;
    },
    message: function() {
      return this.article.content.split("\n");
    },
  },
  created: function () {
    this.getUsersInfo()
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
.read-feed-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.read-top-container {
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
  margin-top: 2vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  min-height: 0;
  /* width: 50vw;
  max-width: 550px; */
  background-color: white;
}

.read-feed-row {
  display: flex;
  justify-content: center;
}

.read-feed-container {
  /* border: 0.1vw solid rgba(175, 175, 175, 0.5); */
  width: 50vw;
  max-width: 550px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.read-feed-header {
  width: 100%;
  padding: 1vw;
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.read-feed-header-name {
  display: flex;
}

.read-feed-header-dots {
  position: relative;
  font-size: 22px;
}

.read-feed-header-dots:hover {
  cursor: pointer;
}

.read-feed-user {
  font-weight: bold;
  font-size: 22px;
}

.read-feed-user:hover {
  cursor: pointer;
}

.read-feed-pet {
  font-weight: bold;
  font-size: 22px;
  margin-left: 0.5vw;
  color: mediumslateblue;
}

.read-feed-img-container {
  position: relative;
  width: 50vw;
  max-width: 550px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: white;
  border: none;
}

.read-feed-img-div {
  position: relative;
  width: 50vw;
  max-width: 550px;
  height: 50vw;
  max-height: 550px;
  overflow: hidden;
  border: none;
}

.read-feed-img-div-center {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  -webkit-transform: translate(50%, 50%);
  -ms-transform: translate(50%, 50%);
  transform: translate(50%, 50%);
}

.read-feed-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 50vw;
  max-width: 550px;
  /* height: 50vw; */
  transition-duration: 0.5s;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  opacity: 1;
}

.read-feed-img-none {
  position: absolute;
  top: 0;
  left: 0;
  width: 50vw;
  max-width: 550px;
  /* height: 50vw; */
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  transition-duration: 0.5s;
  opacity: 0.1;
}

.read-pre-btn {
  position: absolute;
  color: white;
  background-color: rgb(44, 41, 41);
  padding: 15px;
  top: 227px;
  left: 0%;
  transition-duration: 1.5s;
  font-size: 44px;
  font-weight: bold;
  opacity: 0.3;
}

.read-pre-btn:hover {
  color: white;
  background-color: rgb(44, 41, 41);
  opacity: 0.9;
}

.read-next-btn {
  position: absolute;
  color: white;
  background-color: rgb(44, 41, 41);
  padding: 15px;
  top: 227px;
  right: 0%;
  transition-duration: 1.5s;
  font-size: 44px;
  font-weight: bold;
  opacity: 0.3;
}

.read-next-btn:hover {
  color: white;
  background-color: rgb(44, 41, 41);
  opacity: 0.9;
}

.read-feed-function-container {
  width: 100%;
  display: flex;
}

.read-heart-chat-container {
  width: 20%;
  display: flex;
}

.read-heart {
  font-size: 24px;
  margin-left: 10px;
  margin-right: 10px;
}

.read-empty-heart {
  color: black;
  transition-duration: 0.5s;
}

.read-full-heart {
  color: red;
  transition-duration: 0.5s;
}

.read-chat {
  font-size: 24px;
}

.heart:hover {
  cursor: pointer;
}

.read-chat:hover {
  cursor: pointer;
}

.read-dot-container {
  width: 60%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.read-idx-dot {
  font-size: 15px;
  margin: 7px;
}

.read-idx-btn-on {
  width: 12px;
  height: 12px;
  border-radius: 70%;
  background-color: black;
}

.read-idx-btn-on:hover {
  cursor: pointer;
}

.read-idx-btn-off {
  width: 12px;
  height: 12px;
  border-radius: 70%;
  background-color: gray;
}

.read-idx-btn-off:hover {
  cursor: pointer;
}

.read-feed-like-section {
  display: flex;
  justify-content: space-between;
}

.read-feed-like {
  font-size: 22px;
  font-weight: bold;
}

.read-feed-like:hover {
  cursor: pointer;
}

.read-feed-time {
  font-size: 20px;
  font-weight: bold;
  color: rgb(71, 71, 71);
}

.read-feed-content-section-container {
  width: 350px;
  padding: 16px;
  max-height: 650px;
  border-left: 0.1vw solid rgba(175, 175, 175, 0.5);
  overflow: hidden;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.read-feed-content-section {
  width: 100%;
}

.read-feed-content-nickname {
  font-size: 20px;
  font-weight: bold;
}

.read-feed-content-nickname:hover {
  cursor: pointer;
}

.read-feed-content {
  font-size: 20px;
}

.read-feed-content-add {
  font-size: 20px;
  color: gray;
  /* font-weight: bold; */
}

.read-feed-content-add:hover {
  cursor: pointer;
}

.read-feed-tag {
  font-size: 20px;
  color: blue;
}

.read-feed-tag:hover {
  cursor: pointer;
}

.read-feed-friend-tag {
  font-size: 20px;
  color: tomato;
}

.read-feed-friend-tag:hover {
  cursor: pointer;
}

.read-feed-comment-notice {
  font-size: 20px;
  color: gray;
}

.read-feed-comment-notice:hover {
  cursor: pointer;
}

.read-comment-nickname {
  font-size: 20px;
  font-weight: bold;
}

.read-comment-nickname:hover {
  cursor: pointer;
}

.read-comment-comment {
  font-size: 20px;
}

.read-feed-content-container {
  padding: 1vw;
  width: 100%;
  background-color: white;
  border: none;
}

.selectModal {
  position: absolute;
  top: 0;
  right: 0;
  z-index: 10;
  width: 240px;
  transition-duration: 0.8s;
}

@media ( max-width: 1100px) {

  .read-top-container {
  min-height: 0;
  /* width: 50vw; */
  /* max-width: 550px; */
  background-color: white;
  }

  .read-feed-container {
    border: 0.1vw solid rgba(175, 175, 175, 0.5);
    width: 45vw;
  }

  .read-feed-header {
    width: 100%;
    padding: 1vw;
    background-color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .read-feed-header-name {
    display: flex;
  }

  .read-feed-header-dots {
    position: relative;
    font-size: 1.5vw;
  }

  .read-feed-header-dots:hover {
    cursor: pointer;
  }

  .read-feed-user {
    font-weight: bold;
    font-size: 2vw;
  }

  .read-feed-user:hover {
    cursor: pointer;
  }

  .read-feed-pet {
    font-weight: bold;
    font-size: 2vw;
    margin-left: 0.5vw;
    color: mediumslateblue;
  }

  .read-feed-img-container {
    position: relative;
    width: 45vw;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: white;
  }

  .read-feed-img-div {
    position: relative;
    width: 45vw;
    height: 45vw;
    overflow: hidden;
  }

  .read-feed-img-div-center {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    -webkit-transform: translate(50%, 50%);
    -ms-transform: translate(50%, 50%);
    transform: translate(50%, 50%);
  }


  .read-feed-img {
    position: absolute;
    top: 0;
    left: 0;
    width: 45vw;
    /* height: 50vw; */
    transition-duration: 0.5s;
    -webkit-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    opacity: 1;
  }

  .read-feed-img-none {
    position: absolute;
    top: 0;
    left: 0;
    width: 45vw;
    /* height: 50vw; */
    -webkit-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    transition-duration: 0.5s;
    opacity: 0.1;
  }

  .read-pre-btn {
    position: absolute;
    color: white;
    background-color: rgb(44, 41, 41);
    padding: 1vw;
    top: 42%;
    left: 0%;
    transition-duration: 1.5s;
    font-size: 3vw;
    font-weight: bold;
    opacity: 0.3;
  }

  .read-pre-btn:hover {
    color: white;
    background-color: rgb(44, 41, 41);
    opacity: 0.9;
  }

  .read-next-btn {
    position: absolute;
    color: white;
    background-color: rgb(44, 41, 41);
    padding: 1vw;
    top: 42%;
    right: 0%;
    transition-duration: 1.5s;
    font-size: 3vw;
    font-weight: bold;
    opacity: 0.3;
  }

  .read-next-btn:hover {
    color: white;
    background-color: rgb(44, 41, 41);
    opacity: 0.9;
  }

  .read-feed-function-container {
    width: 100%;
    display: flex;
  }

  .read-heart-chat-container {
    width: 20%;
    display: flex;
  }

  .read-heart {
    font-size: 2.5vw;
    margin-left: 1vw;
    margin-right: 1vw;
  }

  .read-chat {
    font-size: 2.5vw;
  }

  .heart:hover {
    cursor: pointer;
  }

  .read-chat:hover {
    cursor: pointer;
  }

  .read-dot-container {
    width: 60%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .read-idx-dot {
    font-size: 1vw;
    margin: 0.5vw;
  }

  .read-idx-btn-on {
    width: 1vw;
    height: 1vw;
    border-radius: 70%;
    background-color: black;
  }

  .read-idx-btn-on:hover {
    cursor: pointer;
  }

  .read-idx-btn-off {
    width: 1vw;
    height: 1vw;
    border-radius: 70%;
    background-color: gray;
  }

  .read-idx-btn-off:hover {
    cursor: pointer;
  }

  .read-feed-like-section {
    display: flex;
    justify-content: space-between;
  }

  .read-feed-like {
    font-size: 1.8vw;
    font-weight: bold;
  }

  .read-feed-time {
    font-size: 1.5vw;
    font-weight: bold;
    color: rgb(71, 71, 71);
  }

  
  .read-feed-content-section-container {
    width: 40vw;
    max-height: 53vw;
    padding: 1.2vw;
    overflow: hidden;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
  }

  .read-feed-content-section {
    width: 100%;
  }

  .read-feed-content-nickname {
    font-size: 1.8vw;
    font-weight: bold;
  }

  .read-feed-content-nickname:hover {
    cursor: pointer;
  }

  .read-feed-content {
    font-size: 1.8vw;
  }

  .read-feed-content-add {
    font-size: 1.8vw;
    color: gray;
    /* font-weight: bold; */
  }

  .read-feed-content-add:hover {
    cursor: pointer;
  }

  .read-feed-tag {
    font-size: 1.8vw;
    color: blue;
  }

  .read-feed-tag:hover {
    cursor: pointer;
  }

  .read-feed-friend-tag {
    font-size: 1.8vw;
    color: tomato;
  }

  .read-feed-friend-tag:hover {
    cursor: pointer;
  }

  .read-feed-comment-notice {
    font-size: 1.8vw;
    color: gray;
  }

  .read-feed-comment-notice:hover {
    cursor: pointer;
  }

  .read-comment-nickname {
    font-size: 1.8vw;
    font-weight: bold;
  }

  .read-comment-nickname:hover {
    cursor: pointer;
  }

  .read-comment-comment {
    font-size: 1.8vw;
  }

  .read-feed-content-container {
    padding: 1vw;
    width: 100%;
    background-color: white;
  }

  .selectModal {
    position: absolute;
    top: 0;
    right: 0;
    z-index: 10;
    width: 20vw;
    transition-duration: 0.8s;
  }

}

@media ( max-width: 540px) {

  .read-top-container {
    width: 100%;
    background-color: white;
  }

  .read-feed-header {
    width: 100%;
    padding: 1vw 3vw;
    border-bottom: none;
    background-color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .read-feed-header-name {
    display: flex;
  }

  .read-feed-header-dots {
    position: relative;
    font-size: 5vw;
  }

  .read-feed-header-dots:hover {
    cursor: pointer;
  }

  .read-feed-user {
    font-weight: bold;
    font-size: 5vw;
  }

  .read-feed-user:hover {
    cursor: pointer;
  }

  .read-feed-pet {
    font-weight: bold;
    font-size: 5vw;
    margin-left: 2vw;
    color: mediumslateblue;
  }

  .read-feed-row {
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
  }

  .read-feed-container {
    width: 91.1%;
    border: none;
  }

  .read-feed-img-container {
    width: 91.1vw;
  }

  .read-feed-content-container {
    width: 91.1vw;
  }

  .read-feed-img-div {
    width: 91.1vw;
    height: 91.1vw;
  }

  .read-feed-img {
    width: 91.1vw;
  }

  .read-feed-img-none {
    width: 91.1vw;
  }

  .read-pre-btn {
    position: absolute;
    color: white;
    background-color: rgb(44, 41, 41);
    padding: 2vw;
    /* top: 45vw; */
    left: 0%;
    transition-duration: 1.5s;
    font-size: 6vw;
    font-weight: bold;
    opacity: 0.3;
  }

  .read-pre-btn:hover {
    color: white;
    background-color: rgb(44, 41, 41);
    opacity: 0.9;
  }

  .read-next-btn {
    position: absolute;
    color: white;
    background-color: rgb(44, 41, 41);
    padding: 2vw;
    /* top: 45vw; */
    right: 0%;
    transition-duration: 1.5s;
    font-size: 6vw;
    font-weight: bold;
    opacity: 0.3;
  }

  .read-next-btn:hover {
    color: white;
    background-color: rgb(44, 41, 41);
    opacity: 0.9;
  }

  .read-heart-chat-container {
    width: 20%;
    display: flex;
    margin-left: 0vw;
  }

  .read-heart {
    font-size: 5vw;
    margin-left: 2vw;
    margin-right: 2vw;
  }

  .read-chat {
    font-size: 5vw;
  }

  .read-idx-dot {
    font-size: 2vw;
    margin: 1vw;
  }

  .read-idx-btn-on {
    width: 2vw;
    height: 2vw;
    border-radius: 70%;
    background-color: black;
  }

  .read-idx-btn-off {
    width: 2vw;
    height: 2vw;
    border-radius: 70%;
    background-color: gray;
  }

  .read-feed-like {
    font-size: 3vw;
    font-weight: bold;
  }

  .read-feed-time {
    font-size: 3vw;
    font-weight: bold;
    color: rgb(71, 71, 71);
  }

  .read-feed-content-container {
    border: none;
  }

  .read-feed-content-section-container {
    width: 100%;
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
    max-height: 53vw;
    padding: 1.2vw 4vw;
    overflow: hidden;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
  }

  .read-feed-content-section {
    width: 100%;
  }

  .read-feed-content-nickname {
    font-size: 4vw;
    font-weight: bold;
  }

  .read-feed-content-nickname:hover {
    cursor: pointer;
  }

  .read-feed-content {
    font-size: 4vw;
  }

  .read-feed-content-add {
    font-size: 4vw;
    color: gray;
    /* font-weight: bold; */
  }

  .read-feed-content-add:hover {
    cursor: pointer;
  }

  .read-feed-tag {
    font-size: 4vw;
    color: blue;
  }

  .read-feed-tag:hover {
    cursor: pointer;
  }

  .read-feed-friend-tag {
    font-size: 4vw;
    color: tomato;
  }

  .read-feed-friend-tag:hover {
    cursor: pointer;
  }

  .read-feed-comment-notice {
    font-size: 4vw;
    color: gray;
  }

  .read-feed-comment-notice:hover {
    cursor: pointer;
  }

  .read-comment-nickname {
    font-size: 4vw;
    font-weight: bold;
  }

  .read-comment-nickname:hover {
    cursor: pointer;
  }

  .read-comment-comment {
    font-size: 4vw;
  }

  .read-feed-content-container {
    padding: 1vw;
    width: 100%;
    background-color: white;
  }

  .selectModal {
    width: 50vw;
  }

}

</style>
