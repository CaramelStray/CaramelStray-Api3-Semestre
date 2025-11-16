<template>
  <div class="dashboard-container">
    <!-- Loading State -->
    <div v-if="loading" class="loading-state">
      <div class="spinner"></div>
      <p>Carregando dados do dashboard...</p>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="error-state">
      <svg viewBox="0 0 24 24" width="48" height="48">
        <circle cx="12" cy="12" r="10" fill="none" stroke="#ef4444" stroke-width="2"/>
        <path d="M12 8v4M12 16h.01" stroke="#ef4444" stroke-width="2" stroke-linecap="round"/>
      </svg>
      <p>{{ error }}</p>
      <button @click="fetchDashboardData" class="retry-button">Tentar novamente</button>
    </div>

    <!-- Dashboard Content -->
    <div v-else>
      <!-- Header -->
      <div class="dashboard-header">
        <h1 class="page-title">Painel de Controle</h1>
        <p class="page-subtitle">Acompanhe as principais métricas e indicadores da empresa</p>
      </div>

      <!-- Metrics Cards -->
      <div class="metrics-grid">
        <div class="metric-card">
          <div class="metric-card__header">
            <div class="metric-card__label">Total de Colaboradores</div>
            <div class="metric-card__icon">
              <svg viewBox="0 0 24 24" width="20" height="20">
                <circle cx="12" cy="8" r="3" fill="none" stroke="currentColor" stroke-width="2"/>
                <path d="M4 20c0-4 4-6 8-6s8 2 8 6" fill="none" stroke="currentColor" stroke-width="2"/>
              </svg>
            </div>
          </div>
          <div class="metric-card__value">{{ metrics.totalColaboradores }}</div>
          <div class="metric-card__change positive">Total de colaboradores cadastrados</div>
        </div>

        <div class="metric-card">
          <div class="metric-card__header">
            <div class="metric-card__label">Avaliações Concluídas</div>
            <div class="metric-card__icon">
              <svg viewBox="0 0 24 24" width="20" height="20">
                <circle cx="12" cy="12" r="10" fill="none" stroke="currentColor" stroke-width="2"/>
                <path d="M9 12l2 2 4-4" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
              </svg>
            </div>
          </div>
          <div class="metric-card__value">{{ metrics.avaliacoesConcluidas }}</div>
          <div class="metric-card__change positive">Avaliações concluídas este mês</div>
        </div>

        <div class="metric-card">
          <div class="metric-card__header">
            <div class="metric-card__label">Meta Mensal</div>
            <div class="metric-card__icon">
              <svg viewBox="0 0 24 24" width="20" height="20">
                <path d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" fill="none"/>
              </svg>
            </div>
          </div>
          <div class="metric-card__value">{{ metrics.mediaMensal }}%</div>
          <div class="metric-card__change positive">Percentual de aprovação mensal</div>
        </div>

        <div class="metric-card">
          <div class="metric-card__header">
            <div class="metric-card__label">Pendências</div>
            <div class="metric-card__icon">
              <svg viewBox="0 0 24 24" width="20" height="20">
                <rect x="3" y="3" width="18" height="18" rx="2" fill="none" stroke="currentColor" stroke-width="2"/>
                <path d="M9 11h6M9 15h6" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              </svg>
            </div>
          </div>
          <div class="metric-card__value">{{ metrics.pendencias }}</div>
          <div class="metric-card__change" :class="metrics.pendencias > 0 ? 'negative' : 'positive'">
            {{ metrics.pendencias > 0 ? 'Avaliações pendentes' : 'Nenhuma pendência' }}
          </div>
        </div>
      </div>

      <!-- Charts Section -->
      <div class="charts-grid">
        <!-- Monthly Evolution Chart -->
        <div class="chart-card chart-card--large">
          <div class="chart-card__header">
            <h3 class="chart-card__title">Evolução Mensal</h3>
            <p class="chart-card__subtitle">Colaboradores contratados por mês</p>
          </div>
          <div class="chart-card__body">
            <canvas ref="lineChart"></canvas>
          </div>
        </div>

        <!-- Top 5 Competencies -->
        <div class="chart-card chart-card--large">
          <div class="chart-card__header">
            <h3 class="chart-card__title">Top 5 Competências Mais Avaliadas</h3>
            <p class="chart-card__subtitle">Competências com mais avaliações realizadas</p>
          </div>
          <div class="chart-card__body">
            <canvas ref="barChart"></canvas>
          </div>
        </div>

        <!-- Competency Distribution -->
        <div class="chart-card">
          <div class="chart-card__header">
            <h3 class="chart-card__title">Distribuição por Competência</h3>
            <p class="chart-card__subtitle">Número de colaboradores por competência</p>
          </div>
          <div class="chart-card__body">
            <canvas ref="competencyBarChart"></canvas>
          </div>
        </div>

        <!-- Area Distribution -->
        <div class="chart-card">
          <div class="chart-card__header">
            <h3 class="chart-card__title">Distribuição por Área de Atuação</h3>
            <p class="chart-card__subtitle">Colaboradores em cada departamento</p>
          </div>
          <div class="chart-card__body">
            <canvas ref="pieChart"></canvas>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import Chart from 'chart.js/auto';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

