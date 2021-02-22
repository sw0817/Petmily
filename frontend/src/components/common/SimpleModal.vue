<template>
  <div style="font-family: GmarketSansMedium;" class="simple-modal-container">
    <div class="simple-title-header">
      <div class="simple-title-title">
        알림
      </div>
    </div>
      <div class="simple-modal-content">
        {{ contents }}
      </div>
    <div class="simple-modal-footer">
      <div class="modal-exit" @click="exit" @keyup.enter="exit">
        확인
      </div>
    </div>
  </div>
</template>

<script>

export default {
  name: "SimpleModal",
  props: {
    contents: {
      type: String,
    },
  },
  data: function () {
    return {
      
    }
  },
  methods: {
    exit: function () {
      this.$emit("modalOff")
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
.simple-modal-container {
  display: flex;
  flex-direction: column;
  background-color: white;
  width: 50vw;
  margin: 0 auto;
  /* padding: 1vw; */
  border-radius: 1vw;
  box-shadow: 0 0.2vw 0.8vw rgba(0, 0, 0, 0.33);
  transition: all .3s ease;
}

.simple-title-header {
  font-size: 1.5vw;
  font-weight: bold;
  color: mediumslateblue;
  padding: 1vw;
}

.simple-modal-content {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 1.5vw;
  font-weight: bold;
  border-top: 0.1vw solid mediumslateblue;
  padding: 3vw 1vw;
  transition-duration: 0.5s;
}

.simple-modal-footer {
  font-size: 1.5vw;
  font-weight: bold;
  padding: 1vw;
  text-align: end;
}

.modal-exit:hover {
  cursor: pointer;
}

@media ( max-width: 540px ) {

  .simple-modal-container {
    width: 80vw;
  }
  
  .simple-title-header {
    font-size: 5vw;
  }
    
  .simple-modal-content {
    font-size: 5vw;
  }

  .simple-modal-footer {
    font-size: 5vw;
  }

}


</style>
