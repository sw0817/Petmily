<template>
  <div class="read-feed-item">
    <Navbar
      style="position:sticky; top:0; z-index:15; width: 100%;"
    >
    </Navbar>
    <div class="read-top-container">
      <div class="read-feed-header">
        <div class="read-feed-header-name">
          <span class="read-feed-user" style="font-family: GmarketSansMedium;">{{ myNickname }}</span>
          <span class="read-feed-pet" v-for="(pet, idx) in tagedPets" :key="idx" style="font-family: GmarketSansMedium;">
            <span> {{ pet.pet_name }}</span>
          </span>
        </div>
        <div class="create-feed-header-dots">
          <div class="add-photo-btn">
            <label for="file">
              <i class="fas fa-camera-retro"></i>
            </label>
            <input type="file" id="file" ref="files" accept="image/*, video/*" @change="imageUpload" multiple>
          </div>
          <button class="read-my-feed-dots" @click="petModal">
            <i class="fas fa-paw"></i>
          </button>
          <button class="read-my-feed-dots" @click="createFeed">
            <i class="fas fa-arrow-right"></i>
          </button>
        </div>
      </div>
      <div class="read-feed-row">
      <!-- <div v-if="files.length" class="read-feed-row"> -->
        <div class="read-feed-container">
          <div class="read-feed-img-container" v-if="!files.length">
            <div class="read-feed-img-div">
              <div class="read-feed-no-img">
                <div>
                  사진을 등록해주세요.
                </div>
              </div>
            </div>
          </div>
          <div class="read-feed-img-container" v-if="files.length">
            <div v-for="(image, idx) in files" :key="'A'+idx">
              <div class="read-feed-img-div" v-if="idx == imgIdx && image.type == 'image'">
                <div class="img-close-btn2" @click="fileDeleteButton(idx)">
                  x
                </div>
                <div class="read-feed-img-div-center">
                  <div :class="imgFilters[idx]">
                    <img
                      :class="{'feed-img2': imgState, 'feed-img-none2': !imgState}"
                      :src="image.preview"
                    >
                  </div>
                </div>
              </div>

              <div class="read-feed-img-div" v-else-if="idx == imgIdx && image.type == 'video'">
                <div class="img-close-btn2" @click="fileDeleteButton(idx)">
                  x
                </div>
                <div class="read-feed-img-div-center">
                  <div :class="imgFilters[idx]">
                    <video
                      :class="{'feed-img': imgState, 'feed-img-none': !imgState}"
                      :src="image.preview"
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
              v-if="imgIdx < files.length-1"
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
                  v-for="(dot, idx) in files.length"
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
          <div class="read-feed-content-container" v-if="files.length">
            <div class="read-feed-like-section">
              <!-- 여기에 필터 -->
              <div class="create-feed-filter-section">
                <ul class="filter-wrap">
                  <li v-for="(fil, idx) in filters" :key="idx" class="filter">
                    <div class="feed-img3-div">
                      <div class="feed-img3-div-center">
                        <div :class="fil" @click="changePreviewFilter(fil)">
                          <img
                            v-if="files[imgIdx].type == 'image'"
                            :src="files[imgIdx]['preview']"
                            class="feed-img3"
                          >
                          <video
                            v-else-if="files[imgIdx].type == 'video'"
                            :src="files[imgIdx]['preview']"
                            class="feed-img3"
                          >
                          </video>
                        </div>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </div>
        </div>
        <div class="read-feed-content-section-container">
          <div class="read-feed-content-section">
            <section class="read-feed-content-content">
              <span class="read-feed-content" style="font-family: GmarketSansMedium;">
                <textarea
                  name=""
                  id=""
                  v-model.trim="content"
                  maxlength="500"
                  placeholder="어떤 일이 있었나요 ?"
                  rows="7"
                  class="read-feed-content-textarea"
                >
                </textarea>
              </span>
            </section>
          </div>
          <span
            class="read-feed-tag-section"
            v-for="(tag, idx) in tags" 
            :key="'B'+idx">
            <span
              class="read-feed-tag"
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
    <!-- 저장을 알리는 모달. axios로 저장하고 얘 띄우고, 확인 눌렀을 때 밖으로 보내주자. -->
    <div class="text-modal" v-if="simpleShow">
      <div class="text-modal-wrapper">
        <SimpleModal
          :contents="simpleModalContents"
          @modalOff="simpleOff"
        />
      </div>
    </div>

    <div class="text-modal" v-if="simpleShow2">
      <div class="text-modal-wrapper">
        <SimpleModal
          :contents="simpleModalContents2"
          @modalOff="simpleOff2"
        />
      </div>
    </div>
  </div>
</template>

