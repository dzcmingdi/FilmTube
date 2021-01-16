<template>
  <v-card>
    <v-card-title>
      <span class="headline" >电影/连续剧 资源绑定</span>
    </v-card-title>
    <v-container>
      <v-row >
        <v-col>
          <search-auto-complete @series-info-complete="seriesInfoComplete"></search-auto-complete>
        </v-col>
      </v-row>
    </v-container>
    <v-card-actions>
      <v-spacer></v-spacer>
      <video-dialog-select @changeVideoTemplate="changeVideoTemplate" template="1"></video-dialog-select>
      <v-btn
          color="blue darken-1"
          text
          @click="$root.dialogShow({option:'close'})"
      >
        关闭
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import searchAutoComplete from './search-auto-complete.vue'
import axios from "axios";
import VideoDialogSelect from './video-dialog-select.vue'

export default {
  name: "template1",
  components: {
    VideoDialogSelect,
    'search-auto-complete': searchAutoComplete
  },
  props:['videoType','number'],
  data: () => ({
    videoInfo:{}
  }),
  methods: {
    changeVideoTemplate: function (args) {
      this.$emit('changeVideoTemplate',args)
    },
    seriesInfoComplete: function (args) {
      axios.post('/video/bind', {
        id: args.id,
        episode: args.episode,
        type: this.videoType,
      }).then((response) => {
        let body = response.data
        if (body.code !== 0) return
        this.$root.dialogShow({
          option: 'upload',
          number: this.number,
          id: args.id,
          kind: args.kind,
          episode: args.episode
        })
      })
    }
  }
}
</script>

<style scoped>

</style>