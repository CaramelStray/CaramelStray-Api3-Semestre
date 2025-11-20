<template>
  <aside class="sidebar">
    <div class="sidebar__header">
      <div class="sidebar__logo">
        <svg viewBox="0 0 24 24" width="20" height="20"><rect x="3" y="3" width="18" height="18" rx="2" fill="none" stroke="currentColor" stroke-width="2"/><path d="M9 3v18M15 3v18M3 9h18M3 15h18" stroke="currentColor" stroke-width="2"/></svg>
      </div>
      <div class="sidebar__text">
        <div class="sidebar__title">Gestor de Equipes</div>
        <div class="sidebar__subtitle">Liderança</div>
      </div>
    </div>

    <div class="sidebar__user" v-if="usuarioLogado">
      <div class="user-avatar">{{ getInitials(usuarioLogado.nomeCompleto) }}</div>
      <div class="user-info">
        <div class="user-name">{{ usuarioLogado.nomeCompleto }}</div>
        <div class="user-email">{{ usuarioLogado.email }}</div>
      </div>
    </div>

    <nav class="sidebar__nav">
      <router-link :to="{ name: 'LiderInicio' }" class="nav-item">
        <svg viewBox="0 0 24 24" width="20" height="20"><path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z" fill="none" stroke="currentColor" stroke-width="2"/><path d="M9 22V12h6v10" fill="none" stroke="currentColor" stroke-width="2"/></svg>
        <span>Início</span>
      </router-link>

      <router-link :to="{ name: 'LiderFuncionarios' }" class="nav-item">
        <svg viewBox="0 0 24 24" width="20" height="20"><circle cx="12" cy="8" r="3" fill="none" stroke="currentColor" stroke-width="2"/><path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="none" stroke="currentColor" stroke-width="2"/></svg>
        <span>Colaboradores</span>
      </router-link>

      <router-link :to="{ name: 'LiderAvaliacoes' }" class="nav-item">
        <svg viewBox="0 0 24 24" width="20" height="20"><path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" fill="none" stroke="currentColor" stroke-width="2"/></svg>
        <span>Avaliações</span>
      </router-link>

    </nav>
    <div class="sidebar__footer">
      <button class="nav-item" @click="logout">
        <svg viewBox="0 0 24 24" width="20" height="20"><path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4m7 14l5-5-5-5m5 5H9" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round"/></svg>
        <span>Sair</span>
      </button>
    </div>
  </aside>
</template>

<script setup>
import { useAuth } from '../auth';

const { usuarioLogado, logout } = useAuth();

const getInitials = (fullName) => {
  if (!fullName) return '?';
  const names = fullName.trim().split(' ');
  const initials = names.length > 1 
    ? `${names[0][0]}${names[names.length - 1][0]}`
    : names[0].substring(0, 2);
  return initials.toUpperCase();
};
</script>

<style scoped>
/* Seus estilos originais */
.sidebar { display: flex; flex-direction: column; width: 240px; flex-shrink: 0; height: 100vh; position: sticky; top: 0; background: var(--card, #ffffff); border-right: 1px solid var(--border, #e3eeee); padding: 20px 0; box-sizing: border-box; z-index: 20; }
.sidebar__header { padding: 0 20px 20px; border-bottom: 1px solid var(--border, #e3eeee); margin-bottom: 16px; display: flex; align-items: center; gap: 12px; flex-shrink: 0; }
.sidebar__logo { width: 40px; height: 40px; background: var(--blue, #2563eb); border-radius: 8px; display: flex; align-items: center; justify-content: center; color: #fff; flex-shrink: 0; }
.sidebar__text { display: flex; flex-direction: column; flex-grow: 1; min-width: 0; }
.sidebar__title { font-size: 15px; font-weight: 700; color: var(--title, #274b57); margin-bottom: 2px; }
.sidebar__subtitle { font-size: 12px; color: var(--subtitle, #6e7f89); }
.sidebar__user { padding: 12px 20px; display: flex; align-items: center; gap: 12px; margin-bottom: 8px; flex-shrink: 0; }
.user-avatar { width: 40px; height: 40px; border-radius: 50%; background: var(--blue, #2563eb); color: #fff; display: flex; align-items: center; justify-content: center; font-weight: 600; font-size: 16px; flex-shrink: 0; }
.user-info { flex: 1; min-width: 0; }
.user-name { font-size: 14px; font-weight: 600; color: var(--title, #274b57); }
.user-email { font-size: 12px; color: var(--subtitle, #6e7f89); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.sidebar__nav { flex: 1; padding: 8px 12px; overflow-y: auto; }
.nav-item { display: flex; align-items: center; gap: 12px; padding: 10px 12px; border-radius: 8px; color: var(--subtitle, #6e7f89); text-decoration: none; transition: all 0.2s; margin-bottom: 4px; cursor: pointer; border: none; background: transparent; width: 100%; font-size: 14px; font-family: inherit; box-sizing: border-box; }
.nav-item:hover { background: var(--muted, #eaf5f5); color: var(--title, #274b57); }
.nav-item.active { background: var(--blue, #2563eb); color: #fff; }
.nav-item.active:hover { background: var(--blue, #2563eb); color: #fff; }
.sidebar__footer { padding: 12px 12px 0; border-top: 1px solid var(--border, #e3eeee); margin-top: auto; flex-shrink: 0; }
.sidebar__footer .nav-item { margin-bottom: 0; }
</style>