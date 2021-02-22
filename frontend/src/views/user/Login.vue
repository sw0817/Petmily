<template>
  <div style="font-family: GmarketSansMedium;" class="user" id="login">
    <div class="wrapC">
      <div class="login-header">
        <!-- 로고 -->
        <div class="login-logo">
          <img class="logo-image" src="@/assets/images/logo-image-cutout.png">
        </div>
        <!-- Sign up -->
        <div class="signup-and-find">
          <div class="signup-and-find-div">
            <div @click ="moveToFind" class="find-div">
              아이디(이메일) / 비밀번호 찾기
            </div>
          </div>
          <div class="signup-and-find-div">
              <div @click="moveToSignUp" class="signup-div">Sign up</div>
          </div>
        </div>
      </div>

      <div class="login-content">
        <div class="login-content-header">
          <div class="login-content-content">
            <div :class="{'login-password-show': this.intro1, 'login-password-not-show': !this.intro1}">
              <div class="notice-text">
                귀여운 동물친구들을
              </div>
            </div>
            <div>
              <div
                :class="{'login-password-show': this.intro2, 'login-password-not-show': !this.intro2}">
                <div class="notice-text">
                  만나러 가볼까요 ?
                </div>
              </div>
            </div>
          </div>
          <!-- <div class="login-image-box">
            <img class="log-pet" src="@/assets/images/puppy-img-origin.jpg" alt="">
          </div> -->
        </div>
      </div>



      <!-- 로그인 창 -->
      <div class="login-content">
        <div class="login-content-content">
          <div class="login-content-email-notice">
            <div class="login-content-email-notice-content">
              우선 이메일을 입력해주세요.
            </div>
          </div>
          <div class="login-content-email-input-div">
            <input
              class="login-content-email-input"
              v-model="email"
              v-bind:class="{error : error.email}"
              id="email"
              placeholder="example@example.com"
              type="text"
            />
          </div>
        </div>
        <div>
          <div
            class="error-text"
            v-if="error.email"
          >
            {{error.email}}
          </div>
        </div>

        
        <!-- 숨겨져 있다가 나오는 -->

        <div :class="{'login-password-show': this.show, 'login-password-not-show': !this.show}">
          <div>
            <div style="margin-top: 8vh">
              <div class="login-content-email-notice-content">비밀번호를 입력해주세요.</div>
            </div>
            <div class="">
              <input
                class="login-content-email-input"
                style="font-family: GmarketSansMedium"
                v-model="password"
                type="password"
                v-bind:class="{error : error.password, complete:!error.password&&password.length!==0}"
                id="password"
                @keyup.enter="onLogin"
                placeholder="password를 입력해주세요."
              />
            </div>
            <div class="">
              <div
                class="error-text"
                v-if="error.password"
              >
                {{error.password}}
              </div>
            </div>
          </div>
        
          <!-- 로그인 버튼 -->
          <div :class="{'login-password-show': this.buttonShow, 'login-password-not-show': !this.buttonShow}">
            <div>
              <button
                class="login-button"
                @click="onLogin"
              >어서오세요</button>
            </div>
            <div>
              <div 
                class="error-text" 
                v-if="accountError"
              >
                {{accountError}}
              </div>
            </div>
          </div>
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
  </div>
</template>

