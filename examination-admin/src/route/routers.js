import Main from '@/components/main';
import config from '@/config';

/**
 * meta除了原生参数外可配置的参数:
 * meta: {
 *  title: { String|Number|Function }
 *         显示在侧边栏、面包屑和标签栏的文字
 *         使用'{{ 多语言字段 }}'形式结合多语言使用，例子看多语言的路由配置;
 *         可以传入一个回调函数，参数是当前路由对象，例子看动态路由和带参路由
 *  hideInBread: (false) 设为true后此级路由将不会出现在面包屑中
 *  hideInMenu: (false) 设为true后在左侧菜单不会显示该页面选项
 *  notCache: (false) 设为true后页面在切换标签后不会缓存，如果需要缓存，无需设置这个字段，而且需要设置页面组件name属性和路由配置的name一致
 *  access: (null) 可访问该页面的权限数组，当前路由设置的权限会影响子路由
 *  icon: (-) 该页面在左侧菜单、面包屑和标签导航处显示的图标，如果是自定义图标，需要在图标名称前加下划线'_'
 *  beforeCloseName: (-) 设置该字段，则在关闭当前tab页时会去'@/router/before-close.js'里寻找该字段名对应的方法，作为关闭前的钩子函数
 * }
 */

export default [
  {
    path: '/login',
    name: 'login',
    meta: {
      title: '登录',
      hideInMenu: true,
    },
    component: () => import(/* webpackChunkName: "login" */ '@/views/login'),
  },
  {
    path: '/',
    name: '_home',
    redirect: '/home',
    component: Main,
    meta: {
      hideInMenu: true,
      notCache: true,
    },
    children: [
      {
        path: '/home',
        name: 'home',
        meta: {
          hideInMenu: true,
          title: '首页',
          notCache: true,
          icon: 'iconfont icon-home',
        },
        component: () => import(/* webpackChunkName: "Home" */ '@/views/Home'),
      },
    ],
  },
  {
    path: '/examination',
    name: 'Examination',
    meta: {
      access: [config.super_role],
      icon: 'iconfont icon-tijianyuyue',
      title: '预约',
    },
    component: Main,
    children: [
      {
        path: 'reservation',
        name: 'Reservation',
        meta: {
          access: [config.super_role],
          icon: 'iconfont icon-tijianyuyuedengji',
          title: '医技预约',
        },
        component: () => import(/* webpackChunkName: "reservation" */ '@/views/reservation'),
      },
    ],
  },
  {
    path: '/resource',
    name: 'Resource',
    meta: {
      access: [config.super_role],
      icon: 'iconfont icon-component1',
      title: '资源',
    },
    component: Main,
    children: [
      {
        path: 'checkItem',
        name: 'CheckItem',
        meta: {
          icon: 'iconfont icon-tijianxiangmu',
          title: '检查项目',
          access: [config.super_role],
        },
        component: () => import(/* webpackChunkName: "checkItem" */ '@/views/checkItem'),
      },
      {
        path: 'limb',
        name: 'Limb',
        meta: {
          icon: 'iconfont icon-wodetijian',
          title: '检查部位',
          access: [config.super_role],
        },
        component: () => import(/* webpackChunkName: "limb" */ '@/views/limb'),
      },
      {
        path: 'reserveTime',
        name: 'ReserveTime',
        meta: {
          icon: 'iconfont icon-yuyuexinxi1',
          title: '预约时段维护',
          access: [config.super_role],
        },
        component: () => import(/* webpackChunkName: "reserveTime" */ '@/views/reserveTime'),
      },
      {
        path: 'reserveTemplate',
        name: 'ReserveTemplate',
        meta: {
          icon: 'iconfont icon-ic_temp',
          title: '预约资源模板',
          access: [config.super_role],
        },
        component: () => import(/* webpackChunkName: "reserveTemplate" */ '@/views/reserveTemplate'),
      },
      {
        path: 'reserveResource',
        name: 'ReserveResource',
        meta: {
          icon: 'iconfont icon-xueshengtijianshuju',
          title: '预约资源',
          access: [config.super_role],
        },
        component: () => import(/* webpackChunkName: "reserveResource" */ '@/views/reserveResource'),
      },
    ],
  },
  {
    path: '/systemConfig',
    name: 'SystemConfig',
    meta: {
      access: [config.super_role],
      icon: 'iconfont icon-systemmanage',
      title: '系统管理',
    },
    component: Main,
    children: [
      {
        path: 'userList',
        name: 'User',
        meta: {
          access: [config.super_role],
          icon: 'iconfont icon-user',
          title: '用户管理',
        },
        component: () => import(/* webpackChunkName: "user" */ '@/views/user'),
      },
      {
        path: 'roleList',
        name: 'Role',
        meta: {
          access: [config.super_role],
          icon: 'iconfont icon-role',
          title: '角色管理',
        },
        component: () => import(/* webpackChunkName: "role" */ '@/views/role'),
      },
      {
        path: 'organ',
        name: 'Organ',
        meta: {
          access: [config.super_role],
          icon: 'iconfont icon-organization',
          title: '机构管理',
        },
        component: () => import(/* webpackChunkName: "organ" */ '@/views/organ'),
      },
      {
        path: 'notice',
        name: 'Notice',
        meta: {
          access: [config.super_role],
          icon: 'iconfont icon-tijianxuzhi',
          title: '提示信息维护',
        },
        component: () => import(/* webpackChunkName: "notice" */ '@/views/notice'),
      },
      {
        path: 'reserveSetting',
        name: 'ReserveSetting',
        meta: {
          access: [config.super_role],
          icon: 'iconfont icon-systemset',
          title: '预约设置',
        },
        component: () => import(/* webpackChunkName: "reserveSetting" */ '@/views/reserveSetting'),
      },
    ],
  },
  {
    path: '/401',
    name: 'error_401',
    meta: {
      hideInMenu: true,
    },
    component: () => import(/* webpackChunkName: "401" */ '@/views/error-page/401.vue'),
  },
  {
    path: '/500',
    name: 'error_500',
    meta: {
      hideInMenu: true,
    },
    component: () => import(/* webpackChunkName: "500" */ '@/views/error-page/500.vue'),
  },
  {
    path: '*',
    name: 'error_404',
    meta: {
      hideInMenu: true,
    },
    component: () => import(/* webpackChunkName: "404" */ '@/views/error-page/404.vue'),
  },
];
