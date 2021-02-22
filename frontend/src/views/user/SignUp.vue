<template>
  <div style="font-family: GmarketSansMedium;" class="user" id="signUp">
    <div class="wrapC">
      <div class="user-modify-container" style="min-height:0vh;">
        <div style="margin-top: 5vh;" class="signup-logo-btn">
          <img :class="{'login-password-show': this.intro0, 'login-password-not-show': !this.intro0}" style="width: 40%" src="@/assets/images/logo-image-cutout.png" @click="moveToLogin">
        </div>
        <div class="user-modify-email-div" style="margin-top: 5vh;">
          <div :class="{'login-password-show': this.intro1, 'login-password-not-show': !this.intro1}">
            <div class="user-signup-notice">
              아직 PetMily 회원이
            </div>
          </div>
          <div :class="{'login-password-show': this.intro2, 'login-password-not-show': !this.intro2}">
            <div class="user-signup-notice">
              아니시군요 ?
            </div>
          </div>
        </div>
        
      </div>

      <div :class="{'login-password-show': this.intro3, 'login-password-not-show': !this.intro3}">
      <!-- 이메일 -->
        <div class="user-modify-container">
          <div class="user-modify-email-div">
            <div class="user-modify-email">
              이메일을 입력해주세요.
            </div>
            <div class="user-modify-flex">
              <input
                class="user-modify-nickname-input"
                v-model="user_email"
                v-bind:class="{error : error.user_email}"
                id="user_email"
                placeholder="example@example.com"
                type="text"
              />
              <button
                v-if="error.user_email || user_email.length == 0"
                class="modify-certificationBtn-off"
              >
                이메일 발송
              </button>
              <div
                v-else
                @click="clickEmailCheck"
                class="modify-certificationBtn"
              >
                이메일 발송
              </div>
            </div>
            <div class="modify-error-text" v-if="error.user_email">{{error.user_email}}</div>
            <div class="text-modal" v-if="simpleShow">
              <div class="text-modal-wrapper">
                <SimpleModal
                  :contents="simpleModalContents"
                  @modalOff="simpleOff"
                />
              </div>
            </div>
          </div>

          <!--  -->

          <div class="user-modify-email-div" v-if="showcertify">
            <div class="user-modify-email">
              인증코드
            </div>
            <div class="user-modify-flex">
              <input
                class="user-modify-nickname-input"
                v-model="user_certifiedkey"
                type="text"
                id="user_certifiedkey"
                maxlength="11"
                placeholder="인증코드를 입력해주세요. (11자)"
              />
              <div
                @click="isCorreckCertifiedKey"
                class="modify-certificationBtn"
              >
                인증하기
              </div>
            </div>
            <div class="modify-error-text" v-if="error.certified">{{error.certified}}</div>
            <div class="text-modal" v-if="simpleShow">
              <div class="text-modal-wrapper">
                <SimpleModal
                  :contents="simpleModalContents"
                  @modalOff="simpleOff"
                />
              </div>
            </div>
          </div>
          
          <!-- 닉네임 -->

          <div class="user-modify-email-div">
            <div class="user-modify-email">
              닉네임은 한글, 영문, 숫자만 입력 가능합니다.
            </div>
            <input
              class="user-modify-name-input"
              v-model="user_nickname"
              maxlength="4"
              type="text"
              id="user_nickname"
              placeholder="닉네임을 입력해주세요. (2~4자)"
            />
            <div class="modify-error-text" v-if="error.user_nickname">{{error.user_nickname}}</div>
          </div>

          <!-- 비밀번호 -->

          <div class="user-modify-email-div">
            <div class="user-modify-email">
              비밀번호를 입력해주세요.
            </div>
            <input
              class="user-modify-name-input"
              style="font-family: GmarketSansMedium;"
              v-model="user_password"
              type="password"
              v-bind:class="{error : error.user_password, complete:!error.user_password&&user_password.length!==0}"
              id="user_password"
            />
            <div class="modify-error-text" v-if="error.user_password">{{error.user_password}}</div>
          </div>

          <!-- 비밀번호 확인 -->

          <div class="user-modify-email-div">
            <div class="user-modify-email">
              비밀번호를 한 번 더 입력해주세요.
            </div>
            <input
              class="user-modify-name-input"
              style="font-family: GmarketSansMedium;"
              v-model="passwordConfirmation"
              type="password"
              id="passwordConfirmation"
            />
            <div class="modify-error-text" v-if="error.user_passwordConfirmation">{{error.user_passwordConfirmation}}</div>
          </div>

          <!-- 이름 확인 -->

          <div class="user-modify-email-div">
            <div class="user-modify-email">
              이름을 입력해주세요.
            </div>
            <input
              class="user-modify-name-input"
              v-model="user_name"
              type="text"
              id="user_name"
              maxlength="10"
              placeholder="이름을 입력해주세요. (2~10자)"
            />
            <div class="modify-error-text" v-if="error.user_name">
              {{ error.user_name }}
            </div>
          </div>

          <!-- 생년월일 -->

          <div class="user-modify-email-div">
            <div class="user-modify-email">
              생년월일을 입력해주세요. (0000.00.00)
            </div>
            <div class="user-modify-flex">
              <input
                class="user-modify-year"
                v-model="year"
                type="text"
                id="year"
                maxlength="4"
                placeholder="연도 (4자)"
              />
              <select
                v-model="selectedMonth"
                class="user-modify-month">
                <option selected disabled value="">월</option>
                <option value="01">1</option>
                <option value="02">2</option>
                <option value="03">3</option>
                <option value="04">4</option>
                <option value="05">5</option>
                <option value="06">6</option>
                <option value="07">7</option>
                <option value="08">8</option>
                <option value="09">9</option>
                <option value="10">10</option>
                <option value="11">11</option>
                <option value="12">12</option>
              </select>
              <input
                class="user-modify-day"
                v-model="date"
                type="text"
                id="date"
                maxlength="2"
                placeholder="일"
              />
            </div>
            <div class="modify-error-text-date" v-if="error.year">
              {{ error.year }}
            </div>
            <div class="modify-error-text-date" v-if="error.month">
              {{ error.month }}
            </div>
            <div class="modify-error-text-date" v-if="error.date">
              {{ error.date }}
            </div>
          </div>

          <!-- 성별 -->

          <div class="user-modify-email-div">
            <div class="user-modify-email">
              성별을 선택해주세요.
            </div>
            <div class="user-modify-btn">
              <button
                :class="{modifygenderButtonOn: maleButton, modifygenderButtonOff: !maleButton}"
                @click="clickMale"
              >
                남자
              </button>
              <button
                :class="{modifygenderButtonOn: femaleButton, modifygenderButtonOff: !femaleButton}"
                @click="clickFemale"
              >
                여자
              </button>
            </div>
            <div class="modify-error-text" v-if="error.user_gender">
              {{ error.user_gender }}
            </div>
          </div>

          <div class="user-modify-email-div">
            <div class="user-modify-email">
              간단한 한줄소개를 작성해주세요.
            </div>
            <input
              class="user-modify-name-input"
              v-model="user_content"
              type="text"
              id="user_content"
              maxlength="30"
              placeholder="한줄소개를 작성해주세요."
            />
          </div>

          <!-- 회원가입 버튼 -->
          <div :class="{'login-password-show': this.buttonShow && certified == 'Y', 'login-password-not-show': !this.buttonShow || certified != 'Y'}">
            <button
              class="user-sign-confirm-btn"
              @click="clickSignup"
            >
              회원가입하기
            </button>
          </div>
          <div class="text-modal" v-if="simpleShow">
            <div class="text-modal-wrapper">
              <SimpleModal
                :contents="simpleModalContents"
                @modalOff="simpleOff2"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import PV from "password-validator";
