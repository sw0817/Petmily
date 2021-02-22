<template>
  <div style="font-family: GmarketSansMedium;" class="selecet-modal-container" v-click-outside="onClickOutside">
    <div v-for="(content, idx) in contents"
      :key="idx"
      class="selecet-modal-content">
      <div @click="selectContent(content)">
        {{ content }}
      </div>
    </div>

    <div class="selecet-modal-footer">
      <div class="modal-exit" @click="exit">
        취소
      </div>
    </div>
  </div>
</template>

<script>
import MyAlerts from "../alert/MyAlerts"
export default {
  name: "SelectModal",
  props: {
    contents: {
      type: Array,
    },
  },
  data: function () {
    return {
  
    }
  },
  methods: {

    exit: function () {
      this.$emit("modalOff")
    },
    selectContent: function (content) {
      if (content == "삭제") {
        this.$emit("delete")
      } else if (content == "신고") {
        this.$emit("report")
      } else if (content == "게시물로 이동") {
        this.$emit("read")
      } else if (content == "수정") {
        this.$emit("update")
      } else if (content == "프로필") {
        this.$emit("myProfile")
      } else if (content == "내 알림") {
        this.$emit("myAlerts")
      } else if (content == "메시지") {
        this.$emit("myMessages")
      } else if (content == "로그아웃") {
        this.$emit("signOut")
      } else if (content == "펫 태그") {
        this.$emit("petTag")
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
.selecet-modal-container {
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

.selecet-modal-content {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  padding: 12px 12px;
  transition-duration: 0.5s;
}

.selecet-modal-footer {
  font-size: 18px;
  font-weight: bold;
  padding: 12px 12px;
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  text-align: center;
}

.modal-exit:hover {
  cursor: pointer;
}

@media ( max-width: 1100px) {

  .selecet-modal-container {
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

  .selecet-modal-content {
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 1.5vw;
    font-weight: bold;
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
    padding: 1vw 1vw;
    transition-duration: 0.5s;
  }

  .selecet-modal-footer {
    font-size: 1.5vw;
    font-weight: bold;
    padding: 1vw 1vw;
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
    text-align: center;
  }

  .modal-exit:hover {
    cursor: pointer;
  }
}

@media ( max-width: 540px ) {

  .selecet-modal-content {
    font-size: 3vw;
  }

  .selecet-modal-footer {
    font-size: 3vw;
  }

}


</style>
