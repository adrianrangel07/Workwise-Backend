package Proyectodeaula.Workwise.Chatbot;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

        private final Map<String, List<Map<String, Object>>> respuestas;
        private final Map<String, List<String>> sinonimos;
        private final Random random = new Random();

        public ChatbotService() {
                respuestas = new HashMap<>();
                sinonimos = new HashMap<>();

                inicializarSinonimos();
                inicializarRespuestas();
        }

        // ============================================================
        // 🔹 SINÓNIMOS / INTENCIONES
        // ============================================================
        private void inicializarSinonimos() {

                sinonimos.put("saludo", Arrays.asList(
                                "alo", "aloha", "buen día", "buenas", "buenas noches", "buenas tardes",
                                "buenos días", "hello", "hey", "hi", "hola", "holi", "holis",
                                "qué hubo", "qué pasa", "qué tal", "que hubo", "que pasa", "que tal",
                                "saludos"));

                sinonimos.put("login", Arrays.asList(
                                "abrir sesión", "abrir sesion", "acceder", "acceso", "continuar sesión", "entrar",
                                "entrar al sistema", "identificarme", "iniciar cuenta", "iniciar sesión",
                                "iniciar sesion",
                                "ingresar", "log in", "loguear", "necesito entrar", "sign in", "tengo cuenta",
                                "ya estoy registrado", "ya me registré", "ya me registre", "ya tengo cuenta"));

                sinonimos.put("registro", Arrays.asList(
                                "abrir cuenta", "afiliarme", "crear cuenta", "crear perfil", "crear perfil nuevo",
                                "crear usuario", "darme de alta", "hacer cuenta", "hacer perfil", "inscribirme",
                                "matricularme", "nuevo usuario", "registrar", "registrarme", "registrarse", "registro",
                                "sign up", "suscribirme", "unirme"));

                sinonimos.put("oferta", Arrays.asList(
                                "bolsa de trabajo", "buscar empleo", "buscar trabajo", "busco empleo",
                                "chamba", "convocatoria", "convocatorias", "empleo", "empleos", "encontrar trabajo",
                                "laburo", "necesito trabajo", "oferta", "ofertas", "oportunidad laboral",
                                "oportunidades", "puesto", "puestos", "trabajo", "trabajos",
                                "ver empleos", "ver ofertas", "ver vacantes", "vacante", "vacantes",
                                "quiero trabajar"));

                sinonimos.put("perfil", Arrays.asList(
                                "actualizar cuenta", "actualizar datos", "actualizar información",
                                "ajustes de cuenta", "cambiar datos", "configuración", "configurar perfil",
                                "datos personales", "editar información", "editar perfil", "gestionar cuenta",
                                "gestionar perfil", "información personal", "mi cuenta", "mi perfil",
                                "modificar perfil", "preferencias", "ver perfil"));

                sinonimos.put("información", Arrays.asList(
                                "asistencia", "ayuda", "capacidades", "como funcionas", "cómo funcionas",
                                "como me ayudas", "cómo me ayudas", "funciones", "help", "info", "información",
                                "para que sirves", "para qué sirves", "para qué me sirves", "que haces",
                                "qué haces", "que hay", "qué hay", "que puedo hacer", "qué puedo hacer",
                                "que puedes hacer", "qué puedes hacer", "que ofreces", "qué ofreces",
                                "qué es esto", "que es esto", "soporte"));

                sinonimos.put("despido", Arrays.asList(
                                "adios", "adiós", "bien", "bye", "chévere", "ciao", "de acuerdo", "entendido",
                                "excelente", "fantástico", "genial", "gracias", "hasta luego", "hasta pronto",
                                "listo", "me ayudaste", "me sirvió", "muchas gracias", "nos vemos", "ok",
                                "okey", "okeydokey", "perfecto", "super", "todo claro", "vale"));

                sinonimos.put("ayuda", Arrays.asList(
                                "ayuda", "ayudame", "ayúdame", "dame una guía", "explicame", "help",
                                "me ayudas", "me pierdo", "necesito ayuda", "necesito soporte",
                                "no comprendo", "no entiendo", "no se", "no sé", "no sé qué hacer",
                                "no sé usar", "puedes ayudarme", "qué hago", "que hago", "socorro",
                                "auxilio", "tengo dudas"));

                sinonimos.put("postulacion", Arrays.asList(
                                "aplicar", "aplicar oferta", "aplicación", "como me postulo", "cómo me postulo",
                                "como postular", "cómo postular", "donde postular", "dónde postular", "enviar cv",
                                "enviar hoja de vida", "enviar solicitud", "hacer postulacion", "hacer postulación",
                                "mandar cv", "necesito postularme", "postular", "postularme", "postulación",
                                "proceso de postulacion", "proceso de postulación", "registrar postulacion",
                                "registrar postulación", "quiero postularme"));

                sinonimos.put("hoja_vida", Arrays.asList(
                                "actualizar curriculum", "actualizar cv", "actualizar hoja de vida",
                                "actualizar documento", "adjuntar cv", "adjuntar hoja de vida", "adjuntar mi cv",
                                "cargar cv", "cargar hoja de vida", "cargar documento laboral", "crear cv",
                                "crear hoja de vida", "curriculum", "currículum", "cv", "descargar plantilla",
                                "editar cv", "editar hoja de vida", "enviar cv", "enviar hoja de vida",
                                "ejemplo de cv", "ejemplo de hoja de vida", "formato hoja de vida",
                                "hacer cv", "hacer hoja de vida", "hoja de vida", "hoja vida", "mandar cv",
                                "mandar hoja de vida", "mi cv", "mi hoja de vida", "modificar cv",
                                "modificar hoja de vida", "modelo hoja de vida", "plantilla cv",
                                "plantillas de hoja de vida", "subir archivo de cv", "subir cv",
                                "subir hoja de vida", "subir mi cv", "subir mi hoja de vida", "ver ejemplos de cv",
                                "ver plantillas"));

                sinonimos.put("tiempo_respuesta", Arrays.asList(
                                "cuando responden", "cuando sabré", "cuándo sabré", "cuánto se demoran",
                                "cuánto tardan", "demora empresa", "demora en responder", "estado postulación",
                                "me avisarán", "me van a responder", "respuesta empresa", "respuesta postulación",
                                "tardan en responder", "tiempo de espera", "tiempo de respuesta", "tiempo espera"));

                sinonimos.put("guias_entrevista", Arrays.asList(
                                "ayuda entrevista", "como ir a entrevista", "cómo ir a entrevista",
                                "cómo responder entrevista", "consejos de entrevista", "entrevista laboral",
                                "guia entr      evista", "guía de entrevista", "guías de entrevista",
                                "manual entrevista", "preparar entrevista", "prepararme para entrevista",
                                "preguntas entrevista", "recomendaciones entrevista", "simulacro entrevista",
                                "tips de entrevista", "ver consejos", "ver guías", "ver tips"));

                // 📊 NUEVO: DATOS / ESTADÍSTICAS
                sinonimos.put("datos", Arrays.asList(
                                "estadísticas", "estadisticas", "datos", "informes", "gráficas", "graficas",
                                "números", "numeros", "porcentajes", "tasas", "datos del desempleo",
                                "información estadística", "información de desempleo", "estadísticas laborales",
                                "estadísticas de empleo", "datos laborales", "datos sobre empleo",
                                "estadísticas de la página", "datos de la plataforma", "rendimiento de la página",
                                "informes de trabajo", "estadísticas de usuarios", "datos de registro",
                                "datos de uso", "tasa de desempleo", "gráficos de empleo",
                                "estadísticas de Cartagena"));

                // 🧑‍💻 NUEVO: SOBRE NOSOTROS / CREADORES
                sinonimos.put("sobre_nosotros", Arrays.asList(
                                "acerca de", "acerca de workwise", "creadores", "desarrolladores",
                                "equipo de trabajo", "fundadores", "historia", "información del proyecto",
                                "información sobre ustedes", "origen", "quiénes son", "quienes son",
                                "quiénes crearon esto", "quién lo hizo", "quien lo hizo", "sobre la plataforma",
                                "sobre la empresa", "sobre nosotros", "workwise team", "workwise cartagena",
                                "quién desarrolló esto", "quien desarrolló esto", "quién está detrás",
                                "quien está detrás"));

                // Normaliza todos los sinónimos agregados
                for (var entry : sinonimos.entrySet()) {
                        List<String> normalizados = entry.getValue().stream()
                                        .map(this::normalizarTexto)
                                        .toList();
                        sinonimos.put(entry.getKey(), normalizados);
                }

        }

        // ============================================================
        // 🔹 RESPUESTAS ALEATORIAS / DINÁMICAS
        // ============================================================
        private void inicializarRespuestas() {

                // 👋 SALUDO
                respuestas.put("saludo", List.of(
                                crearRespuesta("¡Hola! 👋 Bienvenido a *Workwise Cartagena* 🌴. ¿Qué deseas hacer hoy?",
                                                botonesMenuPrincipal()),
                                crearRespuesta("¡Hola! 😊 Soy tu asistente virtual laboral. Puedo ayudarte a registrarte, postularte o explorar empleos en Cartagena.",
                                                botonesMenuPrincipal()),
                                crearRespuesta("¡Hey! 👋 ¿Listo para encontrar tu próxima oportunidad laboral en Cartagena? 💼",
                                                botonesMenuPrincipal()),
                                crearRespuesta("¡Bienvenido de nuevo! 😄 ¿Quieres revisar tus postulaciones o ver nuevas ofertas?",
                                                botonesMenuPrincipal()),
                                crearRespuesta("¡Hola! 🙌 Estoy aquí para ayudarte a conectar con oportunidades laborales en Cartagena. ¿Por dónde empezamos?",
                                                botonesMenuPrincipal())));

                // 🔐 LOGIN
                respuestas.put("login", List.of(
                                crearRespuesta("Para iniciar sesión, haz clic en el botón de abajo 👇",
                                                List.of(crearBoton("Ir al login", "abrir_login"))),
                                crearRespuesta("¿Ya tienes una cuenta? Perfecto 😎, inicia sesión aquí:",
                                                List.of(crearBoton("Iniciar sesión", "abrir_login"))),
                                crearRespuesta("🔑 Accede a tu cuenta para seguir tu proceso de empleo:",
                                                List.of(crearBoton("Ir al login", "abrir_login"))),
                                crearRespuesta("Recuerda que iniciar sesión te permite postularte y actualizar tu perfil fácilmente. 👇",
                                                List.of(crearBoton("Abrir login", "abrir_login")))));

                // 📝 REGISTRO
                respuestas.put("registro", List.of(
                                crearRespuesta("¿Deseas registrarte como persona o empresa?",
                                                List.of(crearBoton("🧍 Persona", "abrir_registro_persona"),
                                                                crearBoton("🏢 Empresa", "abrir_registro_empresa"))),
                                crearRespuesta("¡Genial! 💪 Regístrate para acceder a todas las ofertas laborales disponibles en Cartagena.",
                                                List.of(crearBoton("Registrarme como persona",
                                                                "abrir_registro_persona"))),
                                crearRespuesta("🎯 Para comenzar, necesitas crear una cuenta. ¿Cómo quieres registrarte?",
                                                List.of(crearBoton("🧍 Persona", "abrir_registro_persona"),
                                                                crearBoton("🏢 Empresa", "abrir_registro_empresa"))),
                                crearRespuesta("Crear una cuenta es rápido y gratuito ✅. Elige el tipo de usuario y comienza tu búsqueda de empleo.",
                                                List.of(crearBoton("Persona", "abrir_registro_persona"),
                                                                crearBoton("Empresa", "abrir_registro_empresa")))));

                // 💼 OFERTAS DE TRABAJO
                respuestas.put("oferta", List.of(
                                crearRespuesta("Aquí puedes explorar nuestras ofertas laborales disponibles en Cartagena 👇",
                                                List.of(crearBoton("Ver ofertas", "abrir_ofertas"))),
                                crearRespuesta("Tenemos oportunidades laborales en sectores como turismo, tecnología, construcción, servicios y más 🏗️💻. ¡Echa un vistazo!",
                                                List.of(crearBoton("Ver todas las ofertas", "abrir_ofertas"))),
                                crearRespuesta("💼 En Cartagena hay empresas buscando talento como el tuyo. ¡Explora las vacantes disponibles!",
                                                List.of(crearBoton("Ver empleos", "abrir_ofertas"))),
                                crearRespuesta("🚀 Cada semana se actualizan las vacantes con nuevas oportunidades. Te recomiendo revisar constantemente.",
                                                List.of(crearBoton("Explorar ofertas", "abrir_ofertas"))),
                                crearRespuesta("📋 Puedes filtrar las ofertas por área, salario, modalidad o experiencia. ¡Encuentra la ideal para ti!",
                                                List.of(crearBoton("Buscar empleos", "abrir_ofertas")))));

                // 👤 PERFIL Y GESTIÓN DE CUENTA
                respuestas.put("perfil", List.of(
                                crearRespuesta("Puedes actualizar tu información personal desde aquí 👇",
                                                List.of(crearBoton("Ir a mi perfil", "abrir_perfil"))),
                                crearRespuesta("Desde tu perfil puedes cambiar tus datos, cargar tu hoja de vida y mejorar tu visibilidad ante empresas. ✨",
                                                List.of(crearBoton("Editar perfil", "abrir_perfil"))),
                                crearRespuesta("👤 Gestiona tu perfil para destacar frente a los reclutadores.",
                                                List.of(crearBoton("Abrir perfil", "abrir_perfil"))),
                                crearRespuesta("📂 Para subir tu hoja de vida, entra a tu perfil y usa la opción *'Cargar CV'*. Así las empresas podrán conocerte mejor.",
                                                List.of(crearBoton("Subir hoja de vida", "abrir_perfil"))),
                                crearRespuesta("🔐 Si necesitas cambiar tu contraseña, puedes hacerlo en la sección de *Configuración de cuenta*. Es rápido y seguro.",
                                                List.of(crearBoton("Actualizar contraseña", "abrir_perfil"))),
                                crearRespuesta("💾 Recuerda mantener tu perfil actualizado: foto, CV y datos de contacto. ¡Eso mejora tus oportunidades de ser contratado!",
                                                List.of(crearBoton("Actualizar perfil", "abrir_perfil")))));

                // 🎓 GUÍAS DE ENTREVISTA
                respuestas.put("guias_entrevista", List.of(
                                crearRespuesta("🎯 Contamos con guías prácticas para ayudarte a preparar tus entrevistas laborales. ¡Te serán de mucha ayuda!",
                                                List.of(crearBoton("Ver guías de entrevista",
                                                                "abrir_guias_entrevista"))),
                                crearRespuesta("🧠 ¿Nervios antes de una entrevista? Te recomiendo revisar nuestras guías con consejos para causar una gran impresión.",
                                                List.of(crearBoton("Abrir guías de entrevista",
                                                                "abrir_guias_entrevista"))),
                                crearRespuesta("💬 Tenemos guías con preguntas frecuentes, tips de vestimenta y cómo destacar tus habilidades.",
                                                List.of(crearBoton("Ver consejos de entrevista",
                                                                "abrir_guias_entrevista")))));

                // ⏳ TIEMPO DE RESPUESTA EMPRESAS
                respuestas.put("tiempo_respuesta", List.of(
                                crearRespuesta("⏳ El tiempo de respuesta depende de cada empresa. Algunas responden en pocos días, otras pueden tardar más de una semana.",
                                                List.of(crearBoton("Ver postulaciones", "abrir_postulaciones"))),
                                crearRespuesta("📨 Generalmente, las empresas revisan los perfiles durante la primera semana tras la publicación del empleo, pero los tiempos varían.",
                                                List.of(crearBoton("Ver estado de postulaciones",
                                                                "abrir_postulaciones"))),
                                crearRespuesta("💡 Consejo: si una empresa no ha respondido aún, no te preocupes. A veces tardan dependiendo de la cantidad de postulantes.",
                                                List.of(crearBoton("Mis postulaciones", "abrir_postulaciones")))));

                // 🧾 INFORMACIÓN GENERAL
                respuestas.put("información", List.of(
                                crearRespuesta("Soy tu asistente virtual de *Workwise Cartagena* 💼🌴. Te ayudo a encontrar empleo, postularte y gestionar tu perfil.",
                                                botonesMenuPrincipal()),
                                crearRespuesta("En esta plataforma puedes: registrarte, ver ofertas, postularte y contactar empresas locales de Cartagena. 📍",
                                                botonesMenuPrincipal()),
                                crearRespuesta("✨ En Workwise te conectamos con las mejores oportunidades laborales de Cartagena. También ofrecemos consejos para entrevistas y ferias de empleo.",
                                                botonesMenuPrincipal()),
                                crearRespuesta("🌐 Además de buscar empleo, puedes mejorar tu perfil, subir tu hoja de vida y prepararte para entrevistas con nuestras guías.",
                                                botonesMenuPrincipal()),
                                crearRespuesta("📊 Nuestra misión es ayudarte a crecer profesionalmente conectándote con empresas de distintos sectores en Cartagena.",
                                                botonesMenuPrincipal())));

                // 💬 AYUDA GENERAL
                respuestas.put("ayuda", List.of(
                                crearRespuesta("¡Claro! 😊 Puedo ayudarte con:\n\n• 📝 Registro en la plataforma\n• 🔐 Iniciar sesión\n• 💼 Buscar ofertas laborales\n• 👤 Gestionar tu perfil\n• 📩 Postularte a empleos\n• 📂 Subir hoja de vida\n• 🎓 Ver guías de entrevista\n\n¿Qué necesitas?",
                                                botonesMenuPrincipal()),
                                crearRespuesta("Estoy aquí para asistirte 💡. Puedes preguntarme sobre registro, ofertas, perfil, guías de entrevista o cómo postularte a un empleo.",
                                                botonesMenuPrincipal()),
                                crearRespuesta("🙋 Si tienes dudas, dime algo como:\n- “Cómo subo mi hoja de vida”\n- “Cómo cambio mi contraseña”\n- “Ver guías de entrevista”",
                                                botonesMenuPrincipal())));

                // 📨 POSTULACIÓN
                respuestas.put("postulacion", List.of(
                                crearRespuesta("Para postularte, primero busca una oferta laboral, haz clic sobre ella y selecciona el botón 'Postularme' 💼",
                                                List.of(crearBoton("Ir a buscar ofertas", "abrir_ofertas"))),
                                crearRespuesta("💡 Consejo: asegúrate de tener tu perfil completo antes de postularte. ¡Eso aumenta tus posibilidades de ser contratado!",
                                                List.of(crearBoton("Ver ofertas disponibles", "abrir_ofertas"))),
                                crearRespuesta("🧾 Solo necesitas iniciar sesión, elegir una oferta y presionar 'Postularme'. ¡Y listo! 🎯",
                                                List.of(crearBoton("Explorar empleos", "abrir_ofertas"))),
                                crearRespuesta("Si ya aplicaste a una oferta, puedes revisar el estado de tu postulación en tu perfil. 👇",
                                                List.of(crearBoton("Ver mis postulaciones", "abrir_postulaciones")))));
                // 📄 HOJAS DE VIDA (CV)
                respuestas.put("hoja_vida", List.of(
                                crearRespuesta("📄 ¿Quieres subir tu hoja de vida? Sigue estos pasos:\n\n1️⃣ Inicia sesión en tu cuenta.\n2️⃣ Ve a *Mi Perfil*.\n3️⃣ Busca la sección *'Hoja de vida'* o *'CV'*.\n4️⃣ Haz clic en *'Subir archivo'* y selecciona tu documento.\n5️⃣ Guarda los cambios. ¡Listo! Tu CV estará disponible para postularte.",
                                                List.of(
                                                                crearBoton("Subir hoja de vida", "abrir_perfil"),
                                                                crearBoton("Cómo adjuntar",
                                                                                "abrir_ayuda_adjuntar_cv"))),

                                crearRespuesta("✅ Formatos recomendados: **PDF (recomendado)** o **DOC/DOCX**.\nEvita imágenes o formatos inusuales.\n\n💾 Tamaño máximo sugerido: **5 MB**. Si tu archivo es más grande, te recomiendo comprimirlo antes de subirlo.",
                                                List.of(
                                                                crearBoton("Ver plantillas", "abrir_plantillas_cv"),
                                                                crearBoton("Subir hoja de vida", "abrir_perfil"))),

                                crearRespuesta("✍️ Consejos para tu hoja de vida:\n• Usa una estructura clara: contacto, perfil profesional, experiencia, educación y habilidades.\n• Destaca logros medibles (Ej: *“Aumenté las ventas un 20%”*).\n• Ajusta tu CV a cada oferta laboral.\n• Evita errores de ortografía o abreviaturas poco claras.",
                                                List.of(
                                                                crearBoton("Ver ejemplos de CV", "abrir_ejemplos_cv"),
                                                                crearBoton("Subir hoja de vida", "abrir_perfil"))),

                                crearRespuesta("📎 ¿Cómo adjunto mi hoja de vida al postularme?\n1️⃣ Selecciona la oferta.\n2️⃣ Haz clic en *'Postularme'*.\n3️⃣ Verifica que tu CV esté cargado (puedes cambiarlo si lo deseas).\n4️⃣ Envía tu postulación.\n\n💡 Si ya subiste tu hoja de vida al perfil, se adjunta automáticamente.",
                                                List.of(
                                                                crearBoton("Ver ofertas", "abrir_ofertas"),
                                                                crearBoton("Subir hoja de vida", "abrir_perfil"))),

                                crearRespuesta("🔄 ¿Quieres actualizar tu CV?\nSolo entra a *Mi Perfil* → *Hoja de vida* → *Actualizar archivo* → selecciona el nuevo documento → *Guardar*. Así tus próximas postulaciones usarán la versión actualizada.",
                                                List.of(
                                                                crearBoton("Actualizar CV", "abrir_perfil"))),

                                crearRespuesta("🔒 Privacidad: tu hoja de vida **solo será visible** para las empresas a las que te postules o según tus ajustes de visibilidad. Puedes modificar esto desde tu perfil en la sección *Configuración de privacidad*.",
                                                List.of(
                                                                crearBoton("Abrir configuración", "abrir_perfil"))),

                                crearRespuesta("🧾 ¿Necesitas ayuda para crear tu CV? Te recomiendo usar nuestras plantillas y ejemplos. Están diseñadas para destacar tus habilidades y experiencia.",
                                                List.of(
                                                                crearBoton("Ver plantillas", "abrir_plantillas_cv"),
                                                                crearBoton("Ver ejemplos", "abrir_ejemplos_cv"))),

                                crearRespuesta("⚠️ Problemas comunes al subir tu hoja de vida:\n• Archivo demasiado grande 📁\n• Formato no soportado 📄\n• Conexión inestable 🌐\n\nIntenta nuevamente o contacta con soporte si el error persiste.",
                                                List.of(
                                                                crearBoton("Reintentar carga", "abrir_perfil"),
                                                                crearBoton("Contactar soporte", "abrir_soporte"))),

                                crearRespuesta("💡 Tip rápido: personaliza tu CV para cada postulación. Ajusta tu perfil, habilidades y palabras clave según la vacante. ¡Eso aumenta mucho tus posibilidades!",
                                                List.of(
                                                                crearBoton("Ver consejos CV", "abrir_tips_cv"),
                                                                crearBoton("Subir hoja de vida", "abrir_perfil"))),

                                crearRespuesta("🧩 Estructura sugerida para tu hoja de vida:\n\n- Nombre completo y contacto 📞\n- Perfil profesional 🎯\n- Experiencia laboral 💼\n- Educación 🎓\n- Habilidades y certificaciones 🧠\n- Idiomas 🌎\n\n¿Quieres que te muestre una plantilla?",
                                                List.of(
                                                                crearBoton("Ver plantilla", "abrir_plantillas_cv"),
                                                                crearBoton("Ver ejemplos", "abrir_ejemplos_cv")))));

                // 👋 DESPEDIDA
                respuestas.put("despido", List.of(
                                crearRespuesta("¡De nada! 😊 Espero haberte ayudado. ¡Mucho éxito en tus postulaciones! 💼",
                                                botonesMenuPrincipal()),
                                crearRespuesta("Gracias por usar *Workwise Cartagena* 🌴. ¡Vuelve pronto y sigue buscando tu empleo ideal!",
                                                botonesMenuPrincipal()),
                                crearRespuesta("¡Fue un placer ayudarte! 🙌 Si necesitas más información, estaré aquí cuando lo requieras.",
                                                botonesMenuPrincipal()),
                                crearRespuesta("👋 ¡Hasta luego! Recuerda mantener tu perfil actualizado y seguir postulando a nuevas vacantes.",
                                                botonesMenuPrincipal())));

                // 📊 DATOS Y ESTADÍSTICAS
                respuestas.put("datos", List.of(
                                crearRespuesta("📈 Aquí puedes consultar estadísticas actualizadas sobre el empleo en Cartagena, incluyendo tasas de desempleo, sectores más activos y cifras recientes.",
                                                List.of(crearBoton("Ver estadísticas", "abrir_estadistica"))),
                                crearRespuesta("¿Quieres conocer cómo está el panorama laboral en la ciudad? 🌆 Accede a los datos de desempleo, empleabilidad y más aquí 👇",
                                                List.of(crearBoton("Abrir estadísticas", "abrir_estadistica"))),
                                crearRespuesta("📊 En esta sección encontrarás información detallada sobre los indicadores laborales y el desempeño del mercado de trabajo en Cartagena.",
                                                List.of(crearBoton("Ver datos laborales", "abrir_estadistica"))),
                                crearRespuesta("Puedes explorar datos sobre la actividad económica y el nivel de contratación en diferentes sectores de la ciudad.",
                                                List.of(crearBoton("Ver estadísticas", "abrir_estadistica"))),
                                crearRespuesta("💡 También podrás consultar cifras sobre el uso de la plataforma Workwise: número de usuarios registrados, postulaciones activas y empresas participantes.",
                                                List.of(crearBoton("Estadísticas Workwise", "abrir_estadistica"))),
                                crearRespuesta("¿Te interesa conocer las cifras de desempleo o el crecimiento de la plataforma? 🔍 Aquí puedes verlo todo en tiempo real.",
                                                List.of(crearBoton("Ver datos", "abrir_estadistica"))),
                                crearRespuesta("Estos datos te ayudarán a entender mejor las tendencias del mercado laboral y cómo posicionarte para conseguir empleo 💼.",
                                                List.of(crearBoton("Explorar estadísticas", "abrir_estadistica")))));

                // 🧑‍💻 SOBRE NOSOTROS / CREADORES
                respuestas.put("sobre_nosotros", List.of(
                                crearRespuesta("👋 Somos *Workwise Cartagena*, una plataforma diseñada para conectar talento con oportunidades laborales locales. 🌴",
                                                List.of(crearBoton("Conocer más", "abrir_sobre_nosotros"))),
                                crearRespuesta("💼 En Workwise ayudamos a personas y empresas de Cartagena a encontrarse. Promovemos la empleabilidad y el crecimiento profesional en la región.",
                                                List.of(crearBoton("Ver información", "abrir_sobre_nosotros"))),
                                crearRespuesta("🌟 Nuestro objetivo es impulsar el empleo en Cartagena mediante herramientas digitales accesibles, eficientes y seguras.",
                                                List.of(crearBoton("Conocer nuestra misión", "abrir_sobre_nosotros"))),
                                crearRespuesta("📊 Workwise nació como un proyecto académico y social que busca mejorar la conexión entre buscadores de empleo y reclutadores locales.",
                                                List.of(crearBoton("Saber más", "abrir_sobre_nosotros"))),
                                crearRespuesta("🤝 Detrás de Workwise hay un equipo comprometido con la innovación y el desarrollo económico de Cartagena.",
                                                List.of(crearBoton("Ver equipo", "abrir_sobre_nosotros"))),
                                crearRespuesta("👨‍💻👩‍💼 Somos un grupo de desarrolladores y profesionales locales que queremos facilitar tu camino laboral. ¡Nos encanta verte crecer! 🚀",
                                                List.of(crearBoton("Conocer al equipo", "abrir_sobre_nosotros"))),
                                crearRespuesta("¿Quieres saber quiénes están detrás de la plataforma? 💡 Aquí puedes conocer más sobre nosotros, nuestra historia y objetivos.",
                                                List.of(crearBoton("Ver más", "abrir_sobre_nosotros")))));

                // 🧠 DEFAULT / RESPUESTA POR DEFECTO
                respuestas.put("default", List.of(
                                crearRespuesta("No estoy seguro de eso 😅. Puedo ayudarte con registro, login, ofertas de trabajo, perfil o guías de entrevista.",
                                                botonesMenuPrincipal()),
                                crearRespuesta("Hmm 🤔 no entendí bien. ¿Podrías intentar de otra manera? Te dejo algunas opciones 👇",
                                                botonesMenuPrincipal()),
                                crearRespuesta("Perdón, no tengo información exacta sobre eso 🙈. Pero puedo ayudarte con temas como empleos, registro o tu cuenta.",
                                                botonesMenuPrincipal())));
        }

        // ============================================================
        // 🔹 LÓGICA PRINCIPAL
        // ============================================================
        public Map<String, Object> obtenerRespuesta(String mensajeUsuario) {
                if (mensajeUsuario == null || mensajeUsuario.isBlank())
                        return crearRespuesta("Por favor escribe un mensaje 😅", null);

                String mensaje = normalizarTexto(mensajeUsuario);
                String intencion = detectarIntencion(mensaje);

                List<Map<String, Object>> posibles = respuestas.get(intencion);
                if (posibles == null || posibles.isEmpty())
                        posibles = respuestas.get("default");

                return posibles.get(random.nextInt(posibles.size()));
        }

        private String normalizarTexto(String texto) {
                if (texto == null)
                        return "";
                String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD)
                                .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""); // Elimina tildes
                normalizado = normalizado.toLowerCase()
                                .replaceAll("[^a-zñ0-9 ]", " ") // elimina símbolos raros
                                .replaceAll("\\s+", " ") // compacta espacios
                                .trim();
                return normalizado;
        }

        private String detectarIntencion(String mensaje) {
                String[] palabras = mensaje.split(" ");
                String mejorCoincidencia = null;
                int mejorPuntaje = 0;

                for (Map.Entry<String, List<String>> entrada : sinonimos.entrySet()) {
                        int puntaje = 0;

                        for (String sinonimo : entrada.getValue()) {
                                String s = normalizarTexto(sinonimo);

                                // Coincidencia directa
                                if (mensaje.contains(s)) {
                                        puntaje += 3;
                                }

                                // Coincidencia parcial por palabras
                                for (String palabra : palabras) {
                                        if (s.contains(palabra) || palabra.contains(s)) {
                                                puntaje += 1;
                                        }
                                }

                                // Coincidencia difusa (Levenshtein simplificado)
                                if (calcularDistanciaLevenshtein(s, mensaje) <= 2 && mensaje.length() > 4) {
                                        puntaje += 2;
                                }
                        }

                        if (puntaje > mejorPuntaje) {
                                mejorPuntaje = puntaje;
                                mejorCoincidencia = entrada.getKey();
                        }
                }

                return mejorPuntaje > 1 ? mejorCoincidencia : null;
        }

        // ============================================================
        // 🔹 UTILIDADES DE DETECCIÓN
        // ============================================================

        private int calcularDistanciaLevenshtein(String a, String b) {

                int[][] dp = new int[a.length() + 1][b.length() + 1];

                for (int i = 0; i <= a.length(); i++)
                        dp[i][0] = i;
                for (int j = 0; j <= b.length(); j++)
                        dp[0][j] = j;

                for (int i = 1; i <= a.length(); i++) {
                        for (int j = 1; j <= b.length(); j++) {
                                int costo = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                                dp[i][j] = Math.min(
                                                Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                                                dp[i - 1][j - 1] + costo);
                        }
                }

                return dp[a.length()][b.length()];
        }

        // ============================================================
        // 🔹 UTILIDADES DE RESPUESTA
        // ============================================================
        private Map<String, Object> crearRespuesta(String texto, List<Map<String, String>> botones) {
                Map<String, Object> res = new HashMap<>();
                res.put("texto", texto);
                if (botones != null && !botones.isEmpty())
                        res.put("botones", botones);
                return res;
        }

        private Map<String, String> crearBoton(String texto, String accion) {
                Map<String, String> b = new HashMap<>();
                b.put("texto", texto);
                b.put("accion", accion);
                return b;
        }

        private List<Map<String, String>> botonesMenuPrincipal() {
                return List.of(
                                crearBoton("Ver ofertas", "abrir_ofertas"),
                                crearBoton("Registrarme", "abrir_registro"),
                                crearBoton("Iniciar sesión", "abrir_login"));
        }
}