package fr.jhelp.tablelayout.ui.composable.layout.table

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import kotlin.math.max

class TableLayoutScope
{
    private var index = 0
    internal val cells = mutableStateListOf<TableCell>()

    fun Modifier.cell(cellX: Int, cellY: Int, width: Int = 1, height: Int = 1): Modifier
    {
        val localWidth = max(1, width)
        val localHeight = max(1, height)

        if (this@TableLayoutScope.index >= this@TableLayoutScope.cells.size)
        {
            this@TableLayoutScope.cells.add(TableCell(cellX, cellY, localWidth, localHeight))
        }
        else
        {
            val cell = this@TableLayoutScope.cells[this@TableLayoutScope.index]

            if (cell.cellX != cellX || cell.cellY != cellY || cell.width != localWidth || cell.height != localHeight)
            {
                this@TableLayoutScope.cells[this@TableLayoutScope.index] = TableCell(cellX, cellY, localWidth, localHeight)
            }
        }

        this@TableLayoutScope.index++
        return this
    }

    fun seal()
    {
        if (this.cells.size > this.index)
        {
            this.cells.subList(this.index, this.cells.size).clear()
        }

        this.index = 0
    }
}