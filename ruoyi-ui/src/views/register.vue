<template>
  <div class="register-page">
    <div class="register-bg-layer" aria-hidden="true" />
    <div class="register-card-wrap">
      <div class="register-brand">
        <div class="register-brand-logo">
          <svg-icon icon-class="user" class="brand-icon" />
        </div>
        <div class="register-brand-text">
          <h1 class="register-title">居民账号注册</h1>
          <p class="register-subtitle">社区一网通办 · 注册后可使用群众端服务</p>
        </div>
      </div>
      <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form">
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" type="text" auto-complete="off" placeholder="请输入账号（2-20 个字符）">
            <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            auto-complete="off"
            placeholder="请输入密码（5-20 个字符）"
            @keyup.enter.native="handleRegister"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            auto-complete="off"
            placeholder="请再次输入密码"
            @keyup.enter.native="handleRegister"
          >
            <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
          </el-input>
        </el-form-item>
        <el-form-item prop="code" v-if="captchaEnabled">
          <el-input
            v-model="registerForm.code"
            auto-complete="off"
            placeholder="请输入验证码"
            style="width: 63%"
            @keyup.enter.native="handleRegister"
          >
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
          <div class="register-code">
            <img :src="codeUrl" @click="getCode" class="register-code-img" alt="验证码，点击刷新" title="点击更换验证码"/>
          </div>
        </el-form-item>
        <el-form-item class="register-actions">
          <el-button
            :loading="loading"
            size="medium"
            type="primary"
            class="submit-btn"
            @click.native.prevent="handleRegister"
          >
            <span v-if="!loading">注 册</span>
            <span v-else>提交中…</span>
          </el-button>
          <div class="back-login-link">
            <router-link class="link-type" :to="'/login'">已有账号？返回登录</router-link>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { getCodeImg, register } from "@/api/login";

export default {
  name: "Register",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      codeUrl: "",
      registerForm: {
        username: "",
        password: "",
        confirmPassword: "",
        code: "",
        uuid: ""
      },
      registerRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入账号" },
          { min: 2, max: 20, message: '账号长度须在 2 到 20 个字符之间', trigger: 'blur' }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入密码" },
          { min: 5, max: 20, message: '密码长度须在 5 到 20 个字符之间', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, trigger: "blur", message: "请再次输入密码" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      captchaEnabled: true
    };
  },
  created() {
    this.getCode();
  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.registerForm.uuid = res.uuid;
        }
      });
    },
    handleRegister() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true;
          register(this.registerForm).then(res => {
            const username = this.registerForm.username;
            this.$alert(
              "<span style=\"color:#67c23a\">账号 <strong>" + username + "</strong> 注册成功，请登录。</span>",
              '提示',
              {
                dangerouslyUseHTMLString: true,
                type: 'success'
              }
            ).then(() => {
              this.$router.push("/login");
            }).catch(() => {});
          }).catch(() => {
            this.loading = false;
            if (this.captchaEnabled) {
              this.getCode();
            }
          })
        }
      });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
$ruoyi-sidebar: #304156;
$ruoyi-primary: #409eff;

.register-page {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  padding: 24px;
  box-sizing: border-box;
}

.register-bg-layer {
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
      rgba($ruoyi-sidebar, 0.82) 0%,
      rgba(lighten($ruoyi-sidebar, 10%), 0.42) 50%,
      rgba($ruoyi-primary, 0.28) 100%
    );
    animation: regTint 12s ease-in-out infinite alternate;
  }
}

@keyframes regTint {
  0% { opacity: 1; }
  100% { opacity: 0.93; }
}

.register-card-wrap {
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
  animation: registerCardIn 0.58s cubic-bezier(0.22, 1, 0.36, 1) forwards;
  backdrop-filter: blur(10px);

  &::before {
    content: "";
    display: block;
    height: 4px;
    background: linear-gradient(90deg, $ruoyi-sidebar 0%, $ruoyi-primary 100%);
  }
}

@keyframes registerCardIn {
  from {
    opacity: 0;
    transform: translateY(24px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.register-brand {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 22px 24px 8px;

  &-logo {
    width: 48px;
    height: 48px;
    border-radius: 10px;
    background: linear-gradient(135deg, darken($ruoyi-primary, 8%), $ruoyi-primary);
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    animation: brandPulseReg 2.5s ease-in-out infinite;
  }
}

@keyframes brandPulseReg {
  0%, 100% { box-shadow: 0 0 0 0 rgba($ruoyi-primary, 0.4); }
  50% { box-shadow: 0 0 0 8px rgba($ruoyi-primary, 0); }
}

.brand-icon {
  font-size: 24px;
}

.register-title {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  letter-spacing: 1px;
}

.register-subtitle {
  margin: 6px 0 0;
  font-size: 13px;
  color: #909399;
}

.register-form {
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

  .back-login-link {
    margin-top: 14px;
    text-align: center;
  }
}

.register-actions {
  margin-bottom: 0;
}

.register-code {
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
