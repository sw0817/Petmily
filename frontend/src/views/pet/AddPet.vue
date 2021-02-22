<template>
  <div style="font-family: GmarketSansMedium;">
    <Navbar
    style="position:sticky; top:0; z-index:3; width: 100%;"
    >
    </Navbar>
    <div class="addPet-container">
      <div class="addPet-title">
        {{ user.user_nickname }}님의 펫 등록
      </div>
      <div class="pet-regist-container">
        <!-- 사진 -->
        <section class="petImg-section">
          <img v-if="!file" class="pet-img" src="@/assets/images/basicPet.png" alt="">
          <div v-else>
            <img :src="preview" class="pet-img"/>
          </div>
          <div class="petImg-info">
            <div class="image-box2">
              <label for="file">
                펫 사진 등록
                <i class="fas fa-camera-retro"></i>
              </label>
              <input type="file" id="file" ref="files" accept="image/*" @change="imageUpload">
            </div>
          </div>
        </section>
        <!-- 펫 정보 -->
        <section class="petInfo-section">
          <div class="petInfo-section-div">
            <div class="addPet-input-name-div">
              <input 
                class="addPet-input-name"
                type="text" 
                v-model="petName"
                maxlength="4"
                placeholder="펫 이름을 입력해주세요."
              >
            </div>
            <select class="addPet-kind" v-model="selectedKind">
              <option selected disabled value="">펫 종류를 선택해주세요.</option>
              <option value="강아지">강아지</option>
              <option value="고양이">고양이</option>
              <option value="햄스터">햄스터</option>
              <option value="앵무새">앵무새</option>
              <option value="거북이">거북이</option>
              <option value="물고기">물고기</option>
              <option value="병아리">병아리</option>
              <option value="토끼">토끼</option>
              <option value="라쿤">라쿤</option>
              <option value="오리">오리</option>
              <option value="고슴도치">고슴도치</option>
              <option value="기니피그">기니피그</option>
              <option value="기타">기타</option>
            </select>
            <div class="addPet-input-birth-div">
              <button @click="showCalendar" class="addPet-calendar-btn">
                <i class="far fa-calendar-alt"></i>
              </button>
              <div v-if="calendarShow" class="diary-header-calendar">
                <CalendarModal 
                  @choiceDate="changeDate"
                />
              </div>
              <div v-if="calendarShow" @click="showCalendar" class="calendarOff2">x</div>
              <div class="addPet-birth-day">{{ currentYear }}년 {{ currentMonth }}월 {{ currentDay }}일</div>
            </div>
            <select class="addPet-kind" v-model="selectedGender">
              <option selected disabled value="">펫 성별을 선택해주세요.</option>
              <option value="M">수컷</option>
              <option value="W">암컷</option>
            </select>
          </div>
        </section>
      </div>
    </div>
    <div class="addPet-content-container">
      <div class="addPet-content">
        <textarea 
          style="width: 100%;" 
          placeholder="회원님의 반려동물에 대해 설명해주세요." 
          v-model="petContent"
          maxlength="50"
          cols="30" rows="2"
        >
        </textarea>
      </div>
    </div>
    <!-- 사진 -->

    <!-- 등록 버튼 -->
    <div class="pet-regist">
      <button class="pet-regist-btn" @click="registerPet">
        펫 등록하기
      </button>
    </div>
    <div class="pet-regist-error-container">
      <div class="pet-regist-error" v-if="error">
        {{ this.error }}
      </div>
    </div>
    <div class="text-modal" v-if="simpleShow">
      <div class="text-modal-wrapper">
        <SimpleModal
          :contents="simpleModalContents"
          @modalOff="simpleOff"
        />
      </div>
    </div>
  </div>
</template>

