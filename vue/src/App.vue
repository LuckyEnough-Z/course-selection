<template>
  <div id="app" class="appa" style="margin: 0;padding: 0;width: 100%;height: 100%;position: absolute;">
    <Nav v-if="navIsShow"/>
    <Login v-else/>
    <router-view/>
  </div>
</template>

<script>
  import Nav from './components/Nav'
  import Login from './components/Login'

  export default {
    name: 'app',
    components: {
      Nav,
      Login
    },
    computed: {
      navIsShow() {
        return this.$store.state.isLogin
      }
    }
  }
</script>

<style>
.appa{
  background-image: url("./assets/bg.jpg");

}
  body {
    margin: 0;
    padding: 0;
  }
</style>
