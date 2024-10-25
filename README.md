# Table layout in compose

Aim is to create a table layout in compose and explain how it was made steps by steps.

It is an example of custom layout creation in compose.

Menu:
* [What is a table layout ?](#what-is-a-table-layout-)
* [Create data classes](#create-data-classes)
* [Do the layout](#do-the-layout-)
  * [Scope creation](#scope-creation)
  * [Other DSL things](#other-dsl-things)
  * [How to use androidx.compose.ui.layout.Layout](#how-to-use-androidxcomposeuilayoutlayout)
  * [Measure the preferred component size](#measure-the-preferred-component-size)
  * [Resolve components final size and location](#resolve-components-final-size-and-location)
  * [Force compose use computed size](#force-compose-use-computed-size)
  * [Place the components](#place-the-components)
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

## Do the layout 

* [Scope creation](#scope-creation)
* [Other DSL things](#other-dsl-things)
* [How to use androidx.compose.ui.layout.Layout](#how-to-use-androidxcomposeuilayoutlayout)
* [Measure the preferred component size](#measure-the-preferred-component-size)
* [Resolve components final size and location](#resolve-components-final-size-and-location)
* [Force compose use computed size](#force-compose-use-computed-size)
* [Place the components](#place-the-components)

### Scope creation

### Other DSL things

### How to use androidx.compose.ui.layout.Layout

### Measure the preferred component size

### Resolve components final size and location

### Force compose use computed size

### Place the components

## Usage example
