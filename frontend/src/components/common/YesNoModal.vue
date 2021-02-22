<template>
  <div style="font-family: GmarketSansMedium;" class="simple-modal-container">
    <div class="simple-title-header">
      <div class="simple-title-title">
        알림
      </div>
    </div>
      <div class="yes-no-simple-modal-content">
        {{ contents }}
        <div v-if="contents=='이 게시글을 신고하시겠습니까?'">
          <textarea
            name=""
            id=""
            maxlength="500"
            placeholder="신고 이유를 작성해주세요."
            rows="3"
            class="read-feed-content-textarea"
          >
          </textarea>
        </div>
      </div>

    <div class="yes-no-simple-modal-footer">
      <div class="modal-exit" @click="confirm" @keyup.enter="confirm">
        확인
      </div>
      <div class="modal-exit" @click="exit">
        취소
      </div>              
    </div>
  </div>
</template>

<script>
export default {
  name: "YesNoModal",
  props: {
    contents: {
      type: String,
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
    confirm: function () {
      this.$emit("deleteDiary")
    },
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
.simple-modal-container {
  display: flex;
  flex-direction: column;
  background-color: white;
  width: 50vw;
  margin: 0 auto;
  /* padding: 1vw; */
  border-radius: 1vw;
  box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
  transition: all .3s ease;
}

.simple-title-header {
  font-size: 1.5vw;
  font-weight: bold;
  color: mediumslateblue;
  padding: 1vw;
}

.yes-no-simple-modal-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 2.5vw;
  font-weight: bold;
  border-top: 0.1vw solid mediumslateblue;
  padding: 3vw 1vw;
  transition-duration: 0.5s;
}

.yes-no-simple-modal-content div {
  display: flex;
  justify-content: center;
  width: 100%;
}

.yes-no-simple-modal-content div textarea {
  width: 80%;
}

.yes-no-simple-modal-footer {
  display: flex;
  justify-content: space-between;
  font-size: 2vw;
  font-weight: bold;
  padding: 1vw;
  text-align: end;
}

.modal-exit {
  margin: 0 3vw;
}

.modal-exit:hover {
  cursor: pointer;
}

@media ( max-width: 540px ) {

  .simple-modal-container {
  width: 80vw;
  }
  
  .simple-title-header {
    font-size: 5vw;
  }
    
  .yes-no-simple-modal-content {
    font-size: 5vw;
  }

  .yes-no-simple-modal-footer {
    font-size: 5vw;
  }

}


</style>
