<template>
  <div class="card" :style="getBackgroundColor">
    <div style="display: flex; border-bottom: 1px solid;">
      <div style="flex: 2;">项目名称</div>
      <div style="flex: 1;">预约时间</div>
      <div style="flex: 1;">申请单号</div>
    </div>
    <div style="display: flex; margin-top: 5px;" v-for="item in applyList" :key="item.id">
      <div style="flex: 2;">{{ item.checkItem }}</div>
      <div style="flex: 1;">{{ item.reserveDate }}</div>
      <div style="flex: 1;">{{ item.applyNo }}</div>
    </div>
    <div
      style="margin-top: 10px; display: grid; grid-template-columns: repeat(4, 1fr); grid-gap: 5px;"
    >
      <div style="display: flex; grid-column: 1/3; align-items: center;">
        <div style="flex: 1">姓名</div>
        <div style="flex: 1">{{ personInfo.personName }}</div>
      </div>
      <div style="display: flex; align-items: center;">
        <div style="flex: 1">姓别</div>
        <div style="flex: 1">{{ personInfo.gender }}</div>
      </div>
      <div style="display: flex; align-items: center;">
        <div style="flex: 1">年龄</div>
        <div style="flex: 1">{{ personInfo.age }}</div>
      </div>
      <div style="display: flex; grid-column: 1/3; align-items: center;">
        <div style="flex: 1">检查部位</div>
        <div style="flex: 1">{{ personInfo.limb }}</div>
      </div>
      <div :style="{ gridRow: 2, gridColumn: 4, color: this.payStatus === 1 ? 'green' : 'red' }">{{ payStatus | getPayStatus }}</div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReserveInfo',
  props: {
    applyList: {
      type: Array,
      default: () => [],
    },
    personInfo: {
      type: Object,
      default: () => ({
        personName: '',
        gender: '',
        age: '',
        limb: '',
      }),
    },
    payStatus: {
      type: Number,
      default: 0,
    },
  },
  filters: {
    getPayStatus(val) {
      if (val === 1) {
        return '已支付';
      }
      return '未支付';
    },
  },
  computed: {
    getBackgroundColor() {
      if (this.payStatus === 1) {
        return { backgroundColor: '#d2ffef' };
      }
      return { backgroundColor: '#f4f4f4' };
    },
  },
};
</script>

<style lang="scss" scoped>
.card {
  border-radius: 10px;
  padding: 10px;
  margin: 10px;
  font-size: 14px;
}
</style>
