<template>
  <div style="font-family: GmarketSansMedium;" class="ab-modal-container">
    <div class="ab-modal-content" v-click-outside="onClickOutside">
      <button class="ab-btn-a" @click="selectA">
        <i v-if="a == '회원 정보 수정'" class="fas fa-info-circle"></i>
        <i v-else class="fas fa-users"></i>
        <div>
          {{a}}
        </div>
      </button>
      <button class="ab-btn-b" @click="selectB">
        <i v-if="b == '비밀번호 변경'" class="fas fa-key"></i>
        <i v-else class="fas fa-user-friends"></i>
        <div>
          {{b}}
        </div>
      </button>
    </div>
  </div>
</template>

<script>
export default {
  name: "AorBModal",
  props: {
    a: {
      type: String,
    },
    b: {
      type: String,
    },
  },
  data: function () {
    return {
      modalShow:true,
    }
  },
  methods: {
    selectA: function () {
      this.$emit("selectA")
    },
    selectB: function () {
      this.$emit("selectB")
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
.ab-modal-container {
  display: flex;
  flex-direction: column;
  width: 60vw;
  margin: 0 auto;
  transition: all .3s ease;
}


.ab-modal-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 25vw;
  transition-duration: 0.5s;
}

.ab-btn-a {
  width: 40%;
  height: 70%;
  font-size: 3vw;
  border: 0.5vw solid rgba(123, 104, 238, 0.33);
  border-radius: 3vw;
  background-color: white;
  transition-duration: 0.3s;
}

.ab-btn-a:hover {
  font-weight: bold;
  background-color: rgba(123, 104, 238, 0.66);
}

.ab-btn-b {
  width: 40%;
  height: 70%;
  font-size: 3vw;
  border-radius: 3vw;
  border: 0.5vw solid rgba(259, 99, 71, 0.33);
  background-color: white;
  transition-duration: 0.3s;
}

.ab-btn-b:hover {
  font-weight: bold;
  background-color: rgba(259, 99, 71, 0.66);
}

@media ( max-width: 540px ) {

  .ab-modal-container {
    width: 90vw;
  }
    
  .ab-modal-content {
    justify-content: space-around;
    height: 40vw;
    font-size: 5vw;
  }

  .ab-btn-a {
    font-size: 5vw;
    height: 90%;
    width: 45%;
    border: 1vw solid rgba(123, 104, 238, 0.33);
    border-radius: 6vw;
  }

  .ab-btn-b {
    font-size: 5vw;
    height: 90%;
    width: 45%;
    border: 1vw solid rgba(259, 99, 71, 0.33);
    border-radius: 6vw;
  }

}


</style>
