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
          <el-table-column label="项目名称" prop="name" />
          <el-table-column label="可预约天数" prop="days" />
          <el-table-column label="性别" prop="gender" :formatter="getGenderDic" />
          <el-table-column label="预约方式" prop="way" :formatter="getWayDic" />
          <el-table-column label="归属执行科室" prop="dept" />
          <el-table-column label="执行地点" prop="address" />
          <el-table-column label="体检使用" prop="tjsy">
            <template slot-scope="scope">
              {{ scope.row.tjsy === '1' ? '是' : '否' }}
            </template>
          </el-table-column>
          <el-table-column label="报到机名" prop="reportName" />
          <el-table-column label="医嘱分类" prop="medicalOrder" />
          <el-table-column label="有效标志" prop="status" :formatter="getStatusDic" />
          <el-table-column label="操作" align="center" width="160" fixed="right">
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
            <el-form-item label="项目名称" prop="name" :label-width="labelWidth">
              <el-input type="string" v-model="form.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="可预约天数" prop="days" :label-width="labelWidth">
              <el-input v-model.number="form.days" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="性别" prop="gender" :label-width="labelWidth">
              <el-select v-model="form.gender">
                <el-option
                  v-for="item in genderDic"
                  :key="item.key"
                  :label="item.text"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预约方式" prop="way" :label-width="labelWidth">
              <el-select v-model="form.way">
                <el-option
                  v-for="item in wayDic"
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
            <el-form-item label="归属执行科室" prop="dept" :label-width="labelWidth">
              <el-input type="string" v-model="form.dept" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行地点" prop="address" :label-width="labelWidth">
              <el-input type="string" v-model="form.address" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="体检使用" prop="tjsy" :label-width="labelWidth">
              <el-switch
                v-model="form.tjsy"
                active-value="1"
                inactive-value="0"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报到机名" prop="reportName" :label-width="labelWidth">
              <el-input type="string" v-model="form.reportName" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="医嘱分类" prop="medicalOrder" :label-width="labelWidth">
              <el-input type="string" v-model="form.medicalOrder" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="有效标志" prop="status" :label-width="labelWidth">
              <el-select v-model="form.status">
                <el-option
                  v-for="item in statusDic"
                  :key="item.key"
                  :label="item.text"
                  :value="item.key"
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
} from '@/api/checkItem';

export default {
  name: 'CheckItem',
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
      maxHeight: window.innerHeight - 210,
      labelWidth: '120px',
      title: '新建',
      dialogFormVisible: false,
      form: {
        code: '',
        name: '',
        days: '',
        gender: '',
        way: '',
        dept: '',
        address: '',
        tjsy: '',
        reportName: '',
        medicalOrder: '',
        status: '1',
        createUser: '',
        createTime: '',
        createUnit: '',
      },
      rules: {
        name: [{ required: true, message: '不能为空', trigger: 'blur' }],
        gender: [{ required: true, message: '不能为空', trigger: 'blur' }],
        way: [{ required: true, message: '不能为空', trigger: 'blur' }],
        days: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { type: 'number', message: '必须为数字' },
        ],
        dept: [{ required: true, message: '不能为空', trigger: 'blur' }],
        address: [{ required: true, message: '不能为空', trigger: 'blur' }],
        status: [{ required: true, message: '不能为空', trigger: 'blur' }],
        medicalOrder: [{ required: true, message: '不能为空', trigger: 'blur' }],
      },
      statusDic: [
        { key: '1', text: '正常' },
        { key: '0', text: '作废' },
      ],
      genderDic: [
        { key: '3', text: '全部' },
        { key: '1', text: '男' },
        { key: '2', text: '女' },
      ],
      wayDic: [
        { key: '1', text: '人次' },
        { key: '2', text: '部位' },
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
      this.resetForm();
      this.form = Object.assign(this.form, val);
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
        code: '',
        name: '',
        days: '',
        gender: '',
        way: '',
        dept: '',
        address: '',
        tjsy: '',
        reportName: '',
        medicalOrder: '',
        status: '1',
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
      remove({ id: val.code }).then((res) => {
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
    getStatusDic(row, column, cellValue, index) {
      const result = this.statusDic.filter(item => cellValue === item.key);
      if (result.length > 0) {
        return result[0].text;
      }
      return '';
    },
    getGenderDic(row, column, cellValue, index) {
      const result = this.genderDic.filter(item => cellValue === item.key);
      if (result.length > 0) {
        return result[0].text;
      }
      return '';
    },
    getWayDic(row, column, cellValue, index) {
      const result = this.wayDic.filter(item => cellValue === item.key);
      if (result.length > 0) {
        return result[0].text;
      }
      return '';
    },
  },
  mounted() {
    this.getTableData();
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
