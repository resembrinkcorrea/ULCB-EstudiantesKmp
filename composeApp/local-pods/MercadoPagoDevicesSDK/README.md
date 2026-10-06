# MercadoPagoDevicesSDK — Local Pod

Versión: `2.0.3`  
Fuente oficial: `https://github.com/melisource/fury_device-sdk-ios`

---

## Por qué se usa como pod local y no remoto

El pod oficial de MercadoPago **no declara `swift_version`** en su podspec.

En un proyecto KMP + CocoaPods esto provoca un error fatal durante `pod install` porque:

1. **CocoaPods 1.16+ valida `swift_version` antes** de correr cualquier `post_install` hook.
2. El plugin KMP genera un Podfile sintético en `composeApp/build/cocoapods/synthetic/ios/` que no tiene acceso al proyecto Xcode real, por lo que no puede heredar `SWIFT_VERSION` del target.
3. Agregar `SWIFT_VERSION` en el `post_install` del Podfile de `iosApp` llega tarde — la validación ya falló.
4. Usar `pod 'MercadoPagoDevicesSDK', :git => '...'` tampoco resuelve el problema porque el podspec remoto sigue sin tener `swift_version`.

### La única solución que funciona

Copiar los fuentes del SDK (`LibraryComponents/Classes/`) a esta carpeta y declarar un podspec local con `swift_version = '5.0'`. El código fuente es idéntico al oficial v2.0.3 — solo cambia el podspec.

---

## Cómo obtener los fuentes (si hay que actualizar la versión)

```bash
# 1. Agregar temporalmente el pod remoto en iosApp/Podfile:
#    pod 'MercadoPagoDevicesSDK', '~> X.Y.Z'
# 2. pod install
# 3. Copiar:
cp -r iosApp/Pods/MercadoPagoDevicesSDK/LibraryComponents/ \
      composeApp/local-pods/MercadoPagoDevicesSDK/LibraryComponents/
# 4. Actualizar s.version en MercadoPagoDevicesSDK.podspec
# 5. Revertir el Podfile al pod local
```

---

## Estructura esperada

```
MercadoPagoDevicesSDK/
├── MercadoPagoDevicesSDK.podspec   ← con swift_version = '5.0'
├── README.md
└── LibraryComponents/
    └── Classes/
        ├── BPXLUUIDHandler.h
        ├── BPXLUUIDHandler.m
        ├── CwlSysctl.swift
        ├── Device.swift
        ├── DeviceId.swift
        ├── Fingerprint.swift
        ├── MercadoPagoDevicesSDK.swift
        ├── Utils.swift
        └── VendorSpecificAttributes.swift
```

---

## Referencias

- Contexto completo: `ai-orchestrator/context/mercadopago-ios-sdk-setup.md`
- Flujo de pago Yape: `ai-orchestrator/flows/flujo-pago-yape-kmp.md`
- Estudio general MP: `ai-orchestrator/context/mercadopago-estudio.md`
