<template>
  <div class="alarm-modal">
    <div class="alarm-modal-wrapper">
      <div class="tag-table-container">
        <div class="tag-table-header">
          <div class="tag-table-pre">
            <button class="tag-table-pre-btn" @click="done">
              <i class="fas fa-arrow-left"></i>
            </button>
          </div>
          <div class="friend-tag-title">
            내 친구 태그하기
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: 'AlarmModal',
  props: {
  },
  data: function () {
    return {
      title: ""
    }
  },
  methods: {
    done: function () {
      this.$emit('done')
    },
  },
  created: function () {
  },
}
</script>

<style>

.alarm-modal {
  position: fixed;
  z-index: 15;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity .3s ease;
}

.alarm-modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.tag-table-container {
  display: flex;
  flex-direction: column;
  width: 50vw;
  height: 40vw;
  margin: 0 auto;
  /* padding: 2vw 3vw; */
  background-color: #fff;
  border-radius: 0.5vw;
  box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
  transition: all .3s ease;
}

.pet-tag-title {
  width: 60%;
  font-size: 2.5vw;
  font-weight: bold;
  text-align: center;
}

.friend-tag-title {
  width: 60%;
  font-size: 2.5vw;
  font-weight: bold;
  text-align: center;
}

.tag-table-header {
  display: flex;
  align-items: center;
  margin-top: 2vw;
  margin-left: 3vw;
  margin-right: 3vw;
}

.tag-table-pre {
  width: 20%;
}

.tag-table-done {
  width: 20%;
  text-align: end;
}

.tag-table-done-btn {
  font-size: 1.5vw;
  font-weight: bold;
  color: mediumslateblue;
}

