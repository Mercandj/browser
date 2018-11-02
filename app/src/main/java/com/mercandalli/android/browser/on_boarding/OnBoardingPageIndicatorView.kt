@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.on_boarding

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration

import androidx.core.content.ContextCompat
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewConfigurationCompat
import androidx.viewpager.widget.ViewPager

import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import com.mercandalli.android.browser.R

/**
 * Draws circles (one for each view). The current view position is filled and
 * others are only stroked.
 */
class OnBoardingPageIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr),
    OnBoardingPageIndicator {

    private var radiusInternal: Float = 0F
    private val paintPageFill = Paint(ANTI_ALIAS_FLAG)
    private val mPaintStroke = Paint(ANTI_ALIAS_FLAG)
    private val mPaintFill = Paint(ANTI_ALIAS_FLAG)
    private var viewPager: ViewPager? = null
    private var listener: ViewPager.OnPageChangeListener? = null
    private var mCurrentPage: Int = 0
    private var mSnapPage: Int = 0
    private var pageOffset: Float = 0.toFloat()
    private var scrollState: Int = 0
    private var mOrientation: Int = 0
    private var mCentered: Boolean = false
    private var snap: Boolean = false

    private val touchSlop: Int
    private var mLastMotionX = -1f
    private var mActivePointerId = INVALID_POINTER
    private var mIsDragging: Boolean = false

    private var mNbItemRemovedDraw: Int = 0

    var isCentered: Boolean
        get() = mCentered
        set(centered) {
            mCentered = centered
            invalidate()
        }

    var pageColor: Int
        get() = paintPageFill.color
        set(pageColor) {
            paintPageFill.color = pageColor
            invalidate()
        }

    var fillColor: Int
        get() = mPaintFill.color
        set(fillColor) {
            mPaintFill.color = fillColor
            invalidate()
        }

    var orientation: Int
        get() = mOrientation
        set(orientation) = when (orientation) {
            HORIZONTAL, VERTICAL -> {
                mOrientation = orientation
                requestLayout()
            }

            else -> throw IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.")
        }

    var strokeColor: Int
        get() = mPaintStroke.color
        set(strokeColor) {
            mPaintStroke.color = strokeColor
            invalidate()
        }

    var strokeWidth: Float
        get() = mPaintStroke.strokeWidth
        set(strokeWidth) {
            mPaintStroke.strokeWidth = strokeWidth
            invalidate()
        }

    var radius: Float
        get() = radiusInternal
        set(radius) {
            radiusInternal = radius
            invalidate()
        }

    var isSnap: Boolean
        get() = snap
        set(snap) {
            this.snap = snap
            invalidate()
        }

    init {
        if (isInEditMode) {
            touchSlop = 0
        } else {
            // Load defaults from resources
            val res = resources
            val defaultPageColor = ContextCompat.getColor(context, R.color.on_boarding_circle_indicator_page_color_light)
            val defaultFillColor = ContextCompat.getColor(context, R.color.on_boarding_circle_indicator_fill_color_light)
            val defaultOrientation = res.getInteger(R.integer.on_boarding_circle_indicator_orientation)
            val defaultStrokeColor = ContextCompat.getColor(context, R.color.on_boarding_circle_indicator_stroke_color_light)
            val defaultStrokeWidth = res.getDimension(R.dimen.on_boarding_circle_indicator_stroke_width)
            val defaultRadius = res.getDimension(R.dimen.on_boarding_circle_indicator_radius)

            // Retrieve styles attributes
            val attrs = context.obtainStyledAttributes(attrs, R.styleable.OnBoardingPageIndicatorView, 0, 0)

            mCentered = attrs.getBoolean(R.styleable.OnBoardingPageIndicatorView_centered, true)
            mOrientation = attrs.getInt(R.styleable.OnBoardingPageIndicatorView_android_orientation, defaultOrientation)
            paintPageFill.style = Style.FILL
            paintPageFill.color = attrs.getColor(R.styleable.OnBoardingPageIndicatorView_pageColor, defaultPageColor)
            mPaintStroke.style = Style.STROKE
            mPaintStroke.color = attrs.getColor(R.styleable.OnBoardingPageIndicatorView_strokeColor, defaultStrokeColor)
            mPaintStroke.strokeWidth = attrs.getDimension(R.styleable.OnBoardingPageIndicatorView_strokeWidth, defaultStrokeWidth)
            mPaintFill.style = Style.FILL
            mPaintFill.color = attrs.getColor(R.styleable.OnBoardingPageIndicatorView_fillColor, defaultFillColor)
            radiusInternal = attrs.getDimension(R.styleable.OnBoardingPageIndicatorView_radius, defaultRadius)
            snap = attrs.getBoolean(R.styleable.OnBoardingPageIndicatorView_snap, false)

            val background = attrs.getDrawable(R.styleable.OnBoardingPageIndicatorView_android_background)
            if (background != null) {
                setBackground(background)
            }

            attrs.recycle()

            val configuration = ViewConfiguration.get(context)
            touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration)
        }
    }

    fun setNbItemRemovedTodraw(nbItemRemoved: Int) {
        mNbItemRemovedDraw = nbItemRemoved
    }

    override fun setDarkTheme(darkEnabled: Boolean) {
        if (darkEnabled) {
            paintPageFill.color = ContextCompat.getColor(context, R.color.on_boarding_circle_indicator_page_color_dark)
            mPaintFill.color = ContextCompat.getColor(context, R.color.on_boarding_circle_indicator_fill_color_dark)
            mPaintStroke.color = ContextCompat.getColor(context, R.color.on_boarding_circle_indicator_stroke_color_dark)
        } else {
            paintPageFill.color = ContextCompat.getColor(context, R.color.on_boarding_circle_indicator_page_color_light)
            mPaintFill.color = ContextCompat.getColor(context, R.color.on_boarding_circle_indicator_fill_color_light)
            mPaintStroke.color = ContextCompat.getColor(context, R.color.on_boarding_circle_indicator_stroke_color_light)
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (viewPager == null) {
            return
        }
        val count = viewPager!!.adapter!!.count - mNbItemRemovedDraw
        if (count == 0) {
            return
        }

        val longSize: Int
        val longPaddingBefore: Int
        val longPaddingAfter: Int
        val shortPaddingBefore: Int
        if (mOrientation == HORIZONTAL) {
            longSize = width
            longPaddingBefore = paddingLeft
            longPaddingAfter = paddingRight
            shortPaddingBefore = paddingTop
        } else {
            longSize = height
            longPaddingBefore = paddingTop
            longPaddingAfter = paddingBottom
            shortPaddingBefore = paddingLeft
        }

        val fiveRadius = radiusInternal * 5
        val shortOffset = shortPaddingBefore + radiusInternal
        var longOffset = longPaddingBefore + radiusInternal
        if (mCentered) {
            longOffset += (longSize - longPaddingBefore - longPaddingAfter) / 2.0f - count * fiveRadius / 2.0f
        }

        var dX: Float
        var dY: Float

        var pageFillRadius = radiusInternal
        if (mPaintStroke.strokeWidth > 0) {
            pageFillRadius -= mPaintStroke.strokeWidth / 2.0f
        }

        // Draw stroked circles
        for (iLoop in 0 until count) {
            val drawLong = longOffset + iLoop * fiveRadius
            if (mOrientation == HORIZONTAL) {
                dX = drawLong
                dY = shortOffset
            } else {
                dX = shortOffset
                dY = drawLong
            }
            // Only paint fill if not completely transparent
            if (paintPageFill.alpha > 0) {
                canvas.drawCircle(dX, dY, pageFillRadius, paintPageFill)
            }

            // Only paint stroke if a stroke width was non-zero
            if (java.lang.Float.compare(pageFillRadius, radiusInternal) != 0) {
                canvas.drawCircle(dX, dY, radiusInternal, mPaintStroke)
            }
        }

        // Draw the filled circle according to the current scroll
        var cx = (if (snap) mSnapPage else mCurrentPage) * fiveRadius
        if (!snap) {
            cx += pageOffset * fiveRadius
        }
        if (mOrientation == HORIZONTAL) {
            dX = longOffset + cx
            dY = shortOffset
        } else {
            dX = shortOffset
            dY = longOffset + cx
        }
        canvas.drawCircle(dX, dY, radiusInternal, mPaintFill)
    }

    override fun onTouchEvent(motionEvent: MotionEvent): Boolean {
        if (super.onTouchEvent(motionEvent)) {
            return true
        }
        if (viewPager == null || viewPager!!.adapter!!.count == 0) {
            return false
        }

        val action = motionEvent.action and MotionEvent.ACTION_MASK
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                mActivePointerId = motionEvent.getPointerId(0)
                mLastMotionX = motionEvent.x
            }

            MotionEvent.ACTION_MOVE -> {
                val activePointerIndex = motionEvent.findPointerIndex(mActivePointerId)
                val x = MotionEventCompat.getX(motionEvent, activePointerIndex)
                val deltaX = x - mLastMotionX

                if (!mIsDragging && Math.abs(deltaX) > touchSlop) {
                    mIsDragging = true
                }

                if (mIsDragging) {
                    mLastMotionX = x
                    if (viewPager!!.isFakeDragging || viewPager!!.beginFakeDrag()) {
                        viewPager!!.fakeDragBy(deltaX)
                    }
                }
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                if (!mIsDragging) {
                    val count = viewPager!!.adapter!!.count
                    val width = width
                    val halfWidth = width / 2f
                    val sixthWidth = width / 6f

                    if (mCurrentPage > 0 && motionEvent.x < halfWidth - sixthWidth) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            viewPager!!.currentItem = mCurrentPage - 1
                        }
                        return true
                    } else if (mCurrentPage < count - 1 && motionEvent.x > halfWidth + sixthWidth) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            viewPager!!.currentItem = mCurrentPage + 1
                        }
                        return true
                    }
                }

                mIsDragging = false
                mActivePointerId = INVALID_POINTER
                if (viewPager!!.isFakeDragging) viewPager!!.endFakeDrag()
            }

            MotionEvent.ACTION_POINTER_DOWN -> {
                val index = motionEvent.actionIndex
                mLastMotionX = motionEvent.getX(index)
                mActivePointerId = motionEvent.getPointerId(index)
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = motionEvent.actionIndex
                val pointerId = motionEvent.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mActivePointerId = motionEvent.getPointerId(newPointerIndex)
                }
                mLastMotionX = motionEvent.getX(motionEvent.findPointerIndex(mActivePointerId))
            }
        }

        return true
    }

    override fun setViewPager(view: ViewPager) {
        if (viewPager === view) {
            return
        }
        if (viewPager != null) {
            viewPager!!.removeOnPageChangeListener(this)
        }
        if (view.adapter == null) {
            throw IllegalStateException("ViewPager does not have adapter instance.")
        }
        viewPager = view
        viewPager!!.addOnPageChangeListener(this)
        invalidate()
    }

    override fun setViewPager(view: ViewPager, initialPosition: Int) {
        setViewPager(view)
        setCurrentItem(initialPosition)
    }

    override fun setCurrentItem(item: Int) {
        if (viewPager == null) {
            throw IllegalStateException("ViewPager has not been bound.")
        }
        viewPager!!.currentItem = item
        mCurrentPage = item
        invalidate()
    }

    override fun notifyDataSetChanged() {
        invalidate()
    }

    override fun onPageScrollStateChanged(state: Int) {
        scrollState = state

        if (listener != null) {
            listener!!.onPageScrollStateChanged(state)
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        mCurrentPage = position
        pageOffset = positionOffset
        invalidate()

        if (listener != null) {
            listener!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    override fun onPageSelected(position: Int) {
        if (snap || scrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position
            mSnapPage = position
            invalidate()
        }

        if (listener != null) {
            listener!!.onPageSelected(position)
        }
    }

    override fun setOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
        this.listener = listener
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (mOrientation == HORIZONTAL) {
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec))
        } else {
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec))
        }
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private fun measureLong(measureSpec: Int): Int {
        var result: Int
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY || viewPager == null) {
            // We were told how big to be
            result = specSize
        } else {
            // Calculate the width according the views count
            val count = viewPager!!.adapter!!.count
            result = (paddingLeft.toFloat() + paddingRight.toFloat() +
                count.toFloat() * 2f * radiusInternal + (count - 1) * radiusInternal + 1f).toInt()
            // Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    /**
     * Determines the height of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The height of the view, honoring constraints from measureSpec
     */
    private fun measureShort(measureSpec: Int): Int {
        var result: Int
        val specMode = View.MeasureSpec.getMode(measureSpec)
        val specSize = View.MeasureSpec.getSize(measureSpec)

        if (specMode == View.MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize
        } else {
            // Measure the height
            result = (2 * radiusInternal + paddingTop.toFloat() + paddingBottom.toFloat() + 1f).toInt()
            // Respect AT_MOST value if that was what is called for by measureSpec
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        mCurrentPage = savedState.currentPage
        mSnapPage = savedState.currentPage
        requestLayout()
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = SavedState(superState)
        savedState.currentPage = mCurrentPage
        return savedState
    }

    internal class SavedState : View.BaseSavedState {
        var currentPage: Int = 0

        constructor(superState: Parcelable) : super(superState)

        override fun writeToParcel(dest: Parcel, flags: Int) {
            super.writeToParcel(dest, flags)
            dest.writeInt(currentPage)
        }
    }

    companion object {

        private const val INVALID_POINTER = -1
    }
}
