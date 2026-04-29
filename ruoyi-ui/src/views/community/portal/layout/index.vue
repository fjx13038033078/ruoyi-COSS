<template>
  <div class="portal-shell">
    <header class="portal-header">
      <div class="portal-brand" @click="$router.push('/portal/home')">Community One-Stop Services</div>
      <nav class="portal-nav">
        <router-link to="/portal/home">Home</router-link>
        <router-link to="/portal/matter">Guides</router-link>
        <router-link to="/portal/apply">Apply Online</router-link>
        <router-link to="/portal/my-apply">My Applications</router-link>
        <router-link to="/portal/visit">Doorstep Visit</router-link>
        <router-link to="/portal/notice">Notices</router-link>
        <router-link to="/user/profile">Profile</router-link>
      </nav>
      <div class="portal-actions">
        <el-tooltip content="Larger font for accessibility">
          <el-button size="mini" type="text" @click="$store.dispatch('app/togglePortalLargeFont')">
            {{ portalLargeFont ? 'Normal' : 'Large font' }}
          </el-button>
        </el-tooltip>
        <el-dropdown trigger="click" @command="onUserCommand">
          <span class="portal-user"><img :src="avatar" class="avatar" /> {{ name }}</span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="profile">Profile</el-dropdown-item>
            <el-dropdown-item command="logout" divided>Logout</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </header>
    <main class="portal-main">
      <router-view />
    </main>
    <footer class="portal-footer">Demo service for graduation project</footer>
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
        this.$confirm('Sign out?', 'Tip', { type: 'warning' })
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
