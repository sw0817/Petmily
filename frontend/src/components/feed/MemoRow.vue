<template>
  <div style="font-family: GmarketSansMedium;">
    <div>
      <div v-if="isShow" class="feed-comment-container">
        <div class="feed-comment-user-img-div">
          <img
            @click="moveToProfile"
            class="feed-comment-user-img"
            :src="petImgUrl[0]"
          >
        </div>
        <div class="feed-comment-content-div">
          <div class="feed-comment-content-header">
            <div class="feed-comment-header-nickname" @click="moveToProfile">
              {{ this.nickName }}
            </div>
            <!-- 수정 삭제 (작성자만)-->
            <div v-if="this.memo.user_id == this.userId" class="feed-comment-header-edit">
              <button class="feed-comment-header-edit-btn" @click="showMyModify">
                수정
              </button>
              <button class="feed-comment-header-delete-btn" @click="deleteMyMemo">
                삭제
              </button>
            </div>
          </div>
          <div class="feed-comment-content-content">
            <span v-if="isShow" class="feed-comment-content-comment">
              {{ this.memo.comment }}
            </span>
            <span class="feed-comment-content-date">
              {{returnedTime_ys}}
            </span>
          </div>
        </div>
        <!-- <b-col class="ml-3 text-interval">
          <span class="petInfo-img2">
            <img :src="petImgUrl[0]" alt="">
          </span>
          <strong style="padding-left:1vw">{{ this.nickName }}</strong> 
          &nbsp;
          <span class="memo-date"> {{returnedTime_ys}} </span>
        </b-col>
        <b-col
          class="text-right mr-3"
          v-if="this.memo.user_id == this.userId"
        >
          <span v-if="this.ok">
            <b-button
              variant="link" 
              @click="showMyModify"
            >
              수정
            </b-button>
            <b-button
              variant="link" 
              @click="deleteMyMemo"
            >
              삭제
            </b-button>
          </span>
        </b-col> -->
      </div>
      <div v-else class="feed-comment-container">
        <div class="feed-comment-modify-div">
          <input
            type="text"
            class="feed-comment-modify-input"
            v-model="modiMemo.comment"
            maxlength="200"
            placeholder="댓글을 입력하세요."
          >
        </div>
        <div class="feed-comment-header-edit">
          <button @click="modifyMyMemo" class="feed-comment-header-edit-btn">
            수정
          </button>
          <button @click="cancelMyModify" class="feed-comment-header-edit-btn">
            취소
          </button>
        </div>
      </div>
      <!-- <b-card-body class="change-location">
        <div v-if="isShow">
          <span>{{ this.memo.comment }} </span>
        </div>
        <b-row class="mb-3 mt-2" v-else>
          <b-col cols="10">
            <div class="login-content-email-input-div">
              <input
                class="login-content-email-input-memo"
                v-model="modiMemo.comment"
                id="comment"
                v-text="this.memo.comment"
                type="text"
              />
            </div>
          </b-col>
          <span
            ><b-button
              style="height: 80px; border:0px"
              variant="outline-primary"
              @click="modifyMyMemo"
              >수정</b-button
            >
            <b-button
              variant="outline-danger"
              style="height: 80px; border:0px"
              @click="cancelMyModify"
              >취소</b-button
            >
          </span>
        </b-row>
      </b-card-body> -->
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { modifyMemo, deleteMemo, getPetInfo, getAllMember } from "@/api/memo.js";
import moment from 'moment/moment'

