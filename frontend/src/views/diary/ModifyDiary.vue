<template>
  <div style="font-family: GmarketSansMedium;">
    <DiaryNav />
    <div class="text-modal" v-if="textSelModalShow">
      <div class="text-modal-wrapper">
        <TextSelModal 
          :title="feelingsTitle"
          :contents="feelingsExam"
          :icons="emotionIcon"
          @modalOff="modalOff"
          @SelectFeeling="changeFeeling"
        />
      </div>
    </div>
    <div class="text-modal" v-if="textSelModalShow2">
      <div class="text-modal-wrapper">
        <TextSelModal 
          :title="topicTitle"
          :contents="topicExam"
          :icons="activityIcon"
          @modalOff="modalOff2"
          @SelectFeeling="changeTopic"
        />
      </div>
    </div>
    <div class="text-modal" v-if="textSelModalShow3">
      <div class="text-modal-wrapper">
        <TextSelModal 
          :title="weatherTitle"
          :contents="weatherExam"
          :icons="weatherIcon"
          @modalOff="modalOff3"
          @SelectFeeling="changeWeather"
        />
      </div>
    </div>
    <!-- 새로운 다이어리 전용 네브바 (로고 포함) 이름도 짓고 ! 펫밀리 다이어리 마법 다이어리 -->
    <div class="diary-container">
      <div class="diary-header">
        <section class="diary-header-section">
          <div class="diary-header-date">
            
            <div class="diary-header-date-date">
              <div class="diary-header-date-num">{{ currentYear }}년</div>
            </div>
            <div class="diary-header-date-date">
              <div class="diary-header-date-num">{{ currentMonth }}월</div>
            </div>
            <div class="diary-header-date-date">
              <div class="diary-header-date-num">{{ currentDay }}일</div>
            </div>
          </div>

        </section>
        <div class="diary-header-title">
          <div class="diary-pet-title">
            누구의 이야기인가요 ?
          </div>
          <div class="diary-pet-list">
            <div
              v-for="(pet, idx) in pets"
              :key="idx"
            >
              <div :class="{'diary-pet': !selectedPets.includes(pet.pet_id), 'diary-pet-selected': selectedPets.includes(pet.pet_id)}"
                @click="selectPet(pet.pet_id)"
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
            <button class="feeling-btn" @click="selectFeeling">
              <img
                v-if="selectedFeelingIdx != 9" :src="emotionIcon[selectedFeelingIdx]"
                class="btn-in-icon"
              >
              {{ selectedFeeling }}
            </button>
          </div>
          <div class="right">
            <div class="question-weather">
              날씨
            </div>
            <button class="feeling-btn" @click="selectWeather">
              <img
                v-if="selectedWeatherIdx != 10" :src="weatherIcon[selectedWeatherIdx]"
                class="btn-in-icon"
              >
              {{ selectedWeather }}
            </button>
          </div>
        </div>
        <div class="question-topic-container">
          <div class="left">
            <div class="question-topic">
              오늘의 활동
            </div>
            <button class="feeling-btn" @click="selectTopic">
              <img
                v-if="selectedTopicIdx != 7" :src="activityIcon[selectedTopicIdx]"
                class="btn-in-icon"
              >
              {{ selectedTopic }}
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
            <input
              class="diary-title-input"
              v-model="diaryTitle"
              type="text"
              maxlength="20"
              id="diaryTitle"
            >
          </div>
        </div>
        <!-- 사진 동영상 -->
        <div>
          <ImagePreview
            :files="files"
            :imgIdx="imgIdx"
            @deleteImg="deleteImg"
            @changeImg="changeImg"
            @nextImg="nextImg"
            @preImg="preImg"
          />
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
        <!--  -->
        <textarea
          rows="10"
          v-model="diaryContent"
          id="diaryContent"
          maxlength="500"
        >
        </textarea>
      </div>
      <div class="modify-save-btn-div">
        <button class="modify-save-btn" @click="modifyDiary">
          저장
        </button>
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

