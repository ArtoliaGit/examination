<template>
  <div class="page">
    <el-card>
      <el-row class="filter-container">
        <el-input
          placeholder="用户名"
          v-model="query.username"
          size="small"
          style="width: 100px;"
          class="filter-item"
        />
        <el-button
          type="primary"
          size="small"
          class="filter-item"
          icon="el-icon-search"
          style="margin-left: 10px;"
          @click="handleSearch"
        >
          查询
        </el-button>
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
          fit
          :max-height="maxHeight"
          :height="maxHeight"
          style="width: 100%"
          header-row-class-name="table-header"
          v-loading="tableLoading"
          size="small"
          @row-dblclick="handleEdit"
        >
          <el-table-column label="用户名" prop="username" sortable />
          <el-table-column label="所属机构" prop="organcode" sortable />
          <el-table-column label="机构类型" prop="organtype" sortable />
          <el-table-column label="状态" prop="status" :formatter="getStatusDic" sortable />
          <el-table-column label="维护人" prop="createUser" sortable />
          <el-table-column label="维护时间" prop="createTime" sortable />
          <el-table-column label="维护机构" prop="createUnit" sortable />
          <el-table-column label="操作" align="center" width="200">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button
                type="danger"
                size="mini"
                @click="handleDelete(scope.row.userId)"
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
      <el-form :model="form" ref="form" :rules="rules">
        <el-form-item label="姓名" prop="personName" :label-width="labelWidth">
          <el-input type="string" v-model="form.personName" style="width: 50%;" />
        </el-form-item>
        <el-form-item label="用户名" prop="username" :label-width="labelWidth">
          <el-input type="string" v-model="form.username" style="width: 50%;" />
        </el-form-item>
        <el-form-item label="密码" prop="password" :label-width="labelWidth" v-if="op === 'create'">
          <el-input v-model="form.password" type="password" style="width: 50%;" />
        </el-form-item>
        <el-form-item label="所属机构" prop="organcode" :label-width="labelWidth">
          <el-input v-model="form.organcode" type="organcode" style="width: 50%;" />
        </el-form-item>
        <el-form-item label="机构类型" prop="organtype" :label-width="labelWidth">
          <el-input v-model="form.organtype" type="organtype" style="width: 50%;" />
        </el-form-item>
        <el-form-item label="角色" prop="roles" :label-width="labelWidth">
          <el-select v-model="form.roles" multiple style="width: 50%;">
            <el-option
              v-for="item in roleDic"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId"
            />
          </el-select>
        </el-form-item>
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
  getUserList,
  save,
  deleteUser,
} from '@/api/user';
import {
  getRoleList,
} from '@/api/role';

export default {
  name: 'User',
  data() {
    return {
      tableData: [],
      statusDic: [
        { key: '0', text: '禁用' },
        { key: '1', text: '正常' },
      ],
      page: {
        page: 1,
        pageSize: 10,
        total: 0,
        pageSizes: [10, 20, 30, 40, 50],
      },
      tableLoading: false,
      form: {
        userId: '',
        username: '',
        password: '',
        organcode: '',
        organtype: '',
        roles: [],
      },
      dialogFormVisible: false,
      labelWidth: '120px',
      rules: {
        username: [{ required: true, message: '不能为空', trigger: 'change' }],
        password: [{ required: true, message: '不能为空', trigger: 'change' }],
      },
      op: 'create',
      title: '新建',
      query: {
        username: '',
      },
      maxHeight: window.innerHeight - 260,
      roleDic: [],
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
    getTableData() {
      const params = {
        page: this.page.page,
        pageSize: this.page.pageSize,
        username: this.query.username,
      };
      this.tableLoading = true;
      getUserList(params).then((res) => {
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
    handleAdd() {
      this.op = 'create';
      this.title = '新增';
      this.dialogFormVisible = true;
      this.form = {
        userId: '',
        username: '',
        password: '',
        organcode: '',
        organtype: '',
        roles: [],
      };
    },
    handleSave() {
      const data = this.form;
      data.status = '1';
      data.roles = this.form.roles.map(item => ({ roleId: item }));
      this.$refs.form.validate((valid) => {
        if (valid) {
          save(data, { op: this.op }).then((res) => {
            if (res.code === 200) {
              this.$message({
                type: 'success',
                message: '保存成功',
              });
              this.getTableData();
              this.handleCancel();
            } else {
              this.$message({
                type: 'error',
                message: '保存失败',
              });
            }
          });
        }
      });
    },
    handleCancel() {
      this.dialogFormVisible = false;
    },
    handleEdit(val) {
      this.form.userId = val.userId;
      this.form.username = val.username;
      this.form.password = val.password;
      this.form.organcode = val.organcode;
      this.form.organtype = val.organtype;
      this.form.roles = val.roles.map(item => item.roleId);
      this.op = 'update';
      this.title = '修改';
      this.dialogFormVisible = true;
    },
    handleDelete(id) {
      deleteUser({ userId: id }).then((res) => {
        if (res.code === 200) {
          this.$message({
            type: 'success',
            message: '删除成功',
          });
        }
        this.getTableData();
      });
    },
    refresh() {
      this.getTableData();
    },
    getRoleDic() {
      const params = {
        page: 1,
        pageSize: 1000,
      };
      getRoleList(params).then((res) => {
        if (res.code === 200) {
          this.roleDic = res.data;
        }
      });
    },
    getStatusDic(row, column, cellValue, index) {
      const result = this.statusDic.filter(item => cellValue === item.key);
      if (result.length > 0) {
        return result[0].text;
      }
      return '';
    },
  },
  mounted() {
    this.getTableData();
    this.getRoleDic();
    window.onresize = () => {
      this.maxHeight = window.innerHeight - 260;
    };
  },
};
</script>

<style lang="scss" scoped>
.tabbar {
  margin-top: 5px;
  margin-bottom: 10px;
}
.btn {
  margin-left: 10px;
}
.el-pagination {
  margin-top: 10px;
  float: right;
}
</style>
