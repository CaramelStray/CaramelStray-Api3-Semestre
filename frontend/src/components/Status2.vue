<template>
  <div>
    <main class="container">
      <section class="summary">
        <div class="kpi">
          <div class="kpi__row">
            <div class="kpi__title">Pendentes</div>
            <div class="kpi__icon icon--blue"><svg viewBox="0 0 24 24" class="kpi__svg"><circle cx="12" cy="12" r="9"/><path d="M12 7v5l3 2"/></svg></div>
          </div>
          <div class="kpi__value">{{ totais.pendentes }}</div>
        </div>
        <div class="kpi">
          <div class="kpi__row">
            <div class="kpi__title">Concluídas</div>
            <div class="kpi__icon icon--green"><svg viewBox="0 0 24 24" class="kpi__svg"><circle cx="12" cy="12" r="9"/><path d="m8.5 12 2.5 2.5 4.5-5"/></svg></div>
          </div>
          <div class="kpi__value">{{ totais.concluidas }}</div>
        </div>
        <div class="kpi">
          <div class="kpi__row">
            <div class="kpi__title">Atrasadas</div>
            <div class="kpi__icon icon--red"><svg viewBox="0 0 24 24" class="kpi__svg"><circle cx="12" cy="12" r="9"/><path d="M12 8v5"/><path d="M12 16h.01"/></svg></div>
          </div>
          <div class="kpi__value">{{ totais.atrasadas }}</div>
        </div>
      </section>

      <div class="avaliacoes-container">
        <h2 class="list__title">Avaliações Atribuídas</h2>
        <p v-if="avaliacoes.length === 0" class="text-center text-slate-500 py-5">
            Nenhuma avaliação encontrada.
        </p>
        <article v-for="a in avaliacoes" :key="a.id" class="card item">
          <header class="item__header">
            <div class="item__title">
              {{ a.titulo }}
              <span :class="['badge', statusClass(a.status)]">
                {{ labelStatus(a.status) }}
              </span>
            </div>
            <button
                v-if="a.status === 'PENDENTE' || a.status === 'AGUARDANDO_REVISAO'"
                class="btn btn--primary btn--start"
                @click="iniciar(a)"
            >
              {{ a.status === 'PENDENTE' ? 'Iniciar' : 'Continuar' }}
            </button>
            <button
                v-else
                class="btn" disabled
             >
               Ver Resultado
             </button>
          </header>
          <div class="item__meta"> Prazo: {{ formatarData(a.prazo) }} <span class="dot">•</span> Perguntas: {{ a.perguntas }} </div>
          <div class="item__splitter"></div>
          <div class="item__stats">
            <div class="stat">
              <div class="stat__icon"><svg viewBox="0 0 24 24" class="stat__svg"><path d="M5 7h14M5 12h14M5 17h10"/></svg></div>
              <div class="stat__text"><div class="stat__title">Perguntas</div><div class="stat__value">{{ a.perguntas }}</div></div>
            </div>
          </div>
        </article>
      </div>
    </main>
  </div>
</template>

<script setup>

import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useAuth } from '../auth'



const router = useRouter()
const { usuarioLogado } = useAuth();
const avaliacoes = ref([]); // <<< Começa vazio
const loading = ref(true); // <<< Adiciona estado de loading
const error = ref(null);

const totais = computed(() => {
    const pendentes = avaliacoes.value.filter(a => a.status === 'PENDENTE' || a.status === 'AGUARDANDO_REVISAO').length;
    // Considera APROVADO, REPROVADO, etc. como concluídas
    const concluidas = avaliacoes.value.filter(a => a.status !== 'PENDENTE' && a.status !== 'AGUARDANDO_REVISAO').length;
    // Lógica de atrasadas precisaria da data de prazo, que não vem por padrão
    const atrasadas = 0; // <<< Simplificado por enquanto

    return { pendentes, concluidas, atrasadas };
});

