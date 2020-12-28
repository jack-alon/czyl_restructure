package com.bjcr.demo

import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bjcr.component_base.utils.ScreenUtil

class MainActivity : AppCompatActivity() {
    private val hashMap = HashMap<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    /**
     * 文字和图片混排之后生成一张图片
     */
    private fun initWordImage(){
        val imageView = findViewById<ImageView>(R.id.iv_content)
        val bitmap = BitmapFactory.decodeResource(resources,R.mipmap.icon_scan_3)
        initSparseArray()
        val tempBitmap = generateWordImage("AAA邀请您于2020-12-25 11：15来访","山东省日照市莒县棋山镇庞庄存2002号",
                "温馨提示：人脸识别录入链接已通过短信形式发送至访客手机人脸识别录入链接已通过短信形式发送至访客手机，密码和人脸在来访当天生效，仅可使用一次，使用或逾期后自动生效。"
                ,bitmap,hashMap)
        imageView.setImageBitmap(tempBitmap)
    }

    private fun initSparseArray() {
        hashMap["访客手机"] = "15101519287"
        hashMap["来访事由"] = "链接已通过短信"
        hashMap["来访日期"] = "2020-12-25 19:30"
        hashMap["门禁密码"] = "123456"
    }

    private fun generateWordImage(title: String, houseAddress: String,
                                  tipContent: String, bitmap: Bitmap,
                                  map: HashMap<String,String>): Bitmap{
        val spaceHeight = ScreenUtil.dip2px(this,16f)

        val newBitmapWidth = ScreenUtil.getScreenWidth(this)
        val newBitmapHeight = newBitmapWidth * 3 / 2.0f
        val newBitmap = Bitmap.createBitmap(newBitmapWidth,newBitmapHeight.toInt() + spaceHeight,Bitmap.Config.ARGB_8888)

        val paintBackground = Paint()
        paintBackground.color = ContextCompat.getColor(this,R.color.white)
        val canvas = Canvas(newBitmap)
        canvas.drawRect(0f,0f,newBitmapWidth.toFloat(),newBitmapHeight,paintBackground)

        val titlePaint1 = Paint()
        titlePaint1.color = ContextCompat.getColor(this,R.color.c_666)
        titlePaint1.textSize = ScreenUtil.size2sp(16f,this)

        val titlePaint2 = Paint()
        titlePaint2.color = ContextCompat.getColor(this,R.color.c_333)
        val typeFace = Typeface.create(Typeface.SANS_SERIF,Typeface.BOLD)
        titlePaint2.typeface = typeFace
        titlePaint2.textSize = ScreenUtil.size2sp(18f,this)

        val dividerPaint = Paint()
        dividerPaint.color = ContextCompat.getColor(this,R.color.c_ccc)

        val dividerLine1 = Path()
        val dividerLine2 = Path()

        val contentPaintTitle = Paint()
        contentPaintTitle.textSize = ScreenUtil.size2sp(16f,this)
        contentPaintTitle.color = ContextCompat.getColor(this,R.color.c_999)

        val contentPaint = Paint()
        contentPaint.textSize = ScreenUtil.size2sp(16f,this)
        contentPaint.color = ContextCompat.getColor(this,R.color.c_333)

        val tipPaint = Paint()
        tipPaint.textSize = ScreenUtil.size2sp(14f,this)
        tipPaint.typeface = Typeface.DEFAULT
        tipPaint.color = ContextCompat.getColor(this,R.color.c_999)

        val tipPaint2 = Paint()
        tipPaint2.textSize = ScreenUtil.size2sp(12f,this)
        tipPaint2.color = ContextCompat.getColor(this,R.color.c_999)

        val titleWidth1 = titlePaint1.measureText(title)
        val titleHeight1 = titlePaint1.fontMetrics.bottom - titlePaint1.fontMetrics.top
        val startX = (newBitmapWidth - titleWidth1)/2.0f
        canvas.drawText(title,0,title.length,startX, spaceHeight.toFloat() * 3,titlePaint1)

        val titleWidth2 = titlePaint2.measureText(houseAddress)
        val titleHeight2 = titlePaint2.fontMetrics.bottom - titlePaint2.fontMetrics.top
        val startX2 = (newBitmapWidth - titleWidth2)/2.0f
        val startY2 = 4 * spaceHeight + titleHeight1
        canvas.drawText(houseAddress,0,houseAddress.length,startX2, startY2.toFloat(),titlePaint2)

        val dividerLineTop = startY2 + titleHeight2
        dividerLine1.addRect(spaceHeight.toFloat(),dividerLineTop,
                (newBitmapWidth - spaceHeight).toFloat(),dividerLineTop + 2,
                Path.Direction.CW)
        canvas.drawPath(dividerLine1,dividerPaint)

        val contentTop1 = dividerLineTop + spaceHeight*2
        val contentHeight = contentPaintTitle.fontMetrics.bottom - contentPaint.fontMetrics.top

        var mapPos = 0
        map.keys.forEach {
            Log.e("it","it = $it")
            canvas.drawText(it,0,it.length,spaceHeight.toFloat(),contentTop1 + (contentHeight + spaceHeight / 2) * mapPos ,contentPaintTitle)
            val contentWidth11 = contentPaint.measureText(map[it])
            val contentStartX = newBitmapWidth - spaceHeight - contentWidth11
            canvas.drawText(map[it]!!,0,map[it]!!.length,contentStartX,contentTop1+(contentHeight + spaceHeight / 2) * mapPos,contentPaint)
            mapPos ++
        }
        val dividerLineTop2 = contentTop1 + (contentHeight + spaceHeight / 2) * mapPos - spaceHeight
        dividerLine2.addRect(spaceHeight.toFloat(),dividerLineTop2,
                (newBitmapWidth - spaceHeight).toFloat(),dividerLineTop2 + 2,
                Path.Direction.CW)
        canvas.drawPath(dividerLine2,dividerPaint)

        val tipContentHeight = dividerLineTop2 + spaceHeight * 2
        //画多行文本
        val tipContentWidth = tipPaint.measureText(tipContent)
        val tipTextHeight = tipPaint.fontMetrics.bottom - tipPaint.fontMetrics.top
        var rowCount = 1f
        var decimalPart = 0f
        var drawTipEndHeight = 0
        if (tipContentWidth > newBitmapWidth - 2* spaceHeight){
            rowCount = tipContentWidth / (newBitmapWidth - 2 * spaceHeight)
            decimalPart = rowCount - rowCount.toInt()
            val fillTextWidth = ((tipContent.length - tipContent.length * decimalPart.toInt() + 3)/rowCount).toInt()
            var startPos = 0
            var tempEndHeight = 0f
            for (index in 1..rowCount.toInt()){
                val tempStr = tipContent.substring(startPos,(fillTextWidth - 1) * index)
                canvas.drawText(tempStr,0,tempStr.length,spaceHeight.toFloat(),tipContentHeight + (tipTextHeight + 6) *(index - 1),tipPaint)
                startPos = (fillTextWidth - 1) * index
                tempEndHeight = tipContentHeight + (tipTextHeight + 6) *(index - 1)
            }
            val tempEndStr = tipContent.subSequence(startPos,tipContent.length - 1)
            canvas.drawText(tempEndStr,0,tempEndStr.length,spaceHeight.toFloat(),(tempEndHeight + spaceHeight).toFloat(),tipPaint)
            drawTipEndHeight = (tempEndHeight + spaceHeight *2).toInt()
        }
        canvas.drawBitmap(bitmap,spaceHeight.toFloat(),drawTipEndHeight.toFloat(),Paint())
        canvas.save()
        canvas.restore()
        return newBitmap
    }
}