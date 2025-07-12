<template>
  <div v-if="error" class="alert alert-error">
    {{ error }}
    <button @click="$emit('close')" style="float: right; background: none; border: none; font-size: 18px; cursor: pointer;">
      ×
    </button>
  </div>
</template>

<script setup lang="ts">
interface Props {
  error: string | null
}

defineProps<Props>()
defineEmits<{
  close: []
}>()
</script>