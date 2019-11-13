package com.example.testappapi.data.model.statusType

import androidx.annotation.StringDef
import com.example.testappapi.data.model.statusType.StatusType.Companion.STATUS_ERROR
import com.example.testappapi.data.model.statusType.StatusType.Companion.STATUS_OK

@StringDef(STATUS_OK,STATUS_ERROR)
annotation class StatusType {
    companion object{
        const val STATUS_OK = "OK"
        const val STATUS_ERROR = "ERROR"
    }
}