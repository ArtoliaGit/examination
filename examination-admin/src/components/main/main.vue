<template>
  <el-container class="main">
    <el-aside class="sidebar-container">
      <side-menu :menu-list="menuList" :is-collapse="isCollapse" />
    </el-aside>
    <el-container class="main-container">
      <el-header style="box-shadow: 0px 2px 5px #b9b5b5;">
        <header-bar
          :is-collapse="isCollapse"
          @on-collapse="handleCollapse"
        />
      </el-header>
      <el-container style="overflow: hide;">
        <!-- <el-header height="40px" style="padding: 0;">
          <div class="tag-nav-wrapper">
            <tags-nav
              :value="$route"
              @input="handleClick"
              :list="tagNavList"
              @on-close="handleCloseTag"
            />
          </div>
        </el-header> -->
        <el-main>
          <transition name="fade-transform" mode="out-in">
            <keep-alive :include="cacheList">
              <router-view class="show-content" />
            </keep-alive>
          </transition>
        </el-main>
      </el-container>
    </el-container>
  </el-container>
</template>

<script>
import { mapMutations } from 'vuex';
import './main.scss';
import SideMenu from './side-menu';
import HeaderBar from './header-bar';
import TagsNav from './tags-nav';
import routers from '@/route/routers';
import { getNewTagList, routeEqual } from '@/utils/utils';

export default {
  name: 'Main',
  components: {
    SideMenu,
    HeaderBar,
    // TagsNav,
  },
  data() {
    return {
      isCollapse: false,
    };
  },
  computed: {
    menuList() {
      return this.$store.getters.menuList;
    },
    tagNavList() {
      return this.$store.state.app.tagNavList;
    },
    cacheList() {
      const list = [
        'ParentView',
        ...(this.tagNavList.length
          ? this.tagNavList
            .filter(item => !(item.meta && item.meta.notCache))
            .map(item => item.name)
          : []),
      ];
      return list;
    },
  },
  methods: {
    ...mapMutations([
      'setBreadCrumb',
      'setHomeRoute',
      'setTagNavList',
      'addTag',
      'closeTag',
    ]),
    handleCollapse(state) {
      this.isCollapse = state;
    },
    handleCloseTag(res, type, route) {
      if (type !== 'others') {
        if (type === 'all') {
          this.turnToPage(this.$config.homeName);
        } else if (routeEqual(this.$route, route)) {
          this.closeTag(route);
        }
      }
      this.setTagNavList(res);
    },
    handleClick(item) {
      this.turnToPage(item);
    },
    turnToPage(route) {
      let { name, params, query } = {};
      if (typeof route === 'string') name = route;
      else {
        ({ name, params, query } = route);
      }
      if (name.indexOf('isTurnByHref_') > -1) {
        window.open(name.split('_')[1]);
        return;
      }
      this.$router.push({
        name,
        params,
        query,
      });
    },
  },
  watch: {
    $route(newRoute) {
      const {
        name, query, params, meta,
      } = newRoute;
      this.addTag({
        route: {
          name, query, params, meta,
        },
        type: 'push',
      });
      this.setBreadCrumb(newRoute);
      this.setTagNavList(getNewTagList(this.tagNavList, newRoute));
    },
  },
  mounted() {
    this.setTagNavList();
    this.setHomeRoute(routers);
    const {
      name, params, query, meta,
    } = this.$route;
    this.addTag({
      route: {
        name, params, query, meta,
      },
    });
    this.setBreadCrumb(this.$route);
    if (!this.tagNavList.find(item => item.name === this.$route.name)) {
      this.$router.push({
        name: this.$config.homeName,
      });
    }
  },
};
</script>
