<template>
  <div style="font-family: GmarketSansMedium;">
    <div class="find-container">
      <div @click="goLogin">
        <img class="logoImg" src="@/assets/images/logo-image-cutout.png" alt="">
      </div>
      <div class="find-id-container">
        <div class="find-id-title">
          아이디 찾기
        </div>
        <div class="find-id-content-container">
          <div class="find-id-id">
            <div class="find-id-id-label">
              이름 :
            </div>
            <div class="find-id-id-input">
              <input
                id="1"
                type="text"
                v-model="user_name"
                placeholder="가입한 계정 이름을 입력해주세요."
              >
            </div>
          </div>
          <!--  -->
          <div class="find-id-birth">
            <div class="find-id-birth-label">
              생년월일 (YYYYMMDD) :
            </div>
            <div class="find-id-birth-input">
              <input
                id="2"
                type="text"
                v-model="user_birth"
                maxlength="8"
                placeholder="생년월일 8자리를 입력해주세요."
              >
            </div>
          </div>
        </div>
        <div class="find-id-btn-container">
          <button class="find-id-btn" @click="findId">
            아이디 찾기
          </button>
          <div v-if="error.findId" class="error-message">
            {{ error.findId }}
          </div>
          <div v-else-if="findedEmail" class="finded-id">
            회원님의 아이디(이메일)은 ' {{ findedEmail }} ' 입니다.
          </div>
        </div>
      </div>

      <hr>
      <!-- 비밀번호 찾기 -->

      <div class="find-id-container">
        <div class="find-id-title">
          비밀번호 찾기
        </div>
        <div class="find-id-content-container">
          <div class="find-id-id">
            <div class="find-id-id-label">
              이름 :
            </div>
            <div class="find-id-id-input2">
              <input
                id="3"
                type="text"
                v-model="user_name2"
                placeholder="가입한 계정 이름을 입력해주세요."
              >
            </div>
          </div>
          <!--  -->
          <div class="find-id-birth">
            <div class="find-id-birth-label">
              아이디 (가입한 이메일) :
            </div>
            <div class="find-id-birth-input">
              <input
                id="4"
                type="text"
                v-model="user_email"
                placeholder="가입한 이메일을 정확히 입력해주세요."
              >
            </div>
          </div>
        </div>
        <div class="find-id-btn-container">
          <button class="find-id-btn" @click="clickEmailCheck">
            이메일 발송
          </button>
          <div v-if="error.findPassword" class="error-message">
            {{ error.findPassword }}
          </div>
          <div v-else-if="sendedEmail && changeEmail" class="sended-email">
            {{ user_email }} (으)로 이메일을 발송하였습니다!
          </div>
        </div>
        <!-- 인증코드 -->
        <div v-if="sendedEmail">
          <div class="find-password-certify-container">
            <div class="find-password-certify">
              <div class="find-password-certify-label">
                인증코드
              </div>
              <div class="find-password-certify-input">
                <input
                  id="5"
                  type="text"
                  maxlength="11"
                  v-model="user_certifiedkey"
                  placeholder="인증코드 (11자리)"
                >
              </div>
            </div>
          </div>
          <div class="find-id-btn-container">
            <button class="find-id-btn" @click="isCorreckCertifiedKey">
              인증하기
            </button>
            <div v-if="error.certifiedKey" class="error-message">
              {{ error.certifiedKey }}
            </div>
            <div v-else-if="getReady" class="sended-email">
              인증코드가 확인되었습니다.
            </div>
          </div>
        </div>
      </div>
      <hr>
      <!-- 비밀번호 변경 -->
      <div v-if="getReady" class="find-id-container">
        <div class="find-id-title">
          비밀번호 변경하기
        </div>
        <div class="find-id-content-container">
          <div class="find-id-id">
            <div class="find-id-id-label">
              변경할 비밀번호를 입력해주세요.
            </div>
            <div class="find-id-id-input3">
              <input
                id="6"
                type="password"
                v-model="user_password"
                placeholder="password를 입력해주세요. (문자+숫자 8자 이상)"
              >
            </div>
          </div>
          <!--  -->
          <div class="find-id-birth">
            <div class="find-id-birth-label">
              한 번 더 입력해주세요.
            </div>
            <div class="find-id-birth-input">
              <input
                id="7"
                type="password"
                v-model="passwordConfirmation"
                placeholder="password를 한 번 더 입력해주세요."
              >
            </div>
          </div>
        </div>
        <div class="find-id-btn-container">
          <button class="find-id-btn" @click="changePassword">
            비밀번호 변경
          </button>
          <div v-if="error.passwordConfirmation" class="error-message">
            {{ error.passwordConfirmation }}
          </div>
          <div v-else-if="changePasswordDone" >
            <div class="sended-email">
              비밀번호가 변경되었습니다!
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name : "Find",
  components: {
  },
  data:function(){
    return{
      user_name: "",
      user_name2: "",
      user_birth: "",
      user_email: "",
      certified: "",
      user_certifiedkey: "",
      user_password: "",
      passwordConfirmation: "",
      alertMsg: "",
      findedEmail: false,
      sendedEmail: false,
      changeEmail: false,
      getReady: false,
      changePasswordDone: false,
      error: {
        findId: false,
        certifiedKey: false,
        passwordConfirmation: false,
        findPassword: false,
      },
      patternName: /[^a-zA-Z가-힣]/,
      patternNum: /[^0-9]/,
    }
  },
  methods: {
    findId: function () {
      if (this.patternName.test(this.user_name) || this.user_name.length < 1) {
        this.error.findId = "정확한 계정 이름을 입력해주세요."
        this.findedEmail = false
      } else if (this.patternNum.test(this.user_birth) || this.user_birth.length < 8) {
        this.error.findId = "생년월일 8자리를 입력해주세요."
        this.findedEmail = false
      } else {
        // const info = {
        //   user_name: this.user_name,
        //   user_birth: this.user_birth,
        // }
        const info = new FormData
        info.append("user_name", this.user_name)
        info.append("user_birth", this.user_birth)
        axios.post(`${SERVER_URL}/member/findId`, info)
        .then(({data}) => {
          // console.log({data})
          // console.log(data[0].user_email)
          this.error.findId = false
          this.findedEmail = data[0].user_email
        })
        .catch( (err) => {
          console.log(err)
          this.findedEmail = false
          this.error.findId = "계정 이름과 생년월일을 확인해주세요."
        })
      }
      
    },
    certifiedKey: function () { 
      this.certified = Math.random().toString(36).substr(2,11);
    },
    //이메일 인증 체크하기
    //스프링으로 입력한 user_email과 생성된 certified를 보낼거임!
    clickEmailCheck: function () {
      if (this.patternName.test(this.user_name2) || this.user_name2.length < 1) {
        this.error.findPassword = "정확한 계정 이름을 입력해주세요."
        this.sendedEmail = false
      } else if (this.user_email.length < 1) {
        this.error.findPassword = "가입한 이메일을 입력해주세요."
        this.sendedEmail = false
      } else {
        this.certifiedKey(); //인증코드를 만들고!
        const credentials = {
          user_email: this.user_email,
          certified: this.certified,
        }
        axios.post(`${SERVER_URL}/member/emailsend/${this.user_email}/${this.certified}/1`)
        .then(({data}) => {
          // console.log(data)
          this.error.findPassword = false
          this.sendedEmail = true
          this.changeEmail = true
        })
        .catch( (err) => {
          console.log(err)
          this.sendedEmail = false
          this.error.findPassword = "계정 이름과 이메일을 확인해주세요."
        })
      }
      
    },
    isCorreckCertifiedKey: function () {
      // console.log("발급코드 : " + this.certified + " || 입력한코드 : " + this.user_certifiedkey)
      if (this.user_certifiedkey === this.certified) {
        this.getReady = true
        this.error.certifiedKey = false
      } else {
        this.getReady = false
        this.error.certifiedKey = "인증코드를 확인해주세요."
      }
    },
  //비밀번호 변경 -> 비밀번호 확인 과정까지 마쳤다고 가정하고... 데이터 전송부분만 만들었음
    changePassword: function () {
      if (this.user_password != this.passwordConfirmation || this.user_password.length < 8 || this.passwordConfirmation.length < 8) {
        this.error.passwordConfirmation = "비밀번호를 확인해주세요."
        this.changePasswordDone = false
      } else {
        axios.post(`${SERVER_URL}/member/changepw/${this.user_email}/${this.user_password}`)
        .then(({data}) => {
          this.error.passwordConfirmation = false
          this.changePasswordDone = true
          setTimeout(() => {
            this.$router.push({ name: "Login" })
          }, 3000);
        })
        .catch( (err) => {
          this.error.passwordConfirmation = "비밀번호를 확인해주세요."
          this.changePasswordDone = false
          console.log(err)
        })
      }
    },
    goLogin: function () {
      this.$router.push({name: 'Login'})
    },
  },
  watch: {
    user_email: function () {
      this.changeEmail = false
    }
  }
}
</script>

