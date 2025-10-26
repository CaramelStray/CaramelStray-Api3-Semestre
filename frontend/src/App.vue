<template>
  <v-app>
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
// Não precisa mais importar layouts aqui
</script>

<style>
/* Estilos Globais e Variáveis CSS */
body {
  margin: 0;
  font-family: ui-sans-serif, system-ui, -apple-system, "Segoe UI", Roboto, Arial;
  background-color: var(--bg);
  color: var(--subtitle);
  overflow: hidden;
}
:root {
  --bg: #f3f9f9; --card: #ffffff; --border: #e3eeee;
  --shadow: 0 2px 12px rgba(0, 0, 0, 0.06); --title: #274b57;
  --subtitle: #6e7f89; --primary: #5cc2c1; --primary-600: #3fb2b2;
  --muted: #eaf5f5; --blue: #2563eb;
}
</style>
