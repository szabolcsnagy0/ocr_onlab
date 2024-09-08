package com.identity.backend.data.response

import com.identity.backend.data.entities.DocumentTemplate

data class DocumentTemplateResponse(
    val id: Int,
    val name: String,
    val jsonTemplate: String
)

fun DocumentTemplate.toResponse() = DocumentTemplateResponse(
    id = this.id,
    name = this.name,
    jsonTemplate = this.jsonTemplate
)
