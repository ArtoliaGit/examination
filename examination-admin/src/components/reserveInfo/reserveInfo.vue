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
          <span class="label">申请单号</span>
          <span class="value">{{ personInfo.applyNo }}</span>
        </el-col>
        <el-col :span="12">
          <span class="label">申请日期</span>
          <span class="value">{{ personInfo.applyDate }}</span>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <span class="label">医嘱名称</span>
          <span class="value">{{ personInfo.yzmc }}</span>
        </el-col>
        <el-col :span="12">
          <span class="label">医嘱分类</span>
          <span class="value">{{ personInfo.dmmc }}</span>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <span class="label">执行科室</span>
          <span class="value">{{ personInfo.zxksmc }}</span>
        </el-col>
        <el-col :span="12">
          <span class="label">支付状态</span>
          <span class="value" :style="{ color: this.payStatus === 1 ? 'green' : 'red' }">{{ payStatus | getPayStatus }}</span>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ReserveInfo',
  props: {
    personInfo: {
      type: Object,
      default: () => ({
        personName: '',
        gender: '',
        age: '',
        applyNo: '',
        applyDate: '',
        yzmc: '',
        zxks: '',
        dmmc: '',
        zxksmc: '',
      }),
    },
    payStatus: {
      type: Number,
      default: 0,
    },
    isActive: {
      type: Boolean,
      default: false,
    },
    id: {
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
      if (this.payStatus === 1) {
        return { backgroundColor: '#d2ffef' };
      }
      return { backgroundColor: '#f4f4f4' };
    },
  },
  methods: {
    handleClick() {
      // if (this.payStatus === 1) {
        this.$emit('click', this.personInfo, this.id);
      // }
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