import * as EmailValidator from "email-validator";
import SimpleModal from "../../components/common/SimpleModal"
import axios from "axios";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;
// import UserApi from "../../api/UserApi";

export default {
  name: "SignUp",
  components: {
    SimpleModal,
  },
  created() {
    this.component = this;

    this.passwordSchema
      .is()
      .min(8)
      .is()
      .max(20)
      .has()
      .digits()
      .has()
      .letters();
    
    this.startPage()
    
    this.getUserInfo()
  },
  watch: {
    user_password: function(v) {
      this.checkPassword();
    },
    user_email: function(v) {
      this.checkEmail();
    },
    user_nickname: function(v) {
      this.checkNickname();
    },
    passwordConfirmation: function(v) {
      this.checkPasswordConfirmation();
    },
    user_name: function(v) {
      this.checkName();
    },
    date: function(v) {
      this.checkDate();
    },
    year: function(v) {
      this.checkYear();
    },
    selectedMonth: function(v) {
      this.checkMonth();
    },
    user_gender: function(v) {
      this.checkGender();
    },
  },
  methods: {
    moveToLogin: function () {
      this.$router.push({ name: 'Login'})
    },
    checkEmail() {
      if (this.user_email.length >= 0 && !EmailValidator.validate(this.user_email)) {
        this.error.user_email = "이메일 형식으로 작성해주세요.";
        this.buttonShow = false
      }
      else if (this.userEmails.includes(this.user_email)) { 
        this.error.user_email = "이미 가입한 이메일입니다."
        this.buttonShow = false
      } else { this.error.user_email = false;
        this.checkForm()
      }
    },

    checkPassword() {
      if (
        this.user_password.length >= 0 &&
        !this.passwordSchema.validate(this.user_password)
      )
        { this.error.user_password = "영문,숫자 포함 8 자리이상이어야 합니다.";
          this.buttonShow = false
        }
      else { this.error.user_password = false;
      this.checkForm()
      }
    },

    checkPasswordConfirmation() {
      if (
        this.user_password != this.passwordConfirmation
      )
        { this.error.user_passwordConfirmation = "비밀번호가 일치하지 않습니다.";
          this.buttonShow = false
        }
      else { this.error.user_passwordConfirmation = false;
      this.checkForm()
      }
    },

    checkNickname() {
      if ( 10 < this.user_nickname.length || this.user_nickname.length < 2 || this.patternNickName.test(this.user_nickname) ) {
        this.error.user_nickname = "부적합한 닉네임입니다."
        this.buttonShow = false
      } 
     else if (this.userNicknames.includes(this.user_nickname)) { 
      this.error.user_nickname = "이미 가입한 닉네임입니다."
      this.buttonShow = false
      } else { this.error.user_nickname = false;
        this.checkForm() 
      }
    },

    checkName() {
      if ( this.patternName.test(this.user_name) || this.user_name.length < 2 || 10 < this.user_name.length ) {
        this.error.user_name = "정확한 이름을 입력해주세요."
        this.buttonShow = false
      } else {
        this.error.user_name = false
        this.checkForm()
      }
    },

    checkDate() {
      if ( this.patternNum.test(this.date) || this.date == "00" || this.date == "0" || 31 < Number(this.date) || this.date.length == 0 ) {
        this.error.date = "태어난 날짜를 확인해주세요."
        this.buttonShow = false
      } else if ( this.date.length == 1 && this.date.length != "0" ) {
        this.error.date = "날짜를 두 자리수로 입력해주세요. (ex : 01)"
        this.buttonShow = false 
      } else if ( this.selectedMonth == "2" && 29 < Number(this.date) || this.selectedMonth == "4" && 30 < Number(this.date) || this.selectedMonth == "6" && 30 < Number(this.date) || this.selectedMonth == "9" && 30 < Number(this.date) || this.selectedMonth == "11" && 30 < Number(this.date) ) {
        this.error.date = "태어난 날짜를 확인해주세요."
        this.buttonShow = false
      } else {
        this.error.date = false
        this.checkForm()
      }
    },

    checkMonth() {
      if ( !this.selectedMonth ) {
        this.error.month = "태어난 월을 선택해주세요."
        this.buttonShow = false
      } else {
        this.error.month = false
        this.checkForm()
      }
    },

    checkYear() {
      if ( this.patternNum.test(this.year) || !this.year.length == 4 || 2020 < Number(this.year) ) {
        this.error.year = "태어난 년도 4자리를 정확하게 입력해주세요."
        this.buttonShow = false
      } else if ( this.year.length == 4 && Number(this.year) < 1900 ) {
        this.error.year = "정말이세요 ?"
        this.buttonShow = false
      } else {
        this.error.year = false
        this.checkForm()
      }
    },

    checkGender() {
      if ( !this.user_gender ) {
        this.error.user_gender = "성별을 선택해주세요."
        this.buttonShow = false
      } else {
        this.error.user_gender = false
        this.checkForm()
      }
    },

    checkForm() {
      if ( this.error.user_nickname || this.error.certified || !this.user_gender || this.error.date || this.error.month || this.error.year || this.error.user_name || this.error.user_email || this.error.user_password || this.passwordConfirmation.length < 8 ) {
        this.buttonShow = false
      } else {
        this.buttonShow = true
      }
    },

    startPage: function () {
      this.intro0 = false
      this.intro1 = false
      this.intro2 = false
      this.intro3 = false
      setTimeout(() => {
        this.intro0 = true
      }, 1);
      setTimeout(() => {
        this.intro1 = true
      }, 1000);
      setTimeout(() => {
        this.intro2 = true
      }, 2000);
      setTimeout(() => {
        this.intro3 = true
      }, 3000);
    },
    clickFemale: function () {
      this.femaleButton = true
      this.maleButton = false
      this.user_gender = "W"
    },
    clickMale: function () {
      this.femaleButton = false
      this.maleButton = true
      this.user_gender = "M"
    },

    //이메일 인증 체크하기
    //스프링으로 입력한 user_email과 생성된 certified를 보낼거임!
    clickEmailCheck: function(){
      this.simpleShow=!this.simpleShow
      if(this.user_email.length==0){
        this.simpleModalContents="이메일을 입력해주세요."
      }else{
        this.simpleModalContents="잠시만 기다려주세요."
        this.showcertify=true;
        this.certified=Math.random().toString(36).substr(2,11); //인증코드를 만들고!
        axios.post(`${SERVER_URL}/member/emailsend/${this.user_email}/${this.certified}/0`)
        .then(({data}) => {
          this.simpleShow=true;
          this.simpleModalContents=this.user_email+" (으)로 이메일을 발송하였습니다!"

        })
        .catch( (err) => {
          console.log(err)
        })
      }
    },
    simpleOff: function () {
      this.simpleShow=false
    },
    simpleOff2: function () {
      this.moveMain();
      this.simpleShow=false
    },
    isCorreckCertifiedKey: function(){
      if(this.user_certifiedkey===this.certified){
        this.simpleShow=true;
        this.simpleModalContents="인증코드가 일치합니다!"
        this.certified="Y" //인증을 완료했으니 certified를 Y로하고 데이터베이스에 전송
        this.error.certified = false
      }else{
        this.error.certified = "인증코드를 확인하세요!"
      }
    },
    clickSignup: function () {
      this.user_birth = this.year + this.selectedMonth + this.date
      // 이후로 로그인 요청 추가하면 됨.
      const credentials = {
        user_email: this.user_email,
        user_nickname: this.user_nickname,
        user_password: this.user_password,
        user_name: this.user_name,
        user_birth: this.user_birth,
        user_gender: this.user_gender,
        certified: this.certified,
        user_content: this.user_content,
      }

      axios.post(`${SERVER_URL}/member/signup`, credentials)
      .then( (res) => {
        let msg = null;
          if(this.buttonShow){ //모든 form이 입력되었으면
            msg="회원가입이 완료되었습니다."


          }else{
            msg ="회원가입 시 문제가 발생했습니다."
          }
          this.simpleShow=true;
          this.simpleModalContents=msg;

      })
      .catch( (err ) => {
        console.log(err)
        const msg = "회원가입 시 문제가 발생했습니다."
        alert(msg)
      });
    },
    moveMain(){
      if(this.simpleModalContents=="회원가입이 완료되었습니다.")
        this.$router.push({ name: 'Login'})
    },
    getUserInfo: function () {
      axios.get(`${SERVER_URL}/member/getallmember`)
      .then(({data}) => {
        for (let obj of data) {
          this.userEmails.push(obj.user_email)
          this.userNicknames.push(obj.user_nickname)
        }
      })
      .catch( (err) => {
        console.log(err)
      })
    },
  },
  data: () => {
    return {
      show: false,
      buttonShow: false,
      user_email: "",
      user_password: "",
      passwordConfirmation: "",
      passwordSchema: new PV(),
      user_name: "",
      maleButton: false,
      femaleButton: false,
      user_gender: false,
      user_birth: false,
      user_content:"",
      year: "",
      date: "",
      certified:"!@#$%^&**(!@#", //뷰에서 자동발급되는 인증코드(얘는 변수이름 변경하면 안돼용)
      user_certifiedkey:"", //회원이 직접 입력하는 인증코드
      showcertify:false, //이메일전송 버튼 클릭하고나면 인증코드 입력창 나오기
      error: {
        user_email: false,
        user_password: false,
        user_nickname: false,
        user_name: false,
        user_birth: false,
        year: false,
        month: false,
        date: false,
        user_gender: false,
        user_passwordConfirmation: false,
        certified: false,
      },
      isSubmit: false,
      component: this,
      simpleModalContents:"",
      simpleShow:false,
      user_nickname: "",
      // 영어, 숫자만 되게 고쳐볼게요
      patternNickName: /[^0-9a-zA-Z가-힣]/,
      // patternNickName: /[^0-9a-zA-Z가-힣]/,
      patternName: /[^a-zA-Z가-힣]/,
      patternNum: /[^0-9]/,
      // 임시 계정
      userEmails: [],
      userNicknames: [],
      userInfo: [],

      // 월 선택
      selectedMonth: null,
      monthOptions: [
        { value: null, text: "월" },
        { value: "01", text: "1" },
        { value: "02", text: "2" },
        { value: "03", text: "3" },
        { value: "04", text: "4" },
        { value: "05", text: "5" },
        { value: "06", text: "6" },
        { value: "07", text: "7" },
        { value: "08", text: "8" },
        { value: "09", text: "9" },
        { value: "10", text: "10" },
        { value: "11", text: "11" },
        { value: "12", text: "12" },
      ],
      intro0: false,
      intro1: false,
      intro2: false,
      intro3: false,
    };
  },
  computed: {
    computedLoginStyle() {
      return {
        display: this.show.display
      }
    }
  }
};
</script>

