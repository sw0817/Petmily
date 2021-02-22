<template>
  <div class="calendar2">
    <div class="calendar-header2">
      <div class="select-year2">
        <div class="select-year2-btn" @click="onClickPrevYear">{{currentYear-1}}년</div>
        <div class="select-year2-btn" @click="onClickNextYear">{{currentYear+1}}년</div>
      </div>
      <div class="select-month2">
        <div class="calendar-btn" @click="onClickPrev(currentMonth)">◀</div>
        {{ currentYear }}년 {{ currentMonth }}월
        <div class="calendar-btn" @click="onClickNext(currentMonth)">▶</div>
      </div>
      <div class="select-this-month2">
        <div @click="onClickThisMonth">
          이번 달
        </div>
      </div>
    </div>

    <div class="calendar-table">
      <div class="calendar-days-container">
        <div class="calendar-week-row" v-for="(row, index) in currentCalendarMatrix" :key="index">
          <div class="calendar-table-day2" v-for="(day, index2) in row" :key="index2">
            <div class="calendar-table-day2-num" @click="writeDiary(day)">
              {{ day }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Calendar',
  data: function () {
    return {
      weekNames: ["월요일", "화요일", "수요일","목요일", "금요일", "토요일", "일요일"],
      rootYear: 1904,
      rootDayOfWeekIndex: 4,
      currentYear: new Date().getFullYear(),
      currentMonth: new Date().getMonth()+1,
      currentDay: new Date().getDate(),
      currentMonthStartWeekIndex: null,
      currentCalendarMatrix: [],
      endOfDay: null,
      // memoDatas: [],
    }
  },
  mounted: function () {
      this.init();
  },
  methods: {
      init: function () {
        this.currentMonthStartWeekIndex = this.getStartWeek(this.currentYear, this.currentMonth);
        this.endOfDay = this.getEndOfDay(this.currentYear, this.currentMonth);
        this.initCalendar();
      },
      initCalendar: function () {
        this.currentCalendarMatrix = [];
        let day=1;
        for (let i=0; i<6; i++) {
          let calendarRow = [];
          for (let j=0; j<7; j++) {
            if (i==0 && j<this.currentMonthStartWeekIndex) {
              calendarRow.push("");
            }
            else if (day<=this.endOfDay) {
              calendarRow.push(day);
              day++;
            }
            else {
              calendarRow.push("");
            }
          }
          this.currentCalendarMatrix.push(calendarRow);
        }
      },
      getEndOfDay: function (year, month) {
          switch (month) {
              case 1:
              case 3:
              case 5:
              case 7:
              case 8:
              case 10:
              case 12:
                return 31;
              case 4:
              case 6:
              case 9:
              case 11:
                return 30;
              case 2:
                if ( (year%4 == 0) && (year%100 != 0) || (year%400 == 0) ) {
                return 29;   
                }
                else {
                    return 28;
                }
              default:
                // console.log("unknown month " + month);
                return 0;
          }
      },
      getStartWeek: function (targetYear, targetMonth) {
        let year = this.rootYear;
        let month = 1;
        let sumOfDay = this.rootDayOfWeekIndex;
        let w = 1
        while (w) {
          if (targetYear > year) {
            for (let i=0; i<12; i++) {
              sumOfDay += this.getEndOfDay(year, month+i);
            }
            year++;
          }
          else if (targetYear == year) {
            if (targetMonth > month) {
              sumOfDay += this.getEndOfDay(year, month);
              month++;
            }
            else if (targetMonth == month) {
              return (sumOfDay)%7;
            }
          }
        }
      },
      onClickPrev: function (month) {
        month--;
        if (month<=0) {
          this.currentMonth = 12;
          this.currentYear -= 1;
        }
        else {
          this.currentMonth -= 1;
        }
        this.init();
      },
      onClickNext: function (month) {
        month++;
        if (month>12) {
          this.currentMonth = 1;
          this.currentYear += 1;
        }
        else {
          this.currentMonth += 1;
        }
        this.init();
      },
      onClickPrevYear: function () {
        this.currentYear -= 1;
        this.init();
      },
      onClickNextYear: function () {
        this.currentYear += 1;
        this.init();
      },
      onClickThisMonth: function () {
        let thisYear = new Date().getFullYear()
        let thisMonth = new Date().getMonth()+1
        this.currentYear = thisYear
        this.currentMonth = thisMonth
        this.init()
      },
      writeDiary: function (day) {
        const date = {
          "year": this.currentYear,
          "month": this.currentMonth,
          "day": day,
        }
        this.$emit("choiceDate", date)
      },
      isToday: function (year, month, day) {
        let date = new Date();
        return year == date.getFullYear() && month == date.getMonth()+1 && day == date.getDate(); 
      },
  },
}
</script>

<style type="text/css">

.calendar2 {
  border-left: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.calendar-header2 {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
  padding: 1vw 0;
}

.calendar-btn:hover {
  cursor: pointer;
}

.select-year2 {
  display: flex;
  align-items: center;
  font-size: 1vw;
  width: 25%;
  margin-left: 3%;
}

.select-year2-btn {
  margin-right: 1vw;
}

.select-year2-btn:hover {
  cursor: pointer;
}

.select-month2 {
  display: flex;
  align-items: center;
  font-size: 1vw;
}

.select-this-month2 {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  font-size: 1vw;
  width: 25%;
  margin-right: 3%;
}

.select-this-month2 div:hover {
  cursor: pointer;
}

.calendar-table {
  display: flex;
  flex-direction: column;
}

.calendar-day-names {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.calendar-day-name {
  width: 14.28%;
  font-size: 1.2vw;
  padding: 0.5vw 0;
  /* padding-left: 1vw; */
  text-align: center;
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.calendar-week-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.calendar-days-container {
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.calendar-table-day2 {
  position: relative;
  width: 14.28%;
  height: 4vh;
  padding: 0.5vw 0;
  padding-left: 1vw;
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-right: 0.1vw solid rgba(175, 175, 175, 0.5);
  transition-duration: 0.5s;
}

.calendar-table-day2:hover {
  background-color: mediumslateblue;
}

.calendar-table-day2-num {
  position: absolute;
  font-size: 1.2vw;
  top: 5%;
  right: 5%;
}

.calendar-table-day2-num:hover {
  cursor: pointer;
}

.write-diary-btn {
  margin-left: 2vw;
}

</style>
