<template>
<div>
  <div class="head" >
    <button class="namebtn" @click="goProfile()">{{yourNickname}} </button> 님과의 채팅
    <button class="xbtn" @click="backRoom()"> 이전 </button>
  </div>
    <div><br><br><br>
        <div v-for="(message, idx) in messages" v-bind:key="idx" style="font-family: GmarketSansMedium;">
          <div v-if="messages[idx].send_id!=myId" class="bubble">
            <div style="display:inline-block; font-size: 15px; margin:4px; color: mediumslateblue"> {{message.send_nickname}} </div>
            <div style="display:inline-block; font-size: 13px; margin:4px;"> {{ message.body }} </div>
            <div style="display:inline-block; font-size: 8px; margin-left:5px; color: gray;"> {{sendTime[idx]}}</div>
          </div>
          <div v-else class="bubble-you">
            <div style="display:inline-block; font-size: 15px; margin:4px; color:black;">나</div>
            <div style="display:inline-block; font-size: 13px; margin:4px;"> {{ message.body }} </div>
            <div style="display:inline-block; font-size: 8px; margin-left:5px; color: gray;"> {{sendTime[idx]}}</div>
          </div>
      </div>
    </div><br><br><br>

    <ul class="bottom">
      <div class="mes-input">
        <input
          class="msg"
          type="text"
          v-model="body"
          placeholder="메시지를 입력해주세요."
          @keyup.enter="submitChatMessageForm"
        >
      </div>    
      <button v-if="1 < body.trim().length" class="mes-btn" @click="submitChatMessageForm">
        전송
      </button>
      <button v-else class="mes-btn2" disabled>
        전송
      </button>
    </ul>

</div>

</template>

<script>
import axios from "axios";
import VueJwtDecode from "vue-jwt-decode"
import moment from "moment/moment"

