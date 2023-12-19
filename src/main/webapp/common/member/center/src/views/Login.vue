<template>
  <div>
    <div style="width: 500px; margin: 50px;">
      <el-input v-model="username" placeholder="用户名"></el-input>
      <el-input v-model="password" placeholder="密码"></el-input>
      <el-input v-model="verify" placeholder="验证码"></el-input>
      <img src="/loginVerifycode.do" alt="">
      <div>
        <el-button type="primary" size="medium" @click="loginFun">登录</el-button>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'

export default {
  data () {
    return {
      username: '',//用户名
      password: '',//密码
      verify: '',//验证码
    };
  },
  computed: {
    ...mapState(['onOffBtn'])
  },
  components: {
  },
  methods: {
    loginFun () {
      this.$axiosPack.post("/login.do?username=" + this.username + "&password=" + this.password + "&verifyCode=" + this.verify).then(res => {
        if (res) {
          this.$successFun('登录成功！')
          this.$router.push('/index')
          setTimeout(() => {
            location.reload()
          }, 500);
        }
      });
    },
  },
};
</script>

<style lang="scss">
</style>