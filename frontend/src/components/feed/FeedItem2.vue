<template>
  <div class="top-container">
    <div class="feed-item2-container">
      <div class="feed-header">
        <div class="feed-header-name">
          <span class="feed-user" @click="moveToUserProfile" style="font-family: GmarketSansMedium;">{{ feed.nickname }}</span>
          <span class="feed-pet" v-for="(pet, idx) in feed.pets" :key="idx" style="font-family: GmarketSansMedium;">
            <span> {{ pet }}</span>
          </span>
        </div>
        <div class="feed-header-dots">
          <button v-if="myId == feed.user_id" class="my-feed-dots" @click="simpleOn2">
            <i class="fas fa-ellipsis-h"></i>
          </button>
          <button v-else class="others-feed-dots" @click="simpleOn">
            <i class="fas fa-ellipsis-h"></i>
          </button>
          <div class="feedSelectModal" v-if="simpleShow">
            <SelectModal
              :contents="contents"
              @modalOff="simpleOff"
              @report="reportFeed"
              @read="readFeed"
            />
          </div>
          <div class="feedSelectModal" v-if="simpleShow2">
            <SelectModal
              :contents="contents2"
              @modalOff="simpleOff2"
              @delete="yesNoOn"
              @read="readFeed"
              @update="updateFeed"
            />
          </div>
        </div>
      </div>
      <div class="feed-item2-img-container">
        <div v-for="(image, idx) in imgs" :key="'A'+idx">
          <div class="feed-img-div" v-if="idx == imgIdx && feed.file_type[idx] == 'image'">
            <div class="feed-img-div-center">
              <div :class="feed.image_filters[idx]">
                <img
                  :class="{'feed-img': imgState, 'feed-img-none': !imgState}"
                  :src="image"
                >
              </div>
            </div>
          </div>
          <div class="feed-img-div" v-else-if="idx == imgIdx && feed.file_type[idx] == 'video'">
            <div class="feed-img-div-center">
              <!-- <div :class="feed.image_filters"> -->
                <video
                  :class="{'feed-img': imgState, 'feed-img-none': !imgState}"
                  :src="image"
                  autoplay
                  muted
                  playsinline
                  loop
                  controls
                ></video>
              <!-- </div> -->
            </div>
          </div>
        </div>
        <button
          v-if="0 < imgIdx"
          class="pre-btn"
          @click="preImg"
        >
          <i class="fas fa-angle-left"></i>
        </button>
        <button
          v-if="imgIdx < imgs.length-1"
          class="next-btn"
          @click="nextImg"
        >
          <i class="fas fa-angle-right"></i>
        </button>
        <div class="feed-function-container">
          <section class="heart-chat-container">
            <div class="heart">
              <button class="empty-heart" v-if="!isLiking" @click="giveHeart">
                <i class="fas fa-heart"></i>
              </button>
              <button class="full-heart" v-else @click="deleteHeart">
                <i class="fas fa-heart"></i>
              </button>
            </div>
            <div class="chat" @click="focusChat">
              <i class="far fa-comment"></i>
            </div>
          </section>
          <div class="dot-container">
            <span
              v-for="(dot, idx) in imgs.length"
              :key="'C'+idx"
              class="idx-dot"
            >
              <div @click="changeImgIdx(idx)">
                <button v-if="idx != imgIdx" class="idx-btn-off">
                </button>
                <button v-else class="idx-btn-on">
                </button>
              </div>
            </span>
          </div>
        </div>
      </div>
      <div class="feed-content-container">
        <div class="feed-like-section">
          <div class="feed-like" style="font-family: GmarketSansMedium;" @click="ReadLike">
            <!-- 좋아요 목록으로 받아서 length로 하는게 맞다. -->
            좋아요 {{ like.length }}개
          </div>
          <div class="feed-time" style="font-family: GmarketSansMedium;">
            <!-- 작성날짜 or 몇시간전 -->
            <!-- 몇시간전 구현할 예정 -->
            {{ returnedTime }}
          </div>
        </div>
        <div class="feed-content-section">
          <section class="feed-content-content">
            <span class="feed-content-nickname" style="font-family: GmarketSansMedium;">
              {{feed.nickname}}
            </span>
            <span v-if="feed.feed_content.length < 30" class="feed-content" style="font-family: GmarketSansMedium;">
              {{feed.feed_content}}
            </span>
            <span v-else-if="feed.feed_content.length >= 30 && contentAdd" class="feed-content" style="font-family: GmarketSansMedium;">
              <span>
                {{feed.feed_content}}
              </span>
              <span @click="readContentAdd" class="read-feed-content-add">
                줄이기
              </span>
            </span>
            <span v-else-if="feed.feed_content.length >= 30" class="feed-content" style="font-family: GmarketSansMedium;">
              <span>
                {{feed.feed_content.slice(0, 30)}} ...
              </span>
              <span @click="readContentAdd" class="feed-content-add">
                더 보기
              </span>
            </span>
          </section>
        </div>
        <span
          class="feed-tag-section"
          v-for="(tag, idx) in feed.tags" 
          :key="'B'+idx">
          <span
            class="feed-tag"
            style="font-family: GmarketSansMedium;"
            @click="searchTag(tag)"
          >#{{tag}} </span>
        </span>
        <div><span
          class="feed-tag-section"
          v-for="(tag, idx) in feed.friends"
          :key="'C'+idx">
          <span
            class="feed-friend-tag"
            style="font-family: GmarketSansMedium;"
            @click="searchFriendTag(feed.friends_id[idx])"
          >@{{tag}} </span>
        </span></div>

        <memo-write
          ref="memoInput"
          v-bind:feed_id="article.feed_id"
          :reloadMemos="reloadMemos"
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
              <!-- <memo-write
                v-bind:feed_id="article.feed_id"
                :reloadMemos="reloadMemos"
              /> -->
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
    <div class="text-modal" v-if="yesNoShow">
      <div class="text-modal-wrapper">
        <YesNoModal
          :contents="yesNoModalContents"
          @modalOff="yesNoOn"
          @deleteDiary="deleteFeed"
        />
      </div>
    </div>
    <div class="text-modal" v-if="yesNoShow2">
      <div class="text-modal-wrapper">
        <YesNoModal
          :contents="yesNoModalContents2"
          @modalOff="yesNoOn2"
          @deleteDiary="reportFeed"
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
import FollowModal from "../user/FollowModal"
import SelectModal from "../common/SelectModal"
import MemoWrite from "./MemoWrite";
import MemoRow from "./MemoRow";
import { listMemo } from "@/api/memo.js";
import axios from 'axios'
import moment from 'moment/moment'
const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "FeedItem2",
  components: {
    SelectModal,
    YesNoModal,
    FollowModal,
    MemoWrite,
    MemoRow,
  },
  props: {
    feed: {
      type: Object,
    },
    myId: {
      type: Number,
    },
    followings: {
      type: Array,
    },
  },
  data: function () {
    return {
      imgs: [],
      imgIdx: 0,
      pre: "<",
      next: ">",
      imgState: true,
      isLiking: false,
      contents: [
        "신고",
        "게시물로 이동",
      ],
      contents2: [
        "삭제",
        "수정",
        "게시물로 이동",
      ],
      simpleShow: false,
      simpleShow2: false,
      yesNoShow: false,
      yesNoModalContents: "정말 삭제하시겠습니까 ?",
      yesNoShow2:false,
      yesNoModalContents2:"이 게시글을 신고하시겠습니까?",
      contentAdd: false,
      like: [],
      timeValue: this.feed.feed_date,
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
    }
  },
  methods: {
    unfollow: function (member) {
      this.$emit("unfollow", member)
    },
    follow: function (member) {
      this.$emit("follow", member)
    },
    ReadLike: function () {
      this.likeShow = !this.likeShow
    },
    focusChat: function () {
      this.$refs.memoInput.$refs.memosinput.focus()
      // console.log(this.like)
    },
    showEvent: function(){
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
        return memo.feed_id === this.article.feed_id;
      });
      // 이 부분 풀면 제대로 동작함. But, 이 컴포넌트만 리로딩됨
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
    searchTag: function (tag) {
      this.$emit('searchingTag', tag)
    },
    searchFriendTag: function (tag) {
      this.$emit('searchingFriendTag', tag)
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
    simpleOn2: function () {
      this.simpleShow2 = !this.simpleShow2
    },
    simpleOff2: function () {
      this.simpleShow2 = !this.simpleShow2
    },
    reportFeed: function () {
      this.yesNoShow2=false
      this.$emit("reportFeed", this.feed.feed_id)
    },
    readFeed: function () {
      this.$emit("readFeed", this.feed.feed_id)
    },
    deleteFeed: function () {
      this.simpleShow = !this.simpleShow
      this.$emit("deleteFeed", this.feed.feed_id)
    },
    yesNoOn: function () {
      this.simpleShow2=false
      this.yesNoShow = !this.yesNoShow
    },
    yesNoOff: function () {
      this.yesNoShow = !this.yesNoShow
      this.deleteFeed()
    },
    yesNoOn2:function(){
      this.simpleShow=false
      this.yesNoShow2=!this.yesNoShow2
    },
    updateFeed: function () {
      this.$emit("updateFeed", this.feed.feed_id)
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
    printIsLiking: function () {
      // console.log(this.isLiking)
      // console.log(this.like)
      // console.log(this.myId)
    },
  },
  created: function () {
    for (let img of this.feed.file_name) {
      this.imgs.push("http://i4a403.p.ssafy.io/images/" + img)
      // this.imgs.push(require("../../assets/images/FeedImg/"+img))
    }
    this.checkLiking()

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
  }
}
</script>

