<template>
  <div class="page">
    <el-card>
      <el-row class="filter-container">
        <el-button
          type="primary"
          size="small"
          class="filter-item"
          icon="el-icon-search"
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
          :max-height="maxHeight"
          :height="maxHeight"
          style="width: 100%"
          header-row-class-name="table-header"
          v-loading="tableLoading"
          size="small"
          @row-dblclick="handleEdit"
        >
          <el-table-column label="角色名" prop="roleName" />
          <el-table-column label="角色描述" prop="roleDescription" />
          <el-table-column label="维护人" prop="createUser" />
          <el-table-column label="维护时间" prop="createTime" />
          <el-table-column label="维护机构" prop="createUnit" />
          <el-table-column label="操作" align="center" min-width="120">
            <template slot-scope="scope">
              <el-button type="primary" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
              <el-button
                type="primary"
                size="mini"
                @click="handleEditResource(scope.row)"
              >
                菜单权限
              </el-button>
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
        <el-form-item label="角色名" prop="roleName" :label-width="labelWidth">
          <el-input type="string" v-model="form.roleName" style="width: 50%;" />
        </el-form-item>
        <el-form-item label="角色描述" prop="roleDescription" :label-width="labelWidth">
          <el-input type="string" v-model="form.roleDescription" style="width: 50%;" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCancel">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog
      title="编辑菜单权限"
      :visible.sync="resourceFormVisible"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
      :show-close="false"
      width="30%"
      center
    >
      <el-tree
        :data="treeData"
        show-checkbox
        node-key="id"
        ref="tree"
        highlight-current
        :props="defaultProps"
        :default-checked-keys="defaultChecked"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCancel">取 消</el-button>
        <el-button type="primary" @click="handleSaveResource">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getRoleList,
  save,
  deleteRole,
  saveResource,
} from '@/api/role';

export default {
  name: 'Role',
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
      title: '新建',
      dialogFormVisible: false,
      labelWidth: '120px',
      form: {
        roleId: '',
        roleName: '',
        roleDescription: '',
        createTime: '',
        createUser: '',
        createUnit: '',
        resource: [],
      },
      resourceFormVisible: false,
      rules: {
        roleName: [{ required: true, message: '不能为空', trigger: 'change' }],
        roleDescription: [{ required: true, message: '不能为空', trigger: 'change' }],
      },
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'label',
      },
      defaultChecked: [],
      maxHeight: window.innerHeight - 210,
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
      };
      this.tableLoading = true;
      getRoleList(params).then((res) => {
        this.tableLoading = false;
        if (res.code === 200) {
          this.tableData = res.data;
        } else {
          this.tableData = [];
        }
      });
    },
    handleEdit(val) {
      this.form = {
        roleId: val.roleId,
        roleName: val.roleName,
        roleDescription: val.roleDescription,
        createTime: val.createTime,
        createUser: val.createUser,
        createUnit: val.createUnit,
      };
      this.op = 'update';
      this.title = '编辑';
      this.dialogFormVisible = true;
    },
    refresh() {
      this.getTableData();
    },
    handleSearch() {
      this.page.page = 1;
      this.getTableData();
    },
    handleAdd() {
      this.form = {
        roleId: '',
        roleName: '',
        roleDescription: '',
        createTime: '',
        createUser: this.$store.state.user.userName,
        createUnit: this.$store.state.user.organ,
        resource: [],
      };
      this.op = 'create';
      this.title = '新建';
      this.dialogFormVisible = true;
    },
    handleCancel() {
      this.dialogFormVisible = false;
      this.resourceFormVisible = false;
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
          });
        }
      });
    },
    handleDelete(id) {
      deleteRole({ roleId: id }).then((res) => {
        if (res.code === 200) {
          this.$message({
            type: 'success',
            message: '删除成功',
          });
        }
        this.getTableData();
      });
    },
    handleEditResource(val) {
      this.form.roleId = val.roleId;
      this.getMenuList();
      this.defaultChecked = this.getResource(val.resource);
      // this.defaultChecked = val.resource;
      this.resourceFormVisible = true;
    },
    getMenuList() {
      const { menuList } = this.$store.getters;
      const changeMenuList = list => list.map((item) => {
        if (item.children && item.children.length > 0) {
          return {
            label: item.meta.title,
            id: item.name,
            children: changeMenuList(item.children),
          };
        }
        return {
          label: item.meta.title,
          id: item.name,
        };
      });
      this.treeData = changeMenuList(menuList);
    },
    handleSaveResource() {
      const checkNodes = this.$refs.tree.getCheckedNodes();
      const halfCheckNodes = this.$refs.tree.getHalfCheckedNodes();
      this.form.resource = checkNodes.map(item => item.id);
      this.form.resource = this.form.resource.concat(halfCheckNodes.map(item => item.id));
      saveResource(this.form).then((res) => {
        if (res.code === 200) {
          this.$message({
            type: 'success',
            message: '保存成功',
          });
          this.getTableData();
        }
      });
    },
    getResource(resource) {
      const { menuList } = this.$store.getters;
      const changeResource = [];
      const filterResource = list => list.forEach((item) => {
        if (item.children && item.children.length > 0) {
          filterResource(item.children);
        } else if (resource.includes(item.name)) {
          changeResource.push(item.name);
        }
      });
      filterResource(menuList);
      return changeResource;
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
.el-tree {
  min-height: 300px;
  overflow-y: auto;
  max-height: 300px;
  border: 1px solid #e6e6e6;
}
</style>
