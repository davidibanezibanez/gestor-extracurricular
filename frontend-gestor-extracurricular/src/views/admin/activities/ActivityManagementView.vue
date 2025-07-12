<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h1>Gestión de Actividades</h1>
      
      <router-link to="/admin/activities/create" class="btn btn-primary">
        Nueva Actividad
      </router-link>
    </div>
    
    <LoadingSpinner v-if="activityStore.loading" />
    
    <ErrorAlert :error="activityStore.error" @close="activityStore.clearError" />
    
    <div v-if="successMessage" class="alert alert-success">
      {{ successMessage }}
    </div>

    <div v-if="!activityStore.loading" class="card">
      <table class="table">
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Tipo</th>
            <th>Fecha</th>
            <th>Inscripciones</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="activity in activityStore.activities" :key="activity.id">
            <td>
              <strong>{{ activity.nameActivity }}</strong>
              <br>
              <small class="text-muted">{{ activity.organizer }}</small>
            </td>
            <td>
              <div class="activity-type" :class="`type-${activity.typeActivity.toLowerCase()}`">
                {{ formatActivityType(activity.typeActivity) }}
              </div>
            </td>
            <td>
              {{ formatDate(activity.activityDate) }}
              <br>
              <small class="text-muted">{{ activity.startTime }} - {{ activity.endTime }}</small>
            </td>
            <td>
              {{ activity.currentEnrollments }}/{{ activity.maximumQuotas }}
              <br>
              <small class="text-muted">
                {{ activity.maximumQuotas - activity.currentEnrollments }} disponibles
              </small>
            </td>
            <td>
              <span v-if="activity.isCompleted" class="status-disabled">Completada</span>
              <span v-else-if="activity.isEnabled" class="status-enabled">Habilitada</span>
              <span v-else class="status-disabled">Deshabilitada</span>
            </td>
            <td>
              <div class="d-flex gap-2">
                <router-link
                  :to="`/activities/${activity.id}`"
                  class="btn btn-secondary small"
                >
                  Ver
                </router-link>
                
                <router-link
                  :to="`/admin/activities/${activity.id}/edit`"
                  class="btn btn-primary small"
                >
                  Editar
                </router-link>
                
                <button
                  v-if="activity.isEnabled"
                  @click="toggleStatus(activity.id, false)"
                  class="btn btn-danger small"
                  :disabled="updating"
                >
                  Deshabilitar
                </button>
                
                <button
                  v-else
                  @click="toggleStatus(activity.id, true)"
                  class="btn btn-success small"
                  :disabled="updating"
                >
                  Habilitar
                </button>
                
                <button
                  @click="deleteActivity(activity.id, activity.nameActivity)"
                  class="btn btn-danger small"
                  :disabled="updating"
                >
                  Eliminar
                </button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      
      <div v-if="activityStore.activities.length === 0" class="text-center">
        <p class="text-muted">No hay actividades registradas</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useActivityStore } from '@/stores/activity.store'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import ErrorAlert from '@/components/common/ErrorAlert.vue'

const activityStore = useActivityStore()

const updating = ref(false)
const successMessage = ref('')

async function toggleStatus(id: number, enable: boolean) {
  updating.value = true
  const success = await activityStore.toggleActivityStatus(id, enable)
  
  if (success) {
    successMessage.value = `Actividad ${enable ? 'habilitada' : 'deshabilitada'} exitosamente`
    setTimeout(() => successMessage.value = '', 3000)
  }
  
  updating.value = false
}

async function deleteActivity(id: number, name: string) {
  if (!confirm(`¿Estás seguro de que quieres eliminar la actividad "${name}"?`)) {
    return
  }
  
  updating.value = true
  const success = await activityStore.deleteActivity(id)
  
  if (success) {
    successMessage.value = 'Actividad eliminada exitosamente'
    setTimeout(() => successMessage.value = '', 3000)
  }
  
  updating.value = false
}

function formatActivityType(type: string): string {
  const types: Record<string, string> = {
    'ACADEMICA': 'Académica',
    'DEPORTIVA': 'Deportiva',
    'ARTISTICA': 'Artística',
    'RELIGIOSA': 'Religiosa',
    'DIRIGENCIA_ESTUDIANTIL': 'Dirigencia Estudiantil',
    'RESPONSABILIDAD_SOCIAL': 'Responsabilidad Social'
  }
  return types[type] || type
}

function formatDate(dateString: string): string {
  const date = new Date(dateString)
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'short',
    day: 'numeric'
  })
}

onMounted(() => {
  activityStore.fetchActivities()
})
</script>