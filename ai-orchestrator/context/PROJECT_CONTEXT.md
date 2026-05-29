# PROJECT CONTEXT — Estado del proyecto

## Módulos KMP — Estado de avance

| Módulo | Avance | Notas |
|---|---|---|
| Login | 100% | Completo. Ícono Android (adaptive icon ilcb_logo_dev.webp) e iOS aplicados. CheckboxComponent: Box redondeado (RoundedCornerShape 6dp) reemplaza Checkbox nativo. MyTextFieldComponent y PasswordTextFieldComponent: dark mode completo (textColor, backGroundColor, colorMixPrimary para bordes). Flecha atrás del formulario institucional reemplazada por Icons.AutoMirrored.Filled.ArrowBack. LoginScreenMicrosoftView: StandardTopBar + fondo backGroundColor + textColor en todos los Text. |

| Módulo | Avance | Notas |
|---|---|---|
| MarcarAsistencia | 100% | Completo. Dialog de resultado con CustomDialogBasic, fix tipo Int en DTO, polling reinicia al cerrar dialog. |
| Encuestas | 100% | Completo. Validado en flujo real — docente y satisfacción funcionando. |
| Notas | 90% | DetalleNotasScreen completo: tabs verticales, TareaAcadCell, PromedioConsolidadoCard, SubTabCard, SimuladorNotasSheet. Pendiente: definir colores de aprobado/desaprobado en TareasAcademicas, Evaluacion Parcial y Final (nota_min aun no confirmada para esas evaluaciones). |
| Horario | 100% | Completo. ClaseCard: paleta stripe IlcbColor (10 colores), Surface 16dp + BorderStroke, franja 6dp CircleShape, badges diferenciados (PRESENCIAL=colorMixPrimary, SINCRONICA=colorEsmeralda, VIRTUAL=colorOrange800), ZoomOutMap restaurado. HorarioPorDiaScreen: StandardTopBar, header de fecha estilo CalendarioMensualHeader (card + flechas circulares + día/mes), alturaTotalDp=1700dp. IlcbColor: paleta IlcbStripe* agregada. DarkModeColors: colorOrange800 agregado. |

| MisAsistencias | 100% | Completo. Dropdowns Carrera/Periodo/Asignatura, AsignaturaAsistenciaCard: cuadrito naranja si porcen > 0 (colorNaranjaOscuro), porcentaje display desde string crudo del servidor. |
| DetalleAsistencia | 73% | En progreso. HorizontalPager (historial/proximas), limite proximas = max(finDeMes, finDeSemana). AsistenciaGraficoPremium: circulo % inasistencia, cuadro Faltas Disp./En Practicas, breakdown PRACTICAS/TEORICA con conteos asistio/falto y barra animada, nota "Sin contar clases no registradas". |

| Biblioteca | 100% | Completo. Fix openUrl usando getPlatformContext(). |
| ArchivosCompartidos | 100% | StandardTopBar, Surface card con AppDropdownMenu (Servicio Academico/Carrera), ModalBottomSheet selector carpeta, patrón 3 secciones + gate booleano, ContenidoTabCompartidos refactorizado (componente puro), ContenidoArchivoItem: badge extension + check flag_descargado/flag_leido. onCheckClick: toggle flagLeido, POST /estadoArchivoLectura, CustomDialogBasic con titulo/mensaje del servidor, recarga contenido al cerrar. |
| VerMatricula | 100% | Completo. Tab "Resumen Historico", tab "Ver Matricula" y tab "Horario" (vista semanal tipo agenda). HorarioMatriculaTab: grilla 7 columnas (días con clase), scroll horizontal + vertical sincronizados, cards posicionadas por hora parseando campo `horario` ("JUE: 07:00 - 09:30;LUN: 07:00 - 09:30"), paleta 20 colores IlcbStripe asignados en orden de aparición. |
| ClasesHoy | 100% | TarjetaClasesHoy en HomeScreen debajo de TarjetaEstadoCuenta: header "Miércoles 20", ClaseCard por cada clase del dia (showExpandIcon=false), "No tiene clases programadas para el dia hoy." si vacío, X para cerrar (dismissible por sesión). fetchClasesHoy en HomeViewModel usa getTodayLocalDate() con fecha_ini=fecha_fin=hoy, llama getHorario. |

