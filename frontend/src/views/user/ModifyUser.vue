<template>
  <div style="font-family: GmarketSansMedium;" class="user" id="modifyUser">
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

        <!-- 닉네임 : 아 중복검사해야하잖아~!~!~!~! -->
        <div class="user-modify-email-div" >
          <div class="user-modify-email">
            닉네임
          </div>
          <div class="user-modify-flex">
            <input
              v-model="user_nickname"
              type="text"
              class="user-modify-nickname-input"
              id="user_nickname"
              maxlength="4"
            />
            <button
              v-if="nickBtnShow"
              @click="clickNickCheck"
              class="modify-certificationBtn"
            >
              중복검사
            </button>
            <div
              v-else
              class="modify-certificationBtn-off"
            >
              중복검사
            </div>
          </div>
          <div class="modify-right-text" v-if="nickChangeOK">사용 가능한 닉네임입니다.</div>
          <div class="modify-error-text" v-if="error.user_nickname">{{error.user_nickname}}</div>
        </div>

        <!-- 이름 확인 -->
        <div class="user-modify-email-div">
          <div class="user-modify-email">
            이름
          </div>
          <input
            class="user-modify-name-input"
            v-model="user_name"
            type="text"
            id="user_name"
            maxlength="10"
          />
          <div class="modify-right-text" v-if="nameChangeOK">변경 가능한 이름입니다.</div>
          <div class="modify-error-text" v-if="error.user_name">
            {{ error.user_name }}
          </div>
        </div>

        <!-- 생년월일 -->
        <div class="user-modify-email-div">
          <div class="user-modify-email">
            생년월일
          </div>

          <div class="user-modify-flex">
            <input
              class="user-modify-year"
              v-model="year"
              type="text"
              id="year"
              maxlength="4"
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
          <div class="modify-right-text" v-if="birthChangeOK && !error.date && !error.month && !error.year">올바른 생년월일입니다.</div>
        </div>
        
        <div class="user-modify-email-div">
          <div class="user-modify-email">
            간단한 한줄소개를 작성해주세요.
          </div>
          <input
            class="user-modify-name-input"
            v-model="user_content"
            type="text"
            maxlength="20"
            id="user_content"
          />
          <div class="modify-right-text" style="font-size: 1vw; margin-top: 0.2vh; color: green;" v-if="contentChangeOK">한줄소개가 변경되었습니다.</div>
          <div class="modify-error-text" style="font-size: 1vw; margin-top: 0.2vh; color: red;" v-if="error.user_content">
            {{ error.user_content }}
          </div>
        </div>
        <!-- 성별 -->
        <div class="user-modify-email-div">
          <div class="user-modify-email">
            성별
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
          <div class="modify-right-text" v-if="genderChangeOK">성별이 변경되었습니다.</div>
        </div>

        <div class="user-modify-btn-flex">
          <button
            class="user-modify-confirm-btn"
            @click="changeDetail"
          >
            회원수정
          </button>
          <button
            @click="clickDeleteUser"
            class="delete-user-btn"
          >
            회원탈퇴
          </button>
        </div>
      </div>
    </div>
    <div class="text-modal" v-if="yesNoShow">
      <div class="text-modal-wrapper">
        <YesNoModal
          :contents="yesNoModalContents"
          @modalOff="yesNoOff"
          @deleteDiary="deleteAllInfo"
        />
      </div>
    </div>
    <div class="text-modal" v-if="yesNoShow2">
      <div class="text-modal-wrapper">
        <YesNoModal
          :contents="yesNoModalContents2"
          @modalOff="yesNoOff2"
          @deleteDiary="updateUser"
        />
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
import YesNoModal from "../../components/common/YesNoModal"
import SimpleModal from "../../components/common/SimpleModal"
import Navbar from "../../components/Nav/Navbar"
import VueJwtDecode from "vue-jwt-decode"
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "ModifyUser",
    components: {
      Navbar,
      YesNoModal,
      SimpleModal,
  },
  props: {
    user_id: {
        type: Number,
      },
  },
  data: function () {
    return {
      userId: this.$route.params.user_id,
      user: "",
      passwordDot:"", //비밀번호를 점으로 표시해주기..
      myId: 0,
      user_nickname:"",
      nickBtnShow:false,
      nickChangeOK:false,
      nameChangeOK:false,
      birthChangeOK:false,
      genderChangeOK:false,
      contentChangeOK:false,
      user_name: "",
      maleButton: false,
      femaleButton: false,
      user_gender: false,
      user_birth: false,
      user_content:"",
      year: "",
      date: "",
      error: {
        user_nickname: false,
        user_nmae: false,
        user_birth: false,
        user_content: false,
        year: false,
        month: false,
        date: false,
        user_gender: false,
      },
      selectedMonth: null,
      patternNickName: /[^0-9a-zA-Z가-힣]/,
      patternName: /[^a-zA-Z가-힣]/,
      patternNum: /[^0-9]/,
      splitDate:[], //받은 생년월일을 - 를 기준으로 나누기
      simpleShow: false,
      simpleModalContents: "펫밀리를 탈퇴하셨습니다.",
      simpleShow2: false,
      simpleModalContents2: "",
      yesNoShow: false,
      yesNoModalContents: "정말 펫밀리를 탈퇴하시겠습니까 ?",
      yesNoShow2: false,
      yesNoModalContents2: "회원정보 수정을 하시겠습니까?",
    };
  },
  created: function () {
    this.component = this;
    this.getUserInfo()
    this.getMyId()
  },
