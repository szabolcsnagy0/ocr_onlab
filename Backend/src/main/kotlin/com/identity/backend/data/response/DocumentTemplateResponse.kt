package com.identity.backend.data.response

import com.identity.backend.data.entities.DocumentTemplate

data class DocumentTemplateResponse(
    val id: Int,
    val name: String
)

fun DocumentTemplate.toResponse() = DocumentTemplateResponse(
    id = this.id,
    name = this.name
)
