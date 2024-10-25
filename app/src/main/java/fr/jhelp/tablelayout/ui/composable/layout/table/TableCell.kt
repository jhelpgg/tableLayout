package fr.jhelp.tablelayout.ui.composable.layout.table

import kotlin.math.max

class TableCell(val cellX: Int, val cellY: Int, width: Int = 1, height: Int = 1)
{
    val width = max(1, width)
    val height = max(1, height)

    /** Computed x in the screen*/
    internal var x: Int = 0

    /** Computed y in the screen*/
    internal var y: Int = 0

    /** Computed width in the screen*/
    internal var realWidth: Int = 0

    /** Computed height in the screen*/
    internal var realHeight: Int = 0
}