<script>
import SimpleModal from "../../components/common/SimpleModal"
import PV from "password-validator";
import * as EmailValidator from "email-validator";
import UserApi from "../../api/UserApi";
import axios from "axios";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "Login",
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
    
    this.start = "start"
    // this.checkLogin()
    const token = localStorage.getItem('jwt')
    if (token) {
      this.$store.state.accessToken = token
      this.$store.state.isUser = true
      this.$router.push("/feed/main");
    } else {
    this.getUserInfo()
    // console.log(this.userEmails)
    }
  },
  watch: {
    password: function(v) {
      this.checkForm();
    },
    email: function(v) {
      this.checkForm();
    },
    start: function(v) {
      this.startPage();
    },
  },
  methods: {
    checkForm() {
      if (this.email.length >= 0 && !EmailValidator.validate(this.email)) {
        this.error.email = "이메일 형식으로 작성해주세요.";
        this.show = false
        this.buttonShow = false
        this.accountError = false
      }
      else { this.error.email = false;
        this.show = true
      }

      if (
        this.password.length >= 0 &&
        !this.passwordSchema.validate(this.password)
      )
        { this.error.password = "영문,숫자 포함 8 자리이상이어야 합니다.";
          this.buttonShow = false
          this.accountError = false
        }
      else { this.error.password = false;
        this.buttonShow = true
      }
    },
    onLogin() {
      var emailIdx = this.userEmails.indexOf(this.email)
      // console.log(emailIdx)
      if ( emailIdx == -1 ) {
        this.accountError = "이메일 또는 비밀번호를 다시 확인해주세요."
      } else {
        if ( this.password == this.userPasswords[emailIdx] ) {
          if (this.simpleShow) {
            this.simpleOff()
          } else {
            this.accountError = false
            const userInfo = {
              user_email: this.email,
              user_password: this.password,
            }
            this.$store.dispatch("login", userInfo)
            this.simpleOn()
          }
        } else {
          this.accountError = "이메일 또는 비밀번호를 다시 확인해주세요."
        }
      }
    },
    moveToSignUp: function () {
      this.$router.push({name: 'SignUp'})
    },
    moveToFind : function(){
      this.$router.push({name: 'Find'})
    },
    startPage: function () {
      this.intro1 = false
      this.intro2 = false
      setTimeout(() => {
        this.intro1 = true
      }, 1000);
      setTimeout(() => {
        this.intro2 = true
      }, 2000);
    },
    getUserInfo: function () {
      axios.get(`${SERVER_URL}/member/getallmember`)
      .then(({data}) => {
        for (let obj of data) {
          this.userEmails.push(obj.user_email)
          this.userPasswords.push(obj.user_password)
        }
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    simpleOn: function () {
      this.simpleShow = !this.simpleShow
    },
    simpleOff: function () {
      this.simpleShow = !this.simpleShow
      this.$router.push({ name: "FeedMain" });
    },
  },
  data: () => {
    return {
      show: false,
      buttonShow: false,
      email: "",
      password: "",
      passwordSchema: new PV(),
      error: {
        email: false,
        passowrd: false
      },
      isSubmit: false,
      component: this,
      userEmails: [],
      userPasswords: [],
      accountError: false,
      intro1: false,
      intro2: false,
      start: "",
      loginStatus: false,
      simpleShow: false,
      simpleModalContents: "로그인을 완료하였습니다.",
    };
  },
  computed: {
    computedLoginStyle() {
      return {
        display: this.show.display
      }
    }
  },
};
</script>

<style>
@font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}


.wrapC {
  display: flex;
  flex-direction: column;
  background-color: white;
  padding: 0 4vw;
  min-height: 100vh;
}

.login-header {
  display: flex;
  justify-content: space-between;
  margin-top: 3vh;
  margin-bottom: 4vh;
}

.login-logo {
  width: 40%;
}

.signup-and-find {
  display: flex;
  flex-direction: column;
  width: 50%;
}

.signup-and-find-div {
  text-align: end;
  white-space: pre;
}

.find-div {
  font-size: 1.5vw;
  color: orange;
  cursor: pointer;
  white-space: pre;
}

.signup-div {
  font-size: 5vw;
  color: RoyalBlue;
  cursor: pointer;
}

.login-image-box {
  width: 25vw;
  height: 15vw;
}

.log-pet {
  float: left;
  width: 100%;
  height: 100%;
  border-radius: 70%;
  text-align: center;
}

.login-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  margin-top: 5vh;
  padding: 0 15vw;
  /* align-items: center; */
}

.login-content-header {
  display: flex;
}

.login-password-show {
  transition: opacity 1s;
}

.login-password-not-show {
  opacity: 0;
}

.login-button {
  width: 100%;
  margin-top: 12vh;
  background-color: white;
  color: mediumslateblue;
  border: 0.3vw solid;
  border-color: mediumslateblue;
  font-weight: bold;
  font-size: 2vw;
  padding: 0.5vw;
  border-radius: 1vw;
  transition-duration: 0.5s;
}

.login-button:hover {
  background-color: mediumslateblue;
  color: white;
}

.login-content-email-notice-content {
  font-size: 1.5vw;
  color: orange;
}

.login-content-email-input {
  border: none;
  border-bottom: 1px solid black;
  width: 100%;
  font-size: 2.5vw;
}

.login-content-email-input:focus {
  outline: none;
}

.error-text {
  font-size: 1.5vw;
  margin-top: 0.5vh;
  color: red;
}

.logo-image {
  width: 70%;
}

.notice-text {
  font-size: 4vw;
}

@media ( max-width: 540px) {

  .login-content {
    padding: 0 2vw;
  }

  .notice-text {
    font-size: 7vw;
  }

  .login-button {
    margin-top: 8vh;
    font-size: 5vw;
    padding: 0.5vw;
    border-radius: 3vw;
    transition-duration: 0.5s;
  }

  .login-button:hover {
    background-color: mediumslateblue;
    color: white;
  }

  .login-content-email-notice-content {
    font-size: 2.5w;
    color: orange;
  }

  .login-content-email-input {
    border: none;
    border-bottom: 1px solid black;
    width: 100%;
    font-size: 5vw;
  }

  .login-content-email-input:focus {
    outline: none;
  }

  .error-text {
    font-size: 2.5vw;
    margin-top: 0.5vh;
    color: red;
  }

}

</style>
