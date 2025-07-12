import { authApi } from '@/config/api'
import type { AuthResponse } from '@/types'

export const authService = {
  async login(username: string, password: string): Promise<AuthResponse> {
    const response = await authApi.post('/auth/log-in', {
      username,
      password
    })
    return response.data
  },

  async register(username: string, password: string, roles: string[]): Promise<AuthResponse> {
    const response = await authApi.post('/auth/sign-up', {
      username,
      password,
      roleRequest: {
        roleListName: roles
      }
    })
    return response.data
  },

  async validateToken(token: string): Promise<any> {
    const response = await authApi.post('/auth/validate-token', {}, {
      headers: { Authorization: `Bearer ${token}` }
    })
    return response.data
  },

  async getUserInfo(username: string): Promise<any> {
    const response = await authApi.get(`/auth/user/${username}`)
    return response.data
  }
}