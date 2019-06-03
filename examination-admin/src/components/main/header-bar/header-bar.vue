<template>
  <div class="header-bar">
    <side-trigger
      class="side-trigger"
      :is-collapse="isCollapse"
      @on-change="handleCollapsedChange"
    />
    <custom-bread-crumb class="custom-bread-crumb" :list="breadCrumbList" />
    <div class="right-menu">
      <user />
    </div>
  </div>
</template>

<script>
import CustomBreadCrumb from './custom-bread-crumb';
import SideTrigger from './side-trigger';
import User from './user';

export default {
  name: 'HeaderBar',
  components: {
    CustomBreadCrumb,
    SideTrigger,
    User,
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
    handleCollapsedChange(state) {
      this.$emit('on-collapse', state);
    },
  },
};
</script>