<style>
@font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
.top-container {
  min-height: 0;
  background-color: white;
  max-width: 550px;
}

.feed-item2-container {
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
  margin-bottom: 2vh;
  width: 100%;
}

.feed-header {
  min-width: 549.9px;
  width: 100%;
  padding: 1vw;
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-right: 0.1px solid rgba(175, 175, 175, 0.5);
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.feed-header-name {
  display: flex;
}

.feed-header-dots {
  position: relative;
  font-size: 22px;
}

.feed-header-dots:hover {
  cursor: pointer;
}

.feed-user {
  font-weight: bold;
  font-size: 22px;
}

.feed-user:hover {
  cursor: pointer;
}

.feed-pet {
  font-weight: bold;
  font-size: 22px;
  margin-left: 0.5vw;
  color: mediumslateblue;
}

.feed-item2-img-container {
  position: relative;
  border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
  width: 50vw;
  max-width: 550px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: white;
}

.feed-img-div {
  position: relative;
  width: 49.9vw;
  max-width: 549px;
  height: 49.9vw;
  max-height: 549px;
  overflow: hidden;
}

.feed-img-div-center {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  -webkit-transform: translate(50%, 50%);
  -ms-transform: translate(50%, 50%);
  transform: translate(50%, 50%);
}

.feed-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 49.9vw;
  max-width: 549px;
  /* height: 50vw; */
  transition-duration: 0.5s;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  opacity: 1;
}

