<template>
  <div style="font-family: GmarketSansMedium;" class="select-calendar-modal-container">
    <div class="select-calendar-title-header">
      <button class="select-calendar-title-btn" @click="prev">
        <i class="fas fa-chevron-left"></i>
      </button>
      <div class="select-calendar-title-title">
        {{ currentYear }}
      </div>
      <button class="select-calendar-title-btn" @click="next">
        <i class="fas fa-chevron-right"></i>
      </button>
    </div>
    <div class="exit-btn" @click="keydownEsc">X</div>
    <div class="select-calendar-modal-content">
      <div class="three-months">
        <div
          v-for="(month, idx) in firstThree"
          :key="idx + 'A'"
        >
          <div class="one-month" @click="select(month)">
            {{ month }}월
          </div>
        </div>
      </div>
      <div class="three-months">
        <div
          v-for="(month, idx) in secondThree"
          :key="idx + 'B'"
        >
          <div class="one-month" @click="select(month)">
            {{ month }}월
          </div>
        </div>
      </div>
      <div class="three-months">
        <div
          v-for="(month, idx) in thirdThree"
          :key="idx + 'C'"
        >
          <div class="one-month" @click="select(month)">
            {{ month }}월
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Selectselect-calendar",
  data: function () {
    return {
      currentYear: new Date().getFullYear(),
      firstThree: [
        1,
        2,
        3,
        4,
      ],
      secondThree: [
        5,
        6,
        7,
        8,
      ],
      thirdThree: [
        9,
        10,
        11,
        12,
      ],
    }
  },
  methods: {
    select: function (month) {
      const date = {
        "year": this.currentYear,
        "month": month
      }
      this.$emit("selectDate", date)
    },
    next: function () {
      this.currentYear += 1
    },
    prev: function () {
      this.currentYear -= 1
    },
    keydownEsc: function () {
      this.$emit("keydownEsc")
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
.select-calendar-modal-container {
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background-color: white;
  width: 50vw;
  margin: 0 auto;
  /* padding: 1vw; */
  border-radius: 1vw;
  box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
  transition: all .3s ease;
}

.exit-btn {
  position: absolute;
  font-size: 2vw;
  top: 1vh;
  right: 1.5vw;
}

.exit-btn:hover {
  cursor: pointer;
}

.select-calendar-title-header {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1vw;
}

.select-calendar-title-title {
  font-size: 3vw;
  font-weight: bold;
  margin: 0 1vw;
}

.select-calendar-title-btn {
  font-size: 3vw;
  font-weight: bold;
  color: tomato;
}

.select-calendar-modal-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 1.5vw;
  font-weight: bold;
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  /* padding: 3vw 1vw; */
  transition-duration: 0.5s;
}

.select-calendar-modal-footer {
  font-size: 1.5vw;
  font-weight: bold;
  padding: 1vw;
  text-align: end;
}

.modal-exit:hover {
  cursor: pointer;
}

.three-months {
  display: flex;
  width: 100%;
}

.three-months div {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
}

.one-month {
  width: 25%;
  padding: 10%;
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
  transition-duration: 0.5s;
}

.one-month:hover {
  cursor: pointer;
  background-color: wheat;
}

@media ( max-width: 540px ) {

  .select-calendar-modal-container {
    width: 80vw;
  }
  
  .exit-btn {
    position: absolute;
    font-size: 4vw;
    top: 1vh;
    right: 2vw;
  }

  .select-calendar-title-header {
    font-size: 5vw;
  }

  .select-calendar-title-title {
  font-size: 6vw;
  font-weight: bold;
  margin: 0 1vw;
  }

  .select-calendar-title-btn {
    font-size: 6vw;
    font-weight: bold;
  }
    
  .select-calendar-modal-content {
    font-size: 5vw;
  }

  .select-calendar-modal-footer {
    font-size: 5vw;
  }

}


</style>
