package fr.jhelp.tablelayout.ui.composable.layout.table

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import kotlin.math.max
import kotlin.math.min

class TableLayoutScope
{
    private var index = 0
    internal var minCellX = Int.MAX_VALUE
    internal var maxCellX = Int.MIN_VALUE
    internal var minCellY = Int.MAX_VALUE
    internal var maxCellY = Int.MIN_VALUE

    internal val cells = mutableStateListOf<TableCell>()

    fun Modifier.cell(cellX: Int, cellY: Int, width: Int = 1, height: Int = 1): Modifier
    {
        val localWidth = max(1, width)
        val localHeight = max(1, height)

        if (this@TableLayoutScope.index == 0)
        {
            this@TableLayoutScope.minCellX = cellX
            this@TableLayoutScope.maxCellX = cellX + localWidth
            this@TableLayoutScope.minCellY = cellY
            this@TableLayoutScope.maxCellY = cellY + localHeight
        }
        else
        {
            this@TableLayoutScope.minCellX = min(this@TableLayoutScope.minCellX, cellX)
            this@TableLayoutScope.maxCellX = max(this@TableLayoutScope.maxCellX, cellX + localWidth)
            this@TableLayoutScope.minCellY = min(this@TableLayoutScope.minCellY, cellY)
            this@TableLayoutScope.maxCellY = max(this@TableLayoutScope.maxCellY, cellY + localHeight)
        }

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

        this@TableLayoutScope.cells[this@TableLayoutScope.index].placeable = null
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