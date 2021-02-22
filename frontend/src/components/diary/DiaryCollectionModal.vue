<template>
  <div style="font-family: GmarketSansMedium;" class="collection-modal-container">
    <div class="collection-modal-title-header">
      <button @click="prev">◀</button>
      <div class="collection-modal-title-title">
        {{ currentYear }}. {{ currentMonth }}
      </div>
      <button @click="next">▶</button>
    </div>
    <div class="modal-collection-contents">
      <div v-for="(diary, idx) in diaries" :key="idx">
          <div v-if="diary.diary_year == currentYear && diary.diary_month == currentMonth" class="collection-modal-content" @click="selectDiary(diary)">
            <span style="color: orange;">
              {{ diary.diary_day }}일 
            </span>
            <span>
             {{ diary.diary_title }}
            </span>
          </div>
      </div>
    </div>
    <div class="collection-modal-footer">
      <div class="collection-modal-exit" @click="exit">
        닫기
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "DiaryCollectionModal",
  props: {
    diaries: {
      type: Array,
    },
  },
  data: function () {
    return {
      currentYear: new Date().getFullYear(),
      currentMonth: new Date().getMonth()+1,
    }
  },
  methods: {
    exit: function () {
      this.$emit("exit")
    },
    selectDiary: function (diary) {
      this.$emit("selectDiary", diary)
    },
    prev: function () {
      if (this.currentMonth == 1) {
        this.currentMonth = 12
        this.currentYear -= 1
      } else {
        this.currentMonth -= 1
      }
    },
    next: function () {
      if (this.currentMonth == 12) {
        this.currentMonth = 1
        this.currentYear += 1
      } else {
        this.currentMonth += 1
      }
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
.collection-modal-container {
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

/* .collection-modal-title-title {

} */

.collection-modal-title-header {
  display: flex;
  align-items: center;
  font-size: 1.5vw;
  font-weight: bold;
  color: rgb(251, 75, 75);
  padding: 1vw;
}

.collection-modal-content {
  font-size: 1.5vw;
  font-weight: bold;
  border-top: 0.1vw solid rgb(251, 75, 75);
  padding: 1vw;
  transition-duration: 0.5s;
}

.collection-modal-content:hover {
  cursor: pointer;
  background-color: rgb(251, 75, 75);
}

.collection-modal-footer {
  font-size: 1.5vw;
  font-weight: bold;
  padding: 1vw;
  text-align: end;
}

.collection-modal-exit:hover {
  cursor: pointer;
}

.modal-collection-contents {
  display: flex;
  flex-direction: column;
}

.modal-collection-contents-div {
  display: flex;
  align-items: center;
}

@media ( max-width: 540px) {

  .collection-modal-container {
    width: 90vw;
    border-radius: 3vw;
    box-shadow: 0 1vw 4vw rgba(0, 0, 0, 0.33);
  }

  .collection-modal-title-header {
    font-size: 5vw;
    padding: 2vw;
  }

  .collection-modal-content {
    font-size: 4vw;
    padding: 1vw;
  }

  .collection-modal-footer {
    font-size: 4vw;
    padding: 2vw;
  }

}

</style>
