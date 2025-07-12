import { activityApi } from '@/config/api'
import type { Activity, CreateActivityRequest, UpdateActivityRequest, Enrollment } from '@/types'

export const activityService = {
  async getActivities(): Promise<Activity[]> {
    const response = await activityApi.get('/activities')
    return response.data
  },

  async getActivityById(id: number): Promise<Activity> {
    const response = await activityApi.get(`/activities/${id}`)
    return response.data
  },

  async createActivity(activityData: CreateActivityRequest): Promise<Activity> {
    const response = await activityApi.post('/activities', activityData)
    return response.data
  },

  async updateActivity(id: number, activityData: UpdateActivityRequest): Promise<Activity> {
    const response = await activityApi.put(`/activities/${id}`, activityData)
    return response.data
  },

  async deleteActivity(id: number): Promise<void> {
    await activityApi.delete(`/activities/${id}`)
  },

  async enableActivity(id: number): Promise<Activity> {
    const response = await activityApi.patch(`/activities/${id}/enable`)
    return response.data
  },

  async disableActivity(id: number): Promise<Activity> {
    const response = await activityApi.patch(`/activities/${id}/disable`)
    return response.data
  },

  async enrollInActivity(activityId: number): Promise<Enrollment> {
    const response = await activityApi.post(`/enrollments/activity/${activityId}`)
    return response.data
  },

  async unenrollFromActivity(activityId: number): Promise<void> {
    await activityApi.delete(`/enrollments/activity/${activityId}`)
  },

  async getStudentEnrollments(username: string): Promise<Enrollment[]> {
    const response = await activityApi.get(`/enrollments/student/${username}`)
    return response.data
  },

  async checkEnrollment(activityId: number, username: string): Promise<boolean> {
    const response = await activityApi.get(`/enrollments/activity/${activityId}/check/${username}`)
    return response.data.isEnrolled
  }
}