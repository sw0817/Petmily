<template>
  <div class="follow-user-info-item-container">
    <div class="follow-user-info-item-pet-img-container">
      <img
        :src="myPetImg"
        class="follow-user-info-item-pet-img"
      >
    </div>
    <div class="follow-member-name" @click="moveToUserProfile">
      {{ myInfo.user_nickname }}
    </div>
  </div>
</template>

<script>
import axios from 'axios'

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "FollowUserInfo",
  props: {
    myId: {
      type: Number,
    },
  },
  data: function () {
    return {
      myInfo: [],
      myPetImg: "",
    }
  },
  methods: {
    getMyInfo: function () {
      axios.post(`${SERVER_URL}/member/getsinglemember/${this.myId}`)
      .then( (res) => {
        this.myInfo = res.data
        this.getMyPetInfo()
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    getMyPetInfo: function () {
      axios.get(`${SERVER_URL}/pet/selectAll/${this.myId}`)
      .then(({data}) => {
        // console.log(data)
        if (data.length < 1) {
          this.myPetImg = "http://i4a403.p.ssafy.io/images/basicPet.png"
          // this.myPetImg = require("../../assets/images/basicPet.png")
        } else {
          this.myPetImg = "http://i4a403.p.ssafy.io/images/" + data[0]["file_name"]
          // this.myPetImg = require("../../assets/images/petImg/"+data[0]["file_name"])
        }
      })
      .catch( (err) => {
        console.log(err)
      })
    },
    moveToUserProfile: function () {
      if (this.$route.path == `/user/profile/${this.myId}`) {
        this.$router.go(this.$router.currentRoute)
      } else {
      this.$router.push({ name: 'UserProfile', params: { user_id: `${this.myId}` } })}
    },
  },
  created: function () {
    this.getMyInfo()
  }
}
</script>

<style>

.follow-member-name:hover {
  cursor: pointer;
}

.follow-user-info-item-container {
  display: flex;
  align-items: center;
}

.follow-user-info-item-pet-img {
  width: 6vw;
  height: 6vw;
  border-radius: 70%;
}

@media ( max-width: 540px ) {

  .follow-user-info-item-pet-img {
    width: 12vw;
    height: 12vw;
  }

}


</style>
