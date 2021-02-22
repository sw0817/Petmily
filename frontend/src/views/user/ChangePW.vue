<template>
  <div style="font-family: GmarketSansMedium;" class="user" id="changepw">
    <Navbar
      style="position:sticky; top:0; z-index:3; width: 100%;"
    >
    </Navbar>
    <div class="wrapC">
      <div class="user-modify-container" style="min-height:0vh;">
        <div class="user-modify-email-div">
          <div class="user-modify-email">
            아이디 (내가 가입한 이메일)
          </div>
          <div class="user-modify-email-input">
            {{user.user_email}}
          </div>
        </div>
        <!-- 이전 비밀번호 -->
        <div class="user-modify-email-div">
          <div class="user-modify-email">
            기존 비밀번호를 입력해주세요.
          </div>
          <input
            class="user-modify-name-input"
            style="font-family: GmarketSansMedium;"
            v-model="nowPassword"
            type="password"
            v-bind:class="{error : error.nowPassword, complete:!error.nowPassword&&nowPassword.length!==0}"
            id="nowPassword"
            placeholder="password를 입력해주세요."
          />
          <div class="modify-error-text" v-show="error.nowPassword">{{error.nowPassword}}</div>
        </div>

        <!-- 새로운 비밀번호 -->
        <div class="user-modify-email-div">
          <div class="user-modify-email">
            새로운 비밀번호를 입력해주세요.
          </div>
          <input
            class="user-modify-name-input"
            style="font-family: GmarketSansMedium;"
            v-model="user_password"
            type="password"
            v-bind:class="{error : error.user_password, complete:!error.user_password&&user_password.length!==0}"
            id="user_password"
            placeholder="password를 입력해주세요."
          />
          <div class="modify-error-text" style="font-size: 1vw; margin-top: 0.2vh; color: red;" v-show="error.user_password">{{error.user_password}}</div>
        </div>


        <!-- 비밀번호 확인 -->

        <div class="user-modify-email-div">
          <div class="user-modify-email">
            새로운 비밀번호를 한 번 더 입력해주세요.
          </div>
          <input
            class="user-modify-name-input"
            style="font-family: GmarketSansMedium;"
            v-model="passwordConfirmation"
            type="password"
            id="passwordConfirmation"
            placeholder="password를 한 번 더 입력해주세요."
          />
          <div class="modify-error-text" style="font-size: 1vw; margin-top: 0.2vh; color: red;" v-show="error.user_passwordConfirmation">{{error.user_passwordConfirmation}}</div>
        </div>

        <!-- 비밀번호 변경 버튼 -->
        <div class="user-modify-email-div">
          <div :class="{'login-password-show': this.buttonShow, 'login-password-not-show': !this.buttonShow}">
            <button
              class="sign-button"
              @click="clickChangePW"
            >
              비밀번호 변경하기
            </button>
          </div>
        </div>
      </div>
    </div>
</div>

</template>

<script>
import VueJwtDecode from "vue-jwt-decode"
import Navbar from "../../components/Nav/Navbar"
import PV from "password-validator";
import axios from "axios";

const SERVER_URL = process.env.VUE_APP_SERVER_URL;
export default {
  name: "ChangePW",
  components: {
    Navbar,
  },
    props: {
    user_id: {
      type: Number,
    },
  },
  data: function () {
    return {
      userId: this.$route.params.user_id,
      passwordSchema: new PV(),
      user: "",
      myId: 0,
      nowPassword:"", //기존 비밀번호 -> user.user_password와 동일한지 먼저 보기!
      user_password:"", //새로운 비밀번호(혹시 몰라서 데이터베이스 칼럼명과 동일하게)
      passwordConfirmation: "",
      error: {
        nowPassword: false,
        user_password: false,
        user_passwordConfirmation: false,
      },
      buttonShow: false,
    }
  },
  watch: {
    nowPassword: function(v){
        this.checkNowPassword()
    },
    user_password: function(v) {
      this.checkPassword();
    },
    passwordConfirmation: function(v) {
      this.checkPasswordConfirmation();
    },

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

    this.getUserInfo()
    this.getMyId()

  },
  methods:{
    getUserInfo: function () {
      axios.get(`${SERVER_URL}/member/getallmember`)
      .then(({data}) => {
        // console.log({data})
        this.user = {data}.data.filter(x => x.user_id == this.userId)[0]
        // console.log(this.user)
        // this.makePasswordDot()
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
    },

    checkNowPassword() {
      if (this.nowPassword.length >= 0 && !this.passwordSchema.validate(this.nowPassword))
        { this.error.nowPassword = "영문,숫자 포함 8 자리이상이어야 합니다.";
          this.buttonShow = false
        }
      else { 
        if (this.nowPassword != this.user.user_password){ 
            this.error.nowPassword = "비밀번호가 일치하지 않습니다.";
            this.buttonShow = false
        }else{
          this.error.nowPassword = false;
        }
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
      else { 
        if (this.user_password == this.nowPassword){ 
                this.error.user_password = "기존 비밀번호와 동일합니다.";
                this.buttonShow = false
            }else{
            this.error.user_password = false;
            }
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
    checkForm() {
      if ( this.error.nowPassword || this.error.user_password || this.passwordConfirmation.length < 8 ) {
        this.buttonShow = false
      } else {
        this.buttonShow = true
      }
    },
    clickChangePW(){
        if(this.user_password!=""){
      axios.post(`${SERVER_URL}/member/changepw/${this.user.user_email}/${this.user_password}`)
      .then(({data}) => {
        
      })
      .catch( (err) => {
        console.log(err)
      })
      alert("비밀번호 변경 완료하였습니다!")
      this.$router.push({ name: "ModifyUser", params: { user_id: this.$store.getters.getUserId}})
    }},
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
</style>
