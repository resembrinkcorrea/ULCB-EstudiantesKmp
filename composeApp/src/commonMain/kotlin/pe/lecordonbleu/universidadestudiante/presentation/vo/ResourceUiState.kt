package pe.lecordonbleu.universidadestudiante.presentation.vo

sealed class ResourceUiState<out T> {
    object Loading : ResourceUiState<Nothing>()
    data class Success<out T>(val data: T) : ResourceUiState<T>()
    data class Error(val message: String) : ResourceUiState<Nothing>()
    object Empty : ResourceUiState<Nothing>()
}
