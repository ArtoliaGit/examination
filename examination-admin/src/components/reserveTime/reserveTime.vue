<template>
  <div>
    <div
      v-for="item in timeList"
      :key="item.id"
      class="time"
      @click="handleClick(item)"
      :style="{ background: isActive === item.id ? '#ffc799' : '#cfe5ff' }"
    >
      <div>{{ item.date }}</div>
      <div>{{ item.time }}</div>
      <div>{{ item.dept }}</div>
      <div style="float: right;">余<span style="font-size: 18px; font-weight: bold;">{{ item.num }}</span></div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReserveTime',
  props: {
    timeList: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      isActive: '',
    };
  },
  methods: {
    handleClick(item) {
      this.isActive = item.id;
      this.$emit('activeTime', item.id);
    },
  },
};
</script>

<style lang="scss" scoped>
.time {
  width: 80px;
  display: inline-block;
  margin-right: 15px;
  margin-top: 10px;
  margin-bottom: 10px;
  height: 80px;
  border-radius: 5px;
  padding: 5px;
  cursor: pointer;
  user-select: none;
}
</style>
