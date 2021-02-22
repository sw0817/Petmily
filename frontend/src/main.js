import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router';
import routes from './routes'
import store from './vuex/store'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.min.css'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import VueHashtagTextarea from 'vue-hashtag-textarea'


Vue.use(VueHashtagTextarea);
Vue.use(Vuetify);
Vue.use(BootstrapVue)

Vue.config.productionTip = false

Vue.use(VueRouter)

const router = new VueRouter({
    routes,
    mode: "history"
});

new Vue({
    router,
    store,
    vuetify : new Vuetify(),
    render: h => h(App),
}).$mount('#app');


