<template>
  <div id="app">
    <router-view class="top-bgc"></router-view>
  </div>
</template>
<script>
// import "./components/css/style.scss";
// import Navbar from "./components/Nav/Navbar"
import axios from "axios";

const SERVER_URL = process.env.VUE_APP_SERVER_URL;

export default {
  name: "app",
  components: {
    // Navbar,
  },
  created: function () {
    console.log("created App")
  },
};
</script>

<style>
  /* #app {
    background-color: #f3f4fe;
  } */
</style>
