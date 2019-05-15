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
        component: () => import('@/views/Home'),
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
        component: () => import('@/views/user'),
      },
      {
        path: 'roleList',
        name: 'Role',
        meta: {
          access: [config.super_role],
          icon: 'iconfont icon-role',
          title: '角色管理',
        },
        component: () => import('@/views/role'),
      },
    ],
  },
  {
    path: '/examination',
    name: 'Examination',
    meta: {
      access: [config.super_role],
      icon: 'fa fa-cog',
      title: '预约',
    },
    component: Main,
    children: [
      {
        path: 'reservation',
        name: 'Reservation',
        meta: {
          access: [config.super_role],
          icon: 'fa fa-cog',
          title: '医技预约',
        },
        component: () => import('@/views/reservation'),
      },
    ],
  },
  {
    path: '/components',
    name: 'components',
    meta: {
      access: [config.super_role],
      icon: 'fa fa-cog',
      title: '组件',
    },
    component: Main,
    children: [
      {
        path: 'message_page',
        name: 'message_page',
        meta: {
          icon: 'fa fa-cog',
          title: '消息中心',
          access: [config.super_role],
        },
        children: [
          {
            path: 'message_page1',
            name: 'message_page1',
            meta: {
              access: [config.super_role],
              icon: 'fa fa-cog',
              title: '消息中心1',
            },
            component: () => import('@/views/About'),
          },
          {
            path: 'message_page2',
            name: 'message_page2',
            meta: {
              icon: 'fa fa-cog',
              title: '消息中心2',
              access: [config.super_role],
            },
            children: [
              {
                path: 'message_page3',
                name: 'message_page3',
                meta: {
                  icon: 'fa fa-file-text-o',
                  title: '消息中心3',
                  access: [config.super_role],
                },
              },
              {
                path: 'message_page4',
                name: 'message_page4',
                meta: {
                  icon: 'fa fa-file-text-o',
                  title: '消息中心4',
                  access: [config.super_role],
                },
              },
            ],
          },
        ],
      },
      {
        path: 'about',
        name: 'about',
        meta: {
          access: [config.super_role],
          icon: 'fa fa-cog',
          title: '关于',
        },
        component: () => import('@/views/Home'),
      },
    ],
  },
  {
    path: '/401',
    name: 'error_401',
    meta: {
      hideInMenu: true,
    },
    component: () => import('@/views/error-page/401.vue'),
  },
  {
    path: '/500',
    name: 'error_500',
    meta: {
      hideInMenu: true,
    },
    component: () => import('@/views/error-page/500.vue'),
  },
  {
    path: '*',
    name: 'error_404',
    meta: {
      hideInMenu: true,
    },
    component: () => import('@/views/error-page/404.vue'),
  },
];