<style>
@font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
.find-container {
  width: 90%;
  margin-left: 5%;
  margin-right: 5%;
  margin-bottom: 10vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.logoImg {
  margin-top: 5vh;
  width: 60%;
}

.logoImg:hover {
  cursor: pointer;
}

.find-id-container {
  margin-top: 4vh;
  width: 80%;
}

.find-id-title {
  font-size: 3vw;
}

.find-id-content-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 3vh;
}

.find-id-id {
  width: 90%;
}

.find-id-id-label {
  font-size: 1.5vw;
  font-weight: bold;
  color: orange;
}

.find-id-id-input {
  width: 100%;
  font-size: 1.5vw;
  font-weight: bold;
  border: none;
  border-bottom: 0.2vw solid black;
  margin-top: 1vh;
}

.find-id-id-input input {
  width: 100%;
}

.find-id-id-input2 {
  width: 100%;
  font-size: 1.5vw;
  font-weight: bold;
  border: none;
  border-bottom: 0.2vw solid black;
  margin-top: 1vh;
}

.find-id-id-input2 input {
  width: 100%;
}

.find-id-id-input3 {
  width: 100%;
  font-size: 1.5vw;
  font-weight: bold;
  border: none;
  border-bottom: 0.2vw solid black;
  margin-top: 1vh;
}