import TextSelModal from "../../components/diary/TextSelModal"
import SimpleModal from "../../components/common/SimpleModal"
import ImagePreview from "../../components/common/ImagePreview"
import DiaryNav from "../../components/Nav/DiaryNav"
import VueJwtDecode from "vue-jwt-decode"
import axios from "axios";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;


export default {
  name: 'ModifyDiary',
  components: {
    ImagePreview,
    SimpleModal,
    TextSelModal,
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
      calendarShow: false,
      textSelModalShow: false,
      textSelModalShow2: false,
      textSelModalShow3: false,
      user: [],
      pets: [],
      diaryTitle: "",
      diaryContent: "",
      petImages: [],
      feelings: [],
      diaryIdx: 0,
      imgState: true,
      selectedPets: [],
      selectedFeelings: [],
      selectedFeeling: "",
      feelingsTitle: "기분",
      selectedTopic: "",
      topicTitle: "오늘의 활동",
      selectedWeather: "",
      weatherTitle: "날씨",
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
      ],
      selectedFeelingIdx: 9,
      selectedTopicIdx: 7,
      selectedWeatherIdx: 10,

      simpleModalContents: "저장되었습니다.",
      simpleModalContents2: "펫을 선택해주세요.",
      simpleShow: false,
      simpleShow2: false,
      // 사진 동영상
      files: [],
      imgIdx: 0,
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
    }
  },
  methods: {
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
    getCurrentFiles: function () {
      axios.get(`${SERVER_URL}/diary/selectFile/${this.myDiary.diary_id}`)
      .then( (res) => {
        // console.log(res)
        // console.log("파일")
        this.files = []
        this.fileTypes = []
        for (let file of res.data) {
          this.fileTypes.push(file.file_type)
          this.files.push("http://i4a403.p.ssafy.io/images/" + file.file_name)
          // this.files.push(require("../../assets/images/DiaryImg/"+file.file_name))
        }
        this.modDia()
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
          this.getUserPets()
          this.getCurrentFiles()
        }
      }
    },
    modDia: function() {
      this.selectedFeeling = this.myDiaries[this.diaryIdx].diary_feeling
      this.selectedWeather = this.myDiaries[this.diaryIdx].diary_weather
      this.selectedTopic = this.myDiaries[this.diaryIdx].diary_activity
      this.diaryTitle = this.myDiaries[this.diaryIdx].diary_title
      this.diaryContent = this.myDiaries[this.diaryIdx].diary_content
      for (let j = 0; j < this.topicExam.length; j++) {
        if (this.topicExam[j] == this.selectedTopic) {
          this.selectedTopicIdx = j
          break
        }
      }
      for (let j = 0; j < this.feelingsExam.length; j++) {
        if (this.feelingsExam[j] == this.selectedFeeling) {
          this.selectedFeelingIdx = j
          break
        }
      }
      for (let j = 0; j < this.weatherExam.length; j++) {
        if (this.weatherExam[j] == this.selectedWeather) {
          this.selectedWeatherIdx = j
          break
        }
      }
    },
    getUser: function () {
      const token = localStorage.getItem('jwt')
      if (token) {
        this.userId = VueJwtDecode.decode(token).user.user_id
      }
    },
    getUserPets: function(){
      axios.get(`${SERVER_URL}/pet/selectAll/${this.userId}`)
        .then(({data}) => {
          this.pets = data
          // console.log(this.pets)
          for (let pet of this.pets) {
            this.petImages.push("http://i4a403.p.ssafy.io/images/"+pet["file_name"])
            // this.petImages.push(require("../../assets/images/petImg/"+pet["file_name"]))
          }
          this.getCurrentPets()
        })
        .catch( (err) => {
          console.log(err)
        })
    },
    getCurrentPets: function () {
      axios.get(`${SERVER_URL}/diary/selectPet/${this.myDiary.diary_id}`)
      .then( (res) => {
        // console.log(res)
        for (let pet of res.data) {
          this.selectedPets.push(pet.pet_id)
        }
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    selectPet: function (petId) {
      let idx = this.selectedPets.indexOf(petId)
      // console.log(idx)
      if (idx == -1) {
        this.selectedPets.push(petId)
      } else {
        this.selectedPets.splice(idx, 1)
      }
    },
    showCalendar: function () {
      this.calendarShow = !this.calendarShow
    },
    changeDate: function (date) {
      // console.log(date)
      this.currentYear = date.year
      this.currentMonth = date.month
      this.currentDay = date.day
      this.calendarShow = false
    },
    selectFeeling: function () {
      this.textSelModalShow = true
    },
    modalOff: function () {
      this.textSelModalShow = false
    },
    changeFeeling: function (topic, idx) {
      this.selectedFeeling = topic
      this.selectedFeelingIdx = idx
      this.textSelModalShow = false
    },
    selectTopic: function () {
      this.textSelModalShow2 = true
    },
    modalOff2: function () {
      this.textSelModalShow2 = false
    },
    changeTopic: function (topic, idx) {
      this.selectedTopic = topic
      this.selectedTopicIdx = idx
      this.textSelModalShow2 = false
    },
    selectWeather: function () {
      this.textSelModalShow3 = true
    },
    modalOff3: function () {
      this.textSelModalShow3 = false
    },
    changeWeather: function (topic, idx) {
      this.selectedWeather = topic
      this.selectedWeatherIdx = idx
      this.textSelModalShow3 = false
    },
    simpleOff: function () {
      this.simpleShow = !this.simpleShow
      this.$router.push({ name: "Calendar" })
    },
    simpleOff2: function () {
      this.simpleShow2 = !this.simpleShow2
    },
    simpleOn: function () {
      this.simpleShow = !this.simpleShow
    },
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
        num = i
      }
      this.uploadImageIndex = num + 1
      // console.log(this.files)
    },
    deleteImg: function (idx) {
      if (idx == this.files.length-1 && idx != 0) {
        this.imgIdx -= 1
      }
      this.files.splice(idx, 1)
    },
    changeImg: function (idx) {
      this.imgIdx = idx
    },
    nextImg: function () {
      this.imgIdx += 1
    },
    preImg: function () {
      this.imgIdx -= 1
    },
    modifyDiary: function () {
      const formData = new FormData()
      if (0 < this.files.length) {
        for (let file of this.files) {
          formData.append("filesupload", file["file"])
          formData.append("fileTypes", file["type"])
        }
      }
      for (let petId of this.selectedPets) {
        formData.append("pet_id", petId)
      }
      // formData.append("pet_name", "asd")
      formData.set("diary_id", this.myDiaries[this.diaryIdx].diary_id)
      formData.set("user_id", this.userId)
      formData.set("diary_content", this.diaryContent)
      formData.set("diary_title", this.diaryTitle)
      formData.set("diary_year", this.currentYear)
      formData.set("diary_month", this.currentMonth)
      formData.set("diary_day", this.currentDay)
      formData.set("diary_feeling", this.selectedFeeling)
      formData.set("diary_weather", this.selectedWeather)
      formData.set("diary_activity", this.selectedTopic)
      // for (let key of formData.entries()) {
      //   console.log(`${key}`)
      // }
      let confirm = 1
      if (this.selectedPets.length == 0) {
        this.simpleModalContents2 = "펫을 선택해주세요."
        this.simpleShow2 = !this.simpleShow2
        confirm = 0
      }
      if (confirm) {
        axios.put(`${SERVER_URL}/diary/updateDiary`, formData)
        .then( (res) => {
          // console.log(res)
          this.simpleOn()
        })
        .catch( (err) => {
          console.log(err)
        })
      }
    },
  },
  created: function () {
    // console.log(this.$route.params.year, this.$route.params.month, this.$route.params.day)
    this.getUser()
    this.getUserPets()
    this.getDiaryInfo()
    if (!this.currentYear) {
      this.currentYear = new Date().getFullYear()
      this.currentMonth = new Date().getMonth()+1
      this.currentDay = new Date().getDate()
    }
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
}

.diary-header {
  display: flex;
  justify-content: space-between;
  margin-top: 5vh;
}

.diary-header-title {
  display: flex;
  flex-direction: column;
  width: 60%;
}

.diary-header-section {
  display: flex;
  /* width: 50%; */
}

.diary-header-date-num {
  font-size: 2vw;
  color: black;
}

.diary-header-date-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-left: 1vw;
  font-size: 1.5vw;
  color: gray;
}

