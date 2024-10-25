package fr.jhelp.tablelayout.ui.composable.layout.table

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TableLayout(modifier: Modifier = Modifier, content: @Composable TableLayoutScope.() -> Unit)
{
    val scope = TableLayoutScope()
    scope.content()

    // TODO layout composable here
}