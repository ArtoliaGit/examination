<template>
  <div>
    <i
      class="iconfont icon-nav"
      :style="{transform: (isCollapse ? 'rotate(90deg)' : 'rotate(0deg)'), display: 'inline-block'}"
      @click="handleClick"
    />
  </div>
</template>

<script>
export default {
  name: 'SideTrigger',
  props: {
    isCollapse: {
      type: Boolean,
      default: false,
    },
  },
  methods: {
    handleClick() {
      this.$emit('on-change', !this.isCollapse);
    },
  },
};
</script>
