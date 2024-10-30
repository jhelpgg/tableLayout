package fr.jhelp.tablelayout.ui.composable.layout.table

import android.util.Log
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
               scope.seal()

               // Compute cell dimension
               val parentWidth = constraints.maxWidth
               val parentHeight = constraints.maxHeight
               var minCellX = Int.MAX_VALUE
               var maxCellX = Int.MIN_VALUE
               var minCellY = Int.MAX_VALUE
               var maxCellY = Int.MIN_VALUE

               for (cell in scope.cells)
               {
                   minCellX = min(minCellX, cell.cellX)
                   maxCellX = max(maxCellX, cell.cellX + cell.width)
                   minCellY = min(minCellY, cell.cellY)
                   maxCellY = max(maxCellY, cell.cellY + cell.height)
               }

               val cellWidth = if (maxCellX > minCellX) parentWidth / (maxCellX - minCellX) else 1
               val cellHeight = if (maxCellX > minCellY) parentHeight / (maxCellY - minCellY) else 1

               // Compute components position and dimension
               for (cell in scope.cells)
               {
                   cell.x = (cell.cellX - minCellX) * cellWidth
                   cell.y = (cell.cellY - minCellY) * cellHeight
                   cell.realWidth = cellWidth * cell.width
                   cell.realHeight = cellHeight * cell.height
               }

               // Force compose use computed size
               for ((index, measurable) in measurables.withIndex())
               {
                   val cell = scope.cells[index]

                   cell.placeable= measurable.measure(
                       constraints.copy(
                           minWidth = cell.realWidth,
                           minHeight = cell.realHeight,
                           maxWidth = cell.realWidth,
                           maxHeight = cell.realHeight
                                       )
                                                     )
               }

               //Place components
               layout(parentWidth, parentHeight)
               {
                   for (cell in scope.cells)
                   {
                       cell.placeable?.place(cell.x, cell.y)
                   }
               }
           })
}