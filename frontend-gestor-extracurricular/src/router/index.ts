import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/views/HomeView.vue')
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/auth/LoginView.vue')
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/auth/RegisterView.vue')
    },
    {
      path: '/activities',
      name: 'activities',
      component: () => import('@/views/activities/ActivityListView.vue')
    },
    {
      path: '/activities/:id',
      name: 'activity-detail',
      component: () => import('@/views/activities/ActivityDetailView.vue')
    },
    {
      path: '/admin/activities',
      name: 'admin-activities',
      component: () => import('@/views/admin/activities/ActivityManagementView.vue'),
      meta: { requiresAuth: true, requiresAdminActivities: true }
    },
    {
      path: '/admin/activities/create',
      name: 'create-activity',
      component: () => import('@/views/admin/activities/CreateActivityView.vue'),
      meta: { requiresAuth: true, requiresAdminActivities: true }
    },
    {
      path: '/admin/activities/:id/edit',
      name: 'edit-activity',
      component: () => import('@/views/admin/activities/EditActivityView.vue'),
      meta: { requiresAuth: true, requiresAdminActivities: true }
    },
    {
      path: '/admin/users',
      name: 'admin-users',
      component: () => import('@/views/admin/users/UserManagementView.vue'),
      meta: { requiresAuth: true, requiresAdminUsers: true }
    },
    {
      path: '/admin/users/create',
      name: 'create-user',
      component: () => import('@/views/admin/users/CreateUserView.vue'),
      meta: { requiresAuth: true, requiresAdminUsers: true }
    },
    {
      path: '/student/enrollments',
      name: 'student-enrollments',
      component: () => import('@/views/student/EnrollmentsView.vue'),
      meta: { requiresAuth: true, requiresStudent: true }
    },
    {
      path: '/profile',
      name: 'profile',
      component: () => import('@/views/ProfileView.vue'),
      meta: { requiresAuth: true }
    }
  ]
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  console.log('Router guard - to:', to.path, 'user role:', authStore.userRole) // Debug
  
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
    return
  }
  
  if (to.meta.requiresAdminUsers && !authStore.isAdminUsers) {
    console.log('Access denied - requires admin users') // Debug
    next('/')
    return
  }
  
  if (to.meta.requiresAdminActivities && !authStore.isAdminActivities) {
    console.log('Access denied - requires admin activities') // Debug
    next('/')
    return
  }
  
  if (to.meta.requiresStudent && !authStore.isStudent) {
    console.log('Access denied - requires student') // Debug
    next('/')
    return
  }
  
  next()
})

export default router