import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Route meta fields (RuoYi conventions)
 *
 * hidden: true                     // hide from sidebar (e.g. login, 404)
 * alwaysShow: true                 // when multiple children, nested mode; single child flattens unless alwaysShow
 * redirect: noRedirect             // breadcrumb not clickable
 * name: 'route-name'               // required for keep-alive
 * query: '{"id": 1}'               // default route query
 * roles / permissions              // access control
 * meta: {
 *   noCache: true                  // disable keep-alive cache
 *   title, icon, breadcrumb, activeMenu
 * }
 */

// Public routes (no async permission load)
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path(.*)',
        component: () => import('@/views/redirect')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/views/register'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: () => import('@/views/index'),
        name: 'Index',
        meta: { title: 'Home', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: () => import('@/views/system/user/profile/index'),
        name: 'Profile',
        meta: { title: 'Profile', icon: 'user' }
      }
    ]
  },
  {
    path: '/portal',
    component: () => import('@/views/community/portal/layout/index'),
    hidden: true,
    redirect: '/portal/home',
    children: [
      { path: 'home', name: 'PortalHome', component: () => import('@/views/community/portal/home/index'), meta: { title: 'Community Home' } },
      { path: 'matter', name: 'PortalMatterList', component: () => import('@/views/community/portal/matter/index'), meta: { title: 'Guides' } },
      { path: 'matter-detail/:matterId(\\d+)', name: 'PortalMatterDetail', component: () => import('@/views/community/portal/matter-detail/index'), meta: { title: 'Matter Detail' } },
      { path: 'apply', name: 'PortalApply', component: () => import('@/views/community/portal/apply/index'), meta: { title: 'Apply Online' } },
      { path: 'my-apply', name: 'PortalMyApply', component: () => import('@/views/community/portal/my-apply/index'), meta: { title: 'My Applications' } },
      { path: 'visit', name: 'PortalVisit', component: () => import('@/views/community/portal/visit/index'), meta: { title: 'Doorstep Visit' } },
      { path: 'notice', name: 'PortalNotice', component: () => import('@/views/community/portal/notice/index'), meta: { title: 'Community Notices' } }
    ]
  }
]

// Dynamic routes merged with server menu routes
export const dynamicRoutes = [
  {
    path: '/system/user-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:user:edit'],
    children: [
      {
        path: 'role/:userId(\\d+)',
        component: () => import('@/views/system/user/authRole'),
        name: 'AuthRole',
        meta: { title: 'Assign roles', activeMenu: '/system/user' }
      }
    ]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    permissions: ['system:role:edit'],
    children: [
      {
        path: 'user/:roleId(\\d+)',
        component: () => import('@/views/system/role/authUser'),
        name: 'AuthUser',
        meta: { title: 'Assign users', activeMenu: '/system/role' }
      }
    ]
  },
  {
    path: '/system/dict-data',
    component: Layout,
    hidden: true,
    permissions: ['system:dict:list'],
    children: [
      {
        path: 'index/:dictId(\\d+)',
        component: () => import('@/views/system/dict/data'),
        name: 'Data',
        meta: { title: 'Dictionary data', activeMenu: '/system/dict' }
      }
    ]
  },
]

// Avoid vue-router redundant navigation rejection noise
let routerPush = Router.prototype.push;
let routerReplace = Router.prototype.replace;
Router.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
Router.prototype.replace = function replace(location) {
  return routerReplace.call(this, location).catch(err => err)
}

export default new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
