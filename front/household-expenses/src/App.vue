<script setup lang="ts">
import MenuBar from './components/MenuBar.vue';
import type { titleInfo } from './components/MenuBar.vue'

const menuContents: Map<string, titleInfo> = new Map([
  ["/", {title: "Home", icon: "home"}],
  ["/calendar", {title: "カレンダー", icon: "calendar_month"}],
  ["/account", {title: "貯金管理", icon: "account_balance_wallet"}],
  ["/settings", {title: "設定", icon: "settings"}],
  ["/logout", {title: "ログアウト", icon: "logout"}],
])
const msg:string = "HelloWorld"
</script>

<template>
  <div class="main">
    <!-- サイドバー -->
    <MenuBar :menu-contents="menuContents" />
  </div>
</template>

<style scoped>
</style>
