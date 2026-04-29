import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import { getToken } from '@/utils/auth'
import { isRelogin } from '@/utils/request'

NProgress.configure({ showSpinner: false })

const whiteList = ['/login', '/register']

function isPortalFrontUser (roles) {
  if (!roles || roles.length === 0) {
    return false
  }
  if (roles.includes('admin')) {
    return false
  }
  if (roles.includes('community_staff')) {
    return false
  }
  return roles.includes('resident')
}

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    to.meta.title && store.dispatch('settings/setTitle', to.meta.title)
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done()
    } else if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      if (store.getters.roles.length === 0) {
        isRelogin.show = true
        // 判断当前用户是否已拉取完user_info信息
        store.dispatch('GetInfo').then(() => {
          isRelogin.show = false
          store.dispatch('GenerateRoutes').then(accessRoutes => {
            // 根据roles权限生成可访问的路由表
            router.addRoutes(accessRoutes) // 动态添加可访问路由表
            next({ ...to, replace: true }) // hack方法 确保addRoutes已完成
          })
        }).catch(err => {
            store.dispatch('LogOut').then(() => {
              Message.error(err)
              next({ path: '/' })
            })
          })
      } else {
        if (isPortalFrontUser(store.getters.roles)) {
          if (to.path === '/' || to.path === '/index') {
            next({ path: '/portal/home', replace: true })
            NProgress.done()
            return
          }
          var allowPortal = to.path.startsWith('/portal')
          var allowProfile = to.path.startsWith('/user')
          if (!allowPortal && !allowProfile) {
            next({ path: '/portal/home', replace: true })
            NProgress.done()
            return
          }
        } else if (to.path.startsWith('/portal')) {
          next({ path: '/index', replace: true })
          NProgress.done()
          return
        }
        next()
      }
    }
  } else {
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next(`/login?redirect=${encodeURIComponent(to.fullPath)}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done()
})
