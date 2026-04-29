<template>
  <div class="login-page">
    <div class="login-bg-layer" aria-hidden="true" />
    <div class="login-card-wrap">
      <div class="login-brand">
        <div class="login-brand-logo">
          <svg-icon icon-class="user" class="brand-icon" />
        </div>
        <div class="login-brand-text">
          <h1 class="login-title">社区一网通办系统</h1>
          <p class="login-subtitle">欢迎使用 · 请登录后继续操作</p>
        </div>
      </div>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            type="text"
            auto-complete="off"
            placeholder="请输入账号"
          >
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            placeholder="请输入密码"
            @keyup.enter.native="handleLogin"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="code" v-if="captchaEnabled">
          <el-input
            v-model="loginForm.code"
            auto-complete="off"
            placeholder="请输入验证码"
            style="width: 63%"
            @keyup.enter.native="handleLogin"
          >
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
          <div class="login-code">
            <img :src="codeUrl" @click="getCode" class="login-code-img" alt="验证码，点击刷新" title="点击更换验证码"/>
          </div>
        </el-form-item>
        <el-checkbox v-model="loginForm.rememberMe" class="remember-check">记住密码</el-checkbox>
        <el-form-item class="login-actions">
          <el-button
            :loading="loading"
            size="medium"
            type="primary"
            class="submit-btn"
            @click.native.prevent="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登录中…</span>
          </el-button>
          <div class="register-link" v-if="register">
            <router-link class="link-type" :to="'/register'">没有账号？去注册</router-link>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { getCodeImg } from "@/api/login";
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'

export default {
  name: "Login",
  data() {
    return {
      codeUrl: "",
      loginForm: {
        username: "admin",
        password: "admin123",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      captchaEnabled: true,
      register: true,
      redirect: undefined
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.getCode();
    this.getCookie();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.loginForm.uuid = res.uuid;
        }
      });
    },
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe),
        code: this.loginForm.code,
        uuid: this.loginForm.uuid
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{});
          }).catch(() => {
            this.loading = false;
            if (this.captchaEnabled) {
              this.getCode();
            }
          });
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
/* 与若依侧边栏深色主题 #304156、主色 #409EFF 呼应 */
$ruoyi-sidebar: #304156;
$ruoyi-primary: #409eff;

.login-page {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 24px;
  box-sizing: border-box;
}

.login-bg-layer {
  position: fixed;
  inset: 0;
  background: url("../assets/images/login-background.jpg") center center / cover no-repeat;
  z-index: 0;

  &::after {
    content: "";
    position: absolute;
    inset: 0;
    background: linear-gradient(
      125deg,
      rgba($ruoyi-sidebar, 0.88) 0%,
      rgba($ruoyi-sidebar, 0.45) 42%,
      rgba($ruoyi-primary, 0.22) 100%
    );
    animation: loginTint 12s ease-in-out infinite alternate;
  }
}

@keyframes loginTint {
  0% { opacity: 1; filter: saturate(1); }
  100% { opacity: 0.92; filter: saturate(1.06); }
}

.login-card-wrap {
  position: relative;
  z-index: 1;
  width: 100%;
  max-width: 420px;
  border-radius: 12px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.96);
  box-shadow:
    0 4px 24px rgba(48, 65, 86, 0.12),
    0 24px 48px rgba(0, 0, 0, 0.08);
  animation: loginCardIn 0.55s cubic-bezier(0.22, 1, 0.36, 1) forwards;
  backdrop-filter: blur(10px);

  &::before {
    content: "";
    display: block;
    height: 4px;
    background: linear-gradient(90deg, $ruoyi-sidebar 0%, $ruoyi-primary 100%);
  }
}

@keyframes loginCardIn {
  from {
    opacity: 0;
    transform: translateY(22px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-brand {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 22px 24px 8px;

  &-logo {
    width: 48px;
    height: 48px;
    border-radius: 10px;
    background: linear-gradient(135deg, $ruoyi-sidebar, lighten($ruoyi-sidebar, 12%));
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    animation: brandPulse 2.5s ease-in-out infinite;
  }
}

@keyframes brandPulse {
  0%, 100% { box-shadow: 0 0 0 0 rgba($ruoyi-primary, 0.35); }
  50% { box-shadow: 0 0 0 8px rgba($ruoyi-primary, 0); }
}

.brand-icon {
  font-size: 24px;
}

.login-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  letter-spacing: 1px;
}

.login-subtitle {
  margin: 6px 0 0;
  font-size: 13px;
  color: #909399;
}

.login-form {
  padding: 8px 24px 24px;

  .el-input {
    height: 40px;

    .el-input__inner {
      height: 40px;
      line-height: 40px;
      border-radius: 8px;
      background-color: #f5f7fa;
      border-color: #e4e7ed;
      transition: border-color 0.2s, box-shadow 0.2s;

      &:focus {
        background: #fff;
        border-color: rgba($ruoyi-primary, 0.55);
        box-shadow: 0 0 0 2px rgba($ruoyi-primary, 0.12);
      }
    }
  }

  .input-icon {
    height: 40px;
    width: 14px;
    margin-left: 2px;
  }

  .remember-check {
    margin: 0 0 18px;
    color: #606266;
  }

  .login-actions {
    margin-bottom: 0;
  }

  .submit-btn {
    width: 100%;
    height: 42px;
    border-radius: 8px;
    font-size: 15px;
    letter-spacing: 6px;
    text-indent: 6px;
    border: none;
    background: linear-gradient(90deg, $ruoyi-sidebar 0%, darken($ruoyi-primary, 6%) 100%);
    transition: transform 0.2s ease, box-shadow 0.2s ease;

    &:hover,
    &:focus {
      transform: translateY(-1px);
      box-shadow: 0 8px 20px rgba($ruoyi-primary, 0.35);
    }
  }

  .register-link {
    margin-top: 14px;
    text-align: center;
  }
}

.login-code {
  width: 33%;
  height: 40px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
    border-radius: 6px;
    height: 40px;
    transition: transform 0.15s;

    &:hover {
      transform: scale(1.03);
    }
  }
}

.link-type {
  color: $ruoyi-primary;
  font-size: 13px;

  &:hover {
    color: lighten($ruoyi-primary, 6%);
  }
}
</style>
