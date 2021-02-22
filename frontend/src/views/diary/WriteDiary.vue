<template>
  <div style="font-family: GmarketSansMedium;">
    <DiaryNav />
    <AlarmModal 
      v-if="showAlarmModal"
      @done="done"
    />
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
          <button class="diary-header-calendar-btn" @click="showCalendar">
            <i class="far fa-calendar-alt"></i>
          </button>
          <div v-if="calendarShow" class="diary-header-calendar">
            <CalendarModal 
              @choiceDate="changeDate"
            />
          </div>
          <div v-if="calendarShow" @click="showCalendar" class="calendarOff">x</div>
          <div class="diary-header-date-date">
            <div class="diary-header-date-num">{{ currentYear }}년</div>
          </div>
          <div class="diary-header-date-date">
            <div class="diary-header-date-num">{{ currentMonth }}월</div>
          </div>
          <div class="diary-header-date-date">
            <div class="diary-header-date-num">{{ currentDay }}일</div>
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
          <div class="right">
            <label for="file">
              + 사진
              <i class="fas fa-camera-retro"></i>
            </label>
            <input type="file" id="file" ref="files" accept="image/*, video/*" @change="imageUpload" multiple>
            <!-- <button class="feeling-btn">
              + 사진
            </button> -->
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
              placeholder="눌러서 제목을 입력해주세요"
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
        <!--  -->
        <textarea
          rows="10"
          v-model="diaryContent"
          maxlength="500"
          placeholder="눌러서 일기를 입력해주세요"
        >
        </textarea>
      </div>
      <div class="save-btn-div-write">
        <button class="save-btn-write" @click="createDiary">
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

import AlarmModal from "../../components/common/AlarmModal"
import CalendarModal from "../../components/common/CalendarModal"
import TextSelModal from "../../components/diary/TextSelModal"
import SimpleModal from "../../components/common/SimpleModal"
import ImagePreview from "../../components/common/ImagePreview"
import DiaryNav from "../../components/Nav/DiaryNav"
import VueJwtDecode from "vue-jwt-decode"
import axios from "axios";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;


export default {
  name: 'WriteDiary',
  components: {
    AlarmModal,
    CalendarModal,
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
      showAlarmModal: false,
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
      selectedPets: [],
      selectedFeelings: [],
      selectedFeeling: "선택안함",
      selectedFeelingIdx: 9,
      feelingsTitle: "기분",
      selectedTopic: "선택안함",
      selectedTopicIdx: 7,
      topicTitle: "오늘의 활동",
      selectedWeather: "선택안함",
      selectedWeatherIdx: 10,
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
      myDiariesData: [],
    }
  },
  methods: {
    done: function () {
      this.showAlarmModal = !this.showAlarmModal
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
          for (let pet of this.pets) {
            this.petImages.push("http://i4a403.p.ssafy.io/images/" +pet["file_name"])
            // this.petImages.push(require("../../assets/images/petImg/"+pet["file_name"]))
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
    simpleOn: function () {
      this.simpleShow = !this.simpleShow
    },
    simpleOff2: function () {
      this.simpleShow2 = !this.simpleShow2
    },
    imageUpload: function () {
      let num = -1
      for (let i = 0; i < this.$refs.files.files.length && i < 10; i++) {
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
    createDiary: function () {
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
      formData.append("user_id", this.userId)
      formData.append("diary_content", this.diaryContent)
      formData.append("diary_title", this.diaryTitle)
      formData.append("diary_year", this.currentYear)
      formData.append("diary_month", this.currentMonth)
      formData.append("diary_day", this.currentDay)
      formData.append("diary_feeling", this.selectedFeeling)
      formData.append("diary_weather", this.selectedWeather)
      formData.append("diary_activity", this.selectedTopic)
      // for (let key of formData.entries()) {
      //   console.log(`${key}`)
      // }
      let confirm = 1
      for (let myDiary of this.myDiariesData) {
        if (this.currentYear == myDiary.diary_year && this.currentMonth == myDiary.diary_month && this.currentDay == myDiary.diary_day) {
          this.simpleModalContents2 = "이미 같은 날짜에 일기가 있습니다."
          this.simpleShow2 = !this.simpleShow2
          confirm = 0
          break
        }
      }
      if (this.selectedPets.length == 0 && confirm == 1) {
        this.simpleModalContents2 = "펫을 선택해주세요."
        this.simpleShow2 = !this.simpleShow2
        confirm = 0
      }
      if (confirm) {
        axios.post(`${SERVER_URL}/diary/newDiary`, formData)
        .then( (res) => {
          this.simpleOn()
        })
        .catch( (err) => {
          console.log(err)
        })
      }
    },
    getMyDiaries: function () {
      axios.get(`${SERVER_URL}/diary/allDiary/${this.userId}`)
      .then( (res) => {
        this.myDiariesData = res.data
      })
      .catch( (err) => {
        console.log(err)
      })
    },
  },
  created: function () {
    this.getUser()
    this.getUserPets()
    if (!this.currentYear) {
      this.currentYear = new Date().getFullYear()
      this.currentMonth = new Date().getMonth()+1
      this.currentDay = new Date().getDate()
    }
    this.getMyDiaries()
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

.btn-in-icon {
  width: 2.5vw;
  height: 2.5vw;
  border-radius: 70%;
  margin-right: 0.5vw;
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

.diary-header-date {
  display: flex;
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
  font-size: 2vw;
  color: black;
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
  width: 50vw;
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

.diary-header-calendar-btn {
  height: 2vw;
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
  display: flex;
  align-items: center;
  justify-content: center;
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
  border-bottom: 0.1vw solid black;
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
  z-index: 99;
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
  font-size: 1.5vw;
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

  .btn-in-icon {
    width: 5vw;
    height: 5vw;
    margin-right: 1vw;
  }

  .diary-header-calendar {
    width: 50vw;
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
    right: 7vw;
    top: 13vh;
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
    font-size: 3vw;
    color: gray;
  }

  .diary-header-date-num {
    font-size: 2vw;
    color: black;
  }

  .diary-pet-title {
    font-size: 2vw;
    font-weight: bold;
    margin-bottom: 1vh;
  }

  .diary-header-calendar-btn {
    height: 2vw;
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
    border-bottom: 0.1vw solid black;
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

  .save-btn-div-write {
    margin-top: 2vh;
    margin-bottom: 5vh;
    width: 100%;
  }

  .save-btn-write {
    font-size: 1.5vw;
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


}


</style>
