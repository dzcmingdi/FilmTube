<template>
  <v-container style="margin-top: 5%">
    <v-row justify="start">
      <v-col v-for="(film,i) in filmList" :key="i" lg="3">
        <v-card
            class="mx-auto"
            max-width="344"
        >
          <v-img
              :src="'http://' + hostname + '/video/' + film.id + '/cover' + film.coverImageType"
              height="200px"
          ></v-img>
          <v-card-title>
            {{ film.name }}
          </v-card-title>

          <v-card-subtitle>
            {{ film.title }}
          </v-card-subtitle>
          <v-card-text>

            <div class="grey--text lg-3">
              上映时间：{{ film.date }}
            </div>

          </v-card-text>
          <v-card-actions>
            <v-btn
                color="orange lighten-2"
                text
                v-on:click="videoPlayRoute(film.id,film.kind)"
            >
              观看
            </v-btn>

            <v-spacer></v-spacer>

          </v-card-actions>


        </v-card>
      </v-col>
    </v-row>
    <v-row>
      <v-col>

        <v-pagination
            :length="pageLength"
            :value="page"
            v-on:input="changeVideoPage"
        ></v-pagination>

      </v-col>

    </v-row>
  </v-container>
</template>

<script>
import filmTubeAppBar from "../filmtube-app-bar.vue";
import $ from "jquery";
import axios from "axios";

export default {
  name: "show.vue",
  components: {
    'filmtube-app-bar': filmTubeAppBar
  },

  created() {
  },
  updated() {
  },
  mounted() {
    this.init()
  },
  data: () => ({
    filmList: [],
    pageLength: 0,
    page: 1,
    kind: 0,
    hostname: window.location.hostname,
    singlePageVideoCounts: 8
  }),
  computed: {},
  watch: {
    $route(to, from) {
      this.init()
    }
  },
  methods: {
    init: function () {
      this.kind = this.$route.params.kind
      let followingFunction2 = function (res) {
        this.handleVideoListData(res.data)
        this.filmList = res.data
      }.bind(this)
      $.ajax({
        url: '/video/list',
        method: 'get',
        async: false,
        data: {
          l: 0,
          r: 8 - 1,
          kind: this.$route.params.kind
        },
        success: followingFunction2
      })
      let followingFunction1 = function (res) {
        if (res.code !== 0) return
        let videoListLength = res.data.length
        let singlePageVideoCounts = this.singlePageVideoCounts
        let pageLength = Math.round(videoListLength / singlePageVideoCounts)
        if (videoListLength > singlePageVideoCounts * pageLength) pageLength += 1
        this.pageLength = pageLength
      }.bind(this)
      $.ajax({
        url: '/video/list/length',
        data: {
          kind: this.$route.params.kind
        },
        type: 'get',
        async: false,
        success: followingFunction1
      })
    },
    getFilmsFromPageNumber: function (number) {
      axios({
        url: '/video/list',
        method: 'get',
        params: {
          l: (number - 1) * 8,
          r: number * 8 - 1,
          kind: this.kind
        }
      }).then((response) => {
        let body = response.data
        // 由格式转换为后缀
        this.handleVideoListData(body.data)
        this.filmList = body.data
      })
    },
    changeVideoPage: function (number) {
      this.page = number
      this.getFilmsFromPageNumber(number)
    },
    videoPlayRoute: function (id, kind) {
      this.$router.push('play/' + id)

    },

    handleVideoListData: function (videoList) {
      for (let i = 0; i < videoList.length; i++) {
        switch (videoList[i].coverImageType) {
          case "image/jpeg":
            videoList[i].coverImageType = '.jpeg'
            break
          case "image/png":
            videoList[i].coverImageType = '.png'
            break
          default:
            videoList[i].coverImageType = '.jpeg'
        }
      }
    }
  }
}
</script>

<style scoped>

</style>