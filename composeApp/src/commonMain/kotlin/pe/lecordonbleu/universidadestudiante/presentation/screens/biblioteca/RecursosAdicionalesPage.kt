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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
import pe.lecordonbleu.universidadestudiante.DarkModeColors
import pe.lecordonbleu.universidadestudiante.data.remote.dto.LinksBiblioteca
import pe.lecordonbleu.universidadestudiante.presentation.screens.biblioteca.customcell.ItemBibliotecaCell

@Composable
fun RecursosAdicionalesPage(
    secciones: Map<String, List<LinksBiblioteca>>,
    colors: DarkModeColors,
    onItemClick: (LinksBiblioteca) -> Unit
) {
    var query by remember { mutableStateOf("") }
    var categoriaSeleccionada by remember { mutableStateOf("Todos") }
    var mostrarCategorias by remember { mutableStateOf(true) }

    val categorias = remember(secciones) {
        listOf("Todos") + secciones.keys.toList()
    }

    val seccionesFiltradas = remember(secciones, query, categoriaSeleccionada, mostrarCategorias) {
        filtrarRecursos(
            secciones = secciones,
            query = query,
            categoriaSeleccionada = categoriaSeleccionada,
            usarFiltroCategoria = mostrarCategorias
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item("search_bar") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "Buscar recurso...",
                            color = colors.textColor.copy(alpha = 0.45f)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = colors.colorMixPrimary
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.colorExpenseItem,
                        unfocusedContainerColor = colors.colorExpenseItem,
                        focusedBorderColor = colors.colorMixPrimary.copy(alpha = 0.35f),
                        unfocusedBorderColor = colors.textColor.copy(alpha = 0.08f),
                        focusedTextColor = colors.textColor,
                        unfocusedTextColor = colors.textColor,
                        cursorColor = colors.colorMixPrimary
                    )
                )
                Surface(
                    modifier = Modifier
                        .size(56.dp)
                        .clickable {
                            mostrarCategorias = !mostrarCategorias
                            if (!mostrarCategorias) {
                                categoriaSeleccionada = "Todos"
                            }
                        },
                    shape = RoundedCornerShape(18.dp),
                    color = colors.colorExpenseItem,
                    shadowElevation = 1.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = colors.colorMixPrimary,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }
        }

        if (mostrarCategorias) {
            item("categorias_box") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = colors.colorExpenseItem,
                    shadowElevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Filtrar por categoría:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(end = 8.dp)
                        ) {
                            items(categorias.size) { index ->
                                val categoria = categorias[index]
                                val isSelected = categoria == categoriaSeleccionada

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (isSelected) colors.colorMixPrimary.copy(alpha = 0.14f) else colors.colorBlancoGris,
                                    tonalElevation = 0.dp,
                                    shadowElevation = 0.dp,
                                    modifier = Modifier.clickable { categoriaSeleccionada = categoria }
                                ) {
                                    Text(
                                        text = categoria,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) colors.colorMixPrimary else colors.textColor.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (seccionesFiltradas.isEmpty()) {
            item("empty_state") {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = colors.colorExpenseItem,
                    shadowElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "No encontramos resultados",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colors.textColor
                        )
                        Text(
                            text = "Prueba con otra categoría o cambia el texto de búsqueda.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textColor.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        } else {
            seccionesFiltradas.forEach { (titulo, items) ->
                item(key = "section_$titulo") {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        color = colors.colorExpenseItem,
                        tonalElevation = 1.dp,
                        shadowElevation = 3.dp
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .background(
                                            color = colors.colorMixPrimary.copy(alpha = 0.12f),
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = colors.colorMixPrimary,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = titulo,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = colors.textColor
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${items.size} recursos",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = colors.textColor.copy(alpha = 0.58f)
                                    )
                                }
                            }

                            HorizontalDivider(color = colors.textColor.copy(alpha = 0.06f))

                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                items.forEach { link ->
                                    ItemBibliotecaCell(
                                        item = link,
                                        colors = colors,
                                        onClick = { onItemClick(link) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun filtrarRecursos(
    secciones: Map<String, List<LinksBiblioteca>>,
    query: String,
    categoriaSeleccionada: String,
    usarFiltroCategoria: Boolean
): Map<String, List<LinksBiblioteca>> {
    val base = if (!usarFiltroCategoria || categoriaSeleccionada == "Todos") {
        secciones
    } else {
        secciones.filterKeys { it == categoriaSeleccionada }
    }

    if (query.isBlank()) return base

    val texto = query.trim().lowercase()

    return base.mapValues { (titulo, items) ->
        items.filter { item ->
            titulo.lowercase().contains(texto) ||
                item.nombre_biblioteca_det.lowercase().contains(texto) ||
                item.descri_biblioteca_det.lowercase().contains(texto)
        }
    }.filterValues { it.isNotEmpty() }
}