<style>
@font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
.signup-logo-btn {
  margin-top: 5vh;
  display: flex;
  justify-content: center;
}

.signup-logo-btn:hover {
  cursor: pointer;
}

.user-signup-notice {
  font-size: 4vw;
  /* margin-top: 5vh; */
}

.login-password-show {
  transition: opacity 1s;
}

.login-password-not-show {
  opacity: 0;
}

.logo-image {
  width: 100%;
}

.sign-button {
  width: 100%;
  margin-top: 12vh;
  background-color: white;
  color:mediumslateblue;
  border: 0.3vw solid;
  border-color:mediumslateblue;
  font-weight: bold;
  font-size: 2vw;
  padding: 0.5vw;
  border-radius: 1vw;
  transition-duration: 0.5s;
}

.sign-button:hover {
  background-color:mediumslateblue;
  color: white;
}

.genderButtonOn {
  width: 100%;
  margin-top: 2vh;
  background-color:mediumslateblue;
  color: white;
  border: 0.3vw solid;
  border-color:mediumslateblue;
  font-weight: bold;
  font-size: 2vw;
  padding: 0.5vw;
  border-radius: 1vw;
  transition-duration: 0.5s;
}

.genderButtonOff {
  width: 100%;
  margin-top: 2vh;
  background-color: white;
  color:mediumslateblue;
  border: 0.3vw solid;
  border-color:mediumslateblue;
  font-weight: bold;
  font-size: 2vw;
  padding: 0.5vw;
  border-radius: 1vw;
  transition-duration: 0.5s;
}

