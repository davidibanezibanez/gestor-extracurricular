import { defineStore } from 'pinia'
import { ref } from 'vue'
import { activityService } from '@/services/activity.service'
import type { Activity, CreateActivityRequest, UpdateActivityRequest, Enrollment } from '@/types'

export const useActivityStore = defineStore('activity', () => {
  const activities = ref<Activity[]>([])
  const currentActivity = ref<Activity | null>(null)
  const enrollments = ref<Enrollment[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  async function fetchActivities() {
    loading.value = true
    error.value = null
    
    try {
      activities.value = await activityService.getActivities()
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al cargar actividades'
    } finally {
      loading.value = false
    }
  }

  async function fetchActivityById(id: number) {
    loading.value = true
    error.value = null
    
    try {
      currentActivity.value = await activityService.getActivityById(id)
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al cargar actividad'
    } finally {
      loading.value = false
    }
  }

  async function createActivity(activityData: CreateActivityRequest) {
    loading.value = true
    error.value = null
    
    try {
      const newActivity = await activityService.createActivity(activityData)
      activities.value.push(newActivity)
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al crear actividad'
      return false
    } finally {
      loading.value = false
    }
  }

  async function updateActivity(id: number, activityData: UpdateActivityRequest) {
    loading.value = true
    error.value = null
    
    try {
      const updatedActivity = await activityService.updateActivity(id, activityData)
      const index = activities.value.findIndex(a => a.id === id)
      if (index !== -1) {
        activities.value[index] = updatedActivity
      }
      if (currentActivity.value?.id === id) {
        currentActivity.value = updatedActivity
      }
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al actualizar actividad'
      return false
    } finally {
      loading.value = false
    }
  }

  async function deleteActivity(id: number) {
    loading.value = true
    error.value = null
    
    try {
      await activityService.deleteActivity(id)
      activities.value = activities.value.filter(a => a.id !== id)
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al eliminar actividad'
      return false
    } finally {
      loading.value = false
    }
  }

  async function toggleActivityStatus(id: number, enable: boolean) {
    loading.value = true
    error.value = null
    
    try {
      const updatedActivity = enable 
        ? await activityService.enableActivity(id)
        : await activityService.disableActivity(id)
      
      const index = activities.value.findIndex(a => a.id === id)
      if (index !== -1) {
        activities.value[index] = updatedActivity
      }
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al cambiar estado de actividad'
      return false
    } finally {
      loading.value = false
    }
  }

  async function enrollInActivity(activityId: number) {
    loading.value = true
    error.value = null
    
    try {
      await activityService.enrollInActivity(activityId)
      // Actualizar la actividad para reflejar el nuevo número de inscripciones
      await fetchActivityById(activityId)
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al inscribirse en actividad'
      return false
    } finally {
      loading.value = false
    }
  }

  async function unenrollFromActivity(activityId: number) {
    loading.value = true
    error.value = null
    
    try {
      await activityService.unenrollFromActivity(activityId)
      // Actualizar la actividad para reflejar el nuevo número de inscripciones
      await fetchActivityById(activityId)
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al desinscribirse de actividad'
      return false
    } finally {
      loading.value = false
    }
  }

  async function fetchStudentEnrollments(username: string) {
    loading.value = true
    error.value = null
    
    try {
      enrollments.value = await activityService.getStudentEnrollments(username)
    } catch (err: any) {
      error.value = err.response?.data?.message || 'Error al cargar inscripciones'
    } finally {
      loading.value = false
    }
  }

  function clearError() {
    error.value = null
  }

  return {
    activities,
    currentActivity,
    enrollments,
    loading,
    error,
    fetchActivities,
    fetchActivityById,
    createActivity,
    updateActivity,
    deleteActivity,
    toggleActivityStatus,
    enrollInActivity,
    unenrollFromActivity,
    fetchStudentEnrollments,
    clearError
  }
})