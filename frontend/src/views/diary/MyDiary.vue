<template>
  <div style="font-family: GmarketSansMedium;">
    <DiaryNav />
    <!-- 새로운 다이어리 전용 네브바 (로고 포함) 이름도 짓고 ! 펫밀리 다이어리 마법 다이어리 -->
    <div v-if="myDiaries.length" class="diary-container">
      <div class="diary-header">
        <section class="diary-header-section">
          <div class="diary-header-date2">
            <button class="diary-header-date-date2" @click="prev">
              <i class="fas fa-chevron-left"></i>
            </button>
            <div class="diary-header-date-date2">
              <div class="diary-header-date-num2">{{ currentYear }}년</div>
            </div>
            <div class="diary-header-date-date2">
              <div class="diary-header-date-num2">{{ currentMonth }}월</div>
            </div>
            <div class="diary-header-date-date2">
              <div class="diary-header-date-num2">{{ currentDay }}일</div>
            </div>
            <button class="diary-header-date-date2" @click="next">
              <i class="fas fa-chevron-right"></i>
            </button>
          </div>
        </section>
        <div class="diary-header-title">
          <div class="diary-pet-title">
            <div v-for="(pet, idx) in pets"
              :key="idx+'A'"
            >
              <div class="diary-pet-title-name">
                {{petNames[idx]}}
              </div>
            </div>
          </div>
          <div class="diary-pet-list">
            <div
              v-for="(pet, idx) in pets"
              :key="idx"
            >
              <div class="diary-pet"
              >
                <img
                  :src="petImages[idx]"
                >
              </div>
            </div>
          </div>

        </div>
        
      </div>
      <div class="diary-topic">
        <div class="feeling-container">
          <div class="left">
            <div class="question-feeling">
              기분
            </div>
            <button class="feeling-btn">
              <img
                v-if="selectedFeelingIdx != 9" :src="emotionIcon[selectedFeelingIdx]"
                class="btn-in-icon"
              >
              {{ myDiaries[diaryIdx].diary_feeling }}
            </button>
          </div>
          <div class="right">
            <div class="question-weather">
              날씨
            </div>
            <button class="feeling-btn">
              <img
                v-if="selectedWeatherIdx != 10" :src="weatherIcon[selectedWeatherIdx]"
                class="btn-in-icon"
              >
              {{ myDiaries[diaryIdx].diary_weather }}
            </button>
          </div>
        </div>
        <div class="question-topic-container">
          <div class="left">
            <div class="question-topic">
              오늘의 활동
            </div>
            <button class="feeling-btn">
              <img
                v-if="selectedTopicIdx != 7" :src="activityIcon[selectedTopicIdx]"
                class="btn-in-icon"
              >
              {{ myDiaries[diaryIdx].diary_activity }}
            </button>
          </div>
        </div>
      </div>
      <!-- 사진 추가 -->
      
      <!--  -->
      <div class="diary-content">
        <div>
          <div>
            <div class="diary-title">제목</div>
            <div class="diary-title-input">
              {{ myDiaries[diaryIdx].diary_title }}
            </div>
          </div>
        </div>
        <!-- 사진 동영상 -->
        <div class="diary-img-container">
          <div v-for="(file, idx) in files" :key="'A'+idx">
            <div class="feed-img-div" v-if="idx == imgIdx && fileTypes[idx] == 'image'">
              <div class="feed-img-div-center">
                <img
                  :class="{'feed-img': imgState, 'feed-img-none': !imgState}"
                  :src="file"
                >
              </div>
            </div>
            <div class="feed-img-div" v-else-if="idx == imgIdx && fileTypes[idx] == 'video'">
              <div class="feed-img-div-center">
                <video
                  :class="{'feed-img': imgState, 'feed-img-none': !imgState}"
                  :src="file"
                  autoplay
                  muted
                  playsinline
                  loop
                  controls
                ></video>
              </div>
            </div>
          </div>
          <button
            v-if="0 < imgIdx"
            class="diary-pre-btn"
            @click="preImg"
          >
            <i class="fas fa-angle-left"></i>
          </button>
          <button
            v-if="imgIdx < files.length-1"
            class="diary-next-btn"
            @click="nextImg"
          >
            <i class="fas fa-angle-right"></i>
          </button>
          <div class="diary-function-container">
            <div class="dot-container">
              <span
                v-for="(dot, idx) in files.length"
                :key="'C'+idx"
                class="idx-dot"
              >
                <div @click="changeImg(idx)">
                  <button v-if="idx != imgIdx" class="idx-btn-off">
                  </button>
                  <button v-else class="idx-btn-on">
                  </button>
                </div>
              </span>
            </div>
          </div>
        </div>
        <div class="my-diary-content">
          {{ myDiaries[diaryIdx].diary_content }}
        </div>
      </div>
      <div class="save-btn-of-div">
        <button class="save-btn" @click="moveToModifyDiary">
          수정하기
        </button>
        <button class="save-btn" @click="deleteDiaryNotice">
          삭제하기
        </button>
      </div>
    </div>
    <!-- 삭제 -->
    <div class="text-modal" v-if="simpleShow">
      <div class="text-modal-wrapper">
        <YesNoModal
          :contents="simpleModalContents"
          @modalOff="simpleOff"
          @deleteDiary="deleteDiary"
        />
      </div>
    </div>
  </div>
