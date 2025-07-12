<template>
  <div class="card" style="max-width: 400px; margin: 50px auto;">
    <h2 class="text-center mb-3">Iniciar Sesión</h2>
    
    <ErrorAlert :error="authStore.error" @close="authStore.clearError" />
    
    <form @submit.prevent="handleLogin">
      <div class="form-group">
        <label for="username">Usuario</label>
        <input
          id="username"
          v-model="form.username"
          type="text"
          class="form-control"
          required
          :disabled="authStore.loading"
        >
      </div>
      
      <div class="form-group">
        <label for="password">Contraseña</label>
        <input
          id="password"
          v-model="form.password"
          type="password"
          class="form-control"
          required
          :disabled="authStore.loading"
        >
      </div>
      
      <button
        type="submit"
        class="btn btn-primary"
        style="width: 100%;"
        :disabled="authStore.loading"
      >
        {{ authStore.loading ? 'Iniciando sesión...' : 'Iniciar Sesión' }}
      </button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'
import ErrorAlert from '@/components/common/ErrorAlert.vue'

const router = useRouter()
const authStore = useAuthStore()

const form = reactive({
  username: '',
  password: ''
})

async function handleLogin() {
  const success = await authStore.login(form.username, form.password)
  
  if (success) {
    // Esperar un poco para que se actualicen los computeds
    await new Promise(resolve => setTimeout(resolve, 100))
    
    console.log('REDIRECTING - User role:', authStore.userRole)
    console.log('Is Admin Users:', authStore.isAdminUsers)
    console.log('Is Admin Activities:', authStore.isAdminActivities)
    console.log('Is Student:', authStore.isStudent)
    
    if (authStore.isAdminUsers) {
      console.log('→ Redirecting to /admin/users')
      router.push('/admin/users')
    } else if (authStore.isAdminActivities) {
      console.log('→ Redirecting to /admin/activities')
      router.push('/admin/activities')
    } else if (authStore.isStudent) {
      console.log('→ Redirecting to /activities')
      router.push('/activities')
    } else {
      console.log('→ Redirecting to / (default)')
      router.push('/')
    }
  }
}
</script>