.feed-img-none {
  position: absolute;
  top: 0;
  left: 0;
  width: 49.9vw;
  max-width: 549px;
  /* height: 50vw; */
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  transition-duration: 0.5s;
  opacity: 0.1;
}

.pre-btn {
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

.pre-btn:hover {
  color: white;
  background-color: rgb(44, 41, 41);
  opacity: 0.9;
}

.next-btn {
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

.next-btn:hover {
  color: white;
  background-color: rgb(44, 41, 41);
  opacity: 0.9;
}

.feed-function-container {
  width: 100%;
  display: flex;
}

.heart-chat-container {
  width: 20%;
  display: flex;
}

.heart {
  font-size: 24px;
  margin-left: 10px;
  margin-right: 10px;
}

.empty-heart {
  color: black;
  transition-duration: 0.5s;
}

.full-heart {
  color: red;
  transition-duration: 0.5s;
}

.chat {
  font-size: 24px;
}

.heart:hover {
  cursor: pointer;
}

.chat:hover {
  cursor: pointer;
}

.dot-container {
  width: 60%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.idx-dot {
  font-size: 15px;
  margin: 7px;
}

.idx-btn-on {
  width: 12px;
  height: 12px;
  border-radius: 70%;
  background-color: black;
}

.idx-btn-on:hover {
  cursor: pointer;
}

.idx-btn-off {
  width: 12px;
  height: 12px;
  border-radius: 70%;
  background-color: gray;
}

.idx-btn-off:hover {
  cursor: pointer;
}

.feed-like-section {
  display: flex;
  justify-content: space-between;
}

.feed-like {
  font-size: 22px;
  font-weight: bold;
}

.feed-like:hover {
  cursor: pointer;
}

.feed-time {
  font-size: 20px;
  font-weight: bold;
  color: rgb(71, 71, 71);
}

.feed-content-nickname {
  font-size: 20px;
  font-weight: bold;
}

.feed-content-nickname:hover {
  cursor: pointer;
}

.feed-content {
  font-size: 20px;
}

.feed-content-add {
  font-size: 20px;
  color: gray;
  /* font-weight: bold; */
}

.feed-content-add:hover {
  cursor: pointer;
}

.feed-tag {
  font-size: 20px;
  color: blue;
}

.feed-tag:hover {
  cursor: pointer;
}

.feed-friend-tag {
  font-size: 20px;
  color: tomato;
}

.feed-friend-tag:hover {
  cursor: pointer;
}

.feed-comment-notice {
  font-size: 20px;
  color: gray;
}

.feed-comment-notice:hover {
  cursor: pointer;
}

.comment-nickname {
  font-size: 20px;
  font-weight: bold;
}

.comment-nickname:hover {
  cursor: pointer;
}

.comment-comment {
  font-size: 20px;
}

.feed-content-container {
  padding: 1vw;
  background-color: white;
  border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
  width: 50vw;
  max-width: 550px;
}

.feedSelectModal {
  position: absolute;
  top: 0;
  right: 0;
  z-index: 10;
  width: 20vw;
  transition-duration: 0.8s;
}

@media ( max-width: 1100px) {

  .top-container {
    min-height: 0;
    background-color: white;
    max-width: 550px;
  }

  .feed-item2-container {
    border: 0.1vw solid rgba(175, 175, 175, 0.5);
    margin-bottom: 2vh;
    width: 100%;
  }

  .feed-header {
    min-width: 0px;
    border-right: none;
    padding: 1vw;
    background-color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .feed-header-name {
    display: flex;
  }

  .feed-header-dots {
    position: relative;
    font-size: 1.5vw;
  }

  .feed-header-dots:hover {
    cursor: pointer;
  }

  .feed-user {
    font-weight: bold;
    font-size: 2vw;
  }

  .feed-user:hover {
    cursor: pointer;
  }

  .feed-pet {
    font-weight: bold;
    font-size: 2vw;
    margin-left: 0.5vw;
    color: mediumslateblue;
  }

  .feed-item2-img-container {
    position: relative;
    width: 50vw;
    max-width: 550px;
    border-right: none;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: white;
  }

  .feed-img-div {
    position: relative;
    width: 50vw;
    max-width: 550px;
    height: 50vw;
    max-height: 550px;
    overflow: hidden;
  }

  .feed-img-div-center {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    -webkit-transform: translate(50%, 50%);
    -ms-transform: translate(50%, 50%);
    transform: translate(50%, 50%);
  }

  .feed-img {
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

  .feed-img-none {
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

  .pre-btn {
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

  .pre-btn:hover {
    color: white;
    background-color: rgb(44, 41, 41);
    opacity: 0.9;
  }

  .next-btn {
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

  .next-btn:hover {
    color: white;
    background-color: rgb(44, 41, 41);
    opacity: 0.9;
  }

  .feed-function-container {
    width: 100%;
    display: flex;
  }

  .heart-chat-container {
    width: 20%;
    display: flex;
  }

  .heart {
    font-size: 2.5vw;
    margin-left: 1vw;
    margin-right: 1vw;
  }

  .empty-heart {
    color: black;
    transition-duration: 0.5s;
  }

  .full-heart {
    color: red;
    transition-duration: 0.5s;
  }

  .chat {
    font-size: 2.5vw;
  }

  .heart:hover {
    cursor: pointer;
  }

  .chat:hover {
    cursor: pointer;
  }

  .dot-container {
    width: 60%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .idx-dot {
    font-size: 1vw;
    margin: 0.5vw;
  }

  .idx-btn-on {
    width: 1vw;
    height: 1vw;
    border-radius: 70%;
    background-color: black;
  }

  .idx-btn-on:hover {
    cursor: pointer;
  }

  .idx-btn-off {
    width: 1vw;
    height: 1vw;
    border-radius: 70%;
    background-color: gray;
  }

  .idx-btn-off:hover {
    cursor: pointer;
  }

  .feed-like-section {
    display: flex;
    justify-content: space-between;
  }

  .feed-like {
    font-size: 1.8vw;
    font-weight: bold;
  }

  .feed-time {
    font-size: 1.5vw;
    font-weight: bold;
    color: rgb(71, 71, 71);
  }

  .feed-content-nickname {
    font-size: 1.5vw;
    font-weight: bold;
  }

  .feed-content-nickname:hover {
    cursor: pointer;
  }

  .feed-content {
    font-size: 1.5vw;
  }

  .feed-content-add {
    font-size: 1.5vw;
    color: gray;
    /* font-weight: bold; */
  }

  .feed-content-add:hover {
    cursor: pointer;
  }

  .feed-tag {
    font-size: 1.5vw;
    color: blue;
  }

  .feed-tag:hover {
    cursor: pointer;
  }

  .feed-friend-tag {
    font-size: 1.5vw;
    color: tomato;
  }

  .feed-friend-tag:hover {
    cursor: pointer;
  }

  .feed-comment-notice {
    font-size: 1.5vw;
    color: gray;
  }

  .feed-comment-notice:hover {
    cursor: pointer;
  }

  .comment-nickname {
    font-size: 1.5vw;
    font-weight: bold;
  }

  .comment-nickname:hover {
    cursor: pointer;
  }

  .comment-comment {
    font-size: 1.5vw;
  }

  .feed-content-container {
    padding: 1vw;
    background-color: white;
    border-right: none;
    width: 50vw;
    max-width: 550px;
  }

  .feedSelectModal {
    position: absolute;
    top: 0;
    right: 0;
    z-index: 10;
    width: 20vw;
    transition-duration: 0.8s;
  }

}

@media ( max-width: 540px) {
  .feed-item2-container {
    width: 100%;
  }

  .top-container {
    width: 100%;
  }

  .feed-item2-img-container {
    width: 100%;
  }

  .feed-content-container {
    width: 100%;
  }

  .feed-img-div {
    width: 91.1vw;
    height: 91.1vw;
  }

  .feed-img {
    width: 91.1vw;
  }

  .feed-img-none {
    width: 91.1vw;
  }

  .pre-btn {
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

  .pre-btn:hover {
    color: white;
    background-color: rgb(44, 41, 41);
    opacity: 0.9;
  }

  .next-btn {
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

  .next-btn:hover {
    color: white;
    background-color: rgb(44, 41, 41);
    opacity: 0.9;
  }

  .heart-chat-container {
    width: 20%;
    display: flex;
    margin-left: 0vw;
  }

  .heart {
    font-size: 6vw;
    margin-left: 2vw;
    margin-right: 2vw;
  }

  .chat {
    font-size: 6vw;
  }

  .idx-dot {
    font-size: 2vw;
    margin: 1vw;
  }

  .idx-btn-on {
    width: 2vw;
    height: 2vw;
    border-radius: 70%;
    background-color: black;
  }

  .idx-btn-off {
    width: 2vw;
    height: 2vw;
    border-radius: 70%;
    background-color: gray;
  }

  .feedSelectModal {
    width: 50vw;
  }

  .feed-like {
    font-size: 4vw;
    font-weight: bold;
  }

  .feed-time {
    font-size: 4vw;
    font-weight: bold;
    color: rgb(71, 71, 71);
  }

  .feed-content-nickname {
    font-size: 3.5vw;
    font-weight: bold;
  }

  .feed-content-nickname:hover {
    cursor: pointer;
  }

  .feed-content {
    font-size: 3.5vw;
  }

  .feed-content-add {
    font-size: 3.5vw;
    color: gray;
  }

  .feed-content-add:hover {
    cursor: pointer;
  }

  .feed-tag {
    font-size: 3.5vw;
    color: blue;
  }

  .feed-friend-tag {
    font-size: 3.5vw;
    color: tomato;
  }

  .feed-comment-notice {
    font-size: 3.5vw;
    color: gray;
  }

  .comment-nickname {
    font-size: 3.5vw;
    font-weight: bold;
  }

  .comment-comment {
    font-size: 3.5vw;
  }

  .feed-header-dots {
    position: relative;
    font-size: 4.5vw;
  }

  .feed-user {
    font-weight: bold;
    font-size: 4vw;
  }

  .feed-user:hover {
    cursor: pointer;
  }

  .feed-pet {
    font-weight: bold;
    font-size: 4vw;
    margin-left: 1vw;
    color: mediumslateblue;
  }

}

</style>