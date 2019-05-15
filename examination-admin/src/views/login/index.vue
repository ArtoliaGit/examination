<template>
  <div class="login">
    <el-card class="login-content">
      <div class="login-title">
        <span>系统登录</span>
      </div>
      <el-form
        ref="loginForm"
        :model="form"
        :rules="rules"
        @keydown.enter.native="handleSubmit"
      >
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名">
            <template slot="prepend">
              <i class="iconfont icon-user" />
            </template>
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="form.password"
            placeholder="请输入密码"
            :type="hidePassword ? 'password' : 'text'"
          >
            <template slot="prepend">
              <i class="iconfont icon-lock-fill" />
            </template>
            <div slot="suffix" class="eye" @click="handleClickEye">
              <i class="iconfont icon-eyeclose-fill" v-if="hidePassword" />
              <i class="iconfont icon-eye-fill" v-else />
            </div>
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleSubmit">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { mapActions } from 'vuex';

export default {
  data() {
    return {
      form: {
        username: '',
        password: '',
      },
      hidePassword: true,
      rules: {
        userName: {
          required: true,
          message: '账号不能为空',
          trigger: 'blur',
        },
        password: {
          required: true,
          message: '密码不能为空',
          trigger: 'blur',
        },
      },
    };
  },
  methods: {
    ...mapActions([
      'handleLogin',
      'getUserInfo',
    ]),
    handleSubmit() {
      this.handleLogin(this.form).then(() => {
        this.getUserInfo().then(() => {
          this.$router.push({
            name: 'home',
          });
        });
      }).catch(() => {
        this.$message({
          type: 'error',
          message: '用户名或密码不正确',
        });
      });
    },
    handleClickEye() {
      this.hidePassword = !this.hidePassword;
    },
  },
};
</script>

<style lang="scss" scoped>
.login {
  width: 100%;
  height: 100%;
  &-content {
    position: absolute;
    top: 40%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 400px;
  }
  &-title {
    font-size: 24px;
    font-weight: 600;
    margin-bottom: 20px;
  }
  .eye {
    height: 100%;
    display: flex;
    align-items: center;
  }
}
</style>
