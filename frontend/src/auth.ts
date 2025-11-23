import { ref, computed } from 'vue';
import axios from 'axios';
import router from '@/router';

const usuarioLogado = ref<any>(null);
const token = ref<string | null>(localStorage.getItem('token'));

if (token.value) {
  axios.defaults.headers.common['Authorization'] = `Bearer ${token.value}`;
}

export function useAuth() {

  // --- LOGIN ---
  const login = async (email: string, password: string) => {
    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        email: email,
        password: password
      });

      const { token: newToken, userId } = response.data;

      localStorage.setItem('token', newToken);
      token.value = newToken;

      axios.defaults.headers.common['Authorization'] = `Bearer ${newToken}`;

      // Carrega os dados completos do usuário
      const usuario = await carregarUsuarioLogado(userId);

      // === AQUI ESTÁ A MUDANÇA DE ROTA ===
      if (usuario.perfilId === 1) {
        // Diretor
        router.push({ name: 'LiderInicio' });
      } else if (usuario.perfilId === 2) {
        // Supervisor (Agora vai para o lugar certo!)
        router.push({ name: 'SupervisorInicio' });
      } else {
        // Colaborador
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

  // --- LOGOUT ---
  const logout = async () => {
    console.log("Deslogando usuário...");
    localStorage.removeItem('token');
    token.value = null;
    usuarioLogado.value = null;
    axios.defaults.headers.common['Authorization'] = null;

    await router.replace({ name: 'Login' });
  };

  // --- CARREGAR DADOS ---
  const carregarUsuarioLogado = async (id: number) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/funcionario/${id}`);
      usuarioLogado.value = response.data;
      console.log("Usuário logado carregado:", response.data.nomeCompleto);
      return response.data;
    } catch (error) {
      console.error("Falha ao carregar dados do usuário:", error);
      await logout();
      throw new Error("Sessão inválida. Faça login novamente.");
    }
  };

  // --- RESTAURAR SESSÃO (Ao dar F5) ---
  const carregarSessao = async () => {
    if (token.value && !usuarioLogado.value) {
      console.log("Token encontrado, tentando restaurar sessão...");
      try {
        // Usa o endpoint correto (/me) para não bugar o diretor
        const response = await axios.get('http://localhost:8080/api/auth/me');

        usuarioLogado.value = response.data;
        console.log("Sessão restaurada para:", usuarioLogado.value.nomeCompleto);

        // Se o usuário estiver na tela de login mas já tiver sessão, redireciona ele
        const currentPath = router.currentRoute.value.path;
        if (currentPath === '/login' || currentPath === '/') {
          if (usuarioLogado.value.perfilId === 1) router.push({ name: 'LiderInicio' });
          else if (usuarioLogado.value.perfilId === 2) router.push({ name: 'SupervisorInicio' });
          else router.push({ name: 'ColaboradorPerfil', params: { id: usuarioLogado.value.codigo } });
        }

      } catch (e) {
        console.log("Erro ao restaurar sessão, limpando.");
        await logout();
      }
    } else if (usuarioLogado.value) {
      // Caso raro onde o estado já existe mas a rota está errada
      // (Geralmente não precisa fazer nada aqui, o Vue Router cuida)
    } else if (!token.value) {
      // Se não tem token, força login
      if (router.currentRoute.value.name !== 'Login') {
        router.replace({ path: '/login' });
      }
    }
  };

  return {
    usuarioLogado: computed(() => usuarioLogado.value),
    token: computed(() => token.value),
    isLoggedIn: computed(() => !!token.value),
    carregarUsuarioLogado,
    login,
    logout,
    carregarSessao
  };
}
