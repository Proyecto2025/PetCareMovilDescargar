# PetCare – Aplicación Android en Kotlin

PetCare es una aplicación móvil desarrollada en **Kotlin + Jetpack Compose**, conectada a una API REST en Spring Boot.  
Permite gestionar publicaciones de adopción, ayuda y extravíos, así como consejos sobre el cuidado de animales.

## Índice
1. [Funcionalidades principales](#funcionalidades-principales)
2. [Arquitectura de la aplicación](#arquitectura-de-la-aplicación)
3. [Tecnologías utilizadas](#tecnologías-utilizadas)

---

# Funcionalidades principales

### Autenticación
- Registro de usuario  
- Inicio de sesión  
- Mantener sesión iniciada (DataStore)  
- Edición de datos personales  
- Cambio de contraseña  
- Cerrar sesión  
- Eliminar cuenta  

### Publicaciones (Posts)
- Crear post (adopción, ayuda, extravío)  
- Subir imagen desde galería  
- Filtrar por:
  - Tipo de publicación  
  - Tipo de animal  
  - Provincia  
  - Municipio  
- Ver detalles completos del post  

### Consejos
- Listado de consejos  
- Filtrar por categoría  
- Ver detalles  

### Perfil
- Ver posts y consejos creados  
- Menú lateral con opciones de seguridad y ajustes  

---

# Arquitectura de la aplicación

La app sigue una arquitectura **MVVM** con separación clara entre capas:

data/
- api/        : Retrofit services, DTOs, requests
- repository/ : Lógica de acceso a datos
- local/      : DataStore, providers
- model/      : Modelos internos (UI models)
- mapper/     : Transformación DTO a Modelo interno

ui/
- post/        : Pantallas de posts
- advice/      : Pantallas de consejos
- user/        : Login, registro, perfil
- components/  : Componentes reutilizables
- screens/     : Navegación principal
- viewModel/   : Lógica de presentación
- navigation/  : Navigation Compose
- theme/       : Estilos y colores

util/
- Config       : Claves API externas (Geoapify)



### Beneficios de esta arquitectura
- Código limpio y mantenible  
- UI desacoplada del backend  
- Modelos internos independientes de los DTOs  

---

# Tecnologías utilizadas

- **Kotlin**
- **Jetpack Compose**
- **ViewModel + StateFlow**
- **Retrofit + Kotlinx Serialization**
- **DataStore**
- **Navigation Compose**
- **Coil** (carga de imágenes)
- **Geoapify** (autocompletado de provincias y municipios)
- **Cloudinary** (subida de imágenes)
- **SonarQube** (análisis de calidad)




