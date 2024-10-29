# Table layout in compose

Aim is to create a table layout in compose and explain how it was made steps by steps.

It is an example of custom layout creation in compose.

Menu:
* [What is a table layout ?](#what-is-a-table-layout-)
* [Create data classes](#create-data-classes)
* [Do the layout](#do-the-layout-)
  * [Scope creation](#scope-creation)
  * [How to use androidx.compose.ui.layout.Layout](#how-to-use-androidxcomposeuilayoutlayout)
  * [Measure the preferred component size](#measure-the-preferred-component-size)
  * [Resolve components final size and location](#resolve-components-final-size-and-location)
  * [Force compose use computed size](#force-compose-use-computed-size)
  * [Place the components](#place-the-components)
  * [The hidden issue and its correction](#the-hidden-issue-and-its-correction)
* [Usage example](#usage-example)

## What is a table layout ?

Table layout is place components inside a grid. Let imagine the parent component is cut in grid like this :
![Screen cut in grid](doc/EmptyScreen.png)

The we can place components inside like this :
![Screen with some component inside](doc/Exemple1.png)

* The component 1 (in yellow) it top left corner is at (2, 30) and takes 9 x 2 cells
* The component 2 (in green) it top left corner is at (4, 16) and takes 12 x 4 cells
* The component 3 (in blue) it top left corner is at (0, 0) and takes 8 x 10 cells
* The component 4 (in red) it top left corner is at (8, 0) and takes 5 x 14 cells

## Create data classes

We will need to store the each components table cell information, for this we can create the class

```kotlin
class TableCell(val cellX: Int, val cellY: Int, val width: Int = 1, val height: Int = 1)
```

We will want avoid width and height under 1, so we change to

```kotlin
import kotlin.math.max

class TableCell(val cellX: Int, val cellY: Int, width: Int = 1, height: Int = 1)
{
    val width = max(1, width)
    val height = max(1, height)
}
```

We will then after have to resolve the real location in parent and computed size. 
This information can be internal since it will be used and computed be the table layout and 
we don't want someone change them by accident.

The class becomes :
```kotlin
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
```

## Do the layout

For create the layout, we well need a scope that store components constraints. 
We want have something that look likes other layout. 
That is something that looks like
```kotlin
TableLayout(modifier = modifer) {
  Text("", 
       modifier=Modifier.cell(cellX = 2, cellY = 2))

  Button(modifier=Modifier.cell(cellX = 5, cellY = 6, width = 12, height = 23)) { 
      Text("Click")
  }
}
```

For this, we will create a scope, is role will to make available the `cell` function and 
store the TableCell information. 

Here we face the first issue, composable are in function, not inside an object, 
we don't have a hand on components draw by the composable function. 
We will store the information in order they are declares, by chance the list wil will receive later
in this tutorial have the same order, so we can match by using the index.

* [Scope creation](#scope-creation)
* [How to use androidx.compose.ui.layout.Layout](#how-to-use-androidxcomposeuilayoutlayout)
* [Measure the preferred component size](#measure-the-preferred-component-size)
* [Resolve components final size and location](#resolve-components-final-size-and-location)
* [Force compose use computed size](#force-compose-use-computed-size)
* [Place the components](#place-the-components)
* [The hidden issue and its correction](#the-hidden-issue-and-its-correction)
* [Optimisation](#optimisation)

### Scope creation

Since `TableLayoutScope` will be called in **DSL** way, we can add some local extensions.
The first version of `TableLayoutScope` will be

```kotlin
import androidx.compose.ui.Modifier

class TableLayoutScope
{
    internal val cells = ArrayList<TableCell>()

    fun Modifier.cell(cellX: Int, cellY: Int, width: Int = 1, height: Int = 1)
    {
        this@TableLayoutScope.cells.add(TableCell(cellX, cellY, width, height))
    }
}
```

And the start of the `TableLayout` method is 

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TableLayout(modifier: Modifier = Modifier, content: @Composable TableLayoutScope.() -> Unit)
{
    val scope = TableLayoutScope()
    scope.content()

    // TODO layout composable here
}
```

It may look good, but as we will see in [How to use androidx.compose.ui.layout.Layout](#how-to-use-androidxcomposeuilayoutlayout)
we will need change the lines before the comment.

And in [The hidden issue and its correction](#the-hidden-issue-and-its-correction) their an hidden issue
in `TableLayoutScope`, that will force us to add a trick.

### How to use androidx.compose.ui.layout.Layout

For layout the components we will use `androidx.compose.ui.layout.Layout`.

So wil will write :

```kotlin
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun TableLayout(modifier: Modifier = Modifier, content: @Composable TableLayoutScope.() -> Unit)
{
    val scope = TableLayoutScope()
    scope.content()

    Layout(modifier = modifier, content = content,
           measurePolicy = { measurables, constraints ->
                TODO()
           })
}
```

But we notice that `content` can't be used for two reasons :
* It is already called before, but we want it is called by layout with our scope
* Thew function gives to Layout must also be `@UiComposable` for `Layout` work properly

To solve this we change the code to

```kotlin
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
```

With this way, `content` used the scope and be called by layout since we give it a function to call.
More over, we use it to have a `@Composabale` that is also a `@UiComposable`

The lambda function gives to the `measurePolicy` parameter will do the layout.
It have two values : 
* `measurables` : List of components to measure (in their creation order). 
  They are the composable defines in the `TableLayoutScope`
* `constraints` : Are the parents constraints : that is to say the minimum and maximum size we can use

The measure policy requires to return a `MeasureResult`, that contains the measured components.

We will have it by call the `layout` method that will place components.
Note :
> `layout` method must be called only one time and in preference at last things do.

### Measure the preferred component size

To layout the component we will need a cell dimension.
For this we compute the minimum and maximum cell position.

```kotlin
          // Compute cell dimension
          val parentWidth = constraints.maxWidth
          val parentHeight = constraints.maxHeight
          var minCellX = Int.MAX_VALUE
          var maxCellX = Int.MIN_VALUE
          var minCellY = Int.MAX_VALUE
          var maxCellY = Int.MIN_VALUE
          
          // Measure components preferred size
          for (cell in scope.cells)
          {
            minCellX = min(minCellX, cell.cellX)
            maxCellX = max(maxCellX, cell.cellX + cell.width)
            minCellY = min(minCellY, cell.cellY)
            maxCellY = max(maxCellY, cell.cellY + cell.height)
          }
          
          val cellWidth = if (maxCellX > minCellX) parentWidth / (maxCellX - minCellX) else 1
          val cellHeight = if (maxCellX > minCellY) parentHeight / (maxCellY - minCellY) else 1
```

Note:
> For some case you may need to have the components preferred size, 
> to get them you can do :

```kotlin
               for ((index, measurable) in measurables.withIndex())
               {
                   val cell = scope.cells[index]
                   cell.preferredWidth = measurable.maxIntrinsicWidth(parentWidth)
                   cell.preferredHeight = measurable.maxIntrinsicHeight(parentHeight)
               }
```

> With this you can store temporary the measured preferred size.
> And if you want be clever you can use this loop to compute something else in same time like

```kotlin
               for ((index, measurable) in measurables.withIndex())
               {
                   val cell = scope.cells[index]
                   cell.preferredWidth = measurable.maxIntrinsicWidth(parentWidth)
                   cell.preferredHeight = measurable.maxIntrinsicHeight(parentHeight)
                   minCellX = min(minCellX, cell.cellX)
                   maxCellX = max(maxCellX, cell.cellX + cell.width)
                   minCellY = min(minCellY, cell.cellY)
                   maxCellY = max(maxCellY, cell.cellY + cell.height)
               }
```
> By example

### Resolve components final size and location

Now we can compute components location and dimension

```kotlin
          // Compute components position and dimension
          for (cell in scope.cells)
          {
            cell.x = (cell.cellX - minCellX) * cellWidth
            cell.y = (cell.cellY - minCellY) * cellHeight
            cell.realWidth = cellWidth * cell.width
            cell.realHeight = cellHeight * cell.height
          }
```

### Force compose use computed size

By default `Layout`will use the component preferred size. 
But we want it use our computed size.
To do so we will force it to measure components with our constraints.
The trick is to not give compose a choice on giving it as minimum and maximum the value we want.

```kotlin
               // Force compose use computed size
               for ((index, measurable) in measurables.withIndex())
               {
                   val cell = scope.cells[index]

                   val placeable = measurable.measure(
                       constraints.copy(
                           minWidth = cell.realWidth,
                           minHeight = cell.realHeight,
                           maxWidth = cell.realWidth,
                           maxHeight = cell.realHeight
                                       )
                                                     )
               }
```

Note : 
> we use a copy of the constraints with modification, 
> because it is the most simple way to have an instance with the value we want. 

For the last step we will need the computed `placeable` to place it.
So we add this field to `TableCell` and store it this loop.

```kotlin
import androidx.compose.ui.layout.Placeable
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

    /** Placeable to used for place the component*/
    internal var placeable: Placeable? = null
}
```

And the loop become :

```kotlin
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
```

### Place the components

### The hidden issue and its correction

### Optimisation

## Usage example
