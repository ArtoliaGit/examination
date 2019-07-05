<template>
  <div class="page">
    <el-card>
      <el-row class="filter-container">
        <el-select
          placeholder="检查项目"
          v-model="query.checkItem"
          size="small"
          style="width: 150px; margin-right: 10px;"
          class="filter-item"
          @change="getTableData"
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
          @click="getTableData"
        >
          刷新
        </el-button>
        <el-button
          type="primary"
          size="small"
          class="filter-item"
          @click="handleOrderCall"
        >
          顺序呼叫
        </el-button>
        <el-button
          type="primary"
          size="small"
          class="filter-item"
          @click="handleSelectCall"
        >
          选择呼叫
        </el-button>
        <el-button
          type="primary"
          size="small"
          class="filter-item"
          @click="handleAbandonCall"
        >
          弃呼
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
          @row-click="handleRowClick"
        >
          <el-table-column label="时段" prop="timeSlot" />
          <el-table-column label="序列" prop="bdxl" />
          <el-table-column label="等待患者" prop="personName" />
          <el-table-column label="性别" prop="gender" />
          <el-table-column label="年龄" prop="age" />
        </el-table>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import {
  getList,
  updateStatus,
} from '@/api/checkIn';
import { getAllList as getCheckItemList } from '@/api/checkItem';

export default {
  name: 'CheckIn',
  data() {
    return {
      tableData: [],
      query: {
        checkItem: '',
      },
      checkItemDic: [],
      row: null,
      called: null,
      tableLoading: false,
      maxHeight: window.innerHeight - 170,
    };
  },
  methods: {
    getTableData() {
      const params = {
        checkItem: this.query.checkItem,
      };
      getList(params).then((res) => {
        if (res.code === 200) {
          this.tableData = res.data;
          return Promise.resolve(res);
        }
        this.tableData = [];
        return Promise.reject();
      });
    },
    handleOrderCall() {
      if (this.tableData.length === 0) {
        return;
      }
      const data = {
        id: this.tableData[0].id,
        jhzt: '1',
      };
      this.finishCall()
        .then((res) => {
          if (res.code === 200) {
            this.called = null;
            this.row = null;
            return this.getTableData();
          }
          return Promise.reject();
        })
        .then(() => updateStatus(data))
        .then((res) => {
          if (res.code === 200) {
            [this.called] = this.tableData;
            this.$message({
              type: 'success',
              message: '叫号成功',
            });
          }
        })
        .catch(() => {
          this.$message({
            type: 'error',
            message: '叫号失败',
          });
        });
    },
    handleSelectCall() {
      if (!this.row) {
        this.$message({
          type: 'warning',
          message: '未选中被呼叫的人',
        });
        return;
      }
      const data = {
        id: this.row.id,
        hjzt: '1',
      };
      this.finishCall()
        .then((res) => {
          if (res.code === 200) {
            this.called = null;
            this.row = null;
            return this.getTableData();
          }
          return Promise.reject();
        })
        .then(() => updateStatus(data))
        .then((res) => {
          if (res.code === 200) {
            this.called = this.row;
            this.$message({
              type: 'success',
              message: '叫号成功',
            });
          }
        })
        .catch(() => {
          this.$message({
            type: 'error',
            message: '叫号失败',
          });
        });
    },
    handleAbandonCall() {
      if (!this.called) {
        return;
      }
      const data = {
        id: this.called.id,
        hjzt: '3',
      };
      updateStatus(data).then((res) => {
        if (res.code === 200) {
          this.$message({
            type: 'success',
            message: '已弃呼',
          });
        }
      });
    },
    handleRowClick(row) {
      this.row = row;
    },
    finishCall() {
      if (!this.called) {
        const result = { code: 200 };
        return Promise.resolve(result);
      }
      const data = {
        id: this.called.id,
        hjzt: '2',
      };
      return updateStatus(data);
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
    getCheckItemDic(row, column, cellValue, index) {
      const result = this.checkItemDic.filter(item => cellValue === item.key);
      if (result.length > 0) {
        return result[0].text;
      }
      return '';
    },
  },
  mounted() {
    this.setCheckItemDic();
    window.onresize = () => {
      this.maxHeight = window.innerHeight - 170;
    };
  },
};
</script>
