<template>
<div class="chatroom">
  <div class="chatroomhead">
    <div class="chatimg">
      <img @click="moveToFeedMain" class="chat-img" src="@/assets/images/petchat.png">
    </div>
    <div class="chathead"></div>
    <button class="gotofeed" @click="moveToFeedMain">이전</button>
  </div> 
   
<!-- 여기서 내가 보낸게 가장 최근일수도(1), 남이 보낸게 가장 최근일수도(2) 있음
    1인 경우 : -->
  <div v-if="chatList.length == 0" class="boxbox">
    <div class="chatListNotice2" @click="moveToFeedMain">
      아직 대화를 시작한 상대가 없어요.
    </div>
  </div>
  <div v-else class="chatroom">
    <div class="chatListNotice">
      대화 목록
    </div>
    <div class="boxbox" v-for="(chat, idx) in chatList" :key="idx">
      <div class="box" @click="goRoom(idx)">
        <div class="flex-col">
          <div class="who">{{roomNames[idx]}} 님과의 대화방</div>
          <div v-if="chat.body.length < 25" class="room"> {{chat.body}} </div>
          <div v-else class="room"> {{chat.body.slice(0, 25)}}... </div>
        </div>
        <div class="msgsuc" v-if="chatList[idx].send_id==myId">  </div>
        <div class="notread" v-else-if="!chatList[idx].isread">  </div>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import axios from "axios";