watch: {
    user_nickname: function(v) {
      this.checkNickname();
    },
    user_name: function(v) {
      this.checkName();
    },    
     user_content: function(v) {
      this.checkContent();
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
  methods:{
    getUserInfo: function () {
      axios.get(`${SERVER_URL}/member/getallmember`)
      .then(({data}) => {
        // console.log({data})
        this.user = {data}.data.filter(x => x.user_id == this.userId)[0]
        // console.log(this.user)
        this.makePasswordDot()
        this.user_nickname=this.user.user_nickname
        this.user_name=this.user.user_name
        this.user_birth=this.user.user_birth
        this.user_content=this.user.user_content
        this.splitDate=this.user_birth.split("-");
        this.year=this.splitDate[0];
        this.selectedMonth=this.splitDate[1];
        this.date=this.splitDate[2];

        this.user_gender=this.user.user_gender;
        if(this.user_gender=="W") this.femaleButton=true;
        else if(this.user_gender=="M") this.maleButton=true;

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
    makePasswordDot(){ //패스워드 점으로 표시
        for(let i=0;i<this.user.user_password.length;i++){
            this.passwordDot+="*";
        }    
    },
    clickChangePW: function () { //비밀번호 변경 화면으로 이동
      this.$router.push({ name: "ChangePW", params: { user_id: this.$store.getters.getUserId}})
    },
    clickNickCheck(){ //닉네임 중복검사
      axios.get(`${SERVER_URL}/member/changenickname/${this.user_nickname}`)
      .then(({data}) => {
        if(data==0){ //중복된 닉네임 없음!
            this.nickChangeOK=true; //사용 가능한 닉네임임을 알려주기!(error는 아니니까~)
            this.error.user_nickname=false;
        }else{
                this.nickChangeOK=false;
                this.error.user_nickname = "이미 가입한 닉네임입니다."
            }
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    checkNickname() { //닉네임이 변경이 되면 중복검사 버튼이 나와야하고... 중복검사를 안하면 회원수정이 안되어야함
      this.nickChangeOK=false;
      if ( 10 < this.user_nickname.length || this.user_nickname.length < 2 || this.patternNickName.test(this.user_nickname) ) {
        this.error.user_nickname = "부적합한 닉네임입니다."
        this.nickBtnShow=false;
      } 
      else{
        this.error.user_nickname=false;
        if(this.user.user_nickname===this.user_nickname){
            this.nickChangeOK=false;
            this.nickBtnShow=false;
        }else{
            this.nickBtnShow=true;
        }
      }
    },
    checkName() {
      if ( this.patternName.test(this.user_name) || this.user_name.length < 2 || 10 < this.user_name.length ) {
        this.error.user_name = "정확한 이름을 입력해주세요."
        this.nameChangeOK=false;
      } else {
        this.error.user_name = false
        if(this.user_name!=this.user.user_name){
            this.nameChangeOK=true; //이름이 바꼈음을 알려주는 게 좋지 않을까?
        }else{
            this.nameChangeOK=false;
        }
      }
    },
    checkContent() {
      if ( this.user_content.length < 1 || 200 < this.user_content.length ) {
        this.error.user_content = "정확한 이름을 입력해주세요."
        this.contentChangeOK=false;
      } else {
        this.error.user_content = false
        if(this.user_content!=this.user.user_content){
            this.contentChangeOK=true; //이름이 바꼈음을 알려주는 게 좋지 않을까?
        }else{
            this.contentChangeOK=false;
        }
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
      }

    this.isChangedBirth();
    },

    checkMonth() {
      if ( !this.selectedMonth ) {
        this.error.month = "태어난 월을 선택해주세요."
        this.buttonShow = false
      } else {
        this.error.month = false
      }

        this.isChangedBirth();
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
      }
    this.isChangedBirth();

    },
    //생년월일이 변경되었는지 체크
    isChangedBirth(){
      if(this.year!=this.splitDate[0] || this.selectedMonth!=this.splitDate[1] || this.date!=this.splitDate[2]){
          this.birthChangeOK=true;
      }else{
          this.birthChangeOK=false;
      }
    },

    checkGender() {
      if ( !this.user_gender ) {
        this.error.user_gender = "성별을 선택해주세요."
        this.buttonShow = false
      } else {
        this.error.user_gender = false
      }

 //성별이 변경되었는지 체크
      if(this.user_gender!=this.user.user_gender)
        this.genderChangeOK=true;
      else
        this.genderChangeOK=false;
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
    yesNoOff2: function () {
      this.yesNoShow2 = !this.yesNoShow2
    },
    updateUser: function () {
      this.user_birth = this.year + this.selectedMonth + this.date
      axios.put(`${SERVER_URL}/member/userupdate/${this.user.user_email}`, {
        user_email:this.user.user_email,
        user_password: this.user.user_password,
        user_name: this.user_name,
        user_nickname: this.user_nickname,
        user_birth: this.user_birth,
        user_gender: this.user_gender,
        user_content:this.user_content
      })
      .then(({ data }) => {
        if(data==="SUCCESS"){
          this.simpleModalContents2 = "회원정보 수정이 완료되었습니다!"
          this.simpleShow2 = !this.simpleShow2
          this.$router.push({ name: "UserProfile", params: { user_id: this.$store.getters.getUserId}})
        }
        else
          this.simpleModalContents2 = "회원정보 수정에 실패했습니다."
          this.simpleShow2 = !this.simpleShow2
      });
    },
    changeDetail(){ //회원정보 수정!!!!!!
      if (this.nickChangeOK || this.nameChangeOK || this.birthChangeOK || this.genderChangeOK || this.contentChangeOK){
        this.yesNoShow2 = !this.yesNoShow2
      } else { //정보 변경 안됨
      if ( this.error.user_nickname  || !this.user_gender || this.error.date || this.error.month || this.error.year || this.error.user_name) {
        this.simpleModalContents2 = "잘못 입력한 정보가 존재합니다."
        this.simpleShow2 = !this.simpleShow2
      } else {
        this.$router.push({ name: "UserProfile", params: { user_id: this.$store.getters.getUserId}})
      }}
    },
    clickDeleteUser: function () { //회원탈퇴
      this.yesNoShow = !this.yesNoShow
    },
    deleteAllInfo: function(){
      this.deleteUser()
      .then(this.deleteFeedalert)
      .then(this.deleteFollowalert)
      .then(this.deleteFeed)
      .then(this.deleteFile)
      .then(this.deleteFriendTag)
      .then(this.deleteBackground)
      .then(this.deleteDiarypettag)
      .then(this.deleteLikely)
      .then(this.deleteFollow)
      .then(this.deleteMemo)
      .then(this.deletePet)
      .then(this.deleteRoom)
      .then(this.deleteChat)
      .then(this.deleteRecentchat)
      .then(this.deletePettag)
      .then(this.deleteTag)
    },
    deleteUser: function () {
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/member/userdelete/${this.user.user_email}`)
        .then(({data}) => {
          if(data === "SUCCESS"){
            this.simpleShow = !this.simpleShow
            resolve(this.userId);  // this.userId
          }
          else{
            this.simpleModalContents2 = "회원탈퇴에 실패했습니다."
            this.simpleShow2 = !this.simpleShow2
          }
        }).catch((e) => {
          reject(new Error(`삭제시 에러발생`))
          //console.log(e)
        })
      })
    },
    deleteFeedalert: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/alert/feeddeletealertuserid/${user_id}`)
        .then(({data}) => {
          resolve(this.userId);  // this.userId
        }).catch((e) => {
          reject(new Error(`피드알람 삭제시 에러발생`))
        })
      })
    },
    deleteFollowalert: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/alert/deletealertuserid/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)  // this.userId
        }).catch((e) => {
          reject(new Error(`팔로우 알람 삭제시 에러발생`))
        })
      })
    },
    deleteFollow: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/member/deleteallfollow/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)  // this.userId
        }).catch((e) => {
          reject(new Error(`팔로우 정보 삭제시 에러발생`))
        })
      })
    },
    deleteFeed: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/feed/deletionallfeeduserid/${user_id}`)
        .then(({data}) =>{
          resolve(this.userId)
        }).catch((e) =>{
          reject(new Error(`탈퇴회원 피드 정보 삭제시 에러발생`))
        })
      })
    },
    deleteFile: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/feed/deletionallfileuserid/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 피드 정보 삭제시 에러 발생`))
        })
      })
    },
    deleteFriendTag: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/feed/deletionallfriend/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 친구정보 삭제시 에러 발생`))
        })
      })
    },
    deleteBackground: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/diary/deletebg/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 다이어리 배경이미지 삭제시 에러 발생`))
        })
      })
    },
    deleteDiary: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/diary/deletediarybyuserid/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 다이어리 삭제시 에러 발생`))
        })
      })
    },
    deleteDiaryfile: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/diary/deletediaryfilebyuserid/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 다이어리파일 삭제시 에러 발생`))
        })
      })
    },
    deleteDiarypettag: function(user_id){
       return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/pet/deletediarypettag/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 펫태그 삭제시 에러 발생`))
        })
      })
    },
    deleteLikely: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/like/deletelikeuserid/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 좋아요 에러 발생`))
        })
      })
    },
    deleteMemo: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/memo/deletememouserid/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 댓글 에러 발생`))
        })
      })
    },
    deletePet: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/pet/deletepetbyuserid/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 펫정보 에러 발생`))
        })
      })
    },
    deleteRoom: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/chat/deleteroomuserid/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 room 에러 발생`))
        })
      })
    },
    deleteChat: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/chat/deletechat/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 chat 에러 발생`))
        })
      })
    },
    deleteRecentchat: function(user_id){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/chat/deleterecentchat/${user_id}`)
        .then(({data}) => {
          resolve(this.userId)
        }).catch((e) => {
          reject(new Error(`탈퇴회원 recentchat 에러 발생`))
        })
      })
    },
    deletePettag: function(){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/feed/deletionallpettags`)
        .then(({data}) => {
          resolve()
        }).catch((e) => {
          reject(new Error(`탈퇴회원  pettag 에러 발생`))
        })
      })
    },
    deleteTag:function(){
      return new Promise((resolve, reject) => {
        axios.delete(`${SERVER_URL}/feed/deletionalltags`)
        .then(({data}) => {
          resolve()
        }).catch((e) => {
          reject(new Error(`탈퇴회원  tag 에러 발생`))
        })
      })
    },
    yesNoOff: function () {
      this.yesNoShow = !this.yesNoShow
    },
    simpleOff: function () {
      this.simpleShow = !this.simpleShow
      this.$store.dispatch("logout")
      this.$router.push({ name: "Login"})
    },
    simpleOff2: function () {
      this.simpleShow2 = !this.simpleShow2
    },
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
.user-modify-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.user-modify-email-div {
  display: flex;
  flex-direction: column;
  width: 70%;
}

