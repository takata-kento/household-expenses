<script setup lang="ts">
import { ref } from 'vue'
import { RouterLink, RouterView } from 'vue-router'

export type titleInfo = {title: string, icon: string}

const props = defineProps<{
  menuContents: Map<string, titleInfo>
}>()
const isExpanded = ref(false)
const toggleMenubar = () => {
  isExpanded.value = !isExpanded.value;
}
</script>

<template>
  <nav :class="['menubar', { 'expanded': isExpanded }]">
    <button @click="toggleMenubar" class="hamburger">
      <span class="material-symbols-outlined icon">menu</span>
    </button>
    <ul class="menu">
      <li v-for="[path, menuInfo] in props.menuContents">
        <RouterLink :to="path" v-if="isExpanded">
          <span class="material-symbols-outlined icon">{{ menuInfo.icon }}</span>
          <span class="text">{{ menuInfo.title }}</span>
        </RouterLink>
        <RouterLink :to="path" v-else>
          <span class="material-symbols-outlined icon">{{ menuInfo.icon }}</span>
        </RouterLink>
      </li>
    </ul>
  </nav>
</template>

<style scoped>
nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

/* webブラウザ用 */
@media (min-width: 431px) {
  /* サイドバー */
  .menubar {
    position: fixed; /* 通常通り表示されるが動かない */
    top: 0;
    left: 0;
    height: 100vh; /* vh = ビューポート(現在見えている画面の長さ)の1/100 */
    width: 60px;
    background-color: #2c3e50;
    transition: width 0.7s ease;
    overflow: hidden;
    z-index: 100;
  }

  .menubar.expanded {
    width: 250px;
  }
}

/* スマホ用 */
@media (max-width: 430px) {
  /* サイドバー */
  .menubar {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    height: 60px;
    width: 100vh;
    background-color: #2c3e50;
    transition: height 0.7s ease;
    overflow: hidden;
    z-index: 100;
  }

  .menubar.expanded {
    height: 350px;
  }
}

/* ハンバーガーボタン */
.hamburger {
  width: 100%;
  height: 40px;
  padding: 20px;
  background: none;
  border: none;
  cursor: pointer;
  color: white;
  font-size: 25px;
  text-align: left;
}

.hamburger .icon {
  display: inline-block;
  width: 20px;
}

/* サイドバーに配置するメニュー要素 */
.menu {
  list-style: none;
  padding: 0;
  margin: 0;
}

.menu li a {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  color: white;
  text-decoration: none;
  white-space: nowrap;
  transition: background-color 0.2s;
}

.menu .icon {
  font-size: 27px;
  flex-shrink: 0;
}

.menu .text {
  font-size: 17px;
  padding: 0 0 0 10px;
}
</style>
