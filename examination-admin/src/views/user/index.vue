<template>
  <div class="page">
    <el-card>
      <el-row class="tabbar">
        <el-input placeholder="用户名" v-model="query.username" size="small" style="width: 100px;" />
        <el-button
          type="primary"
          size="small"
          class="btn"
          icon="el-icon-search"
          @click="handleSearch"
          :loading="tableLoading"
        >
          查询
        </el-button>
        <el-button
          type="primary"
          size="small"
          class="btn"
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
          style="width: 100%"
          header-row-class-name="table-header"
          v-loading="tableLoading"
          @row-dblclick="handleEdit"
        >
          <el-table-column label="用户名" prop="username" sortable />
          <el-table-column label="姓名" prop="personName" sortable />
          <el-table-column label="状态" prop="status" sortable>
            <template slot-scope="scope">
              {{ statusDic[scope.row.status] }}
            </template>
          </el-table-column>
          <el-table-column label="创建日期" prop="createTime" sortable />
          <el-table-column label="创建人" prop="createUser" sortable />
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
      statusDic: {
        0: '禁用',
        1: '正常',
      },
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
        personName: '',
        roles: [],
      },
      dialogFormVisible: false,
      labelWidth: '120px',
      rules: {
        username: [{ required: true, message: '不能为空', trigger: 'change' }],
        password: [{ required: true, message: '不能为空', trigger: 'change' }],
      },
      op: 'create',
      title: '新建用户',
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
      this.getTableData();
    },
    handleAdd() {
      this.form = {
        userId: '',
        username: '',
        password: '',
        personName: '',
        roles: [],
      };
      this.op = 'create';
      this.title = '新增用户';
      this.dialogFormVisible = true;
    },
    handleSave() {
      const data = this.form;
      data.roles = this.form.roles.map(item => ({ roleId: item }));
      save(data, { op: this.op }).then((res) => {
        if (res.code === 200) {
          this.$message({
            type: 'success',
            message: '保存成功',
          });
          this.getTableData();
        } else {
          this.$message({
            type: 'error',
            message: '保存失败',
          });
        }
        this.dialogFormVisible = false;
      });
    },
    handleCancel() {
      this.dialogFormVisible = false;
    },
    handleEdit(val) {
      this.form.userId = val.userId;
      this.form.username = val.username;
      this.form.personName = val.personName;
      this.form.password = val.password;
      this.form.roles = val.roles.map(item => item.roleId);
      this.op = 'update';
      this.title = '修改用户';
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
  },
  mounted() {
    this.getTableData();
    this.getRoleDic();
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
