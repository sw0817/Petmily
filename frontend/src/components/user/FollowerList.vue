<template>
  <div style="font-family: GmarketSansMedium;">
    <!-- 형태는 followerList와 동일~ -->
    <div class="follow-container">
        <ul v-if="myId==user_id">
        <li v-for="(follower,index) in followerList" v-bind:key="index">
            <b-button style="display:inline-block; margin:2px;" @click="userProfile(follower)">{{follower.from_nickname}}</b-button>
            <div style="display:inline-block; margin:5px;" v-for="(pets,index2) in followerPets[index]" v-bind:key="index2">
             {{pets.pet_name}}
            </div>
            <b-button variant="success" style="display:inline-block;" v-if="!follower.f4f" @click="updatefollow(follower,index)">팔로우</b-button>
            <b-button variant="outline-primary" style="display:inline-block;" @click="deletefollow(follower, index)">삭제</b-button>
        </li>
        </ul>

        <ul v-else>
        <li v-for="(follower,index) in followerList" v-bind:key="index">
            <b-button style="display:inline-block; margin:2px;" @click="userProfile(follower)">{{follower.from_nickname}}</b-button>
            <div style="display:inline-block; margin:5px;" v-for="(pets,index2) in followerPets[index]" v-bind:key="index2">
             {{pets.pet_name}}
            </div>
            <b-button variant="success" style="display:inline-block; background-color:purple" v-if="!inMyList(follower)" @click="updateMyfollow(follower,index)">팔로우</b-button>
        </li>
        </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL;
export default {
  name: "FollowerList",
  props: {
    followers: {
      type: Array,
    },
    myfollowings: {
      type: Array,
    },
    userId: {
      type: Number,
    },
    myId: {
      type: Number,
    },
    myNickname: {
      type: String,
    },
  },
  data: function () {
    return {
      followerList: this.followers,
      myfollowingList: this.myfollowings,
      followerPets: [], //팔로워들의 동물id 담기(사진을 가져와야 하므로..)
      user_id:0,
      my_nickname:this.myNickname, //vue warn이 떠서 이렇게라도...
    }
  },
  created: function (){
      this.showFollowersPets(); 
  },
  methods: {
    showFollowersPets:function(){
      this.user_id=this.userId
      for(let i=0;i<this.followerList.length;i++){
        //follower의 id를 가져와서 pet정보를 가져온다!
        const id = this.followerList[i].from_id;
        axios.get(`${SERVER_URL}/pet/selectAll/${id}`)
        .then( (res) => {
          this.followerPets.push(res.data);
          // console.log(i+ " 가져온 펫? : "+id)
          //console.log("gg"+this.followerPets[1][0].pet_id)
        })
        .catch( (err ) => {
          console.log(err)
          const msg = "문제가 발생했습니다."
          alert(msg)
        });
      }

    },
    //myfollowingList에 들어있는지? + 나 자신이면 ㄴㄴ
    inMyList: function(follower){
      // console.log("내 followinglist")
      // console.log(this.myfollowingList)
      for(let i=0;i< this.myfollowingList.length;i++){
        if(follower.from_id==this.myfollowingList[i].to_id)
          return true;
        else if(follower.from_id==this.myId)
          return true;
      }
      return false;
    },
    deletefollow: function(follower, idx){ //팔로우 삭제 버튼 클릭 시 나를 팔로우 못하게 삭제
      const credentials = {
        from_id: follower.from_id,
        to_id: follower.to_id,
        f4f: follower.f4f,
      }

      axios.post(`${SERVER_URL}/member/unfollow`, credentials)
      .then( (res) => {
        console.log(res)
        this.followers.splice(idx, 1)
      })
      .catch( (err ) => {
        console.log(err)
        const msg = "문제가 발생했습니다."
        alert(msg)
      });
    },

    updatefollow: function(follower, idx){ //팔로우 클릭하면 팔로우됨..(from, to 가 바뀌어야 한다..)
      const credentials = {
        from_id: follower.to_id,
        from_nickname: follower.to_nickname,
        to_id: follower.from_id,
        to_nickname: follower.from_nickname,
        f4f: follower.f4f,
      }

      axios.post(`${SERVER_URL}/member/follow`, credentials)
      .then( (res) => {
        // console.log(res)
        this.followers.splice(idx, 1)
      })
      .catch( (err ) => {
        console.log(err)
        const msg = "문제가 발생했습니다."
        alert(msg)
      });
    },
    
    //남의 프로필에 가서 팔로워리스트를 보고 내가 팔로우버튼을 클릭했을 때.
    updateMyfollow:function(follower){
      //myId의 nickname 가져오기
      axios.post(`${SERVER_URL}/member/getsinglemember/${this.myId}`)
      .then(({data}) => {
        this.my_nickname=data.user_nickname

    //follow 하기! : from은 myId/my_nickname이고... to는 follower의 from ...
      const credentials = {
        from_id: this.myId,
        from_nickname: this.my_nickname,
        to_id: follower.from_id,
        to_nickname: follower.from_nickname,
        f4f: follower.f4f,
      }

      axios.post(`${SERVER_URL}/member/follow`, credentials)
      .then( (res) => {
        // console.log(res)
      })
      .catch( (err ) => {
        console.log(err)
        const msg = "문제가 발생했습니다."
        alert(msg)
      });

      })
      .catch( (err) => {
        console.log(err)
      })
    },

    userProfile(follower){
      if (this.$route.path == `/user/profile/${follower.from_id}`) {
        this.$router.go(this.$router.currentRoute)
      } else {
        this.$router.push({ name: 'UserProfile', params: { user_id: follower.from_id } })
      }
      
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
</style>
