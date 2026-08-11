package pe.lecordonbleu.universidadestudiante.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.foundation.border
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.Text
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import kotlinx.coroutines.delay
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import ulcbintranetkmp.composeapp.generated.resources.Res
import ulcbintranetkmp.composeapp.generated.resources.ulcb_logo_circle
import org.jetbrains.compose.resources.painterResource
import pe.lecordonbleu.universidadestudiante.getColorsTheme
import pe.lecordonbleu.universidadestudiante.leCordonBleuFont
import pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario.customcell.AccionIconButton

@Composable
fun MyTextFieldComponent(
    labelValue: String,
    painterResource: ImageVector,
    onTextChanged: (String) -> Unit,
    errorStatus: Boolean = false,
    initialValue: String = "",
    enabled: Boolean = true
) {
    val colors = getColorsTheme()
    var textValue by remember { mutableStateOf(initialValue) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = colors.textColor,
            focusedBorderColor = colors.colorMixPrimary,
            unfocusedBorderColor = colors.textColor.copy(alpha = ContentAlpha.disabled),
            focusedLabelColor = colors.colorMixPrimary,
            unfocusedLabelColor = colors.textColor.copy(alpha = ContentAlpha.medium),
            cursorColor = colors.colorMixPrimary,
            leadingIconColor = colors.textColor.copy(alpha = ContentAlpha.medium),
            backgroundColor = colors.backGroundColor
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
        singleLine = true,
        value = textValue,
        onValueChange = {
            textValue = it
            onTextChanged(it)
        },
        leadingIcon = { Icon(painterResource, contentDescription = null) },
        isError = errorStatus,
        enabled = enabled
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun <T> AppDropdownMenu(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    label: String = "",
    placeholder: String = "Seleccionar",
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val colors = getColorsTheme()

    val displayText = selectedItem?.let { itemLabel(it) } ?: placeholder

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = it },
        modifier = modifier
    ) {
        androidx.compose.material3.OutlinedTextField(
            value = displayText,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = if (label.isNotEmpty()) {
                {
                    Text(
                        text = label,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        color = colors.textColor.copy(alpha = 0.6f)
                    )
                }
            } else null,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp), // Bordes ligeramente más suaves
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = colors.textColor,
                unfocusedTextColor = colors.textColor,
                disabledTextColor = colors.textColor.copy(alpha = 0.5f),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedBorderColor = colors.colorMixPrimary,
                unfocusedBorderColor = colors.textColor.copy(alpha = 0.2f), // Borde unfocused más sutil (0.2f)
                disabledBorderColor = colors.textColor.copy(alpha = 0.1f),
                focusedLabelColor = colors.colorMixPrimary,
                unfocusedLabelColor = colors.textColor.copy(alpha = 0.6f),
                focusedTrailingIconColor = colors.colorMixPrimary,
                unfocusedTrailingIconColor = colors.textColor.copy(alpha = 0.5f)
            ),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 15.sp, // Un punto más grande para mejor lectura
                fontWeight = FontWeight.Medium
            ),
            singleLine = true
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = colors.colorExpenseItem,
            shape = RoundedCornerShape(12.dp), // Redondea las esquinas del menú flotante
            modifier = Modifier.background(
                color = colors.colorExpenseItem,
                shape = RoundedCornerShape(12.dp)
            )
        ) {
            items.forEach { item ->
                val isSelected = item == selectedItem

                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = itemLabel(item),
                                // Color y peso diferente si está seleccionado
                                color = if (isSelected) colors.colorMixPrimary else colors.textColor,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .weight(1f) // Para que el texto no empuje al ícono fuera de la pantalla
                                    .padding(end = 8.dp)
                                    .basicMarquee(
                                        iterations = Int.MAX_VALUE,
                                        repeatDelayMillis = 2000,
                                        initialDelayMillis = 1000,
                                        velocity = 40.dp
                                    )
                            )

                            // Ícono de validación visual para el elemento seleccionado
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Rounded.Check, // Asegúrate de importar androidx.compose.material.icons.rounded.Check
                                    contentDescription = "Seleccionado",
                                    tint = colors.colorMixPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    },
                    onClick = {
                        expanded = false
                        onItemSelected(item)
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    // Le damos un fondo sutil y redondeado al elemento seleccionado dentro de la lista
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 2.dp) // Separación de los bordes del menú
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isSelected) colors.colorMixPrimary.copy(alpha = 0.08f)
                            else Color.Transparent
                        )
                )
            }
        }
    }
}