<script>
import SimpleModal from "../../components/common/SimpleModal"
import CalendarModal from "../../components/common/CalendarModal"
import Navbar from "../../components/Nav/Navbar"
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "AddPet",
  props: {
    user_id: {
      type: Number,
    },
  },
  components: {
    SimpleModal,
    CalendarModal,
    Navbar,
  },
  data: function () {
    return {
      userId: this.$route.params.user_id,
      user: "",
      file: "",
      preview: "",
      petName: "",
      selectedKind: "",
      selectedGender: "",
      petContent: "",
      patternName: /[^a-zA-Z가-힣]/,
      patternNum: /[^0-9]/,
      error: "",
      simpleShow: false,
      simpleModalContents: "펫 등록이 완료되었습니다.",

      // 캘린더
      calendarShow: false,
      currentYear: new Date().getFullYear(),
      currentMonth: new Date().getMonth()+1,
      currentDay: new Date().getDate(),
    }
  },
  methods: {
    getUserInfo: function () {
      axios.get(`${SERVER_URL}/member/getallmember`)
      .then(({data}) => {
        // console.log({data})
        this.user = {data}.data.filter(x => x.user_id == this.userId)[0]
        // console.log(this.user)
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    // 사진용
    imageUpload: function () {
      // console.log(this.$refs)
      this.file = this.$refs.files.files[0]
      this.preview = URL.createObjectURL(this.$refs.files.files[0])
    },
    registerPet: function () {
      if (!this.file) {
        this.error = "펫 사진을 등록해주세요."
        document.querySelector('.image-box2').focus()
      } else if (!this.petName) {
        this.error = "펫 이름을 입력해주세요."
        document.querySelector('.addPet-input-name').focus()
      } else if (this.patternName.test(this.petName)) {
        this.error = "영문, 한글로만 펫 이름을 입력해주세요."
        document.querySelector('.addPet-input-name').focus()
      } else if (!this.selectedKind) {
        this.error = "펫 종류를 선택해주세요."
        document.querySelector('.addPet-kind').focus()
      } else if (!this.petBirth) {
        this.error = "펫 생일을 입력해주세요."
        document.querySelector('.addPet-input-birth').focus()
      } else if (this.patternNum.test(this.petBirth)) {
        this.error = "펫 생일을 숫자로만 입력해주세요."
        document.querySelector('.addPet-input-birth').focus()
      } else if (this.petBirth.length != 8) {
        this.error = "펫 생일 8자리를 입력해주세요."
        document.querySelector('.addPet-input-birth').focus()
      } else if (!this.selectedGender) {
        this.error = "펫 성별을 등록해주세요."
        document.querySelector('.pet-gender').focus()
      } else {
        this.error = ""
        const formData = new FormData()
        // console.log(this.file)
        formData.append("fileupload", this.file)
        formData.append("user_id", this.userId)
        formData.append("pet_name", this.petName)
        formData.append("pet_kind", this.selectedKind)
        formData.append("pet_gender", this.selectedGender)
        formData.append("pet_age", this.petBirth)
        formData.append("pet_content", this.petContent)
        // for (let key of formData.entries()) {
        //   console.log(`${key}`)
        // }
        axios.post(`${SERVER_URL}/pet/insertProc`, formData
          )
          .then( (res) => {
            // console.log("모든 펫 정보")
            // console.log(res.data);
            this.simpleShow = !this.simpleShow
          })
          .catch( (err) => {
            console.log(err)
          })
      }
    },
    simpleOff: function () {
      this.simpleShow = !this.simpleShow
      this.$router.push({name: "UserProfile"});
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
  },
  computed: {
    petBirth: function () {
      if (String(this.currentMonth).length == 1) {
        if (String(this.currentDay).length == 1) {
          return `${this.currentYear}0${this.currentMonth}0${this.currentDay}`
        } else {
          return `${this.currentYear}0${this.currentMonth}${this.currentDay}`
        }
      } else {
        if (String(this.currentDay).length == 1) {
          return `${this.currentYear}${this.currentMonth}0${this.currentDay}`
        }
      }
      return `${this.currentYear}${this.currentMonth}${this.currentDay}`
    }
  },
  created: function () {
    this.getUserInfo()
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
.calendarOff2 {
  font-size: 3vw;
  z-index: 11;
  position: absolute;
  /* top: 30vw; */
  right: 5vw;
  transform: translate(0, -80%);
  -webkit-transform: translate(0, -80%);
  -ms-transform: translate(0, -80%);
}

.calendarOff2:hover {
  cursor: pointer;
}

.addPet-container {
  width: 90%;
  margin-left: 5%;
  margin-right: 5%;
  margin-top: 2vh;
}

.addPet-title {
  font-size: 4vw;
}

.addPet-input-name-div {
  width: 100%;
  margin-right: 0;
  /* margin-bottom: 2vh; */
}

.addPet-input-name {
  width: 100%;
  font-size: 2vw;
  margin-bottom: 2vh;
  border: none;
  border-bottom: 0.2vw solid black;
}

.addPet-input-birth-div {
  display: flex;
  width: 100%;
  margin-bottom: 2vh;
  border-bottom: 0.2vw solid black;
}

/* .addPet-input-birth {
  width: 100%;
  font-size: 2vw;
  border: none;
  border-bottom: 0.2vw solid black;
} */

.addPet-calendar-btn {
  font-size: 2vw;
}

.addPet-birth-day {
  width: 100%;
  border: none;
  font-size: 2vw;
  margin-left: 1vw;
}

.addPet-kind {
  width: 100%;
  border: none;
  border-bottom: 0.2vw solid black;
  border-radius: 0px;
  font-size: 2vw;
  margin-bottom: 2vh;
}

.addPet-content-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 3vh;
}

.addPet-content {
  width: 80%;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 0.2vw solid black;
}

.addPet-content textarea {
  font-size: 2vw;
}

.pet-regist-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 3vh;
}

.pet-img {
  width: 25vw;
  height: 25vw;
  border-radius: 70%;
}

.image-box2 {
  width: 80%;
  margin-top: 2.5vw;
  padding-bottom: 1.3vw;
  text-align: center;
}

.image-box2 input[type='file'] {
  position: absolute;
  width: 0;
  height: 0;
  padding: 0;
  overflow: hidden;
  border: 0;
}

.image-box2 label {
  display: inline-block;
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
}

.image-box2 label:hover {
  background-color:mediumslateblue;
  color:white;
}

.petImg-info {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.petImg-info span {
  font-size: 2vw;
}

.petImg-info i {
  margin-left: 1vw;
  font-size: 4vw;
}

.petImg-section {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 40%;
}

.petInfo-section {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 55%;
  margin-left: 5%;
}

.petInfo-section-div {
  width: 80%;
}

.pet-regist {
  margin-top: 5vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.pet-delete {
  margin-top: 3vh;
  margin-bottom: 10vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.pet-regist-error-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 0.5vh;
  /* margin-bottom: 10vh; */
}

.pet-regist-error {
  margin-top: 0.5vh;
  font-size: 1.5vw;
  font-weight: bold;
  color: red;
}

.pet-regist-btn {
  width: 80%;
  padding: 0.5vw;
  border-radius: 1vw;
  border: 0.3vw solid;
  border-color: mediumslateblue;
  background-color: white;
  color:mediumslateblue;
  font-size: 2.5vw;
  font-weight: bold;
  transition-duration: 0.5s;
}

.pet-regist-btn:hover {
  background-color:mediumslateblue;
  color:white;
}

.pet-delete-btn {
  width: 80%;
  padding: 0.5vw;
  border-radius: 1vw;
  border: 0.3vw solid;
  border-color: gray;
  background-color: white;
  color:gray;
  font-size: 2.5vw;
  font-weight: bold;
  transition-duration: 0.5s;
}

.pet-delete-btn:hover {
  background-color:gray;
  color:white;
}

@media ( max-width: 540px) {
  
  .calendarOff2 {
    font-size: 6vw;
    z-index: 11;
    position: absolute;
    /* top: 30vw; */
    right: 5vw;
    transform: translate(0, -80%);
    -webkit-transform: translate(0, -80%);
    -ms-transform: translate(0, -80%);
  }

  .addPet-title {
    font-size: 6vw;
  }

  .addPet-input-name {
    font-size: 4vw;
  }

  .addPet-input-birth {
    font-size: 4vw;
  }

  .addPet-kind {
    font-size: 4vw;
  }

  .addPet-content textarea {
    font-size: 4vw;
  }

  .pet-img {
    width: 25vw;
    height: 25vw;
    border-radius: 70%;
  }

  .image-box2 label {
    font-size: 3vw;
    text-align: center;
  }

  .petImg-info {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .petImg-info span {
    font-size: 4vw;
  }

  .petImg-info svg {
    margin-left: 1vw;
    font-size: 4vw;
  }

  .petImg-section {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    width: 40%;
  }

  .petInfo-section-div {
    width: 100%;
  }

  .pet-regist {
    margin-top: 5vh;
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .pet-regist-error {
    margin-top: 0.5vh;
    font-size: 4vw;
    font-weight: bold;
    color: red;
  }

  .pet-regist-btn {
    width: 80%;
    padding: 1vw;
    border-radius: 1vw;
    border: 0.3vw solid;
    border-color: mediumslateblue;
    background-color: white;
    color:mediumslateblue;
    font-size: 4vw;
    transition-duration: 0.5s;
  }

  .pet-delete-btn {
    width: 80%;
    padding: 1vw;
    border-radius: 1vw;
    border: 0.3vw solid;
    border-color: gray;
    background-color: white;
    color:gray;
    font-size: 4vw;
    transition-duration: 0.5s;
  }

  .pet-gender {
    font-size: 4vw;
  }

  .pet-gender select {
    font-size: 4vw;
  }

  .addPet-birth-day {
    font-size: 4vw;
    font-weight: bold;
  }

  .addPet-calendar-btn {
    font-size: 4vw;
    font-weight: bold;
  }

}



</style>
