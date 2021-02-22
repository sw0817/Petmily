<template>
  <div style="font-family: GmarketSansMedium;">
    <!-- 동물 정보 (여긴 이제 컴포넌트로 들어갈 듯) -->
    <div class="petInfo-container">
      <header class="petInfo">
        <div class="petInfo-img">
          <img src="https://picsum.photos/200" alt="">
        </div>
        <section class="petInfo-content">
          <div class="petInfo-title-container">
            <span class="petInfo-content-name2">
              아직 반려동물을 등록하지 않았어요.
            </span>
          </div>
        </section>
      </header>
    </div>
    <!-- 여기까지 동물 별 컴포넌트 -->
  </div>
</template>

<script>
export default {
  name: "NoPet",
  data: function () {
    return {

    }
  },
  methods: {
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

.petInfo-img {
  width: 25%;
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

.petInfo-content-name2 {
  font-size: 1.5vw;
}

.petInfo-content-edit {
  font-size: 3vw;
  margin-left: 0.5vw;
}

.petInfo-content-edit:hover {
  cursor: pointer;
}

</style>
