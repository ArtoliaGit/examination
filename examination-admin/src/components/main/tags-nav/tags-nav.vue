<template>
  <div class="tags-nav">
    <div class="close-con">
      <el-dropdown @command="handleTagsOption" style="height: 100%;">
        <el-button size="small" type="text" style="height: 100%;">
          <i style="font-size: 18px" class="el-icon-circle-close-outline" />
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item
            v-for="(item, key) of menuList"
            :command="key"
            :key="key"
          >
            {{ item }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <div class="btn-con left-btn">
      <el-button type="text" @click="handleScroll(240)">
        <i style="font-size: 18px" class="el-icon-arrow-left" />
      </el-button>
    </div>
    <div class="btn-con right-btn">
      <el-button type="text" @click="handleScroll(-240)">
        <i style="font-size: 18px" class="el-icon-arrow-right" />
      </el-button>
    </div>
    <div
      class="scroll-outer"
      ref="scrollOuter"
      @DOMMouseScroll="handlescroll"
      @mousewheel="handlescroll"
    >
      <div ref="scrollBody" class="scroll-body" :style="{left: tagBodyLeft + 'px'}">
        <transition-group name="taglist-moving-animation">
          <router-link
            v-for="(item, index) in list"
            ref="tagsPageOpened"
            :key="`tag-nav-${index}`"
            :class="isCurrentTag(item)?'active':''"
            :data-route-item="item"
            :to="{ path: item.path, query: item.query, fullPath: item.fullPath }"
            tag="span"
            class="tags-view-item"
            @click.native="handleClick(item)"
            @contextmenu.prevent.native="contextMenu(item, $event)"
          >
            {{ showTitleInside(item) }}
            <span
              v-if="item.name !== $config.homeName"
              class="el-icon-close"
              @click.prevent.stop="handleClose(item)"
            />
          </router-link>
        </transition-group>
      </div>
    </div>
  </div>
</template>

<script>
import { showTitle, routeEqual } from '@/utils/utils';
import beforeClose from '@/route/before-close';

export default {
  name: 'TagsNav',
  props: {
    value: {
      type: Object,
      default: undefined,
    },
    list: {
      type: Array,
      default() {
        return [];
      },
    },
  },
  data() {
    return {
      tagBodyLeft: 0,
      rightOffset: 40,
      outerPadding: 4,
      contextMenuLeft: 0,
      contextMenuTop: 0,
      visible: false,
      menuList: {
        others: '关闭其他',
        all: '关闭所有',
      },
    };
  },
  computed: {
    currentRouteObj() {
      const { name, params, query } = this.value;
      return { name, params, query };
    },
  },
  methods: {
    handlescroll(e) {
      const { type } = e;
      let delta = 0;
      if (type === 'DOMMouseScroll' || type === 'mousewheel') {
        delta = (e.wheelDelta) ? e.wheelDelta : -(e.detail || 0) * 40;
      }
      this.handleScroll(delta);
    },
    handleScroll(offset) {
      const outerWidth = this.$refs.scrollOuter.offsetWidth;
      const bodyWidth = this.$refs.scrollBody.offsetWidth;
      if (offset > 0) {
        this.tagBodyLeft = Math.min(0, this.tagBodyLeft + offset);
      } else if (outerWidth < bodyWidth) {
        if (this.tagBodyLeft < -(bodyWidth - outerWidth)) {
          this.tagBodyLeft = this.tagBodyLeft;
        } else {
          this.tagBodyLeft = Math.max(this.tagBodyLeft + offset, outerWidth - bodyWidth);
        }
      } else {
        this.tagBodyLeft = 0;
      }
    },
    handleTagsOption(type) {
      if (type.includes('all')) {
        // 关闭所有，除了home
        const res = this.list.filter(item => item.name === this.$config.homeName);
        this.$emit('on-close', res, 'all');
      } else if (type.includes('others')) {
        // 关闭除当前页和home页的其他页
        const res = this.list.filter(
          item => routeEqual(this.currentRouteObj, item) || item.name === this.$config.homeName,
        );
        this.$emit('on-close', res, 'others', this.currentRouteObj);
        setTimeout(() => {
          this.getTagElementByRoute(this.currentRouteObj);
        }, 100);
      }
    },
    handleClose(current) {
      if (current.meta
        && current.meta.beforeCloseName
        && current.meta.beforeCloseName in beforeClose) {
        new Promise(beforeClose[current.meta.beforeCloseName]).then((close) => {
          if (close) {
            this.close(current);
          }
        });
      } else {
        this.close(current);
      }
    },
    close(route) {
      const res = this.list.filter(item => !routeEqual(route, item));
      this.$emit('on-close', res, undefined, route);
    },
    handleClick(item) {
      this.$emit('input', item);
    },
    showTitleInside(item) {
      return showTitle(item, this);
    },
    isCurrentTag(item) {
      return routeEqual(this.currentRouteObj, item);
    },
    moveToView(tag) {
      const outerWidth = this.$refs.scrollOuter.offsetWidth;
      const bodyWidth = this.$refs.scrollBody.offsetWidth;
      if (bodyWidth < outerWidth) {
        this.tagBodyLeft = 0;
      } else if (tag.offsetLeft < -this.tagBodyLeft) {
        // 标签在可视区域左侧
        this.tagBodyLeft = -tag.offsetLeft + this.outerPadding;
      } else if (tag.offsetLeft > -this.tagBodyLeft
        && tag.offsetLeft + tag.offsetWidth < -this.tagBodyLeft + outerWidth) {
        // 标签在可视区域
        const calcWidth = outerWidth - tag.offsetWidth - tag.offsetLeft - this.outerPadding;
        this.tagBodyLeft = Math.min(0, calcWidth);
      } else {
        // 标签在可视区域右侧
        this.tagBodyLeft = -(tag.offsetLeft - (outerWidth - this.outerPadding - tag.offsetWidth));
      }
    },
    getTagElementByRoute(route) {
      this.$nextTick(() => {
        this.refsTag = this.$refs.tagsPageOpened;
        this.refsTag.forEach((item, index) => {
          if (routeEqual(route, item.$attrs['data-route-item'])) {
            const tag = this.refsTag[index].$el;
            this.moveToView(tag);
          }
        });
      });
    },
    contextMenu(item, e) {
      if (item.name === this.$config.homeName) {
        return;
      }
      this.visible = true;
      const offsetLeft = this.$el.getBoundingClientRect().left;
      this.contextMenuLeft = e.clientX - offsetLeft + 10;
      this.contextMenuTop = e.clientY - 64;
    },
    closeMenu() {
      this.visible = false;
    },
  },
  watch: {
    $route(to) {
      this.getTagElementByRoute(to);
    },
    visible(value) {
      if (value) {
        document.body.addEventListener('click', this.closeMenu);
      } else {
        document.body.removeEventListener('click', this.closeMenu);
      }
    },
  },
  mounted() {
    setTimeout(() => {
      this.getTagElementByRoute(this.$route);
    }, 200);
  },
};
</script>

<style rel="stylesheet/scss" lang="scss">
  @import './tags-nav.scss';
</style>