.find-id-id-input3 input {
  width: 100%;
  font-family: GmarketSansMedium;
}

.find-id-birth {
  width: 90%;
  margin-top: 1vh;
}

.find-id-birth-label {
  font-size: 1.5vw;
  font-weight: bold;
  color: orange;
}

.find-id-birth-input {
  width: 100%;
  font-size: 1.5vw;
  font-weight: bold;
  border: none;
  border-bottom: 0.2vw solid black;
  margin-top: 1vh;
}

.find-id-birth-input input {
  width: 100%;
  font-family: GmarketSansMedium;
}

.find-id-btn-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 3vh;
}

.find-id-btn {
  width: 90%;
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

.find-id-btn:hover {
  background-color:mediumslateblue;
  color:white;
}

.finded-id {
  margin-top: 0.5vh;
  font-size: 2vw;
  font-weight: bold;
  color: green;
}

.error-message {
  margin-top: 0.5vh;
  font-size: 2vw;
  font-weight: bold;
  color: red;
}

.sended-email {
  margin-top: 0.5vh;
  font-size: 2vw;
  font-weight: bold;
  color: green;
}

.find-password-certify {
  width: 90%;
  margin-top: 1vh;
}

.find-password-certify-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 4vh;
  transition-duration: 0.5;
}

.find-password-certify-label {
  font-size: 1.5vw;
  font-weight: bold;
  color: orange;
}

.find-password-certify-input {
  width: 100%;
  font-size: 1.5vw;
  font-weight: bold;
  border: none;
  border-bottom: 0.2vw solid black;
  margin-top: 1vh;
}

.find-password-certify-input input {
  width: 100%;
}

</style>
