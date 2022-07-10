<template>
    <div class="nav">
      <header class="header">
        <el-row>
          <el-col :span="19" style="height: 100%">
            <el-menu class="el-menu-demo"
              mode="horizontal"
              text-color="#f2f2f2"
              active-text-color="#001933">
                <el-menu-item>导航选项</el-menu-item>
              <el-menu-item v-for="item in menus" v-bind:key="item.name">
                <router-link v-bind:to="item.path">{{ item.title }}</router-link>
              </el-menu-item>
            </el-menu>
          </el-col>
          <el-col :span="5" style="height: 100%">
            <el-dropdown  trigger="click" style="color: #00e5e6;height: 100%">
              <div class="avatar-wrapper">
                <i class="el-icon-user-solid" style="font-weight: 600;"/>
                欢迎您 {{ name }}
                <i class="el-icon-caret-bottom"/>
              </div>
              <el-dropdown-menu slot="dropdown" class="user-dropdown">
                <el-dropdown-item divided>
                  <span style="display:block;" @click="logout">注销登录</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-col>
        </el-row>
      </header>
    </div>
</template>

<script>
import routes from '../router/routes'
import store from '../store'

export default {
  data(){
    return{
      name: store.state.user.name
    }
  },
    computed: {
        menus() {
            const ms = routes.filter(item => item.meta && !item.meta.hideInMenu).map(item => ({
                title: item.meta.title,
                path: item.path,
                name: item.name,
                permissions: item.meta.permissions,
            }))
            // eslint-disable-next-line no-console
            console.log(ms, this.$store.state.permissions)
            return ms.filter(item => item.permissions.some(ite => this.$store.state.permissions.some(it => it === ite)));
        }
    },
    methods: {
        logout: function() {
            this.$store.commit('clearUserPermission')
            this.$router.push('/login')
        }
    }
}
</script>

<style scoped>
.header{
  background-image: url("../assets/head.png");
  background-color: #fff;
  border-bottom: 1px solid #DCDEE2;
}
.el-menu-demo{
  background-image: url("../assets/head.png");
  text-align: center;
  width:100%;
  height:10%;
  /*position:static;*/
  /*background-size :100% 100%;*/
}
.el-menu-demo:hover{
  color: #66b1ff;
}
</style>
