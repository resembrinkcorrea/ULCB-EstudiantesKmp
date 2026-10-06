package pe.lecordonbleu.universidadestudiante.presentation.components.mainComponent

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import ulcbintranetkmp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.painterResource
import pe.lecordonbleu.universidadestudiante.DarkModeColors
import pe.lecordonbleu.universidadestudiante.getAppVersion
import ulcbintranetkmp.composeapp.generated.resources.logo_ulcb_anniversary
import ulcbintranetkmp.composeapp.generated.resources.ulcb_logo_circle
import ulcbintranetkmp.composeapp.generated.resources.ulcb_logo_white

@Composable
fun MainDrawerContent(
    userNombre: String,
    userApellido: String,
    userEmail: String,
    userFoto: String,
    colors: DarkModeColors,
    onCloseDrawer: () -> Unit,
    onLogoutClick: () -> Unit,
    onGoProfile: () -> Unit,
    showProximasClases: Boolean = true,
    onProximasClasesToggle: (Boolean) -> Unit = {},
    showArchivosObligatorios: Boolean = true,
    onArchivosObligatoriosToggle: (Boolean) -> Unit = {}
) {
    ModalDrawerSheet(
        drawerContainerColor = Color.Transparent,
        windowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        modifier = Modifier.width(300.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.colorBlancoGris)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    colors.colorAzulOscuro,
                                    colors.colorAzulOscuro.copy(alpha = 0.8f)
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
                            )
                        )
                        .drawBehind {
                            drawLine(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.White.copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 1.dp.toPx()
                            )
                        }
                        .padding(top = 48.dp, bottom = 32.dp, start = 20.dp, end = 20.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.ulcb_logo_white),
                            contentDescription = "Logo ULCB",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxWidth(0.85f).height(160.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.Transparent,
                            border = BorderStroke(
                                width = 2.dp,
                                brush = Brush.sweepGradient(
                                    colors = listOf(
                                        colors.colorMixPrimary,
                                        colors.colorAmbar,
                                        colors.colorMixPrimary
                                    )
                                )
                            ),
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                AsyncImage(
                                    model = userFoto.ifEmpty { Res.drawable.ulcb_logo_circle },
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(colors.colorExpenseItem),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "$userNombre $userApellido".trim(),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            lineHeight = 20.sp,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = userEmail,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 13.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "v${getAppVersion()}",
                            color = Color.White.copy(alpha = 0.4f),
                            fontSize = 11.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Surface(
                    onClick = { onGoProfile() },
                    shape = RoundedCornerShape(16.dp),
                    color = colors.colorMixPrimary.copy(alpha = 0.1f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = colors.colorMixPrimary.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .height(56.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Ir al perfil",
                            tint = colors.colorMixPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Mi Perfil",
                            color = colors.colorMixPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "DASHBOARD",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.textColor.copy(alpha = 0.4f),
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    DrawerToggleItem(
                        label = "Próximas clases",
                        icon = { Icon(Icons.Rounded.CalendarMonth, null, tint = colors.colorMixPrimary, modifier = Modifier.size(20.dp)) },
                        checked = showProximasClases,
                        onCheckedChange = onProximasClasesToggle,
                        colors = colors
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    DrawerToggleItem(
                        label = "Archivos obligatorios",
                        icon = { Icon(Icons.Default.Folder, null, tint = colors.colorRojo, modifier = Modifier.size(20.dp)) },
                        checked = showArchivosObligatorios,
                        onCheckedChange = onArchivosObligatoriosToggle,
                        colors = colors
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    onClick = {
                        onCloseDrawer()
                        onLogoutClick()
                    },
                    shape = RoundedCornerShape(16.dp),
                    color = colors.colorNaranjaOscuro.copy(alpha = 0.1f),
                    border = BorderStroke(
                        width = 1.dp,
                        color = colors.colorNaranjaOscuro.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(bottom = 32.dp)
                        .height(56.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar Sesión",
                            tint = colors.colorNaranjaOscuro,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Cerrar Sesión",
                            color = colors.colorNaranjaOscuro,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DrawerToggleItem(
    label: String,
    icon: @Composable () -> Unit,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    colors: DarkModeColors
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            icon()
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = colors.textColor
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = colors.colorBlancoGris,
                checkedTrackColor = colors.colorMixPrimary,
                uncheckedThumbColor = colors.colorGrisNeutro,
                uncheckedTrackColor = colors.colorGrisNeutro.copy(alpha = 0.3f)
            )
        )
    }
}
