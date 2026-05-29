package pe.lecordonbleu.universidadestudiante.presentation.screens.perfil

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.perfil.customcell.PerfilEstudianteTabs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilEstudianteScreen(navigator: NavController, viewModel: PerfilViewModel) {
    val perfilState by viewModel.uiStatePerfil.collectAsStateWithLifecycle()
    val settingsStorage = getSettingsStorage()

    val idUsuario = remember { settingsStorage.getInt("idUsuario", 0) }
    val nombre = remember { settingsStorage.getString("persNombre", "") }
    val apellidoPat = remember { settingsStorage.getString("persApellidoPat", "") }
    val apellidoMat = remember { settingsStorage.getString("persApellidoMat", "") }
    val estUrlFoto = remember { settingsStorage.getString("estUrlFoto", "") }
    val nombreCompleto = "$apellidoPat $apellidoMat\n $nombre"

    LaunchedEffect(Unit) {
        if (idUsuario != 0) {
            viewModel.setPerfilRequest(idUsuario)
        }
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "PERFIL",
                subtitle = "Datos del estudiante",
                onBackClick = { navigator.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            EncabezadoPerfil(nombreCompleto = nombreCompleto, estUrlFoto = estUrlFoto ?: "")
            PerfilEstudianteTabs(state = perfilState)
        }
    }
}

@Composable
fun EncabezadoPerfil(nombreCompleto: String, estUrlFoto: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = estUrlFoto,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .border(width = 3.dp, color = MaterialTheme.colorScheme.primary, shape = CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = nombreCompleto.uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "Estudiante",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }
    }
}
