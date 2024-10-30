package fr.jhelp.tablelayout.ui.composable.layout.table

import androidx.compose.runtime.Stable
import androidx.compose.ui.layout.Placeable

@Stable
internal data class TableCell(val cellX: Int, val cellY: Int, val width: Int, val height: Int)
{
    /** Computed x in the screen*/
    var x: Int = 0

    /** Computed y in the screen*/
    var y: Int = 0

    /** Computed width in the screen*/
    var realWidth: Int = 0

    /** Computed height in the screen*/
    var realHeight: Int = 0

    /** Placeable to used for place the component*/
    var placeable: Placeable? = null
}