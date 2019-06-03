<template>
  <div>
    <el-dropdown class="avatar-container" trigger="click">
      <div class="avatar-wrapper">
        <img src="../../../../assets/avator.jpg" class="user-avatar">
        <i class="el-icon-caret-bottom" />
      </div>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item>
          <span style="display:block;" @click="logout">退出登录</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
import './user.scss';
import { mapActions } from 'vuex';

export default {
  data() {
    return {

    };
  },
  methods: {
    ...mapActions([
      'handleLogout',
    ]),
    logout() {
      this.handleLogout();
    },
  },
};
</script>
