<template>
  <div>
    <div class="card">
      <h1>Bienvenido al Sistema de Gestión de Actividades Extracurriculares</h1>
      <p class="text-muted">
        <br>
        ¡Bienvenido/a al Sistema de Gestión de Actividades Extracurriculares de la Universidad! Esta plataforma está diseñada para centralizar y facilitar la participación, organización y administración de actividades complementarias a la formación académica universitaria. Si eres estudiante, aquí podrás explorar eventos, talleres, deportes y más. Nuestro objetivo es enriquecer la experiencia universitaria promoviendo el desarrollo integral a través de diversas opciones formativas y recreativas.
      </p>
      
      <div class="d-flex gap-2" style="margin-top: 20px;">
        <router-link v-if="!authStore.isAuthenticated" to="/login" class="btn btn-primary">
          Iniciar Sesión
        </router-link>
      </div>
    </div>
    <div class="cards-roles">
      <div class="card">
        <h1>Administrador usuarios</h1>
        <p class="text-muted">
          <br>
          Como administrador de usuarios, tienes acceso a gestionar los perfiles y permisos de todos los participantes del sistema. Podrás crear y habilitar a nuevos estudiantes o administradores, asignar roles, y verificar datos relevantes. Tu labor es fundamental para mantener la seguridad y organización de la plataforma, asegurando que cada usuario tenga las herramientas adecuadas según sus funciones dentro de la universidad.
        </p>
      </div>
      <div class="card">
        <h1>Administrador actividades</h1>
        <p class="text-muted">
          <br>
          En este rol, eres responsable de crear y gestionar todas las actividades extracurriculares disponibles en el sistema. Podrás definir horarios, cupos y categorías. Tu trabajo garantiza que la oferta de actividades sea variada, de calidad y alineada con los objetivos institucionales.
        </p>
      </div>
      <div class="card">
        <h1>Estudiantes</h1>
        <p class="text-muted">
          <br>
          Como estudiante, este sistema te permite explorar y inscribirte en actividades extracurriculares que se ajusten a tus intereses y horarios. Podrás ver detalles de cada evento y gestionar tu participación. Además, podrás llevar un registro de tus actividades inscritas, ayudándote a construir un currículum más completo durante tu vida universitaria.
        </p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '@/stores/auth.store'

const authStore = useAuthStore()
</script>

<style scoped>
.cards-roles {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

.cards-roles .card {
  flex: 1;
  min-width: 0;
}
</style>