const metrics = ref({
  totalColaboradores: 0,
  avaliacoesConcluidas: 0,
  mediaMensal: 0,
  pendencias: 0
});

const evolucaoMensal = ref([]);
const loading = ref(true);
const error = ref(null);

const lineChart = ref(null);
const barChart = ref(null);
const competencyBarChart = ref(null);
const pieChart = ref(null);

// Função para buscar dados do dashboard
const fetchDashboardData = async () => {
  try {
    loading.value = true;
    error.value = null;

    // Buscar dados principais do dashboard
    const dashboardResponse = await axios.get(`${API_URL}/dashboard`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    });

    const data = dashboardResponse.data;

    // Atualizar métricas
    metrics.value = {
      totalColaboradores: data.totalColaboradores || 0,
      avaliacoesConcluidas: data.avaliacoesConcluidasMes || 0,
      mediaMensal: data.metaMensal ? Math.round(data.metaMensal) : 0,
      pendencias: data.totalPendencias || 0
    };

    // Atualizar evolução mensal
    evolucaoMensal.value = data.evolucaoMensal || [];

  } catch (err) {
    console.error('Erro ao buscar dados do dashboard:', err);
    error.value = 'Não foi possível carregar os dados do dashboard';
  } finally {
    loading.value = false;
  }
};

const createLineChart = () => {
  if (!lineChart.value) return;

  const ctx = lineChart.value.getContext('2d');
  
  // Mapear os meses em português para labels
  const mesesPortugues = {
    'Jan': 'Jan', 'Feb': 'Fev', 'Mar': 'Mar', 'Apr': 'Abr',
    'May': 'Mai', 'Jun': 'Jun', 'Jul': 'Jul', 'Aug': 'Ago',
    'Sep': 'Set', 'Oct': 'Out', 'Nov': 'Nov', 'Dec': 'Dez',
    // Caso venha em português completo
    'Janeiro': 'Jan', 'Fevereiro': 'Fev', 'Março': 'Mar', 'Abril': 'Abr',
    'Maio': 'Mai', 'Junho': 'Jun', 'Julho': 'Jul', 'Agosto': 'Ago',
    'Setembro': 'Set', 'Outubro': 'Out', 'Novembro': 'Nov', 'Dezembro': 'Dez'
  };

  // Usar dados reais da API ou dados de exemplo
  const labels = evolucaoMensal.value.length > 0 
    ? evolucaoMensal.value.map(item => mesesPortugues[item.mes] || item.mes)
    : ['Jan', 'Fev', 'Mar', 'Abr', 'Mai', 'Jun', 'Jul', 'Ago', 'Set', 'Out', 'Nov'];

  const data = evolucaoMensal.value.length > 0
    ? evolucaoMensal.value.map(item => item.quantidade)
    : [14, 12, 18, 22, 16, 14, 20, 26, 23, 21, 18];

  const maxValue = Math.max(...data, 20) + 5;

  new Chart(ctx, {
    type: 'line',
    data: {
      labels: labels,
      datasets: [{
        label: 'Colaboradores',
        data: data,
        borderColor: '#2563eb',
        backgroundColor: 'rgba(37, 99, 235, 0.1)',
        tension: 0.4,
        fill: true,
        pointBackgroundColor: '#2563eb',
        pointBorderColor: '#fff',
        pointBorderWidth: 2,
        pointRadius: 4,
        pointHoverRadius: 6
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          max: maxValue,
          ticks: {
            color: '#6e7f89'
          },
          grid: {
            color: '#e3eeee'
          }
        },
        x: {
          ticks: {
            color: '#6e7f89'
          },
          grid: {
            display: false
          }
        }
      }
    }
  });
};

const createBarChart = () => {
  const ctx = barChart.value.getContext('2d');
  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['Trabalho em Equipe', 'Comunicação', 'Liderança', 'Gestão de Tempo', 'Resolução de Problemas'],
      datasets: [{
        label: 'Avaliações',
        data: [145, 132, 128, 125, 98],
        backgroundColor: '#2563eb',
        borderRadius: 6
      }]
    },
    options: {
      indexAxis: 'y',
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false
        }
      },
      scales: {
        x: {
          beginAtZero: true,
          max: 160,
          ticks: {
            stepSize: 40,
            color: '#6e7f89'
          },
          grid: {
            color: '#e3eeee'
          }
        },
        y: {
          ticks: {
            color: '#6e7f89'
          },
          grid: {
            display: false
          }
        }
      }
    }
  });
};

