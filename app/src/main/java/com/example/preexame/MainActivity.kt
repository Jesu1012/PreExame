package com.example.preexame


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.Button
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.preexame.ui.theme.PreExameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PreExameTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EditarImagenPerfil(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun EditarImagenPerfil(modifier: Modifier) {
    var indiceFormas by remember { mutableStateOf(0) }
    var indiceGradienteFormas by remember { mutableStateOf(0) }
    var colores by remember { mutableStateOf(listOf(Color.Blue, Color.Magenta, Color.Cyan)) }
    val formas = listOf(
        FormaCorte("Circle", CircleShape),
        FormaCorte("Rounded Corner", RoundedCornerShape(12.dp)),
        FormaCorte("Cut Corner", CutCornerShape(12.dp))
    )
    val tipoGradientes = listOf(
        TipoGradiente("Radial", { Brush.radialGradient(colors = colores, center = Offset(100f, 100f)) }),
        TipoGradiente("Horizontal", { Brush.horizontalGradient(colors = colores) }),
        TipoGradiente("Vertical", { Brush.verticalGradient(colors = colores) })
    )
    val formaActual = formas[indiceFormas].forma
    val brushActual = tipoGradientes[indiceGradienteFormas].creaBrush()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Editar imagen de perfil", style = MaterialTheme.typography.displaySmall)
        Spacer(Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.dog),
            contentDescription = "Imagen de perfil",
            modifier = Modifier
                .size(150.dp)
                .border(10.dp, brushActual, shape = formaActual)
                .clip(formaActual)
                .padding(10.dp)
        )
        Spacer(Modifier.height(10.dp))
        Row {
            Button(onClick = {
                colores = listOf(Color.aleatorio(), Color.aleatorio(), Color.aleatorio())
            }) {
                Text("Cambiar color")
            }
            Spacer(Modifier.width(10.dp))
            SelectorDropdown(tipoGradientes.map { it.nombre }, indiceGradienteFormas) { index ->
                indiceGradienteFormas = index
            }
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = {
            indiceFormas = (indiceFormas + 1) % formas.size
        }) {
            Text("Cambiar forma")
        }
    }
}

@Composable
fun SelectorDropdown(
    opciones: List<String>,
    indiceElegido: Int,
    cambioSeleccion: (Int) -> Unit
) {
    var verDropdown by remember { mutableStateOf(false) }
    Box {
        Button(onClick = { verDropdown = true }) {
            Text(opciones[indiceElegido])
        }
        DropdownMenu(
            expanded = verDropdown,
            onDismissRequest = { verDropdown = false }
        ) {
            opciones.forEachIndexed { index, opcion ->
                DropdownMenuItem(onClick = {
                    cambioSeleccion(index)
                    verDropdown = false
                }, text = { Text(opcion)})
            }
        }
    }
}

fun Color.Companion.aleatorio(): Color = Color((0xFF000000..0xFFFFFFFF).random())
data class FormaCorte(val nombre: String, val forma: Shape)
data class TipoGradiente(val nombre: String, val creaBrush:()-> Brush)

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PreExameTheme {
        EditarImagenPerfil(modifier = Modifier)
    }
}