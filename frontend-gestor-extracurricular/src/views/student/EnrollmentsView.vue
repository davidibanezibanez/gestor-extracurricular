<template>
  <div>
    <h1>Mis Inscripciones</h1>
    
    <LoadingSpinner v-if="activityStore.loading" />
    
    <ErrorAlert :error="activityStore.error" @close="activityStore.clearError" />

    <div v-if="!activityStore.loading && activityStore.enrollments.length === 0" class="text-center">
      <div class="card">
        <h3>No tienes inscripciones</h3>
        <p class="text-muted">Explora las actividades disponibles y inscríbete en las que te interesen.</p>
        <router-link to="/activities" class="btn btn-primary">
          Ver Actividades
        </router-link>
      </div>
    </div>

    <div v-if="activityStore.enrollments.length > 0">
      <div class="grid grid-2">
        <div
          v-for="enrollment in activityStore.enrollments"
          :key="enrollment.id"
          class="activity-card"
        >
          <div class="d-flex justify-content-between align-items-start mb-3">
            <h3>{{ enrollment.activityName }}</h3>
            <span
              :class="enrollment.isCompleted ? 'status-enabled' : 'status-disabled'"
              class="small"
            >
              {{ enrollment.isCompleted ? 'Completada' : 'Pendiente' }}
            </span>
          </div>
          
          <div class="small text-muted mb-3">
            <div><strong>Fecha de inscripción:</strong> {{ formatDateTime(enrollment.enrollmentDate) }}</div>
          </div>
          
          <div class="d-flex gap-2">
            <router-link
              :to="`/activities/${enrollment.activityId}`"
              class="btn btn-primary"
            >
              Ver Detalles
            </router-link>
            
            <button
              v-if="!enrollment.isCompleted"
              @click="unenroll(enrollment.activityId, enrollment.activityName)"
              class="btn btn-danger"
              :disabled="unenrolling"
            >
              {{ unenrolling ? 'Desinscribiendo...' : 'Desinscribirse' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth.store'
import { useActivityStore } from '@/stores/activity.store'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import ErrorAlert from '@/components/common/ErrorAlert.vue'

const authStore = useAuthStore()
const activityStore = useActivityStore()

const unenrolling = ref(false)

async function unenroll(activityId: number, activityName: string) {
  if (!confirm(`¿Estás seguro de que quieres desinscribirte de "${activityName}"?`)) {
    return
  }
  
  unenrolling.value = true
  const success = await activityStore.unenrollFromActivity(activityId)
  
  if (success && authStore.user?.username) {
    // Recargar inscripciones
    await activityStore.fetchStudentEnrollments(authStore.user.username)
  }
  
  unenrolling.value = false
}

function formatDateTime(dateTimeString: string): string {
  const date = new Date(dateTimeString)
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  if (authStore.user?.username) {
    activityStore.fetchStudentEnrollments(authStore.user.username)
  }
})
</script>