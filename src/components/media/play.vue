<template>
  <v-container style="margin-top: 5%">
    <v-row justify="start">

      <v-col lg="9">

        <video
            id="film-tube-player"
            style="width: 100%;height: 100%"
            controls
            controlsList="nodownload"
            v-if="selectedEpisode.type !== undefined"
            :src="'http://'  + hostname +  '/video/'+ $route.params.id  + '/' + seriesEpisodeSrc  + 'src' + selectedEpisode.type">
        </video>


      </v-col>

      <v-col lg="3">
        <v-chip-group
            active-class="primary--text"
            mandatory
            column
            :value="selectedEpisode.episode"
            v-on:change="changeEpisode"
            v-if="videoInfo.kind === 1"
        >
          <v-chip v-for="(episode,i) in episodes" :key="i">
            {{ episode }}
          </v-chip>

        </v-chip-group>
      </v-col>

    </v-row>

  </v-container>


</template>

<script>
import axios from "axios";

export default {
  name: "play.vue",
  data: () => ({
    videoInfo: {},
    selectedEpisode: {
      episode: 0,
      type: undefined
    },
    hostname:window.location.hostname
  }),
  computed: {
    episodes: {
      get: function () {
        let episodes = []
        if (this.videoInfo === undefined) return episodes
        for (let i = 1; i <= this.videoInfo.duration; i++) {
          episodes.push(i)
        }

        return episodes
      },
      set: function () {
      }
    },
    seriesEpisodeSrc: {
      get: function () {
        if (this.videoInfo.kind === 0) return ''
        else if (this.videoInfo.kind === 1) return (this.selectedEpisode.episode + 1).toString() + '/'
      },
      set: function () {
      }
    }
  },
  created() {

    axios({
      url: '/video/' + this.$route.params.id + '/info',
      method: 'get',
    }).then(function (response) {
      let body = response.data
      this.videoInfo = body.data
    }.bind(this))
    axios({
      url: '/video/play/src',
      method: 'get',
      params: {
        id: this.$route.params.id,
        episode: 1
      }
    }).then(function (response) {
      let body = response.data
      if (body.data.type !== undefined) {
        this.$set(this.selectedEpisode, 'type', body.data.type)
      }
    }.bind(this))

  },
  mounted() {
  },
  watch: {
    $route(to, from) {
      console.log(to)
    }
  },
  methods: {
    changeEpisode: function (episode) {
      this.$set(this.selectedEpisode, 'episode', episode)
      // 事件传来的episode是从0开始计数
      // 数据库从1开始计数
      // 故请求时episode + 1
      axios({
        url: '/video/play/src',
        method: 'get',
        params: {
          id: this.$route.params.id,
          episode: episode + 1
        }
      }).then(function (response) {
        let body = response.data
        this.$set(this.selectedEpisode, 'type', body.data.type)
      }.bind(this))
    }
  }
}
</script>

<style scoped>

</style>