export default {
  name: "memorow",
  data() {
    return {
      modiDate: "",
      modiMemo: {
        memo_id: 0,
        comment: "",
        feed_id: 0
      },
      isShow: true,
      petInfo:[],
      petImgUrl:[],
      AllMember:[],
      nickName:"",
      ok:true,
    };
  },
  props: {
    memo: Object,
    feed_id: Number,
    reloadMemos: Function,
    feed_nickname: String,
  },
  computed: {
    ...mapState(["userId"])
    ,
    returnedTime_ys: function () {
      const today = moment(new Date(),'YYYY.MM.DD HH:mm:ss').toDate();
      const timeValue = moment((this.memo.create_date),'YYYY.MM.DD HH:mm:ss').toDate();
      const betweenTime = Math.floor((today-timeValue) / 1000 / 60);

      if (betweenTime < 1) return '방금전';
      if (betweenTime < 60) {
          return `${betweenTime}분전`;
      }

      const betweenTimeHour = Math.floor(betweenTime / 60);
      if (betweenTimeHour < 24) {
          return `${betweenTimeHour}시간전`;
      }

      const betweenTimeDay = Math.floor(betweenTime / 60 / 24);
      if (betweenTimeDay < 365) {
          return `${betweenTimeDay}일전`;
      }

      return `${Math.floor(betweenTimeDay / 365)}년전`;
    },
    
  },
  methods: {
    moveToProfile: function () {
      this.$router.push({ name: 'UserProfile', params: { user_id: `${this.memo.user_id}` } })
    },
    showMyModify: function() {
      this.ok=false;
      this.isShow = false;
    },
    modifyMyMemo: function() {
      // this.modiMemo.comment = this.memo.comment
      this.modiMemo.feed_id = this.feed_id;
      this.modiMemo.memo_id = this.memo.memo_id;
      this.isShow = true;
      this.ok=true;
      modifyMemo(
        this.modiMemo,
        (response) => {
          this.reloadMemos(response.data);
        },
        (error) => {
          console.log(error);
        }
      );
    },
    cancelMyModify() {
      this.ok = true
      this.isShow = true;
    },
    deleteMyMemo() {
      // console.log(this.memo.memo_id);
      // 추후에 reloadMeos와 같이 사진과 닉네임을 바꿔야 할 것 같음
      //  this.reloadMemos(response.data);
      deleteMemo(
        { feed_id: this.feed_id, memo_id: this.memo.memo_id },
        (response) => {
              getPetInfo(
            (response) => {
              this.petInfo = response.data; 
              this.petImgUrl.push("http://i4a403.p.ssafy.io/images/basicPet.png")
              // this.petImgUrl.push(require("@/assets/images/petImg/lHOREmRvISbmLOvGjB4OBtDXAjraXDgl.jpg" )) // 기본 이미지  
              for(let i = 0; i<this.petInfo.length; i++){
                if(this.petInfo[i].user_id == this.memo.user_id){
                  this.petImgUrl.splice(0, 1); // 기본 이미지 삭제
                  this.petImgUrl.push("http://i4a403.p.ssafy.io/images/" + this.petInfo[i].file_name)
                  // this.petImgUrl.push(require("@/assets/images/petImg/" + this.petInfo[i].file_name));
                  break;
                }
              }
              // console.log(this.petImgUrl + " 첫번째 펫 사진")
              getAllMember(
                (response) => {
                  this.AllMember = response.data;
                  // console.log(this.AllMember[0].user_id + "모든 멤보")
                  for(let i=0; i<this.AllMember.length; i++){
                    if(this.AllMember[i].user_id == this.memo.user_id){
                      this.nickName = this.AllMember[i].user_nickname;
                      // console.log(this.nickName);
                      break;
                    }
                  }
                  this.reloadMemos(response.data);
                }
              );
            },
            (error) => {
              console.log(error)
            }
          );
        },
        (error) => {
          console.log(error);
        }
      );
    }
  },
  created: function(){
    getPetInfo(
      (response) => {
        this.petInfo = response.data; 
        this.petImgUrl.push( "http://i4a403.p.ssafy.io/images/basicPet.png")
        // this.petImgUrl.push(require("@/assets/images/petImg/1ynZESZexD3lBAnU0GgaTx0U7xLQAfMJ.jpg" )) // 기본 이미지  

        for(let i = 0; i<this.petInfo.length; i++){
          if(this.petInfo[i].user_id == this.memo.user_id){
           this.petImgUrl.splice(0, 1); // 기본 이미지 삭제
           this.petImgUrl.push("http://i4a403.p.ssafy.io/images/" + this.petInfo[i].file_name);
          //  this.petImgUrl.push(require("@/assets/images/petImg/" + this.petInfo[i].file_name));
            break;
          }
        }
      },
      (error) => {
        console.log(error)
      }
    );
    getAllMember(
      (response) => {
        this.AllMember = response.data;
        for(let i=0; i<this.AllMember.length; i++){
          if(this.AllMember[i].user_id == this.memo.user_id){
            this.nickName = this.AllMember[i].user_nickname;
            // console.log(this.nickName);
            break;
          }
        }
      }
    );
  }
};
</script>

