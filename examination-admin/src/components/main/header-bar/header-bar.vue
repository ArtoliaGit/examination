<template>
  <div class="header-bar">
    <side-trigger
      class="side-trigger"
      :is-collapse="isCollapse"
      @on-change="handleCollapsedChange"
    />
    <custom-bread-crumb class="custom-bread-crumb" :list="breadCrumbList" />
    <div class="right-menu">
      <el-button type="primary" @click="handleLogout" size="mini">退出登陆</el-button>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex';
import CustomBreadCrumb from './custom-bread-crumb';
import SideTrigger from './side-trigger';

export default {
  name: 'HeaderBar',
  components: {
    CustomBreadCrumb,
    SideTrigger,
  },
  props: {
    isCollapse: {
      type: Boolean,
      default: false,
    },
  },
  computed: {
    breadCrumbList() {
      return this.$store.state.app.breadCrumbList;
    },
  },
  methods: {
    ...mapActions([
      'handleLogout',
    ]),
    handleCollapsedChange(state) {
      this.$emit('on-collapse', state);
    },
    logout() {
      this.handleLogout();
    },
  },
};
</script>
