<template>
  <div class="read-feed-item" style="font-family: GmarketSansMedium;">
    <Navbar
      style="position:sticky; top:0; z-index:15; width: 100%;"
    >
    </Navbar>
    <div class="read-top-container">
      <div class="read-feed-header">
        <div class="read-feed-header-name">
          <span class="modify-feed-user" style="font-family: GmarketSansMedium;">{{ feed.nickname }}</span>
          <span class="read-feed-pet" v-for="(pet, idx) in tagedPets" :key="idx" style="font-family: GmarketSansMedium;">
            <span> {{ pet.pet_name }}</span>
          </span>
        </div>
        <div class="read-feed-header-dots">
          <button class="read-my-feed-dots" @click="petModal">
            <i class="fas fa-paw"></i>
          </button>
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
        </div>
        <div class="read-feed-content-section-container">
          <div class="read-feed-content-section">
            <section class="read-feed-content-content">
              <textarea
                name=""
                id=""
                v-model.trim="modifyContent"
                maxlength="500"
                placeholder="어떤 일이 있었나요 ?"
                rows="7"
                class="read-feed-content-textarea"
              >
              </textarea>
            </section>
          </div>
          <span
            class="modify-feed-tag-section"
            v-for="(tag, idx) in tags" 
            :key="'B'+idx">
            <span
              class="modify-feed-tag"
              style="font-family: GmarketSansMedium;"
            >{{tag}} </span>
          </span>
          <div>
            <span
              class="modify-feed-tag-section"
              v-for="(tag, idx) in tagedFriends"
              :key="'C'+idx">
              <span
                class="modify-feed-friend-tag"
                style="font-family: GmarketSansMedium;"
              >
                @{{tag.to_nickname}}
              </span>
              <button @click="tagFriendCancle(tag)" class="modify-feed-friend-tag-cancle">
                <i class="fas fa-times"></i>
              </button>
            </span>
          </div>
        </div>
      </div>
    </div>
    <!-- 동물 태그하기 -->
    <div v-if="petShow" class="pet-modal">
      <div class="modal-wrapper">
        <div class="tag-table-container">
          <div class="tag-table-header">
            <div class="tag-table-pre">
              <button class="tag-table-pre-btn" @click="petModal">
                <i class="fas fa-arrow-left"></i>
              </button>
            </div>
            <div class="pet-tag-title">
              내 동물 태그하기
            </div>
            <div class="tag-table-done">
              <button class="tag-table-done-btn" @click="petModal">
                완료
              </button>
            </div>
          </div>
          <div class="pet-tag-myPet-title">
            나의 펫
          </div>
          <div v-if="tagedPets.length" class="taged-pet">
            <div class="taged-pet-title">
              태그된 펫
            </div>
            <span
              v-for="(tagedPet, idx) in tagedPets"
              :key="idx"
              class="taged-pet-list"
            >
              <span class="taged-pet-name">
                {{ tagedPet.pet_name }} 
              </span>
              <span class="taged-pet-btn-span">
                <button class="taged-pet-btn" @click="tagPetCancle(tagedPet)">
                  <i class="fas fa-times"></i>
                </button>
              </span>
            </span>
          </div>
          <div class="myPet-list">
            <div
              v-for="(pet, idx) in pets"
              :key="idx"
              class="pet-tag-table"
            >
              <div class="pet-tag-img">
                <img :src="myPetImg[idx]" alt="">
              </div>
              <div class="pet-tag-petName" @click="tagPet(pet)">
                {{ pet.pet_name }}
              </div>
            </div>
          </div>
        </div>
        <div class="pet-modal-layer"></div>
      </div>
    </div>

    <!-- 친구 태그하기 -->
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
              내 친구 태그하기
            </div>
            <div class="tag-table-done">
              <button class="tag-table-done-btn" @click="friendModal">
                완료
              </button>
            </div>
          </div>
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
          <div v-if="tagedFriends.length" class="taged-friend">
            <div class="taged-friend-title">
              태그된 친구
            </div>
            <span
              v-for="(tagedFriend, idx) in tagedFriends"
              :key="idx"
              class="taged-friend-list"
            >
              <span class="taged-friend-name">
                {{ tagedFriend.to_nickname }} 
              </span>
              <span class="taged-friend-btn-span">
                <button class="taged-friend-btn" @click="tagFriendCancle(tagedFriend)">
                  <i class="fas fa-times"></i>
                </button>
              </span>
            </span>
          </div>
          <div class="myFriend-list">
            <div
              v-for="(friend, idx) in filteredFriends"
              :key="idx"
              class="friend-tag-table"
            >
              <span class="friend-tag-friendName" @click="tagFriend(friend)">
                {{ friend.to_nickname }}
              </span>
            </div>
          </div>
        </div>
        <div class="friend-modal-layer"></div>
      </div>
    </div>
    <button class="feed-modify-btn" @click="modifyFeed">수정 완료</button>
  </div>
</template>

