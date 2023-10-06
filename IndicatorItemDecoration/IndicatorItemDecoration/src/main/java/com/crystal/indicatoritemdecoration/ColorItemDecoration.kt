package com.crystal.indicatoritemdecoration

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.TypedValue
import android.view.animation.AccelerateInterpolator
import android.view.animation.Interpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/* Parameter (dp 기준)
* context,
* 비활성화 Indicator 넓이 (dp 기준),
* 비활성화 Indicator 높이 (dp 기준),
* 활성화 Indicator 넓이 (dp 기준),
* 활성화 Indicator 높이  (dp 기준),
* 비활성화 Indicator 색상 (Int),
* 활성화 Indicator 색상 (Int),
* Indicator 사이 간격 (패딩),
* indicator x축 시작점 : 0 ~ 1
* indicator y축 중심점 : 0 ~ 1
* */

class ColorItemDecoration(
    private val context: Context,
    private var inactiveWidth: Float,
    private var inactiveHeight: Float,
    private var activeWidth: Float,
    private var activeHeight: Float,
    private val inactiveColor: Int,
    private val activeColor: Int,
    private var itemBetweenPadding: Float,
    indicatorStartX: Float?,
    indicatorPositionY: Float?
): RecyclerView.ItemDecoration() {

    private val mInterpolator: Interpolator = AccelerateInterpolator()
    private var mIndicatorStartX = 0.5f
    private var mIndicatorPositionY = 0.9f
    private val mPaint = Paint()

    companion object {
        private fun dpToPx(dp: Float): Float {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().displayMetrics)
        }
    }

    init {
        inactiveWidth = dpToPx(inactiveWidth)
        inactiveHeight = dpToPx(inactiveHeight)
        activeWidth = dpToPx(activeWidth)
        activeHeight = dpToPx(activeHeight)
        itemBetweenPadding = dpToPx(itemBetweenPadding)

        if (indicatorStartX != null) {
            mIndicatorStartX = indicatorStartX
        }
        if (indicatorPositionY != null) {
            mIndicatorPositionY = indicatorPositionY
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        /* 아이템 수 */
        val itemCount = parent.adapter!!.itemCount

        /* 패딩값을 제외한 아이템 길이 */
        val totalWidth = (inactiveWidth * (itemCount - 1)) + activeWidth


        /* 총 길이 (총 아이템 길이 + 아이템 사이의 총 패딩 길이) */
        val indicatorTotalWidth = totalWidth + itemBetweenPadding * itemCount

        /* Indicator x 시작점 */
        val indicatorStartX = parent.width * mIndicatorStartX  - indicatorTotalWidth / 2

        /* Indicator y 중심점 */
        val indicatorPositionY = (parent.height) * (mIndicatorPositionY)

        // drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount)
        val layoutManager = parent.layoutManager as LinearLayoutManager?
        val activePosition = layoutManager!!.findFirstVisibleItemPosition()

        if (activePosition == RecyclerView.NO_POSITION) {
            return
        }

        val activeChild = layoutManager.findViewByPosition(activePosition)
        val left = activeChild!!.left
        val width = activeChild.width
        val right = activeChild.right
        val progress = mInterpolator.getInterpolation(left * -1 / width.toFloat())

        drawIndicator(c,indicatorStartX, indicatorPositionY, itemCount, activePosition, progress)
    }

    private fun drawIndicator(
        c: Canvas,
        indicatorStartX: Float,
        indicatorPositionY: Float,
        itemCount: Int,
        activePosition: Int,
        progress: Float
    ) {

        val inactiveItemWidth = inactiveWidth + itemBetweenPadding
        val activeItemWidth = activeWidth  + itemBetweenPadding

        var start = indicatorStartX

        for (i in 0 until itemCount) {
            if (activePosition == i) {
                mPaint.color = activeColor

                val rect = RectF(start , indicatorPositionY - (activeHeight / 2f), start + activeWidth  , indicatorPositionY + (activeHeight / 2f) )
                c.drawRoundRect(rect, indicatorPositionY , indicatorPositionY, mPaint)
                start += activeItemWidth

            } else {
                mPaint.color = inactiveColor
                val rect = RectF(start , indicatorPositionY - (inactiveHeight / 2f), start + inactiveWidth  , indicatorPositionY + (inactiveHeight / 2f) )
                c.drawRoundRect(rect, indicatorPositionY , indicatorPositionY, mPaint)
                start += inactiveItemWidth
            }
        }

    }




}