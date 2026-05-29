package pe.lecordonbleu.universidadestudiante.presentation.screens.perfil.customcell

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListPerfilEstudiante
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ResponsePerfilEstudiante
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState

@Composable
fun PerfilEstudianteTabs(
    state: ResourceUiState<List<ResponsePerfilEstudiante>>,
    modifier: Modifier = Modifier
) {
    val colors = getColorsTheme()
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Datos Personales", "Contacto")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            contentColor = colors.colorAzulContraste
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    selectedContentColor = colors.colorAzulContraste,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    text = { Text(title) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is ResourceUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is ResourceUiState.Error -> {
                Text(
                    text = state.message ?: "Error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            is ResourceUiState.Success -> {
                val perfil = state.data.firstOrNull()?.ListPerfilEstudiante?.firstOrNull()
                if (perfil != null) {
                    if (selectedTabIndex == 0) {
                        DatosPersonalesTab(perfil)
                    } else {
                        ContactoTab(perfil)
                    }
                } else {
                    Text("No se encontraron datos.", modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }

            ResourceUiState.Empty -> {
                println("no hay datos para mostrar")
            }
        }
    }
}

@Composable
fun DatosPersonalesTab(perfil: ListPerfilEstudiante) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TituloSeccion("DATOS PERSONALES")
            Campo("Apellido Paterno:", perfil.usuario_apellido_pat)
            Campo("Apellido Materno:", perfil.usuario_apellido_mat)
            Campo("Nombres:", perfil.ususario_nombre)
            Campo("Sexo:", perfil.genero_nombre)
            Campo("Fecha de Nacimiento:", perfil.fecha_nacimiento.trim())
            Campo("País de Origen:", perfil.pais_procedencia)

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            TituloSeccion("DOCUMENTO DE IDENTIDAD")
            Campo("Tipo de Documento:", perfil.docu_nombre)
            Campo("Número de Documento:", perfil.numero_documento.toString())
            Campo("Estado Civil:", perfil.estado_civil)
        }
    }
}

@Composable
fun ContactoTab(perfil: ListPerfilEstudiante) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TituloSeccion("DOMICILIO ACTUAL")
            Campo("Ubigeo:", perfil.ubigeo)
            Campo("Dirección:", perfil.direc_resi)

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            TituloSeccion("DATOS DE CONTACTO")
            Campo("Email Personal:", perfil.correo_personal)
            Campo("Email Institucional:", perfil.correoelec_ins)
            Campo("Teléfono Fijo:", perfil.telefono1)
            Campo("Teléfono Celular:", perfil.telefono2 ?: "")
            Campo("Contacto de Emergencia:", perfil.nombre_contacto)
        }
    }
}

@Composable
fun Campo(label: String, valor: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@Composable
fun TituloSeccion(texto: String) {
    val colors = getColorsTheme()
    Text(
        text = texto,
        style = MaterialTheme.typography.titleSmall,
        color = colors.colorAzulContraste,
        fontWeight = FontWeight.Bold
    )
}
