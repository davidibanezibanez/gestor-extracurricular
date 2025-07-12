<template>
  <nav class="navbar">
    <div class="container">
      <router-link to="/" class="navbar-brand">
        Gestor Actividades Extracurriculares
      </router-link>
      
      <div class="navbar-nav">
        
        <router-link to="/activities" v-if="authStore.isStudent" class="btn-secondary">
          Actividades
        </router-link>
        
        <template v-if="authStore.isAuthenticated">
          
          <template v-if="authStore.isAdminActivities">
            <router-link to="/admin/activities" class="btn-secondary">Gestionar Actividades</router-link>
          </template>
          
          
          <template v-if="authStore.isAdminUsers">
            <router-link to="/admin/users" class="btn-secondary">Gestionar Usuarios</router-link>
          </template>
          
          
          <template v-if="authStore.isStudent">
            <router-link to="/student/enrollments" class="btn-secondary">Mis Inscripciones</router-link>
          </template>
          
          <router-link to="/profile" class="btn-secondary">Perfil</router-link>
          <button @click="logout" class="btn btn-secondary">
            Cerrar Sesión ({{ authStore.user?.username }})
          </button>
        </template>
        
        <template v-else>
          <router-link to="/login" class="btn-secondary">Iniciar Sesión</router-link>
        </template>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth.store'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

function logout() {
  authStore.logout()
  router.push('/')
}
</script>