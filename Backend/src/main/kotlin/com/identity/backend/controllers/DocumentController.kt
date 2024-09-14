package com.identity.backend.controllers

import com.identity.backend.data.entities.Document
import com.identity.backend.data.entities.DocumentField
import com.identity.backend.data.request.DocumentRequest
import com.identity.backend.data.response.DocumentResponse
import com.identity.backend.data.response.toResponse
import com.identity.backend.repository.DocumentRepository
import com.identity.backend.services.AuthenticationService
import com.identity.backend.services.ImageUploadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user/profiles/{id}/document")
class DocumentController(
    val imageUploadService: ImageUploadService,
    val authenticationService: AuthenticationService,
) {
    @Autowired
    private lateinit var documentRepository: DocumentRepository

    @PostMapping("new")
    fun createDocument(
        @PathVariable("id") profileId: Int,
        @RequestBody documentRequest: DocumentRequest
    ): ResponseEntity<DocumentResponse> {
        val profile = authenticationService.getProfileById(profileId) ?: return ResponseEntity.notFound().build()

        val newDocument = Document(
            profileId = profileId,
            documentName = documentRequest.documentName,
            fieldsList = mutableListOf()
        ).apply {
            documentRequest.front?.let {
                front = imageUploadService.findImageFile(it, profile.userId).readBytes()
            }
            documentRequest.back?.let {
                back = imageUploadService.findImageFile(it, profile.userId).readBytes()
            }
        }

        // First, save the document to generate the ID
        val savedDocument = documentRepository.save(newDocument)

        val documentFields = documentRequest.fieldsList.map { field ->
            DocumentField(
                documentId = savedDocument.id,
                title = field.title,
                value = field.value
            )
        }

        savedDocument.fieldsList = documentFields.toMutableList()

        documentRepository.save(savedDocument)

        // Delete user folder and files
        imageUploadService.deleteUserFolder(profile.userId)

        return ResponseEntity.ok(newDocument.toResponse())
    }

    @GetMapping("list")
    fun listAll(
        @PathVariable("id") profileId: Int
    ): ResponseEntity<List<DocumentResponse>> =
        authenticationService
            .getProfileById(profileId)
            ?.documentList?.map {
                it.toResponse()
            }
            ?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    @GetMapping("{document_id}/front")
    fun getFrontImageOfNationalId(
        @PathVariable("id") profileId: Int,
        @PathVariable("document_id") documentId: Int
    ): ResponseEntity<ByteArray> =
        authenticationService.getProfileById(profileId)?.documentList?.find { it.id == documentId }
            ?.let { document ->
                document.front?.let {
                    ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(it)
                }
            } ?: ResponseEntity.notFound().build()

    @GetMapping("{document_id}/back")
    fun getBackImageOfNationalId(
        @PathVariable("id") profileId: Int,
        @PathVariable("document_id") documentId: Int
    ): ResponseEntity<ByteArray> =
        authenticationService.getProfileById(profileId)?.documentList?.find { it.id == documentId }
            ?.let { document ->
                document.back?.let {
                    ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(it)
                }
            } ?: ResponseEntity.notFound().build()

    @DeleteMapping("{document_id}")
    fun delete(
        @PathVariable("id") profileId: Int,
        @PathVariable("document_id") documentId: Int
    ): ResponseEntity<Any> = authenticationService.getProfileById(profileId)?.let { profile ->
        val ok = profile.documentList.removeIf { it.id == documentId }
        if (ok) {
            documentRepository.deleteById(documentId)
            ResponseEntity.ok().build()
        } else null
    } ?: ResponseEntity.notFound().build()
}