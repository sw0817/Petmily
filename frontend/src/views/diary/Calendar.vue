<template>
  <div class="calendar" style="font-family: GmarketSansMedium;">
    <DiaryNav />
    <div class="calendar-header">
      <div class="select-year">
        <div class="change-background">
          <label for="file">
            배경
            <i class="fas fa-camera-retro"></i>
          </label>
          <div v-if="backgroundImg" class="background-save" @click="postBgImg">
            저장
          </div>
          <div v-if="backgroundImg" class="background-cancle" @click="BgImgCancle">
            취소
          </div>
          <div v-else-if="!backgroundImg && myBackgroundImg" class="background-cancle" @click="deleteBgImg">
            삭제
          </div>
          <input type="file" id="file" ref="files" accept="image/*" @change="imageUpload">
        </div>
        <!-- <div class="select-year-btn" @click="onClickPrevYear">prevYear</div>
        <div class="select-year-btn" @click="onClickNextYear">nextYear</div> -->
      </div>
      <div class="select-month">
        <div class="calendar-btn" @click="onClickPrev(currentMonth)">◀</div>
        <div class="calendar-year" @click="calendarShow">{{ currentYear }}년 {{ currentMonth }}월</div>
        <div class="calendar-btn" @click="onClickNext(currentMonth)">▶</div>
      </div>
      <div class="select-this-month">
        <div class="select-year-btn" @click="diaryCollectionOn">
          모아보기
        </div>
        <div class="select-year-btn" @click="onClickThisMonth">
          오늘
        </div>
        <div>
          <button class="write-diary-btn" @click="writeDiaryToday">
            <i class="fas fa-edit"></i>
          </button>
        </div>
      </div>
    </div>

    <div class="calendar-table">
      <!--  -->
      <div v-if="preview" class="background-picture">
        <div class="background-picture-center">
          <img class="background-picture-img" :src="preview">
        </div>
      </div>
      <div v-else-if="myBackgroundImg" class="background-picture">
        <div class="background-picture-center">
          <img class="background-picture-img" :src="myBackgroundImg">
        </div>
      </div>
      <!--  -->
      <div class="calendar-day-names">
        <div class="calendar-day-name" v-for="(weekName, index) in weekNames" :key="index">
          {{ weekName }}
        </div>
      </div>
      <div class="calendar-days-container">
        <div class="calendar-week-row" v-for="(row, index) in currentCalendarMatrix" :key="index">
          <div class="calendar-table-day" v-for="(day, index2) in row" :key="index2">
            <button v-if="isToday(day)" class="calendar-table-day-num-today" @click="writeDiary(day)">
              {{ day }}
            </button>
            <div v-else class="calendar-table-day-num" @click="writeDiary(day)">
              {{ day }}
            </div>
            <div
              v-for="(myDiary, idx) in myDiaries"
              :key="idx"
            >
              <div
                v-if="currentYear == myDiary.diary_year && currentMonth == myDiary.diary_month && day == myDiary.diary_day"
                @click="viewDiary(day)"
                class="isDiary"
              >
                <div class="calendar-tag-div">
                  <span v-for="(pet, idx) in myDiary.pets" :key="idx+'a'">
                    <span v-for="(petImage, idx) in petImages" :key="idx+'b'">
                      <img class="calendar-pet-img" v-if="petImage.id == pet" :src="petImages[idx].url">
                    </span>
                  </span>
                </div>
                <div class="calendar-tag-div">
                  <div v-for="(img, idx) in myDiary.tag_img" :key="idx+'t'">
                    <img class="calendar-pet-img" :src="img" alt="">
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="text-modal" v-if="selectCalendarShow">
      <div class="text-modal-wrapper">
        <SelectCalendar
          @selectDate="selectDate"
          @keydownEsc="keydownEsc"
        />
      </div>
    </div>
    <div class="text-modal" v-if="diaryCollectionShow">
      <div class="text-modal-wrapper">
        <DiaryCollectionModal
          :diaries="myDiaries"
          @selectDiary="selectDiary"
          @exit="diaryCollectionOff"
        />
      </div>
    </div>
  </div>
</template>

