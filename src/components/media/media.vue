<template>
  <v-app>
    <filmtube-app-bar></filmtube-app-bar>
    <router-view></router-view>
  </v-app>
</template>

<script>
import show from "./show.vue";
import filmTubeAppBar from '../filmtube-app-bar.vue'

export default {
  name: "media.vue",
  components: {
    'show': show,
    'filmtube-app-bar': filmTubeAppBar
  },
  data: () => ({
  }),
  methods: {}
}
</script>

<style scoped>

</style>