<script>
import Navbar from "../../components/Nav/Navbar"
import VueJwtDecode from "vue-jwt-decode"
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "ModifyFeed",
  components: {
    Navbar,
  },
  props: {
    feed_id: {
      type: Number,
    }
  },
  watch: {
    modifyContent: function () {
      if (this.modifyContent[this.modifyContent.length-1] == '@') {
        document.querySelector('.read-feed-content-textarea').disabled = true
        this.friendModalOpen()
      }
      const contentTags = []
      this.tagContent = this.modifyContent.replace(/#[^/?#\s,;]+/gm, function(p1) {
        contentTags.push(p1)
        })
      this.tags = contentTags
    },
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
        "게시물로 이동",
      ],
      contents2: [
        "펫 태그"
      ],
      simpleShow: false,
      simpleShow2: false,
      contentAdd: false,
      like: [],
      preFeed: [],
      feed: {
      },
      users: [],
      modifyContent: "",
      pets: [],
      tagedPets: [],
      myPetImg: [],
      petShow: false,
      friendShow: false,
      tagedFriends: [],
      friends: [],
      filteredFriends: [],
      friendSearchInput: "",
      tagContent: '',
      tags: [],
    }
  },
  methods: {
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
        this.preFeed = res.data
        if (this.myId == this.preFeed.feed.user_id) {
          this.getSum()
        } else {
          this.$router.push({ name: "FeedMain" })
        }
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
        temp["file_name"].push(this.preFeed.files[j].file_name)
        temp["file_type"].push(this.preFeed.files[j].file_type)
        temp["image_filters"].push(this.preFeed.files[j].filter_name)
      }
      for (let j = 0; j < this.preFeed.petTag.length; j++) {
          // temp["pets"].push(this.preFeed.petTag[j].pet_name)
          for (let k = 0; k < this.pets.length; k++) {
            if (this.pets[k].pet_name == this.preFeed.petTag[j].pet_name) {
              this.tagedPets.push(this.pets[k])
            }
          }
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
        for (let k = 0; k < this.friends.length; k++) {
          if (this.friends[k].to_nickname == this.preFeed.friendTag[j].friend_nickname) {
            this.tagedFriends.push(this.friends[k])
          }
        }
          // temp["friends"].push(this.preFeed.friendTag[j].friend_nickname);
          // temp["friends_id"].push(this.preFeed.friendTag[j].friend_id);
      }
      this.feed = temp
      for (let img of this.feed.file_name) {
        this.imgs.push("http://i4a403.p.ssafy.io/images/"+img)
        // this.imgs.push(require("../../assets/images/FeedImg/"+img))
      }
      this.modifyContent = this.feed.feed_content
      document.querySelector('.read-feed-content-textarea').focus()
    },
    getMyId: function () {
      const token = localStorage.getItem('jwt')
      if (token) {
        this.myId = VueJwtDecode.decode(token).user.user_id
      }
      this.getUsersPet()
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
    getUsersPet: function() {
      axios.get(`${SERVER_URL}/pet/selectAll/${this.myId}`)
        .then(({data}) => {
          this.pets = data
          // console.log(this.pets)
          // console.log("위봐")
          this.getMyPetImg()
        })
        .catch( (err) => {
          console.log(err)
        })
    },
    getUserFollowings: function(){
      axios.post(`${SERVER_URL}/member/following/${this.myId}`)
        .then(({data}) => {
          this.friends = data
          this.filteredFriends = data
          this.getFeed()
        })
        .catch( (err) => {
          console.log(err)
        })
    },
    getMyPetImg: function () {
      for (let pet of this.pets) {
        this.myPetImg.push("http://i4a403.p.ssafy.io/images/" + pet["file_name"])
        // this.myPetImg.push(require("../../assets/images/petImg/"+pet["file_name"]))
      }
      this.getUserFollowings()
    },
    petModal: function () {
      this.petShow = !this.petShow
    },
    friendModal: function () {
      this.friendShow = !this.friendShow
      this.modifyContent = this.modifyContent.slice(0, this.modifyContent.length-1)
      document.querySelector('.read-feed-content-textarea').disabled = false
      document.querySelector('.read-feed-content-textarea').focus()
    },
    friendModalOpen: function () {
      this.friendShow = !this.friendShow
    },
    tagPet: function (pet) {
      if (!this.tagedPets.includes(pet)) {
        this.tagedPets.push(pet)
      }
    },
    tagPetCancle: function (pet) {
      this.tagedPets = this.tagedPets.filter(
        myPet => myPet != pet
      )
    },
    tagFriend: function (friend) {
      if (!this.tagedFriends.includes(friend)) {
        this.tagedFriends.push(friend)
      }
    },
    tagFriendCancle: function (friend) {
      this.tagedFriends = this.tagedFriends.filter(
        myFriend => myFriend != friend
      )
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
    readContentAdd: function () {
      this.contentAdd = !this.contentAdd
    },
    modifyFeed: function () {
      const formData = new FormData()
      const tags = this.tags.map(x => x.slice(1))

      formData.set("user_id", this.myId)
      // console.log(this.myId)
      formData.set("feed_id", this.$route.params.feed_id)
      // console.log(this.$route.params.feed_id)
      formData.set("content", this.modifyContent)
      // console.log(this.modifyContent)
      if (0 < tags.length) {
        for (let tag of tags) {
          formData.append("tag_things", tag)
          // console.log(tag)
        }
      }
      if (0 < this.tagedPets.length) {
        for (let pet of this.tagedPets) {
          formData.append("pet_name", pet.pet_name)
          formData.append("pet_id", pet.pet_id)
          // console.log(pet.pet_name)
          // console.log(pet)
        }
      }
      if (0 < this.tagedFriends.length) {
        for (let friend of this.tagedFriends) {
          formData.append("friend_id", friend.to_id)
          formData.append("friend_nickname", friend.to_nickname)
          // console.log(friend.to_id)
          // console.log(friend.to_nickname)
        }
      }
      axios.put(`${SERVER_URL}/feed/update`, formData)
      .then( (res) => {
        // console.log(res)
        this.$router.push({ name: "ReadFeed", params: {feed_id: this.$route.params.feed_id} })
      })
      .catch( (err) => {
        console.log(err)
      })
    },
  },
  computed: {
    returnedTime: function () {
      const today = new Date()
      const timeValue = new Date(this.feed.feed_date)
      const betweenTime = Math.floor((today.getTime() - timeValue.getTime()) / 1000 / 60);
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

.modify-feed-user {
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

.read-feed-content-textarea {
  width: 100%;
  height: 60%;
  min-height: 60%;
  font-size: 20px;
}

.read-feed-content-textarea:focus {
  outline: none;
}

.read-feed-content-add {
  font-size: 20px;
  color: gray;
  /* font-weight: bold; */
}

.read-feed-content-add:hover {
  cursor: pointer;
}

.modify-feed-tag {
  font-size: 20px;
  color: blue;
}

.modify-feed-friend-tag {
  font-size: 20px;
  color: tomato;
}

.modify-feed-friend-tag-cancle {
  font-size: 20px;
  color: tomato;
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

.feed-modify-btn {
  align-items: center;
  text-align: center;
  width: 900px;
  height: 57px;
  padding: 9px 16px;
  background-color: white;
  color:  mediumslateblue;
  vertical-align: middle;
  font-size: 22px;
  font-weight: bold;
  cursor: pointer;
  border-radius: 10px;
  border: 3px solid;
  transition-duration: 0.5s;
  margin-top: 2vh;
}

.feed-modify-btn:hover {
  background-color:mediumslateblue;
  color:white;
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
    font-size: 2vw;
  }

  .read-feed-header-dots:hover {
    cursor: pointer;
  }

  .read-feed-user {
    font-weight: bold;
    font-size: 2vw;
  }

  .modify-feed-user {
    font-weight: bold;
    font-size: 2vw;
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
    top: 43.5%;
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
    top: 43.5%;
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

  .modify-feed-tag {
    font-size: 1.8vw;
    color: blue;
  }

  .modify-feed-friend-tag {
    font-size: 1.8vw;
    color: tomato;
  }

  .modify-feed-friend-tag-cancle {
    font-size: 1.8vw;
    color: tomato;
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

  .feed-modify-btn {
    align-items: center;
    text-align: center;
    width: 86%;
    height: 6vw;
    padding: 0.8vw 1.5vw;
    background-color: white;
    color:  mediumslateblue;
    vertical-align: middle;
    font-size: 2vw;
    font-weight: bold;
    cursor: pointer;
    border-radius: 1vw;
    border: 0.3vw solid;
    transition-duration: 0.5s;
    margin-top: 2vh;
  }

  .feed-modify-btn:hover {
    background-color:mediumslateblue;
    color:white;
  }

  .read-feed-content-textarea {
    font-size: 1.8vw;
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

  .modify-feed-user {
    font-weight: bold;
    font-size: 5vw;
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

  .modify-feed-tag {
    font-size: 4vw;
    color: blue;
  }

  .modify-feed-friend-tag {
    font-size: 4vw;
    color: tomato;
  }
  
  .modify-feed-friend-tag-cancle {
    font-size: 4vw;
    color: tomato;
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

  .feed-modify-btn {
    align-items: center;
    text-align: center;
    width: 95%;
    height: 10vw;
    padding: 0.8vw 1.5vw;
    background-color: white;
    color:  mediumslateblue;
    vertical-align: middle;
    font-size: 4vw;
    font-weight: bold;
    cursor: pointer;
    border-radius: 1vw;
    border: 0.3vw solid;
    transition-duration: 0.5s;
    margin-top: 2vh;
    margin-bottom: 2vh;
  }

  .feed-modify-btn:hover {
    background-color:mediumslateblue;
    color:white;
  }

  .read-feed-content-textarea {
    width: 100%;
    height: 30%;
    max-height: 30%;
    font-size: 4vw;
  }

}

</style>
