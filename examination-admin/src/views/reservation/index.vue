<template>
  <div>
    <el-row class="content">
      <el-col :span="7" class="left">
        <el-row class="first">
          <el-row class="title">预约申请</el-row>
          <el-row class="search" :gutter="20">
            <el-col :span="16">
              <el-input type="string" size="mini" />
            </el-col>
            <el-col :span="6">
              <el-button type="primary" size="mini">查询</el-button>
            </el-col>
          </el-row>
          <el-row class="single">
            <el-radio-group v-model="type">
              <el-radio :label="1">申请单号</el-radio>
              <el-radio :label="2">门诊号</el-radio>
              <el-radio :label="3">住院号</el-radio>
              <el-radio :label="4">体检号</el-radio>
            </el-radio-group>
          </el-row>
        </el-row>
        <el-row class="second">
          <el-row class="title">预约信息</el-row>
          <el-row class="reserveInfo">
            <reserve-info
              v-for="item in reserveInfoList"
              :key="item.id"
              :apply-list="item.applyList"
              :person-info="item.personInfo"
              :pay-status="item.payStatus"
            />
          </el-row>
        </el-row>
        <el-row class="third">
          <el-row>
            <span style="margin-right: 20px; font-weight: bold;">预约历史</span>
            <el-button type="primary" size="mini">打印</el-button>
            <el-button type="primary" size="mini">退约</el-button>
            <el-button type="primary" size="mini">改约</el-button>
          </el-row>
          <el-row>
            <!-- <table style="width: 100%; text-align: left;">
              <tr>
                <th>项目名称</th>
                <th>检查时间</th>
                <th>申请单号</th>
              </tr>
              <tr v-for="item in reserveHistory" :key="item.id">
                <td>{{ item.checkItem }}</td>
                <td>{{ item.checkTime }}</td>
                <td>{{ item.applyNo }}</td>
              </tr>
            </table> -->
            <el-table :data="reserveHistory" size="mini" style="margin-top: 5px; width: 100%;">
              <el-table-column prop="checkItem" label="项目名称" />
              <el-table-column prop="checkTime" label="检查时间" />
              <el-table-column prop="applyNo" label="申请单号" />
            </el-table>
          </el-row>
        </el-row>
      </el-col>
      <el-col :span="17" class="right">
        <div class="first">
          <div class="title">预约时间</div>
          <div class="reserve-content">
            <reserve-date :date-list="dateList" @activeDate="getSelectDate" />
          </div>
        </div>
        <div class="second">
          <div style="font-weight: bold;">预约时段</div>
          <div class="second">
            <reserve-time :time-list="timeList" />
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import ReserveInfo from '@/components/reserveInfo';
import ReserveDate from '@/components/reserveDate';
import ReserveTime from '@/components/reserveTime';

export default {
  name: 'Reservation',
  components: {
    ReserveInfo,
    ReserveDate,
    ReserveTime,
  },
  data() {
    return {
      type: 1,
      reserveInfoList: [
        {
          id: 1,
          applyList: [{
            id: 1, checkItem: 'B超', reserveDate: '2018.6.4', applyNo: '00192734',
          }],
          personInfo: {
            personName: '刘世丽',
            gender: '女',
            age: 40,
            limb: '胸部',
          },
          payStatus: 1,
        },
        {
          id: 2,
          applyList: [{
            id: 2, checkItem: 'B超', reserveDate: '2018.6.4', applyNo: '00192734',
          }],
          personInfo: {
            personName: '刘世丽',
            gender: '女',
            age: 40,
            limb: '胸部',
          },
          payStatus: 0,
        },
      ],
      reserveHistory: [
        {
          id: 1, checkItem: 'B超', checkTime: '2019.6.4', applyNo: '00182898',
        },
        {
          id: 2, checkItem: 'B超', checkTime: '2019.6.4', applyNo: '00182898',
        },
      ],
      isActive: 0,
      dateList: [
        {
          year: 2019, month: 6, day: 1, date: '2019-06-01',
        },
        {
          year: 2019, month: 6, day: 2, date: '2019-06-02',
        },
        {
          year: 2019, month: 6, day: 3, date: '2019-06-03',
        },
        {
          year: 2019, month: 6, day: 4, date: '2019-06-04',
        },
        {
          year: 2019, month: 6, day: 5, date: '2019-06-05',
        },
        {
          year: 2019, month: 6, day: 6, date: '2019-06-06',
        },
        {
          year: 2019, month: 6, day: 7, date: '2019-06-07',
        },
        {
          year: 2019, month: 6, day: 8, date: '2019-06-08',
        },
        {
          year: 2019, month: 6, day: 9, date: '2019-06-09',
        },
        {
          year: 2019, month: 6, day: 10, date: '2019-06-10',
        },
        {
          year: 2019, month: 6, day: 11, date: '2019-06-11',
        },
        {
          year: 2019, month: 6, day: 12, date: '2019-06-12',
        },
        {
          year: 2019, month: 6, day: 13, date: '2019-06-13',
        },
        {
          year: 2019, month: 6, day: 14, date: '2019-06-14',
        },
        {
          year: 2019, month: 7, day: 1, date: '2019-07-01',
        },
        {
          year: 2019, month: 7, day: 2, date: '2019-07-02',
        },
      ],
      timeList: [
        {
          id: '1', date: '5.4', dept: 'B超1', time: '8:00-9:00', num: 3,
        },
        {
          id: '2', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
        {
          id: '3', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
        {
          id: '4', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
        {
          id: '5', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
        {
          id: '6', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
        {
          id: '7', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
        {
          id: '8', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
        {
          id: '9', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
        {
          id: '10', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
        {
          id: '21', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
        {
          id: '22', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
        {
          id: '23', date: '5.4', dept: 'B超2', time: '9:00-10:00', num: 4,
        },
      ],
    };
  },
  methods: {
    getSelectDate(date) {
      console.log(date);
    },
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
  height: calc(100vh - 125px);
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
    flex: 1;
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
    .reserve-content {
      padding: 5px;
    }
  }
  .second {
    padding: 5px;
  }
}
.el-radio {
  margin-right: 5px;
}
</style>
