package fr.jhelp.tablelayout

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import fr.jhelp.tablelayout.ui.composable.layout.table.TableLayout
import fr.jhelp.tablelayout.ui.theme.TableLayoutTheme

class MainActivity : ComponentActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TableLayoutTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                            )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier)
{
    var visible: Boolean by remember { mutableStateOf(true) }

    TableLayout(modifier = modifier) {
        Text(text = "Hello $name!",
             modifier = Modifier
                 .background(Color.Red)
                 .cell(0, 0, 2, 1))

        if (visible)
        {
            Text(text = "Small text",
                 modifier = Modifier
                     .background(Color.Blue)
                     .cell(0, 1))
        }

        Button(modifier = Modifier
            .background(Color.Green)
            .cell(1, 1),
               onClick = { visible = visible.not() }) {
            Text(text = "Click me")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview()
{
    TableLayoutTheme {
        Greeting("Android")
    }
}