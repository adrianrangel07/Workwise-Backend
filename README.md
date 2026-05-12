🧩 Workwise – Backend Oficial
<p align="center"> <img src="https://github.com/user-attachments/assets/e6c1a1d1-cc19-4443-a9b1-bf43379d633c" alt="Workwise Logo" width="200"/> </p><p align="center"> Plataforma de gestión de empleo para la ciudad de Cartagena <br> <strong>Conectando talento local con oportunidades laborales</strong> </p><p align="center"> <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" /> <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" /> <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" /> <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" /> <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white" />
</br>

📋 Descripción General
Workwise es una plataforma web integral diseñada para facilitar la conexión entre empresas y talento local en la ciudad de Cartagena, contribuyendo directamente al Objetivo de Desarrollo Sostenible #8: Trabajo decente y crecimiento económico.

Este repositorio contiene el backend completo de la aplicación, desarrollado con tecnologías empresariales modernas que garantizan escalabilidad, seguridad y alto rendimiento. El sistema proporciona una API REST robusta que soporta todas las operaciones del ecosistema Workwise.

🎯 Propósito del Backend
Proveer servicios esenciales para la operación segura y eficiente de la plataforma mediante:

API RESTful para comunicación con frontend Angular

Gestión centralizada de usuarios, ofertas y postulaciones

Sistema de autenticación robusto y seguro

Integraciones inteligentes con servicios externos

Arquitectura escalable preparada para crecimiento

✨ Características Principales
🔐 Módulo de Autenticación y Seguridad
Característica	Descripción	Tecnología
Autenticación JWT	Tokens seguros con expiración configurable	Spring Security + JWT
Roles Múltiples	Sistema granular de permisos	Spring Security
Registro Verificado	Validación por correo electrónico	JavaMailSender
CORS Configurado	Acceso controlado desde frontend	WebConfig
Encriptación	Datos sensibles protegidos	BCrypt
👥 Gestión de Usuarios
Tipo de Usuario	Permisos	Características Especiales
👤 Candidato	Básicos	Perfil profesional, CV, habilidades, historial laboral
🏢 Empresa	Intermedios	Publicación de ofertas, gestión de postulaciones, dashboard
🛡️ Administrador	Totales	Gestión de usuarios, moderación, estadísticas globales
💼 Sistema de Ofertas Laborales

Publicación Inteligente: Formularios guiados para creación de vacantes

Filtros Avanzados: Búsqueda por categoría, salario, experiencia y ubicación


Estados Múltiples: Abierta, Cerrada, En revisión

Estadísticas: Visualizaciones para empresas y/o personas

📄 Sistema de Postulaciones
text
Flujo de Postulación:
1. Candidato explora ofertas → 2. Aplica con un click → 3. Sistema notifica a empresa
4. Empresa revisa perfil → 5. Cambia estado de postulación → 6. Candidato recibe actualizaciones

🧠 Sistema de Recomendaciones Inteligentes
Matchmaking Automático: Algoritmo de compatibilidad candidato-oferta

Ranking Personalizado: Puntuación basada en múltiples factores

Aprendizaje Continuo: Mejora basada en interacciones de usuarios

🛠️ Tecnologías Utilizadas
🔧 Backend Principal
<p> <img src="https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white" /> 
    <img src="https://img.shields.io/badge/Spring%20Boot-3.1.5-6DB33F?logo=springboot&logoColor=white" /> 
    <img src="https://img.shields.io/badge/Spring%20Security-6.1.5-6DB33F?logo=springsecurity&logoColor=white" /> 
    <img src="https://img.shields.io/badge/JPA/Hibernate-6.2.4-59666C?logo=hibernate&logoColor=white" /> </p>
🗄️ Base de Datos y Persistencia
<p> <img src="https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white" /> </p>
🔐 Seguridad y Autenticación
<p> <img src="https://img.shields.io/badge/JWT-0.11.5-000000?logo=jsonwebtokens&logoColor=white" /> <img src="https://img.shields.io/badge/BCrypt-0.10.1-004D40" /> </p>
🛠️ Herramientas de Desarrollo
<p> <img src="https://img.shields.io/badge/Docker-24.0-2496ED?logo=docker&logoColor=white" /> 
    <img src="https://img.shields.io/badge/Lombok-1.18.30-A50034" /> 
    <img src="https://img.shields.io/badge/Vercel-Deploy-000000?logo=vercel&logoColor=white" /> </p>

📊 Base de Datos - Modelo Relacional

🔧 Configuración y Despliegue
Requisitos Previos
Java JDK 17+

MySQL 8.0+

Docker 24.0+

Maven 3.9+

workwise-backend

🚀 Roadmap de Desarrollo
Q1 2025 🟢 En Progreso
Arquitectura base y autenticación

CRUD de usuarios y ofertas

Sistema de postulaciones básico

Pruebas unitarias y de integración

Optimización de consultas SQL

Sistema de logging centralizado

Q2 2025 🟡 Planificado
Motor de búsqueda avanzado 

Dashboard de analytics con power BI

Q3 2025 🔵 Futuro
Microservicios especializados

App móvil nativa

Mercado de habilidades especializadas

Programa de certificaciones

🛡️ Consideraciones de Seguridad
Medidas Implementadas
Autenticación JWT con refresh tokens

BCrypt para hashing de contraseñas

Validación de entrada en todos los endpoints

CORS configurado específicamente para el frontend

Protección contra CSRF y XSS

Rate limiting en endpoints sensibles

Encriptación de datos sensibles en base de datos

📞 Soporte y Contacto
Canales de Comunicación
Reporte de Issues: GitHub Issues

Discusiones: GitHub Discussions

Respuesta a Incidentes
Nivel	Tiempo de Respuesta	Canal Preferido
Crítico (Sistema caído)	< 1 hora	Email + WhatsApp
Alto (Funcionalidad principal)	< 4 horas	Email
Medio (Mejora o bug menor)	< 24 horas	GitHub Issues
Bajo (Consulta general)	< 72 horas	GitHub Discussions

👨‍💻 Autor
<p align="center"> <strong>Adrian Rangel</strong> <br> <em>Desarrollador Full Stack – Colombia</em> </p><p align="center"> <a href="https://github.com/adrianrangel07"> <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" /> </a> <a href="https://www.linkedin.com/in/adrian-de-jesus-rangel-vides-2150311ab/"> <img src="https://img.shields.io/badge/LinkedIn-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white" /> </a> </p>
Stack Principal: Java | Spring Boot | Angular | Docker | MySQL 

<p align="center"> <em>✨ Conectando el talento cartagenero con las oportunidades del mañana ✨</em> </p><p align="center"> <img src="https://img.shields.io/badge/🇨🇴-Hecho%20en%20Cartagena-yellow" /> <img src="https://img.shields.io/badge/🚀-Producción%20Ready-green" /> </p>

🔗 **Visita el Frontend del Proyecto:**  
[![Front WorkWise](https://img.shields.io/badge/Front--WorkWise-Visit-0366d6?logo=github&logoColor=white)]([https://github.com/adrianrangel07/Front-WorkWise](https://github.com/yonierva/Front-WorkWise)

hola
