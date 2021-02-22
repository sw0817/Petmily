<template>
  <div style="font-family: GmarketSansMedium;">
    <!-- 형태는 followerList와 동일~ -->
    <div>
        <ul>
        <li v-for="(alert,index) in alertContents" v-bind:key="index">
            <b-button style="display:inline-block; margin:4px;" @click="userProfile(alert)">{{alert.from_nickname}}</b-button>
            <div style="display:inline-block; margin:4px;">{{alert.alert_content}}</div>
            <b-button variant="outline-primary" style="display:inline-block;" @click="deleteAlert(alert, index)">확인</b-button>
        </li>
        </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL;
export default {
  name: "MyAlerts",
  props: {
    alertContents: {
      type: Array,
    },
    user_id: {
      type: Number,
    },
  },
  data: function () {
    return {
      alerts: this.alertContents,
      alert_id:0,
    }
  },
  created: function (){
  },
  methods: {
    deleteAlert(alert,index){
      this.alert_id=alert.alert_id
      axios.delete(`${SERVER_URL}/alert/deletealert/${this.alert_id}`)
      .then( (res) => {
          if(res)
            console.log("알람 확인! -> 새로고침 필요")
      })
      .catch( (err ) => {
        console.log(err)
        const msg = "문제가 발생했습니다."
        alert(msg)
      });
    },

    userProfile(alert){
      this.$router.push({ name: 'UserProfile', params: { user_id: alert.from_id } })
    }
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
</style>
