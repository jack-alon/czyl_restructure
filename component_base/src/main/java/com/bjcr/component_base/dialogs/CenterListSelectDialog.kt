package com.bjcr.component_base.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class CenterListSelectDialog<T: BaseCenterListSelectData>(datas: List<T>): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return super.onCreateView(inflater, container, savedInstanceState)
    }


}

data class BaseCenterListSelectData(val id: Int, val content: String)