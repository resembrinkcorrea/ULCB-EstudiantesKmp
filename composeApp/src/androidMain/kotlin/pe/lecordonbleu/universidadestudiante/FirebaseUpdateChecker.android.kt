package pe.lecordonbleu.universidadestudiante

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

actual fun getStoreUpdateUrl(): String =
    "https://play.google.com/store/apps/details?id=pe.lecordonbleu.universidadestudiante"

actual fun getFirebaseVersionUrl(): String = "Update Android ULCB"

actual fun fetchFirebaseVersion(onVersion: (String) -> Unit): () -> Unit {
    val ref = FirebaseDatabase.getInstance().getReference(getFirebaseVersionUrl())
    val listener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val version = snapshot.child("Version").getValue(String::class.java) ?: return
            onVersion(version)
        }
        override fun onCancelled(error: DatabaseError) {}
    }
    ref.addValueEventListener(listener)
    return { ref.removeEventListener(listener) }
}
