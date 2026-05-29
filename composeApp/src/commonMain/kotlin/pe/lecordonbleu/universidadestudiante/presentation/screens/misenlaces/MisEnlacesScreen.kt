package pe.lecordonbleu.universidadestudiante.presentation.screens.misenlaces

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import pe.lecordonbleu.universidadestudiante.LoadingIndicator
import pe.lecordonbleu.universidadestudiante.data.remote.dto.ListLinksInstitucional
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.misenlaces.customcell.EnlaceCardItem
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState
import pe.lecordonbleu.universidadestudiante.util.openUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MisEnlacesScreen(
    viewModel: MisEnlacesViewModel,
    navigator: NavController
) {
    val colors = getColorsTheme()
    val settings = getSettingsStorage()
    val idUneg = settings.getInt("id_uneg", 1)
    val idSistema = settings.getInt("idSistema", 0)

    var enlaces by remember { mutableStateOf<List<ListLinksInstitucional>>(emptyList()) }
    var showLoading by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setRequest(idUneg, idSistema)
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Mis Enlaces",
                subtitle = "ACCESOS DIRECTOS",
                onBackClick = { navigator.popBackStack() }
            )
        },
        containerColor = colors.backGroundColor
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(enlaces) { item ->
                EnlaceCardItem(
                    item = item,
                    onClick = { url -> openUrl(null, url) }
                )
            }
        }
    }

    when (uiState) {
        is ResourceUiState.Loading -> {
            showLoading = true
        }
        is ResourceUiState.Success -> {
            showLoading = false
            enlaces = (uiState as ResourceUiState.Success).data.ListLinksInstitucional
        }
        is ResourceUiState.Error -> {
            showLoading = false
        }
        ResourceUiState.Empty -> {}
    }

    if (showLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingIndicator(modifier = Modifier.size(50.dp))
        }
    }
}
