<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h1>Crear Nueva Actividad</h1>

      <router-link to="/admin/activities" class="btn btn-secondary">
        Volver
      </router-link>
    </div>

    <ErrorAlert :error="activityStore.error" @close="activityStore.clearError" />

    <div class="card">
      <form @submit.prevent="handleSubmit">
        <div class="grid grid-2">
          <div>
            <div class="form-group">
              <label for="nameActivity">Nombre de la Actividad *</label>
              <input id="nameActivity" v-model="form.nameActivity" type="text" class="form-control" required
                :disabled="activityStore.loading">
            </div>

            <div class="form-group">
              <label for="typeActivity">Tipo de Actividad *</label>
              <select id="typeActivity" v-model="form.typeActivity" class="form-control" required
                :disabled="activityStore.loading">
                <option value="">Seleccionar tipo</option>
                <option value="ACADEMICA">Académica</option>
                <option value="DEPORTIVA">Deportiva</option>
                <option value="ARTISTICA">Artística</option>
                <option value="RELIGIOSA">Religiosa</option>
                <option value="DIRIGENCIA_ESTUDIANTIL">Dirigencia Estudiantil</option>
                <option value="RESPONSABILIDAD_SOCIAL">Responsabilidad Social</option>
              </select>
            </div>

            <div class="form-group">
              <label for="organizer">Organizador *</label>
              <input id="organizer" v-model="form.organizer" type="text" class="form-control" required
                :disabled="activityStore.loading">
            </div>

            <div class="form-group">
              <label for="description">Descripción</label>
              <textarea id="description" v-model="form.description" class="form-control" rows="4"
                :disabled="activityStore.loading"></textarea>
            </div>
          </div>

          <div>
            <div class="form-group">
              <label for="activityDate">Fecha de la Actividad *</label>
              <input id="activityDate" v-model="form.activityDate" type="date" class="form-control" required
                :min="tomorrow" :disabled="activityStore.loading">
            </div>

            <div class="grid grid-2">
              <div class="form-group">
                <label for="startTime">Hora de Inicio *</label>
                <input id="startTime" v-model="form.startTime" type="time" class="form-control" required
                  :disabled="activityStore.loading">
              </div>

              <div class="form-group">
                <label for="endTime">Hora de Fin *</label>
                <input id="endTime" v-model="form.endTime" type="time" class="form-control" required
                  :disabled="activityStore.loading">
              </div>
            </div>

            <div class="form-group">
              <label for="maximumEnrollDate">Fecha Límite de Inscripción *</label>
              <input id="maximumEnrollDate" v-model="form.maximumEnrollDate" type="date" class="form-control" required
                :min="today" :max="form.activityDate" :disabled="activityStore.loading">
            </div>

            <div class="form-group">
              <label for="maximumQuotas">Cupos Máximos *</label>
              <input id="maximumQuotas" v-model="form.maximumQuotas" type="number" class="form-control" required min="1"
                :disabled="activityStore.loading">
            </div>
          </div>
        </div>

        <div class="d-flex gap-2">
          <button type="submit" class="btn btn-primary" :disabled="activityStore.loading">
            {{ activityStore.loading ? 'Creando...' : 'Crear Actividad' }}
          </button>

          <router-link to="/admin/activities" class="btn btn-secondary">
            Cancelar
          </router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useActivityStore } from '@/stores/activity.store'
import type { CreateActivityRequest, ActivityType } from '@/types'
import ErrorAlert from '@/components/common/ErrorAlert.vue'

const router = useRouter()
const activityStore = useActivityStore()

const today = computed(() => {
  const now = new Date()
  return now.toISOString().split('T')[0]
})

const tomorrow = computed(() => {
  const now = new Date()
  now.setDate(now.getDate() + 1)
  return now.toISOString().split('T')[0]
})

const form = reactive<CreateActivityRequest>({
  nameActivity: '',
  activityDate: '',
  typeActivity: '' as ActivityType,
  startTime: '',
  endTime: '',
  maximumEnrollDate: '',
  maximumQuotas: 1,
  organizer: '',
  description: '',
  activityPicture: ''
})

async function handleSubmit() {
  const success = await activityStore.createActivity(form)

  if (success) {
    router.push('/admin/activities')
  }
}
</script>