import axios from 'axios'

const API_BASE_URL = 'http://200.13.4.247'

// Crear instancia separada para cada servicio
export const authApi = axios.create({
  baseURL: `${API_BASE_URL}:8080`,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const userApi = axios.create({
  baseURL: `${API_BASE_URL}:8081`,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const activityApi = axios.create({
  baseURL: `${API_BASE_URL}:8082`,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

export const reportApi = axios.create({
  baseURL: `${API_BASE_URL}:8083`,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// Interceptor para TODAS las instancias
const apis = [authApi, userApi, activityApi, reportApi]

apis.forEach(apiInstance => {
  // Request interceptor para agregar token
  apiInstance.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('token')
      if (token) {
        config.headers.Authorization = `Bearer ${token}`
      }
      console.log(` ${config.method?.toUpperCase()} ${config.url}`, config.headers.Authorization ? '(with token)' : '(no token)')
      return config
    },
    (error) => {
      return Promise.reject(error)
    }
  )

  // Response interceptor para manejar errores
  apiInstance.interceptors.response.use(
    (response) => response,
    (error) => {
      console.error(` API Error:`, error.response?.status, error.config?.url)
      if (error.response?.status === 401) {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        window.location.href = '/login'
      }
      return Promise.reject(error)
    }
  )
})

// Mantener compatibilidad con el código existente
export const api = activityApi

export const AUTH_API = `${API_BASE_URL}:8080`
export const USER_API = `${API_BASE_URL}:8081`
export const ACTIVITY_API = `${API_BASE_URL}:8082`
export const REPORT_API = `${API_BASE_URL}:8083`