.user-modify-email {
  margin-top: 6vh;
  font-size: 1vw;
  color: orange;
}

.user-modify-flex {
  display: flex;
  align-items: center;
}

.user-modify-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1vh;
}

.user-modify-email-input {
  border: none;
  border-bottom: 0.2vw solid black;
  width: 100%;
  color: #6e6f6f;
  font-size: 2vw;
}

.user-modify-nickname-input {
  border: none;
  border-bottom: 0.2vw solid black;
  width: 75%;
  font-size: 2vw;
}

.user-modify-name-input {
  border: none;
  border-bottom: 0.2vw solid black;
  width: 100%;
  font-size: 2vw;
}

.modify-certificationBtn {
  width: 20%;
  margin-left: 5%;
  padding: 0.5vw;
  border-radius: 1vw;
  border: 0.3vw solid;
  border-color: mediumslateblue;
  background-color: white;
  color:mediumslateblue;
  font-size: 1.3vw;
  font-weight: bold;
  transition-duration: 0.5s;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modify-certificationBtn-off {
  width: 20%;
  margin-left: 5%;
  padding: 0.5vw;
  border-radius: 1vw;
  border: 0.3vw solid;
  border-color: gray;
  background-color: white;
  color:gray;
  font-size: 1.3vw;
  font-weight: bold;
  transition-duration: 0.5s;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modify-certificationBtn:hover {
  background-color:mediumslateblue;
  color:white;
  cursor: pointer;
}


.modify-right-text {
  font-size: 1vw;
  margin-top: 0.2vh;
  color: green;
}

.modify-error-text {
  font-size: 1vw;
  margin-top: 0.2vh;
  color: red;
}

.modify-error-text-date {
  font-size: 1vw;
  margin-top: 0.2vh;
  color: red;
}

.user-modify-year {
  border: none;
  border-bottom: 0.2vw solid black;
  border-radius: 0px;
  width: 15vw;
  font-size: 1.5vw;
}

.user-modify-month {
  border: none;
  border-bottom: 0.2vw solid black;
  border-radius: 0px;
  font-size: 1.5vw;
  color: black;
  width: 15vw;
  margin-left: 2vw;
}

.user-modify-day {
  border: none;
  border-bottom: 0.2vw solid black;
  border-radius: 0px;
  font-size: 1.5vw;
  color: black;
  width: 15vw;
  margin-left: 2vw;
}


.modifygenderButtonOn {
  width: 40%;
  margin: 0 1vw;
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

.modifygenderButtonOff {
  width: 40%;
  margin: 0 1vw;
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

.modifygenderButtonOff:hover {
  background-color:mediumslateblue;
  color: white;
}

.user-modify-btn-flex {
  display: flex;
  align-items: center;
  width: 80%;
}

.user-modify-confirm-btn {
  width: 70%;
  margin: 0 1vw;
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

.user-modify-confirm-btn:hover {
  background-color:mediumslateblue;
  color: white;
}

.delete-user-btn {
  width: 20%;
  margin: 0 1vw;
  margin-top: 2vh;
  background-color: white;
  color:gray;
  border: 0.3vw solid;
  border-color:gray;
  font-weight: bold;
  font-size: 2vw;
  padding: 0.5vw;
  border-radius: 1vw;
  transition-duration: 0.5s;
}

@media (max-width: 540px) {

  .user-modify-email-div {
    display: flex;
    flex-direction: column;
    width: 95%;
  }

  .user-modify-email {
    margin-top: 4vh;
    font-size: 1.5vw;
    color: orange;
  }

  .user-modify-email-input {
    font-size: 5vw;
  }

  .user-modify-nickname-input {
    font-size: 5vw;
  }

  .user-modify-name-input {
    font-size: 5vw;
  }

  .modify-certificationBtn {
    font-size: 3vw;
  }

  .modify-certificationBtn-off {
    font-size: 3vw;
  }

  .modify-right-text {
    font-size: 2vw;
    margin-top: 0.2vh;
    color: green;
  }

  .modify-error-text {
    font-size: 2vw;
    margin-top: 0.2vh;
    color: red;
  }

  .modify-error-text-date {
    font-size: 2vw;
    margin-top: 0.2vh;
    color: red;
  }

  .user-modify-year {
    border: none;
    border-bottom: 0.2vw solid black;
    border-radius: 0px;
    width: 25vw;
    font-size: 5vw;
  }

  .user-modify-month {
    border: none;
    border-bottom: 0.2vw solid black;
    border-radius: 0px;
    font-size: 5vw;
    color: black;
    width: 25vw;
    margin-left: 2vw;
  }

  .user-modify-day {
    border: none;
    border-bottom: 0.2vw solid black;
    border-radius: 0px;
    font-size: 5vw;
    color: black;
    width: 25vw;
    margin-left: 2vw;
  }


  .modifygenderButtonOn {
    width: 40%;
    margin: 0 1vw;
    margin-top: 2vh;
    background-color:mediumslateblue;
    color: white;
    border: 0.3vw solid;
    border-color:mediumslateblue;
    font-weight: bold;
    font-size: 4vw;
    padding: 0.5vw;
    border-radius: 1vw;
    transition-duration: 0.5s;
  }

  .modifygenderButtonOff {
    width: 40%;
    margin: 0 1vw;
    margin-top: 2vh;
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

  .modifygenderButtonOff:hover {
    background-color:mediumslateblue;
    color: white;
  }

  .user-modify-btn-flex {
    display: flex;
    align-items: center;
    width: 95%;
  }

  .user-modify-confirm-btn {
    width: 65%;
    margin: 0 1vw;
    margin-top: 2vh;
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

  .user-modify-confirm-btn:hover {
    background-color:mediumslateblue;
    color: white;
  }

  .delete-user-btn {
    width: 25%;
    margin: 0 1vw;
    margin-top: 2vh;
    background-color: white;
    color:gray;
    border: 0.3vw solid;
    border-color:gray;
    font-weight: bold;
    font-size: 4vw;
    padding: 0.5vw;
    border-radius: 1vw;
    transition-duration: 0.5s;
  }

}

</style>

