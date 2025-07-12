import { userApi } from '@/config/api'
import type { User, CreateUserRequest, UpdateUserRequest } from '@/types'

export const userService = {
  async getUsers(): Promise<User[]> {
    const response = await userApi.get('/users')
    return response.data
  },

  async getUserByUsername(username: string): Promise<User> {
    const response = await userApi.get(`/users/${username}`)
    return response.data
  },

  async createUser(userData: CreateUserRequest): Promise<User> {
    const response = await userApi.post('/users', userData)
    return response.data
  },

  async updateUser(username: string, userData: UpdateUserRequest): Promise<User> {
    const response = await userApi.put(`/users/${username}`, userData)
    return response.data
  },

  async deleteUser(username: string): Promise<void> {
    await userApi.delete(`/users/${username}`)
  },

  async enableUser(username: string): Promise<User> {
    const response = await userApi.patch(`/users/${username}/enable`)
    return response.data
  },

  async disableUser(username: string): Promise<User> {
    const response = await userApi.patch(`/users/${username}/disable`)
    return response.data
  }
}