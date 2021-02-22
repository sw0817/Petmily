<template>
  <div style="font-family: GmarketSansMedium;">
    <div v-if="files.length" class="feed-img-top-container">
      <div class="feed-img-container2">
        <div v-for="(image, idx) in files" :key="'A'+idx">
          <div class="feed-img2-div" v-if="idx == imgIdx && image.type == 'image'">
            <div class="img-close-btn2" @click="fileDeleteButton(idx)">
              x
            </div>
            <div class="feed-img2-div-center">
              <img
                :class="{'feed-img2': imgState, 'feed-img-none2': !imgState}"
                :src="image.preview"
                :id="`image-${idx}`"
              >
            </div>
          </div>

          <div class="feed-img2-div" v-else-if="idx == imgIdx && image.type == 'video'">
            <div class="img-close-btn2" @click="fileDeleteButton(idx)">
              x
            </div>
            <div class="feed-img2-div-center">
              <video
                :class="{'feed-img2': imgState, 'feed-img-none2': !imgState}"
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
          class="pre-btn2"
          @click="preImg"
        >
          <i class="fas fa-angle-left"></i>
        </button>
        <button
          v-if="imgIdx < files.length-1"
          class="next-btn2"
          @click="nextImg"
        >
          <i class="fas fa-angle-right"></i>
        </button>
        <div class="feed-function-container2">
          <div class="dot-container2">
            <span
              v-for="(dot, idx) in files.length"
              :key="'C'+idx"
              class="idx-dot2"
            >
              <div @click="changeImgIdx(idx)">
                <button v-if="idx != imgIdx" class="idx-btn2-off">
                </button>
                <button v-else class="idx-btn2-on">
                </button>
              </div>
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "DiaryImg",
  data: function () {
    return {
      imgState: true,
      files: [], // 업로드용 파일
      imageTypes: [
        "image/apng",
        "image/bmp",
        "image/gif",
        "image/jpeg",
        "image/pjpeg",
        "image/png",
        "image/svg+xml",
        "image/tiff",
        "image/webp",
        "image/x-icon",
      ],
      videoTypes: [
        "video/mpeg",
        "video/mp4",
        "video/ogg",
        "video/quicktime",
        "video/webm",
        "video/mpg",
        "video/m4v",
        "video/m4p",
        "video/ogv",
        "video/mov",
      ],
      uploadImageIndex: 0,
      imgIdx: 0,
    }
  },
  methods: {
    fileDeleteButton: function (idx) {
      if (idx == this.files.length-1 && idx != 0) {
        this.imgIdx -= 1
      }
      this.files.splice(idx, 1)
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
</style>
