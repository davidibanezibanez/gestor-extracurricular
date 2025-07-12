export interface User {
  id: number
  username: string
  email: string
  role: string
  profilePicture?: string
  isEnabled: boolean
}

export interface Activity {
  id: number
  nameActivity: string
  activityDate: string
  typeActivity: ActivityType
  startTime: string
  endTime: string
  maximumEnrollDate: string
  maximumQuotas: number
  organizer: string
  description?: string
  activityPicture?: string
  isEnabled: boolean
  isCompleted: boolean
  currentEnrollments: number
  canEnroll: boolean
}

export type ActivityType = 
  | 'ACADEMICA' 
  | 'DEPORTIVA' 
  | 'ARTISTICA' 
  | 'RELIGIOSA' 
  | 'DIRIGENCIA_ESTUDIANTIL' 
  | 'RESPONSABILIDAD_SOCIAL'

export interface Enrollment {
  id: number
  activityId: number
  activityName: string
  studentUsername: string
  enrollmentDate: string
  isCompleted: boolean
}

export interface AuthResponse {
  username: string
  message: string
  jwt: string
  status: boolean
}

export interface CreateActivityRequest {
  nameActivity: string
  activityDate: string
  typeActivity: ActivityType
  startTime: string
  endTime: string
  maximumEnrollDate: string
  maximumQuotas: number
  organizer: string
  description?: string
  activityPicture?: string
}

export interface CreateUserRequest {
  username: string
  email: string
  role: string
  profilePicture?: string
}

export interface UpdateUserRequest {
  email?: string
  profilePicture?: string
}

export interface UpdateActivityRequest {
  nameActivity?: string
  activityDate?: string
  typeActivity?: ActivityType
  startTime?: string
  endTime?: string
  maximumEnrollDate?: string
  maximumQuotas?: number
  organizer?: string
  description?: string
  activityPicture?: string
}