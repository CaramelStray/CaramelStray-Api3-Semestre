<template>
  <div class="container">
    <h1>Gerenciamento de Perfis</h1>
    <form @submit.prevent="createPerfil" class="form-card">
      <h2>Adicionar Novo Perfil</h2>
      <input v-model="newPerfil.nome" placeholder="Nome do Perfil" required />
      <button type="submit">Adicionar Perfil</button>
    </form>
    <hr>
    <div class="list-card">
      <h2>Lista de Perfis</h2>
      <ul v-if="perfis.length">
        <li v-for="perfil in perfis" :key="perfil.id">
          {{ perfil.nome }}
        </li>
      </ul>
      <p v-else>Nenhum perfil cadastrado.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/perfis';
const perfis = ref([]);
const newPerfil = ref({ nome: '' });

const fetchPerfis = async () => {
  try {
    const response = await axios.get(API_URL);
    perfis.value = response.data;
  } catch (error) {
    console.error('Erro ao buscar perfis:', error);
  }
};

const createPerfil = async () => {
  try {
    await axios.post(API_URL, newPerfil.value);
    newPerfil.value.nome = '';
    fetchPerfis();
  } catch (error) {
    console.error('Erro ao criar perfil:', error);
  }
};

onMounted(() => {
  fetchPerfis();
});
</script>

<style scoped>
.container { max-width: 800px; margin: 0 auto; padding: 20px; font-family: sans-serif; }
.form-card, .list-card { background: #f4f4f4; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
input, button { padding: 10px; margin-top: 5px; width: 100%; box-sizing: border-box; }
button { background-color: #007bff; color: white; border: none; cursor: pointer; }
ul { list-style: none; padding: 0; }
li { background: #fff; padding: 10px; border-bottom: 1px solid #ddd; }
</style>