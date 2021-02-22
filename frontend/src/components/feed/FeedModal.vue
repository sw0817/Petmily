<template>
  <div class="modal-top-container">
    <div class="modal-feed-container">
      <div class="modal-feed-header">
        <div class="modal-feed-header-dots">
          <button v-if="myId == feed.user_id" class="my-feed-dots" @click="simpleOn2">
            <i class="fas fa-ellipsis-h"></i>
          </button>
          <button v-else class="others-feed-dots" @click="simpleOn">
            <i class="fas fa-ellipsis-h"></i>
          </button>
          <div class="selectModal" v-if="simpleShow">
            <SelectModal
              :contents="contents"
              @modalOff="simpleOff"
              @report="reportFeed"
              @read="readFeed"
            />
          </div>
          <div class="selectModal" v-if="simpleShow2">
            <SelectModal
              :contents="contents2"
              @modalOff="simpleOff2"
              @delete="deleteFeed"
              @read="readFeed"
              @update="updateFeed"
            />
          </div>
        </div>
      </div>
      <div class="feed-img-container">
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
            <div class="heart" @click="printFeed">
              <i v-if="isLiking" class="fas fa-heart"></i>
              <i v-else class="far fa-heart"></i>
            </div>
            <div class="chat">
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
          <div class="feed-like" style="font-family: GmarketSansMedium;">
            <!-- 좋아요 목록으로 받아서 length로 하는게 맞다. -->
            좋아요 {{ feed.like.length }}개
          </div>
          <div class="feed-time" style="font-family: GmarketSansMedium;">
            <!-- 작성날짜 or 몇시간전 -->
            <!-- 몇시간전 구현할 예정 -->
            {{ feed.feed_date }}
          </div>
        </div>
        <div class="feed-content-section">
          <section class="feed-content-content">
            <span class="feed-content-nickname" style="font-family: GmarketSansMedium;">
              <!-- {{feed.nickname}} -->
            </span>
            <span class="feed-content" style="font-family: GmarketSansMedium;">
              {{feed.feed_content}}
            </span>
          </section>
        </div>
        <span
          class="feed-tag-section"
          v-for="(tag, idx) in feed.tags" 
          :key="'B'+idx">
          <span
            class="feed-tag"
            @click="searchTag(tag)"
          >#{{tag}} </span>
        </span>
        <div class="feed-comment-section">
          <div v-if="0 < comments.length" class="feed-comment">
            <div>
              <span class="feed-comment-notice" style="font-family: GmarketSansMedium;">
                댓글 {{ comments.length }}개 모두 보기
              </span>
            </div>
            <div>
              <span class="comment-nickname" style="font-family: GmarketSansMedium;">
                {{ comments[0].user_id}} 
              </span>
              <span class="comment-comment" style="font-family: GmarketSansMedium;">
                {{ comments[0].content }}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SelectModal from "../common/SelectModal"

export default {
  name: "FeedModal",
  components: {
    SelectModal,
  },
  props: {
    feed: {
      type: Object,
    },
    myId: {
      type: Number,
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
      comments: [],
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
    }
  },
  methods: {
    searchTag: function (tag) {
      this.$emit('searchingTag', tag)
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
    // printFeed: function () {
    //   console.log(this.feed)
    //   console.log(this.myId)
    // },
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
      this.$emit("reportFeed", this.feed.feed_id)
    },
    readFeed: function () {
      this.$emit("readFeed", this.feed.feed_id)
    },
    deleteFeed: function () {
      this.$emit("deleteFeed", this.feed.feed_id)
    },
    updateFeed: function () {
      this.$emit("updateFeed", this.feed.feed_id)
    },
  },
  created: function () {
    for (let img of this.feed.file_name) {
      this.imgs.push("http://i4a403.p.ssafy.io/images/" + img)
      // this.imgs.push(require("../../assets/images/FeedImg/"+img))
    }
    this.getComments()
    // console.log(this.$route.params.feed)
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
.modal-top-container {
  min-height: 0;
}

.modal-feed-container {
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
  margin-bottom: 2vh;
}

.modal-feed-header {
  padding: 1vw;
  background-color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-feed-header-name {
  display: flex;
}

.modal-feed-header-dots {
  position: relative;
  font-size: 1.5vw;
}

.modal-feed-header-dots:hover {
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

.feed-img-container {
  position: relative;
  width: 50vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: white;
}

.feed-content-container {
  width: 50vw;
}

.feed-img-div {
  position: relative;
  width: 50vw;
  height: 50vw;
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
  top: 43.5%;
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
  top: 43.5%;
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

.feed-tag {
  font-size: 1.5vw;
  color: blue;
}

.feed-tag:hover {
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
}

.selectModal {
  position: absolute;
  top: 0;
  right: 0;
  z-index: 10;
  width: 20vw;
  transition-duration: 0.8s;
}

@media ( max-width: 540px) {
  .modal-feed-container {
    width: 100%;
  }

  .modal-top-container {
    width: 100%;
  }

  .feed-img-container {
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
    font-size: 5vw;
    margin-left: 2vw;
    margin-right: 2vw;
  }

  .chat {
    font-size: 5vw;
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

  .selectModal {
    width: 50vw;
  }

}

</style>
