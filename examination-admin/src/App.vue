<template>
  <div id="app">
    <router-view />
  </div>
</template>

<style lang="scss">
@mixin size {
  width: 100%;
  height: 100%;
}
html, body {
  @include size;
  padding: 0;
  margin: 0;
  overflow: hidden;
}
#app {
  @include size;
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  // text-align: center;
  color: #2c3e50;
}
</style>
