package com.bjcr.component_base.utils

import android.content.Context
import android.graphics.*
import androidx.core.content.ContextCompat
import com.bjcr.component_base.R

/**
 * 分享操作
 */
class ShareUtils {

    companion object{
        fun shareGenerateWordImage(context: Context,title: String, dateStr: String, houseAddress: String,
                                   tipContent: String, bitmap: Bitmap,
                                   map: LinkedHashMap<String,String>): Bitmap {
            return  ShareUtils().generateWordImage(context,title,dateStr,houseAddress,tipContent,bitmap,map)
        }
    }

    /**
     * 生成微信分享访客邀请图片
     */
    private fun generateWordImage(context: Context,title: String, dateStr: String, houseAddress: String,
                                  tipContent: String, bitmap: Bitmap,
                                  map: LinkedHashMap<String,String>): Bitmap {
        //创建bitmap
        val spaceHeight = ScreenUtil.dip2px(context,16f)
        val newBitmapWidth = ScreenUtil.dip2px(context,318f)
        val newBitmapHeight = ScreenUtil.dip2px(context,419f)
        val newBitmap = Bitmap.createBitmap(newBitmapWidth,newBitmapHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        //画背景
        drawBackGround(context,canvas,newBitmapWidth,newBitmapHeight)
        //画标题1
        val titlePaint1 = Paint()
        titlePaint1.color = ContextCompat.getColor(context, R.color.c_666)
        titlePaint1.textSize = ScreenUtil.size2sp(16f,context)
        titlePaint1.isAntiAlias = true
        titlePaint1.isDither = true
        val titleWidth1 = titlePaint1.measureText(title)
        val titleHeight1 = titlePaint1.fontMetrics.bottom - titlePaint1.fontMetrics.top
        val startX = (newBitmapWidth - titleWidth1)/2.0f
        canvas.drawText(title,0,title.length,startX, spaceHeight.toFloat() * 3,titlePaint1)
        //画标题日期
        val dateWidth = titlePaint1.measureText(dateStr)
        val dateHeight = titlePaint1.fontMetrics.bottom - titlePaint1.fontMetrics.top
        val startXDate = (newBitmapWidth - dateWidth)/2.0f
        val startYDate = spaceHeight.toFloat() * 3 + titleHeight1 + spaceHeight
        canvas.drawText(dateStr,0,dateStr.length,startXDate, startYDate,titlePaint1)
        //画来访地址
        val titlePaint2 = Paint()
        titlePaint2.color = ContextCompat.getColor(context,R.color.c_333)
        val typeFace = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        titlePaint2.typeface = typeFace
        titlePaint2.textSize = ScreenUtil.size2sp(18f,context)
        val startY2 = startYDate + dateHeight + spaceHeight
        val titleHeight2 = drawMoreRowText(titlePaint2,houseAddress,newBitmapWidth + 16,spaceHeight,canvas,startY2)
        //画分割线
        val dividerPaint = Paint()
        dividerPaint.color = ContextCompat.getColor(context,R.color.c_ccc)
        val dividerLine1 = Path()
        val dividerLineTop = titleHeight2.toFloat()
        dividerLine1.addRect(spaceHeight.toFloat(),dividerLineTop,
                (newBitmapWidth - spaceHeight).toFloat(),dividerLineTop + 2,
                Path.Direction.CW)
        canvas.drawPath(dividerLine1,dividerPaint)
        //画内容标题
        val contentPaintTitle = Paint()
        contentPaintTitle.textSize = ScreenUtil.size2sp(16f,context)
        contentPaintTitle.color = ContextCompat.getColor(context,R.color.c_999)
        //画内容文案
        val contentPaint = Paint()
        contentPaint.textSize = ScreenUtil.size2sp(16f,context)
        contentPaint.color = ContextCompat.getColor(context,R.color.c_333)
        //循环画内容和内容标题
        val contentTop1 = dividerLineTop + spaceHeight*2
        val contentHeight = contentPaintTitle.fontMetrics.bottom - contentPaint.fontMetrics.top
        var mapPos = 0
        map.keys.forEach {
            canvas.drawText(it,0,it.length,spaceHeight.toFloat(),contentTop1 + (contentHeight + spaceHeight / 2) * mapPos ,contentPaintTitle)
            val contentWidth11 = contentPaint.measureText(map[it])
            val contentStartX = newBitmapWidth - spaceHeight - contentWidth11
            canvas.drawText(map[it]!!,0,map[it]!!.length,contentStartX,contentTop1+(contentHeight + spaceHeight / 2) * mapPos,contentPaint)
            mapPos ++
        }
        //分割线
        val dividerLineTop2 = contentTop1 + (contentHeight + spaceHeight / 2) * mapPos - spaceHeight
        dividerLine1.reset()
        dividerLine1.addRect(spaceHeight.toFloat(),dividerLineTop2,
                (newBitmapWidth - spaceHeight).toFloat(),dividerLineTop2 + 2,
                Path.Direction.CW)
        canvas.drawPath(dividerLine1,dividerPaint)
        //多行文本
        val tipPaint = Paint()
        tipPaint.textSize = ScreenUtil.size2sp(14f,context)
        tipPaint.typeface = Typeface.DEFAULT
        tipPaint.color = ContextCompat.getColor(context,R.color.c_999)
        val tipContentHeight = dividerLineTop2 + spaceHeight * 2
        val bitmapImageWidth = ScreenUtil.dip2px(context,76f)
        drawMoreRowText(tipPaint,tipContent,newBitmapWidth - bitmapImageWidth,spaceHeight,canvas,tipContentHeight)
        //画图片
        val tempHeight = tipPaint.fontMetrics.bottom - tipPaint.fontMetrics.top
        val matrix = Matrix()
        matrix.postScale(bitmapImageWidth*1.0f/bitmap.width,bitmapImageWidth*1.0f/bitmap.height)
        val tempBitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
        canvas.drawBitmap(tempBitmap,(newBitmapWidth - spaceHeight - bitmapImageWidth).toFloat(),tipContentHeight - tempHeight/2, Paint())
        canvas.save()
        canvas.restore()
        return newBitmap
    }

    /**
     * 画背景
     */
    private fun drawBackGround(context: Context,canvas: Canvas, newBitmapWidth: Int, newBitmapHeight: Int){
        val paintBackground = Paint()
        paintBackground.color = ContextCompat.getColor(context,R.color.translate)
        canvas.drawRect(0f,0f,newBitmapWidth.toFloat(),newBitmapHeight.toFloat(),paintBackground)
        val arcLength = ScreenUtil.dip2px(context,8f).toFloat()
        val arcPaint = Paint()
        arcPaint.color = ContextCompat.getColor(context,R.color.white)
        arcPaint.isAntiAlias = true
        arcPaint.isDither = true
        val rect = RectF(0f,0f,newBitmapWidth.toFloat(),newBitmapHeight.toFloat())
        canvas.drawRoundRect(rect,arcLength,arcLength,arcPaint)
    }

    /**
     * 画文本
     */
    private fun drawMoreRowText(
            tipPaint: Paint,
            tipContent: String,
            newBitmapWidth: Int,
            spaceHeight: Int,
            canvas: Canvas,
            tipContentHeight: Float): Int {
        val tipContentWidth = tipPaint.measureText(tipContent)
        val tipTextHeight = tipPaint.fontMetrics.bottom - tipPaint.fontMetrics.top
        var rowCount = 1f
        var drawTipEndHeight = 0
        if (tipContentWidth > newBitmapWidth - 2* spaceHeight){
            rowCount = tipContentWidth / (newBitmapWidth - 2 * spaceHeight)
            val fillTextWidth = (tipContent.length.toFloat() / rowCount).toInt()
            var startPos = 0
            var tempEndHeight = 0f
            for (index in 1..rowCount.toInt()){
                val tempStr = tipContent.substring(startPos,(fillTextWidth) * index)
                canvas.drawText(tempStr,0,tempStr.length,spaceHeight.toFloat(),tipContentHeight + (tipTextHeight + 6) *(index - 1),tipPaint)
                startPos = (fillTextWidth) * index
                tempEndHeight = tipContentHeight + (tipTextHeight + 6) *(index - 1)
            }
            val tempEndStr = tipContent.subSequence(startPos,tipContent.length - 1)
            canvas.drawText(tempEndStr,0,tempEndStr.length,spaceHeight.toFloat(),tempEndHeight + spaceHeight + 6,tipPaint)
            drawTipEndHeight = (tempEndHeight + spaceHeight *2).toInt()
        }else {
            drawTipEndHeight = (tipContentHeight + (tipTextHeight + 6) + spaceHeight).toInt()
            canvas.drawText(tipContent,0,tipContent.length,spaceHeight.toFloat(),tipContentHeight + (tipTextHeight + 6) + spaceHeight,tipPaint)
        }
        return drawTipEndHeight
    }

}