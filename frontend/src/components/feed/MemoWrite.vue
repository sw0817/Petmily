<template>
  <div class="feed-comment-container" style="font-family: GmarketSansMedium;">
    <div class="feed-comment-write-div">
      <input
        ref="memosinput"
        class="feed-comment-write-input"
        v-model="memo.comment"
        placeholder="댓글 입력..."
        type="text"
      />
      <button 
        class="feed-comment-header-write-btn"
        @click="memoRegister"
      >
        등록
      </button>
    </div>
  </div>
</template>

<script>
import { mapState } from "vuex";
import { writeMemo } from "@/api/memo.js";

export default {
  name: "memowrite",
  data() {
    return {
      memo: {
        user_id: null,
        comment: null,
        feed_id: 0
      }
    };
  },
  props: { feed_id: Number, reloadMemos: Function },
  computed: {
    ...mapState(["userId"])
  },
  methods: {
    async memoRegister() {
      if(this.memo.comment){
        this.memo.user_id = this.userId;
        this.memo.feed_id = this.feed_id;
        await writeMemo(
          this.memo,
          (response) => {
            // this.$emit("memo-list", response.data);
            this.memo.comment = "";
            this.reloadMemos(response.data);
          },
          (error) => {
            console.log(error);
          }
        );
      }
    }
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
.login-content-email-input-memo{
  border: none;
  border-bottom: 1px solid black;
  width: 100%;
  font-size: 1.5vw;
}

.login-content-email-input:focus {
  outline: none;
}

.feed-comment-header-write-btn {
  font-size: 16px;
  margin-left: 10px;
  color: black;
}

.feed-comment-write-input {
  border: none;
  border-bottom: 1px solid black;
  width: 80%;
  font-size: 18px;
}

.feed-comment-write-input:focus {
  outline: none;
}

.feed-comment-write-div {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

@media (max-width: 1100px) {

  .feed-comment-header-write-btn {
    font-size: 1.3vw;
    margin-left: 1vw;
    color: black;
  }

  .feed-comment-write-input {
    border: none;
    border-bottom: 1px solid black;
    width: 80%;
    font-size: 1.5vw;
  }

  .feed-comment-write-input:focus {
    outline: none;
  }

  .feed-comment-write-div {
    width: 100%;
    display: flex;
    align-items: center;
  }

}

@media (max-width: 540px) {

  .feed-comment-header-write-btn {
    font-size: 3.5vw;
    margin-left: 1vw;
    color: black;
  }

  .feed-comment-write-input {
    border: none;
    border-bottom: 1px solid black;
    width: 80%;
    font-size: 3.5vw;
  }

  .feed-comment-write-input:focus {
    outline: none;
  }

  .feed-comment-write-div {
    width: 100%;
    display: flex;
    align-items: center;
  }

}


</style>
