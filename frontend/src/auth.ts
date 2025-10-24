import { ref, computed } from 'vue';
import axios from 'axios';

const usuarioLogado = ref<any>(null); // Usamos 'any' por simplicidade por enquanto

export function useAuth() {

    
    const carregarUsuarioLogado = async (id: number) => { 
        try {
            const response = await axios.get(`http://localhost:8080/api/funcionario/${id}`);
            usuarioLogado.value = response.data;
            console.log("Usuário logado como:", response.data.nomeCompleto, "| Perfil ID:", response.data.perfilId, "| Área ID:", response.data.areaId);
        } catch (error) {
            console.error("Falha ao carregar dados do usuário logado:", error);
            usuarioLogado.value = null;
        }
    };

    return {
        usuarioLogado: computed(() => usuarioLogado.value),
        carregarUsuarioLogado
    };
}