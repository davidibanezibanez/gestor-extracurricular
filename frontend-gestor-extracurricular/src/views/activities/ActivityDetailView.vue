<template>
  <div>
    <LoadingSpinner v-if="activityStore.loading" />
    
    <ErrorAlert :error="activityStore.error" @close="activityStore.clearError" />
    
    <div v-if="activity" class="card">
      <div class="d-flex justify-content-between align-items-start mb-3">
        <div>
          <div class="activity-type" :class="`type-${activity.typeActivity.toLowerCase()}`">
            {{ formatActivityType(activity.typeActivity) }}
          </div>
          <h2>{{ activity.nameActivity }}</h2>
        </div>
        
        <div>
          <router-link
            v-if="authStore.userRole === 'ADMIN_ACTIVITIES'"
            :to="`/admin/activities/${activity.id}/edit`"
            class="btn btn-secondary"
          >
            Editar
          </router-link>
          
          <button
            v-if="authStore.isStudent && activity.canEnroll && !isEnrolled"
            @click="enroll"
            class="btn btn-success"
            :disabled="enrolling"
          >
            {{ enrolling ? 'Inscribiendo...' : 'Inscribirse' }}
          </button>
          
          <button
            v-if="authStore.isStudent && isEnrolled && !activity.isCompleted"
            @click="unenroll"
            class="btn btn-danger"
            :disabled="enrolling"
          >
            {{ enrolling ? 'Desinscribiendo...' : 'Desinscribirse' }}
          </button>
        </div>
      </div>

      <div class="grid grid-2">
        <div>
          <h3>Información General</h3>
          <div class="mb-3">
            <p><strong>Fecha:</strong> {{ formatDate(activity.activityDate) }}</p>
            <p><strong>Hora:</strong> {{ activity.startTime }} - {{ activity.endTime }}</p>
            <p><strong>Organizador:</strong> {{ activity.organizer }}</p>
            <p><strong>Fecha límite de inscripción:</strong> {{ formatDate(activity.maximumEnrollDate) }}</p>
          </div>
          
          <h4>Descripción</h4>
          <p>{{ activity.description || 'Sin descripción disponible' }}</p>
        </div>
        
        <div>
          <h3>Disponibilidad</h3>
          <div class="mb-3">
            <p><strong>Cupos máximos:</strong> {{ activity.maximumQuotas }}</p>
            <p><strong>Inscritos actuales:</strong> {{ activity.currentEnrollments }}</p>
            <p><strong>Cupos disponibles:</strong> {{ activity.maximumQuotas - activity.currentEnrollments }}</p>
          </div>
          
          <div class="mb-3">
            <p><strong>Estado:</strong>
              <span v-if="activity.isCompleted" class="status-disabled">Completada</span>
              <span v-else-if="!activity.isEnabled" class="status-disabled">Deshabilitada</span>
              <span v-else-if="activity.canEnroll" class="status-enabled">Disponible para inscripción</span>
              <span v-else class="status-disabled">No disponible</span>
            </p>
          </div>
          
          <div v-if="authStore.isStudent && isEnrolled" class="alert alert-success">
            ✓ Estás inscrito en esta actividad
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'
import { useActivityStore } from '@/stores/activity.store'
import { activityService } from '@/services/activity.service'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import ErrorAlert from '@/components/common/ErrorAlert.vue'

const route = useRoute()
const authStore = useAuthStore()
const activityStore = useActivityStore()

const enrolling = ref(false)
const isEnrolled = ref(false)

const activity = computed(() => activityStore.currentActivity)

async function checkEnrollment() {
  if (authStore.isStudent && authStore.user?.username && activity.value) {
    try {
      isEnrolled.value = await activityService.checkEnrollment(
        activity.value.id, 
        authStore.user.username
      )
    } catch (error) {
      console.error('Error checking enrollment:', error)
    }
  }
}

async function enroll() {
  if (!activity.value) return
  
  enrolling.value = true
  const success = await activityStore.enrollInActivity(activity.value.id)
  
  if (success) {
    isEnrolled.value = true
  }
  
  enrolling.value = false
}

async function unenroll() {
  if (!activity.value) return
  
  enrolling.value = true
  const success = await activityStore.unenrollFromActivity(activity.value.id)
  
  if (success) {
    isEnrolled.value = false
  }
  
  enrolling.value = false
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
    month: 'long',
    day: 'numeric'
  })
}

onMounted(async () => {
  const activityId = Number(route.params.id)
  await activityStore.fetchActivityById(activityId)
  await checkEnrollment()
})
</script>