<script>
import SelectCalendar from "../../components/diary/SelectCalendar"
import DiaryCollectionModal from "../../components/diary/DiaryCollectionModal"
import DiaryNav from "../../components/Nav/DiaryNav"
import VueJwtDecode from "vue-jwt-decode"
import axios from "axios";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: 'Calendar',
  components: {
    SelectCalendar,
    DiaryCollectionModal,
    DiaryNav,
  },
  data: function () {
    return {
      weekNames: ["월요일", "화요일", "수요일","목요일", "금요일", "토요일", "일요일"],
      rootYear: 1904,
      rootDayOfWeekIndex: 4,
      currentYear: new Date().getFullYear(),
      currentMonth: new Date().getMonth()+1,
      currentDay: new Date().getDate(),
      currentMonthStartWeekIndex: null,
      currentCalendarMatrix: [],
      endOfDay: null,
      selectCalendarShow: false,
      diaryCollectionShow: false,
      userId: 0,
      myDiaries: [],
      myDiariesData: [],
      myDiariesFile: [],
      myDiariesPet: [],
      myPets: [],
      myPetIds: [],
      petImages: [],
      // memoDatas: [],
      backgroundImg: "",
      myBackgroundImg: "",
      preview: "",
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
    }
  },
  mounted: function () {
      this.init();
  },
  methods: {
    deleteBgImg: function () {
      axios.delete(`${SERVER_URL}/diary/deletebg/${this.userId}`)
      .then( (res) => {
        // console.log("내 배경사진 삭제")
        // console.log(res)
        this.backgroundImg = ""
        this.myBackgroundImg = ""
        this.preview = ""
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    updateBgImg: function () {
      const formData = new FormData()
      formData.append("user_id", this.userId)
      formData.append("filesupload", this.backgroundImg)
      axios.put(`${SERVER_URL}/diary/updatebg`, formData)
      .then( (res) => {
        // console.log("내 배경사진 수정")
        // console.log(res)
        if (res.data) {
          this.myBackgroundImg = this.myBackgroundImg = "http://i4a403.p.ssafy.io/images/"+ res.data
          // this.myBackgroundImg = this.myBackgroundImg = require("../../assets/images/DiaryBgImg/"+res.data)
          this.preview = ""
          this.backgroundImg = ""
        }
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getMyBgImg: function () {
      axios.get(`${SERVER_URL}/diary/getbg/${this.userId}`)
      .then( (res) => {
        // console.log("내 배경사진")
        // console.log(res.data)
        if (res.data) {
          // console.log("뭐냐고")
          this.myBackgroundImg = "http://i4a403.p.ssafy.io/images/"+res.data.file_name
          // this.myBackgroundImg = require("../../assets/images/DiaryBgImg/"+res.data.file_name)
          this.preview = "http://i4a403.p.ssafy.io/images/"+res.data.file_name
          // this.preview = require("../../assets/images/DiaryBgImg/"+res.data.file_name)

        }
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    BgImgCancle: function () {
      this.backgroundImg = ""
      this.preview = ""
    },
    postBgImg: function () {
      const formData = new FormData()
      formData.append("filesupload", this.backgroundImg)
      formData.append("user_id", this.userId)
      if (this.myBackgroundImg.length < 1) {
        axios.post(`${SERVER_URL}/diary/newbg`, formData)
        .then( (res) => {
          // console.log(res)
          this.backgroundImg = ""
          this.preview = ""
          this.myBackgroundImg = "http://i4a403.p.ssafy.io/images/" + res.data
          // this.myBackgroundImg = require("../../assets/images/DiaryBgImg/"+res.data)
        })
        .catch( (err) => {
          console.log(err)
        })
      } else {
        this.updateBgImg()
      }
    },
    imageUpload: function () {
      this.backgroundImg = this.$refs.files.files[0]
      this.preview = URL.createObjectURL(this.$refs.files.files[0])
    },
    init: function () {
      this.currentMonthStartWeekIndex = this.getStartWeek(this.currentYear, this.currentMonth);
      this.endOfDay = this.getEndOfDay(this.currentYear, this.currentMonth);
      this.initCalendar();
    },
    initCalendar: function () {
      this.currentCalendarMatrix = [];
      let day=1;
      for (let i=0; i<6; i++) {
        let calendarRow = [];
        for (let j=0; j<7; j++) {
          if (i==0 && j<this.currentMonthStartWeekIndex) {
            calendarRow.push("");
          }
          else if (day<=this.endOfDay) {
            calendarRow.push(day);
            day++;
          }
          else {
            calendarRow.push("");
          }
        }
        this.currentCalendarMatrix.push(calendarRow);
      }
    },
    getEndOfDay: function (year, month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
              return 31;
            case 4:
            case 6:
            case 9:
            case 11:
              return 30;
            case 2:
              if ( (year%4 == 0) && (year%100 != 0) || (year%400 == 0) ) {
              return 29;   
              }
              else {
                  return 28;
              }
            default:
              // console.log("unknown month " + month);
              return 0;
        }
    },
    getStartWeek: function (targetYear, targetMonth) {
      let year = this.rootYear;
      let month = 1;
      let sumOfDay = this.rootDayOfWeekIndex;
      let w = 1
      while (w) {
        if (targetYear > year) {
          for (let i=0; i<12; i++) {
            sumOfDay += this.getEndOfDay(year, month+i);
          }
          year++;
        }
        else if (targetYear == year) {
          if (targetMonth > month) {
            sumOfDay += this.getEndOfDay(year, month);
            month++;
          }
          else if (targetMonth == month) {
            return (sumOfDay)%7;
          }
        }
      }
    },
    onClickPrev: function (month) {
      month--;
      if (month<=0) {
        this.currentMonth = 12;
        this.currentYear -= 1;
      }
      else {
        this.currentMonth -= 1;
      }
      this.init();
    },
    onClickNext: function (month) {
      month++;
      if (month>12) {
        this.currentMonth = 1;
        this.currentYear += 1;
      }
      else {
        this.currentMonth += 1;
      }
      this.init();
    },
    onClickPrevYear: function () {
      this.currentYear -= 1;
      this.init();
    },
    onClickNextYear: function () {
      this.currentYear += 1;
      this.init();
    },
    onClickThisMonth: function () {
      let thisYear = new Date().getFullYear()
      let thisMonth = new Date().getMonth()+1
      this.currentYear = thisYear
      this.currentMonth = thisMonth
      this.init()
    },
    writeDiary: function (day) {
      this.$router.push({ name: 'WriteDiary', params: { month: this.currentMonth, year: this.currentYear, day: day } })
    },
    writeDiaryToday: function () {
      let date = new Date();
      this.$router.push({ name: 'WriteDiary', params: { month: date.getMonth()+1, year: date.getFullYear(), day: date.getDate() } })
    },
    isToday: function (day) {
      let date = new Date();
      if (this.currentYear == date.getFullYear() && this.currentMonth == date.getMonth()+1 && day == date.getDate()) {
        return true
      } 
    },
    calendarShow: function () {
      this.selectCalendarShow = !this.selectCalendarShow
    },
    selectDate: function (date) {
      this.currentYear = date.year
      this.currentMonth = date.month
      this.init()
      this.selectCalendarShow = !this.selectCalendarShow
    },
    getUserPets: function(){
      axios.get(`${SERVER_URL}/pet/selectAll/${this.userId}`)
        .then(({data}) => {
          // console.log(data)
          this.myPets = data
          for (let pet of this.myPets) {
            this.petImages.push(
              {
                "id": pet.pet_id,
                // "url": require("../../assets/images/petImg/"+pet["file_name"])
                "url": "http://i4a403.p.ssafy.io/images/"+pet["file_name"]
            })
            this.myPetIds.push(
              pet.pet_id
            )
          }
        })
        .catch( (err) => {
          console.log(err)
        })
    },
    getMyDiaries: function () {
      axios.get(`${SERVER_URL}/diary/allDiary/${this.userId}`)
      .then( (res) => {
        this.myDiariesData = res.data
        // console.log("다이어리정보")
        // console.log(res.data)
        this.getMyDiariesFiles()
        // console.log(this.myDiaries)
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getMyDiariesFiles: function () {
      axios.get(`${SERVER_URL}/diary/allDiaryFile/${this.userId}`)
      .then( (res) => {
        // console.log("다이어리파일")
        // console.log(res)
        this.myDiariesFile = res.data
        this.getMyDiariesPets()
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getMyDiariesPets: function () {
      axios.get(`${SERVER_URL}/diary/allPet/${this.userId}`)
      .then( (res) => {
        // console.log("펫정보")
        // console.log(res)
        this.myDiariesPet = res.data
        this.getSum()
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getSum: function () {
      // console.log(this.myDiariesData)
      for (let i=0; i<this.myDiariesData.length; i++) {
        var temp = {};
        temp["diary_id"] = this.myDiariesData[i].diary_id;
        temp["diary_content"] = this.myDiariesData[i].diary_content;
        temp["diary_year"] = this.myDiariesData[i].diary_year;
        temp["diary_year"] = this.myDiariesData[i].diary_year;
        temp["diary_month"] = this.myDiariesData[i].diary_month;
        temp["diary_day"] = this.myDiariesData[i].diary_day;
        temp["diary_activity"] = this.myDiariesData[i].diary_activity;
        temp["tag_img"] = []
        for (let j = 0; j < this.topicExam.length-1; j++) {
          if (this.topicExam[j] == this.myDiariesData[i].diary_activity) {
            temp["tag_img"].push(this.activityIcon[j])
            break
          }
        }
        temp["diary_title"] = this.myDiariesData[i].diary_title;
        temp["diary_feeling"] = this.myDiariesData[i].diary_feeling;
        for (let j = 0; j < this.feelingsExam.length-1; j++) {
          if (this.feelingsExam[j] == this.myDiariesData[i].diary_feeling) {
            temp["tag_img"].push(this.emotionIcon[j])
            break
          }
        }
        temp["diary_weather"] = this.myDiariesData[i].diary_weather;
        for (let j = 0; j < this.weatherExam.length-1; j++) {
          if (this.weatherExam[j] == this.myDiariesData[i].diary_weather) {
            temp["tag_img"].push(this.weatherIcon[j])
            break
          }
        }
        temp["user_id"] = this.myDiariesData[i].user_id;
        temp["diary_gps"] = this.myDiariesData[i].diary_gps;
        temp["file_name"] = []
        temp["file_type"] = []
        temp["pets"] = []
        // temp["diary_gps"] = [];
        // console.log("여기임")
        // console.log(this.myDiariesFile)
        for(let j = 0; j < this.myDiariesFile.length; j++){
          if(temp["diary_id"] == this.myDiariesFile[j].diary_id){
            temp["file_name"].push(this.myDiariesFile[j].file_name);
            temp["file_type"].push(this.myDiariesFile[j].file_type)
          } 
        }
        for(let j = 0; j < this.myDiariesPet.length; j++){
          if(temp["diary_id"] == this.myDiariesPet[j].diary_id){
            temp["pets"].push(this.myDiariesPet[j].pet_id);
          }
        }
        if (this.myDiaries.length < 1) {
          this.myDiaries.push(temp);
        } else {
          this.myDiaries.splice(0, 0, temp)
        }
      }
      // console.log(this.myDiaries)
      this.getMyBgImg()
    },
    getMyId: function () {
      const token = localStorage.getItem('jwt')
      if (token) {
        this.userId = VueJwtDecode.decode(token).user.user_id
        // this.myNickname = VueJwtDecode.decode(token).user.user_nickname
      }
      this.getUserPets()
      this.getMyDiaries()
    },
    viewDiary: function (day) {
      this.$router.push({ name: 'MyDiary', params: { year: this.currentYear, month: this.currentMonth, day: day } })
    },
    diaryCollectionOn: function () {
      this.diaryCollectionShow = !this.diaryCollectionShow
    },
    diaryCollectionOff: function () {
      this.diaryCollectionShow = !this.diaryCollectionShow
    },
    selectDiary: function (diary) {
      // console.log(diary)
      // console.log(diary.diary_year)
      this.$router.push({ name: 'MyDiary', params: { year: diary.diary_year, month: diary.diary_month, day: diary.diary_day } })
      this.diaryCollectionShow = !this.diaryCollectionShow
    },
    keydownEsc: function () {
      this.selectCalendarShow = false
    },
  },
  created: function () {
    this.getMyId()
  },
}
</script>

<style type="text/css">
@font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}

.change-background {
  display: flex;
  align-items: center;
}

.background-save {
  font-size: 2vw;
  margin-left: 1vw;
}

.background-cancle {
  font-size: 2vw;
  margin-left: 1vw;
}

.background-save:hover {
  cursor: pointer;
  border-bottom: 0.5vw solid black;
}

.background-cancle:hover {
  cursor: pointer;
  border-bottom: 0.5vw solid black;
}

.change-background input {
  position: absolute;
  width: 0;
  height: 0;
  padding: 0;
  overflow: hidden;
  border: 0;
}

.change-background label {
  display: inline-block;
  margin-bottom: 0;
  color:  black;
  vertical-align: middle;
  font-size: 2vw;
  cursor: pointer;
}

.change-background label:hover {
  border-bottom: 0.5vw solid black;
}

.calendar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 3vw 0;
}

.calendar-btn:hover {
  cursor: pointer;
}

.calendar-year:hover {
  cursor: pointer;
}

.select-year {
  display: flex;
  align-items: center;
  font-size: 2vw;
  width: 22%;
  margin-left: 3%;
}

.select-year-btn {
  margin-right: 1vw;
}

.select-year-btn:hover {
  cursor: pointer;
  border-bottom: 0.5vw solid black;
}

.select-month {
  display: flex;
  align-items: center;
  font-size: 3vw;
}

.select-this-month {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-size: 2vw;
  width: 22%;
  margin-right: 3%;
}

.select-this-month div:hover {
  cursor: pointer;
}

.calendar-table {
  position: relative;
  display: flex;
  flex-direction: column;
}

.background-picture {
  position: absolute;
  top: 4vw;
  overflow: hidden;
  width: 100vw;
  height: 78vh;
  display: flex;
  justify-content: center;
}

.background-picture-center {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  -webkit-transform: translate(50%, 50%);
  -ms-transform: translate(50%, 50%);
  transform: translate(50%, 50%);
}

.background-picture-img {
  top: 0;
  left: 0;
  width: 100vw;
  height: auto;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  opacity: 0.5;
}

.calendar-day-names {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.calendar-day-name {
  width: 14.28%;
  font-size: 1.2vw;
  padding: 1vw 0;
  /* padding-left: 1vw; */
  text-align: center;
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.calendar-week-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.calendar-table-day {
  position: relative;
  width: 14.28%;
  height: 13vh;
  /* padding-left: 1vw; */
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
  transition-duration: 0.5s;
}

.calendar-table-day:hover {
  background-color: tomato;
}

.calendar-table-day-num {
  position: absolute;
  font-size: 1.2vw;
  top: 5%;
  right: 5%;
}

.calendar-table-day-num-today {
  position: absolute;
  /* width: 2vw;
  height: 2vw; */
  /* border: 0.2vw solid red;
  border-radius: 70%; */
  font-size: 1.5vw;
  color: red;
  top: 5%;
  right: 5%;
}

/* .calendar-table-day-num-today:hover {
  cursor: pointer;
} */

.calendar-table-day-num:hover {
  cursor: pointer;
}

.write-diary-btn {
  margin-left: 2vw;
}

.isDiary {
  font-size: 1.5vw;
  margin-top: 1vh;
  margin-left: 1vw;
}

.isDiary:hover {
  cursor: pointer;
}

.calendar-pet-img {
  margin-top: 5px;
  margin-right: 5px;
  width: 40px;
  height: 40px;
  border-radius: 70%;
}

.calendar-tag-div {
  display: flex;
}

@media (max-width: 1100px) {

.change-background {
  display: flex;
  align-items: center;
}

.background-save {
  font-size: 2vw;
  margin-left: 1vw;
}

.background-cancle {
  font-size: 2vw;
  margin-left: 1vw;
}

.background-save:hover {
  cursor: pointer;
  border-bottom: 0.5vw solid black;
}

.background-cancle:hover {
  cursor: pointer;
  border-bottom: 0.5vw solid black;
}

.change-background input {
  position: absolute;
  width: 0;
  height: 0;
  padding: 0;
  overflow: hidden;
  border: 0;
}

.change-background label {
  display: inline-block;
  margin-bottom: 0;
  color:  black;
  vertical-align: middle;
  font-size: 2vw;
  cursor: pointer;
}

.change-background label:hover {
  border-bottom: 0.5vw solid black;
}

.calendar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin: 3vw 0;
}

.calendar-btn:hover {
  cursor: pointer;
}

.calendar-year:hover {
  cursor: pointer;
}

.select-year {
  display: flex;
  align-items: center;
  font-size: 2vw;
  width: 22%;
  margin-left: 3%;
}

.select-year-btn {
  margin-right: 1vw;
}

.select-year-btn:hover {
  cursor: pointer;
  border-bottom: 0.5vw solid black;
}

.select-month {
  display: flex;
  align-items: center;
  font-size: 3vw;
}

.select-this-month {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-size: 2vw;
  width: 22%;
  margin-right: 3%;
}

.select-this-month div:hover {
  cursor: pointer;
}

.calendar-table {
  position: relative;
  display: flex;
  flex-direction: column;
}

.background-picture {
  position: absolute;
  top: 4vw;
  overflow: hidden;
  width: 100vw;
  height: 78vh;
  display: flex;
  justify-content: center;
}

.background-picture-center {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  -webkit-transform: translate(50%, 50%);
  -ms-transform: translate(50%, 50%);
  transform: translate(50%, 50%);
}

.background-picture-img {
  top: 0;
  left: 0;
  width: 100vw;
  height: auto;
  -webkit-transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  opacity: 0.5;
}

.calendar-day-names {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.calendar-day-name {
  width: 14.28%;
  font-size: 1.2vw;
  padding: 1vw 0;
  /* padding-left: 1vw; */
  text-align: center;
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.calendar-week-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.calendar-table-day {
  position: relative;
  width: 14.28%;
  height: 13vh;
  /* padding-left: 1vw; */
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
  transition-duration: 0.5s;
}

.calendar-table-day:hover {
  background-color: tomato;
}

.calendar-table-day-num {
  position: absolute;
  font-size: 1.2vw;
  top: 5%;
  right: 5%;
}

.calendar-table-day-num-today {
  position: absolute;
  /* width: 2vw;
  height: 2vw; */
  /* border: 0.2vw solid red;
  border-radius: 70%; */
  font-size: 1.5vw;
  color: red;
  top: 5%;
  right: 5%;
}

/* .calendar-table-day-num-today:hover {
  cursor: pointer;
} */

.calendar-table-day-num:hover {
  cursor: pointer;
}

.write-diary-btn {
  margin-left: 2vw;
}

.isDiary {
  font-size: 1.5vw;
  margin-top: 1vh;
  margin-left: 1vw;
}

.isDiary:hover {
  cursor: pointer;
}

.calendar-pet-img {
  margin-top: 0.5vw;
  margin-right: 0.5vw;
  width: 3vw;
  height: 3vw;
  border-radius: 70%;
}

.calendar-tag-div {
  display: flex;
}

}


@media ( max-width: 540px) {

  .calendar-tag-div {
    display: flex;
    flex-direction: column;
  }

  .calendar-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin: 3vw 0;
  }

  .select-year {
    display: flex;
    align-items: center;
    font-size: 3vw;
    font-weight: bold;
    width: 30%;
    margin-left: 3%;
  }

  .background-save {
    font-size: 3vw;
  }

  .background-cancle {
    font-size: 3vw;
  }

  .change-background label {
    font-size: 3vw;
  }

  .select-month {
    display: flex;
    align-items: center;
    font-size: 4vw;
  }

  .select-this-month {
    font-size: 3vw;
    font-weight: bold;
    width: 30%;
    margin-right: 3%;
  }

  .calendar-day-name {
    width: 14.28%;
    font-size: 1.5vw;
    font-weight: bold;
    padding: 1vw 0;
    text-align: center;
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
    border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
    border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .calendar-week-row {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .calendar-table-day {
    position: relative;
    width: 14.28%;
    height: 15vh;
    border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
    border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
    transition-duration: 0.5s;
  }

  .background-picture {
    position: absolute;
    top: 5vw;
    overflow: hidden;
    width: 100vw;
    height: 90vh;
    display: flex;
    justify-content: center;
  }

  .calendar-table-day-num {
    position: absolute;
    font-size: 4vw;
    top: 2%;
    right: 5%;
  }

  .calendar-table-day-num-today {
    position: absolute;
    font-size: 4.5vw;
    color: red;
    top: 2%;
    right: 5%;
  }

  .write-diary-btn {
    margin-left: 2vw;
  }

  .isDiary {
    display: flex;
    font-size: 1.5vw;
    margin-top: 3vh;
    margin-left: 0.5vw;
    margin-right: 0.5vw;
  }

  .calendar-pet-img {
    margin-top: 0.5vw;
    width: 6vw;
    height: 6vw;
    border-radius: 70%;
  }

}

</style>
