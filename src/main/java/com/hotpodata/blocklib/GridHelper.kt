package com.hotpodata.blocklib

import android.graphics.Rect

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

    fun subtractGrid(target: Grid, obj: Grid, xOffset: Int, yOffset: Int) {
        for (i in obj.slots.indices) {
            for (j in obj.slots[i].indices) {
                if (obj.at(i, j) != null) {
                    val x = i + xOffset;
                    val y = j + yOffset;
                    if (target.saneCoords(x, y)) {
                        target.put(x, y, null)
                    }
                }
            }
        }
    }

    fun maskGrid(src: Grid, maskValue: Any): Grid {
        var grid = Grid(src.width, src.height)
        for (i in src.slots.indices) {
            for (j in src.slots[i].indices) {
                if (src.at(i, j) != null) {
                    grid.put(i, j, maskValue)
                }
            }
        }
        return grid
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

    fun numEmptyColsLeftRight(grid: Grid): Pair<Int, Int> {
        var firstColFromLeft = 0
        while (grid.colEmpty(firstColFromLeft) && firstColFromLeft < grid.width) {
            firstColFromLeft++
        }
        var firstColFromRight = 0
        while (grid.colEmpty(grid.width - 1 - firstColFromRight) && grid.width - 1 - firstColFromRight > 0) {
            firstColFromRight++
        }
        return Pair(firstColFromLeft, firstColFromRight)
    }

    fun numEmptyRowsTopBtm(grid: Grid): Pair<Int, Int> {
        var firstRowFromTop = 0
        while (grid.rowEmpty(firstRowFromTop) && firstRowFromTop < grid.height) {
            firstRowFromTop++
        }
        var firstRowFromBtm = 0
        while (grid.rowEmpty(grid.height - 1 - firstRowFromBtm) && grid.height - 1 - firstRowFromBtm > 0) {
            firstRowFromBtm++
        }
        return Pair(firstRowFromTop, firstRowFromBtm)
    }

    fun trim(grid: Grid): Grid {
        var vertPad = numEmptyRowsTopBtm(grid)
        var horiPad = numEmptyColsLeftRight(grid)
        var ret = Grid(grid.width - horiPad.first - horiPad.second, grid.height - vertPad.first - vertPad.second)
        addGrid(ret, copyGridPortion(grid, horiPad.first, vertPad.first, grid.width - horiPad.second, grid.height - vertPad.second), 0, 0)
        return ret
    }

    fun findInGrid(grid: Grid, target: Any): Rect? {
        var minX = grid.width
        var maxX = -1
        var minY = grid.height
        var maxY = -1
        for (i in 0..grid.width - 1) {
            for (j in 0..grid.height - 1) {
                if (grid.at(i, j) == target) {
                    if (i < minX) {
                        minX = i
                    }
                    if (i > maxX) {
                        maxX = i
                    }
                    if (j < minY) {
                        minY = j
                    }
                    if (j > maxY) {
                        maxY = j
                    }
                }
            }
        }
        if (grid.saneCoords(minX, minY) && grid.saneCoords(maxX, maxY)) {
            return Rect(minX, minY, maxX, maxY)
        }
        return null
    }
}