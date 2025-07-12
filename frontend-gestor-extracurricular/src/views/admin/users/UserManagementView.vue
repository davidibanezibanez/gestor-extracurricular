<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h1>Gestión de Usuarios</h1>
      
      <router-link to="/admin/users/create" class="btn btn-primary">
        Nuevo Usuario
      </router-link>
    </div>
    
    <LoadingSpinner v-if="userStore.loading" />
    
    <ErrorAlert :error="userStore.error" @close="userStore.clearError" />
    
    <div v-if="successMessage" class="alert alert-success">
      {{ successMessage }}
    </div>

    <div v-if="!userStore.loading" class="card">
      <table class="table">
        <thead>
          <tr>
            <th>Usuario</th>
            <th>Email</th>
            <th>Rol</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in userStore.users" :key="user.id">
            <td>
              <strong>{{ user.username }}</strong>
            </td>
            <td>{{ user.email }}</td>
            <td>{{ formatRole(user.role) }}</td>
            <td>
              <span v-if="user.isEnabled" class="status-enabled">Habilitado</span>
              <span v-else class="status-disabled">Deshabilitado</span>
            </td>
            <td>
              <div class="d-flex gap-2">
                <button
                  @click="editUser(user)"
                  class="btn btn-primary small"
                >
                  Editar
                </button>
                
                <button
                  v-if="user.isEnabled"
                  @click="toggleStatus(user.username, false)"
                  class="btn btn-danger small"
                  :disabled="updating"
                >
                  Deshabilitar
                </button>
                
                <button
                  v-else
                  @click="toggleStatus(user.username, true)"
                  class="btn btn-success small"
                  :disabled="updating"
                >
                  Habilitar
                </button>
                
                <button
                  @click="deleteUser(user.username)"
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
      
      <div v-if="userStore.users.length === 0" class="text-center">
        <p class="text-muted">No hay usuarios registrados</p>
      </div>
    </div>

    <!-- Modal de Edición -->
    <div v-if="showEditModal" class="modal-overlay" @click="closeEditModal">
      <div class="modal-content" @click.stop>
        <h3>Editar Usuario</h3>
        
        <form @submit.prevent="saveUser">
          <div class="form-group">
            <label for="email">Email</label>
            <input
              id="email"
              v-model="editForm.email"
              type="email"
              class="form-control"
              required
            >
          </div>
          
          <div class="d-flex gap-2">
            <button type="submit" class="btn btn-primary" :disabled="updating">
              {{ updating ? 'Guardando...' : 'Guardar' }}
            </button>
            <button type="button" @click="closeEditModal" class="btn btn-secondary">
              Cancelar
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { useUserStore } from '@/stores/user.store'
import type { User, UpdateUserRequest } from '@/types'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import ErrorAlert from '@/components/common/ErrorAlert.vue'

const userStore = useUserStore()

const updating = ref(false)
const successMessage = ref('')
const showEditModal = ref(false)
const currentUser = ref<User | null>(null)

const editForm = reactive<UpdateUserRequest>({
  email: '',
  profilePicture: ''
})

async function toggleStatus(username: string, enable: boolean) {
  updating.value = true
  const success = await userStore.toggleUserStatus(username, enable)
  
  if (success) {
    successMessage.value = `Usuario ${enable ? 'habilitado' : 'deshabilitado'} exitosamente`
    setTimeout(() => successMessage.value = '', 3000)
  }
  
  updating.value = false
}

async function deleteUser(username: string) {
  if (!confirm(`¿Estás seguro de que quieres eliminar el usuario "${username}"?`)) {
    return
  }
  
  updating.value = true
  const success = await userStore.deleteUser(username)
  
  if (success) {
    successMessage.value = 'Usuario eliminado exitosamente'
    setTimeout(() => successMessage.value = '', 3000)
  }
  
  updating.value = false
}

function editUser(user: User) {
  currentUser.value = user
  editForm.email = user.email
  editForm.profilePicture = user.profilePicture || ''
  showEditModal.value = true
}

async function saveUser() {
  if (!currentUser.value) return
  
  updating.value = true
  const success = await userStore.updateUser(currentUser.value.username, editForm)
  
  if (success) {
    successMessage.value = 'Usuario actualizado exitosamente'
    setTimeout(() => successMessage.value = '', 3000)
    closeEditModal()
  }
  
  updating.value = false
}

function closeEditModal() {
  showEditModal.value = false
  currentUser.value = null
  editForm.email = ''
  editForm.profilePicture = ''
}

function formatRole(role: string): string {
  const roles: Record<string, string> = {
    'ADMIN_USERS': 'Admin. Usuarios',
    'ADMIN_ACTIVITIES': 'Admin. Actividades',
    'STUDENT': 'Estudiante'
  }
  return roles[role] || role
}

onMounted(() => {
  userStore.fetchUsers()
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 24px;
  border-radius: 8px;
  max-width: 500px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}
</style>