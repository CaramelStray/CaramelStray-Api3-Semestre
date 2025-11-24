<template>
  <div class="page-layout">
    <SideMenuSupervisor />

    <div class="main-panel">
      <header class="topbar">
        <div class="brand">
          <div class="brand__text">
            <div class="brand__title">{{ $route.meta.title || 'Sistema RH' }}</div>
            <div class="brand__subtitle">{{ $route.meta.subtitle || 'Supervisor' }}</div>
          </div>
        </div>
      </header>

      <div class="content-area">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script setup>
import SideMenuSupervisor from '../components/SideMenuSupervisor.vue';
</script>

<style scoped>
/* --- ESTRUTURA (LAYOUT) --- */
.page-layout {
  display: flex; /* OBRIGATÓRIO: Coloca Menu e Conteúdo lado a lado */
  min-height: 100vh;
  overflow: hidden; /* Impede que a página inteira role */
  background-color: var(--background);
}

.main-panel {
  flex: 1; /* Ocupa o resto da tela ao lado do menu */
  display: flex;
  flex-direction: column;
  height: 100vh;
  overflow: hidden; /* Impede rolagem aqui também */
  min-width: 0; 
}

.topbar {
  height: 70px;
  border-bottom: 1px solid var(--border);
  background: var(--card);
  display: flex;
  align-items: center;
  padding: 0 30px;
  flex-shrink: 0; 
}

.brand__title {
  font-size: 18px;
  font-weight: 700;
  color: var(--title);
}
.brand__subtitle {
  font-size: 13px;
  color: var(--subtitle);
}

.content-area {
  padding: 30px;
  flex: 1;
  background-color: var(--background); /* Garante que o fundo não seja preto */
  overflow-y: auto;
}

/* --- DEFINIÇÃO DE CORES (VARIÁVEIS GLOBAIS DESTE LAYOUT) --- */
/* Isso copia o padrão visual do Diretor para o Supervisor */
.page-layout {
  --background: #f8fbfc;  /* Fundo cinza claro */
  --card: #ffffff;        /* Fundo branco dos cards */
  --title: #274b57;       /* Cor dos títulos */
  --subtitle: #6e7f89;    /* Cor dos subtítulos */
  --text: #4a5568;        /* Texto comum */
  --border: #e3eeee;      /* Bordas */
  --blue: #2563eb;        /* Cor primária (Botões) */
  --shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);
}
</style>
