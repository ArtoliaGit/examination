<template>
  <div class="card" :style="getBackgroundColor" @click="handleClick">
    <div style="clear: both; overflow: hidden;">
      <el-row>
        <el-col :span="8">
          <span class="label">姓名</span>
          <span class="value">{{ personInfo.personName }}</span>
        </el-col>
        <el-col :span="8">
          <span class="label">姓别</span>
          <span class="value">{{ personInfo.gender }}</span>
        </el-col>
        <el-col :span="8">
          <span class="label">年龄</span>
          <span class="value">{{ personInfo.age }}</span>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <span class="label">预约号</span>
          <span class="value">{{ id }}</span>
        </el-col>
        <el-col :span="12">
          <span class="label">预约日期</span>
          <span class="value">{{ personInfo.reserveDate }}</span>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <span class="label">时段</span>
          <span class="value">{{ personInfo.timeSlotName }}</span>
        </el-col>
        <el-col :span="12">
          <span class="label">检查项目</span>
          <span class="value">{{ personInfo.checkItemName }}</span>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReserveHistory',
  props: {
    personInfo: {
      type: Object,
      default: () => ({
        personName: '',
        gender: '',
        age: '',
        zxks: '',
        zxksmc: '',
        reserveDate: '',
      }),
    },
    isActive: {
      type: Boolean,
      default: false,
    },
    id: {
      type: String,
      default: '',
    },
    timeSlotName: {
      type: String,
      default: '',
    },
    checkItemName: {
      type: String,
      default: '',
    },
  },
  filters: {
    getPayStatus(val) {
      if (val === 1) {
        return '已支付';
      }
      return '未支付';
    },
    getGender(val) {
      if (val === '1') {
        return '男';
      } if (val === '2') {
        return '女';
      }
    },
  },
  computed: {
    getBackgroundColor() {
      if (this.isActive) {
        return { backgroundColor: '#ffc799' };
      }
      return { backgroundColor: '#d2ffef' };
    },
  },
  methods: {
    handleClick() {
      this.$emit('click', this.personInfo, this.id);
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
  cursor: pointer;
}
.label {
  margin-right: 10px;
}
.value {
  font-weight: bold;
}
</style>
