<template>
  <div class="portal-shell">
    <header class="portal-header">
      <div class="portal-brand" @click="$router.push('/portal/home')">社区一网通办</div>
      <nav class="portal-nav">
        <router-link to="/portal/home">首页</router-link>
        <router-link to="/portal/matter">办事指南</router-link>
        <router-link to="/portal/apply">在线申报</router-link>
        <router-link to="/portal/my-apply">我的办件</router-link>
        <router-link to="/portal/visit">上门服务</router-link>
        <router-link to="/portal/notice">通知公告</router-link>
        <router-link to="/user/profile">个人中心</router-link>
      </nav>
      <div class="portal-actions">
        <el-tooltip content="无障碍：放大页面字体">
          <el-button size="mini" type="text" @click="$store.dispatch('app/togglePortalLargeFont')">
            {{ portalLargeFont ? '标准字体' : '大字体' }}
          </el-button>
        </el-tooltip>
        <el-dropdown trigger="click" @command="onUserCommand">
          <span class="portal-user"><img :src="avatar" class="avatar" /> {{ name }}</span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile">个人中心</el-dropdown-item>
            <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </header>
    <main class="portal-main">
      <router-view />
    </main>
    <footer class="portal-footer">毕设演示——社区一网通办</footer>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'PortalLayout',
  computed: {
    ...mapGetters(['avatar', 'name']),
    portalLargeFont() {
      return this.$store.getters.portalLargeFont
    }
  },
  methods: {
    onUserCommand(cmd) {
      if (cmd === 'profile') {
        this.$router.push('/user/profile')
      } else if (cmd === 'logout') {
        this.$confirm('确定退出登录吗？', '提示', { type: 'warning' })
          .then(() => this.$store.dispatch('LogOut'))
          .then(() => { location.href = '/login' })
      }
    }
  }
}
</script>

<style scoped>
.portal-shell { min-height: 100vh; display: flex; flex-direction: column; background: #f5f7fa; }
.portal-header {
  height: 56px; padding: 0 24px; display: flex; align-items: center; justify-content: space-between;
  background: #304156; color: #fff;
}
.portal-brand { font-size: 18px; font-weight: 600; cursor: pointer; }
.portal-nav a {
  margin: 0 10px; color: #bdc8d4; text-decoration: none; font-size: 14px;
}
.portal-nav a.router-link-active { color: #fff; font-weight: 600; }
.portal-actions { display: flex; align-items: center; gap: 12px; }
.portal-user { display: inline-flex; align-items: center; gap: 8px; cursor: pointer; color: #eee; font-size: 14px; }
.avatar { width: 28px; height: 28px; border-radius: 50%; vertical-align: middle; }
.portal-main { flex: 1; padding: 16px 20px 40px; max-width: 1100px; width: 100%; margin: 0 auto; box-sizing: border-box; }
.portal-footer { text-align: center; padding: 12px; color: #909399; font-size: 13px; }
</style>
