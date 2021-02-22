<template>
  <div style="font-family: GmarketSansMedium;">
    <!-- 형태는 followerList와 동일~ -->
    <div class="follow-container">
        <ul v-if="myId==user_id">
        <li v-for="(following,index) in followingList" v-bind:key="index">
            <b-button style="display:inline-block; margin:2px;" @click="userProfile(following)">{{following.to_nickname}}</b-button>
            <div style="display:inline-block; margin:5px;" v-for="(pets,index2) in followingPets[index]" v-bind:key="index2">
             {{pets.pet_name}}
            </div>
            <b-button variant="outline-primary" style="display:inline-block;" @click="unfollow(following, index)">취소</b-button>
        </li>
        </ul>


        <ul v-else>
        <li v-for="(following,index) in followingList" v-bind:key="index">
            <b-button style="display:inline-block; margin:2px;" @click="userProfile(following)" >{{following.to_nickname}}</b-button>
            <div style="display:inline-block; margin:5px;" v-for="(pets,index2) in followingPets[index]" v-bind:key="index2">
             {{pets.pet_name}}
            </div>
            <b-button variant="success" style="display:inline-block; background-color:purple" v-if="!inMyList(following)" @click="updateMyfollow(following,index)">팔로우</b-button>
        </li>
        </ul>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
const SERVER_URL = process.env.VUE_APP_SERVER_URL;
export default {
  name: "FollowingList",
  props: {
    followings: {
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
      followingList: this.followings,
      myfollowingList: this.myfollowings,
      followingPets: [], //팔로우한 사람들의 동물id 담기(사진을 가져와야 하므로..)
      user_id:this.userId,
      my_nickname:this.myNickname, //vue warn이 떠서 이렇게라도...
    }
  },
  created: function (){
      this.showFollowingsPets(); 
  },
  methods: {
    showFollowingsPets:function(){
      this.user_id=this.userId
      for(let i=0;i<this.followingList.length;i++){
        //following의 id를 가져와서 pet정보를 가져온다!
        const id = this.followingList[i].to_id;
        axios.get(`${SERVER_URL}/pet/selectAll/${id}`)
        .then( (res) => {
          this.followingPets.push(res.data);
          // console.log(i+ " 가져온 펫? : "+id)
        })
        .catch( (err ) => {
          console.log(err)
          const msg = "문제가 발생했습니다."
          alert(msg)
        });
      }

    },
    unfollow: function(following, idx){ //팔로우 취소 버튼 클릭 시 follow 데이터베이스에서 삭제
      const credentials = {
        from_id: following.from_id,
        to_id: following.to_id,
        f4f: following.f4f,
      }

      axios.post(`${SERVER_URL}/member/unfollow`, credentials)
      .then( (res) => {
        // console.log(res)
        this.followings.splice(idx, 1)
      })
      .catch( (err ) => {
        console.log(err)
        const msg = "문제가 발생했습니다."
        alert(msg)
      });
    },

    //myfollowingList에 들어있는지? + 나 자신이면 ㄴㄴ
    inMyList: function(following){
      // console.log("내 nickname")
      // console.log(this.myNickname)
      // console.log(this.myfollowingList)
      for(let i=0;i< this.myfollowingList.length;i++){
        if(following.to_id==this.myfollowingList[i].to_id)
          return true;
        else if(following.to_id==this.myId)
          return true;
      }
      return false;
    },

    //남의 프로필에 가서 팔로잉리스트를 보고 내가 팔로우버튼을 클릭했을 때.
    updateMyfollow:function(following){
      //myId의 nickname 가져오기
      axios.post(`${SERVER_URL}/member/getsinglemember/${this.myId}`)
      .then(({data}) => {
        this.my_nickname=data.user_nickname

      //follow 하기! : from은 myId/my_nickname이고... to는 following의 to ...
      const credentials = {
        from_id: this.myId,
        from_nickname: this.my_nickname,
        to_id: following.to_id,
        to_nickname: following.to_nickname,
        f4f: following.f4f,
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
    userProfile(following){
      if (this.$route.path == `/user/profile/${following.to_id}`) {
        this.$router.go(this.$router.currentRoute)
      } else {
        this.$router.push({ name: 'UserProfile', params: { user_id: following.to_id } })
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