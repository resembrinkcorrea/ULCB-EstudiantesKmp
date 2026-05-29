# Cuenta Corriente

## Objetivo
Permitir la seleccion y pago de cuotas pendientes respetando el orden de pago y las restricciones vigentes del modulo.

## Validaciones
- El pago de cuotas se realiza en orden.
- No se permite habilitar cuotas futuras si la secuencia previa no corresponde.
- Si la primera cuota impaga corresponde a la cuota 1, la cuota 1 se selecciona completa automaticamente.
- Si la cuota 1 se selecciona automaticamente, la cuota 2 queda habilitada.
- Las cuotas posteriores permanecen deshabilitadas hasta que el flujo las habilite.
- Si una cuota tiene mas de un item con el mismo numero de cuota, la seleccion aplica a toda la cuota.
- Al seleccionar una cuota, se habilita la siguiente cuota valida del flujo.
- Al desmarcar una cuota posterior dentro de la secuencia, las cuotas siguientes se desmarcan y se deshabilitan.
- Si existe deuda vigente, el pago no puede continuar.
- El boton de pago solo puede continuar cuando existe una seleccion valida y no hay bloqueo por deuda.

## Casos observados
- Si la primera cuota impaga no es la cuota 1, no se selecciona automaticamente.
- Si la primera cuota habilitada se desmarca, las cuotas posteriores se limpian del flujo.
- Si se desmarca una cuota posterior dentro de la cadena seleccionada, las siguientes cuotas tambien salen de la seleccion.
- El modulo puede volver a verificar deuda cuando cambia la seleccion de cuotas en ciertos puntos del flujo.
- Los items pagados no participan en la seleccion de pago.

## Valores relevantes del backend
- `estado = 1`: pagado
- `estado = 2`: pendiente
- `estado = 3`: pendiente/vencido o vencido segun `estado_nombre`
- `estado_nombre = "PAGADO"`: estado pagado
- `estado_nombre = "PENDIENTE"`: pendiente
- `estado_nombre = "VENCIDO"`: vencido
- `estado_nombre = "PENDIENTE/VENCIDO"`: pendiente vencido
- `monto_pendiente = "0.00"`: no aporta monto para pago
- `nro_cuota`: agrupa items que pertenecen a la misma cuota
