<template>
  <div v-if="files.length" class="img-preview-container">
    <div class="diary-img-container">
      <div v-for="(image, idx) in files" :key="'A'+idx">
        <div class="feed-img-div" v-if="idx == imgIdx && image.type == 'image'">
          <div class="img-close-btn" @click="fileDeleteButton(idx)">
            x
          </div>
          <div class="feed-img-div-center">
            <img
              :class="{'feed-img': imgState, 'feed-img-none': !imgState}"
              :src="image.preview"
              :id="`image-${idx}`"
            >
          </div>
        </div>

        <div class="feed-img-div" v-else-if="idx == imgIdx && image.type == 'video'">
          <div class="img-close-btn" @click="fileDeleteButton(idx)">
            x
          </div>
          <div class="feed-img-div-center">
            <video
              :class="{'feed-img': imgState, 'feed-img-none': !imgState}"
              :src="image.preview"
              :id="`image-${idx}`"
              autoplay
              muted
              playsinline
              loop
              controls
            >
            </video>
          </div>
        </div>
      </div>
      <button
        v-if="0 < imgIdx"
        class="diary-pre-btn"
        @click="preImg"
      >
        <i class="fas fa-angle-left"></i>
      </button>
      <button
        v-if="imgIdx < files.length-1"
        class="diary-next-btn"
        @click="nextImg"
      >
        <i class="fas fa-angle-right"></i>
      </button>
      <div class="diary-function-container">
        <div class="dot-container">
          <span
            v-for="(dot, idx) in files.length"
            :key="'C'+idx"
            class="idx-dot"
          >
            <div @click="changeImgIdx(idx)">
              <button v-if="idx != imgIdx" class="idx-btn-off">
              </button>
              <button v-else class="idx-btn-on">
              </button>
            </div>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ImagePreview",
  props: {
    files: {
      type: Array,
    },
    imgIdx: {
      type: Number,
    },
  },
  data: function () {
    return {
      imgState: true,
    }
  },
  methods: {
    preImg: function () {
      if (0 < this.imgIdx) {
        setTimeout(() => {
          this.imgState = false
        }, 0);
        setTimeout(() => {
          // this.imgIdx -= 1
          this.$emit("preImg")
          this.imgState = true
        }, 500);
      }
    },
    nextImg: function () {
      if (this.imgIdx < this.files.length) {
        setTimeout(() => {
          this.imgState = false
        }, 0);
        setTimeout(() => {
          // this.imgIdx += 1
          this.$emit("nextImg")
          this.imgState = true
        }, 500);
      }
    },
    changeImgIdx: function (idx) {
      setTimeout(() => {
          this.imgState = false
        }, 0);
        setTimeout(() => {
          // this.imgIdx = idx
          this.$emit("changeImg", idx)
          this.imgState = true
        }, 500);
    },
    fileDeleteButton: function (idx) {
      this.$emit("deleteImg", idx)
    },
  },
}
</script>

<style>

.img-preview-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.img-preview {
  position: relative;
  padding: 2vw;
  width: 34vw;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: white;
  /* margin-right: 1vw; */
}


.img-close-btn {
  position: absolute;
  line-height: 1.4vw;
  z-index: 2;
  font-size: 1.5vw;
  right: 1vw;
  top: 1vw;
  color: #fff;
  font-weight: bold;
  background-color: #666666;
  width: 1.7vw;
  height: 1.7vw;
  text-align: center;
  cursor: pointer;
}

@media (max-width : 540px) {

  .img-close-btn {
    position: absolute;
    line-height: 5vw;
    z-index: 2;
    font-size: 8vw;
    right: 4vw;
    top: 3vw;
    color: #fff;
    font-weight: bold;
    background-color: #666666;
    width: 6.5vw;
    height: 6.5vw;
    text-align: center;
    cursor: pointer;
  }

}

</style>
