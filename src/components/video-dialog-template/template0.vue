<template>
  <v-card>
    <v-card-title>
      <span class="headline">电影/连续剧 信息创建</span>
    </v-card-title>
    <v-card-text>

      <v-container>
        <small>*表示必填</small>
        <v-row>

          <v-col cols="12">
            <v-text-field
                label="名称*"
                required
                v-model="videoInfo.name"
            ></v-text-field>
          </v-col>
          <v-col cols="12">
            <v-text-field
                label="标题*"
                required
                v-model="videoInfo.title">

            </v-text-field>
          </v-col>
          <v-col cols="12">
            <v-text-field
                label="简介*"
                required
                v-model="videoInfo.content"
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field v-if="videoInfo.kind === 0"
                          label="时长*（以分钟为单位）"
                          required
                          v-model="videoInfo.duration"
            ></v-text-field>
            <v-text-field v-else label="剧集数*" required v-model="videoInfo.duration">

            </v-text-field>
          </v-col>
          <v-col>
            <v-select
                v-model="videoInfo.kind"
                :items="[{text:'电影',value:0},{text:'连续剧',value:1}]"
                clearable
                label="视频种类"
            ></v-select>
          </v-col>


        </v-row>
        <v-row>
          <v-col>
            <v-date-picker v-model="picker" color="#2196F3" v-on:change="changeVideoDate"></v-date-picker>
          </v-col>
          <v-col>
            <v-text-field disabled :value="videoInfo.date" label="上映日期"></v-text-field>
          </v-col>
        </v-row>

        <v-row>
          <v-col>
            <v-file-input v-model="videoCoverImage"
                          placeholder="请上传封面（接受*.jpg,*.jpeg,*.png,*.img等图片格式）"
                          outlined dense></v-file-input>
          </v-col>
        </v-row>
      </v-container>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>

      <!--      <v-btn color="blue darken-1"-->
      <!--             text @click="$root.$children[0].changeVideoTemplate">切换模板-->
      <!--      </v-btn>-->

      <video-dialog-select @changeVideoTemplate="changeVideoTemplate" template="0"></video-dialog-select>
      <v-btn
          color="blue darken-1"
          text
          @click="$root.dialogShow({option:'close'})"
      >
        关闭
      </v-btn>
      <v-btn
          color="blue darken-1"
          text
          @click="uploadVideoInfo"
      >
        上传
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import axios from "axios";
import videoDialogSelect from './video-dialog-select.vue'

export default {
  name: "template0",
  components: {videoDialogSelect},
  props: ['videoType','number'],
  data: () => ({
    videoInfo: {
      date: "",
      name: "",
      title: "",
      content: "",
      duration: "",
      kind: 0,
      coverImageType: "",
      episode: undefined
    },
    videoCoverImage: undefined,
    // date picker
    picker: new Date().toISOString().substr(0, 10),
  }),
  methods: {
    uploadVideoInfo: function () {
      let videoInfo = this.videoInfo
      videoInfo.coverImageType = this.videoCoverImage.type
      videoInfo.duration = parseInt(videoInfo.duration)
      let followingFunction2 = function (response) {
        let body = response.data;
        if (body.code !== 0) alert("上传失败")
      }
      let followingFunction1 = function (response) {
        let body = response.data;
        if (body.code !== 0) {
          alert("上传失败")
        }
        this.$root.dialogShow({
          option: 'upload',
          number: this.number,
          id: body.data.id,
          kind: videoInfo.kind,
          type: this.videoType,
          episode: videoInfo.kind === 0 ? undefined : 1
        })
        // this.changeDialogStatus();
        let formData = new FormData()
        formData.append("image", this.videoCoverImage)
        return axios({
          url: '/video/upload/request/cover',
          method: 'post',
          headers: {
            'content-type': 'application/form-data'
          },
          data: formData,
          params: {
            id: body.data.id,
            type: this.videoCoverImage.type
          }
        })

      }.bind(this)
      axios
      (
          {
            url: '/video/upload/request',
            data: {
              video: videoInfo,
              type: this.videoType,
            },
            headers: {
              'Content-type': 'application/json;charset=UTF-8'
            },
            method: 'post'
          }
      ).then(followingFunction1).then(followingFunction2)
    },
    // changeDialogStatus: function (args) {
    //   this.$root.dialogShow(args)
    // },
    changeVideoDate: function (str) {
      this.$set(this.videoInfo, 'date', str)
    },
    changeVideoTemplate: function (args) {
      this.$emit('changeVideoTemplate', args)
    }
  }
}
</script>

<style scoped>

</style>