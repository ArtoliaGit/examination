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
          <el-table-column label="类型" prop="type" width="60" />
          <el-table-column label="检查项目" prop="checkItem" :formatter="getCheckItemDic" width="100" />
          <el-table-column label="提示信息" prop="info" />
          <el-table-column label="排序" prop="ord" width="60" />
          <el-table-column label="状态" prop="status" :formatter="getStatusDic" width="60" />
          <el-table-column label="操作" align="center" width="180" fixed="right">
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
            <el-form-item label="类型" prop="type" :label-width="labelWidth">
              <el-input type="string" v-model="form.type" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="检查项目" prop="checkItem" :label-width="labelWidth">
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
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="排序" prop="ord" :label-width="labelWidth">
              <el-input v-model.number="form.ord" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="提示信息" prop="info" :label-width="labelWidth">
              <el-input type="textarea" :rows="4" v-model="form.info" maxlength="200" show-word-limit />
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
  deleteNotice,
} from '@/api/notice';
import { getAllList as getCheckItemList } from '@/api/checkItem';

export default {
  name: 'Notice',
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
        id: '',
        type: '',
        checkItem: '',
        info: '',
        ord: '',
        status: '1',
        createUser: '',
        createTime: '',
        createUnit: '',
      },
      rules: {
        type: [{ required: true, message: '不能为空', trigger: 'blur' }],
        checkItem: [{ required: true, message: '不能为空', trigger: 'blur' }],
        info: [{ required: true, message: '不能为空', trigger: 'blur' }],
        ord: [
          { required: true, message: '不能为空', trigger: 'blur' },
          { type: 'number', message: '必须为数字' },
        ],
      },
      statusDic: [
        { key: '1', text: '正常' },
        { key: '0', text: '作废' },
      ],
      checkItemDic: [],
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
      this.form.ord = this.form.ord && parseInt(this.form.ord, 10);
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
        type: '',
        checkItem: '',
        info: '',
        ord: '',
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
      deleteNotice(val).then((res) => {
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