.pet-tag-myPet-title {
  font-size: 1.8vw;
  font-weight: bold;
  margin-top: 1vw;
  color: gray;
  padding: 1vw 3vw;
  align-items: center;
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.friend-tag-myFriend-title {
  display: flex;
  font-size: 1.8vw;
  font-weight: bold;
  margin-top: 1vw;
  color: gray;
  padding: 1vw 3vw;
  align-items: center;
  border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
  border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
}

.myPet-list {
  display: flex;
  flex-direction: column;
  justify-content: center;
  height: 50%;
  overflow: hidden;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

.pet-tag-table {
  display: flex;
  align-items: center;
  margin-left: 3vw;
  margin-right: 3vw;
  margin-top: 3vw;
}

.pet-tag-img {
  width: 5vw;
  height: 5vw;
}

.pet-tag-img img {
  width: 5vw;
  height: 5vw;
  border-radius: 70%;
}

.pet-tag-petName {
  margin-left: 5%;
  width: 40%;
  font-size: 1.5vw;
  font-weight: bold;
  color: mediumslateblue;
}

.pet-tag-petName:hover {
  cursor: pointer;
}

.taged-pet {
  margin-left: 3vw;
  margin-right: 3vw;
}

.taged-pet-title {
  margin-top: 1vw;
  margin-bottom: 0.5vw;
  font-size: 1.5vw;
  color: grey;
}

.taged-pet-list {
  margin-right: 1vw;
  font-size: 1.5vw;
}

.taged-pet-name {
  color: mediumslateblue;
}

.firend-tag-myFriend-title {
  font-size: 1.8vw;
  font-weight: bold;
  margin-top: 1vw;
  color: gray;
}

.myFriend-list {
  margin-top: 1vw;
  display: flex;
  flex-direction: column;
  padding: 0 3vw;
  overflow: hidden;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
  /* justify-content: center; */
  /* height: 50%; */
}

.friend-tag-search-logo {
  font-size: 2vw;
}

.friend-tag-search-input {
  font-size: 2vw;
  margin-left: 1vw;
}

.friend-tag-search-input:focus {
  outline: none;
}

.friend-tag-table {
  display: flex;
  align-items: center;
  /* margin: auto 0; */
}

.friend-tag-img {
  width: 5vw;
  height: 5vw;
}

.friend-tag-img img {
  width: 5vw;
  height: 5vw;
  border-radius: 70%;
}

.friend-tag-friendName {
  margin-left: 5%;
  margin-top: 1vw;
  width: 40%;
  font-size: 1.5vw;
  font-weight: bold;
  /* color: mediumslateblue; */
}

.friend-tag-friendName:hover {
  cursor: pointer;
}

.taged-friend {
  margin-left: 3vw;
  margin-right: 3vw;
}

.taged-friend-title {
  margin-top: 1vw;
  margin-bottom: 0.5vw;
  font-size: 1.5vw;
  color: grey;
}

.taged-friend-list {
  margin-right: 1vw;
  font-size: 1.5vw;
}

.taged-friend-name {
  color: mediumslateblue;
}

.write-container2 {
  width: 100%;
  min-height: 30vw;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
}

@media ( max-width: 540px ) {

  .tag-table-container {
    display: flex;
    flex-direction: column;
    width: 80vw;
    height: 80vw;
    margin: 0 auto;
    /* padding: 2vw 3vw; */
    background-color: #fff;
    border-radius: 3vw;
    box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
    transition: all .3s ease;
  }

  .pet-tag-title {
    width: 60%;
    font-size: 5vw;
    font-weight: bold;
    text-align: center;
  }

  .friend-tag-title {
    width: 60%;
    font-size: 5vw;
    font-weight: bold;
    text-align: center;
  }

  .tag-table-header {
    display: flex;
    align-items: center;
    margin-top: 2vw;
    margin-left: 3vw;
    margin-right: 3vw;
  }

  .tag-table-pre {
    width: 20%;
  }

  .tag-table-done {
    width: 20%;
    text-align: end;
  }

  .tag-table-done-btn {
    font-size: 5vw;
    font-weight: bold;
    color: mediumslateblue;
  }

  .pet-tag-myPet-title {
    font-size: 5vw;
    font-weight: bold;
    margin-top: 1vw;
    color: gray;
    padding: 1vw 3vw;
    align-items: center;
    border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .friend-tag-myFriend-title {
    display: flex;
    font-size: 5vw;
    font-weight: bold;
    margin-top: 1vw;
    color: gray;
    padding: 1vw 3vw;
    align-items: center;
    border-bottom: 0.1vw solid rgba(175, 175, 175, 0.5);
    border-top: 0.1vw solid rgba(175, 175, 175, 0.5);
  }

  .myPet-list {
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 50%;
    overflow: hidden;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
  }

  .pet-tag-table {
    display: flex;
    align-items: center;
    margin-left: 3vw;
    margin-right: 3vw;
    margin-top: 3vw;
  }

  .pet-tag-img {
    width: 15vw;
    height: 15vw;
  }

  .pet-tag-img img {
    width: 15vw;
    height: 15vw;
    border-radius: 70%;
  }

  .pet-tag-petName {
    margin-left: 5%;
    width: 40%;
    font-size: 5vw;
    font-weight: bold;
    color: mediumslateblue;
  }

  .pet-tag-petName:hover {
    cursor: pointer;
  }

  .taged-pet {
    margin-left: 3vw;
    margin-right: 3vw;
  }

  .taged-pet-title {
    margin-top: 1vw;
    margin-bottom: 0.5vw;
    font-size: 4vw;
    color: grey;
  }

  .taged-pet-list {
    margin-right: 1vw;
    font-size: 5vw;
  }

  .taged-pet-name {
    color: mediumslateblue;
  }

  .firend-tag-myFriend-title {
    font-size: 4vw;
    font-weight: bold;
    margin-top: 1vw;
    color: gray;
  }

  .myFriend-list {
    margin-top: 1vw;
    display: flex;
    flex-direction: column;
    padding: 0 3vw;
    overflow: hidden;
    overflow-y: auto;
    -webkit-overflow-scrolling: touch;
    /* justify-content: center; */
    /* height: 50%; */
  }

  .friend-tag-search-logo {
    font-size: 5vw;
  }

  .friend-tag-search-input {
    font-size: 5vw;
    margin-left: 1vw;
  }

  .friend-tag-search-input:focus {
    outline: none;
  }

  .friend-tag-table {
    display: flex;
    align-items: center;
    /* margin: auto 0; */
  }

  .friend-tag-img {
    width: 15vw;
    height: 15vw;
  }

  .friend-tag-img img {
    width: 15vw;
    height: 15vw;
    border-radius: 70%;
  }

  .friend-tag-friendName {
    margin-left: 5%;
    margin-top: 1vw;
    width: 40%;
    font-size: 5vw;
    font-weight: bold;
    /* color: mediumslateblue; */
  }

  .friend-tag-friendName:hover {
    cursor: pointer;
  }

  .taged-friend {
    margin-left: 3vw;
    margin-right: 3vw;
  }

  .taged-friend-title {
    margin-top: 1vw;
    margin-bottom: 0.5vw;
    font-size: 4vw;
    color: grey;
  }

  .taged-friend-list {
    margin-right: 1vw;
    font-size: 5vw;
  }

  .taged-friend-name {
    color: mediumslateblue;
  }

}

</style>
