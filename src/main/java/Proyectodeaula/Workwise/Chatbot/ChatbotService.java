package Proyectodeaula.Workwise.Chatbot;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

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
                                "hola", "holi", "holis", "buenas", "saludos", "qué tal", "que tal",
                                "buenos días", "buenas tardes", "buenas noches", "hey", "hi", "hello",
                                "buen día", "qué hubo", "que hubo", "qué pasa", "que pasa", "alo", "aloja"));

                sinonimos.put("login", Arrays.asList(
                                "iniciar sesión", "iniciar sesion", "loguear", "log in", "sign in",
                                "acceder", "entrar", "ingresar", "acceso", "identificarme",
                                "ya tengo cuenta", "tengo cuenta", "ya estoy registrado", "ya me registré",
                                "ya me registre", "quiero entrar", "necesito entrar", "abrir sesión",
                                "abrir sesion", "entrar al sistema", "iniciar cuenta", "continuar sesión"));

                sinonimos.put("registro", Arrays.asList(
                                "registrar", "registrarme", "registro", "crear cuenta", "hacer cuenta",
                                "nuevo usuario", "abrir cuenta", "darme de alta", "inscribirme",
                                "suscribirme", "unirme", "crear perfil", "hacer perfil", "sign up",
                                "registrarse", "matricularme", "afiliarme", "crear usuario", "crear perfil nuevo"));

                sinonimos.put("oferta", Arrays.asList(
                                "oferta", "ofertas", "trabajo", "empleo", "vacante", "vacantes",
                                "puesto", "puestos", "convocatoria", "convocatorias", "bolsa de trabajo",
                                "trabajos", "empleos", "buscar trabajo", "encontrar trabajo",
                                "oportunidades", "oportunidad laboral", "laburo", "chamba",
                                "quiero trabajar", "necesito trabajo", "busco empleo", "buscar empleo",
                                "ver empleos", "ver vacantes", "ver ofertas"));

                sinonimos.put("perfil", Arrays.asList(
                                "perfil", "mi perfil", "mis datos", "datos personales",
                                "actualizar información", "editar perfil", "modificar perfil",
                                "cambiar datos", "actualizar datos", "mi cuenta", "configuración",
                                "preferencias", "información personal", "editar información",
                                "actualizar cuenta", "gestionar cuenta", "gestionar perfil",
                                "ver perfil", "ajustes de cuenta", "configurar perfil"));

                sinonimos.put("información", Arrays.asList(
                                "información", "info", "qué puedes hacer", "que puedes hacer",
                                "qué puedo hacer", "que puedo hacer", "qué hay", "que hay",
                                "qué ofreces", "que ofreces", "qué haces", "que haces",
                                "para qué sirves", "para que sirves", "funciones", "capacidades",
                                "ayuda", "help", "soporte", "asistencia", "qué es esto", "que es esto",
                                "cómo funcionas", "como funcionas", "cómo me ayudas", "para qué sirves"));

                sinonimos.put("despido", Arrays.asList(
                                "gracias", "muchas gracias", "ok", "vale", "perfecto", "adios", "adiós",
                                "hasta luego", "nos vemos", "chao", "bye", "goodbye", "hasta pronto",
                                "ciao", "listo", "de acuerdo", "okey", "okeydokey", "genial", "está bien",
                                "esta bien", "bien", "excelente", "fantástico", "super", "chévere",
                                "me sirvió", "me ayudaste", "todo claro", "entendido"));

                sinonimos.put("ayuda", Arrays.asList(
                                "ayuda", "help", "socorro", "auxilio", "no sé", "no se",
                                "no entiendo", "me pierdo", "qué hago", "que hago", "cómo funciona",
                                "como funciona", "necesito ayuda", "me ayudas", "puedes ayudarme",
                                "no sé usar", "no sé qué hacer", "necesito soporte", "ayúdame", "ayudame",
                                "explicame", "dame una guía", "tengo dudas", "no comprendo"));

                sinonimos.put("postulacion", Arrays.asList(
                                "postularme", "postular", "como me postulo", "aplicar", "inscribirme",
                                "enviar solicitud", "quiero postularme", "necesito postularme",
                                "cómo postular", "como postular", "dónde postular", "donde postular",
                                "proceso de postulación", "proceso de postulacion", "hacer postulación",
                                "hacer postulacion", "registrar postulación", "registrar postulacion",
                                "aplicación", "postulación", "aplicar oferta", "enviar cv", "mandar cv"));

                sinonimos.put("hoja_vida", Arrays.asList(
                                "hoja de vida", "hoja vida", "curriculum", "currículum", "cv", "c.v.",
                                "subir cv", "adjuntar cv", "adjuntar hoja de vida", "subir hoja de vida",
                                "cargar cv", "cargar hoja de vida", "actualizar cv", "actualizar hoja de vida",
                                "subir mi cv", "subir mi hoja de vida", "adjuntar mi cv", "enviar cv",
                                "enviar hoja de vida", "mandar cv", "mandar hoja de vida",
                                "mi hoja de vida", "mi cv", "plantilla cv", "formato hoja de vida",
                                "modelo hoja de vida", "ejemplo de cv", "ejemplo de hoja de vida",
                                "crear cv", "crear hoja de vida", "hacer cv", "hacer hoja de vida",
                                "editar cv", "editar hoja de vida", "modificar cv", "modificar hoja de vida",
                                "descargar plantilla", "ver plantilla", "cargar documento laboral",
                                "actualizar documento", "subir archivo de cv", "actualizar curriculum",
                                "ver ejemplos de cv", "plantillas de hoja de vida", "ver plantillas"));

                sinonimos.put("guias_entrevista", Arrays.asList(
                                "guía de entrevista", "guías de entrevista", "consejos de entrevista",
                                "tips de entrevista", "preparar entrevista", "preguntas entrevista",
                                "recomendaciones entrevista", "ayuda entrevista", "como ir a entrevista",
                                "cómo responder entrevista", "prepararme para entrevista",
                                "guia entrevista", "manual entrevista", "ver guías", "ver consejos",
                                "ver tips", "entrevista laboral", "simulacro entrevista"));

                sinonimos.put("tiempo_respuesta", Arrays.asList(
                                "tiempo de respuesta", "cuánto tardan", "cuánto se demoran",
                                "me van a responder", "cuando responden", "respuesta empresa",
                                "respuesta postulación", "demora empresa", "demora en responder",
                                "tiempo espera", "tardan en responder", "tiempo de espera",
                                "cuando sabré", "cuándo sabré", "me avisarán", "estado postulación"));
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

        private String detectarIntencion(String mensaje) {
                mensaje = limpiarRuido(mensaje);

                for (Map.Entry<String, List<String>> entry : sinonimos.entrySet()) {
                        if (contieneSinonimo(mensaje, entry.getValue()))
                                return entry.getKey();
                }

                if (contienePatron(mensaje, "no.*entiendo", "no.*sé", "no.*se"))
                        return "ayuda";

                return "default";
        }

        // ============================================================
        // 🔹 UTILIDADES DE DETECCIÓN
        // ============================================================
        private boolean contieneSinonimo(String texto, List<String> sinonimos) {
                for (String s : sinonimos)
                        if (texto.contains(s))
                                return true;
                return false;
        }

        private boolean contienePatron(String texto, String... patrones) {
                for (String p : patrones)
                        if (Pattern.compile(p).matcher(texto).find())
                                return true;
                return false;
        }

        private String normalizarTexto(String texto) {
                return Normalizer.normalize(texto, Normalizer.Form.NFD)
                                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                                .toLowerCase()
                                .replaceAll("[^a-zñáéíóúü0-9 ]", " ")
                                .replaceAll("\\s+", " ").trim();
        }

        private String limpiarRuido(String texto) {
                String[] ruido = {
                                "un", "una", "unos", "unas", "el", "la", "los", "las",
                                "por", "favor", "porfa", "de", "del", "a", "al", "me", "mi", "mis", "tu", "tus",
                                "su", "sus", "quiero", "necesito", "deseo", "puedo", "podrías", "podrias",
                                "con", "sin", "sobre", "bajo", "entre", "hacia", "hasta"
                };
                for (String palabra : ruido)
                        texto = texto.replaceAll("\\b" + palabra + "\\b", "");
                return texto.replaceAll("\\s+", " ").trim();
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