.diary-pet-title {
  font-size: 2vw;
  font-weight: bold;
  margin-bottom: 1vh;
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
  width: 65vw;
  right: 5vw;
  position: absolute;
}

.calendarOff {
  font-size: 3vw;
  z-index: 11;
  position: absolute;
  right: 5vw;
  transform: translate(0, -80%);
  -webkit-transform: translate(0, -80%);
  -ms-transform: translate(0, -80%);
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

.save-btn-div {
  margin-top: 2vh;
  margin-bottom: 5vh;
  width: 100%;
}

.modify-save-btn {
  font-size: 2vw;
  font-weight: bold;
  width: 100%;
  text-align: center;
  padding: 1vw;
  background-color: rgb(255, 166, 166);
  border-radius: 4vw;
  transition-duration: 0.5s;
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


@media ( max-width: 540px ) {
  
  .diary-container {
    display: flex;
    flex-direction: column;
    padding: 0 2vw;
  }

  .diary-header-calendar {
    width: 90vw;
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
    font-size: 6vw;
    z-index: 11;
    position: absolute;
    right: 5vw;
    transform: translate(0, -50%);
    -webkit-transform: translate(0, -50%);
    -ms-transform: translate(0, -50%);
  }
  
  /*  */
  .diary-header-section {
    display: flex;
    /* width: 50%; */
  }

  .diary-header-date-date {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-left: 1vw;
    font-size: 1.5vw;
    color: gray;
  }

  .diary-header-date-num {
    font-size: 4vw;
    color: black;
  }

  .diary-pet-title {
    font-size: 5vw;
    font-weight: bold;
    margin-bottom: 1vh;
  }

  .diary-header-calendar-btn {
    height: 2vw;
    font-size: 4vw;
  }

  .diary-topic {
    margin-top: 4vh;
  }

  .question-feeling {
    font-size: 5vw;
    font-weight: bold;
  }

  .feeling-btn {
    font-size: 5vw;
    font-weight: bold;
    margin-left: 2vw;
    padding: 1.5vw, 3vw;
    background-color: wheat;
    color: black;
    border: 0.3vw solid gray;
    border-radius: 3vw;
    width: 25vw;
  }

  .question-topic {
    font-size: 5vw;
    font-weight: bold;
  }

  .question-weather {
    font-size: 5vw;
    font-weight: bold;
  }

  .diary-title {
    font-size: 7vw;
  }

  .diary-title-input {
    width: 100%;
    font-size: 5vw;
    font-weight: bold;
    margin-top: 0.5vh;
    margin-bottom: 1vh;
    border-bottom: 0.1vw solid black;
  }

  .diary-title-input:focus {
    outline: none;
  }

  .diary-content textarea {
    width: 100%;
    font-size: 4vw;
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

  .save-btn-div-write {
    margin-top: 2vh;
    margin-bottom: 5vh;
    width: 100%;
  }

  .save-btn-write {
    font-size: 5vw;
    font-weight: bold;
    width: 100%;
    text-align: center;
    padding: 1vw;
    background-color: rgb(255, 166, 166);
    border-radius: 4vw;
    transition-duration: 0.5s;
  }

  .save-btn-write:hover {
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
    font-size: 5vw;
    font-weight: bold;
    margin-left: 2vw;
    margin-bottom: 0;
    text-align: center;
    padding: 1vw, 2vw;
    background-color: wheat;
    color: black;
    border: 0.1vw solid gray;
    border-radius: 3vw;
    width: 25vw;
  }

}


</style>
