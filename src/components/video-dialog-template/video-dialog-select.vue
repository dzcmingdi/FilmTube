<template>
  <v-select
      v-on:change="changeVideoTemplate"
      :value="templates[template]"
      :items="templates"
      label="模板选择"
  ></v-select>
</template>

<script>
export default {
  name: "video-dialog-select.vue",
  props: ['template'],
  data: () => ({

    templates:
        [
          {text: '信息创建模板', value: 0},
          {text: '信息绑定模板', value: 1},
        ],
  }),
  methods: {
    changeVideoTemplate: function (template) {
      this.$emit('changeVideoTemplate', {template})
    }
  }
}
</script>

<style scoped>

</style>