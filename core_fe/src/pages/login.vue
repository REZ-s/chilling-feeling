<template>
  <div class="d-flex align-center justify-center" style="height: 100vh">
    <v-sheet width="256px" class="mx-auto">
      <v-form fast-fail @submit.prevent="login">
        <v-text-field variant="outlined" v-model="username" label="Username"></v-text-field>
        <v-text-field variant="outlined" v-model="password" label="Password"></v-text-field>
        <a href="#" class="text-body-2 font-weight-regular">Forgot Password?</a>
      </v-form>

      <v-btn type="submit" color="primary" block class="mt-2">sign in</v-btn>

      <v-btn type="submit" color="green" block class="mt-2">sign up</v-btn>

      <v-btn style="width: 256px; height: 45px" type="submit" block class="mt-2 mx-1">
        <img src="/google_login_light_normal_web.png" style="width: 264px; height: 52px;">
      </v-btn>

      <v-btn style="width: 256px; height: 45px" type="submit" block class="mt-2 mx-1">
        <img src="/naver_login_official.png" style="width: 256px; height: 45px;">
      </v-btn>

      <v-btn style="width: 256px; height: 45px" type="submit" block class="mt-2 mx-1">
        <img src="/kakao_login_official.png" style="width: 256px; height: 45px;">
      </v-btn>

    </v-sheet>
  </div>
</template>

<script>
export default {
  name: "loginPage",

  data() {
    return {
      username: '',
      password: '',
    };
  },
  methods: {
    login() {
      // Your login logic here
    },
  },
}
</script>
