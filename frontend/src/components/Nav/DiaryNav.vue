<template>
  <div style="font-family: GmarketSansMedium;">
    <nav class="diary-nav">
      <ul class="diary-nav-menu">
        <div class="diary-nav-menu-div-logo">
          <li style="width: 100%">
            <img @click="moveToDiary" class="logo-diary" src="@/assets/images/logo-diary.png"></li>
        </div>
        <div class="diary-nav-menu-div">
          <li class="diary-nav-menu-profile">
             <img @click="moveToFeedMain" class="icons" src="@/assets/images/icons/feed.png"></li>
          <li class="diary-nav-menu-profile">
        <img @click="myProfile" class="icons" src="@/assets/images/icons/profile.png">
          </li>
        </div>
      </ul>
    </nav>
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
import VueJwtDecode from "vue-jwt-decode"
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default {
  name: "DiaryNav",
  components: {
    SimpleModal,
  },
  data: function () {
    return {
      messages: [],
      user_id: 0,
      pets: [],
      simpleModalContents: "먼저 펫을 등록해주세요.",
      simpleShow: false,
    }
  },
  methods: {
    moveToFeedMain: function () {
        this.$router.push({ name: 'FeedMain'})
    },
    myProfile: function () {
      this.$router.push({ name: 'UserProfile', params: { user_id: `${this.$store.getters.getUserId}` } })
    },
    moveToDiary: function () {
      if (this.$route.path == `/diary`) {
        this.$router.go(this.$router.currentRoute)
      } else {
      this.$router.push({ name: 'Calendar' })
      }
    },
    simpleOff: function () {
      this.simpleShow = !this.simpleShow
      this.$router.push({ name: 'UserProfile', params: { user_id: `${this.$store.getters.getUserId}` } })
    },
    getUserPets: function(){
      axios.get(`${SERVER_URL}/pet/selectAll/${this.user_id}`)
        .then(({data}) => {
          if (data.length < 1) {
            this.simpleShow = true
          }
        })
        .catch( (err) => {
          console.log(err)
        })
    },
  },
  created: function () {
    const token = localStorage.getItem('jwt')
    if (token) {
      this.$store.state.accessToken = token
      const info = VueJwtDecode.decode(token)
      this.$store.state.userId = info.user.user_id
      this.user_id=this.$store.getters.getUserId;
    } else {
      this.$router.push({ name: 'Login' })
    }
    this.getUserPets()
  },
  computed: {
  },
  watch: {
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

.icons {
  width: 60px;
  height: 60px;
  color: #6d6d6d;
}

.diary-nav {
  display: flex;
  justify-content: center;
  align-items: center;
 
  width: 100%;
  height: 10vh;
  border-bottom: 1px solid  rgba(175, 175, 175, 0.5);
}

.diary-nav-menu {
  list-style: none;
  display: flex;
  color: white;
  justify-content: space-between;
  align-items: center;
  width: 95%;
  margin: 10px 5px 0 5px;
}

.logo-diary {
  cursor: pointer;
  margin-top: 0vh;
  width: 100%;
}
.diary-nav-menu-div {
   display: flex;
  flex-direction: row;
  justify-content: flex-end;
  list-style: none;
  width: 25%;
  /* padding-right: 30px; */
  align-items: center;
  color: rgb(145, 145, 145);
  /* width: 30%; */
}

.diary-nav-menu-div li:hover {
  cursor: pointer;
}

.diary-nav-menu-div-logo {
  list-style: none;
  display: flex;
  color: white;
  width: 25%;
  max-width: 235px;
}

.diary-nav-menu-div-logo-title {
  color: white;
  font-size: 1.5vw;
}

.diary-nav-menu-div-logo-title:hover {
  cursor: pointer;
}

.diary-nav-menu-div-logo-btn {
  color: white;
  font-size: 1.5vw;
  margin-left: 1vw;
}

ul div li {
  padding: 1vw;
  display: flex;
  align-items: center;
}

.fa-user-circle:hover {
  cursor: pointer;
}

.diary-nav-menu-profile {
  display: flex;
  flex-direction: column;
  font-size: 1.5vw;
}

@media (max-width: 1100px) {

  .icons{
    width: 5.5vw;
    height: 5.5vw;
  }

}

@media (max-width: 540px) {

  .diary-nav-menu {
    margin: 3.6px 5px 0 5px;
  }

  ul div li {
    padding: 0.1vw;
  }

  .icons{
    width: 8vw;
    height: 8vw;
  }

}

</style>
