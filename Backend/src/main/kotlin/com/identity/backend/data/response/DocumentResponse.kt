package com.identity.backend.data.response

import com.identity.backend.data.entities.Document
import com.identity.backend.data.entities.DocumentField

data class DocumentResponse(
    val id: Int,
    val documentName: String? = null,
    val fieldsList: List<DocumentField>,
    val profileId: Int
)

fun Document.toResponse() = DocumentResponse(
    id = id,
    documentName = documentName,
    fieldsList = fieldsList,
    profileId = profileId
)