const SERVER_URL = process.env.VUE_APP_SERVER_URL;
export default {

  name: "MyChatRoom",
  components: {
  },
    props: {
    user_id: {
      type: Number,
    },
  },

data: function () {
  return {
    myId: this.$route.params.user_id,
    myNickname:"",
    chatList:[],
    roomNames:[],
    yourId:0, //개인 채팅방으로 들어갈 때 쓸 파라미터용?
  }           
},
created(){
  this.getMyInfo();
},
methods:{
  moveToFeedMain: function () {
      if (this.$route.path == '/feed/main') {
        this.$router.go(this.$router.currentRoute)
      } else {
        this.$router.push({ name: 'FeedMain'})
      }
    },
  getMyInfo(){
      axios.post(`${SERVER_URL}/member/getsinglemember/${this.myId}`)
        .then(({data}) => {
          this.myNickname=data.user_nickname
          this.getMyList();
        })
  },
  getMyList(){ //내 채팅목록(가장 최근 내용만) 가져오기
    if (this.$store.getters.getMessageUnread.length < 1) {
      axios.post(`${SERVER_URL}/chat/mychatlist/${this.myId}`)
        .then(({data}) => {
          this.chatList=data
          for(var i = 0;i<this.chatList.length;i++){
            /*누구와의 채팅방인지 : 
              roomNames를 따로만든건... 마지막 내용의 보낸이가 "나"일수도 "남"일수도 있기 때문!! 인덱스는 chatList와 같다
              하지만 화면에 보이는 건 ~~님과의 채팅방 이니까 결국 "남"이 나와야 한다 
            */
            if(this.chatList[i].send_id==this.myId){ //마지막으로 보낸사람이 나면 -> send_nickname receive_nickname 해서 출력해야댐
              this.roomNames.push(this.chatList[i].receive_nickname)
            }else{
              this.roomNames.push(this.chatList[i].send_nickname)
            }
            //버튼식으로 클릭해서 사람 프로필에 갈때는 chatList.send_id를 user_id처럼해서 보내면 된다!!!!!!!
          }
    })} else {
      this.chatList=this.$store.getters.getMessageUnread
      for(var i = 0;i<this.chatList.length;i++){
          /*누구와의 채팅방인지 : 
            roomNames를 따로만든건... 마지막 내용의 보낸이가 "나"일수도 "남"일수도 있기 때문!! 인덱스는 chatList와 같다
            하지만 화면에 보이는 건 ~~님과의 채팅방 이니까 결국 "남"이 나와야 한다 
          */
          if(this.chatList[i].send_id==this.myId){ //마지막으로 보낸사람이 나면 -> send_nickname receive_nickname 해서 출력해야댐
            this.roomNames.push(this.chatList[i].receive_nickname)
          }else{
            this.roomNames.push(this.chatList[i].send_nickname)
          }
          //버튼식으로 클릭해서 사람 프로필에 갈때는 chatList.send_id를 user_id처럼해서 보내면 된다!!!!!!!
        }
    }
  },
  goRoom(idx){ //개인 채팅방 들어가기
    if(this.chatList[idx].send_id==this.myId){ //보낸 사람이 "나" -> "너" 는 받는 사람
      this.yourId=this.chatList[idx].receive_id
    }else{
      this.yourId=this.chatList[idx].send_id //보낸 사람이 "너"
    }

    //이 사람방에 들어가면..recentchat table도 isread를 true로 바꿔줌
    axios.post(`${SERVER_URL}/chat/recentIsreadTrue/${this.chatList[idx].recentchat_id}`)
    this.$router.push({ name: "Chat", params: { user_id: this.yourId}})
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
.chatroom {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.chatroomhead {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 2vh 2vw;
  /* margin: 2vh 0; */
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.gotofeed {
  /* width: 10%; */
  background-color: #fff;
  color: #333;
  /* line-height: 50px; */
}
.gotofeed:hover {
   background-color: #fff;
   border-color: #59b1eb;
   color: #59b1eb;
}

.chat-img {
  width: 150px;
  cursor: pointer;
  /* margin-left: 2vh; */
  /* margin-top: 2vh; */
}

.chathead { 
  font-size: 30px;
  text-align: center;
  margin-top: 1vh;
  
}

.boxbox{
  width: 50%;
  min-width: 360px;
  display: flex;
  justify-content: center;
}

.chatListNotice {
  padding: 2vh 0;
  width: 50%;
  min-width: 360px;
  display: flex;
  justify-content: center;
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.chatListNotice2 {
  padding: 2vh 0;
  width: 50%;
  min-width: 360px;
  display: flex;
  justify-content: center;
}

.boxbox:hover {
  cursor: pointer;
}

.box {
  position: relative;
  width: 100%;
  height: 10vh;
  margin: 0.1px;
  display: flex;
  justify-content: left;
  align-items: center;
  border: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-top: none;

}

.who {
  color : rgb(88, 85, 85); 
  float: left;
  font-size: 15px;
  margin-left: 1vh;
}
.chatroom {
  font-family: "GmarketSansMedium";
  
}
.flex-col {
  display: flex;
  flex-direction: column;
}

.room {
  float: left;
  font-size: 15px;
  color:  gray;
  margin-left: 2vh;
}

.notread {
  font-size: 15px; 
  display: inline-block; 
  margin-left: 1vh;
  margin-bottom: 3.6vh;
  color: gray;
  width: 10px;
  height: 10px;
  border-radius: 50%;
  background: #FF6F58;
  
}



/* .circle {
  width:35px;
  height: 13px;
  margin: 2em;
  border-radius: 50%;
  background: #FF6F58;
} */

@media (max-width: 540px) {

  .chatroomhead {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 2vh 5px;
    /* margin: 2vh 0; */
  }

  .gotofeed {
    /* width: 10%; */
    background-color: #fff;
    color: #333;
    /* line-height: 50px; */
  }

  .gotofeed:hover {
    background-color: #fff;
    border-color: #59b1eb;
    color: #59b1eb;
  }

  .chat-img {
    width: 150px;
    cursor: pointer;
    /* margin-left: 2vh; */
    /* margin-top: 2vh; */
  }

  .chathead { 
    font-size: 30px;
    text-align: center;
    margin-top: 1vh;
    
  }

  .boxbox{
    margin-top: 0;
    width: 100%;
    display: flex;
    justify-content: center;
  }

  .chatListNotice {
    display: none;
  }

  .box {
    position: relative;
    width: 100%;
    height: 10vh;
    margin: 0.1px;
    display: flex;
    justify-content: left;
    align-items: center;
    border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
    border-top: none;
    border-left: none;
    border-right: none;

  }

  .who {
    color : rgb(88, 85, 85); 
    float: left;
    font-size: 15px;
    margin-left: 1vh;
  }
  .chatroom {
    font-family: "GmarketSansMedium";
    
  }
  .flex-col {
    display: flex;
    flex-direction: column;
  }

  .room {
    float: left;
    font-size: 15px;
    color:  gray;
    margin-left: 2vh;
  }

  .notread {
    font-size: 15px; 
    display: inline-block; 
    margin-left: 1vh;
    margin-bottom: 3.6vh;
    color: gray;
    width: 10px;
    height: 10px;
    border-radius: 50%;
    background: #FF6F58;
    
  }

}


</style>
