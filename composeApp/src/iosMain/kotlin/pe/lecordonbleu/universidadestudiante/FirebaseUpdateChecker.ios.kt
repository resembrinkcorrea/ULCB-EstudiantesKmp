package pe.lecordonbleu.universidadestudiante

import cocoapods.FirebaseDatabase.FIRDatabase
import cocoapods.FirebaseDatabase.FIRDataEventType
import kotlinx.cinterop.ExperimentalForeignApi

actual fun getStoreUpdateUrl(): String =
    "itms-apps://itunes.apple.com/app/id1661167886"
actual fun getFirebaseVersionUrl(): String = "Update ULCB"

@OptIn(ExperimentalForeignApi::class)
actual fun fetchFirebaseVersion(onVersion: (String) -> Unit): () -> Unit {
    val ref = FIRDatabase.database().referenceWithPath(getFirebaseVersionUrl())
    val handle = ref.observeEventType(FIRDataEventType.FIRDataEventTypeValue, withBlock = { snapshot ->
        val dict = snapshot?.value as? Map<*, *>
        val version = dict?.get("Version") as? String ?: return@observeEventType
        onVersion(version)
    })
    return { ref.removeObserverWithHandle(handle) }
}
