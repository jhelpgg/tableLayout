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
               scope.seal()

               // Compute cell dimension
               val parentWidth = constraints.maxWidth
               val parentHeight = constraints.maxHeight

               val cellWidth = if (scope.maxCellX > scope.minCellX) parentWidth / (scope.maxCellX - scope.minCellX) else 1
               val cellHeight = if (scope.maxCellX > scope.minCellY) parentHeight / (scope.maxCellY - scope.minCellY) else 1

               // Compute components position and dimension
               for (cell in scope.cells)
               {
                   cell.x = (cell.cellX - scope.minCellX) * cellWidth
                   cell.y = (cell.cellY - scope.minCellY) * cellHeight
                   cell.realWidth = cellWidth * cell.width
                   cell.realHeight = cellHeight * cell.height
               }

               // Force compose use computed size
               for ((index, measurable) in measurables.withIndex())
               {
                   val cell = scope.cells[index]

                   cell.placeable = measurable.measure(
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