@Composable
fun <T> SearchableDropdownMenu(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    label: String = "",
    placeholder: String = "Seleccionar",
    searchPlaceholder: String = "Buscar...",
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var debouncedQuery by remember { mutableStateOf("") }
    val colors = getColorsTheme()
    val focusRequester = remember { FocusRequester() }

    val displayText = selectedItem?.let { itemLabel(it) } ?: placeholder

    LaunchedEffect(searchQuery) {
        if (searchQuery.isEmpty()) debouncedQuery = ""
        else { delay(800); debouncedQuery = searchQuery }
    }

    val filteredItems = remember(debouncedQuery, items) {
        if (debouncedQuery.isEmpty()) items
        else items.filter { itemLabel(it).contains(debouncedQuery, ignoreCase = true) }
    }

    LaunchedEffect(expanded) {
        if (expanded) {
            delay(100)
            try { focusRequester.requestFocus() } catch (_: Exception) {}
        } else {
            searchQuery = ""
            debouncedQuery = ""
        }
    }

    Column(modifier = modifier) {
        Box {
            androidx.compose.material3.OutlinedTextField(
                value = displayText,
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                label = if (label.isNotEmpty()) {
                    {
                        Text(
                            text = label,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = colors.textColor.copy(alpha = 0.6f)
                        )
                    }
                } else null,
                trailingIcon = {
                    androidx.compose.material3.Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = if (expanded) colors.colorMixPrimary else colors.textColor.copy(alpha = 0.5f)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colors.textColor,
                    unfocusedTextColor = colors.textColor,
                    disabledTextColor = colors.textColor.copy(alpha = 0.5f),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedBorderColor = colors.colorMixPrimary,
                    unfocusedBorderColor = colors.textColor.copy(alpha = 0.2f),
                    disabledBorderColor = colors.textColor.copy(alpha = 0.1f),
                    focusedLabelColor = colors.colorMixPrimary,
                    unfocusedLabelColor = colors.textColor.copy(alpha = 0.6f),
                    focusedTrailingIconColor = colors.colorMixPrimary,
                    unfocusedTrailingIconColor = colors.textColor.copy(alpha = 0.5f)
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 15.sp, fontWeight = FontWeight.Medium),
                singleLine = true
            )
            if (enabled) {
                Box(modifier = Modifier.matchParentSize().clickable { expanded = !expanded })
            }
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
                    .background(colors.colorExpenseItem)
                    .border(1.dp, colors.textColor.copy(alpha = 0.12f),
                        RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp))
            ) {
                androidx.compose.material3.OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        androidx.compose.material3.Text(
                            text = searchPlaceholder, fontSize = 14.sp,
                            color = colors.textColor.copy(alpha = 0.4f)
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .focusRequester(focusRequester),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colors.textColor,
                        unfocusedTextColor = colors.textColor,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = colors.colorMixPrimary,
                        unfocusedBorderColor = colors.textColor.copy(alpha = 0.2f),
                        cursorColor = colors.colorMixPrimary
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp)
                )

                if (debouncedQuery.isNotEmpty() && filteredItems.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Sin resultados", fontSize = 14.sp,
                            color = colors.textColor.copy(alpha = 0.4f))
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 250.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        filteredItems.forEach { item ->
                            val isSelected = item == selectedItem
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) colors.colorMixPrimary.copy(alpha = 0.08f)
                                        else Color.Transparent
                                    )
                                    .clickable { expanded = false; onItemSelected(item) }
                                    .padding(horizontal = 12.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                androidx.compose.material3.Text(
                                    text = itemLabel(item),
                                    color = if (isSelected) colors.colorMixPrimary else colors.textColor,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 14.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                                )
                                if (isSelected) {
                                    androidx.compose.material3.Icon(
                                        imageVector = Icons.Rounded.Check,
                                        contentDescription = null,
                                        tint = colors.colorMixPrimary,
                                        modifier = Modifier.size(18.dp)
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

@Composable
fun PasswordTextFieldComponent(
    labelValue: String,
    painterResource: ImageVector,
    onTextSelected: (String) -> Unit,
    errorStatus: Boolean = false,
    initialValue: String = ""
) {
    val colors = getColorsTheme()
    var password by remember { mutableStateOf(initialValue) }
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        label = { Text(text = labelValue) },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = colors.textColor,
            focusedBorderColor = colors.colorMixPrimary,
            unfocusedBorderColor = colors.textColor.copy(alpha = ContentAlpha.disabled),
            focusedLabelColor = colors.colorMixPrimary,
            unfocusedLabelColor = colors.textColor.copy(alpha = ContentAlpha.medium),
            cursorColor = colors.colorMixPrimary,
            leadingIconColor = colors.textColor.copy(alpha = ContentAlpha.medium),
            trailingIconColor = colors.textColor.copy(alpha = ContentAlpha.medium),
            backgroundColor = colors.backGroundColor
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        value = password,
        onValueChange = {
            password = it
            onTextSelected(it)
        },
        leadingIcon = { Icon(painterResource, contentDescription = null) },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        isError = errorStatus
    )
}

@Composable
fun ButtonComponent(
    value: String,
    onButtonClicked: () -> Unit,
    isEnabled: Boolean = true,
    textSize: Int = 14,
    icon: Painter? = null,
    backgroundBrush: Brush? = null,
    backgroundColor: Color,
    contentColor: Color,
    borderRadius: Int = 12
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .padding(horizontal = 16.dp),
        onClick = onButtonClicked,
        shape = RoundedCornerShape(borderRadius.dp),
        enabled = isEnabled,
        elevation = ButtonDefaults.elevation(4.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = backgroundBrush ?: SolidColor(backgroundColor),
                    shape = RoundedCornerShape(borderRadius.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                icon?.let {
                    Icon(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp).padding(end = 12.dp),
                        tint = Color.Unspecified
                    )
                }
                Text(text = value, fontSize = textSize.sp, fontWeight = FontWeight.Medium, color = contentColor)
            }
        }
    }
}

