

🧩 Workwise – Backend Oficial 
Plataforma de gestión de empleo para la ciudad de Cartagena
Desarrollada con Java + Spring Boot + MySQL + Docker
<p align="center"> <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=coffeescript&logoColor=white" /> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" /> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" /> <img src="https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white" /> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" /> <img src="https://img.shields.io/badge/Vercel-000000?style=for-the-badge&logo=vercel&logoColor=white" /> </p>
📘 Descripción General

Workwise es una plataforma web que facilita la conexión entre empresas y personas en la ciudad de Cartagena, promoviendo el Objetivo de Desarrollo Sostenible #8 (Trabajo decente y crecimiento económico).

El backend provee todos los servicios esenciales para la operación segura, rápida y eficiente del sistema, permitiendo:

Gestión de usuarios (personas y empresas)

Procesos de autenticación y autorización vía JWT

Administración de ofertas laborales

Envío y seguimiento de postulaciones

Integración con chatbot para soporte inteligente

Manejo de roles: candidato, empresa, administrador

Procesamiento de CV, validaciones y flujos de selección

Recomendaciones personalizadas de empleos

Es un backend robusto, modular y escalable, construido en Java + Spring Boot, pensado para integrarse con el frontend de Angular, alojado por separado.

🎯 Objetivo del Proyecto

Aportar a la empleabilidad en Cartagena mediante una plataforma de acceso fácil que:

Conecte rápidamente empresas con talento local

Permita a las personas encontrar oportunidades compatibles con su perfil

Genere un proceso de selección transparente y ágil

Ofrezca recursos profesionales para mejorar la empleabilidad

⚙️ Características Principales del Backend
🔐 Módulo de Autenticación

Registro de candidatos y empresas

Login seguro

Autenticación JWT

Recuperación y verificación de cuenta por correo

Roles y permisos:

👤 Candidato

🏢 Empresa

🛡️ Administrador

🧑‍💼 Gestión de Usuarios

Perfiles completos para candidatos

Perfiles empresariales

Subida, actualización y validación de CV

Gestión de habilidades y categorías profesionales

Información laboral y académica

💼 Gestión de Ofertas

Creación, edición y eliminación de vacantes

Filtros avanzados por categoría, salario, ubicación

Estadísticas por empresa

Estado de ofertas: abierta, cerrada, pausada

📄 Postulaciones

Aplicación directa a una oferta

Seguimiento de estado

Procesos de selección

Historial del candidato

Gestión empresarial de postulaciones

🤖 Chatbot Integrado

El backend se comunica con un chatbot inteligente que:

Asiste a candidatos en la búsqueda de empleo

Responde dudas comunes

Recomienda recursos y ofertas

Guía a empresas durante la publicación de vacantes

🧠 Sistema Inteligente

Incluye algoritmos que permiten:

Matchmaking inteligente entre candidato y oferta

Recomendación automática de empleos según perfil

Ranking de compatibilidad

Análisis de CV (estructura, palabras clave, habilidades)

Detección de posibles coincidencias de habilidades y categorías

📚 Recursos Formativos

Workwise ofrece un apartado único de recursos:

Videos

PDFs

Tips profesionales

Guías para entrevistas

Consejos para mejorar el CV

Esto hace que Workwise vaya más allá de un simple portal de empleo.

🧬 Arquitectura del Sistema
Frontend Angular  →  Workwise Backend (API REST)  →  MySQL
                           ↓
                         Chatbot
                           ↓
                        Servicios externos


El backend es un monolito modular, separado del frontend, pero estructurado por capas:

controller/
service/
repository/
model/
config/
security/

🗄️ Base de Datos (MySQL)
Entidades Principales

Usuario

Persona

Empresa

Oferta

Postulacion

Habilidad

CategoriaProfesional

VerificacionEmail

(y otras entidades de apoyo)

Relaciones:

Persona ↔ Usuario

Empresa ↔ Ofertas

Persona ↔ Postulaciones ↔ Ofertas

Ofertas ↔ Habilidades

Categorías ↔ Personas / Ofertas

🐳 Despliegue
Docker

El sistema está completamente contenerizado:

docker build -t workwise-backend .
docker run -p 8080:8080 workwise-backend

Producción

Backend desplegado mediante contenedores

Frontend alojado en Vercel

🧪 Instalación en Local
1. Clonar el repositorio
git clone https://github.com/adrianrangel07/Workwise-Backend.git

2. Instalar dependencias
./mvnw clean install

3. Ejecutar
./mvnw spring-boot:run

📈 Roadmap Profesional
Q1 – 2025

🔧 Mejoras en seguridad

🧪 Pruebas Unitarias y de Integración

🌐 Internacionalización

Q2 – 2025

🔎 Motor de búsqueda avanzado

🧠 IA para análisis de CV

🚀 Optimización de recomendaciones

Q3 – 2025

📨 Sistema de notificaciones por correo y WhatsApp

📊 Panel empresarial avanzado

🗄️ Módulo de analíticas

Q4 – 2025

🤝 Red de contactos

📱 App móvil (Ionic / Flutter)

🔌 Microservicios para escalabilidad

🔒 Seguridad Implementada

JWT para protección de endpoints

Roles y privilegios

CORS configurado

Validación de datos en API

Verificación por correo electrónico

Control total de sesiones

🤝 Contribución

Se aceptan contribuciones para:

Optimización del rendimiento

Nuevas funcionalidades

Mejoras en seguridad

Documentación

Testing

👨‍💻 Autor

Adrián Rangel
Desarrollador Full Stack – Cartagena, Colombia
📌 Java | Spring Boot | Angular | Docker | MySQL
🌐 GitHub: github.com/adrianrangel07

🎉 Gracias por revisar Workwise Backend