<style>
@font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
.feed-comment-container {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  padding: 5px;
}

.feed-comment-modify-div {
  width: 80%;
}

.feed-comment-modify-input {
  border: none;
  border-bottom: 1px solid black;
  width: 100%;
  font-size: 15px;
}

.feed-comment-modify-input:focus {
  outline: none;
}

.feed-comment-user-img-div {
  display: flex;
  justify-content: center;
  width: 10%;
  min-width: 35px;
  height: 35px;
}

.feed-comment-user-img {
  border-radius: 70%;
  width: 35px;
  height: 35px;
}

.feed-comment-user-img:hover {
  cursor: pointer;
}

.feed-comment-content-div {
  display: flex;
  flex-direction: column;
  width: 90%;
}

.feed-comment-content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.feed-comment-header-nickname {
  font-size: 18px;
  font-weight: bold;
  color: black;
}

.feed-comment-header-nickname:hover {
  cursor: pointer;
}

.feed-comment-header-edit-btn {
  font-size: 16px;
  margin-left: 10px;
  color: gray;
}

.feed-comment-header-delete-btn {
  font-size: 16px;
  margin-left: 10px;
  color: red;
}

.feed-comment-content-content {
  width: 100%;
}

.feed-comment-content-comment {
  font-size: 17px;
  color: black;
}

.feed-comment-content-date {
  font-size: 17px;
  color: gray;
}

.text-interval{
  margin: -1vw;
  left: -1vw;
}

.petInfo-img2 img {
  border-radius: 70%;
  width: 3vw;
  height: 3vw;
  margin-left: 1%;
}
.memo-right{
  right:10vw;
}
.memo-date{
  padding-right: 2vw;
  color: gray;
}
.change-location{
  position: relative;
  top: -13px;
  left: 60px;
}

@media (max-width: 1100px) {

  .feed-comment-modify-div {
    width: 80%;
  }

  .feed-comment-modify-input {
    border: none;
    border-bottom: 1px solid black;
    width: 100%;
    font-size: 1.5vw;
  }

  .feed-comment-modify-input:focus {
    outline: none;
  }

  .feed-comment-user-img-div {
    display: flex;
    justify-content: center;
    width: 10%;
    min-width: 3vw;
    height: 3vw;
  }

  .feed-comment-user-img {
    border-radius: 70%;
    width: 3vw;
    height: 3vw;
  }

  .feed-comment-content-div {
    display: flex;
    flex-direction: column;
    width: 90%;
  }

  .feed-comment-content-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .feed-comment-header-nickname {
    font-size: 1.5vw;
    font-weight: bold;
    color: black;
  }

  .feed-comment-header-edit-btn {
    font-size: 1.3vw;
    margin-left: 1vw;
    color: gray;
  }

  .feed-comment-header-delete-btn {
    font-size: 1.3vw;
    margin-left: 1vw;
  }

  .feed-comment-content-content {
    width: 100%;
  }

  .feed-comment-content-comment {
    font-size: 1.3vw;
    color: black;
  }

  .feed-comment-content-date {
    font-size: 1.3vw;
    color: gray;
  }

}

@media (max-width: 540px) {

  .feed-comment-modify-input {
    border: none;
    border-bottom: 1px solid black;
    width: 100%;
    font-size: 3.5vw;
  }

  .feed-comment-modify-input:focus {
    outline: none;
  }

  .feed-comment-user-img-div {
    display: flex;
    justify-content: center;
    width: 10%;
    min-width: 6vw;
    height: 6vw;
  }

  .feed-comment-user-img {
    border-radius: 70%;
    width: 6vw;
    height: 6vw;
  }

  .feed-comment-header-nickname {
    font-size: 3.5vw;
    font-weight: bold;
    color: black;
  }

  .feed-comment-header-edit-btn {
    font-size: 3vw;
    margin-left: 1vw;
    color: gray;
  }

  .feed-comment-header-delete-btn {
    font-size: 3vw;
    margin-left: 1vw;
  }

  .feed-comment-content-content {
    width: 100%;
  }

  .feed-comment-content-comment {
    font-size: 3.5vw;
    color: black;
  }

  .feed-comment-content-date {
    font-size: 3vw;
    color: gray;
  }

}


</style>
