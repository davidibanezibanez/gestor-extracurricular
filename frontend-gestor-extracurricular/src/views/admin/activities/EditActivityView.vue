<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h1>Editar Actividad</h1>

      <router-link to="/admin/activities" class="btn btn-secondary">
        Volver
      </router-link>
    </div>

    <LoadingSpinner v-if="activityStore.loading && !activity" />

    <ErrorAlert :error="activityStore.error" @close="activityStore.clearError" />

    <div v-if="activity" class="card">
      <form @submit.prevent="handleSubmit">
        <div class="grid grid-2">
          <div>
            <div class="form-group">
              <label for="nameActivity">Nombre de la Actividad *</label>
              <input id="nameActivity" v-model="form.nameActivity" type="text" class="form-control"
                :disabled="updating">
            </div>

            <div class="form-group">
              <label for="typeActivity">Tipo de Actividad *</label>
              <select id="typeActivity" v-model="form.typeActivity" class="form-control" :disabled="updating">
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
              <input id="organizer" v-model="form.organizer" type="text" class="form-control" :disabled="updating">
            </div>

            <div class="form-group">
              <label for="description">Descripción</label>
              <textarea id="description" v-model="form.description" class="form-control" rows="4"
                :disabled="updating"></textarea>
            </div>
          </div>

          <div>
            <div class="form-group">
              <label for="activityDate">Fecha de la Actividad *</label>
              <input id="activityDate" v-model="form.activityDate" type="date" class="form-control"
                :disabled="updating">
            </div>

            <div class="grid grid-2">
              <div class="form-group">
                <label for="startTime">Hora de Inicio *</label>
                <input id="startTime" v-model="form.startTime" type="time" class="form-control" :disabled="updating">
              </div>

              <div class="form-group">
                <label for="endTime">Hora de Fin *</label>
                <input id="endTime" v-model="form.endTime" type="time" class="form-control" :disabled="updating">
              </div>
            </div>

            <div class="form-group">
              <label for="maximumEnrollDate">Fecha Límite de Inscripción *</label>
              <input id="maximumEnrollDate" v-model="form.maximumEnrollDate" type="date" class="form-control"
                :disabled="updating">
            </div>

            <div class="form-group">
              <label for="maximumQuotas">Cupos Máximos *</label>
              <input id="maximumQuotas" v-model="form.maximumQuotas" type="number" class="form-control" min="1"
                :disabled="updating">
            </div>
          </div>
        </div>

        <div class="d-flex gap-2">
          <button type="submit" class="btn btn-primary" :disabled="updating">
            {{ updating ? 'Guardando...' : 'Guardar Cambios' }}
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
import { reactive, ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useActivityStore } from '@/stores/activity.store'
import type { UpdateActivityRequest } from '@/types'
import LoadingSpinner from '@/components/common/LoadingSpinner.vue'
import ErrorAlert from '@/components/common/ErrorAlert.vue'

const route = useRoute()
const router = useRouter()
const activityStore = useActivityStore()

const updating = ref(false)

const activity = computed(() => activityStore.currentActivity)

const form = reactive<UpdateActivityRequest>({
  nameActivity: '',
  activityDate: '',
  typeActivity: undefined,
  startTime: '',
  endTime: '',
  maximumEnrollDate: '',
  maximumQuotas: 1,
  organizer: '',
  description: '',
  activityPicture: ''
})

function populateForm() {
  if (activity.value) {
    form.nameActivity = activity.value.nameActivity
    form.activityDate = activity.value.activityDate
    form.typeActivity = activity.value.typeActivity
    form.startTime = activity.value.startTime
    form.endTime = activity.value.endTime
    form.maximumEnrollDate = activity.value.maximumEnrollDate
    form.maximumQuotas = activity.value.maximumQuotas
    form.organizer = activity.value.organizer
    form.description = activity.value.description || ''
    form.activityPicture = activity.value.activityPicture || ''
  }
}

async function handleSubmit() {
  if (!activity.value) return

  updating.value = true
  const success = await activityStore.updateActivity(activity.value.id, form)

  if (success) {
    router.push('/admin/activities')
  }

  updating.value = false
}

onMounted(async () => {
  const activityId = Number(route.params.id)
  await activityStore.fetchActivityById(activityId)
  populateForm()
})
</script>