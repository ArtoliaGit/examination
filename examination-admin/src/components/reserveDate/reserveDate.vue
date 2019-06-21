<template>
  <div>
    <div v-for="item in dateList" :key="item.date" class="day">
      <div class="notice">
        <span v-if="item.day === 1">{{ `${item.month}月` }}</span>
      </div>
      <div
        class="cube"
        @click="handleClick(item)"
        :style="{ background: isActive === item.date ? '#ffc799' : '#cfe5ff' }"
      >
        {{ item.day }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReserveDate',
  props: {
    dateList: {
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
      this.isActive = item.date;
      this.$emit('activeDate', item.date);
    },
  },
};
</script>

<style lang="scss" scoped>
.day {
  width: 40px;
  display: inline-block;
  margin: 5px;
  .notice {
    height: 20px;
    width: 100%;
    font-size: 10px;
    text-align: center;
    line-height: 20px;
  }
  .cube {
    height: 40px;
    width: 100%;
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    user-select: none;
  }
}
</style>
