package com.hotpodata.blocklib

/**
 * Created by jdrotos on 12/20/15.
 */
object GridHelper {
    fun gridsCollide(ref: Grid, obj: Grid, xOffset: Int, yOffset: Int): Boolean {
        if (xOffset + obj.width < 0 || xOffset > ref.width) {
            return false
        } else if (yOffset + obj.height < 0 || yOffset > ref.height) {
            return false
        } else {
            for (i in obj.slots.indices) {
                for (j in obj.slots[i].indices) {
                    if (obj.at(i, j) != null) {
                        val x = i + xOffset;
                        val y = j + yOffset;
                        if (ref.saneCoords(x, y) && ref.at(x, y) != null) {
                            return true
                        }
                    }
                }
            }
            return false
        }
    }

    fun gridInBounds(ref: Grid, obj: Grid, xOffset: Int, yOffset: Int): Boolean {
        if (xOffset + obj.width < 0 || xOffset > ref.width) {
            return false
        } else if (yOffset + obj.height < 0 || yOffset > ref.height) {
            return false
        } else {
            for (i in obj.slots.indices) {
                for (j in obj.slots[i].indices) {
                    if (obj.at(i, j) != null) {
                        val x = i + xOffset;
                        val y = j + yOffset;
                        if (x < 0 || x >= ref.width || y < 0 || y >= ref.height) {
                            return false
                        }
                    }
                }
            }
            return true
        }
    }

    fun addGrid(target: Grid, obj: Grid, xOffset: Int, yOffset: Int) {
        for (i in obj.slots.indices) {
            for (j in obj.slots[i].indices) {
                if (obj.at(i, j) != null) {
                    val x = i + xOffset;
                    val y = j + yOffset;
                    if (target.saneCoords(x, y)) {
                        target.put(x, y, obj.at(i, j))
                    }
                }
            }
        }
    }

    fun copyGrid(obj: Grid): Grid {
        var grid = Grid(obj.width, obj.height)
        addGrid(grid, obj, 0, 0)
        return grid
    }

    fun copyGridPortion(src: Grid, left: Int, top: Int, right: Int, bottom: Int): Grid {
        var grid = Grid(right - left, bottom - top)
        if (gridInBounds(src, grid, left, top)) {
            for (i in left..right - 1) {
                for (j in top..bottom - 1) {
                    grid.put(i - left, j - top, src.at(i, j))
                }
            }
        }
        return grid
    }
}