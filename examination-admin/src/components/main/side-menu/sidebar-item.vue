<template>
  <el-submenu :index="item.name">
    <template slot="title">
      <i :class="item.icon" />
      <span slot="title">{{ item.meta.title }}</span>
    </template>
    <template v-for="submenu in item.children">
      <template v-if="!submenu.children || (submenu.children && submenu.children.length === 0)">
        <el-menu-item :index="submenu.name" :key="submenu.name" :route="{ name: submenu.name }">
          <i :class="submenu.icon" />
          <span slot="title">{{ submenu.meta.title }}</span>
        </el-menu-item>
      </template>
      <template v-else>
        <sidebar-item :item="submenu" :key="submenu.name" />
      </template>
    </template>
  </el-submenu>
</template>

<script>
export default {
  name: 'SidebarItem',
  props: {
    item: {
      type: Object,
      required: true,
    },
  },
};
</script>
