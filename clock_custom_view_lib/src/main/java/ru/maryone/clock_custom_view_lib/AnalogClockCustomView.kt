package ru.maryone.clock_custom_view_lib

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class AnalogClockCustomView : View{
    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
        0
    )

    private val handler: Handler = Handler(Looper.myLooper()!!)
    private lateinit var updaterTimeRunnable: Runnable


    private val paintBlack: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = BLACK_COLOR }
    private val paintWhite: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = WHITE_COLOR }

    private var hours = 0f
    private var minutes = 0f
    private var seconds = 0f

    private var viewWidth = 0f
    private var viewHeight = 0f
    private var viewRadius = 0f

    private var handHourLength = 0f
    private var handMinutesLength = 0f
    private var handSecondsLength = 0f
    private var dotsPadding = 0f

    init {
        initRunnableObject()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewRadius, paintBlack)
        canvas.drawCircle(viewWidth / 2, viewHeight / 2, viewRadius - BORDER_RADIUS, paintWhite)

        drawDots(canvas, HOUR_ANGLE, DOT_RADIUS_BIG)
        drawDots(canvas, MINUTE_ANGLE, DOT_RADIUS_SMALL)

        drawHand(canvas, HOUR_HAND_WIDTH, HOUR_ANGLE,  handHourLength, hours)
        drawHand(canvas, MINUTE_HAND_WIDTH, MINUTE_ANGLE, handMinutesLength, minutes)
        drawHand(canvas, SECOND_HAND_WIDTH, MINUTE_ANGLE, handSecondsLength, seconds)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewWidth = w.toFloat()
        viewHeight = h.toFloat()
        viewRadius = ((min(viewWidth, viewHeight)) * (0.9) / 2).toFloat()
        handHourLength = viewRadius * 0.6f
        handMinutesLength = viewRadius * 0.75f
        handSecondsLength = viewRadius * 0.9f
        dotsPadding = viewRadius * 0.9f
    }

    fun startClock() = handler.post(updaterTimeRunnable)

    fun stopClock() = handler.removeCallbacks(updaterTimeRunnable)

    private fun initRunnableObject() {
        updaterTimeRunnable = object : Runnable {
            override fun run() {
                val currentT = Calendar.getInstance()
                setClockTime(
                    currentT.get(Calendar.HOUR).toFloat(),
                    currentT.get(Calendar.MINUTE).toFloat(),
                    currentT.get(Calendar.SECOND).toFloat()
                )
                handler.postDelayed(this, 1000)
            }
        }
    }

    private fun getXYPosition(radius: Float, degrees: Float): Pair<Float,Float> {
        val radians = degreesToRadian(degrees)
        return Pair((radius * cos(radians) + width / 2), (height / 2 + radius * sin(radians)))
    }

    private fun degreesToRadian(degrees: Float) =  (degrees * Math.PI / 180).toFloat()

    private fun getDegreesFromValue(value: Float, anglePerPoint: Int, skip: Int = 0)
    = START_CLOCK_ANGLE + value * anglePerPoint + skip

    private fun drawDots(canvas: Canvas, angle: Int, dotRadius: Float) {
        for (position in 0..(ALL_ROUND_360 / angle).toInt()) {
            val valueToDegrees =  getDegreesFromValue(position.toFloat(), angle)
            val xy = getXYPosition(dotsPadding, valueToDegrees)
            canvas.drawCircle(xy.first, xy.second, dotRadius, paintBlack)
        }
    }

    private fun drawHand(canvas: Canvas, strokeWidth: Float, angle: Int, length: Float, value: Float) {
        val handPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            this.color = HAND_COLOR
            this.strokeWidth = strokeWidth
        }
        val handTailDegrees = getDegreesFromValue(value, angle, SKIP_TAIL_DEGREES)
        val startPoint = getXYPosition(length * HAND_TAIL, handTailDegrees)
        val valueToDegrees =  getDegreesFromValue(value, angle)
        val endPoint = getXYPosition(length, valueToDegrees)
        canvas.drawLine(startPoint.first, startPoint.second, endPoint.first, endPoint.second, handPaint)
    }

    private fun setClockTime(hour: Float, minute: Float, second: Float) {
        hours = hour + (minute / 60)
        minutes = minute
        seconds = second
        invalidate()
    }

    companion object {
        const val HAND_COLOR = Color.BLACK
        const val BLACK_COLOR = Color.BLACK
        const val WHITE_COLOR = Color.WHITE
        const val HOUR_HAND_WIDTH: Float = 25f
        const val MINUTE_HAND_WIDTH: Float = 15f
        const val SECOND_HAND_WIDTH: Float = 8f
        const val DOT_RADIUS_BIG = 16f
        const val DOT_RADIUS_SMALL = 8f
        const val MINUTE_ANGLE = 6 //360/60
        const val HOUR_ANGLE = 30  //360/12
        const val BORDER_RADIUS = 30f
        const val ALL_ROUND_360 = 360f
        const val START_CLOCK_ANGLE = 270f
        const val SKIP_TAIL_DEGREES = 180
        const val HAND_TAIL = 0.2f
    }
}