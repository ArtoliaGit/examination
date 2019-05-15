<template>
  <el-breadcrumb separator-class="el-icon-arrow-right">
    <el-breadcrumb-item
      v-for="item in list"
      :to="item.to"
      :key="`bread-crumb-${item.name}`"
    >
      <i :class="item.icon || ''" aria-hidden="true" />
      {{ showTitle(item) }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script>
import { showTitle } from '@/utils/utils';

export default {
  name: 'CustomBreadCrumb',
  props: {
    list: {
      type: Array,
      default: () => [],
    },
  },
  methods: {
    showTitle(item) {
      return showTitle(item, this);
    },
  },
};
</script>
