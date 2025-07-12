<template>
  <div class="activity-card" @click="$emit('click')">
    <div class="activity-type" :class="`type-${activity.typeActivity.toLowerCase()}`">
      {{ formatActivityType(activity.typeActivity) }}
    </div>
    
    <h3>{{ activity.nameActivity }}</h3>
    
    <div class="small text-muted mb-3">
      <div><strong>Fecha:</strong> {{ formatDate(activity.activityDate) }}</div>
      <div><strong>Hora:</strong> {{ activity.startTime }} - {{ activity.endTime }}</div>
      <div><strong>Organizador:</strong> {{ activity.organizer }}</div>
    </div>
    
    <p class="text-muted" v-if="activity.description">
      {{ activity.description.substring(0, 100) }}{{ activity.description.length > 100 ? '...' : '' }}
    </p>
    
    <div class="d-flex justify-content-between align-items-center" style="margin-top: 15px;">
      <div class="small">
        <strong>{{ activity.currentEnrollments }}/{{ activity.maximumQuotas }}</strong> inscritos
      </div>
      
      <div class="d-flex gap-2">
        <span v-if="activity.canEnroll" class="btn btn-success small">
          Disponible
        </span>
        <span v-else-if="activity.isCompleted" class="btn btn-secondary small">
          Completada
        </span>
        <span v-else-if="!activity.isEnabled" class="btn btn-danger small">
          Deshabilitada
        </span>
        <span v-else class="btn btn-secondary small">
          No disponible
        </span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Activity } from '@/types'

interface Props {
  activity: Activity
}

defineProps<Props>()
defineEmits<{
  click: []
}>()

function formatActivityType(type: string): string {
  const types: Record<string, string> = {
    'ACADEMICA': 'Académica',
    'DEPORTIVA': 'Deportiva',
    'ARTISTICA': 'Artística',
    'RELIGIOSA': 'Religiosa',
    'DIRIGENCIA_ESTUDIANTIL': 'Dirigencia Estudiantil',
    'RESPONSABILIDAD_SOCIAL': 'Responsabilidad Social'
  }
  return types[type] || type
}

function formatDate(dateString: string): string {
  const date = new Date(dateString)
  return date.toLocaleDateString('es-ES', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}
</script>