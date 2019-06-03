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
          <el-table-column label="机构编码" prop="organcode" />
          <el-table-column label="机构名称" prop="organname" />
          <el-table-column label="机构类型" prop="type" width="80" :formatter="getOrganType" />
          <el-table-column label="数据库类型" prop="driverclass" :formatter="getDriverDic" />
          <el-table-column label="数据库名" prop="name" width="80" />
          <el-table-column label="数据库ip" prop="ip" width="120" />
          <el-table-column label="数据库端口" prop="port" />
          <el-table-column label="数据库用户名" prop="username" width="80" />
          <el-table-column label="数据库密码" prop="password" />
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
            <el-form-item label="机构编码" prop="organcode" :label-width="labelWidth">
              <el-input type="string" v-model="form.organcode" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="机构名称" prop="organname" :label-width="labelWidth">
              <el-input type="string" v-model="form.organname" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="机构类型" prop="type" :label-width="labelWidth">
              <el-select v-model="form.type">
                <el-option
                  v-for="item in organType"
                  :key="item.key"
                  :label="item.text"
                  :value="item.key"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库类型" prop="driverclass" :label-width="labelWidth">
              <el-select v-model="form.driverclass">
                <el-option
                  v-for="item in driverDic"
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
            <el-form-item label="数据库名" prop="name" :label-width="labelWidth">
              <el-input type="string" v-model="form.name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库ip" prop="ip" :label-width="labelWidth">
              <el-input type="string" v-model="form.ip" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库端口" prop="port" :label-width="labelWidth">
              <el-input type="string" v-model.number="form.port" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据库用户名" prop="username" :label-width="labelWidth">
              <el-input type="string" v-model="form.username" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="数据库密码" prop="password" :label-width="labelWidth">
              <el-input type="string" v-model="form.password" />
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
  getOrganList,
  save,
  deleteOrgan,
} from '@/api/organ';

export default {
  name: 'Organ',
  data() {
    const validateIp = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('不能为空'));
      }
      const regex = new RegExp(/^((25[0-5]|2[0-4]?[0-9]?|1[0-9]?[0-9]?|0)\.){3}(25[0-5]|2[0-4]?[0-9]?|1[0-9]?[0-9]?|0)$/);
      if (regex.test(value)) {
        callback();
      } else {
        callback('ip格式不正确');
      }
    };
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
        organcode: '',
        organname: '',
        type: '',
        driverclass: '',
        name: '',
        ip: '',
        port: '',
        username: '',
        password: '',
        createUser: '',
        createTime: '',
        createUnit: '',
      },
      rules: {
        organcode: [{ required: true, message: '不能为空', trigger: 'blur' }],
        organname: [{ required: true, message: '不能为空', trigger: 'blur' }],
        type: [{ required: true, message: '不能为空', trigger: 'blur' }],
        driverclass: [{ required: true, message: '不能为空', trigger: 'blur' }],
        name: [{ required: true, message: '不能为空', trigger: 'blur' }],
        ip: [{ required: true, validator: validateIp, trigger: 'blur' }],
        port: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { type: 'number', message: '端口必须为数字' },
        ],
        username: [{ required: true, message: '不能为空', trigger: 'blur' }],
        password: [{ required: true, message: '不能为空', trigger: 'blur' }],
      },
      driverDic: [
        { key: '1', text: 'Mysql' },
        { key: '2', text: 'Sql Server' },
        { key: '3', text: 'Oracle' },
      ],
      organType: [
        { key: '1', text: '中心' },
        { key: '2', text: '下属站' },
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
      getOrganList(params).then((res) => {
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
      this.form.port = parseInt(this.form.port, 10);
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
        organcode: '',
        organname: '',
        type: '',
        driverclass: '',
        name: '',
        ip: '',
        port: '',
        username: '',
        password: '',
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
          save(this.form, { op: this.op }).then((res) => {
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
      deleteOrgan(val).then((res) => {
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
    getOrganType(row, column, cellValue, index) {
      const result = this.organType.filter(item => cellValue === item.key);
      if (result.length > 0) {
        return result[0].text;
      }
      return '';
    },
    getDriverDic(row, column, cellValue, index) {
      const result = this.driverDic.filter(item => cellValue === item.key);
      if (result.length > 0) {
        return result[0].text;
      }
      return '';
    },
  },
  mounted() {
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
