<template>
  <div>
    <h1>Mi Perfil</h1>
    
    <div class="card">
      <div class="grid grid-2">
        <div>
          <h3>Información Personal<br><br></h3>
          
          <div class="mb-3">
            <p><strong>Usuario:</strong> {{ authStore.user?.username }}</p>
            <p><strong>Rol:</strong> {{ formatRole(authStore.userRole) }}</p>
          </div>
          
          <div v-if="authStore.user?.profilePicture" class="mb-3">
            <p><strong>Foto de Perfil:</strong></p>
            <img 
              :src="authStore.user.profilePicture" 
              alt="Foto de perfil"
              style="max-width: 200px; height: auto; border-radius: 8px;"
              @error="imageError = true"
            >
            <p v-if="imageError" class="text-muted small">Error al cargar la imagen</p>
          </div>
        </div>
        
        <div v-if="authStore.isStudent">
          <h3>Estadísticas</h3>
          
          <div class="grid grid-2">
            <div class="card text-center">
              <h4>{{ enrollments.length }}</h4>
              <p class="text-muted">Inscripciones Totales</p>
            </div>
            
            <div class="card text-center">
              <h4>{{ completedEnrollments }}</h4>
              <p class="text-muted">Actividades Completadas</p>
            </div>
          </div>
          
          <div style="margin-top: 20px;">
            <router-link to="/student/enrollments" class="btn btn-primary">
              Ver Mis Inscripciones
            </router-link>
          </div>
        </div>
        
        <div v-else>
          <h3>Panel de Administración<br><br></h3>
          
          <div class="d-flex gap-2">
            <router-link
              v-if="authStore.userRole === 'ADMIN_ACTIVITIES'"
              to="/admin/activities"
              class="btn btn-primary"
            >
              Gestionar Actividades
            </router-link>
            
            <router-link
              v-if="authStore.userRole === 'ADMIN_USERS'"
              to="/admin/users"
              class="btn btn-primary"
            >
              Gestionar Usuarios
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth.store'
import { useActivityStore } from '@/stores/activity.store'

const authStore = useAuthStore()
const activityStore = useActivityStore()

const imageError = ref(false)

const enrollments = computed(() => activityStore.enrollments)
const completedEnrollments = computed(() => 
  enrollments.value.filter(e => e.isCompleted).length
)

function formatRole(role: string | null): string {
  if (!role) return 'Sin rol'
  
  const roles: Record<string, string> = {
    'ADMIN_USERS': 'Administrador de Usuarios',
    'ADMIN_ACTIVITIES': 'Administrador de Actividades',
    'STUDENT': 'Estudiante'
  }
  return roles[role] || role
}

onMounted(() => {
  if (authStore.isStudent && authStore.user?.username) {
    activityStore.fetchStudentEnrollments(authStore.user.username)
  }
})
</script>

<style scoped>
.flex-column {
  flex-direction: column;
}
</style>