function labelStatus(statusBackend) {
    switch (statusBackend) {
        case 'PENDENTE': return 'Pendente';
        case 'AGUARDANDO_REVISAO': return 'Enviada (Aguardando Revisão)';
        case 'APROVADO': return 'Concluída (Aprovado)';
        case 'REPROVADO': return 'Concluída (Reprovado)';
        // Adicione outros status se houver
        default: return statusBackend || 'Status Desconhecido';
    }
}

function statusClass(statusBackend) {
    switch (statusBackend) {
        case 'PENDENTE': return 'badge--blue';
        case 'AGUARDANDO_REVISAO': return 'badge--yellow'; // Exemplo: Adicionar estilo para amarelo
        case 'APROVADO': return 'badge--green';
        case 'REPROVADO': return 'badge--red';
        default: return 'badge--muted';
    }
}

function formatarData(iso){
    if (!iso) return '-';
    // Se a data já vier formatada ou for apenas Date (sem hora)
    if (typeof iso === 'string' && iso.length === 10) {
       try {
         const [year, month, day] = iso.split('-');
         return `${day}/${month}/${year}`;
       } catch (e) {
         return iso; // Retorna a string original se falhar
       }
    }
    // Se for um objeto Date ou timestamp completo
    try {
      const d = new Date(iso);
      return d.toLocaleDateString('pt-BR');
    } catch (e) {
      return iso; // Retorna original se falhar
    }
}

function iniciar(avaliacaoInstancia) {
    // Redireciona para a tela de responder, passando o ID da INSTÂNCIA
    console.log(`Iniciando avaliação com ID da instância: ${avaliacaoInstancia.id}`);
    router.push({
        name: 'ColaboradorResponderAvaliacao', // <<< Nome da rota para responder
        params: { avaliacaoId: avaliacaoInstancia.id } // <<< Passa o ID da instância como parâmetro
    });
}

onMounted(async () => {
    loading.value = true;
    error.value = null;
    avaliacoes.value = []; // Limpa dados antigos

    if (!usuarioLogado.value || !usuarioLogado.value.codigo) {
        error.value = "Não foi possível identificar o usuário logado.";
        loading.value = false;
        return;
    }

    const userId = usuarioLogado.value.codigo;
    console.log(`Buscando avaliações para o usuário ID: ${userId}`);

    try {
        // === CHAMA O ENDPOINT DO BACKEND ===
        // Usamos /api/funcionario/{id}/avaliacoes (precisa criar este endpoint)
        // OU buscamos todas as instâncias e filtramos no frontend (menos eficiente)
        // OU usamos o endpoint /pendentes que já existe (mas só traz pendentes)

        // Vamos usar o /pendentes por enquanto e adicionar os outros status manualmente se necessário
        const response = await axios.get(`http://localhost:8080/api/avaliacoes/pendentes/${userId}`);
        console.log("Dados recebidos de /pendentes:", response.data);

        // Mapeia a resposta (AvaliacaoFuncionarioResponseDTO) para o formato do template
        avaliacoes.value = response.data.map(dto => ({
            id: dto.codigo, // ID da instância
            titulo: dto.avaliacaoTitulo,
            status: dto.resultadoStatus,
            prazo: dto.dataPrazo || null, // <<< Pega o prazo se existir no DTO (precisa adicionar no backend DTO?)
            perguntas: '?', // <<< Contagem de perguntas não vem neste DTO. Precisaria buscar separadamente ou adicionar ao DTO.
            // Adicione outros campos se necessário
        }));

        // Exemplo: Se precisar buscar avaliações concluídas separadamente (requer outro endpoint)
        // try {
        //   const concluidasResponse = await axios.get(`http://localhost:8080/api/funcionario/${userId}/avaliacoes/concluidas`);
        //   const concluidasMapeadas = concluidasResponse.data.map(dto => ({...}));
        //   avaliacoes.value = [...avaliacoes.value, ...concluidasMapeadas];
        // } catch (concluidasErr) {
        //   console.warn("Não foi possível buscar avaliações concluídas:", concluidasErr);
        // }

        console.log("Avaliações mapeadas:", avaliacoes.value);

    } catch (err) {
        console.error("Erro ao buscar avaliações:", err);
        if (axios.isAxiosError(err) && err.response?.status === 404) {
            error.value = "Nenhuma avaliação pendente encontrada."; // Mensagem mais amigável
            avaliacoes.value = []; // Garante que a lista esteja vazia
        } else {
            error.value = "Não foi possível carregar suas avaliações.";
        }
    } finally {
        loading.value = false;
    }
});

