package fr.jhelp.tablelayout.ui.composable.layout.table

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.layout.Layout

@Composable
fun TableLayout(modifier: Modifier = Modifier, content: @Composable TableLayoutScope.() -> Unit)
{
    val scope = TableLayoutScope()

    val layoutContent = @Composable @UiComposable {
        scope.content()
    }

    Layout(modifier = modifier, content = layoutContent,
           measurePolicy = { measurables, constraints ->
               TODO()

           })
}