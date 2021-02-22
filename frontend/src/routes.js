import Login from './views/user/Login.vue'
import SignUp from './views/user/SignUp.vue'
import UserProfile from './views/user/UserProfile.vue'
import ModifyUser from './views/user/ModifyUser.vue'
import ChangePW from './views/user/ChangePW.vue'
import AddPet from './views/pet/AddPet.vue'
import ModifyPet from './views/pet/ModifyPet.vue'
import FeedMain from './views/feed/IndexFeed.vue'
import ReadFeed from './views/feed/ReadFeed.vue'
import ModifyFeed from './views/feed/ModifyFeed.vue'
import CreateFeed from './views/feed/CreateFeed.vue'
import Find from './views/user/Find.vue'
import MyChatRoom from './views/user/MyChatRoom.vue'
import Chat from './views/user/Chat.vue'
import Calendar from './views/diary/Calendar.vue'
import WriteDiary from './views/diary/WriteDiary.vue'
import MyDiary from './views/diary/MyDiary.vue'
import ModifyDiary from './views/diary/ModifyDiary.vue'
import SearchMap from './views/map/SearchMap.vue'


export default [

    {
        path : '/',
        name : 'Login',
        component : Login
    },
    {
        path : '/user/signup',
        name : 'SignUp',
        component : SignUp
    },
    {
        path : '/user/profile/:user_id',
        name : 'UserProfile',
        component : UserProfile
    },
    {
        path : '/user/profile/:user_id/addpet',
        name : 'AddPet',
        component : AddPet
    },
    {
        path : '/pet/modify/:user_id/:pet_id',
        name : 'ModifyPet',
        component : ModifyPet
    },
    { //마이페이지 상세-수정/탈퇴
        path : '/user/profile/:user_id/modifyuser',
        name : 'ModifyUser',
        component : ModifyUser
    },
    { //마이페이지 상세-비밀번호 변경
        path : '/user/profile/:user_id/changepw',
        name : 'ChangePW',
        component : ChangePW
    },
    {
        path : '/feed/main',
        name : 'FeedMain',
        component : FeedMain
    },
    {
        path : '/feed/:feed_id',
        name : 'ReadFeed',
        component : ReadFeed
    },
    {
        path : '/feed/modify/:feed_id',
        name : 'ModifyFeed',
        component : ModifyFeed
    },
    {
        path : '/feed/create',
        name : 'CreateFeed',
        component : CreateFeed
    },
    {
        path: '/user/Find',
        name: 'Find',
        component : Find
    },
    {
        path: '/diary',
        name: 'Calendar',
        component : Calendar
    },
    {
        path: '/diary/write',
        name: 'WriteDiary',
        component : WriteDiary
    },
    {
        path: '/diary/view/:year/:month/:day',
        name: 'MyDiary',
        component : MyDiary
    },
    {
        path: '/diary/view/:year/:month/:day/modify',
        name: 'ModifyDiary',
        component : ModifyDiary
    },
    {
        path : '/user/profile/:user_id/chat',
        name : 'Chat',
        component : Chat
    },
    {
        path : '/user/profile/:user_id/mychat',
        name : 'MyChatRoom',
        component : MyChatRoom
    },
    {
        path:'/map/searchmap',
        name:'SearchMap',
        component :SearchMap
    }

]
