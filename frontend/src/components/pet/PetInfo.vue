<template>
  <div style="font-family: GmarketSansMedium;">
    <!-- 동물 정보 (여긴 이제 컴포넌트로 들어갈 듯) -->
    <div class="petInfo-container">
      <header class="petInfo">
        <div class="petInfo-next-btn" @click="orderPre">
          <button><i class="fas fa-angle-left"></i></button>
        </div>
        <div class="petInfo-img">
          <img :src="petImgUrl[0]" alt="">
        </div>
        <section class="petInfo-content">
          <div class="petInfo-title-container">
            <span class="petInfo-content-name">
              {{ pet.pet_name }} 
            </span>
            <div v-if="userId == myId">
              <button class="petInfo-content-name" @click="modifyPetInfo">
                <i class="fas fa-cog"></i>
              </button>
            </div>
          </div>
          <div class="PetInfo-content-kind">
            종류 : {{ pet.pet_kind }}
          </div>
          <div class="PetInfo-content-content">
            {{ pet.pet_content }}
          </div>
        </section>
        <div class="petInfo-next-btn" @click="orderNext">
          <button><i class="fas fa-angle-right"></i></button>
        </div>
      </header>
    </div>
    <div class="numbering-container">
      <div class="number">
        {{ number }} / {{ cnt }}
      </div>
    </div>
    <!-- 여기까지 동물 별 컴포넌트 -->
  </div>
</template>

<script>
export default {
  name: "PetInfo",
  props: {
    pet: {
      type: Object,
    },
    userId: {
      type: Number,
    },
    myId: {
      type: Number,
    },
    number: {
      type: Number,
    },
    cnt: {
      type: Number,
    },
  },
  data: function () {
    return {
      petInfo: this.pet,
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
    modifyPetInfo: function () {
      this.$router.push({ name: "ModifyPet", params: { user_id: this.$route.params.user_id, pet_id: this.pet.pet_id } } )
    },
  },
  created: function () {
    this.petImgUrl.push("http://i4a403.p.ssafy.io/images/" + this.petInfo["file_name"])
    // this.petImgUrl.push(require("../../assets/images/petImg/"+this.petInfo["file_name"]))
    // console.log(`유저아이디 ${this.userId}`)
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

.PetInfo-content-kind {
  font-size: 2vw;
}

.PetInfo-content-content {
  font-size: 1.5vw;
}

.petInfo-title-container {
  display: flex;
  align-items: center;
}

.petInfo-content-name {
  font-size: 4vw;
  margin-right: 1vw;
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

  .petInfo-next-btn {
    width: 5%;
    display: flex;
    justify-content: center;
  }

  .petInfo-next-btn button {
    font-size: 3vw;
  }

  .petInfo-img {
    width: 30vw;
    /* height: 25vw; */
    margin-left: 5%;
  }

  .petInfo-img img {
    width: 15vw;
    height: 15vw;
    margin-left: 5%;
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

  .petInfo-content-name {
    font-size: 5vw;
  }

  .PetInfo-content-kind {
    font-size: 3.5vw;
  }

  .PetInfo-content-content {
    font-size: 3vw;
  }


}

</style>