.genderButtonOff:hover {
  background-color:mediumslateblue;
  color: white;
}

.certificationBtn {
  width: 95%;
  padding: 0.5vw;
  border-radius: 1vw;
  border: 0.3vw solid;
  border-color: mediumslateblue;
  background-color: white;
  color:mediumslateblue;
  font-size: 1.3vw;
  font-weight: bold;
  transition-duration: 0.5s;
}

.certificationBtn:hover {
  background-color:mediumslateblue;
  color:white;
}

.email-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.email-input {
  /* width: 66.7%; */
  width: 50%;
  padding-right: 0.75vw;
  padding-left: 0.75vw;
}

.email-btn {
  /* width: 25%; */
  width: 16.7%;
  padding-right: 0.75vw;
  padding-left: 0.75vw;
}

.user-sign-confirm-btn {
  width: 64vw;
  margin: 0 1vw;
  margin-top: 8vh;
  margin-bottom: 8vh;
  background-color: white;
  color:mediumslateblue;
  border: 0.3vw solid;
  border-color:mediumslateblue;
  font-weight: bold;
  font-size: 2vw;
  padding: 0.5vw;
  border-radius: 1vw;
  transition-duration: 0.5s;
}

.user-sign-confirm-btn:hover {
  background-color:mediumslateblue;
  color: white;
}

@media ( max-width: 576px) {

  .user-sign-confirm-btn {
    width: 90vw;
    margin: 0 1vw;
    margin-top: 6vh;
    background-color: white;
    color:mediumslateblue;
    border: 0.3vw solid;
    border-color:mediumslateblue;
    font-weight: bold;
    font-size: 4vw;
    padding: 0.5vw;
    border-radius: 1vw;
    transition-duration: 0.5s;
  }

  .email-input {
    width: 66.7%;
    padding-right: 0.75vw;
    padding-left: 0.75vw;
  }

  .email-btn {
    width: 25%;
    padding-right: 0.75vw;
    padding-left: 0.75vw;
  }

}

</style>
