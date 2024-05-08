package com.identity.backend.controllers

import com.identity.backend.data.entities.Profile
import com.identity.backend.data.response.ProfileResponse
import com.identity.backend.data.response.toProfileResponse
import com.identity.backend.repository.ProfilesRepository
import com.identity.backend.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("user/profiles")
class UserController(
    val authenticationService: AuthenticationService,
) {
    @Autowired
    private lateinit var profilesRepository: ProfilesRepository

    @CrossOrigin
    @GetMapping("list")
    fun getUserProfiles()
            : ResponseEntity<List<ProfileResponse>> = authenticationService.getUser()?.let { user ->
        user.profiles.map {
            it.toProfileResponse()
        }.let {
            ResponseEntity.ok(it)
        }
    } ?: ResponseEntity.notFound().build()

    @PostMapping("new")
    fun createNewProfile(@RequestBody profile: ProfileResponse): ResponseEntity<Profile> {
        val user = authenticationService.getUser() ?: return ResponseEntity.notFound().build()

        val newProfile = Profile(name = profile.name, userId = user.id)

        profilesRepository.save(newProfile)

        return ResponseEntity.ok(newProfile)
    }

    @PatchMapping("{id}/edit")
    fun changeProfileName(
        @PathVariable("id") profileId: Int,
        @RequestBody newProfile: ProfileResponse
    ): ResponseEntity<Any> {
        val profile = authenticationService.getProfileById(profileId) ?: return ResponseEntity.notFound().build()

        profile.name = newProfile.name
        profilesRepository.save(profile)

        return ResponseEntity.ok().build()
    }

    @GetMapping("{id}")
    fun getProfileDetails(
        @PathVariable("id") profileId: Int
    ): ResponseEntity<ProfileResponse> = authenticationService.getProfileById(profileId)?.let {
        ResponseEntity.ok(it.toProfileResponse())
    } ?: ResponseEntity.notFound().build()

    @DeleteMapping("{id}")
    fun deleteProfile(
        @PathVariable("id") profileId: Int
    ): ResponseEntity<Any> = authenticationService.getUser()?.let { user ->
        val ok = user.profiles.removeIf { it.id == profileId }
        if (ok) {
            profilesRepository.deleteById(profileId)
            ResponseEntity.ok().build()
        } else null
    } ?: ResponseEntity.notFound().build()
}