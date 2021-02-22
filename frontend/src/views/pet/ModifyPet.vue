<template>
  <div style="font-family: GmarketSansMedium;">
    <Navbar
    style="position:sticky; top:0; z-index:3; width: 100%;"
    >
    </Navbar>
    <div class="addPet-container">
      <div class="addPet-title">
        {{ user.user_nickname }}님의 펫 수정
      </div>
      <div class="pet-regist-container">
        <!-- 사진 -->
        <section class="petImg-section">
          <img v-if="!newFile" class="pet-img" :src="preview">
          <div v-else>
            <img :src="newPreview" class="pet-img"/>
          </div>
          <div class="petImg-info">
            <div class="image-box2">
              <label for="file">
                펫 사진 수정
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
        펫 수정하기
      </button>
    </div>
    <div class="pet-regist-error-container">
      <div class="pet-regist-error" v-if="error">
        {{ this.error }}
      </div>
    </div>
    <div class="pet-delete">
      <button class="pet-delete-btn" @click="deletePet">
        펫 삭제하기
      </button>
    </div>
    <div class="text-modal" v-if="simpleShow">
      <div class="text-modal-wrapper">
        <SimpleModal
          :contents="simpleModalContents"
          @modalOff="simpleOff"
        />
      </div>
    </div>
    <div class="text-modal" v-if="yesNoShow">
      <div class="text-modal-wrapper">
        <YesNoModal
          :contents="yesNoModalContents"
          @modalOff="yesNoOff"
          @deleteDiary="deletePetOn"
        />
      </div>
    </div>
  </div>
</template>

<script>
import SimpleModal from "../../components/common/SimpleModal"
import YesNoModal from "../../components/common/YesNoModal"
import CalendarModal from "../../components/common/CalendarModal"
import Navbar from "../../components/Nav/Navbar"
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "ModifyPet",
  props: {
    user_id: {
      type: Number,
    },
    pet_id: {
      type: Number,
    },
  },
  components: {
    SimpleModal,
    CalendarModal,
    YesNoModal,
    Navbar,
  },
  data: function () {
    return {
      userId: this.$route.params.user_id,
      user: "",
      file: "",
      newFile: "",
      preview: "",
      newPreview: "",
      petName: "",
      selectedKind: "",
      selectedGender: "",
      petContent: "",
      patternName: /[^a-zA-Z가-힣]/,
      patternNum: /[^0-9]/,
      error: "",
      simpleShow: false,
      simpleModalContents: "펫 수정이 완료되었습니다.",
      yesNoShow: false,
      yesNoModalContents: "정말로 펫을 삭제하시겠습니까 ?",

      // 캘린더
      calendarShow: false,
      currentYear: new Date().getFullYear(),
      currentMonth: new Date().getMonth()+1,
      currentDay: new Date().getDate(),

      // 원래 펫 정보
      petInfo: [],
    }
  },
  methods: {
    getUserPets: function(){
      axios.get(`${SERVER_URL}/pet/selectAll/${this.userId}`)
        .then(({data}) => {
          for (let pet of data) {
            if (pet.pet_id == this.$route.params.pet_id) {
              this.petInfo = pet
              // console.log(this.petInfo)
              break
            }
          }
          if (this.petInfo.length < 1) {
            this.$router.push({ name: "FeedMain" })
          }
          this.petName = this.petInfo.pet_name
          this.selectedKind = this.petInfo.pet_kind
          this.selectedGender = this.petInfo.pet_gender
          this.petContent = this.petInfo.pet_content
          this.currentYear = Number(String(this.petInfo.pet_age).slice(0, 4))
          this.currentMonth = Number(String(this.petInfo.pet_age).slice(4, 6))
          this.currentDay = Number(String(this.petInfo.pet_age).slice(6, 8))
          this.preview = "http://i4a403.p.ssafy.io/images/"+this.petInfo["file_name"]
          // this.preview = require("../../assets/images/petImg/"+this.petInfo["file_name"])
        })
        .catch( (err) => {
          console.log(err)
        })
    },
    getUserInfo: function () {
      axios.get(`${SERVER_URL}/member/getallmember`)
      .then(({data}) => {
        this.user = {data}.data.filter(x => x.user_id == this.userId)[0]
        this.getUserPets()
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    // 사진용
    imageUpload: function () {
      // console.log(this.$refs)
      this.newFile = this.$refs.files.files[0]
      this.newPreview = URL.createObjectURL(this.$refs.files.files[0])
    },
    registerPet: function () {
      if (!this.petName) {
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
        formData.append("fileupload", this.newFile)
        formData.append("pet_id", this.petInfo.pet_id)
        formData.append("pet_name", this.petName)
        formData.append("pet_kind", this.selectedKind)
        formData.append("pet_gender", this.selectedGender)
        formData.append("pet_age", this.petBirth)
        formData.append("pet_content", this.petContent)
        // for (let key of formData.entries()) {
        //   console.log(`${key}`)
        // }
        axios.put(`${SERVER_URL}/pet/petupdate/${this.$route.params.pet_id}`, formData
          )
          .then( (res) => {
            this.simpleShow = !this.simpleShow
          })
          .catch( (err) => {
            console.log(err)
          })
      }
    },
    deletePet: function () {
      this.yesNoShow = !this.yesNoShow
    },
    deletePetOn: function () {
      // axios
      axios.delete(`${SERVER_URL}/pet/petdelete/${this.$route.params.pet_id}`
      )
      .then( (res) => {
        this.yesNoShow = !this.yesNoShow
        this.simpleModalContents = "펫을 삭제하였습니다."
        this.simpleShow = !this.simpleShow
      })
      .catch( (err) => {
        console.log(err)
      })
      
    },
    simpleOff: function () {
      this.simpleShow = !this.simpleShow
      this.$router.push({name: "UserProfile"});
    },
    yesNoOff: function () {
      this.yesNoShow = !this.yesNoShow
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
/* 스타일은 addpet에서 */

</style>
