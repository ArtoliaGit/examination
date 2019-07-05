<template>
  <div>
    <el-row class="content">
      <el-col :span="7" class="left">
        <el-row class="first">
          <el-row class="search" :gutter="20">
            <el-col :span="16">
              <el-input type="string" size="mini" v-model="applyNo" clearable />
            </el-col>
            <el-col :span="6">
              <el-button
                type="primary"
                size="mini"
                @click="handleSearch"
              >
                查询
              </el-button>
            </el-col>
          </el-row>
          <el-row class="single">
            <el-radio-group v-model="type">
              <el-radio :label="1">申请单号</el-radio>
              <el-radio :label="2">门诊号</el-radio>
              <el-radio :label="3">住院号</el-radio>
            </el-radio-group>
          </el-row>
        </el-row>
        <el-row class="second">
          <el-row class="title">预约信息</el-row>
          <el-row class="reserveInfo">
            <reserve-info
              v-for="item in reserveInfoList"
              :key="item.id"
              :id="item.id"
              :person-info="item.personInfo"
              :pay-status="item.payStatus"
              :is-active="item.isActive"
              @click="handleReserveInfoClick"
            />
          </el-row>
        </el-row>
        <el-row class="third">
          <el-row>
            <span style="margin-right: 20px; font-weight: bold;">预约历史</span>
            <el-button type="primary" size="mini" @click="handlePrint" :disabled="btnStatus">打印</el-button>
            <el-button type="primary" size="mini" @click="cancelReserve" :disabled="btnStatus">退约</el-button>
            <el-button type="primary" size="mini" @click="changeReserve" :disabled="btnStatus">改约</el-button>
          </el-row>
          <el-row style="overflow: auto; height: 200px;">
            <reserve-history
              v-for="item in reserveHistory"
              :key="item.id"
              :id="item.id"
              :person-info="item.personInfo"
              :is-active="item.isActive"
              @click="handleReserveHistoryClick"
            />
          </el-row>
        </el-row>
      </el-col>
      <el-col :span="17" class="right">
        <div class="first">
          <div class="title">预约时间</div>
          <div class="reserve-date-content">
            <reserve-date :date-list="dateList" @activeDate="getSelectDate" />
          </div>
        </div>
        <div class="second">
          <div style="font-weight: bold;">预约时段</div>
          <div class="reserve-time-content">
            <reserve-time :time-list="timeList" @activeTime="handleActiveTime" />
          </div>
        </div>
      </el-col>
    </el-row>
    <reserve-form
      v-if="dialogVisible"
      :check-item="checkItem"
      :check-item-name="checkName"
      :person-info="personInfo"
      :reserve-date="reserveDate"
      :time-slot="timeSlot"
      :time-slot-name="timeSlotName"
      :print-status="printStatus"
      :reserve-no="reserveNo"
      :organ-name="organName"
      :way="way"
      @cancel="handleCancel"
      @reserve="handleReserve"
    />
    <ReservePrint
      v-if="dialogPrintVisible"
      :person-info="personInfo"
      :time-slot-name="timeSlotName"
      :reserve-date="reserveDate"
      :check-item-name="checkName"
      :reserve-no="reserveNo"
      :html="html"
      :organ-name="organName"
      @print="handlePrintFinish"
      @cancel="handlePrintCancel"
    />
  </div>
</template>

<script>
import ReserveInfo from '@/components/reserveInfo';
import ReserveDate from '@/components/reserveDate';
import ReserveTime from '@/components/reserveTime';
import ReserveForm from '@/components/reserveForm';
import ReserveHistory from '@/components/reserveHistory';
import ReservePrint from '@/components/reserveForm/reservePrint';
import {
  getReserveCalendar,
  getTimeSlot,
  getReservePersonInfo,
  getList,
  save,
  cancelReserve,
} from '@/api/reservation';
import { getCheckItemByConditions } from '@/api/checkItem';
import { formatTime } from '@/utils/tools';
import { list } from '@/api/notice';
import { getOrganByConditions } from '@/api/organ';

