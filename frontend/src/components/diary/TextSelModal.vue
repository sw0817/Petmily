<template>
  <div style="font-family: GmarketSansMedium;" class="text-modal-container" v-click-outside="onClickOutside">
    <div class="modal-title-header">
      <div class="modal-title-title">
        {{ title }}
      </div>
    </div>
    <div v-for="(topic, idx) in contents" :key="idx" class="modal-text-contents">
      <div class="text-modal-content-array" @click="selectFeeling(topic, idx)">
        <img
          v-if="topic != '선택안함'"
          :src="icons[idx]"
          class="text-modal-content-img"
        >
        <div class="text-modal-content">
          {{ topic }}
        </div>
      </div>
    </div>
    <div class="text-modal-footer">
      <div class="modal-exit" @click="exit">
        닫기
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TextSelModal",
  props: {
    title: {
      type: String,
    },
    contents: {
      type: Array,
    },
    icons: {
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
    selectFeeling: function (topic, idx) {
      this.$emit("SelectFeeling", topic, idx)
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

.text-modal-container {
  display: flex;
  flex-direction: column;
  background-color: white;
  width: 50vw;
  max-width: 550px;
  margin: 0 auto;
  /* padding: 1vw; */
  border-radius: 1vw;
  box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
  transition: all .3s ease;
}

.modal-title-header {
  font-size: 16px;
  font-weight: bold;
  color: rgb(251, 75, 75);
  padding: 10.5px;
}

.text-modal-content-array {
  display: flex;
  align-items: center;
  border-top: 0.1vw solid rgb(251, 75, 75);
  padding: 10.5px;
  transition-duration: 0.5s;
}

.text-modal-content-array:hover {
  cursor: pointer;
  background-color: rgb(251, 75, 75);
}

.text-modal-content-img {
  width: 27.5px;
  height: 27.5px;
  border-radius: 70%;
  margin-right: 11px;
}

.text-modal-content {
  font-size: 16.5px;
  font-weight: bold;
}

.text-modal-footer {
  font-size: 16.5px;
  font-weight: bold;
  padding: 10.5px;
  text-align: end;
}

.modal-exit:hover {
  cursor: pointer;
}

@media (max-width: 1100px) {
  .text-modal-container {
    width: 50vw;
    margin: 0 auto;
    border-radius: 1vw;
    box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
  }

  .modal-title-header {
    font-size: 1.5vw;
    padding: 1vw;
  }

  .text-modal-content-array {
    padding: 1vw;
  }

  .text-modal-content-img {
    width: 2.5vw;
    height: 2.5vw;
    border-radius: 70%;
    margin-right: 1vw;
  }

  .text-modal-content {
    font-size: 1.5vw;
  }

  .text-modal-footer {
    font-size: 1.5vw;
    padding: 1vw;
  }

}

@media ( max-width: 540px ) {

  .text-modal-container {
    width: 80vw;
    margin: 0 auto;
    border-radius: 3vw;
    box-shadow: 0 1vw 4vw rgba(0, 0, 0, 0.33);
  }

  .modal-title-header {
    font-size: 4vw;
    padding: 2vw;
  }

  .text-modal-content-array {
    padding: 1vw 2vw;
  }

  .text-modal-content {
    font-size: 4vw;
  }

  .text-modal-footer {
    font-size: 4vw;
    padding: 2vw;
  }

  .text-modal-content-img {
    width: 5vw;
    height: 5vw;
    border-radius: 70%;
    margin-right: 1vw;
  }

}

</style>