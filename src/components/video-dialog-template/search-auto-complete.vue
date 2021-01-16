<template>
  <v-card

  >
    <v-card-title class="headline red lighten-3">
      搜索连续剧
    </v-card-title>

    <v-card-text>
      <v-autocomplete
          :value="video"
          v-on:change="onChangeSelectedVideo"
          :items="items"
          :loading="isLoading"
          :search-input.sync="search"
          color="white"
          hide-no-data
          hide-selected
          label="影视信息搜索"
          placeholder="请输入内容"
          prepend-icon="mdi-database-search"
          return-object
      ></v-autocomplete>
    </v-card-text>
    <v-divider></v-divider>
    <v-expand-transition>
      <v-list
          v-if="video"
          class="red lighten-3"
      >
        <v-list-item

        >
          <v-list-item-content>
            <v-list-item-title v-text="video.text"></v-list-item-title>
            <v-list-item-subtitle v-text="video.value"></v-list-item-subtitle>
          </v-list-item-content>

          <v-list-item-content v-if="video.kind === 1">
            <v-select
                v-model="episode"
                :items="video.episodeItems"
                label="剧集"
            ></v-select>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-expand-transition>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn
          v-if="video"
          v-on:click="complete"
      >
        完成
      </v-btn>
      &nbsp;
      <v-btn
          :disabled="!video"
          color="grey darken-3"
          @click="video = null"
      >
        清空内容
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import axios from "axios";

export default {
  name: "search-auto-complete.vue",
  data: () => ({
    // auto complete api
    descriptionLimit: 60,
    entries: [],
    isLoading: false,
    video: null,
    episode: 1,
    search: null,
  }),
  computed: {

    items() {
      return this.entries.map(entry => {
        return entry;
      });
    }
  },
  watch: {
    search(val) {
      // Items have already been loaded
      // if (this.items.length > 0) return
      // Items have already been requested
      if (this.isLoading) return
      this.isLoading = true
      axios.get('/video/list', {
        params: {
          l: 0,
          r: 100,
          keywords: this.search
        }
      }).then((response) => {
        let body = response.data;
        if (body.code !== 0) {
          this.isLoading = false;
          return;
        }
        if (body.code === 0) {
          let videoInfoList = body.data;
          let count = videoInfoList.length;
          let entries = [];
          for (let i = 0; i < count; i++) {
            let episodeItems = [];
            for (let j = 0; j < videoInfoList[i].duration; j++) episodeItems.push(j + 1);
            entries.push({text: videoInfoList[i].name, value: videoInfoList[i].id, episodeItems: episodeItems, kind:videoInfoList[i].kind});
          }
          this.entries = entries;
        }
        this.isLoading = false;
      })
    }
  },
  methods: {
    complete: function () {
      this.$emit('series-info-complete', {
        id: this.video.value,
        episode: this.episode,
        kind:this.video.kind
      })
    },
    onChangeSelectedVideo: function (video) {
      this.video = video
      this.episode = 1
    }
  }
}
</script>

<style scoped>

</style>