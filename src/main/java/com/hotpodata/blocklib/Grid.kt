package com.hotpodata.blocklib

/**
 * Created by jdrotos on 12/19/15.
 */
class Grid(val width: Int, val height: Int) {

    //Grid set up as [x,y] or [col,row]
    val slots = Array(width, { Array<Any?>(height, { null }) });

    fun at(x: Int, y: Int): Any? {
        if (saneCoords(x, y)) {
            return slots[x][y]
        }
        return null
    }

    fun put(x: Int, y: Int, obj: Any?) {
        if (saneCoords(x, y)) {
            slots[x][y] = obj
        } else {
            throw RuntimeException("Invalid coordinates");
        }
    }

    fun remove(x: Int, y: Int): Any? {
        if (saneCoords(x, y)) {
            slots[x][y] = null
        }
        return null
    }

    fun clear() {
        for (col in slots) {
            for (i in col.indices) {
                col[i] = null
            }
        }
    }

    fun colEmpty(x: Int): Boolean {
        return !colOccupied(x)
    }

    fun colOccupied(x: Int): Boolean {
        if (saneX(x)) {
            for (i in slots[x]) {
                if (i != null) {
                    return true
                }
            }
            return false
        } else {
            throw RuntimeException("Invalid column");
        }
    }

    fun colFull(x: Int): Boolean {
        if (saneX(x)) {
            for (i in slots[x]) {
                if (i == null) {
                    return false
                }
            }
            return true
        } else {
            throw RuntimeException("Invalid column");
        }
    }

    fun rowEmpty(y: Int): Boolean {
        return !rowOccupied(y);
    }

    fun rowOccupied(y: Int): Boolean {
        if (saneY(y)) {
            for (col in slots) {
                if (col[y] != null) {
                    return true
                }
            }
            return false
        } else {
            throw RuntimeException("Invalid row");
        }
    }

    fun rowFull(y: Int): Boolean {
        if (saneY(y)) {
            for (col in slots) {
                if (col[y] == null) {
                    return false
                }
            }
            return true
        } else {
            throw RuntimeException("Invalid row");
        }
    }

    fun saneCoords(x: Int, y: Int): Boolean {
        return saneX(x) && saneY(y)
    }

    fun saneX(x: Int): Boolean {
        return x >= 0 && x < width;
    }

    fun saneY(y: Int): Boolean {
        return y >= 0 && y < height;
    }


    fun rotate(left: Boolean): Grid {
        var ret = Grid(height, width)

        //First transpose
        for (i in slots.indices) {
            for (j in slots[i].indices) {
                ret.put(j, i, at(i, j))
            }
        }

        if (left) {
            //reverse row order for left rotation
            for (col in ret.slots) {
                col.reverse()
            }
        } else {
            //reverse column order for right rotation
            ret.slots.reverse()
        }
        return ret
    }

    fun getPrintString(blank: String, full: String): String {
        return getPrintString(blank, { x -> full })
    }

    fun getPrintString(blank: String, dataToStrFunc: (Any) -> String): String {
        var builder = StringBuilder()
        for (i in 0..height - 1) {
            for (j in 0..width - 1) {
                builder.append(if (slots[j][i] == null) blank else dataToStrFunc(slots[j][i]!!))
            }
            builder.append("\n")
        }
        return builder.toString()
    }
}