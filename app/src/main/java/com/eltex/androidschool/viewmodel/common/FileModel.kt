package com.eltex.androidschool.viewmodel.common

import android.net.Uri

import com.eltex.androidschool.data.common.AttachmentTypeFile

data class FileModel(
    val uri: Uri,
    val type: AttachmentTypeFile,
)