| CuentaCorriente | 70% | Capa de datos completa: 8 DTOs, 8 requests, AppRepository, AppRepositoryImpl, AppModule (Koin). CuentaCorrienteViewModel: fetcher puro + Pecano (estado, request, set/fetch/reset). CuentaCorrienteScreen: dropdowns, lista de cuotas (CuotaCard), checkboxes, flujo de pago con dialog confirmacion, showLoading antes de setTemporalCuentaCorriente. TemporalCuentaCorrienteRequest: nested data classes replicando nameValuePairs de Gson. PagoFlywireScreen: WebView Flywire + volverACuentaCorriente (popUpTo inclusive=true). WebViewComposable Android: onPageFinished + JavascriptInterface en main thread. WebViewComposable iOS: WKScriptMessageHandler + didFinishNavigation + evaluateJavaScript. HomeScreen: card Estado de Cuenta con onClick /cuentaCorriente, CUENTA CORRIENTE excluido de Mas Servicios, aula demo eliminado. HomeViewModel: aula demo eliminado. Pendiente: flujo Pecano completo en UI, estado final de pantalla post-pago. |

| TramiteDocumentario | 80% | TramiteDocumentarioScreen: StandardTopBar, filtros reactivos, navigate simplificado a /registrarTramite (sin params). RegistrarTramiteDocumentarioScreen: todos los combos migrados a AppDropdownMenu, layout reorganizado en 3 Card contenedores (Informacion General, Entrega y Modalidad, Resumen y Estado), FAB pill button Solicitar tramite (FabPosition.Center), Spacer(80dp) al final del Column para que el FAB no tape contenido, Monto bold 16sp colorMixPrimary, HorizontalDivider solo en Resumen y Estado (Requisitos/Monto), navegacion a Flywire con rutaRetorno /tramiteDocumentario. PagoFlywireScreen: parametro rutaRetorno para retorno dinamico segun caller. Navigator: ruta /pagoFlywire/{codTransaccion}/{rutaRetorno}, usa savedStateHandle (no arguments) para compatibilidad iOS. CuentaCorrienteScreen: navega a /pagoFlywire/$cod/cuentaCorriente. Pendiente: validaciones discrepantes con nativo (motivo/idTipoEntrega, modalPresencial swap, TipoFormularioTramite.G), flujo post-pago. |

| FichaMatricula | 100% | Botón visible en HomeScreen cuando id_proce_mat == 1. Llama generarPdfPreMatApp (GeneralControlClaseServlet). Respuesta ByteArray abierta con expect/actual openPdfFromBytes (Android: FileProvider + ACTION_VIEW, iOS: NSData + UIApplication). FileProvider declarado en AndroidManifest con file_paths.xml (cache-path). |

| Assets Android | 100% | Ícono de app (android:icon / android:roundIcon) reemplazado por ulcb_logo_ico.webp en 5 densidades. Adaptive icon XMLs eliminados. |
| Assets Carrusel | 100% | Imágenes del OnBoarding reemplazadas por unicampus_9/2/3 del nativo. CarruselLogos: logo_ilcb_1 → logo_ulcb_anniversary, logo_ilcb_2 → ulcb_logo (del nativo). |
| Drawer | 100% | Logo cabecera reemplazado por ulcb_logo_white (Image con ContentScale.Fit, fillMaxWidth 0.85f, height 160dp). |
| Convalidaciones | 100% | Completo. |

## Pendientes conocidos
- Notas: confirmar si nota_min aplica también para colorear notas en TareasAcademicas, Evaluacion Parcial y Final — definir colores aprobado/desaprobado cuando se tenga la regla de negocio
- DetalleAsistencia: completar el 30% restante
