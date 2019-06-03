<template>
  <div class="page">
    <el-card>
      <el-row class="filter-container">
        <el-button
          type="primary"
          size="small"
          class="filter-item"
          icon="el-icon-plus"
          @click="handleAdd"
        >
          新增
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
          @row-dblclick="handleEdit"
        >
          <el-table-column label="预约项目" prop="checkItem" :formatter="getCheckItemDic" />
          <el-table-column label="星期" prop="week" :formatter="getWeekDic" />
          <el-table-column label="预约时段" prop="timeSlot" :formatter="getReserveTimeDic" />
          <el-table-column label="总限额" prop="totalLimit" />
          <el-table-column label="操作" align="center" min-width="120" fixed="right">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button
                type="danger"
                size="mini"
                @click="handleDelete(scope.row)"
              >
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="page.page"
          :page-size="page.pageSize"
          :page-sizes="page.pageSizes"
          :total="page.total"
          background
          layout="total, sizes, prev, pager, next, jumper, slot"
        >
          <el-button size="mini" icon="el-icon-refresh" @click="refresh" plain>刷新</el-button>
        </el-pagination>
      </el-row>
    </el-card>

    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
    >
      <el-form :model="form" ref="form" size="small" :rules="rules">
        <el-row>
          <el-col :span="12">
            <el-form-item label="预约项目" prop="checkItem" :label-width="labelWidth">
              <el-select v-model="form.checkItem">
                <el-option
                  v-for="item in checkItemDic"
                  :key="item.key"
                  :label="item.text"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="星期" prop="week" :label-width="labelWidth">
              <el-select v-model="form.week">
                <el-option
                  v-for="item in weekDic"
                  :key="item.key"
                  :label="item.text"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预约时段" prop="timeSlot" :label-width="labelWidth">
              <el-select v-model="form.timeSlot">
                <el-option
                  v-for="item in reserveTimeDic"
                  :key="item.key"
                  :label="item.text"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总限额" prop="totalLimit" :label-width="labelWidth">
              <el-input v-model.number="form.totalLimit" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCancel">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getList,
  save,
  remove,
} from '@/api/reserveTemplate';
import { getAllList as getCheckItemList } from '@/api/checkItem';
import { getAllList as getReserveTimeList } from '@/api/reserveTime';

export default {
  name: 'ReserveTemplate',
  data() {
    return {
      tableData: [],
      page: {
        page: 1,
        pageSize: 10,
        total: 0,
        pageSizes: [10, 20, 30, 40, 50],
      },
      tableLoading: false,
      maxHeight: window.innerHeight - 260,
      labelWidth: '120px',
      title: '新建',
      dialogFormVisible: false,
      form: {
        id: '',
        checkItem: '',
        week: '',
        timeSlot: '',
        totalLimit: '',
        createUser: '',
        createTime: '',
        createUnit: '',
      },
      rules: {
        checkItem: [{ required: true, message: '不能为空', trigger: 'blur' }],
        week: [{ required: true, message: '不能为空', trigger: 'blur' }],
        timeSlot: [{ required: true, message: '不能为空', trigger: 'blur' }],
        totalLimit: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { type: 'number', message: '必须为数字' },
        ],
      },
      checkItemDic: [],
      reserveTimeDic: [],
      weekDic: [
        { key: '1', text: '周一' },
        { key: '2', text: '周二' },
        { key: '3', text: '周三' },
        { key: '4', text: '周四' },
        { key: '5', text: '周五' },
        { key: '6', text: '周六' },
        { key: '7', text: '周日' },
      ],
    };
  },
  methods: {
    handleSizeChange(val) {
      this.page.pageSize = val;
      this.getTableData();
    },
    handleCurrentChange(val) {
      this.page.page = val;
      this.getTableData();
    },
    refresh() {
      this.getTableData();
    },
    getTableData() {
      const params = {
        page: this.page.page,
        pageSize: this.page.pageSize,
      };
      this.tableLoading = true;
      getList(params).then((res) => {
        this.tableLoading = false;
        if (res.code === 200) {
          this.tableData = res.data;
          this.page.total = res.total;
        } else {
          this.tableData = [];
        }
      });
    },
    handleSearch() {
      this.page.page = 1;
      this.getTableData();
    },
    handleEdit(val) {
      this.form = Object.assign({}, val);
      this.form.days = this.form.days && parseInt(this.form.days, 10);
      this.op = 'update';
      this.title = '修改';
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs.form.clearValidate();
      });
    },
    resetForm() {
      this.form = {
        id: '',
        checkItem: '',
        week: '',
        timeSlot: '',
        totalLimit: '',
        createUser: '',
        createTime: '',
        createUnit: '',
      };
    },
    handleAdd() {
      this.resetForm();
      this.op = 'create';
      this.title = '新建';
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs.form.clearValidate();
      });
    },
    handleCancel() {
      this.dialogFormVisible = false;
    },
    handleSave() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          save(this.form).then((res) => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: '保存成功',
              });
              this.handleCancel();
              this.getTableData();
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
        }
      });
    },
    handleDelete(val) {
      remove(val).then((res) => {
        if (res.code === 200) {
          this.$message({
            type: 'success',
            message: '删除成功',
          });
          this.refresh();
        } else {
          this.$message({
            type: 'error',
            message: '删除失败',
          });
        }
      }).catch(() => {
        this.$message({
          type: 'error',
          message: '删除失败',
        });
      });
    },
    setCheckItemDic() {
      getCheckItemList().then((res) => {
        if (res.code === 200) {
          const { data } = res;
          if (data.length > 0) {
            this.checkItemDic = data.map(item => ({ key: item.code, text: item.name }));
          }
        }
      });
    },
    setReserveTimeDic() {
      getReserveTimeList().then((res) => {
        if (res.code === 200) {
          const { data } = res;
          if (data.length > 0) {
            this.reserveTimeDic = data.map(item => ({ key: item.id, text: item.timeSlot }));
          }
        }
      });
    },
    getWeekDic(row, column, cellValue, index) {
      const result = this.weekDic.filter(item => cellValue === item.key);
      if (result.length > 0) {
        return result[0].text;
      }
      return '';
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
    this.getTableData();
    window.onresize = () => {
      this.maxHeight = window.innerHeight - 260;
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
