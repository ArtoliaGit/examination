<template>
  <div class="page">
    <el-card class="card">
      <div class="title">{{ checkItemName }}预约单</div>
      <el-row>
        <el-col :span="8">姓名：{{ personInfo.personName }}</el-col>
        <el-col :span="8">性别：{{ personInfo.gender }}</el-col>
        <el-col :span="8">年龄：{{ personInfo.age }}</el-col>
      </el-row>
      <el-row>
        <el-col :span="8">执行科室：{{ personInfo.zxksmc }}</el-col>
        <el-col :span="8">预约日期：{{ reserveDate }}</el-col>
        <el-col :span="8">预约时段：{{ timeSlotName }}</el-col>
      </el-row>
      <hr>
      <el-row style="height: 60px;" v-if="way === '2'">
        <div style="font-weight: bold; margin-bottom: 10px;">检查部位选择</div>
        <div>
          <el-checkbox-group v-model="checkLimbs" @change="handleChange">
            <el-checkbox
              :label="item.name"
              v-for="item in limbDic"
              :key="item.lid"
            />
          </el-checkbox-group>
        </div>
      </el-row>
      <el-row style="margin-top: 20px;">
        <el-checkbox v-model="urgentStatus" true-label="1" false-label="0">
          <span style="color: red;">加急</span>
        </el-checkbox>
      </el-row>
      <el-row style="margin-top: 20px; margin-bottom: 10px;" v-if="way === '2'">
        <span style="margin-right: 40px;">
          当前时段可预约基数 {{ avaliableLimit }} 个
        </span>
        <span>已选择基数 <span :style="getColor">{{ selectLimit }}</span> 个</span>
      </el-row>
      <el-row class="btn-row">
        <el-button
          type="primary"
          size="mini"
          :disabled="reserveStatus"
          @click="handleReserve"
        >
          预约
        </el-button>
        <el-button
          type="primary"
          size="mini"
          :disabled="printStatus"
          @click="handlePrint"
        >
          打印预约单
        </el-button>
        <el-button
          type="primary"
          size="mini"
          @click="handleCancel"
        >
          关闭
        </el-button>
      </el-row>
    </el-card>
    <ReservePrint
      v-if="dialogVisible"
      :person-info="personInfo"
      :time-slot-name="timeSlotName"
      :reserve-date="reserveDate"
      :check-item-name="checkItemName"
      :reserve-no="reserveNo"
      :html="html"
      :organ-name="organName"
      @print="handlePrintFinish"
      @cancel="handlePrintCancel"
    />
  </div>
</template>

<script>
import { getListByCheckItem } from '@/api/limb';
import { getResourceByConditions } from '@/api/reserveResource';
import ReservePrint from './reservePrint';
import { list } from '@/api/notice';

export default {
  name: 'ReserveForm',
  components: {
    ReservePrint,
  },
  data() {
    return {
      urgentStatus: '0',
      limbDic: [],
      checkLimbs: [],
      avaliableLimit: 0,
      selectLimit: 0,
      reserveStatus: false,
      dialogVisible: false,
      noticeList: [],
      html: '',
    };
  },
  props: {
    checkItem: {
      type: String,
      default: '',
    },
    checkItemName: {
      type: String,
      default: '',
    },
    personInfo: {
      type: Object,
      default: null,
    },
    reserveDate: {
      type: String,
      default: '',
    },
    timeSlot: {
      type: String,
      default: '',
    },
    timeSlotName: {
      type: String,
      default: '',
    },
    printStatus: {
      type: Boolean,
      default: true,
    },
    reserveNo: {
      type: String,
      default: '',
    },
    organName: {
      type: String,
      default: '',
    },
    way: {
      type: String,
      default: '',
    },
  },
  computed: {
    getColor() {
      if (this.selectLimit > this.avaliableLimit) {
        return { color: 'red' };
      }
      return { color: 'green' };
    },
  },
  methods: {
    getLimbDic() {
      const params = {
        checkItem: this.checkItem,
      };
      getListByCheckItem(params).then((res) => {
        if (res.code === 200) {
          this.limbDic = res.data.map(item => ({
            lid: item.lid,
            name: item.name,
            num: item.num,
            checked: false,
          }));
        } else {
          this.limbDic = [];
        }
      });
    },
    getResourceByConditions() {
      const params = {
        check_item: this.checkItem,
        reserve_date: this.reserveDate,
        time_slot: this.timeSlot,
      };
      getResourceByConditions(params).then((res) => {
        if (res.code === 200 && res.data.length > 0) {
          this.avaliableLimit = res.data[0].availableLimit;
        }
      });
    },
    handleChange() {
      this.selectLimit = this.limbDic
        .filter(item => this.checkLimbs.includes(item.name))
        .map(item => item.num)
        .reduce((x, y) => x + y);
    },
    handleCancel() {
      this.$emit('cancel');
    },
    handleReserve() {
      if (this.selectLimit <= this.avaliableLimit) {
        this.$emit('reserve', this.checkLimbs, this.urgentStatus, this.selectLimit);
      } else {
        this.$message({
          type: 'warning',
          message: '超出可预约基数，不能进行预约',
        });
      }
    },
    handlePrint() {
      this.dialogVisible = true;
    },
    handlePrintFinish() {
      this.dialogVisible = false;
    },
    getNotice() {
      const params = {
        checkItem: this.checkItem,
      };
      list(params).then((res) => {
        if (res.code === 200 && res.data.length > 0) {
          this.noticeList = res.data;
          this.html = res.data.map((item, index) => `<p style="margin: 0;">${index + 1}、${item.info}</p>`).reduce((x, y) => x + y);
        }
      });
    },
    handlePrintCancel() {
      this.dialogVisible = false;
    },
  },
  mounted() {
    this.getLimbDic();
    this.getResourceByConditions();
    this.getNotice();
  },
};
</script>

<style lang="scss" scoped>
.page {
  position: fixed;
  width: 100vw;
  height: 100vh;
  left: 0;
  top: 0;
  z-index: 800;
  background-color: rgba(0, 0, 0, 0.4);
  .card {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 600px;
    z-index: 999;
    .title {
      font-size: 24px;
      font-weight: bold;
      text-align: center;
      margin-bottom: 20px;
    }
    hr {
      border-width: 0.5px;
      border-color: black;
    }
    .btn-row {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
      .el-button--mini {
        width: 100px;
      }
    }
  }
}
</style>
