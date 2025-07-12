# Instrucciones de Uso:

1) Importae el JSON en Postman.
2) Configurae la variable jwt_token:
    - Ir a la pestaña "Variables" de la colección.
    - Reemplazar your_jwt_token_here con el JWT que se obtenga del login.

# Flujo recomendado:

1) Primero registrar usuarios con diferentes roles.
2) Hacer login para obtener el JWT (hacer login con un usuario y probar sus endpoints correspondientes, luego los demás).
3) Copiar el JWT del response y actualizar la variable (cada vez que se logee otro usario).
4) Probar los endpoints asociados a cada usuario.

## Endpoints

AUTH SERVICE (8080)
POST /auth/sign-up - Register user - Body: username, password, roleRequest
POST /auth/log-in - Login user - Body: username, password
POST /auth/validate-token - Validate JWT - Header: Authorization
GET /auth/user/{username} - Get user info - Path: username

USER SERVICE (8081)
GET /users - Get all users - Header: Authorization
GET /users/{username} - Get user by username - Header: Authorization, Path: username
POST /users - Create user profile - Header: Authorization, Body: username, email, role, profilePicture
PUT /users/{username} - Update user profile - Header: Authorization, Path: username, Body: email, profilePicture
DELETE /users/{username} - Delete user - Header: Authorization, Path: username
PATCH /users/{username}/enable - Enable user - Header: Authorization, Path: username
PATCH /users/{username}/disable - Disable user - Header: Authorization, Path: username
GET /users/role/{role} - Get users by role - Header: Authorization, Path: role

ACTIVITY SERVICE (8082)
GET /activities - Get all activities - Header: Authorization (optional)
GET /activities/{id} - Get activity by ID - Path: id
GET /activities/type/{type} - Get activities by type - Path: type
POST /activities - Create activity - Header: Authorization, Body: nameActivity, activityDate, typeActivity, startTime, endTime, maximumEnrollDate, maximumQuotas, organizer, description, activityPicture
PUT /activities/{id} - Update activity - Header: Authorization, Path: id, Body: (any activity fields)
DELETE /activities/{id} - Delete activity - Header: Authorization, Path: id
PATCH /activities/{id}/enable - Enable activity - Header: Authorization, Path: id
PATCH /activities/{id}/disable - Disable activity - Header: Authorization, Path: id

ENROLLMENT SERVICE (8082)
GET /enrollments/student/{username} - Get student enrollments - Header: Authorization, Path: username
GET /enrollments/activity/{activityId} - Get activity enrollments - Header: Authorization, Path: activityId
POST /enrollments/activity/{activityId} - Enroll student - Header: Authorization, Path: activityId
DELETE /enrollments/activity/{activityId} - Unenroll student - Header: Authorization, Path: activityId
GET /enrollments/activity/{activityId}/check/{username} - Check enrollment - Header: Authorization, Path: activityId, username

REPORT SERVICE (8083)
POST /reports/activity-completion - Create completion report - Body: activityId, activityName, activityDate, activityType, organizer, completedStudents, totalCompletedStudents, completionDate
GET /reports - Get all reports - Header: Authorization
GET /reports/{id} - Get report by ID - Header: Authorization, Path: id
GET /reports/activity/{activityId} - Get report by activity ID - Header: Authorization, Path: activityId
GET /reports/type/{activityType} - Get reports by type - Header: Authorization, Path: activityType
GET /reports/date-range - Get reports by date range - Header: Authorization, Query: startDate, endDate
GET /reports/pending-external-api - Get pending external API reports - Header: Authorization
POST /reports/{id}/retry-external-api - Retry external API send - Header: Authorization, Path: id