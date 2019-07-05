<template>
  <div>
    <div
      v-for="item in timeList"
      :key="item.id"
      class="time"
      @dblclick="handleClick(item)"
      :style="{ background: isActive === item.id ? '#ffc799' : '#cfe5ff' }"
    >
      <div>{{ item.reserveDate | getDate }}</div>
      <div>{{ item.timeSlotSe }}</div>
      <div>{{ item.name }}</div>
      <div style="float: right;">余<span style="font-size: 18px; font-weight: bold;">{{ item.availableLimit }}</span></div>
    </div>
  </div>
</template>

<script>
import {
  formatTime,
} from '@/utils/tools';

export default {
  name: 'ReserveTime',
  props: {
    timeList: {
      type: Array,
      default: () => [],
    },
  },
  filters: {
    getDate(val) {
      return formatTime(new Date(val), 'M.d');
    },
  },
  data() {
    return {
      isActive: '',
    };
  },
  watch: {
    timeList(val) {
      if (val.length === []) {
        this.isActive = '';
      }
    },
  },
  methods: {
    handleClick(item) {
      if (item.availableLimit > 0) {
        this.isActive = item.id;
        this.$emit('activeTime', item);
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.time {
  width: 90px;
  display: inline-block;
  margin-right: 15px;
  margin-top: 10px;
  margin-bottom: 10px;
  height: 90px;
  border-radius: 5px;
  padding: 5px;
  cursor: pointer;
  user-select: none;
}
</style>