</script>

<style scoped>
/* Estilos mantidos para o design interno (KPIs e Cards) */
.container{
  max-width:1100px;
  margin: 20px auto 0 auto;
  padding:16px;
}
.btn{height:36px;padding:0 12px;border:1px solid #e6effa;background:#fff;border-radius:10px;cursor:pointer;color:#1f2d3d;display:flex;align-items:center;gap:8px;}
.btn--primary{background:#3a6ce1;color:#fff;border-color:#3a6ce1;}
.btn--primary:hover{background:#315fc6;}
.btn--start{min-width:110px; justify-content: center;}
.summary{display:grid;grid-template-columns:repeat(auto-fit,minmax(220px,1fr));gap:16px;margin-top:20px;}
.kpi{background:#fff;border:1px solid #e6effa;border-radius:16px;padding:16px;box-shadow:0 1px 2px rgba(20,40,80,.04);}
.kpi__row{display:flex;align-items:center;justify-content:space-between;}
.kpi__title{color:#3a4b63;font-weight:600;}
.kpi__icon{width:36px;height:36px;border-radius:10px;display:grid;place-items:center;}
.kpi__svg{width:20px;height:20px;stroke:currentColor;fill:none;stroke-width:2;}
.icon--blue{background:#eaf2ff;color:#3a6ce1;}
.icon--green{background:#e7fbef;color:#11a966;}
.icon--red{background:#ffe8ec;color:#d33b53;}
.kpi__value{font-size:26px;font-weight:700;margin-top:8px; color: #142032;}
.avaliacoes-container{background:#ffffff;border:1px solid #e3edfb;border-radius:20px;padding:20px;margin-top:24px;box-shadow:0 2px 8px rgba(18,39,76,.05);}
.list__title{font-size:16px;font-weight:600;margin:0 0 14px;color:#3a4b63;}
.card.item{background:#f3f8ff;border:1px solid #cfe0fb;border-radius:14px;padding:14px 16px;margin-bottom:14px;box-shadow:0 1px 0 rgba(38,86,170,.05);}
.item__header{display:flex;justify-content:space-between;align-items:center;gap:12px;}
.item__title{font-weight:600;color:#27436b;display:flex;align-items:center;gap:10px;}
.badge{display:inline-flex;align-items:center;gap:6px;font-size:12px;padding:4px 10px;border-radius:999px;border:1px solid transparent;font-weight:600;}
.badge--blue{background:#eaf2ff;color:#3a6ce1;}
.badge--green{background:#e7fbef;color:#11a966;}
.badge--muted{background:#eef2f6;color:#64758b;}
.badge--red{background:#feecef;color:#d33b53;}
.item__meta{margin-top:4px;font-size:13px;color:#6b7a90;}
.item__meta .dot{margin:0 8px;opacity:.5;}
.item__splitter{height:1px;background:#e1ebfb;margin:12px 0;}
.item__stats{display:grid;grid-template-columns:1fr;gap:10px;}
.stat{display:flex;align-items:center;gap:10px;}
.stat__icon{width:28px;height:28px;border-radius:8px;display:grid;place-items:center;background:#eef4ff;color:#5477d8;}
.stat__svg{width:18px;height:18px;stroke:currentColor;fill:none;stroke-width:2;}
.stat__title{font-size:12px;color:#6b7a90;}
.stat__value{font-weight:700;color:#1f2d3d;}
</style>
