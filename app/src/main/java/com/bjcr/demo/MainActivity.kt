package com.bjcr.demo

import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bjcr.component_base.utils.ScreenUtil
import com.bjcr.component_base.utils.ShareUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val hashMap = LinkedHashMap<String,String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWordImage()
    }

    /**
     * 文字和图片混排之后生成一张图片
     */
    private fun initWordImage(){
        val bitmap = BitmapFactory.decodeResource(resources,R.mipmap.icon_qr_code)
        initSparseArray()
        val tempBitmap = ShareUtils.shareGenerateWordImage(this,"AAA邀请您于","2020-12-25 11：15来访","山东省日照市莒县棋山镇庞庄存2002号",
                "温馨提示：脸识别录入通过短信形式发送至访客手机，密码和人脸在来访当天生效，仅可使用一次，使用或逾期后自动生效。"
                ,bitmap,hashMap)
        iv_content.setImageBitmap(tempBitmap)
    }

    /**
     * 初始化分享内容
     */
    private fun initSparseArray() {
        hashMap["访客手机"] = "15101519287"
        hashMap["来访事由"] = "链接已通过短信"
        hashMap["来访日期"] = "2020-12-25 19:30"
        hashMap["门禁密码"] = "123456"
    }

}