const SERVER_URL = process.env.VUE_APP_SERVER_URL;
export default {
  name: "Chat",
  components: {
  },
    props: {
    user_id: {
      type: Number,
    },
  },
data: function () {
    return {
      myId: 0,
      myNickname:"",
      yourId:this.$route.params.user_id,
      yourNickname:"",
      body:"", //메시지 보낼 내용    
      messages:[],
      isreads:[],
      sendTime:[],
      from_chat_id:0,   
      room_id:0,
      nowYourId:0,
    }           
  },
  created() {
    this.component = this;
    this.messages=[];
    this.getChattersInfo();
    //this.loadNewMessages(0); //나갔다 들어오거나 맨 처음? 때 일단 0부터 모든 대화내용을 다 가져와야함!
    //setTimeout(setInterval(()=>this.checkChatId(),1000), 3000); //위 과정 떄문에 혹시 몰라서... setinterval을 3초뒤에 실행하게 해놓음
    setInterval(()=>this.checkChatId(),1000);
    //setInterval(()=>this.checkIsRead(),1000)
    setTimeout(() => this.scrollToEnd(), 1500);
  },
  computed: {
    messageLength: function () {
      return this.messages.length
    },
  },
  watch: {
    messageLength: function () {
      setTimeout(() => this.scrollToEnd(), 500);
    },
  },
  methods:{
    scrollToEnd: function() {
      window.scrollTo(0,document.body.scrollHeight)
    },
    getChattersInfo(){ //내 정보와 상대 정보 가져오기
    const token = localStorage.getItem('jwt')
      if (token) {
        this.myId = VueJwtDecode.decode(token).user.user_id
        axios.post(`${SERVER_URL}/member/getsinglemember/${this.myId}`)
          .then(({data}) => {
            this.myNickname=data.user_nickname
            //console.log("내 닉네임 : "+this.myNickname)
          })
      }

      axios.post(`${SERVER_URL}/member/getsinglemember/${this.yourId}`)
      .then(({data}) => {
        this.yourNickname=data.user_nickname
         //console.log("너 닉네임 : "+this.yourNickname)
        this.findRoom();
      })
      
    },

    backRoom:function () {
      this.$router.push({ name: "MyChatRoom", params: { user_id: this.myId}})
    },
    
    findRoom(){ //방을 찾아... -> your_id랑 my_id를 전달해야한다!
      const credentials ={ //방 번호는  1-1 사람별로 고유하게 매칭되고 
                          //my_id가 myId가 될수도, yourId가 될수도 있다.(어차피 sql에서 or 처리) 누가 방팠는진 모르는거니까..
        my_id:this.myId,
        your_id:this.yourId
         }
      axios.post(`${SERVER_URL}/chat/isroom`,credentials)
        .then(({data}) => {
          //console.log("방 번호 ? : "+data.room_id)
          this.room_id=data.room_id
      })
      
    },

    submitChatMessageForm(){ //chatDto 폼에 맞춰서 메시지 전송
      const credentials ={
            room_id:this.room_id,
        send_id:this.myId, //보내는이는 나고
        send_nickname:this.myNickname, 
        receive_id:this.yourId, //받는이는 너
        receive_nickname:this.yourNickname,
        body:this.body, //보내는 내용
        isread:false, 
         }
      axios.post(`${SERVER_URL}/chat/doAddMessage`,credentials)
        .then(({data}) => {
          // console.log("메시지 저장됨!")
      })
      this.body=""
      this.scrollToEnd();
    },


    // checkIsRead(){
    //   axios.post(`${SERVER_URL}/chat/readcheck/${this.room_id}`) 
    //       .then(({data}) => {
    //           this.isreads=data
    //          }
    //   )
    // },
    checkChatId(){ //setinterval을 통해서 해당 room_id의 채팅 개수를 전부 가져옴
      axios.post(`${SERVER_URL}/chat/startChatId/${this.room_id}`) 
          .then(({data}) => {
           //console.log("from_chat_id : "+this.from_chat_id+" | data : "+data)
              //room_id로 작성된 개수..?흠..... 이게 변화가 있는지!!!
              if(this.from_chat_id<data){ //from_chat_id같은 경우에는, 내가 messages라는 배열에 넣은 마지막 chat_id(고유ID)
                this.loadNewMessages(this.from_chat_id+1) //만약에 from_chat_id가 data 보다 작다면 메시지가 새로 추가되었다는 이야기! 
                 //console.log("바뀌기 전 this.from_chat_id : "+this.from_chat_id)
                this.from_chat_id=data //계산이 끝나면 from_chat_id를 맨 마지막 chat_id로 바꿔줌
                //console.log("바뀌기 후 this.from_chat_id : "+this.from_chat_id)
             }
            
          })
          
    },
    loadNewMessages(cnt){ //db에 추가된 메시지 내용을 뽑아내는 함수
      // console.log("loadNewData")
      //console.log("시작하는 chat_id : "+this.from_chat_id)
      const credentials ={
        chat_id:this.chat_id,
        send_id:this.myId,
        receive_id:this.yourId,
      }
      axios.post(`${SERVER_URL}/chat/getAllMessagesFrom/${cnt}`,credentials) //chat_id부터 쭉 채팅 내용 가져오기~
          .then(({data}) => {
            //console.log("from_chat_id : "+this.from_chat_id+" 그 다음 : "+(cnt))
            //  console.log(data)
            for(let i=0;i<data.length;i++){ //messages에 data의 body를 추가.....
                //console.log("durls? : "+data[i])
                this.messages.push(data[i]); //이전에 마지막까지 넣었던 chat_id의 칼럼내용+1 ~ 마지막에 추가된 chat_id의 칼럼내용까지 가져와서 넣어줌
                this.splitDate(data[i].send_date) //이건 날짜 출력 부분인데 잘 몰라서 그냥 이렇게 둠...
            }

        }) 

    },
    // Wed Feb 17 2021 16:38:29 GMT+0900 (대한민국 표준시) <------ newDate
    // 02/17 16:37

    splitDate(date){ //날짜 바꿔주는 함수? 2020-01-01 10:00:00 형식으로 나옴
      // let newDate = moment(new Date(date),'YYYY.MM.DD HH:mm:ss').toDate() - 32400000;
      // newDate = new Date(newDate)
      // newDate = String(newDate)
      // var s = newDate.slice(4, 10) + newDate.slice(15, 21)
      // console.log(s + " <------------- ssssssss")
      // // var s = newDate[5]+newDate[6]+"/"+newDate[8] + newDate[9]+" "
      // // for(var i =10;i<=15;i++){
      // //   s+=newDate[i]
      // // }
      // this.sendTime.push(s)
      var s = date[5]+date[6]+"/"+date[8]+date[9]+" "
      for(var i =10;i<=15;i++)
        s+=date[i]
      
      this.sendTime.push(s)
    },
    goProfile(){ //일단 맨 위에 ~님과의 채팅에서 ~님 클릭시 프로필로 가게 함!! (이게 나은거 같아서...)
      this.$router.push({ name: 'UserProfile', params: { user_id: this.yourId} })
    }

  }
}
</script>

