package com.identity.backend.controllers

import com.identity.backend.data.entities.OtherId
import com.identity.backend.data.request.OtherIdRequest
import com.identity.backend.data.response.OtherIdResponse
import com.identity.backend.repository.OtherIdRepository
import com.identity.backend.services.AuthenticationService
import com.identity.backend.services.ImageUploadService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user/profiles/{id}/other")
class OtherIdController(
    val imageUploadService: ImageUploadService,
    val authenticationService: AuthenticationService,
) {
    @Autowired
    private lateinit var otherIdRepository: OtherIdRepository

    @PostMapping("new")
    fun createOtherId(
        @PathVariable("id") profileId: Int,
        @RequestBody otherIdRequest: OtherIdRequest
    ): ResponseEntity<OtherIdResponse> {
        val profile = authenticationService.getProfileById(profileId) ?: return ResponseEntity.notFound().build()

        val newOtherId = OtherId(
            profileId = profileId
        )

        //Add front image to the id
        otherIdRequest.front?.let {
            newOtherId.front = imageUploadService.findImageFile(it, profile.userId).readBytes()
        }

        //Add back image to the id
        otherIdRequest.back?.let {
            newOtherId.back = imageUploadService.findImageFile(it, profile.userId).readBytes()
        }

        otherIdRepository.save(newOtherId)

        // Delete user folder and files
        imageUploadService.deleteUserFolder(profile.userId)

        return ResponseEntity.ok(newOtherId.toOtherIdResponse())
    }

    @GetMapping("list")
    fun listAll(
        @PathVariable("id") profileId: Int
    ): ResponseEntity<List<OtherIdResponse>> =
        authenticationService
            .getProfileById(profileId)
            ?.otherIdList?.map {
                it.toOtherIdResponse()
            }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @GetMapping("{other_id}/front")
    fun getFrontImageOfOtherId(
        @PathVariable("id") profileId: Int,
        @PathVariable("other_id") otherIdId: Int
    ): ResponseEntity<ByteArray> =
        authenticationService.getProfileById(profileId)?.otherIdList?.find { it.id == otherIdId }?.let { otherId ->
            otherId.front?.let {
                ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(it)
            }
        } ?: ResponseEntity.notFound().build()

    @GetMapping("{other_id}/back")
    fun getBackImageOfOtherId(
        @PathVariable("id") profileId: Int,
        @PathVariable("other_id") otherIdId: Int
    ): ResponseEntity<ByteArray> =
        authenticationService.getProfileById(profileId)?.otherIdList?.find { it.id == otherIdId }?.let { otherId ->
            otherId.back?.let {
                ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(it)
            }
        } ?: ResponseEntity.notFound().build()

    @GetMapping("{other_id}")
    fun details(
        @PathVariable("id") profileId: Int,
        @PathVariable("other_id") otherIdId: Int
    ): ResponseEntity<OtherIdResponse> = authenticationService.getProfileById(profileId)?.let {
        otherIdRepository.findById(otherIdId).get().let {
            ResponseEntity.ok(it.toOtherIdResponse())
        }
    } ?: ResponseEntity.notFound().build()

    @DeleteMapping("{other_id}")
    fun delete(
        @PathVariable("id") profileId: Int,
        @PathVariable("other_id") otherIdId: Int
    ): ResponseEntity<Any> = authenticationService.getProfileById(profileId)?.let { profile ->
        val ok = profile.otherIdList.removeIf { it.id == otherIdId }
        if (ok) {
            otherIdRepository.deleteById(otherIdId)
            ResponseEntity.ok().build()
        } else null
    } ?: ResponseEntity.notFound().build()

    private fun OtherId.toOtherIdResponse() = OtherIdResponse(
        id = this.id,
        profileId = this.profileId
    )
}