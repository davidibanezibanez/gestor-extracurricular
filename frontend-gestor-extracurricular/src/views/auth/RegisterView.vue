<template>
  <div class="card" style="max-width: 400px; margin: 50px auto;">
    <h2 class="text-center mb-3">Registro</h2>
    
    <ErrorAlert :error="authStore.error" @close="authStore.clearError" />
    
    <div v-if="successMessage" class="alert alert-success">
      {{ successMessage }}
    </div>
    
    <form @submit.prevent="handleRegister">
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
          minlength="6"
          :disabled="authStore.loading"
        >
      </div>
      
      <div class="form-group">
        <label for="role">Tipo de Usuario</label>
        <select
          id="role"
          v-model="form.role"
          class="form-control"
          required
          :disabled="authStore.loading"
        >
          <option value="">Seleccionar tipo</option>
          <option value="STUDENT">Estudiante</option>
          <option value="ADMIN_ACTIVITIES">Administrador de Actividades</option>
          <option value="ADMIN_USERS">Administrador de Usuarios</option>
        </select>
      </div>
      
      <button
        type="submit"
        class="btn btn-primary"
        style="width: 100%;"
        :disabled="authStore.loading"
      >
        {{ authStore.loading ? 'Registrando...' : 'Registrarse' }}
      </button>
    </form>
    
    <div class="text-center" style="margin-top: 20px;">
      <p class="text-muted">
        ¿Ya tienes cuenta? 
        <router-link to="/login">Inicia sesión aquí</router-link>
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'
import ErrorAlert from '@/components/common/ErrorAlert.vue'

const router = useRouter()
const authStore = useAuthStore()

const successMessage = ref('')

const form = reactive({
  username: '',
  password: '',
  role: ''
})

async function handleRegister() {
  const success = await authStore.register(form.username, form.password, form.role)
  
  if (success) {
    successMessage.value = 'Usuario registrado exitosamente. Ahora puedes iniciar sesión.'
    form.username = ''
    form.password = ''
    form.role = ''
    
    setTimeout(() => {
      router.push('/login')
    }, 2000)
  }
}
</script>