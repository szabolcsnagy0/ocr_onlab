package com.identity.backend.data.request

import com.identity.backend.data.entities.DocumentTemplate

data class DocumentTemplateRequest(
    val id: Int,
    val name: String,
    val jsonTemplate: String
)

fun DocumentTemplate.toRequest() = DocumentTemplateRequest(
    id = this.id,
    name = this.name,
    jsonTemplate = this.jsonTemplate
)
