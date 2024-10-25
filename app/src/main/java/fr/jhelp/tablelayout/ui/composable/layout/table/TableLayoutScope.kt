package fr.jhelp.tablelayout.ui.composable.layout.table

import androidx.compose.ui.Modifier

class TableLayoutScope
{
    internal val cells = ArrayList<TableCell>()

    fun Modifier.cell(cellX: Int, cellY: Int, width: Int = 1, height: Int = 1)
    {
        this@TableLayoutScope.cells.add(TableCell(cellX, cellY, width, height))
    }
}