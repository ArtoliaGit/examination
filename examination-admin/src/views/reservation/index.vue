<template>
  <div>
    <el-row>
      <el-col :span="8" class="left">
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
          <el-row>
            <div class="card">
              <div style="display: flex; border-bottom: 1px solid;">
                <div style="flex: 2;">项目名称</div>
                <div style="flex: 1;">预约时间</div>
                <div style="flex: 1;">申请单号</div>
              </div>
              <div style="display: flex; margin-top: 10px;">
                <div style="flex: 2;">B超</div>
                <div style="flex: 1;">2018.5.6</div>
                <div style="flex: 1;">00192734</div>
              </div>
              <div
                style="margin-top: 20px; display: grid; grid-template-columns: repeat(4, 1fr); grid-gap: 10px;"
              >
                <div style="display: flex; grid-column: 1/3; align-items: center;">
                  <div style="flex: 1">姓名</div>
                  <div style="flex: 1">刘世丽</div>
                </div>
                <div style="display: flex; align-items: center;">
                  <div style="flex: 1">姓别</div>
                  <div style="flex: 1">女</div>
                </div>
                <div style="display: flex; align-items: center;">
                  <div style="flex: 1">年龄</div>
                  <div style="flex: 1">29</div>
                </div>
                <div style="display: flex; grid-column: 1/3; align-items: center;">
                  <div style="flex: 1">检查部位</div>
                  <div style="flex: 1">腹部</div>
                </div>
                <div style="grid-row: 2; grid-column: 4; color: green;">已付费</div>
              </div>
            </div>
            <div class="card" style="background-color: #efd2ae54;">
              <div style="display: flex; border-bottom: 1px solid;">
                <div style="flex: 2;">项目名称</div>
                <div style="flex: 1;">预约时间</div>
                <div style="flex: 1;">申请单号</div>
              </div>
              <div style="display: flex; margin-top: 10px;">
                <div style="flex: 2;">B超</div>
                <div style="flex: 1;">2018.5.6</div>
                <div style="flex: 1;">00192734</div>
              </div>
              <div
                style="margin-top: 20px; display: grid; grid-template-columns: repeat(4, 1fr); grid-gap: 10px;"
              >
                <div style="display: flex; grid-column: 1/3; align-items: center;">
                  <div style="flex: 1">姓名</div>
                  <div style="flex: 1">刘世丽</div>
                </div>
                <div style="display: flex; align-items: center;">
                  <div style="flex: 1">姓别</div>
                  <div style="flex: 1">女</div>
                </div>
                <div style="display: flex; align-items: center;">
                  <div style="flex: 1">年龄</div>
                  <div style="flex: 1">29</div>
                </div>
                <div style="display: flex; grid-column: 1/3; align-items: center;">
                  <div style="flex: 1">检查部位</div>
                  <div style="flex: 1">腹部</div>
                </div>
                <div style="grid-row: 2; grid-column: 4; color: red;">未付费</div>
              </div>
            </div>
            <div class="card" style="background-color: #efd2ae54;">
              <div style="display: flex; border-bottom: 1px solid;">
                <div style="flex: 2;">项目名称</div>
                <div style="flex: 1;">预约时间</div>
                <div style="flex: 1;">申请单号</div>
              </div>
              <div style="display: flex; margin-top: 10px;">
                <div style="flex: 2;">B超</div>
                <div style="flex: 1;">2018.5.6</div>
                <div style="flex: 1;">00192734</div>
              </div>
              <div
                style="margin-top: 20px; display: grid; grid-template-columns: repeat(4, 1fr); grid-gap: 10px;"
              >
                <div style="display: flex; grid-column: 1/3; align-items: center;">
                  <div style="flex: 1">姓名</div>
                  <div style="flex: 1">刘世丽</div>
                </div>
                <div style="display: flex; align-items: center;">
                  <div style="flex: 1">姓别</div>
                  <div style="flex: 1">女</div>
                </div>
                <div style="display: flex; align-items: center;">
                  <div style="flex: 1">年龄</div>
                  <div style="flex: 1">29</div>
                </div>
                <div style="display: flex; grid-column: 1/3; align-items: center;">
                  <div style="flex: 1">检查部位</div>
                  <div style="flex: 1">腹部</div>
                </div>
                <div style="grid-row: 2; grid-column: 4; color: red;">未付费</div>
              </div>
            </div>
          </el-row>
        </el-row>
        <el-row class="third">
          <el-row>
            <span style="margin-right: 20px;">预约历史</span>
            <el-button type="primary" size="mini">打印</el-button>
            <el-button type="primary" size="mini">退约</el-button>
            <el-button type="primary" size="mini">改约</el-button>
          </el-row>
          <el-row>
            <table style="width: 100%;">
              <tr>
                <th>项目名称</th>
                <th>检查时间</th>
                <th>申请单号</th>
              </tr>
              <tr>
                <td>B超</td>
                <td>2018.4.6</td>
                <td>00182898</td>
              </tr>
              <tr>
                <td>B超</td>
                <td>2018.4.6</td>
                <td>00182898</td>
              </tr>
            </table>
          </el-row>
        </el-row>
      </el-col>
      <el-col :span="16" class="right">
        <div>
          <div style="font-weight: bold;">预约时间</div>
          <div style="padding: 5px; margin-right: 50px;">
            <div style="display: grid; grid-template-columns: repeat(14, 1fr); grid-gap: 10px;">
              <div
                v-for="item in 30"
                :key="item"
                style="width: 40px; height: 40px; background-color: green; border-radius: 5px;"
              />
            </div>
          </div>
        </div>
        <div>
          <div style="font-weight: bold;">预约时段</div>
          <div style="display: grid; grid-template-columns: repeat(7, 1fr); grid-gap: 10px;">
            <div
              v-for="item in 9"
              :key="item"
              style="width: 80px; height: 80px; background-color: #2cdaca; border-radius: 5px; padding: 5px;"
            >
              <div>5.7</div>
              <div>8:00-9:00</div>
              <div>B超1</div>
              <div style="float: right;">余3</div>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'Reservation',
  data() {
    return {
      type: 1,
    };
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
.left {
  border-right: 2px solid #eee;
  .first {
    border-bottom: 1px solid #eee;
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
      margin-bottom: 20px;
    }
  }
  .second {
    padding-top: 10px;
    border-bottom: 1px solid #eee;
    .title {
      font-weight: bold;
    }
    .card {
      background-color: #00ffff54;
      border-radius: 10px;
      padding: 10px;
      margin: 10px;
    }
  }
  .third {
    padding-top: 5px;
  }
}
</style>
