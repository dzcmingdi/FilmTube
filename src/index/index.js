import Vue from 'vue';
import vuetify from "../plugins/vuetify";
import filmTubeAppBar from '../components/filmtube-app-bar.vue';
import store from '../plugins/vuex.js';
import VueRouter from "vue-router";
import media from "../components/media/media.vue";
import show from "../components/media/show.vue";
import play from "../components/media/play.vue";
import VueCoreVideoPlayer from 'vue-core-video-player';


Vue.use(VueRouter)
Vue.use(VueCoreVideoPlayer)
new Vue({
    el: '#index',
    vuetify,
    store,
    router: new VueRouter({
        routes:
            [
                {
                    'path': '/:kind', component: media,
                    children:
                        [
                            {
                                path: 'show',
                                component: show
                            },
                            {
                                path: 'play/:id',
                                component: play
                            }
                        ]
                }
            ]
    }),
    components: {
        'filmtube-app-bar': filmTubeAppBar
    },
    created() {

    },
    data: {},
    methods: {}
})