</template>

<script>

import YesNoModal from "../../components/common/YesNoModal"
import DiaryNav from "../../components/Nav/DiaryNav"
import VueJwtDecode from "vue-jwt-decode"
import axios from "axios";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;


export default {
  name: 'MyDiary',
  components: {
    YesNoModal,
    DiaryNav,
  },
  props: {
    year: {
      type: Number,
    },
    month: {
      type: Number,
    },
    day: {
      type: Number,
    },
  },
  data: function () {
    return {
      currentYear: this.$route.params.year,
      currentMonth: this.$route.params.month,
      currentDay: this.$route.params.day,
      userId: "",
      user: [],
      pets: [],
      myDiaries: [],
      myDiary: [],
      diaryIdx: 0,
      petImages: [],
      petNames: [],
      simpleModalContents: "정말로 삭제하시겠습니까 ?",
      simpleShow: false,
      // 사진 동영상
      files: [],
      fileTypes: [],
      imgIdx: 0,
      imgState: true,
      uploadImageIndex: 0,

      feelingsExam: [
        "행복",
        "신남",
        "사랑",
        "웃김",
        "귀여움",
        "아픔",
        "삐짐",
        "울상",
        "싸움",
        "선택안함",
      ],
      emotionIcon: [
        "http://i4a403.p.ssafy.io/images/smile.png",
        "http://i4a403.p.ssafy.io/images/happy.png",
        "http://i4a403.p.ssafy.io/images/love.png",
        "http://i4a403.p.ssafy.io/images/lala.png",
        "http://i4a403.p.ssafy.io/images/like.png",
        "http://i4a403.p.ssafy.io/images/sad.png",
        "http://i4a403.p.ssafy.io/images/frust.png",
        "http://i4a403.p.ssafy.io/images/upset.png",
        "http://i4a403.p.ssafy.io/images/mad.png",
        "",
        // require("../../assets/images/emotion/smile.png"),
        // require("../../assets/images/emotion/happy.png"),
        // require("../../assets/images/emotion/love.png"),
        // require("../../assets/images/emotion/lala.png"),
        // require("../../assets/images/emotion/like.png"),
        // require("../../assets/images/emotion/sad.png"),
        // require("../../assets/images/emotion/frust.png"),
        // require("../../assets/images/emotion/upset.png"),
        // require("../../assets/images/emotion/mad.png"),
        // "",
      ],
      topicExam: [
        "산책",
        "병원",
        "여행",
        "놀이",
        "목욕",
        "미용",
        "밥",
        "선택안함",
      ],
      activityIcon: [
        "http://i4a403.p.ssafy.io/images/walk.jpg",
        "http://i4a403.p.ssafy.io/images/hospital.jpg",
        "http://i4a403.p.ssafy.io/images/trip.jpg",
        "http://i4a403.p.ssafy.io/images/play.jpg",
        "http://i4a403.p.ssafy.io/images/shower.jpg",
        "http://i4a403.p.ssafy.io/images/beauty.jpg",
        "http://i4a403.p.ssafy.io/images/food.jpg",
        "",
        // require("../../assets/images/activity/walk.jpg"),
        // require("../../assets/images/activity/hospital.jpg"),
        // require("../../assets/images/activity/trip.jpg"),
        // require("../../assets/images/activity/play.jpg"),
        // require("../../assets/images/activity/shower.jpg"),
        // require("../../assets/images/activity/beauty.jpg"),
        // require("../../assets/images/activity/food.jpg"),
        // "",
      ],
      weatherExam: [
        "맑음",
        "흐림",
        "구름",
        "폭우",
        "번개",
        "폭설",
        "안개",
        "비",
        "눈",
        "보름달",
        "선택안함",
      ],
      weatherIcon: [
        "http://i4a403.p.ssafy.io/images/sunny.png",
        "http://i4a403.p.ssafy.io/images/invisible.png",
        "http://i4a403.p.ssafy.io/images/cloudy.png",
        "http://i4a403.p.ssafy.io/images/rainy.png",
        "http://i4a403.p.ssafy.io/images/thunder.png",
        "http://i4a403.p.ssafy.io/images/snowy.png",
        "http://i4a403.p.ssafy.io/images/fog.png",
        "http://i4a403.p.ssafy.io/images/rain.png",
        "http://i4a403.p.ssafy.io/images/snow.png",
        "http://i4a403.p.ssafy.io/images/moon.png",
        "",
        // require("../../assets/images/weather/sunny.png"),
        // require("../../assets/images/weather/invisible.png"),
        // require("../../assets/images/weather/cloudy.png"),
        // require("../../assets/images/weather/rainy.png"),
        // require("../../assets/images/weather/thunder.png"),
        // require("../../assets/images/weather/snowy.png"),
        // require("../../assets/images/weather/fog.png"),
        // require("../../assets/images/weather/rain.png"),
        // require("../../assets/images/weather/snow.png"),
        // require("../../assets/images/weather/moon.png"),
        // "",
      ],
      selectedFeelingIdx: 9,
      selectedTopicIdx: 7,
      selectedWeatherIdx: 10,
    }
  },
  methods: {
    getUser: function () {
      const token = localStorage.getItem('jwt')
      if (token) {
        this.userId = VueJwtDecode.decode(token).user.user_id
      }
    },
    moveToModifyDiary: function() {
      if (this.$route.path == `/diary`) {
        this.$router.go(this.$router.currentRoute)
      } else {
      this.$router.push({ name: 'ModifyDiary' })
      }
    },
    getUserPets: function(){
      axios.get(`${SERVER_URL}/pet/selectAll/${this.userId}`)
        .then(({data}) => {
          this.petNames = []
          this.petImages = []
          for (let pet of data) {
            if (this.pets.includes(pet.pet_id)) {
              this.petNames.push(pet["pet_name"])
              this.petImages.push( "http://i4a403.p.ssafy.io/images/" + pet["file_name"])
              // this.petImages.push(require("../../assets/images/petImg/"+pet["file_name"]))
            }
          }
        })
        .catch( (err) => {
          console.log(err)
        })
    },
    simpleOff: function () {
      this.simpleShow = !this.simpleShow
    },
    changeImg: function (idx) {
      setTimeout(() => {
          this.imgState = false
        }, 0);
        setTimeout(() => {
          this.imgIdx = idx
          this.imgState = true
        }, 500);
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
    getDiaryInfo: function () {
      axios.get(`${SERVER_URL}/diary/allDiary/${this.userId}`)
      .then( (res) => {
        this.myDiaries = res.data
        // console.log(this.myDiaries)
        this.getCurrentIdx()
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getCurrentPets: function () {
      axios.get(`${SERVER_URL}/diary/selectPet/${this.myDiary.diary_id}`)
      .then( (res) => {
        console.log(res)
        this.pets = []
        for (let pet of res.data) {
          this.pets.push(pet.pet_id)
        }
        this.getUserPets()
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getCurrentFiles: function () {
      axios.get(`${SERVER_URL}/diary/selectFile/${this.myDiary.diary_id}`)
      .then( (res) => {
        // console.log(res)
        // console.log("파일")
        this.files = []
        this.fileTypes = []
        for (let file of res.data) {
          this.fileTypes.push(file.file_type)
          this.files.push( "http://i4a403.p.ssafy.io/images/" +file.file_name)
          // this.files.push(require("../../assets/images/DiaryImg/"+file.file_name))
        }
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getCurrentIdx: function () {
      for (let i = 0; i < this.myDiaries.length; i++) {
        if (this.myDiaries[i].diary_year == this.currentYear && this.myDiaries[i].diary_month == this.currentMonth && this.myDiaries[i].diary_day  == this.currentDay) {
          this.diaryIdx = i
          this.myDiary = this.myDiaries[i]
          for (let j = 0; j < this.topicExam.length; j++) {
            if (this.topicExam[j] == this.myDiary.diary_activity) {
              this.selectedTopicIdx = j
              break
            }
          }
          for (let j = 0; j < this.feelingsExam.length; j++) {
            if (this.feelingsExam[j] == this.myDiary.diary_feeling) {
              this.selectedFeelingIdx = j
              break
            }
          }
          for (let j = 0; j < this.weatherExam.length; j++) {
            if (this.weatherExam[j] == this.myDiary.diary_weather) {
              this.selectedWeatherIdx = j
              break
            }
          }
          this.getCurrentPets()
          this.getCurrentFiles()
          // console.log("몇번하냐")
          // console.log(this.myDiary)
        }
      }
    },
    prev: function () {
      if (0 < this.diaryIdx) {
        this.diaryIdx -= 1
        this.currentYear = this.myDiaries[this.diaryIdx].diary_year
        this.currentMonth = this.myDiaries[this.diaryIdx].diary_month
        this.currentDay = this.myDiaries[this.diaryIdx].diary_day
        this.imgIdx = 0
        this.getCurrentIdx()
      }
    },
    next: function () {
      if (this.diaryIdx < this.myDiaries.length-1) {
        this.diaryIdx += 1
        this.currentYear = this.myDiaries[this.diaryIdx].diary_year
        this.currentMonth = this.myDiaries[this.diaryIdx].diary_month
        this.currentDay = this.myDiaries[this.diaryIdx].diary_day
        this.imgIdx = 0
        this.getCurrentIdx()
      }
    },
    deleteDiaryNotice: function () {
      this.simpleShow = !this.simpleShow
    },
    deleteDiary: function () {
      const id = this.myDiaries[this.diaryIdx].diary_id
      axios.delete(`${SERVER_URL}/diary/deleteDiary/${id}`)
      .then( (res) => {
        this.$router.push({ name: "Calendar" })
        // console.log(res)
      })
      .catch( (err) => {
        console.log(err)
      })
    },
  },
  created: function () {
    // console.log(this.$route.params.year, this.$route.params.month, this.$route.params.day)
    this.getUser()
    // this.getUserPets()
    this.getDiaryInfo()
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
.diary-container {
  display: flex;
  flex-direction: column;
  padding: 0 20vw;
  margin-bottom: 5vh;
}

.diary-header {
  display: flex;
  flex-direction: column;
  /* justify-content: space-between; */
  margin-top: 5vh;
}

.diary-header-title {
  display: flex;
  flex-direction: column;
  /* width: 60%; */
}

.diary-header-section {
  display: flex;
  justify-content: flex-end;
  /* width: 50%; */
}

.diary-header-date2 {
  display: flex;
  align-items: flex-start;
  /* text-align: center; */
}

.diary-header-date-date2 {
  display: flex;
  flex-direction: column;
  /* justify-content: center; */
  align-items: center;
  margin-left: 1vw;
  font-size: 1.5vw;
  color: black;
}

.diary-header-date-date2 svg {
  margin-top: 0.7vh;
}

.diary-header-date-num2 {
  font-size: 2vw;
  color: black;
}

.diary-pet-title {
  display: flex;
  font-size: 2vw;
  font-weight: bold;
  margin-bottom: 1vh;
}

.diary-pet-title-name {
  color: blueviolet;
  margin-right: 1vw;
}

.diary-pet-list {
  display: flex;
}

.diary-pet img {
  width: 10vw;
  height: 10vw;
  border-radius: 70%;
  margin-right: 2vw;
  transition-duration: 0.2s;
}

.diary-pet-selected img {
  width: 11vw;
  height: 11vw;
  border-radius: 70%;
  border: 0.2vw solid blue;
  margin-right: 2vw;
  transition-duration: 0.2s;
}

.diary-pet img:hover {
  cursor: pointer;
}

.diary-pet-selected img:hover {
  cursor: pointer;
}

.diary-header-calendar {
  background-color: white;
  z-index: 10;
  width: 40vw;
  right: 5vw;
  position: absolute;
}

.calendarOff {
  font-size: 2vw;
  z-index: 11;
  position: absolute;
  right: 3.5vw;
}

.calendarOff:hover {
  cursor: pointer;
}

.diary-header-calendar-btn button {
  font-size: 2vw;
}

.diary-topic {
  margin-top: 4vh;
}

.question-feeling {
  font-size: 2vw;
  font-weight: bold;
}

.feeling-btn {
  font-size: 2vw;
  font-weight: bold;
  margin-left: 2vw;
  padding: 1vw, 2vw;
  background-color: wheat;
  color: black;
  border: 0.1vw solid gray;
  border-radius: 3vw;
  width: 15vw;
}

.question-topic {
  font-size: 2vw;
  font-weight: bold;
}

.question-weather {
  font-size: 2vw;
  font-weight: bold;
}

.feeling-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.left {
  display: flex;
  align-items: center;
}

.right {
  display: flex;
  align-items: center;
}

.question-topic-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 2vh;
}

.diary-content {
  margin-top: 3vh;
  width: 100%;
}

.diary-title {
  font-size: 3vw;
}

.diary-title-input {
  width: 100%;
  font-size: 2vw;
  margin-top: 0.5vh;
  margin-bottom: 1vh;
}

.diary-title-input:focus {
  outline: none;
}

.diary-content textarea {
  width: 100%;
  font-size: 1.5vw;
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.diary-content textarea:focus {
  outline: none;
}

.text-modal {
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

.text-modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.save-btn-of-div {
  margin-top: 2vh;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.save-btn {
  font-size: 1.5vw;
  font-weight: bold;
  width: 30%;
  text-align: center;
  padding: 1vw;
  background-color: rgb(255, 166, 166);
  border-radius: 4vw;
  transition-duration: 0.5s;
}

.save-btn:hover {
  background-color: rgb(255, 122, 122);
}

/* 사진 */

.right input[type='file'] {
  position: absolute;
  width: 0;
  height: 0;
  padding: 0;
  overflow: hidden;
  border: 0;
}

.right label {
  font-size: 2vw;
  font-weight: bold;
  margin-left: 2vw;
  margin-bottom: 0;
  text-align: center;
  padding: 1vw, 2vw;
  background-color: wheat;
  color: black;
  border: 0.1vw solid gray;
  border-radius: 3vw;
  width: 15vw;
}

.right label:hover {
  cursor: pointer;
}

.diary-img-container {
  margin-top: 0.5vh;
  margin-bottom: 0.5vh;
  position: relative;
  width: 50vw;
  max-width: 550px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: white;
}

.diary-function-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.diary-pre-btn {
  position: absolute;
  color: white;
  background-color: rgb(44, 41, 41);
  padding: 1vw;
  top: 43.5%;
  left: 0;
  transition-duration: 1.5s;
  font-size: 3vw;
  font-weight: bold;
  opacity: 0.3;
}

.diary-pre-btn:hover {
  color: white;
  background-color: rgb(44, 41, 41);
  opacity: 0.9;
}

.diary-next-btn {
  position: absolute;
  color: white;
  background-color: rgb(44, 41, 41);
  padding: 1vw;
  top: 43.5%;
  right: 0;
  transition-duration: 1.5s;
  font-size: 3vw;
  font-weight: bold;
  opacity: 0.3;
}

.diary-next-btn:hover {
  color: white;
  background-color: rgb(44, 41, 41);
  opacity: 0.9;
}

.my-diary-content {
  font-size: 1.5vw;
}

@media ( max-width: 540px ) {
  
  .diary-container {
    display: flex;
    flex-direction: column;
    padding: 0 2vw;
  }

  .diary-header-calendar {
    width: 50vw;
  }

  .diary-img-container {
    width: 91.1vw;
  }
  
  .diary-pet img {
    width: 20vw;
    height: 20vw;
    border-radius: 70%;
    margin-right: 2vw;
    transition-duration: 0.2s;
  }

  .diary-pet-selected img {
    width: 22vw;
    height: 22vw;
    border-radius: 70%;
    border: 0.2vw solid blue;
    margin-right: 2vw;
    transition-duration: 0.2s;
  }

  .calendarOff {
    font-size: 3.5vw;
    z-index: 11;
    position: absolute;
    right: 3vw;
  }

  .diary-pre-btn {
    position: absolute;
    color: white;
    background-color: rgb(44, 41, 41);
    padding: 1vw;
    top: 40%;
    left: 0;
    transition-duration: 1.5s;
    font-size: 10vw;
    font-weight: bold;
    opacity: 0.3;
    }

  .diary-next-btn {
    position: absolute;
    color: white;
    background-color: rgb(44, 41, 41);
    padding: 1vw;
    top: 40%;
    right: 0;
    transition-duration: 1.5s;
    font-size: 10vw;
    font-weight: bold;
    opacity: 0.3;
  }

  .diary-header-date-num2 {
    font-size: 4vw;
    color: black;
  }

  .diary-header-date-date2 {
    font-size: 4vw;
  }

  .diary-header-date-date2 svg {
    margin-top: 0.8vw;
  }

  .my-diary-content {
    font-size: 4.5vw;
  }
  
  .save-btn {
    font-size: 5vw;
    font-weight: bold;
    width: 40%;
    text-align: center;
    padding: 1vw;
    background-color: rgb(255, 166, 166);
    border-radius: 4vw;
    transition-duration: 0.5s;
  }

}


</style>
