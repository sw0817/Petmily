<template>
  <div style="font-family: GmarketSansMedium;">
    <!-- 동물 정보 (여긴 이제 컴포넌트로 들어갈 듯) -->
    <div class="petInfo-container">
      <header class="petInfo">
        <div class="petInfo-next-btn" @click="orderPre">
          <button><i class="fas fa-angle-left"></i></button>
        </div>
        <div class="petInfo-allpet-img-container">
          <div v-for="(img, idx) in petImgUrl" :key="idx">
            <div class="petInfo-img">
              <img :src="img">
            </div>
          </div>
        </div>
        <div class="petInfo-next-btn" @click="orderNext">
          <button><i class="fas fa-angle-right"></i></button>
        </div>
      </header>
    </div>
    <div class="numbering-container">
      <div class="number">
        전체보기 모드
      </div>
    </div>
    <!-- 여기까지 동물 별 컴포넌트 -->
  </div>
</template>

<script>
export default {
  name: "Allpet",
  props: {
    pets: {
      type: Array,
    },
    userId: {
      type: Number,
    },
    myId: {
      type: Number,
    },
  },
  data: function () {
    return {
      petInfo: this.pets,
      petImgUrl: [],
    }
  },
  methods: {
    orderNext: function () {
      this.$emit("orderNext")
    },
    orderPre: function () {
      this.$emit("orderPre")
    },
    getImgs: function () {
      for (let i = 0; i < this.petInfo.length; i++) {
        this.petImgUrl.push("http://i4a403.p.ssafy.io/images/" + this.petInfo[i]["file_name"])
        // this.petImgUrl.push(require("../../assets/images/petImg/"+this.petInfo[i]["file_name"]))
      }
    },
  },
  created: function () {
    this.getImgs()
    // console.log(this.pets)
    // console.log(this.petImgUrl)
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
.petInfo-container {
  width: 100%;
  display: flex;
  justify-content: center;
}

.petInfo {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 80%;
}

.petInfo img {
  border-radius: 70%;
  width: 12vw;
}

.petInfo-next-btn {
  width: 5%;
  display: flex;
  justify-content: center;
}

.petInfo-next-btn button {
  font-size: 3vw;
}

.petInfo-allpet-img-container {
  width: 73vw;
  display: flex;
  justify-content: space-around;
}

.petInfo-img {
  width: 25vw;
  /* height: 25vw; */
  margin-left: 5%;
}

.petInfo-img img {
  width: 12vw;
  height: 12vw;
  margin-left: 5%;
}

.petInfo-content {
  width: 50%;
  margin-left: 5%;
  margin-right: 5%;
}

.petInfo-title-container {
  display: flex;
  align-items: center;
}

.petInfo-content-name {
  font-size: 4vw;
}

.petInfo-content-edit {
  font-size: 3vw;
  margin-left: 0.5vw;
}

.petInfo-content-edit:hover {
  cursor: pointer;
}

.numbering-container {
  margin-top: 0.5vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.number {
  font-size: 1.5vw;
}

.feed-container2 {
  margin-top: 3vh;
  border: none;
}

.feed-title {
  font-size: 2vw;
  font-weight: bold;
}

@media ( max-width: 540px) {

  .petInfo-content-name {
    font-size: 5vw;
  }
  .number {
    font-size: 3.5vw;
  }

  .petInfo-next-btn button {
    font-size: 5vw;
  }

}

</style>
