<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h1>Crear Nuevo Usuario</h1>
      
      <router-link to="/admin/users" class="btn btn-secondary">
        Volver
      </router-link>
    </div>
    
    <ErrorAlert :error="userStore.error" @close="userStore.clearError" />
    
    <div class="card">
      <form @submit.prevent="handleSubmit">
        <div class="grid grid-2">
          <div>
            <div class="form-group">
              <label for="username">Nombre de Usuario *</label>
              <input
                id="username"
                v-model="form.username"
                type="text"
                class="form-control"
                required
                :disabled="userStore.loading"
              >
            </div>
            
            <div class="form-group">
              <label for="email">Email *</label>
              <input
                id="email"
                v-model="form.email"
                type="email"
                class="form-control"
                required
                :disabled="userStore.loading"
              >
            </div>
          </div>
          
          <div>
            <div class="form-group">
              <label for="role">Rol *</label>
              <select
                id="role"
                v-model="form.role"
                class="form-control"
                required
                :disabled="userStore.loading"
              >
                <option value="">Seleccionar rol</option>
                <option value="STUDENT">Estudiante</option>
                <option value="ADMIN_ACTIVITIES">Administrador de Actividades</option>
                <option value="ADMIN_USERS">Administrador de Usuarios</option>
              </select>
            </div>
          </div>
        </div>
        
        <div class="d-flex gap-2">
          <button
            type="submit"
            class="btn btn-primary"
            :disabled="userStore.loading"
          >
            {{ userStore.loading ? 'Creando...' : 'Crear Usuario' }}
          </button>
          
          <router-link to="/admin/users" class="btn btn-secondary">
            Cancelar
          </router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user.store'
import type { CreateUserRequest } from '@/types'
import ErrorAlert from '@/components/common/ErrorAlert.vue'

const router = useRouter()
const userStore = useUserStore()

const form = reactive<CreateUserRequest>({
  username: '',
  email: '',
  role: '',
  profilePicture: ''
})

async function handleSubmit() {
  const success = await userStore.createUser(form)
  
  if (success) {
    router.push('/admin/users')
  }
}
</script>