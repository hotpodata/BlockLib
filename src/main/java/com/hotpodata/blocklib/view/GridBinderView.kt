package com.hotpodata.blocklib.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hotpodata.blocklib.Grid
import java.util.*

/**
 * Created by jdrotos on 12/20/15.
 */
class GridBinderView : View {

    public interface IBlockDrawer {
        fun drawBlock(canvas: Canvas, data: Any)
    }

    public open class RedBlockDrawer() : IBlockDrawer {
        override fun drawBlock(canvas: Canvas, data: Any) {
            canvas.drawColor(Color.RED)
        }
    }

    private var _gridPaint: Paint? = null
    val gridPaint: Paint
        get() {
            var paint = _gridPaint
            if (paint == null) {
                paint = Paint()
                paint.isAntiAlias = true
                paint.style = Paint.Style.STROKE
                paint.strokeCap = Paint.Cap.SQUARE
                paint.color = gridColor
                paint.strokeWidth = gridWidth
                _gridPaint = paint
            }
            return paint
        }


    var grid: Grid? = null
        set(g: Grid?) {
            field = g
            postInvalidate()
        }

    var blockDrawer: IBlockDrawer = RedBlockDrawer()
        set(drawer: IBlockDrawer) {
            field = drawer
            postInvalidate()
        }

    var gridColor: Int = Color.BLACK
        set(color: Int) {
            field = color
            _gridPaint = null
            postInvalidate()
        }

    var gridWidth: Float = 0f
        set(width: Float) {
            field = width
            _gridPaint = null
            postInvalidate()
        }


    public constructor(context: Context) : super(context) {
        init(context, null)
    }

    public constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    public constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init(context, attrs)
    }

    public constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var g = grid
        if (g != null) {
            var drawer = blockDrawer
            var boxWidth = (width - 2 * gridPaint.strokeWidth) / g.width
            var boxHeight = (height - 2 * gridPaint.strokeWidth) / g.height
            for (i in g.slots.indices) {
                //X
                for (j in g.slots[i].indices) {
                    //Y
                    var data = g.at(i, j)
                    if (data != null) {
                        //Clip the canvas
                        canvas.clipRect(gridPaint.strokeWidth + i * boxWidth.toFloat(), gridPaint.strokeWidth + j * boxHeight.toFloat(), gridPaint.strokeWidth + (i + 1) * boxWidth.toFloat(), gridPaint.strokeWidth + (j + 1) * boxHeight.toFloat(), Region.Op.REPLACE);
                        //And then draw
                        drawer.drawBlock(canvas, data)
                    }
                }
            }

            if (gridPaint.strokeWidth > 0) {
                var endHeight = g.height * boxHeight.toFloat()
                var endWidth = g.width * boxWidth.toFloat()
                for (i in 0..g.width) {
                    //X
                    var x = i * boxWidth.toFloat() + gridPaint.strokeWidth / 2f
                    canvas.drawLine(x, 0f, x, endHeight, gridPaint)
                }
                for (i in 0..g.height) {
                    //Y
                    var y = i * boxHeight.toFloat() + gridPaint.strokeWidth / 2f
                    canvas.drawLine(0f, y, endWidth, y, gridPaint)
                }
            }
        }
    }

    public fun getSubGridPosition(subGrid: Grid, xOffset: Int, yOffset: Int): RectF {
        var g = grid
        if (g != null) {
            var boxWidth = (width - 2 * gridPaint.strokeWidth) / g.width
            var boxHeight = (height - 2 * gridPaint.strokeWidth) / g.height
            var startX = gridPaint.strokeWidth + xOffset * boxWidth
            var endX = startX + subGrid.width * boxWidth
            var startY = gridPaint.strokeWidth + yOffset * boxHeight
            var endY = startY + subGrid.height * boxHeight
            return RectF(startX, startY, endX, endY)
        }
        return RectF()
    }


}