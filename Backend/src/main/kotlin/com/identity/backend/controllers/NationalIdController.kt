package com.identity.backend.controllers

import com.identity.backend.data.entities.NationalId
import com.identity.backend.data.request.NationalIdRequest
import com.identity.backend.data.response.NationalIdResponse
import com.identity.backend.data.response.convertDate
import com.identity.backend.data.response.toNationalIdResponse
import com.identity.backend.repository.NationalIdRepository
import com.identity.backend.services.AuthenticationService
import com.identity.backend.services.ImageUploadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user/profiles/{id}/national")
class NationalIdController(
    val imageUploadService: ImageUploadService,
    val authenticationService: AuthenticationService,
) {
    @Autowired
    private lateinit var nationalIdRepository: NationalIdRepository

    @PostMapping("new")
    fun createNationalId(
        @PathVariable("id") profileId: Int,
        @RequestBody nationalIdRequest: NationalIdRequest
    ): ResponseEntity<NationalIdResponse> {
        val profile = authenticationService.getProfileById(profileId) ?: return ResponseEntity.notFound().build()

        val newNationalId = NationalId(
            name = nationalIdRequest.name,
            documentNr = nationalIdRequest.documentNr,
            dateOfBirth = nationalIdRequest.dateOfBirth?.convertDate(),
            sex = nationalIdRequest.sex,
            can = nationalIdRequest.can,
            authority = nationalIdRequest.authority,
            profileId = profileId,
            dateOfExpiry = nationalIdRequest.dateOfExpiry?.convertDate(),
            placeOfBirth = nationalIdRequest.placeOfBirth,
            nationality = nationalIdRequest.nationality,
            mothersName = nationalIdRequest.mothersName,
            nameAtBirth = nationalIdRequest.nameAtBirth
        )

        //Add front image to the id
        nationalIdRequest.front?.let {
            newNationalId.front = imageUploadService.findImageFile(it, profile.userId).readBytes()
        }

        //Add back image to the id
        nationalIdRequest.back?.let {
            newNationalId.back = imageUploadService.findImageFile(it, profile.userId).readBytes()
        }

        nationalIdRequest.documentName?.let {
            newNationalId.documentName = it
        }

        nationalIdRepository.save(newNationalId)

        // Delete user folder and files
        imageUploadService.deleteUserFolder(profile.userId)

        return ResponseEntity.ok(newNationalId.toNationalIdResponse())
    }

    @GetMapping("list")
    fun listAll(
        @PathVariable("id") profileId: Int
    ): ResponseEntity<List<NationalIdResponse>> =
        authenticationService
            .getProfileById(profileId)
            ?.nationalIdList?.map {
                it.toNationalIdResponse()
            }
            ?.let { ResponseEntity.ok(it) } ?: ResponseEntity.notFound().build()

    @GetMapping("{national_id}/front")
    fun getFrontImageOfNationalId(
        @PathVariable("id") profileId: Int,
        @PathVariable("national_id") nationalIdId: Int
    ): ResponseEntity<ByteArray> =
        authenticationService.getProfileById(profileId)?.nationalIdList?.find { it.id == nationalIdId }
            ?.let { nationalId ->
                nationalId.front?.let {
                    ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(it)
                }
            } ?: ResponseEntity.notFound().build()

    @GetMapping("{national_id}/back")
    fun getBackImageOfNationalId(
        @PathVariable("id") profileId: Int,
        @PathVariable("national_id") nationalIdId: Int
    ): ResponseEntity<ByteArray> =
        authenticationService.getProfileById(profileId)?.nationalIdList?.find { it.id == nationalIdId }
            ?.let { nationalId ->
                nationalId.back?.let {
                    ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(it)
                }
            } ?: ResponseEntity.notFound().build()

    @DeleteMapping("{national_id}")
    fun delete(
        @PathVariable("id") profileId: Int,
        @PathVariable("national_id") nationalIdId: Int
    ): ResponseEntity<Any> = authenticationService.getProfileById(profileId)?.let { profile ->
        val ok = profile.nationalIdList.removeIf { it.id == nationalIdId }
        if (ok) {
            nationalIdRepository.deleteById(nationalIdId)
            ResponseEntity.ok().build()
        } else null
    } ?: ResponseEntity.notFound().build()
}