<style>
@font-face {
    font-family: 'GmarketSansMedium';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/GmarketSansMedium.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
.xbtn {
  width: 10%;
  background-color: #fff;
  color: #333;
  line-height: 50px;
  float: right;
}
.xbtn:hover {
   background-color: #fff;
   border-color: #59b1eb;
   color: #59b1eb;
}

.head {
  /* display: flex; */
  font-size: 20px; 
  z-index:2; 
  width: 100%; 
  background-color: white; 
  font-family: GmarketSansMedium; 
  position: fixed; 
  text-align: center;
}

.bubble {
  position:relative;
  z-index: 1;
  margin: 0 0 0.5em;
  border:3px solid #a7a5a7;
  color:#333;
  background:#fff;
  -webkit-border-radius:10px;
  -moz-border-radius:10px;
  border-radius:10px;
  width: 30%;
  left: 1%;
}

.bubble-you {
  position:relative;
  z-index: 1;
  margin: 0 0 0.5em;
  left: 70%;
  border:3px solid #FF6F58;
  color:#333;
  background:#fff;
  -webkit-border-radius:10px;
  -moz-border-radius:10px;
  border-radius:10px;
  width: 30%;
}

.namebtn {
  /* width: 15%; */
  min-width: 10vw;
  padding: 0.5vw;
  border-radius: 10vw;
  border: 0.5vw solid;
  border-color: mediumslateblue;
  background-color: white;
  color:mediumslateblue;
  font-size: 15px;
  font-weight: bold;
  transition-duration: 0.5s;
  margin-top: 1vh;
}

.mes-input {
  margin: 0.5em;
  float: left;
  bottom: 0;
  width: 70%;
}

.mes-btn {
  width: 30%;
  height: 100%;
  border-radius: 1vw;
  border: 2vw solid;
  border-color: rgb(251,228,194);
  background-color: rgb(251,228,194);
  color:rgb(18, 18, 19);
  font-size: auto;
  font-weight: bold;
  transition-duration: 0.5s;
  text-align: center;
  float: right;
}

.mes-btn2 {
  width: 30%;
  height: 100%;
  border-radius: 1vw;
  border: 2vw solid;
  border-color: gray;
  background-color: gray;
  color: rgb(18, 18, 19);
  font-size: auto;
  font-weight: bold;
  transition-duration: 0.5s;
  text-align: center;
  float: right;
}

.bottom {
  z-index: 2;
  position:fixed;
  bottom: 0;
  width: 100%;
  height: 8%;
  background-color: white;
  font-family: "GmarketSansMedium";
  margin: 0;
  display: flex;
}

/* .loading {
  position: absolute;
  text-align: center;
  font-family: "GmarketSansMedium";
  font-size: 30px;
} */

.msg {
  float: right;
  width: 100%;
  height: 100%;
}

@media (max-width: 540px) {

  .xbtn {
    width: 20%;
    background-color: #fff;
    color: #333;
    line-height: 50px;
    float: right;
  }
  .xbtn:hover {
    background-color: #fff;
    border-color: #59b1eb;
    color: #59b1eb;
  }

  .head {
    font-size: 20px; 
    z-index:2; 
    width: 100%; 
    background-color: white; 
    font-family: GmarketSansMedium; 
    position: fixed; 
    text-align: center;
  }

  .bubble {
    position:relative;
    z-index: 1;
    margin: 0 0 0.5em;
    border:3px solid #a7a5a7;
    color:#333;
    background:#fff;
    -webkit-border-radius:10px;
    -moz-border-radius:10px;
    border-radius:10px;
    width: 40%;
    left: 1%;
  }

  .bubble-you {
    position:relative;
    z-index: 1;
    margin: 0 0 0.5em;
    left: 60%;
    border:3px solid #FF6F58;
    color:#333;
    background:#fff;
    -webkit-border-radius:10px;
    -moz-border-radius:10px;
    border-radius:10px;
    width: 40%;
  }

  .namebtn {
    /* width: 30%; */
    width: 20vw;
    padding: 0.5vw;
    border-radius: 10vw;
    border: 1vw solid;
    border-color: mediumslateblue;
    background-color: white;
    color:mediumslateblue;
    font-size: 15px;
    font-weight: bold;
    transition-duration: 0.5s;
    margin-top: 1vh;
  }

  .mes-input {
    margin: 0.5em;
    float: left;
    bottom: 0;
    width: 70%;
  }

  .mes-btn {
    width: 30%;
    height: 100%;
    border-radius: 1vw;
    border: 2vw solid;
    border-color: rgb(251,228,194);
    background-color: rgb(251,228,194);
    color:rgb(18, 18, 19);
    font-size: auto;
    font-weight: bold;
    transition-duration: 0.5s;
    text-align: center;
    float: right;
  }

  .mes-btn2 {
  width: 30%;
  height: 100%;
  border-radius: 1vw;
  border: 2vw solid;
  border-color: gray;
  background-color: gray;
  color: rgb(18, 18, 19);
  font-size: auto;
  font-weight: bold;
  transition-duration: 0.5s;
  text-align: center;
  float: right;
}

  .bottom {
    z-index: 2;
    position:fixed;
    bottom: 0;
    width: 100%;
    height: 8%;
    background-color: white;
    font-family: "GmarketSansMedium";
    margin: 0;
    display: flex;
  }

  /* .loading {
    position: absolute;
    text-align: center;
    font-family: "GmarketSansMedium";
    font-size: 30px;
  } */

  .msg {
    float: right;
    width: 100%;
    height: 100%;
  }

}


</style>
