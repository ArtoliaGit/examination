<template>
  <el-scrollbar wrap-class="scrollbar-wrapper">
    <el-menu
      ref="menu"
      :default-active="getRouteIndex"
      :collapse="isCollapse"
      mode="vertical"
      :router="true"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409eff"
      class="el-menu-vertical-custom"
    >
      <template v-for="item in menuList">
        <template v-if="!item.children || (item.children && item.children.length === 0)">
          <el-menu-item :index="item.name" :key="item.name" :route="{ path: item.name }">
            <i :class="item.meta.icon" />
            <span slot="title">{{ item.meta.title }}</span>
          </el-menu-item>
        </template>
        <template v-else>
          <sidebar-item :item="item" :key="item.name" />
        </template>
      </template>
    </el-menu>
  </el-scrollbar>
</template>

<script>
import SidebarItem from './sidebar-item';

export default {
  name: 'SideMenu',
  components: {
    SidebarItem,
  },
  computed: {
    getRouteIndex() {
      return this.$route.name;
    },
  },
  props: {
    menuList: {
      type: Array,
      required: true,
    },
    isCollapse: {
      type: Boolean,
      default: false,
    },
  },
};
</script>
