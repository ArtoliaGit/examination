<template>
  <div class="page">
    <el-card>
      <el-row class="filter-container">
        <el-select
          placeholder="检查项目"
          v-model="query.checkItem"
          size="small"
          style="width: 150px;"
          class="filter-item"
          @change="handleChange"
        >
          <el-option
            v-for="item in checkItemDic"
            :key="item.key"
            :label="item.text"
            :value="item.key"
          />
        </el-select>
        <el-button
          type="primary"
          size="small"
          class="filter-item"
          icon="el-icon-plus"
          style="margin-left: 10px;"
          @click="handleSave"
        >
          保存
        </el-button>
      </el-row>
      <el-row>
        <el-table
          :data="tableData"
          border
          highlight-current-row
          :max-height="maxHeight"
          :height="maxHeight"
          style="width: 100%"
          header-row-class-name="table-header"
          v-loading="tableLoading"
          size="small"
        >
          <el-table-column
            label="预约时段"
            prop="timeSlot"
            :formatter="getReserveTimeDic"
            min-width="100"
            fixed
          />
          <el-table-column
            :label="item.week"
            :prop="item.field"
            v-for="item in headers"
            :key="item.field"
            width="100"
          >
            <template slot-scope="scope">
              <el-input v-model="scope.row[item.field]" size="mini" />
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import {
  getTableList,
  batchSave,
} from '@/api/reserveResource';
import { getAllList as getCheckItemList } from '@/api/checkItem';
import { getAllList as getReserveTimeList } from '@/api/reserveTime';

export default {
  name: 'ReserveTemplate',
  data() {
    return {
      tableData: [],
      query: {
        checkItem: '',
      },
      tableLoading: false,
      maxHeight: window.innerHeight - 210,
      checkItemDic: [],
      reserveTimeDic: [],
      headers: [],
      weekDic: ['', 'monday', 'tuesday', 'wednesday', 'thursday', 'friday', 'saturday', 'sunday'],
    };
  },
  methods: {
    getTableData() {
      const params = {
        checkItem: this.query.checkItem,
      };
      this.tableLoading = true;
      getTableList(params).then((res) => {
        this.tableLoading = false;
        if (res.code === 200) {
          this.tableData = res.data.data;
          this.headers = res.data.headers;
        } else {
          this.tableData = [];
          this.headers = [];
        }
      });
    },
    handleChange() {
      this.getTableData();
    },
    handleSave() {
      const list = this.tableData.map(
        item => Object.entries(item).filter(x => x[0] !== 'timeSlot').map(
          y => ({
            timeSlot: item.timeSlot,
            reserveDate: y[0].substr(5, y[0].length),
            totalLimit: parseInt(y[1], 10),
            checkItem: this.query.checkItem,
          }),
        ),
      ).filter(item => item.length > 0).flatMap(item => item);
      console.log(list);
      batchSave(list).then((res) => {
        if (res.code === 200) {
          this.$message({
            type: 'success',
            message: '保存成功',
          });
        } else {
          this.$message({
            type: 'error',
            message: '保存失败',
          });
        }
      }).catch(() => {
        this.$message({
          type: 'error',
          message: '保存失败',
        });
      });
    },
    setCheckItemDic() {
      getCheckItemList().then((res) => {
        if (res.code === 200) {
          const { data } = res;
          if (data.length > 0) {
            this.checkItemDic = data.map(item => ({ key: item.code, text: item.name }));
            this.query.checkItem = this.checkItemDic[0].key;
            this.getTableData();
          }
        }
      });
    },
    setReserveTimeDic() {
      getReserveTimeList().then((res) => {
        if (res.code === 200) {
          const { data } = res;
          if (data.length > 0) {
            this.reserveTimeDic = data.map(item => ({ key: item.id, text: `${item.startTime}-${item.endTime}` }));
          }
        }
      });
    },
    getCheckItemDic(row, column, cellValue, index) {
      const result = this.checkItemDic.filter(item => cellValue === item.key);
      if (result.length > 0) {
        return result[0].text;
      }
      return '';
    },
    getReserveTimeDic(row, column, cellValue, index) {
      const result = this.reserveTimeDic.filter(item => cellValue === item.key);
      if (result.length > 0) {
        return result[0].text;
      }
      return '';
    },
  },
  mounted() {
    this.setCheckItemDic();
    this.setReserveTimeDic();
    window.onresize = () => {
      this.maxHeight = window.innerHeight - 210;
    };
  },
};
</script>

<style lang="scss" scoped>
.el-pagination {
  margin-top: 10px;
  float: right;
}
</style>
