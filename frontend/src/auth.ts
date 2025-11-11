import { ref, computed } from 'vue';
import axios from 'axios';
import router from '@/router'; 

const usuarioLogado = ref<any>(null);
const token = ref<string | null>(localStorage.getItem('token')); 

if (token.value) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token.value}`;
}

export function useAuth() {

    // (método login permanece igual)
    const login = async (email: string, password: string) => {
        try {
            const response = await axios.post('http://localhost:8080/api/auth/login', {
                email: email,
                password: password 
            });

            const { token: newToken, userId, nomeCompleto } = response.data;

            localStorage.setItem('token', newToken);
            token.value = newToken;
            
            axios.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;

            // Agora, em vez de carregar, nós SETAMOS o usuário
            // (A resposta do /me é um FuncionarioResponseDTO, vamos simular isso)
            const usuario = await carregarUsuarioLogado(userId); // Chama o /api/funcionario/{id}
            
            if (usuario.perfilId === 1 || usuario.perfilId === 2) {
                router.push({ name: 'LiderInicio' });
            } else {
                router.push({ name: 'ColaboradorPerfil', params: { id: usuario.codigo } });
            }

        } catch (error) {
            console.error("Erro no login:", error);
            localStorage.removeItem('token');
            token.value = null;
            axios.defaults.headers.common['Authorization'] = null;
            
            if (axios.isAxiosError(error) && (error.response?.status === 401 || error.response?.status === 404)) {
                 throw new Error("Email ou senha inválidos.");
            } else {
                 throw new Error("Erro de conexão. Tente novamente.");
            }
        }
    };

    // (método logout permanece igual)
    const logout = async () => {
        console.log("Deslogando usuário...");
        localStorage.removeItem('token');
        token.value = null;
        usuarioLogado.value = null;
        axios.defaults.headers.common['Authorization'] = null;
        
        await router.replace({ name: 'Login' }); 
    };

   
    
    const carregarUsuarioLogado = async (id: number) => { 
        try {
            const response = await axios.get(`http://localhost:8080/api/funcionario/${id}`);
            usuarioLogado.value = response.data; // Salva no estado global
            console.log("Usuário logado carregado:", response.data.nomeCompleto);
            return response.data; 
        } catch (error) {
            console.error("Falha ao carregar dados do usuário:", error);
            await logout(); // Desloga se não conseguir buscar
            throw new Error("Sessão inválida. Faça login novamente.");
        }
    };

    
    const carregarSessao = async () => {
        if (token.value && !usuarioLogado.value) {
            console.log("Token encontrado, tentando restaurar sessão com /api/auth/me");
            try {
                // 1. Chama o novo endpoint /me
                const response = await axios.get('http://localhost:8080/api/auth/me');
                
                // 2. Salva o usuário no estado global
                usuarioLogado.value = response.data;
                console.log("Sessão restaurada para:", usuarioLogado.value.nomeCompleto);

            } catch (e) {
                console.log("Token antigo inválido, limpando.");
                await logout(); // Se o token falhar (expirado/inválido), força o logout
            }
        }
    };

    return {
        usuarioLogado: computed(() => usuarioLogado.value),
        token: computed(() => token.value),
        isLoggedIn: computed(() => !!token.value),
        carregarUsuarioLogado, // (usado pelo login)
        login,
        logout,
        carregarSessao // (usado pelo main.ts)
    };
}