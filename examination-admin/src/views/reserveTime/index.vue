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
          <el-table-column label="开始时间" prop="startTime" />
          <el-table-column label="结束时间" prop="endTime" />
          <el-table-column label="预约时段序列" prop="timeSequence" />
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
            <el-form-item label="开始时间" prop="startTime" :label-width="labelWidth">
              <el-time-picker
                v-model="form.startTime"
                style="width: 100%;"
                format="HH:mm"
                value-format="HH:mm"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime" :label-width="labelWidth">
              <el-time-picker
                v-model="form.endTime"
                style="width: 100%;"
                format="HH:mm"
                value-format="HH:mm"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="预约时段序列" prop="timeSequence" :label-width="labelWidth">
              <el-select v-model="form.timeSequence">
                <el-option
                  v-for="item in timeSequenceDic"
                  :key="item"
                  :label="item"
                  :value="item"
                />
              </el-select>
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
} from '@/api/reserveTime';

export default {
  name: 'CheckItem',
  data() {
    const startTimeRule = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'));
      } else if (this.form.startTime
        && this.form.endTime
        && this.form.startTime >= this.form.endTime) {
        callback(new Error('开始时间不能大于结束时间'));
      } else if (this.tableData.some(item => value >= item.startTime && value <= item.endTime)) {
        callback(new Error('预约时段冲突'));
      }
      callback();
    };
    const endTimeRule = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'));
      } else if (this.form.startTime
        && this.form.endTime
        && this.form.startTime >= this.form.endTime) {
        callback(new Error('结束时间不能小于开始时间'));
      } else if (this.tableData.some(item => value >= item.startTime && value <= item.endTime)) {
        callback(new Error('预约时段冲突'));
      }
      callback();
    };
    const timeSequenceRule = (rule, value, callback) => {
      if (!value) {
        callback(new Error('不能为空'));
      } else if (this.tableData.some(item => item.timeSequence === value)) {
        callback(new Error('预约时段序列重复'));
      }
      callback();
    };
    return {
      tableData: [],
      page: {
        page: 1,
        pageSize: 10000,
        total: 0,
        pageSizes: [10, 20, 30, 40, 50],
      },
      tableLoading: false,
      maxHeight: window.innerHeight - 170,
      labelWidth: '120px',
      title: '新建',
      dialogFormVisible: false,
      form: {
        id: '',
        startTime: '',
        endTime: '',
        timeSequence: '',
        createUser: '',
        createTime: '',
        createUnit: '',
      },
      rules: {
        startTime: [{ validator: startTimeRule, trigger: 'blur' }],
        endTime: [{ validator: endTimeRule, trigger: 'blur' }],
        timeSequence: [{ validator: timeSequenceRule, trigger: 'change' }],
      },
      timeSequenceDic: [],
    };
  },
  methods: {
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
      this.resetForm();
      this.form = Object.assign(this.form, val);
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
        startTime: '',
        endTime: '',
        timeSequence: '',
        createUser: this.$store.state.user.userName,
        createTime: '',
        createUnit: this.$store.state.user.organ,
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
          this.form.createUser = this.$store.state.user.userName;
          this.form.createUnit = this.$store.state.user.organ;
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
    getTimeSequence() {
      for (let i = 0; i < 26; i++) {
        this.timeSequenceDic.push(String.fromCharCode(65 + i));
      }
      this.timeSequenceDic.splice(21, 1);
    },
  },
  mounted() {
    this.getTimeSequence();
    this.getTableData();
    window.onresize = () => {
      this.maxHeight = window.innerHeight - 170;
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