export default {
  name: 'Reservation',
  components: {
    ReserveInfo,
    ReserveDate,
    ReserveTime,
    ReserveForm,
    ReserveHistory,
    ReservePrint,
  },
  data() {
    return {
      type: 1,
      applyNo: '',
      reserveInfoList: [],
      reserveHistory: [],
      isActive: 0,
      dateList: [],
      timeList: [],
      checkItem: '',
      checkItems: [],
      checkName: '',
      reserveDate: '',
      dialogVisible: false,
      personInfo: null,
      dept: '',
      timeSlot: '',
      timeSlotName: '',
      startTime: '',
      endTime: '',
      printStatus: true,
      reserveNo: '',
      noticeList: [],
      html: '',
      dialogPrintVisible: false,
      btnStatus: true,
      organName: '',
      way: '',
    };
  },
  methods: {
    getSelectDate(date) {
      this.reserveDate = date;
      this.getTimeSlot();
    },
    getReserveCalendar() {
      if (this.checkItems.length === 0) {
        return;
      }
      const params = {
        checkItem: this.checkItems.map(item => item.code).join(','),
      };
      getReserveCalendar(params).then((res) => {
        if (res.code === 200) {
          this.dateList = res.data;
          this.reserveDate = this.dateList[0].date;
        } else {
          this.dateList = [];
        }
        this.getTimeSlot();
      });
    },
    getTimeSlot() {
      const params = {
        checkItem: this.checkItems.map(item => item.code).join(','),
        reserveDate: this.reserveDate,
      };
      getTimeSlot(params).then((res) => {
        if (res.code === 200) {
          this.timeList = res.data;
        } else {
          this.timeList = [];
        }
      });
    },
    getReservePersonInfo() {
      if (!this.applyNo) {
        return;
      }
      const params = {
        applyNo: this.applyNo,
        type: this.type,
      };
      let data;
      getReservePersonInfo(params).then((res) => {
        if (res.code === 200) {
          data = res.data.map(item => ({
            id: item.sqdh,
            personInfo: {
              personName: item.brxm,
              gender: item.brxb,
              age: item.brnl,
              idCard: item.sfzh,
              zxks: item.zxks,
              zxksmc: item.zxksmc,
              dmmc: item.dmmc,
              yzfl: item.yzfl,
              yzmc: item.yzmc,
              applyNo: item.sqdh,
              applyDate: formatTime(new Date(item.sqrq), 'yyyy-MM-dd'),
              birthday: item.csny,
              brid: item.sfzh,
              mzhm: item.mzhm,
            },
            payStatus: item.sfbz === '已收费' ? 1 : 0,
          }));
        }
        const para = {
          apply_no: this.applyNo,
        };
        return getList(para);
      }).then((res) => {
        if (res.code === 200) {
          this.reserveHistory = res.data.map(item => ({
            id: item.id,
            personInfo: {
              checkItem: item.checkItem,
              checkItemName: item.checkItemName,
              reserveDate: formatTime(new Date(item.reserveDate), 'yyyy-MM-dd'),
              timeSlot: item.timeSlot,
              timeSlotName: `${item.startTime}-${item.endTime}`,
              ...data.filter(x => x.id === item.applyNo)[0].personInfo,
            },
          }));
          this.reserveInfoList = data.filter(item => !res.data.map(x => x.applyNo).includes(item.id));
        }
      });
    },
    getList() {
      if (!this.applyNo) {
        return;
      }
      const params = {
        apply_no: this.applyNo,
      };
      getList(params).then((res) => {
        if (res.code === 200) {
          this.reserveHistory = res.data.map(item => ({
            id: item.id,
            personInfo: {
              personName: item.personName,
              gender: item.gender,
              age: item.age,
              checkItem: item.check_item,
            },
            payStatus: 1,
          }));
        }
      });
    },
    handleSearch() {
      this.getReservePersonInfo();
    },
    handleReserveInfoClick(val, id) {
      this.reserveInfoList.forEach((item) => {
        if (item.id === id) {
          this.$set(item, 'isActive', true);
          this.personInfo = item.personInfo;
          this.reserveNo = '';
          this.btnStatus = true;
        } else {
          this.$set(item, 'isActive', false);
        }
      });
      this.reserveHistory.forEach((item) => {
        if (item.id === id) {
          this.$set(item, 'isActive', true);
        } else {
          this.$set(item, 'isActive', false);
        }
      });
      this.getCheckItemByConditions(val.zxksmc, val.dmmc);
    },
    handleReserveHistoryClick(val, id) {
      this.reserveInfoList.forEach((item) => {
        if (item.id === id) {
          this.$set(item, 'isActive', true);
          this.personInfo = item.personInfo;
        } else {
          this.$set(item, 'isActive', false);
        }
      });
      this.reserveHistory.forEach((item) => {
        if (item.id === id) {
          this.$set(item, 'isActive', true);
          this.personInfo = item.personInfo;
          this.reserveNo = item.id;
          this.reserveDate = item.personInfo.reserveDate;
          this.timeSlotName = item.personInfo.timeSlotName;
          this.timeSlot = item.personInfo.timeSlot;
          this.checkItem = item.personInfo.checkItem;
          this.checkName = item.personInfo.checkItemName;
          this.checkItems = [{ code: item.personInfo.checkItem }];
          this.btnStatus = false;
          this.dateList = [];
          this.timeList = [];
        } else {
          this.$set(item, 'isActive', false);
        }
      });
      this.getNotice();
    },
    getCheckItemByConditions(zxks, yzfl) {
      const params = {
        dept: zxks,
        medical_order: yzfl,
      };
      getCheckItemByConditions(params).then((res) => {
        if (res.code === 200 && res.data.length > 0) {
          this.checkItems = res.data;
          this.getReserveCalendar();
        }
      });
    },
    handleActiveTime(item) {
      this.dialogVisible = true;
      this.timeSlot = item.timeSlot;
      this.timeSlotName = item.timeSlotSe;
      this.startTime = item.startTime;
      this.endTime = item.endTime;
      this.checkItem = item.checkItem;
      this.checkName = item.name;
      this.dept = item.dept;
      this.way = item.way;
      this.printStatus = true;
    },
    handleCancel() {
      this.dialogVisible = false;
      this.reserveNo = '';
    },
    handleReserve(checkLimbs, urgentStatus, selectLimit) {
      const data = {
        reserveDate: this.reserveDate,
        timeSlot: this.timeSlot,
        applyNo: this.personInfo.applyNo,
        personName: this.personInfo.personName,
        gender: this.personInfo.gender,
        birthday: this.personInfo.birthday,
        age: this.personInfo.age,
        checkItem: this.checkItem,
        dept: this.personInfo.zxks,
        partNum: selectLimit,
        checkPart: checkLimbs.join(','),
        urgentStatus,
        createUser: this.$store.state.user.userName,
        createTime: '',
        createUnit: this.$store.state.user.organ,
        startTime: this.startTime,
        endTime: this.endTime,
        brid: this.personInfo.brid,
        mzhm: this.personInfo.mzhm,
        checkItemName: this.checkName,
        id: this.reserveNo,
      };
      save(data).then((res) => {
        if (res.code) {
          this.$message({
            type: 'success',
            message: '预约成功',
          });
          this.printStatus = false;
          this.reserveNo = res.data;
          this.dateList = [];
          this.timeList = [];
          this.getReservePersonInfo();
        }
      });
    },
    handlePrint() {
      this.dialogPrintVisible = true;
    },
    cancelReserve() {
      const params = {
        id: this.reserveNo,
      };
      cancelReserve(params).then((res) => {
        if (res.code === 200) {
          this.$message({
            type: 'success',
            message: '退约成功',
          });
          this.getReservePersonInfo();
        }
      });
    },
    changeReserve() {
      this.getReserveCalendar();
    },
    handlePrintFinish() {
      this.dialogPrintVisible = false;
    },
    handlePrintCancel() {
      this.dialogPrintVisible = false;
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
    getOrganName() {
      getOrganByConditions().then((res) => {
        if (res.code === 200 && res.data.length > 0) {
          this.organName = res.data[0].organname;
        }
      });
    },
  },
  created() {
    this.getOrganName();
    console.log('created');
  },
};
</script>

<style lang="scss" scoped>
.content {
  height: calc(100vh - 125px);
  user-select: none;
}
.left {
  border-right: 2px solid #d1d1d1;
  height: calc(100vh - 80px);
  display: flex;
  flex-direction: column;
  .first {
    border-bottom: 1px solid #d1d1d1;
    flex: 1;
    .title {
      font-weight: bold;
    }
    .search {
      margin-top: 10px;
      .el-button {
        margin-left: 10px;
      }
    }
    .single {
      margin-top: 10px;
      margin-bottom: 10px;
    }
  }
  .second {
    flex: 3;
    padding-top: 10px;
    border-bottom: 1px solid #d1d1d1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    .title {
      font-weight: bold;
      flex: 1,
    }
    .reserveInfo {
      overflow: hidden;
      overflow-y: auto;
      flex: 12;
    }
  }
  .third {
    flex: 3;
    padding-top: 5px;
  }
}
.right {
  padding-left: 10px;
  .first {
    height: 200px;
    .title {
      font-weight: bold;
    }
    .reserve-date-content {
      padding: 5px;
    }
  }
  .second {
    padding: 5px;
    .reserve-time-content {
      height: 250px;
      overflow: auto;
    }
  }
}
.el-radio {
  margin-right: 20px;
}
</style>
