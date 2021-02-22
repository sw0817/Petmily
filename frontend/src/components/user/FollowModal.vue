<template>
  <div style="font-family: GmarketSansMedium;" class="follow-modal-container" v-click-outside="onClickOutside">
    <div class="follow-title-header">
      <div class="follow-title-title">
        {{ title }}
      </div>
    </div>
      <div class="follow-modal-content">
        <div v-if="followList.length==0"><div style="font-family: GmarketSansMedium;">목록이 없습니다.</div></div>
        <div v-else
          v-for="(member) in followList"
          :key="member+'f'"
        >
          <div class="follow-member-info">
            <div class="follow-member-pet-img">
              <FollowUserInfo
                :myId="member"
              />
            </div>
            <button
              v-if="userId == myId && title == '팔로워'"
              class="follow-member-follow-cancle-btn"
              @click="checkFollowCancle(member)"
            >
              삭제
            </button>
            <div class="text-modal" v-if="simpleShow">
            <div class="text-modal-wrapper">
              <YesNoModal
                :contents="simpleModalContents"
                @modalOff="simpleOff"
                @deleteDiary="deleteDiary"
              />
            </div>
            </div>
            <button
              v-if="myFollowings.includes(member) && member != myId || myFollowings.includes(member) && title == '좋아요' && member != myId"
              class="follow-member-unfollow-btn"
              @click="unfollow(member)"
            >
              언팔로우
            </button>
            <button
              v-else-if="!myFollowings.includes(member) && member != myId"
              class="follow-member-follow-btn"
              @click="follow(member)"
            >
              팔로우
            </button>

          </div>
        </div>
      </div>
    <div class="follow-modal-footer">
      <div class="follow-modal-exit" @click="exit">
        확인
      </div>
    </div>
  </div>
</template>

<script>
import FollowUserInfo from './FollowUserInfo'
import YesNoModal from "../../components/common/YesNoModal"
export default {
  name: "FollowModal",
  components: {
    FollowUserInfo,
    YesNoModal
  },
  props: {
    followList: {
      type: Array,
    },
    title: {
      type: String,
    },
    myFollowings: {
      type: Array,
    },
    myId: {
      type: Number,
    },
    userId: {
      type: Number,
    },
  },
  data: function () {
    return {
      simpleModalContents:"",
      simpleShow:false,
      deleteNum:0,
    }
  },
  methods: {
    exit: function () {
      this.$emit("modalOff")
    },
    unfollow: function (member) {
      this.$emit("unfollow", member)
    },
    follow: function (member) {
      this.$emit("follow", member)
    },
    checkFollowCancle:function(member){
      this.simpleShow = !this.simpleShow
      this.simpleModalContents="팔로워를 삭제하시겠습니까?"
      this.deleteNum=member
    },
    followCancle: function  (member) {
      this.$emit("followCancle", member)
      //console.log("memememeem : "+member)
    },
    simpleOff: function () {
      this.simpleShow=false
    },
    deleteDiary: function () {
      this.simpleShow = !this.simpleShow
      this.followCancle(this.deleteNum)
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
.follow-modal-container {
  display: flex;
  flex-direction: column;
  background-color: white;
  width: 50vw;
  margin: 0 auto;
  border-radius: 2vw;
  box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
  transition: all .3s ease;
}

.follow-title-header {
  display: flex;
  justify-content: center;
  font-size: 2vw;
  font-weight: bold;
  color: black;
  padding: 1vw;
}

.follow-modal-content {
  display: flex;
  flex-direction: column;
  height: 40vh;
  overflow: hidden;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  border-top: 0.1vw solid gray;
  padding: 1vw 2vw;
  transition-duration: 0.5s;
}

.follow-member-info {
  display: flex;
  align-items: center;
  margin-bottom: 1vw;
}

.follow-member-name {
  font-size: 2vw;
  margin-left: 1vw;
  color: tomato;
}

.follow-member-follow-btn {
  font-size: 1.5vw;
  width: 10vw;
  background-color: white;
  border: 0.2vw solid mediumslateblue;
  border-radius: 0.5vw;
  color: mediumslateblue;
  margin-left: 1vw;
  transition-duration: 0.5s;
}

.follow-member-follow-btn:hover {
  background-color: mediumslateblue;
  border: 0.2vw solid mediumslateblue;
  color: white;
  transition-duration: 0.5s;
}

.follow-member-follow-cancle-btn {
  font-size: 1.5vw;
  width: 5vw;
  background-color: white;
  border: 0.2vw solid gray;
  border-radius: 0.5vw;
  color: gray;
  margin-left: 1vw;
  transition-duration: 0.5s;
}

.follow-member-follow-cancle-btn:hover {
  background-color: gray;
  border: 0.2vw solid gray;
  color: white;
  transition-duration: 0.5s;
}

.follow-member-unfollow-btn {
  font-size: 1.5vw;
  width: 10vw;
  background-color: white;
  border: 0.2vw solid gray;
  border-radius: 0.5vw;
  color: gray;
  margin-left: 1vw;
  transition-duration: 0.5s;
}

.follow-member-unfollow-btn:hover {
  background-color: gray;
  border: 0.2vw solid gray;
  color: white;
  transition-duration: 0.5s;
}

.follow-modal-footer {
  display: flex;
  justify-content: flex-end;
  font-size: 2vw;
  font-weight: bold;
  padding: 1vw;
  text-align: end;
}

.follow-modal-exit {
  margin: 0 3vw;
}

.follow-modal-exit:hover {
  cursor: pointer;
}

@media ( max-width: 540px ) {

  .follow-modal-container {
  width: 80vw;
  }
  
  .follow-title-header {
    font-size: 5vw;
  }
    
  .follow-modal-content {
    font-size: 5vw;
  }

  .follow-modal-footer {
    font-size: 5vw;
  }

  .follow-member-name {
    font-size: 4vw;
    margin-left: 2vw;
  }

  .follow-member-follow-btn {
    font-size: 3vw;
    width: 20vw;
    border: 0.4vw solid mediumslateblue;
    border-radius: 1vw;
    margin-left: 2vw;
  }

  .follow-member-follow-btn:hover {
    border: 0.4vw solid mediumslateblue;
  }

  
.follow-member-follow-cancle-btn {
    font-size: 3vw;
    width: 10vw;
    border: 0.4vw solid red;
    border-radius: 1vw;
    margin-left: 2vw;
  }

  .follow-member-follow-cancle-btn:hover {
    border: 0.4vw solid red;
  }

  .follow-member-unfollow-btn {
    font-size: 3vw;
    width: 20vw;
    border: 0.4vw solid gray;
    border-radius: 1vw;
    color: gray;
    margin-left: 2vw;
  }

  .follow-member-unfollow-btn:hover {
    border: 0.4vw solid gray;
  }

}


</style>
