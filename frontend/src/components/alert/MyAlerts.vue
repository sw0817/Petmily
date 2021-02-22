<template>
  <div style="font-family: GmarketSansMedium;" class="simple-modal-container1">
    <div class="simple-title-header1">
      <button class="delete-all-button" @click="deleteAllAlerts">
        모두 지우기
      </button>
    </div>
    <div class="simple-title-header2">
      <div class="simple-title-header2-child">            
        <button
          :class="{selectButtonOn1: feedButton, selectButtonOff1: !feedButton}"
          @click="clickFeed"
        >
          게시글
        </button>
      </div>
      <div class="simple-title-header2-child">            
        <button
          :class="{selectButtonOn1: followButton, selectButtonOff1: !followButton}"
          @click="clickFollow"
        >
          팔로우
        </button>
      </div>
    </div>
    <div v-if="followButton">
      <div class="alert-nothing" v-if="followList.length==0" >새로운 알림이 없습니다.</div>
      <div class="alert-nothing" v-else v-for="(follow,index) in followList" v-bind:key="index">
        <div class="follow-member-name2" @click="userProfile(follow,index)">{{follow.from_nickname}}</div>
        <div class="alert-member-content">
          <div class="follow-member-name2">
            {{follow.alert_content}} 
          </div>
          <div style="display:inline-block;"><button class="follow-member-unfollow-btn2" style="display:inline-block; margin-left: 2px;" @click="deleteFollowAlert(index)">✓</button></div>
        </div>
      </div>
    </div>
    <div v-else>
      <div class="alert-nothing" v-if="feedList.length==0" >새로운 알림이 없습니다.</div>
      <div class="alert-nothing" v-else v-for="(feed,index) in feedList" v-bind:key="index">
        <div class="alert-member-content" @click="moveToFeed(feed)"> <!-- 클릭하면 해당 게시물로 이동.. -->
          <div class="follow-member-name2">
            {{feed.from_nickname}} {{feed.alert_content}} <div style="display:inline-block;"><button class="follow-member-unfollow-btn2" style="display:inline-block;" @click="deleteFeedAlert(index)">✓</button></div>
          </div>
          <div class="follow-member-name2" v-if="feedList[index].alert_kind==1"> <!-- 태그 알림 -->
            <div v-if="feed.feed_content.length < 30">
              :{{feed.feed_content}}
            </div>
            <div v-else>
              :{{feed.feed_content.slice(0, 30)}}...
            </div>
          </div>
          <div class="follow-member-name2" v-else-if="feedList[index].alert_kind==2"> <!-- 댓글 입력 알림 -->
            <div v-if="feed.comment.length < 30">
              :{{feed.comment}}
            </div>
            <div v-else>
              :{{feed.comment.slice(0, 30)}}...
            </div>
          </div>
        </div>
        
      </div>
    </div>
    <div class="simple-modal-footer1">
      <div class="modal-exit" @click="exit" @keyup.enter="exit">
        닫기
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
const SERVER_URL = process.env.VUE_APP_SERVER_URL;
export default {
  name: "MyAlerts",
  props: {
    user_id: { //alert_to가 user_id
      type: Number,
    },
  },
  data: function () {
    return {
      feedButton: true,
      followButton: false,
      feedList:this.$store.getters.getAlertUnreadFeed,
      followList:this.$store.getters.getAlertUnreadFollow,
    }
  },
  created(){
    //맨 처음 시작할 때 내 알림들을 각 리스트에 넣어줘야함
    //setInterval(()=>this.getAlertList(),60000)
  },
  methods: {
    moveToFeed(feed){ //알림 클릭 시 해당 게시물 상세페이지로 이동하게! + feed_id 관련 알림 전부 삭제

      axios.delete(`${SERVER_URL}/alert/clickFeedAlert/${feed.feed_id}/${feed.alert_to}`)
      .then(({res}) => {
        //다시 feedList 갱신
          axios.post(`${SERVER_URL}/alert/feed/${this.user_id}`)
          .then(({data}) => {
            this.feedList=data
            this.$router.push({ name: "ReadFeed", params: {feed_id: feed.feed_id } })
          })
      })

      
    },
    deleteFollowAlert(index){ //확인버튼 누르면 해당 알림만 삭제! -> list에서도 remove해줘야함
      axios.delete(`${SERVER_URL}/alert/deletealert/${this.followList[index].alert_id}`)
      .then(({data}) => {
        this.$delete(this.followList, index)
        this.$store.dispatch('setAlertUnreadFeed', this.followList);
      })
    },
    deleteFeedAlert(index){
      axios.delete(`${SERVER_URL}/alert/feeddeletealert/${this.feedList[index].alert_id}`)
      .then(({data}) => {
      //console.log("해당 알림 삭제")
        this.$delete(this.feedList, index)
        this.$store.dispatch('setAlertUnreadFeed', this.feedList);
      })
    },

    userProfile(follow,index){ //그 사람 프로필 페이지로 이동
      axios.delete(`${SERVER_URL}/alert/deletealert/${this.followList[index].alert_id}`)
        .then(({data}) => {
          this.$delete(this.followList, index)
          this.$store.dispatch('setAlertUnreadFollow', this.followList);
          this.$router.push({ name: 'UserProfile', params: { user_id: `${follow.from_id}` } })
        })
      
    },

    exit: function () {
      this.$emit("modalOff")
    },
    clickFollow: function () {
      this.followButton = true
      this.feedButton = false
      //팔로우 관련 알림리스트 띄워주기
    },
    clickFeed: function () {
      this.followButton = false
      this.feedButton = true
      //피드 관련 알림리스트 띄워주기
    },
    deleteAllAlerts:function(){ //모든 알람 다 지우기
      axios.delete(`${SERVER_URL}/alert/deleteall/${this.user_id}`)
      .then(({data}) => {
        this.followList=[]
        this.feedList=[]
        this.$store.dispatch('setAlertUnreadFollow', this.followList);
        this.$store.dispatch('setAlertUnreadFeed', this.feedList);
      })
      .catch( (err) => {
        console.log(err)
      })

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
.follow-member-name2 {
  font-size: 1vw;
  margin-left: 0.5vw;
  color: black;
  display:inline-block;
}

.follow-member-name2:hover {
  cursor: pointer;
}

.alert-member-content {
  display:inline-block;
  margin:5px 0;
  font-size: 1.5vw;
}

.follow-member-unfollow-btn2 {
  display:flex;
  font-size: 0.5vw;
  width: 2vw;
  margin-right:0.2vw;
  background-color: white;
  border: 0.1vw solid gray;
  border-radius: 0.5vw;
  color: gray;
  transition-duration: 0.5s;
}

.alert-nothing{
  padding-top:1vw; 
  padding-left:1vw;
  font-size: 1.5vw;
  max-height: 900px;
  overflow: hidden;
  overflow-y: auto;
}

.simple-title-header2-child{
  flex:1;
  text-align: center;
}

.modify-certificationBtn-off2 {
  width: 20%;
  margin-left: 5%;
  padding: 0.5vw;
  border-radius: 1vw;
  border: 0.3vw solid;
  border-color: gray;
  background-color: white;
  color:gray;
  font-size: 1.3vw;
  font-weight: bold;
  transition-duration: 0.5s;
  display: flex;
  justify-content: center;
  align-items: center;
}

.simple-title-title1{
  color: rgb(93, 26, 0);
}

.simple-modal-container1 {
  display: flex;
  flex-direction: column;
  background-color: white;
  width: 50vw;
  max-width: 400px;
  margin: 0 auto;
  /* padding: 1vw; */
  border-radius: 1vw;
  box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
  transition: all .3s ease;
}

.simple-title-header1 {
  font-size: 1.0vw;
  font-weight: bold;
  text-align:right;
  color: black;
  padding: 1vw;
}

.simple-title-header2 {
  display: flex;
  font-size: 1.2vw;
  font-weight: bold;
  text-align:center;
  color: rgb(93, 26, 0);
}

.simple-modal-footer1 {
  font-size: 1.2vw;
  padding-top: 1vw;
  text-align: end;
}

.selectButtonOn1 {
  width: 100%;
  margin-top: 0.1vw;
  background-color:rgb(93, 26, 0, 0.9);
  color: white;
  /* border-top: 0.2vw solid; */
  border-bottom: 0.2vw solid;
  border-color:rgb(93, 26, 0);
  /* font-weight: bold; */
  font-size: 1.3vw;
  padding: 0.5vw;
  /* border-radius: 1vw; */
  transition-duration: 0.5s;
}

.selectButtonOff1 {
  width: 100%;
  margin-top: 0.1vw;
  background-color: white;
  color:rgb(93, 26, 0);
  /* border-top: 0.2vw solid; */
  border-bottom: 0.2vw solid;
  border-color:rgb(93, 26, 0);
  /* font-weight: bold; */
  font-size: 1.3vw;
  padding: 0.5vw;
  /* border-radius: 1vw; */
  transition-duration: 0.5s;
}
.selectButtonOff1:hover {
  background-color:rgb(93, 26, 0,0.9);
  color: white;
}

.delete-all-button:hover {
  border-bottom: 0.1vw solid;
  border-color:black;
}

.modal-exit:hover {
  cursor: pointer;
}

.userInfo-follow-btn-following1 {
  width: 100%;
  padding: 1vw;
  border-radius: 1vw;
  border: 0.3vw solid;
  border-color: gray;
  background-color: white;
  color:gray;
  font-size: 1.5vw;
  font-weight: bold;
  transition-duration: 0.5s;
}


@media ( max-width: 540px ) {

  .follow-member-name2 {
    font-size: 2vw;
    margin-left: 0.5vw;
  }

  .follow-member-unfollow-btn2 {
    display:flex;
    font-size: 1vw;
    width: 4vw;
    margin-right:0.2vw;
    background-color: white;
    border: 0.1vw solid gray;
    border-radius: 0.5vw;
    color: gray;
    transition-duration: 0.5s;
  }

  .alert-nothing {
    padding-top:1vw; 
    padding-left:1vw;
    font-size: 3vw;
  }
  
  .alert-member-content {
    display:inline-block;
    margin:2px;
  }

  .simple-title-header2-child{
    flex:1;
    text-align: center;
  }

  .simple-title-title1{
    color: rgb(93, 26, 0);
  }

  .simple-modal-container1 {
    display: flex;
    flex-direction: column;
    background-color: white;
    width: 70vw;
    margin: 0 auto;
    /* padding: 1vw; */
    border-radius: 1vw;
    box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
    transition: all .3s ease;
  }

  .simple-title-header1 {
    font-size: 1.0vw;
    font-weight: bold;
    text-align:right;
    color: black;
    padding: 1vw;
  }

  .simple-title-header2 {
    display: flex;
    font-size: 1.2vw;
    font-weight: bold;
    text-align:center;
    color: rgb(93, 26, 0);
  }

  .simple-modal-content1 {
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 1.5vw;
    font-weight: bold;
    /* border-top: 0.1vw solid rgb(93, 26, 0); */
    padding: 3vw 1vw;
    transition-duration: 0.5s;
  }

  .simple-modal-footer1 {
    font-size: 1.2vw;
    padding-top: 1vw;
    text-align: end;
  }

  .selectButtonOn1 {
    width: 100%;
    margin-top: 0.1vw;
    background-color:rgb(93, 26, 0, 0.9);
    color: white;
    /* border-top: 0.2vw solid; */
    border-bottom: 0.2vw solid;
    border-color:rgb(93, 26, 0);
    /* font-weight: bold; */
    font-size: 1.3vw;
    padding: 0.5vw;
    /* border-radius: 1vw; */
    transition-duration: 0.5s;
  }

  .selectButtonOff1 {
    width: 100%;
    margin-top: 0.1vw;
    background-color: white;
    color:rgb(93, 26, 0);
    /* border-top: 0.2vw solid; */
    border-bottom: 0.2vw solid;
    border-color:rgb(93, 26, 0);
    /* font-weight: bold; */
    font-size: 1.3vw;
    padding: 0.5vw;
    /* border-radius: 1vw; */
    transition-duration: 0.5s;
  }

}


</style>