package www.xuzhiguang.com.writeonscreen

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.R.attr.y
import android.R.attr.x


class LineView : View {
    private var preX = 0.0F
    private var preY = 0.0F
    private var currentX = 0.0F
    private var currentY = 0.0F
    private var bitMapBuffer: Bitmap? = null
    private var bitMapCanvas: Canvas? = null
    private var paint: Paint? = null
    private var path: Path? = null

    //自定义的组件初始化时，初始化path、paint等
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint?.style = Paint.Style.STROKE
        paint?.color = Color.RED
        paint?.strokeWidth = 15F
        path = Path()
    }

    //布局在初始化后布局大小会改变
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (bitMapBuffer == null) {
            var width = measuredWidth
            var height = measuredHeight

            bitMapBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitMapCanvas = Canvas()
            bitMapCanvas?.setBitmap(bitMapBuffer)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawBitmap(bitMapBuffer, 0F, 0F, null)
        canvas!!.drawPath(path, paint)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var eventX = event?.x
        var eventY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                path?.reset()
                preX = eventX!!
                preY = eventY!!
                path?.moveTo(eventX, eventY)

            }

            MotionEvent.ACTION_MOVE -> {
                currentX = eventX!!
                currentY = eventY!!
                val controlX = (eventX + preX) / 2
                val controlY = (eventY + preY) / 2
                path?.quadTo(controlX, controlY, currentX, currentY)
                invalidate()
                preX = currentX
                preY = currentY
            }

            MotionEvent.ACTION_UP -> {
                bitMapCanvas?.drawPath(path, paint)
                invalidate()
            }

        }
        return true
    }

    //重新绘制画板
    fun cleanCanvas() {
        path?.reset()
        bitMapBuffer?.recycle()
        System.gc()
        bitMapBuffer = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bitMapCanvas?.setBitmap(bitMapBuffer)
        invalidate()
    }
}



