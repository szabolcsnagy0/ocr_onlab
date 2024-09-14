package com.identity.backend.data.request

import com.identity.backend.data.entities.DocumentField

data class DocumentRequest(
    val documentName: String,
    val fieldsList: List<DocumentField>,
    val front: String? = null,
    val back: String? = null
)