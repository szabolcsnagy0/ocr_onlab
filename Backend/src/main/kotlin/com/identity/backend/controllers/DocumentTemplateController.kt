package com.identity.backend.controllers

import com.identity.backend.data.entities.DocumentTemplate
import com.identity.backend.data.request.DocumentTemplateRequest
import com.identity.backend.data.response.DocumentTemplateResponse
import com.identity.backend.data.response.toResponse
import com.identity.backend.repository.DocumentTemplateRepository
import com.identity.backend.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user/templates")
class DocumentTemplateController(
    val authenticationService: AuthenticationService
) {
    @Autowired
    private lateinit var documentTemplateRepository: DocumentTemplateRepository

    @CrossOrigin
    @GetMapping("list")
    fun getDocumentTemplates(): ResponseEntity<List<DocumentTemplateResponse>> =
        authenticationService.getUser()?.let { user ->
            user.documentTemplates.map { it.toResponse() }.let {
                ResponseEntity.ok(it)
            }
        } ?: ResponseEntity.notFound().build()

    @PostMapping("new")
    fun createNewDocumentTemplate(@RequestBody documentTemplate: DocumentTemplateRequest): ResponseEntity<DocumentTemplateResponse> {
        val user = authenticationService.getUser() ?: return ResponseEntity.notFound().build()
        val newDocumentTemplate = DocumentTemplate(
            name = documentTemplate.name,
            userId = user.id,
            jsonTemplate = documentTemplate.jsonTemplate
        )
        documentTemplateRepository.save(newDocumentTemplate)
        return ResponseEntity.ok(newDocumentTemplate.toResponse())
    }
}