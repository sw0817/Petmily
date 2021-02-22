<template>
  <div>
    <div class="profile-feed-container">
      <div class="profile-feed-img-container">
        <div class="profile-heart-comment">
          <div class="profile-heart">
            <div>
              <i class="fas fa-heart"></i>
              <span>
                {{ feed.like.length }}
              </span>
            </div>
          </div>
        </div>
        <div class="profile-feed-img-div" v-if="feed.file_type[0] == 'image'">
          <div class="profile-feed-img-div-center">
            <div :class="feed.image_filters[0]">
              <img
                class="profile-feed-img"
                :src="imgs[0]"
              >
            </div>
          </div>
        </div>
        <div class="profile-feed-img-div" v-else-if="feed.file_type[0] == 'video'">
          <div class="profile-feed-img-div-center">
            <!-- <div :class="feed.image_filters"> -->
              <video
                class="profile-feed-img"
                :src="imgs[0]"
                muted
                playsinline
                controls
              ></video>
            <!-- </div> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import FeedModal from "./FeedModal"

export default {
  name: "FeedItem",
  components: {
    // FeedModal,
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
      simpleShow: false,
      simpleShow2: false,
    }
  },
  methods: {

  },
  created: function () {
    for (let img of this.feed.file_name) {
      this.imgs.push("http://i4a403.p.ssafy.io/images/" + img)
      // this.imgs.push(require("../../assets/images/FeedImg/"+img))

    }
    // console.log(`내 피드 이름은 ${this.feed.feed_id} 이고 항목은 ${this.feed}야`)
    // console.log(this.feed)
  },
}
</script>

<style>

.profile-feed-container {
  width: 100%;
  /* border: 0.1vw solid rgba(175, 175, 175, 0.5); */
  margin-bottom: 2vh;
}

.profile-feed-img-container {
  position: relative;
  width: 25vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: white;
}

.profile-feed-content-container {
  width: 25vw;
}

.profile-feed-img-div {
  position: relative;
  width: 25vw;
  height: 25vw;
  overflow: hidden;
}

.profile-heart-comment {
  position: absolute;
  width: 25vw;
  height: 25vw;
  opacity: 0;
  z-index: 10;
  transition-duration: 0.2s;
}

.profile-heart-comment:hover {
  position: absolute;
  opacity: 1;
  transition-duration: 0.2s;
  background-color: rgba(0, 0, 0, 0.5);
  cursor: pointer;
}

.profile-heart {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  width: 50%;
  height: 50%;
  -webkit-transform: translate(100%, 100%);
  -ms-transform: translate(100%, 100%);
  transform: translate(100%, 100%);
}

.profile-heart div {
  position: absolute;
  top: 0;
  left: 0;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  font-size: 2vw;
  color: white;
}

/* .profile-heart span {
  position: absolute;
  top: 0;
  left: 0;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  font-size: 2vw;
  color: white;
} */

.profile-feed-img-div-center {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  -webkit-transform: translate(50%, 50%);
  -ms-transform: translate(50%, 50%);
  transform: translate(50%, 50%);
}


.profile-feed-img {
  position: absolute;
  top: 0;
  left: 0;
  width: 25vw;
  /* height: 25vw; */
  transition-duration: 0.5s;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  opacity: 1;
}

.profile-feed-img-none {
  position: absolute;
  top: 0;
  left: 0;
  width: 25vw;
  height: 25vw;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  transition-duration: 0.5s;
  opacity: 0.1;
}

@media ( max-width: 540px) {
  .profile-feed-container {
    width: 100%;
  }

  .profile-feed-img-container {
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
