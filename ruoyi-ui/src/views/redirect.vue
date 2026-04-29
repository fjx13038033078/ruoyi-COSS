<template>
  <div class="redirect-wrap">
    <div class="redirect-card">
      <i class="el-icon-loading redirect-icon" />
      <p class="redirect-msg">正在跳转，请稍候…</p>
      <p class="redirect-tip">若长时间无响应，请返回首页重试</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RouteRedirect',
  created() {
    const { params, query } = this.$route
    const { path } = params
    this.$router.replace({ path: '/' + path, query }).catch(() => {})
  }
}
</script>

<style scoped lang="scss">
$sidebar: #304156;
$primary: #409eff;

.redirect-wrap {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
  box-sizing: border-box;
  background: linear-gradient(145deg, rgba($sidebar, 0.06) 0%, rgba($primary, 0.09) 100%);
}

.redirect-card {
  text-align: center;
  padding: 40px 48px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.95);
  box-shadow: 0 8px 32px rgba(48, 65, 86, 0.12);
  animation: redirectFade 0.45s ease-out;
}

@keyframes redirectFade {
  from {
    opacity: 0;
    transform: scale(0.96);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}

.redirect-icon {
  font-size: 40px;
  color: $primary;
  animation: spin 0.95s linear infinite;
}

@keyframes spin {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.redirect-msg {
  margin: 20px 0 8px;
  font-size: 16px;
  font-weight: 500;
  color: #303133;
}

.redirect-tip {
  margin: 0;
  font-size: 13px;
  color: #909399;
}
</style>