<script>
import SimpleModal from "../../components/common/SimpleModal"
import Navbar from "../../components/Nav/Navbar"
import VueJwtDecode from "vue-jwt-decode"
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "CreateFeed",
  components: {
    Navbar,
    SimpleModal,
  },
  data: function () {
    return {
      content: '',
      tagContent: '',
      tags: [],
      // 이미지
      imgState: true,
      simpleShow: false,
      simpleShow2: false,
      simpleModalContents: "게시글이 등록되었습니다.",
      simpleModalContents2: "사진을 등록해주세요.",
      files: [], // 업로드용 파일
      imgFilters: [],
      previewFilters: [],
      imageTypes: [
        "image/apng",
        "image/bmp",
        "image/gif",
        "image/jpeg",
        "image/pjpeg",
        "image/png",
        "image/svg+xml",
        "image/tiff",
        "image/webp",
        "image/x-icon",
      ],
      videoTypes: [
        "video/mpeg",
        "video/mp4",
        "video/ogg",
        "video/quicktime",
        "video/webm",
        "video/mpg",
        "video/m4v",
        "video/m4p",
        "video/ogv",
        "video/mov",
      ],
      uploadImageIndex: 0,
      step: 1,
      imgIdx: 0,
      pets: [],
      myId: -1,
      petShow: false,
      friendShow: false,
      tagedPets: [],
      tagedFriends: [],
      myPetImg: [],
      friends: [],
      filteredFriends: [],
      friendSearchInput: "",
      filters: ["normal", "kelvin", "clarendon", "mayfair", "walden", "1977", "brannan", "toaster", "moon", "inkwell"],
      users: [],
      myNickname: "",
    }
  },
  watch: {
    content: function () {
      if (this.content[this.content.length-1] == '@') {
        document.querySelector('.read-feed-content-textarea').disabled = true
        this.friendModalOpen()
      }
      const contentTags = []
      this.tagContent = this.content.replace(/#[^/?#\s,;]+/gm, function(p1) {
        contentTags.push(p1)
        })
      this.tags = contentTags
    },
    friendSearchInput: function () {
      if (this.friendSearchInput.length < 1) {
        this.filteredFriends = this.friends
      } else {
        this.filteredFriends = this.friends.filter(friend => {
          return friend.to_nickname.match(this.friendSearchInput)
        })
      }
      // console.log(this.filteredFriends)
    },
  },
  methods: {
    imageUpload: function () {
      let num = -1
      for (let i = 0; i < this.$refs.files.files.length && i < 10; i++) {
        // console.log(this.$refs.files.files[i])
        if (this.imageTypes.includes(this.$refs.files.files[i].type)) {
          this.files = [
            ...this.files,
            {
              file: this.$refs.files.files[i],
              preview: URL.createObjectURL(this.$refs.files.files[i]),
              number: i,
              type: "image",
            }
          ]
        } else if (this.videoTypes.includes(this.$refs.files.files[i].type)) {
          this.files = [
            ...this.files,
            {
              file: this.$refs.files.files[i],
              preview: URL.createObjectURL(this.$refs.files.files[i]),
              number: i,
              type: "video",
            }
          ]
        }
        this.imgFilters = [
          ...this.imgFilters,
          "normal"
        ]
        this.previewFilters = [
          ...this.previewFilters,
          "normal"
        ]
        num = i
      }
      this.uploadImageIndex = num + 1
    },
    fileDeleteButton: function (idx) {
      if (idx == this.files.length-1 && idx != 0) {
        this.imgIdx -= 1
      }
      this.files.splice(idx, 1)
      this.imgFilters.splice(idx, 1)
    },
    createFeed: function () {
      if (0 == this.files.length) {
        this.simpleOff2()
      } else {
        const formData = new FormData()
        const tags = this.tags.map(x => x.slice(1))
        // console.log(this.files)
        if (0 < this.files.length) {
          for (let file of this.files) {
            // console.log(file["file"])
            formData.append("filesupload", file["file"])
            formData.append("fileTypes", file["type"])
          }
        for (let filter_name of this.imgFilters) {
          formData.append("image_filters", filter_name)
        }
        if (0 < tags.length) {
          for (let tag of tags) {
            formData.append("tag_things", tag)
          }
        } 
        if (0 < this.tagedPets.length) {
          for (let tagedPet of this.tagedPets) {
            formData.append("pet_id", tagedPet["pet_id"])
            formData.append("pet_name", tagedPet["pet_name"])
          }
        } 
        if (0 < this.tagedFriends.length) {
          for (let tagedFriend of this.tagedFriends) {
            formData.append("friend_id", tagedFriend["to_id"])
            formData.append("friend_nickname", tagedFriend["to_nickname"])
          }
        } 
        formData.append("user_id", this.myId)
        formData.append("content", this.content)
        for (let key of formData.entries()) {
          // console.log(`${key}`)
        }
        }
        axios.post(`${SERVER_URL}/feed/uploadFeed`, formData
        )
        .then( (res) => {
          // console.log(res)
          this.simpleShow = !this.simpleShow
        })
        .catch( (err) => {
          console.log(err)
        })
      }
    },
    simpleOff: function () {
      this.simpleShow = !this.simpleShow
      this.$router.push({ name: "FeedMain" })
    },
    simpleOff2: function () {
      this.simpleShow2 = !this.simpleShow2
    },
    searchTag: function (tag) {
      this.$router.push({ name: "FeedMain", params: { searchedTag: tag}})
    },
    nextStep: function () {
      if (this.step == 1) {
        if (this.files.length) {
          this.step += 1
        } else {
          alert("사진을 추가해주세요.")
        }
      } else {
        this.step += 1
      }
    },
    preStep: function () {
      this.step -= 1
    },
    exit: function () {
      this.step = 1
      this.$router.push({ name: "FeedMain" })
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
      if (this.imgIdx < this.files.length) {
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
    getUsersInfo: function () {
      axios.get(`${SERVER_URL}/member/getallmember`)
      .then( (res) => {
        this.users = res.data
        for (let user of this.users) {
          if (user.user_id == this.myId) {
            this.myNickname = user.user_nickname
            break
          }
        }
        this.getUserFollowings()
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getMyId: function () {
      const token = localStorage.getItem('jwt')
      if (token) {
        this.myId = VueJwtDecode.decode(token).user.user_id
      }
      this.getUsersInfo()
    },
    getUsersPet: function() {
      axios.get(`${SERVER_URL}/pet/selectAll/${this.myId}`)
        .then(({data}) => {
          this.pets = data
          this.getMyPetImg()
        })
        .catch( (err) => {
          console.log(err)
        })
    },
    getMyPetImg: function () {
      // console.log("pets")
      for (let pet of this.pets) {
        this.myPetImg.push("http://i4a403.p.ssafy.io/images/"+pet["file_name"])
        // this.myPetImg.push(require("../../assets/images/petImg/"+pet["file_name"]))
      }
    },
    getUserFollowings: function(){
      axios.post(`${SERVER_URL}/member/following/${this.myId}`)
        .then(({data}) => {
          this.friends = data
          this.filteredFriends = data
          this.getUsersPet()
        })
        .catch( (err) => {
          console.log(err)
        })
    },
    petModal: function () {
      this.petShow = !this.petShow
    },
    friendModal: function () {
      this.friendShow = !this.friendShow
      this.content = this.content.slice(0, this.content.length-1)
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
    changePreviewFilter: function (fil) {
      this.previewFilters.splice(this.imgIdx, 1, fil)
      this.changeFilter()
    },
    changeFilter: function () {
      this.imgFilters.splice(this.imgIdx, 1, this.previewFilters[this.imgIdx])
    },
  },
  created: function () {
    this.getMyId()
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
/* 기존 */

.main-container2 {
  width: 80%;
  margin: 0 auto;
}
        
.img-upload-info-container2 {
  margin-top: 4vw;
  color: #222222;
}

.cancle-or-pre {
  width: 15%;
  font-size: 3vw;
}

.done-or-next {
  width: 15%;
  font-size: 3vw;
}

.done-btn {
  width: 100%;
  font-size: 2vw;
}

.step-header {
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.add-photo-btn-div {
  width: 25%;
  margin-left: 5%;
}

.tag-friends {
  width: 25%;
  margin-left: 5%;
  margin-right: 5%;
}

.tag-pets {
  width: 25%;
  margin-left: 5%;
  margin-right: 5%;
}

.tag-friends-list {
  font-size: 2vw;
  font-weight: bold;
  margin-right: 5%;
}

.tag-friends-btn {
  align-items: center;
  text-align: center;
  width: 100%;
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
  margin-bottom: 1.3vw;
  margin-top: 1.3vw;
}

.tag-friends-btn:hover {
  background-color:mediumslateblue;
  color:white;
}

.tag-pets-btn {
  align-items: center;
  text-align: center;
  width: 100%;
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
  margin-bottom: 1.3vw;
  margin-top: 1.3vw;
}

.tag-pets-btn:hover {
  background-color:mediumslateblue;
  color:white;
}

.upload-title {
  display: flex;
  text-align: center;
  justify-content: space-between;
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.img-upload-example {
  height: 100%;
}

.feed-img-top-container {
  display: flex;
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
  align-items: center;
}

.feed-img-container2 {
  position: relative;
  padding: 2vw;
  width: 34vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: white;
  border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.feed-img2-div {
  position: relative;
  width: 30vw;
  height: 30vw;
  overflow: hidden;
}

.filter-application {
  position: absolute;
  z-index: 20;
  font-size: 1.5vw;
  font-weight: bold;
  padding: 1vw;
  border-radius: 2vw;
  top: 1vw;
  right: 5vw;
  background-color: rgba(151, 151, 151, 1);
  box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
  transition-duration: 0.2s;
}

.filter-application:hover {
  background-color: rgb(71, 71, 71);
  color: white;
}

.filter-application:active {
  background-color: rgba(151, 151, 151, 1);
  color: white;
}

.feed-img2-div-center {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  -webkit-transform: translate(50%, 50%);
  -ms-transform: translate(50%, 50%);
  transform: translate(50%, 50%);
}

.feed-img2 {
  position: absolute;
  width: 50vw;
  max-width: 550px;
  height: auto;
  top: 0;
  left: 0;
  transition-duration: 0.5s;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  opacity: 1;
}

.feed-img-none2 {
  position: absolute;
  width: 50vw;
  max-width: 550px;
  height: auto;
  top: 0;
  left: 0;
  transition-duration: 0.5s;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  opacity: 0.1;
}

.pre-btn2 {
  position: absolute;
  color: white;
  background-color: rgb(44, 41, 41);
  padding: 1vw;
  top: 15vw;
  left: 2vw;
  transition-duration: 1.5s;
  font-size: 1.2vw;
  font-weight: bold;
  opacity: 0.3;
}

.pre-btn2:hover {
  color: white;
  background-color: rgb(44, 41, 41);
  opacity: 0.9;
}

.next-btn2 {
  position: absolute;
  color: white;
  background-color: rgb(44, 41, 41);
  padding: 1vw;
  top: 15vw;
  right: 2vw;
  transition-duration: 1.5s;
  font-size: 1.2vw;
  font-weight: bold;
  opacity: 0.3;
}

.next-btn2:hover {
  color: white;
  background-color: rgb(44, 41, 41);
  opacity: 0.9;
}

.feed-function-container2 {
  width: 100%;
  display: flex;
}

.dot-container2 {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.idx-dot2 {
  font-size: 0.5vw;
  margin: 0.25vw;
}

.idx-btn2-on {
  width: 0.5vw;
  height: 0.5vw;
  border-radius: 70%;
  background-color: black;
}

.idx-btn2-on:hover {
  cursor: pointer;
}

.idx-btn2-off {
  width: 0.5vw;
  height: 0.5vw;
  border-radius: 70%;
  background-color: gray;
}

.idx-btn2-off:hover {
  cursor: pointer;
}

.pet-modal {
  position: fixed;
  z-index: 15;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity .3s ease;
}

.friend-modal {
  position: fixed;
  z-index: 15;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity .3s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.tag-table-container {
  display: flex;
  flex-direction: column;
  width: 50vw;
  height: 40vw;
  margin: 0 auto;
  /* padding: 2vw 3vw; */
  background-color: #fff;
  border-radius: 0.5vw;
  box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
  transition: all .3s ease;
}

.pet-tag-title {
  width: 60%;
  font-size: 2.5vw;
  font-weight: bold;
  text-align: center;
}

.friend-tag-title {
  width: 60%;
  font-size: 2.5vw;
  font-weight: bold;
  text-align: center;
}

.tag-table-header {
  display: flex;
  align-items: center;
  margin-top: 2vw;
  margin-left: 3vw;
  margin-right: 3vw;
}

.tag-table-pre {
  width: 20%;
}

.tag-table-done {
  width: 20%;
  text-align: end;
}

.tag-table-done-btn {
  font-size: 1.5vw;
  font-weight: bold;
  color: mediumslateblue;
}

.pet-tag-myPet-title {
  font-size: 1.8vw;
  font-weight: bold;
  margin-top: 1vw;
  color: gray;
  padding: 1vw 3vw;
  align-items: center;
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.friend-tag-myFriend-title {
  display: flex;
  font-size: 1.8vw;
  font-weight: bold;
  margin-top: 1vw;
  color: gray;
  padding: 1vw 3vw;
  align-items: center;
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.myPet-list {
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 50%;
  overflow: hidden;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.pet-tag-table {
  display: flex;
  align-items: center;
  margin-left: 3vw;
  margin-right: 3vw;
  margin-top: 3vw;
}

.pet-tag-img {
  width: 5vw;
  height: 5vw;
}

.pet-tag-img img {
  width: 5vw;
  height: 5vw;
  border-radius: 70%;
}

.pet-tag-petName {
  margin-left: 5%;
  width: 40%;
  font-size: 1.5vw;
  font-weight: bold;
  color: mediumslateblue;
}

.pet-tag-petName:hover {
  cursor: pointer;
}

.taged-pet {
  margin-left: 3vw;
  margin-right: 3vw;
}

.taged-pet-title {
  margin-top: 1vw;
  margin-bottom: 0.5vw;
  font-size: 1.5vw;
  color: grey;
}

.taged-pet-list {
  margin-right: 1vw;
  font-size: 1.5vw;
}

.taged-pet-name {
  color: mediumslateblue;
}

.firend-tag-myFriend-title {
  font-size: 1.8vw;
  font-weight: bold;
  margin-top: 1vw;
  color: gray;
}

.myFriend-list {
  margin-top: 1vw;
  display: flex;
  flex-direction: column;
  padding: 0 3vw;
  overflow: hidden;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.friend-tag-search-logo {
  font-size: 2vw;
}

.friend-tag-search-input {
  font-size: 2vw;
  margin-left: 1vw;
}

.friend-tag-search-input:focus {
  outline: none;
}

.friend-tag-table {
  display: flex;
  align-items: center;
  /* margin: auto 0; */
}

.friend-tag-img {
  width: 5vw;
  height: 5vw;
}

.friend-tag-img img {
  width: 5vw;
  height: 5vw;
  border-radius: 70%;
}

.friend-tag-friendName {
  margin-left: 5%;
  margin-top: 1vw;
  width: 40%;
  font-size: 1.5vw;
  font-weight: bold;
  /* color: mediumslateblue; */
}

.friend-tag-friendName:hover {
  cursor: pointer;
}

.taged-friend {
  margin-left: 3vw;
  margin-right: 3vw;
}

.taged-friend-title {
  margin-top: 1vw;
  margin-bottom: 0.5vw;
  font-size: 1.5vw;
  color: grey;
}

.taged-friend-list {
  margin-right: 1vw;
  font-size: 1.5vw;
}

.taged-friend-name {
  color: mediumslateblue;
}

.write-container2 {
  width: 100%;
  min-height: 30vw;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
}

.write-form {
  width: 100%;
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.hash-tag {
  font-size: 2vw;
  color: blue;
  background-color: rgb(202, 202, 202);
  margin-top: 1vw;
  margin-right: 1vw;
  padding: 0 0.5vw;
  border-radius: 1vw;
}

.filter-container {
  overflow-x: scroll;
}

.filter-wrap {
  display: flex;
  overflow: hidden;
  margin-top: 0.5vh;
  /* width: 60%; */
}

.filter {
  display: inline-block;
  height: 52px;
  width: 52px;
  border: 0.1 solid rgba(175, 175, 175, 0.5);
}

.filter:hover {
  cursor: pointer;
}

.filter img {
  /* height: 3vw; */
  width: 3vw;
}

.feed-img3-div {
  position: relative;
  height: 52px;
  width: 52px;
  overflow: hidden;
}

.feed-img3-div-center {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  -webkit-transform: translate(50%, 50%);
  -ms-transform: translate(50%, 50%);
  transform: translate(50%, 50%);
}

.feed-img3-div-center div img {
  position: absolute;
  width: 52px;
  height: auto;
  top: 0;
  left: 0;
  transition-duration: 0.5s;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  opacity: 1;
}

/* .feed-img3 {
} */

.img-cropper-container {
  position: relative;
  padding-top: 2vw;
  width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.add-photo-btn {
  align-items: center;
}

.add-photo-btn input[type='file'] {
  position: absolute;
  width: 0;
  height: 0;
  padding: 0;
  overflow: hidden;
  border: 0;
}

.add-photo-btn label {
  align-items: center;
  text-align: center;
  color:  mediumslateblue;
  vertical-align: middle;
  font-size: 30px;
  font-weight: bold;
  cursor: pointer;
  margin-bottom: 0;
  margin-right: 20px;
}

.read-my-feed-dots {
  font-size: 30px;
  font-weight: bold;
  cursor: pointer;
  margin-bottom: 0;
  margin-right: 20px;
}

.img-close-btn2 {
  position: absolute;
  line-height: 1.4vw;
  z-index: 2;
  font-size: 1.5vw;
  right: 1vw;
  top: 1vw;
  color: #fff;
  font-weight: bold;
  background-color: #666666;
  width: 1.7vw;
  height: 1.7vw;
  text-align: center;
  cursor: pointer;
}

/* 기존 */

.read-feed-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 3vh;
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
  min-width: 900px;
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

.create-feed-header-dots {
  display: flex;
  justify-content: flex-end;
  position: relative;
  font-size: 30px;
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

.read-feed-no-img {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 22px;
  font-weight: bold;
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

.create-feed-filter-section {
  width: 100%;
  display: flex;
  justify-content: center;
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

  /* 기존 */

  .main-container2 {
    width: 80%;
    margin: 0 auto;
  }
          
  .img-upload-info-container2 {
    margin-top: 4vw;
    color: #222222;
  }

  .cancle-or-pre {
    width: 15%;
    font-size: 3vw;
  }

  .done-or-next {
    width: 15%;
    font-size: 3vw;
  }

  .done-btn {
    width: 100%;
    font-size: 2vw;
  }

  .step-header {
    border: 0.1vw solid rgba(175, 175, 175, 0.5);
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .add-photo-btn-div {
    width: 25%;
    margin-left: 5%;
  }

  .tag-friends {
    width: 25%;
    margin-left: 5%;
    margin-right: 5%;
  }

  .tag-pets {
    width: 25%;
    margin-left: 5%;
    margin-right: 5%;
  }

  .tag-friends-list {
    font-size: 2vw;
    font-weight: bold;
    margin-right: 5%;
  }

  .tag-friends-btn {
    align-items: center;
    text-align: center;
    width: 100%;
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
    margin-bottom: 1.3vw;
    margin-top: 1.3vw;
  }

  .tag-friends-btn:hover {
    background-color:mediumslateblue;
    color:white;
  }

  .tag-pets-btn {
    align-items: center;
    text-align: center;
    width: 100%;
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
    margin-bottom: 1.3vw;
    margin-top: 1.3vw;
  }

  .tag-pets-btn:hover {
    background-color:mediumslateblue;
    color:white;
  }

  .upload-title {
    display: flex;
    text-align: center;
    justify-content: space-between;
    border: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .img-upload-example {
    height: 100%;
  }

  .feed-img-top-container {
    display: flex;
    border: 0.1vw solid rgba(175, 175, 175, 0.5);
    align-items: center;
  }

  .feed-img-container2 {
    position: relative;
    padding: 2vw;
    width: 34vw;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    background-color: white;
    border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .feed-img2-div {
    position: relative;
    width: 30vw;
    height: 30vw;
    overflow: hidden;
  }
    
  .read-feed-no-img {
    font-size: 2vw;
  }

  .filter-application {
    position: absolute;
    z-index: 20;
    font-size: 1.5vw;
    font-weight: bold;
    padding: 1vw;
    border-radius: 2vw;
    top: 1vw;
    right: 5vw;
    background-color: rgba(151, 151, 151, 1);
    box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
    transition-duration: 0.2s;
  }

  .filter-application:hover {
    background-color: rgb(71, 71, 71);
    color: white;
  }

  .filter-application:active {
    background-color: rgba(151, 151, 151, 1);
    color: white;
  }

  .feed-img2-div-center {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    -webkit-transform: translate(50%, 50%);
    -ms-transform: translate(50%, 50%);
    transform: translate(50%, 50%);
  }

  .feed-img2 {
    position: absolute;
    width: 50vw;
    max-width: 550px;
    height: auto;
    top: 0;
    left: 0;
    transition-duration: 0.5s;
    -webkit-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    opacity: 1;
  }

  .feed-img-none2 {
    position: absolute;
    width: 50vw;
    max-width: 550px;
    height: auto;
    top: 0;
    left: 0;
    transition-duration: 0.5s;
    -webkit-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    opacity: 0.1;
  }

  .pre-btn2 {
    position: absolute;
    color: white;
    background-color: rgb(44, 41, 41);
    padding: 1vw;
    top: 15vw;
    left: 2vw;
    transition-duration: 1.5s;
    font-size: 1.2vw;
    font-weight: bold;
    opacity: 0.3;
  }

  .pre-btn2:hover {
    color: white;
    background-color: rgb(44, 41, 41);
    opacity: 0.9;
  }

  .next-btn2 {
    position: absolute;
    color: white;
    background-color: rgb(44, 41, 41);
    padding: 1vw;
    top: 15vw;
    right: 2vw;
    transition-duration: 1.5s;
    font-size: 1.2vw;
    font-weight: bold;
    opacity: 0.3;
  }

  .next-btn2:hover {
    color: white;
    background-color: rgb(44, 41, 41);
    opacity: 0.9;
  }

  .feed-function-container2 {
    width: 100%;
    display: flex;
  }

  .dot-container2 {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .idx-dot2 {
    font-size: 0.5vw;
    margin: 0.25vw;
  }

  .idx-btn2-on {
    width: 0.5vw;
    height: 0.5vw;
    border-radius: 70%;
    background-color: black;
  }

  .idx-btn2-on:hover {
    cursor: pointer;
  }

  .idx-btn2-off {
    width: 0.5vw;
    height: 0.5vw;
    border-radius: 70%;
    background-color: gray;
  }

  .idx-btn2-off:hover {
    cursor: pointer;
  }

  .pet-modal {
    position: fixed;
    z-index: 15;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: table;
    transition: opacity .3s ease;
  }

  .friend-modal {
    position: fixed;
    z-index: 15;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    display: table;
    transition: opacity .3s ease;
  }

  .modal-wrapper {
    display: table-cell;
    vertical-align: middle;
  }

  .tag-table-container {
    display: flex;
    flex-direction: column;
    width: 50vw;
    height: 40vw;
    margin: 0 auto;
    /* padding: 2vw 3vw; */
    background-color: #fff;
    border-radius: 0.5vw;
    box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
    transition: all .3s ease;
  }

  .pet-tag-title {
    width: 60%;
    font-size: 2.5vw;
    font-weight: bold;
    text-align: center;
  }

  .friend-tag-title {
    width: 60%;
    font-size: 2.5vw;
    font-weight: bold;
    text-align: center;
  }

  .tag-table-header {
    display: flex;
    align-items: center;
    margin-top: 2vw;
    margin-left: 3vw;
    margin-right: 3vw;
  }

  .tag-table-pre {
    width: 20%;
  }

  .tag-table-done {
    width: 20%;
    text-align: end;
  }

  .tag-table-done-btn {
    font-size: 1.5vw;
    font-weight: bold;
    color: mediumslateblue;
  }

  .pet-tag-myPet-title {
    font-size: 1.8vw;
    font-weight: bold;
    margin-top: 1vw;
    color: gray;
    padding: 1vw 3vw;
    align-items: center;
    border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .friend-tag-myFriend-title {
    display: flex;
    font-size: 1.8vw;
    font-weight: bold;
    margin-top: 1vw;
    color: gray;
    padding: 1vw 3vw;
    align-items: center;
    border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .myPet-list {
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 50%;
    overflow: hidden;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
  }

  .pet-tag-table {
    display: flex;
    align-items: center;
    margin-left: 3vw;
    margin-right: 3vw;
    margin-top: 3vw;
  }

  .pet-tag-img {
    width: 5vw;
    height: 5vw;
  }

  .pet-tag-img img {
    width: 5vw;
    height: 5vw;
    border-radius: 70%;
  }

  .pet-tag-petName {
    margin-left: 5%;
    width: 40%;
    font-size: 1.5vw;
    font-weight: bold;
    color: mediumslateblue;
  }

  .pet-tag-petName:hover {
    cursor: pointer;
  }

  .taged-pet {
    margin-left: 3vw;
    margin-right: 3vw;
  }

  .taged-pet-title {
    margin-top: 1vw;
    margin-bottom: 0.5vw;
    font-size: 1.5vw;
    color: grey;
  }

  .taged-pet-list {
    margin-right: 1vw;
    font-size: 1.5vw;
  }

  .taged-pet-name {
    color: mediumslateblue;
  }

  .firend-tag-myFriend-title {
    font-size: 1.8vw;
    font-weight: bold;
    margin-top: 1vw;
    color: gray;
  }

  .myFriend-list {
    margin-top: 1vw;
    display: flex;
    flex-direction: column;
    padding: 0 3vw;
    overflow: hidden;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
  }

  .friend-tag-search-logo {
    font-size: 2vw;
  }

  .friend-tag-search-input {
    font-size: 2vw;
    margin-left: 1vw;
  }

  .friend-tag-search-input:focus {
    outline: none;
  }

  .friend-tag-table {
    display: flex;
    align-items: center;
    /* margin: auto 0; */
  }

  .friend-tag-img {
    width: 5vw;
    height: 5vw;
  }

  .friend-tag-img img {
    width: 5vw;
    height: 5vw;
    border-radius: 70%;
  }

  .friend-tag-friendName {
    margin-left: 5%;
    margin-top: 1vw;
    width: 40%;
    font-size: 1.5vw;
    font-weight: bold;
    /* color: mediumslateblue; */
  }

  .friend-tag-friendName:hover {
    cursor: pointer;
  }

  .taged-friend {
    margin-left: 3vw;
    margin-right: 3vw;
  }

  .taged-friend-title {
    margin-top: 1vw;
    margin-bottom: 0.5vw;
    font-size: 1.5vw;
    color: grey;
  }

  .taged-friend-list {
    margin-right: 1vw;
    font-size: 1.5vw;
  }

  .taged-friend-name {
    color: mediumslateblue;
  }

  .write-container2 {
    width: 100%;
    min-height: 30vw;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: flex-start;
  }

  .write-form {
    width: 100%;
    border: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .hash-tag {
    font-size: 2vw;
    color: blue;
    background-color: rgb(202, 202, 202);
    margin-top: 1vw;
    margin-right: 1vw;
    padding: 0 0.5vw;
    border-radius: 1vw;
  }

  .filter-container {
    overflow-x: scroll;
  }

  .filter-wrap {
    display: flex;
    overflow: hidden;
    margin-top: 0.5vh;
    /* width: 60%; */
  }

  .filter {
    display: inline-block;
    height: 4vw;
    width: 4vw;
    border: 0.1 solid rgba(175, 175, 175, 0.5);
  }

  .filter:hover {
    cursor: pointer;
  }

  .filter img {
    /* height: 3vw; */
    width: 4vw;
  }

  .feed-img3-div {
    position: relative;
    width: 4vw;
    height: 4vw;
    overflow: hidden;
  }

  .feed-img3-div-center {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    -webkit-transform: translate(50%, 50%);
    -ms-transform: translate(50%, 50%);
    transform: translate(50%, 50%);
  }

  .feed-img3-div-center div img {
    position: absolute;
    width: 4vw;
    height: auto;
    top: 0;
    left: 0;
    transition-duration: 0.5s;
    -webkit-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    opacity: 1;
  }

  .img-cropper-container {
    position: relative;
    padding-top: 2vw;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .add-photo-btn label {
    align-items: center;
    text-align: center;
    color:  mediumslateblue;
    vertical-align: middle;
    font-size: 3vw;
    font-weight: bold;
    cursor: pointer;
    margin-bottom: 0;
  }

  .img-close-btn2 {
    position: absolute;
    line-height: 1.4vw;
    z-index: 2;
    font-size: 1.5vw;
    right: 1vw;
    top: 1vw;
    color: #fff;
    font-weight: bold;
    background-color: #666666;
    width: 1.7vw;
    height: 1.7vw;
    text-align: center;
    cursor: pointer;
  }

  /* 기존 */

  .read-my-feed-dots {
    font-size: 3vw;
    margin-right: 2vw;
  }

  .read-top-container {
  min-height: 0;
  background-color: white;
  }

  .read-feed-container {
    border: 0.1vw solid rgba(175, 175, 175, 0.5);
    width: 45vw;
  }

  .read-feed-header {
    width: 82vw;
    min-width: 0;
    padding: 1vw;
    background-color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .read-feed-header-name {
    display: flex;
  }

  .create-feed-header-dots {
    position: relative;
    font-size: 1.5vw;
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

  .create-feed-header-dots {
    position: relative;
    font-size: 5vw;
  }

  .read-feed-user {
    font-weight: bold;
    font-size: 4vw;
  }

  .read-feed-user:hover {
    cursor: pointer;
  }

  .read-feed-pet {
    font-weight: bold;
    font-size: 4vw;
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

  .read-feed-img-div-center div img {
    width: 91.1vw;
    height: auto
  }

  .read-feed-img-div-center div img {
    width: 91.1vw;
    height: auto
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

  /* 추가 */

  .add-photo-btn label {
    font-size: 6vw;
    font-weight: bold;
    cursor: pointer;
    margin-bottom: 0;
    margin-right: 2vw;
  }

  .read-my-feed-dots {
    font-size: 6vw;
    margin-right: 2vw;
  }
  
  .img-close-btn2 {
    position: absolute;
    line-height: 4.5vw;
    z-index: 2;
    font-size: 10vw;
    right: 4vw;
    top: 3vw;
    color: #fff;
    font-weight: bold;
    background-color: #666666;
    width: 8vw;
    height: 8vw;
    text-align: center;
    cursor: pointer;
  }

  .filter {
    display: inline-block;
    height: 8.5vw;
    width: 8.5vw;
    border: 0.1 solid rgba(175, 175, 175, 0.5);
  }

  .feed-img3-div {
    position: relative;
    height: 8.5vw;
    width: 8.5vw;
    overflow: hidden;
  }

  .feed-img3-div-center {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    -webkit-transform: translate(50%, 50%);
    -ms-transform: translate(50%, 50%);
    transform: translate(50%, 50%);
  }

  .feed-img3-div-center div img {
    position: absolute;
    width: 8.5vw;
    height: auto;
    top: 0;
    left: 0;
    transition-duration: 0.5s;
    -webkit-transform: translate(-50%, -50%);
    -ms-transform: translate(-50%, -50%);
    transform: translate(-50%, -50%);
    opacity: 1;
  }

  /* 태그 모달 */

  .tag-table-container {
    width: 80vw;
    height: 40vw;
    margin: 0 auto;
    background-color: #fff;
    border-radius: 0.5vw;
    box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
    transition: all .3s ease;
  }

  .pet-tag-title {
    width: 60%;
    font-size: 2.5vw;
    font-weight: bold;
    text-align: center;
  }

  .friend-tag-title {
    width: 60%;
    font-size: 2.5vw;
    font-weight: bold;
    text-align: center;
  }

  .tag-table-header {
    display: flex;
    align-items: center;
    /* width: 100%; */
    margin-top: 2vw;
    margin-left: 3vw;
    margin-right: 3vw;
  }

  .tag-table-pre {
    width: 20%;
  }

  .tag-table-done {
    width: 20%;
    text-align: end;
  }

  .tag-table-done-btn {
    font-size: 1.5vw;
    font-weight: bold;
    color: mediumslateblue;
  }

  .pet-tag-myPet-title {
    font-size: 1.8vw;
    font-weight: bold;
    margin-top: 1vw;
    color: gray;
    padding: 1vw 3vw;
    align-items: center;
    border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .friend-tag-myFriend-title {
    display: flex;
    font-size: 1.8vw;
    font-weight: bold;
    margin-top: 1vw;
    color: gray;
    padding: 1vw 3vw;
    align-items: center;
    border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .myPet-list {
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 50%;
    overflow: hidden;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
  }

  .pet-tag-table {
    display: flex;
    align-items: center;
    margin-left: 3vw;
    margin-right: 3vw;
    margin-top: 3vw;
  }

  .pet-tag-img {
    width: 5vw;
    height: 5vw;
  }

  .pet-tag-img img {
    width: 5vw;
    height: 5vw;
    border-radius: 70%;
  }

  .pet-tag-petName {
    margin-left: 5%;
    width: 40%;
    font-size: 1.5vw;
    font-weight: bold;
    color: mediumslateblue;
  }

  .pet-tag-petName:hover {
    cursor: pointer;
  }

  .taged-pet {
    margin-left: 3vw;
    margin-right: 3vw;
  }

  .taged-pet-title {
    margin-top: 1vw;
    margin-bottom: 0.5vw;
    font-size: 1.5vw;
    color: grey;
  }

  .taged-pet-list {
    margin-right: 1vw;
    font-size: 1.5vw;
  }

  .taged-pet-name {
    color: mediumslateblue;
  }

  .firend-tag-myFriend-title {
    font-size: 1.8vw;
    font-weight: bold;
    margin-top: 1vw;
    color: gray;
  }

  .myFriend-list {
    margin-top: 1vw;
    display: flex;
    flex-direction: column;
    padding: 0 3vw;
    overflow: hidden;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
  }

  .friend-tag-search-logo {
    font-size: 2vw;
  }

  .friend-tag-search-input {
    font-size: 2vw;
    margin-left: 1vw;
  }

  .friend-tag-search-input:focus {
    outline: none;
  }

  .friend-tag-table {
    display: flex;
    align-items: center;
    /* margin: auto 0; */
  }

  .friend-tag-img {
    width: 5vw;
    height: 5vw;
  }

  .friend-tag-img img {
    width: 5vw;
    height: 5vw;
    border-radius: 70%;
  }

  .friend-tag-friendName {
    margin-left: 5%;
    margin-top: 1vw;
    width: 40%;
    font-size: 1.5vw;
    font-weight: bold;
    /* color: mediumslateblue; */
  }

  .friend-tag-friendName:hover {
    cursor: pointer;
  }

  .taged-friend {
    margin-left: 3vw;
    margin-right: 3vw;
  }

  .taged-friend-title {
    margin-top: 1vw;
    margin-bottom: 0.5vw;
    font-size: 1.5vw;
    color: grey;
  }

  .taged-friend-list {
    margin-right: 1vw;
    font-size: 1.5vw;
  }

  .taged-friend-name {
    color: mediumslateblue;
  }

  .read-feed-no-img {
    font-size: 5vw;
  }

}

</style>
