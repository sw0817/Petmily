<template>
  <div style="font-family: GmarketSansMedium;" class="nav-selecet-modal-container" v-click-outside="onClickOutside">
    <div v-for="(content, idx) in contents"
      :key="idx"
      class="nav-selecet-modal-content"  @click="selectContent(content)">
      <div class="nav-selecet-modal-content-child"></div>
      <div class="nav-selecet-modal-content-child">
        {{ content }}
      </div>
      <div class="nav-selecet-modal-content-child">
        <button v-if="content=='메시지' & unreadMessageCnt!=0" class="redCircle" >{{unreadMessageCnt}}</button>
        <button v-if="content=='내 알림' && unreadAlertCnt!=0" class="redCircle" >{{unreadAlertCnt}}</button>
      </div>
    </div>
    <div class="nav-selecet-modal-footer">
      <div class="nav-modal-exit" @click="exit">
        취소
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "NavSelectModal",
  props: {
    contents: {
      type: Array,
    },
  },
  data: function () {
    return {
      unreadAlertCntFeed:this.$store.getters.getAlertUnreadFeed,
      unreadAlertCntFollow:this.$store.getters.getAlertUnreadFollow,
      unreadMessageCnt:this.$store.getters.getMessageUnreadCnt,
      unreadAlertCnt:0,
    }
  },
  created(){
    this.unreadAlertCnt=this.unreadAlertCntFeed.length+this.unreadAlertCntFollow.length
  },
  methods: {

    exit: function () {
      this.$emit("modalOff")
    },
    selectContent: function (content) {
      if (content == "프로필") {
        this.$emit("myProfile")
      } else if (content == "내 알림") {
        this.$emit("myAlerts")
      } else if (content == "메시지") {
        this.$emit("myMessages")
      } else if (content == "로그아웃") {
        this.$emit("signOut")
      }
    },
    onClickOutside(){
      this.$emit("modalOff")
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

.redCircle{
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: red; 
  color: white; 
  z-index: 3; 
  top: 4vw; 
  left: 4vw;
  font-size: 15px;
  min-width: 20px;
  height: 20px;
  border-radius: 70%;
  /* padding: 0 0.5vw; */
}

.nav-selecet-modal-container {
  display: flex;
  flex-direction: column;
  background-color: white;
  /* width: 50vw; */
  margin: 0 auto;
  /* padding: 1vw; */
  border-radius: 1vw;
  box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
  transition: all .3s ease;
}

.nav-selecet-modal-content {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  padding: 12px 12px;
  transition-duration: 0.5s;
}
.nav-selecet-modal-content-child {
  text-align: center;
  white-space: pre;
  flex:1;
  font-size: 17px;
}

.nav-selecet-modal-footer {
  font-size: 18px;
  font-weight: bold;
  padding: 12px 12px;
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  text-align: center;
}

.nav-modal-exit {
  font-size: 17px;
}

.nav-modal-exit:hover {
  cursor: pointer;
}

@media ( max-width: 1100px) {

  .nav-selecet-modal-container {
    display: flex;
    flex-direction: column;
    background-color: white;
    /* width: 50vw; */
    margin: 0 auto;
    /* padding: 1vw; */
    border-radius: 1vw;
    box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
    transition: all .3s ease;
  }

  .nav-selecet-modal-content {
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 1.5vw;
    font-weight: bold;
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
    padding: 1vw 0;
    transition-duration: 0.5s;
  }
  .nav-selecet-modal-content-child{
    text-align: center;
    flex:1;
    font-size:1.5vw;
  }

  .nav-selecet-modal-footer {
    font-size: 1.5vw;
    font-weight: bold;
    padding: 1vw 1vw;
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
    text-align: center;
  }
  .nav-modal-exit {
  font-size: 1.5vw;
  } 

  .nav-modal-exit:hover {
    cursor: pointer;
    
  }

  .redCircle{
    background-color: red; 
    color: white; 
    z-index: 3; 
    top: 2vw;
    left: 2vw;
    min-width: 2vw;
    height: 2vw;
    font-size: 1vw;
    border-radius: 70%;
    /* padding: 0 0.7vw; */
  }
}

@media ( max-width: 540px ) {

  .redCircle {
    background-color: red; 
    color: white; 
    z-index: 3; 
    top: 4vw; 
    left: 4vw;
    min-width: 3vw;
    height: 3vw;
    font-size: 1vw;
    border-radius: 70%; 
    /* padding: 0 1.5vw; */
  }

  .nav-selecet-modal-content {
    font-size: 3vw;
  }

  .nav-selecet-modal-footer {
    font-size: 3vw;
  }

}


</style>