const createCompetencyBarChart = () => {
  const ctx = competencyBarChart.value.getContext('2d');
  new Chart(ctx, {
    type: 'bar',
    data: {
      labels: ['Liderança', 'Comunicação', 'Trabalho em Equipe', 'Ser Multitarefa', 'Inovação', 'Gestão de Tempo'],
      datasets: [{
        label: 'Colaboradores',
        data: [45, 68, 72, 52, 38, 60],
        backgroundColor: '#2563eb',
        borderRadius: 6
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          display: false
        }
      },
      scales: {
        y: {
          beginAtZero: true,
          max: 80,
          ticks: {
            stepSize: 20,
            color: '#6e7f89'
          },
          grid: {
            color: '#e3eeee'
          }
        },
        x: {
          ticks: {
            color: '#6e7f89'
          },
          grid: {
            display: false
          }
        }
      }
    }
  });
};

const createPieChart = () => {
  const ctx = pieChart.value.getContext('2d');
  new Chart(ctx, {
    type: 'pie',
    data: {
      labels: ['TI', 'RH', 'Financeiro', 'Marketing', 'Operações', 'Vendas'],
      datasets: [{
        data: [35, 20, 15, 12, 10, 8],
        backgroundColor: [
          '#2563eb',
          '#3b82f6',
          '#60a5fa',
          '#93c5fd',
          '#bfdbfe',
          '#dbeafe'
        ],
        borderWidth: 2,
        borderColor: '#fff'
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'right',
          labels: {
            padding: 15,
            font: {
              size: 12
            },
            color: '#6e7f89'
          }
        }
      }
    }
  });
};

onMounted(async () => {
  // Primeiro buscar os dados da API
  await fetchDashboardData();
  
  // Depois criar os gráficos com os dados reais
  createLineChart();
  createBarChart();
  createCompetencyBarChart();
  createPieChart();
});
</script>

<style scoped>
.dashboard-container {
  max-width: 1400px;
  margin: 0 auto;
}

/* Loading State */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  gap: 16px;
}

.spinner {
  width: 48px;
  height: 48px;
  border: 4px solid var(--border, #e3eeee);
  border-top-color: var(--blue, #2563eb);
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.loading-state p {
  color: var(--subtitle, #6e7f89);
  font-size: 15px;
}

/* Error State */
.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  gap: 16px;
}

.error-state p {
  color: var(--subtitle, #6e7f89);
  font-size: 15px;
  margin: 0;
}

.retry-button {
  padding: 10px 20px;
  background: var(--blue, #2563eb);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  font-family: inherit;
}

.retry-button:hover {
  background: #1e40af;
  transform: translateY(-1px);
}

/* Header */
.dashboard-header {
  margin-bottom: 24px;
}

.page-title {
  color: var(--title, #274b57);
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 6px 0;
}

.page-subtitle {
  color: var(--subtitle, #6e7f89);
  font-size: 15px;
  margin: 0;
}

/* Metrics Grid */
.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
  gap: 20px;
  margin-bottom: 24px;
}

.metric-card {
  background: var(--card, #ffffff);
  border-radius: 12px;
  border: 1px solid var(--border, #e3eeee);
  padding: 24px;
  transition: all 0.2s ease;
  box-shadow: var(--shadow, 0 2px 12px rgba(0,0,0,.06));
}

.metric-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
}

.metric-card__header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.metric-card__label {
  font-size: 14px;
  color: var(--subtitle, #6e7f89);
  font-weight: 500;
  line-height: 1.4;
}

.metric-card__icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: #e8f0fe;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #2563eb;
  flex-shrink: 0;
}

.metric-card__icon svg {
  stroke: currentColor;
}

.metric-card__value {
  font-size: 40px;
  font-weight: 600;
  color: var(--title, #274b57);
  margin-bottom: 8px;
  line-height: 1;
}

.metric-card__change {
  font-size: 13px;
  font-weight: 500;
}

.metric-card__change.positive {
  color: #2563eb;
}

.metric-card__change.negative {
  color: #ef4444;
}

/* Charts Grid */
.charts-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.chart-card {
  background: var(--card, #ffffff);
  border-radius: 12px;
  border: 1px solid var(--border, #e3eeee);
  padding: 20px;
  box-shadow: var(--shadow, 0 2px 12px rgba(0,0,0,.06));
}

.chart-card--large {
  grid-column: span 1;
}

.chart-card__header {
  margin-bottom: 20px;
}

.chart-card__title {
  color: var(--title, #274b57);
  font-size: 17px;
  font-weight: 600;
  margin: 0 0 4px 0;
}

.chart-card__subtitle {
  color: var(--subtitle, #6e7f89);
  font-size: 13px;
  margin: 0;
}

.chart-card__body {
  height: 300px;
  position: relative;
}

.chart-card__body canvas {
  max-height: 100%;
}

/* Responsive Design */
@media (max-width: 1200px) {
  .charts-grid {
    grid-template-columns: 1fr;
  }
  
  .chart-card--large {
    grid-column: span 1;
  }
}

@media (max-width: 768px) {
  .metrics-grid {
    grid-template-columns: 1fr;
  }
  
  .page-title {
    font-size: 24px;
  }
  
  .metric-card__value {
    font-size: 28px;
  }
  
  .chart-card__body {
    height: 250px;
  }
}
</style>