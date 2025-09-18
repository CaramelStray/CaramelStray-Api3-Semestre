<template>
  <div class="container">
    <h1>Gerenciamento de Áreas</h1>
    <form @submit.prevent="createArea" class="form-card">
      <h2>Adicionar Nova Área</h2>
      <input v-model="newArea.nome" placeholder="Nome da Área" required />
      <button type="submit">Adicionar Área</button>
    </form>
    <hr>
    <div class="list-card">
      <h2>Lista de Áreas</h2>
      <ul v-if="areas.length">
        <li v-for="area in areas" :key="area.id">
          {{ area.nome }}
        </li>
      </ul>
      <p v-else>Nenhuma área cadastrada.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api/areas';
const areas = ref([]);
const newArea = ref({ nome: '' });

const fetchAreas = async () => {
  try {
    const response = await axios.get(API_URL);
    areas.value = response.data;
  } catch (error) {
    console.error('Erro ao buscar áreas:', error);
  }
};

const createArea = async () => {
  try {
    await axios.post(API_URL, newArea.value);
    newArea.value.nome = '';
    fetchAreas();
  } catch (error) {
    console.error('Erro ao criar área:', error);
  }
};

onMounted(() => {
  fetchAreas();
});
</script>

<style scoped>
.container { max-width: 800px; margin: 0 auto; padding: 20px; font-family: sans-serif; }
.form-card, .list-card { background: #f4f4f4; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
input, button { padding: 10px; margin-top: 5px; width: 100%; box-sizing: border-box; }
button { background-color: #28a745; color: white; border: none; cursor: pointer; }
ul { list-style: none; padding: 0; }
li { background: #fff; padding: 10px; border-bottom: 1px solid #ddd; }
</style>