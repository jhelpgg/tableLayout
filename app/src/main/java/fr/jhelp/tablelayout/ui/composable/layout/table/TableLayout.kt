package fr.jhelp.tablelayout.ui.composable.layout.table

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.layout.Layout
import kotlin.math.max
import kotlin.math.min

@Composable
fun TableLayout(modifier: Modifier = Modifier, content: @Composable TableLayoutScope.() -> Unit)
{
    val scope = TableLayoutScope()

    val layoutContent = @Composable @UiComposable {
        scope.content()
    }

    Layout(modifier = modifier, content = layoutContent,
           measurePolicy = { measurables, constraints ->
               val parentWidth = constraints.maxWidth
               val parentHeight = constraints.maxHeight
               var minCellX = Int.MAX_VALUE
               var maxCellX = Int.MIN_VALUE
               var minCellY = Int.MAX_VALUE
               var maxCellY = Int.MIN_VALUE

               // Measure components preferred size
               for ((index, measurable) in measurables.withIndex())
               {
                   val cell = scope.cells[index]

                   cell.realWidth = measurable.maxIntrinsicWidth(parentWidth)
                   cell.realHeight = measurable.maxIntrinsicHeight(parentHeight)
                   minCellX = min(minCellX, cell.cellX)
                   maxCellX = max(maxCellX, cell.cellX + cell.width)
                   minCellY = min(minCellY, cell.cellY)
                   maxCellY = max(maxCellY, cell.cellY + cell.height)
               }

               val cellWidth = if(maxCellX>minCellX)  parentWidth / (maxCellX - minCellX) else 1
               val cellHeight = if(maxCellX>minCellY)  parentHeight /  (maxCellY - minCellY) else 1

               TODO()
           })
}