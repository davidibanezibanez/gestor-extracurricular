<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h1>Actividades Disponibles</h1>

      <div class="d-flex gap-2">
        <select v-model="selectedType" @change="filterActivities" class="form-control" style="width: 200px;">
          <option value="">Todos los tipos</option>
          <option value="ACADEMICA">Académica</option>
          <option value="DEPORTIVA">Deportiva</option>
          <option value="ARTISTICA">Artística</option>
          <option value="RELIGIOSA">Religiosa</option>
          <option value="DIRIGENCIA_ESTUDIANTIL">Dirigencia Estudiantil</option>
          <option value="RESPONSABILIDAD_SOCIAL">Responsabilidad Social</option>
        </select>

        <input v-model="searchQuery" @input="filterActivities" type="text" class="form-control"
          placeholder="Buscar por nombre..." style="width: 200px;" />

        <router-link v-if="authStore.userRole === 'ADMIN_ACTIVITIES'" to="/admin/activities/create"
          class="btn btn-primary">
          Nueva Actividad
        </router-link>
      </div>
    </div>

    <LoadingSpinner v-if="activityStore.loading" />

    <ErrorAlert :error="activityStore.error" @close="activityStore.clearError" />

    <div v-if="!activityStore.loading && filteredActivities.length === 0" class="text-center">
      <p class="text-muted">No hay actividades disponibles</p>
    </div>

    <div class="grid grid-2">
      <ActivityCard v-for="activity in filteredActivities" :key="activity.id" :activity="activity"
        @click="goToDetail(activity.id)" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth.store'
import { useActivityStore } from '@/stores/activity.store'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import ErrorAlert from '@/components/common/ErrorAlert.vue'
import ActivityCard from '@/components/activities/ActivityCard.vue'

const router = useRouter()
const authStore = useAuthStore()
const activityStore = useActivityStore()

const selectedType = ref('')

const filteredActivities = computed(() => {
  return activityStore.activities.filter(activity => {
    const matchesType =
      !selectedType.value || activity.typeActivity === selectedType.value

    const matchesSearch =
      activity.nameActivity.toLowerCase().includes(searchQuery.value.toLowerCase())

    return matchesType && matchesSearch
  })
})

const searchQuery = ref('')

function filterActivities() {
  // Lógica en computed
}

function goToDetail(id: number) {
  router.push(`/activities/${id}`)
}

onMounted(() => {
  activityStore.fetchActivities()
})
</script>