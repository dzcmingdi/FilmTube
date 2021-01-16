<template>
  <v-row justify="center">

    <v-dialog
        v-model="show"
        max-width="600px"
        persistent
    >
      <template0 v-if="template === 0" @changeVideoTemplate="changeVideoTemplate" :videoType="videoType" :number="number"></template0>
      <template1 v-if="template === 1" @changeVideoTemplate="changeVideoTemplate" :videoType="videoType" :number="number"></template1>
    </v-dialog>
  </v-row>
</template>

<script>
import axios from 'axios';
import SearchAutoComplete from './video-dialog-template/search-auto-complete.vue'
import template0 from "./video-dialog-template/template0.vue";
import template1 from "./video-dialog-template/template1.vue";

export default {
  components: {template1, SearchAutoComplete, template0},
  props: ['show', 'number', 'videoType'],
  data: () => ({
    // which template
    template: 0,
    components: {
      'search-auto-complete': SearchAutoComplete
    },
  }),


  methods: {
    changeVideoTemplate: function (args) {
      this.template = args.template
      // switch (template) {
      //   case 'film':
      //     this.$set(this.videoInfo, 'kind', 0);
      //     break;
      //
      //   case 'series':
      //     this.$set(this.videoInfo, 'kind', 1);
      // }
    },

    changeDialogStatus: function (args) {
      this.$emit('dialog-show', args);
    },


  }
}
</script>

<style scoped>

</style>