@Composable
fun CheckboxComponent(
    value: String,
    onTextSelected: (String) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    initialValue: Boolean = true
) {
    val checkedState = remember { mutableStateOf(initialValue) }
    val colors = getColorsTheme()
    val shape = RoundedCornerShape(6.dp)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().heightIn(56.dp)
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(shape)
                .background(if (checkedState.value) colors.primary else Color.Transparent)
                .border(
                    width = 2.dp,
                    color = if (checkedState.value) colors.primary else colors.textColor.copy(alpha = 0.6f),
                    shape = shape
                )
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    checkedState.value = !checkedState.value
                    onCheckedChange(checkedState.value)
                },
            contentAlignment = Alignment.Center
        ) {
            if (checkedState.value) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        ClickableText(
            text = value,
            onClick = {
                checkedState.value = !checkedState.value
                onCheckedChange(checkedState.value)
                onTextSelected(value)
            }
        )
    }
}

@Composable
fun ClickableText(text: String, onClick: () -> Unit) {
    val colors = getColorsTheme()
    Text(
        text = text,
        color = colors.textColor,
        fontSize = 14.sp,
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun LoadingDialog() {
    Dialog(onDismissRequest = {}) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardTopBar(
    title: String,
    subtitle: String,
    onBackClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    titleFontSize: TextUnit = 18.sp
) {
    val colors = getColorsTheme()
    val navigating = remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        colors.colorAzulOscuro.copy(alpha = 0.8f),
                        colors.colorAzulOscuro
                    )
                )
            )
            .drawBehind {
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.White.copy(alpha = 0.2f),
                            Color.Transparent
                        )
                    ),
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx()
                )
            },
        navigationIcon = {
            IconButton(
                onClick = {
                    if (!navigating.value) {
                        navigating.value = true
                        onBackClick()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
        },
        title = {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text(
                    text = title,
                    fontSize = titleFontSize,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.body1,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.8f),
                    letterSpacing = 0.3.sp,
                    fontFamily = leCordonBleuFont(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
        },
        actions = {
            Icon(
                painter = painterResource(Res.drawable.ulcb_logo_circle),
                contentDescription = "Logo",
                tint = Color.White,
                modifier = Modifier.padding(end = 16.dp).size(36.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun ScrollToTopButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: pe.lecordonbleu.universidadestudiante.DarkModeColors = getColorsTheme()
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { it })
    ) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = colors.colorMixPrimary,
            contentColor = Color.White,
            shape = CircleShape,
            modifier = Modifier.size(48.dp)
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "Volver al inicio",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun StatusLabel(label: String, color: Color) {
    val colors = getColorsTheme()
    Row(verticalAlignment = Alignment.CenterVertically) {
        androidx.compose.material3.Text(label, color = colors.textColor)
        Spacer(modifier = Modifier.width(6.dp))
        Box(modifier = Modifier.size(12.dp).background(color, CircleShape))
    }
}

@Composable
fun LockedRow(label: String) {
    val colors = getColorsTheme()
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        androidx.compose.material3.Text(label, color = colors.textColor)
        AccionIconButton(
            icono = Icons.Default.Lock,
            colorFondo = Color.Gray,
            descripcion = "Bloqueado",
            onClick = {},
            habilitado = false
        )
    }
}

