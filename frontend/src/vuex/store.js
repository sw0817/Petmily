import Vue from 'vue'
import Vuex from 'vuex'
import getters from './getters'
import actions from './actions'
import mutations from './mutations'
import axios from 'axios'

Vue.use(Vuex)

// const state = {
//     isUser: false,
//     isLogin: false,
// }

const SERVER_URL = process.env.VUE_APP_SERVER_URL

export default new Vuex.Store({
    state: {
        isUser: false,
        // isUser: true,
        alarms: 0,
        feeds: "",
        accessToken: null,
        userEmail: "아직 로그인하지 않았습니다.",
        userNickname: "",
        userId: "",
        alertUnreadFollow: [], //안 읽은 알람 - follow
        alertUnreadFeed:[], //안 읽은 알람 - feed
        messageUnread: [], //안 읽은 메시지
        unreadMessageCnt:0, //안 읽은 메시지 개수
    },
    mutations: {
        LOGIN_CHANGE: function (state, loginStatus) {
            if (loginStatus) {
                state.isUser = true
            } else {
                state.isUser = false
                localStorage.removeItem('jwt')
                state.accessToken = null
                state.userEmail = ""
                state.userNickname = ""
            }
        },
        LOGOUT: function (state) {
            localStorage.removeItem('jwt')
            state.isUser = false
            state.accessToken = null
            state.userEmail = ""
            state.userNickname = ""
            state.userId = ""
        },
        GOAL: function (state) {
            state.alarms = state.alarms + 1
        },
        GET_FEEDS: function (state, feedsInfo) {
            if (feedsInfo) {
                state.feeds = feedsInfo
            } else {
                state.feeds = ""
            }
        },
        LOGIN: function (state, userInfo) {
            axios.post(`${SERVER_URL}/member/login`, userInfo)
            .then( (res) => {
            res.data["auth-token"] =  res.data["auth-token"].replaceAll("-", "+").replaceAll("_", "/") 
            localStorage.setItem('jwt', res.data["auth-token"])
            state.isUser = true
            state.accessToken = res.data["auth-token"]
            state.userEmail = res.data["user-email"]
            state.userNickname = res.data["user-nickname"]
            state.userId = res.data["user-id"]
            })
            .catch( (err) => {
            console.log(err)
            })
        },
        setAlertUnreadFollow(state, value) {
            state.alertUnreadFollow=value
        },
        setAlertUnreadFeed(state, value) {
            state.alertUnreadFeed=value
        },
        setMessageUnread(state,value) {
            state.messageUnread=value
        },
        setMessageUnreadCnt(state,value) {
            state.unreadMessageCnt=value
        },
    },
    getters: {
        getAccessToken: function (state) {
            return state.accessToken
        },
        getUserEmail: function (state) {
            return state.userEmail
        },
        getUserNickname: function (state) {
            return state.userNickname
        },
        getUserId: function (state) {
            return state.userId
        },
        getAlertUnreadFollow(state) {
            return state.alertUnreadFollow
        },
        getAlertUnreadFeed(state) {
            return state.alertUnreadFeed
        },
        getMessageUnread(state) {
            return state.messageUnread
        },
        getMessageUnreadCnt(state) {
            return state.unreadMessageCnt
        },

    },
    actions: {
        loginChange: function ({ commit }, loginStatus) {
            commit("LOGIN_CHANGE", loginStatus)
        },
        logout: function ({ commit }) {
            commit("LOGOUT")
        },
        goalgoal: function ({ commit }) {
            commit("GOAL")
        },
        getFeeds: function ({ commit }, feedsInfo) {
            commit("GET_FEEDS", feedsInfo)
        },
        login: function ({ commit }, userInfo) {
            commit("LOGIN", userInfo)
        },
        setAlertUnreadFollow: function ({ commit }, value) {
            commit("setAlertUnreadFollow", value)
        },
        setAlertUnreadFeed: function ({ commit }, value) {
            commit("setAlertUnreadFeed", value)
        },
        setMessageUnread: function ({ commit }, value) {
            commit("setMessageUnread", value)
        },
        setMessageUnreadCnt: function ({ commit }, value) {
            commit("setMessageUnreadCnt", value)
        },

    },
})
