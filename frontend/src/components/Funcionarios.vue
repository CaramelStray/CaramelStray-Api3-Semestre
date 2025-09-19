<template>
  <div class="container">
    <h1>Gerenciamento de Funcionários</h1>
    
    <form @submit.prevent="createFuncionario" class="form-card">
      <h2>Adicionar Novo Funcionário</h2>
      <input v-model="newFuncionario.nome" placeholder="Nome" required />
      <input v-model="newFuncionario.email" placeholder="E-mail" required />
      <input v-model="newFuncionario.cpf" placeholder="CPF" required />
      <input v-model="newFuncionario.telefone" placeholder="Telefone" />

      <select v-model="selectedAreaId" required>
        <option disabled value="">Selecione uma Área</option>
        <option v-for="area in areas" :key="area.id" :value="area.id">
          {{ area.nome }}
        </option>
      </select>

      <select v-model="selectedPerfilId" required>
        <option disabled value="">Selecione um Perfil</option>
        <option v-for="perfil in perfis" :key="perfil.id" :value="perfil.id">
          {{ perfil.nome }}
        </option>
      </select>
      
      <button type="submit">Adicionar Funcionário</button>
    </form>

    <hr>

    <div class="list-card">
      <h2>Lista de Funcionários</h2>
      <ul v-if="funcionarios.length">
        <li v-for="funcionario in funcionarios" :key="funcionario.id">
          <strong>{{ funcionario.nome }}</strong> - {{ funcionario.email }}
          <br>
          <span class="details">Área: {{ funcionario.area ? funcionario.area.nome : 'N/A' }} | Perfil: {{ funcionario.perfil ? funcionario.perfil.nome : 'N/A' }}</span>
          <button @click="deleteFuncionario(funcionario.id)" class="delete-btn">Excluir</button>
        </li>
      </ul>
      <p v-else>Nenhum funcionário cadastrado.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const funcionarios = ref([]);
const areas = ref([]);
const perfis = ref([]);

const newFuncionario = ref({
  nome: '',
  email: '',
  cpf: '',
  telefone: '',
});

const selectedAreaId = ref('');
const selectedPerfilId = ref('');

const fetchAllData = async () => {
  try {
    const [
      funcionariosResponse, 
      areasResponse, 
      perfisResponse
    ] = await Promise.all([
      axios.get(`${API_URL}/funcionario`),
      axios.get(`${API_URL}/area`),
      axios.get(`${API_URL}/perfil`)
    ]);

    funcionarios.value = funcionariosResponse.data;
    areas.value = areasResponse.data;
    perfis.value = perfisResponse.data;

  } catch (error) {
    console.error('Erro ao buscar dados:', error);
  }
};

const createFuncionario = async () => {
  try {
    const areaSelecionada = areas.value.find(a => a.id === selectedAreaId.value);
    const perfilSelecionado = perfis.value.find(p => p.id === selectedPerfilId.value);
    
    const funcionarioComRelacoes = {
      ...newFuncionario.value,
      area: areaSelecionada,
      perfil: perfilSelecionado
    };

    await axios.post(`${API_URL}/funcionarios`, funcionarioComRelacoes);
    
    newFuncionario.value = { nome: '', email: '', cpf: '', telefone: '' };
    selectedAreaId.value = '';
    selectedPerfilId.value = '';
    
    fetchAllData();
    
  } catch (error) {
    console.error('Erro ao criar funcionário:', error);
  }
};

const deleteFuncionario = async (id) => {
  try {
    await axios.delete(`${API_URL}/funcionarios/${id}`);
    fetchAllData();
  } catch (error) {
    console.error('Erro ao excluir funcionário:', error);
  }
};

onMounted(() => {
  fetchAllData();
});
</script>

<style scoped>
.container { 
    font-family: Arial, sans-serif; 
    max-width: 800px; 
    margin: 0 auto; 
    padding: 20px;
    color: #333; /* Adicionado: Cor de texto escura para garantir a legibilidade */
}
.form-card, .list-card { 
    background: #f4f4f4; 
    padding: 20px; 
    border-radius: 8px; 
    margin-bottom: 20px; 
    color: #000; /* Pode ser útil para garantir texto preto em fundos cinza */
}
.container { font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px; }
.form-card, .list-card { background: #f4f4f4; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
input, select { display: block; width: 100%; padding: 8px; margin-bottom: 10px; }
button { padding: 10px 15px; background-color: #007bff; color: white; border: none; cursor: pointer; }
.delete-btn { background-color: #dc3545; margin-left: 10px; }
ul { list-style: none; padding: 0; }
li { background: #fff; padding: 10px; border-bottom: 1px solid #ddd; display: flex; flex-direction: column; align-items: flex-start; }
.details { font-size: 0.9em; color: #666; margin-top: 5px;}
li button { align-self: flex-end; margin-top: -30px; }
</style>