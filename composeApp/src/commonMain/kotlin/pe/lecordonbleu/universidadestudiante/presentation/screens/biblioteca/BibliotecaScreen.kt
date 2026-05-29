package pe.lecordonbleu.universidadestudiante.presentation.screens.biblioteca

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import pe.lecordonbleu.universidadestudiante.LoadingIndicator
import pe.lecordonbleu.universidadestudiante.data.remote.dto.LinksBiblioteca
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.getPlatformContext
import pe.lecordonbleu.universidadestudiante.getSettingsStorage
import pe.lecordonbleu.universidadestudiante.presentation.components.StandardTopBar
import pe.lecordonbleu.universidadestudiante.presentation.screens.biblioteca.customcell.CategoriaCard
import pe.lecordonbleu.universidadestudiante.presentation.vo.ResourceUiState
import pe.lecordonbleu.universidadestudiante.util.openUrl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibliotecaScreen(
    viewModel: BibliotecaViewModel,
    navigator: NavController
) {
    // ─── 1. Variables y estados ───────────────────────────────────────────────
    val colors = getColorsTheme()
    val settings = getSettingsStorage()
    val context = getPlatformContext()

    val idUneg = settings.getInt("id_uneg", 1)
    val idTipoUsuario = settings.getInt("idTipoUsuario", 0)
    val tabs = listOf("Bases de Datos", "Recursos Adicionales")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    var showLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var categorias by remember { mutableStateOf(emptyList<LinksBiblioteca>()) }
    var secciones by remember { mutableStateOf(emptyMap<String, List<LinksBiblioteca>>()) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // ─── 2. UI ────────────────────────────────────────────────────────────────
    LaunchedEffect(Unit) {
        viewModel.setRequest(idUneg, idTipoUsuario)
    }

    Scaffold(
        topBar = {
            StandardTopBar(
                title = "Biblioteca",
                subtitle = "RECURSOS DIGITALES",
                onBackClick = { navigator.popBackStack() }
            )
        },
        containerColor = colors.backGroundColor
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (showLoading) {
                LoadingIndicator(modifier = Modifier.align(Alignment.Center))
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.colorNaranjaOscuro,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(32.dp)
                )
            }

            if (!showLoading && errorMessage.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = colors.colorExpenseItem,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tabs.forEachIndexed { index, title ->
                            val isSelected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        color = if (isSelected) colors.colorMixPrimary else colors.colorExpenseItem,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        scope.launch { pagerState.animateScrollToPage(index) }
                                    }
                                    .padding(vertical = 18.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) colors.colorBlanco else colors.textColor.copy(alpha = 0.65f)
                                )
                            }
                        }
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) { page ->
                        when (page) {
                            0 -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(bottom = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    item {
                                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                            categorias.chunked(2).forEach { fila ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                                ) {
                                                    fila.forEach { item ->
                                                        Box(modifier = Modifier.weight(1f)) {
                                                            CategoriaCard(
                                                                item = item,
                                                                colors = colors,
                                                                onClick = { openUrl(context, item.url_biblioteca_cab) }
                                                            )
                                                        }
                                                    }
                                                    if (fila.size == 1) {
                                                        Spacer(modifier = Modifier.weight(1f))
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            else -> {
                                RecursosAdicionalesPage(
                                    secciones = secciones,
                                    colors = colors,
                                    onItemClick = { link ->
                                        openUrl(context, link.url_biblioteca_det)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // ─── 3. when (uiState) ────────────────────────────────────────────────────
    when (val s = uiState) {
        is ResourceUiState.Loading -> {
            showLoading = true
        }
        is ResourceUiState.Success -> {
            showLoading = false
            errorMessage = ""
            val todos = s.data.listadoBiblioteca
            categorias = todos.filter {
                it.url_biblioteca_det == "NULL" && it.nombre_biblioteca_det == "NULL"
            }
            secciones = todos
                .filter { it.url_biblioteca_det != "NULL" || it.nombre_biblioteca_det != "NULL" }
                .groupBy { it.nombre_biblioteca_cab }
        }
        is ResourceUiState.Error -> {
            showLoading = false
            errorMessage = s.message
        }
        ResourceUiState.Empty -> {}
    }
}
