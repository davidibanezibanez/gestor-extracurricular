import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userService } from '@/services/user.service'
import type { User, CreateUserRequest, UpdateUserRequest } from '@/types'

export const useUserStore = defineStore('user', () => {
  const users = ref<User[]>([])
  const currentUser = ref<User | null>(null)
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchUsers() {
    loading.value = true
    error.value = null
    
    try {
      users.value = await userService.getUsers()
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al cargar usuarios'
    } finally {
      loading.value = false
    }
  }

  async function fetchUserByUsername(username: string) {
    loading.value = true
    error.value = null
    
    try {
      currentUser.value = await userService.getUserByUsername(username)
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al cargar usuario'
    } finally {
      loading.value = false
    }
  }

  async function createUser(userData: CreateUserRequest) {
    loading.value = true
    error.value = null
    
    try {
      const newUser = await userService.createUser(userData)
      users.value.push(newUser)
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al crear usuario'
      return false
    } finally {
      loading.value = false
    }
  }

  async function updateUser(username: string, userData: UpdateUserRequest) {
    loading.value = true
    error.value = null
    
    try {
      const updatedUser = await userService.updateUser(username, userData)
      const index = users.value.findIndex(u => u.username === username)
      if (index !== -1) {
        users.value[index] = updatedUser
      }
      if (currentUser.value?.username === username) {
        currentUser.value = updatedUser
      }
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al actualizar usuario'
      return false
    } finally {
      loading.value = false
    }
  }

  async function deleteUser(username: string) {
    loading.value = true
    error.value = null
    
    try {
      await userService.deleteUser(username)
      users.value = users.value.filter(u => u.username !== username)
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al eliminar usuario'
      return false
    } finally {
      loading.value = false
    }
  }

  async function toggleUserStatus(username: string, enable: boolean) {
    loading.value = true
    error.value = null
    
    try {
      const updatedUser = enable 
        ? await userService.enableUser(username)
        : await userService.disableUser(username)
      
      const index = users.value.findIndex(u => u.username === username)
      if (index !== -1) {
        users.value[index] = updatedUser
      }
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al cambiar estado de usuario'
      return false
    } finally {
      loading.value = false
    }
  }

  function clearError() {
    error.value = null
  }

  return {
    users,
    currentUser,
    loading,
    error,
    fetchUsers,
    fetchUserByUsername,
    createUser,
    updateUser,
    deleteUser,
    toggleUserStatus,
    clearError
  }
})