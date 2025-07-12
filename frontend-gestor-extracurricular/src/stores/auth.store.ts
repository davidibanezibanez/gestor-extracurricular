import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authService } from '@/services/auth.service'
import type { AuthResponse } from '@/types'

export const useAuthStore = defineStore('auth', () => {
  const user = ref<any>(null)
  const token = ref<string | null>(localStorage.getItem('token'))
  const loading = ref(false)
  const error = ref<string | null>(null)

  const isAuthenticated = computed(() => !!token.value)
  
  
  const userRole = computed(() => {
    if (!user.value) return null
    
    // Si roles es un array de strings (desde backend)
    if (Array.isArray(user.value.roles) && user.value.roles.length > 0) {
      return user.value.roles[0]
    }
    
    // Si role es un string directo
    if (typeof user.value.role === 'string') {
      return user.value.role
    }
    
    return null
  })
  
  const isAdmin = computed(() => {
    const role = userRole.value
    return role === 'ADMIN_USERS' || role === 'ADMIN_ACTIVITIES'
  })
  
  const isAdminUsers = computed(() => userRole.value === 'ADMIN_USERS')
  const isAdminActivities = computed(() => userRole.value === 'ADMIN_ACTIVITIES')
  const isStudent = computed(() => userRole.value === 'STUDENT')

  async function login(username: string, password: string) {
    loading.value = true
    error.value = null
    
    try {
      const response: AuthResponse = await authService.login(username, password)
      console.log('LOGIN RESPONSE:', response)
      
      if (response.status && response.jwt) {
        token.value = response.jwt
        localStorage.setItem('token', response.jwt)
        
        try {
          const userInfo = await authService.getUserInfo(username)
          console.log('USER INFO FROM BACKEND:', userInfo)
          
          user.value = userInfo
          localStorage.setItem('user', JSON.stringify(userInfo))
          
          // Esperar a que se actualicen los computeds
          await new Promise(resolve => setTimeout(resolve, 0))
          
          console.log('COMPUTED USER ROLE:', userRole.value)
          console.log('IS ADMIN USERS:', isAdminUsers.value)
          console.log('IS ADMIN ACTIVITIES:', isAdminActivities.value)
          console.log('IS STUDENT:', isStudent.value)
          
        } catch (userError) {
          console.error('Error getting user info:', userError)
          // Fallback
          user.value = {
            username: response.username,
            roles: ['STUDENT']
          }
          localStorage.setItem('user', JSON.stringify(user.value))
        }
        
        return true
      } else {
        error.value = response.message || 'Error en el login'
        return false
      }
    } catch (err: any) {
      console.error('Login error:', err)
      error.value = err.response?.data?.message || 'Error de conexión'
      return false
    } finally {
      loading.value = false
    }
  }

  async function register(username: string, password: string, role: string) {
    loading.value = true
    error.value = null
    
    try {
      const roles = [role]
      const response: AuthResponse = await authService.register(username, password, roles)
      
      if (response.status) {
        return true
      } else {
        error.value = response.message || 'Error en el registro'
        return false
      }
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error de conexión'
      return false
    } finally {
      loading.value = false
    }
  }

  function logout() {
    user.value = null
    token.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  function initializeAuth() {
    const savedUser = localStorage.getItem('user')
    if (savedUser && token.value) {
      try {
        user.value = JSON.parse(savedUser)
        console.log('Initialized user:', user.value)
        console.log('Initialized user role:', userRole.value)
      } catch {
        logout()
      }
    }
  }

  function clearError() {
    error.value = null
  }

  return {
    user,
    token,
    loading,
    error,
    isAuthenticated,
    userRole,
    isAdmin,
    isAdminUsers,
    isAdminActivities,
    isStudent,
    login,
    register,
    logout,
    initializeAuth,
    clearError
  }
})