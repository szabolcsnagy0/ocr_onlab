package com.identity.backend.controllers

import com.identity.backend.data.entities.Profile
import com.identity.backend.data.response.ProfileResponse
import com.identity.backend.repository.ProfilesRepository
import com.identity.backend.repository.UserRepository
import com.identity.backend.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("user")
class UserController(
    val authenticationService: AuthenticationService
) {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var profilesRepository: ProfilesRepository

    @CrossOrigin
    @GetMapping("profiles/list")
    fun getUserProfiles()
            : ResponseEntity<List<ProfileResponse>> = authenticationService.getEmail()?.let { email ->
        userRepository.findByEmail(email)?.let { user ->
            user.profiles.map {
                ProfileResponse(
                    id = it.id,
                    name = it.name
                )
            }.let {
                ResponseEntity.ok(it)
            }
        }
    } ?: ResponseEntity.notFound().build()

    @PostMapping("profiles/new")
    fun createNewProfile(@RequestBody profile: ProfileResponse): ResponseEntity<Profile> {
        val user = authenticationService.getEmail()?.let { email ->
            userRepository.findByEmail(email)
        } ?: return ResponseEntity.notFound().build()

        val newProfile = Profile(name = profile.name, userId = user.id)

        profilesRepository.save(newProfile)

        return ResponseEntity.ok(newProfile)
    }

    @PatchMapping("profiles/{id}/edit")
    fun changeProfileName(
        @PathVariable("id") profileId: Int,
        @RequestBody newProfile: ProfileResponse
    ): ResponseEntity<Any> {
        val profile = authenticationService.getEmail()?.let { email ->
            userRepository.findByEmail(email)?.profiles?.find {
                it.id == profileId
            }
        } ?: return ResponseEntity.notFound().build()

        profile.name = newProfile.name
        profilesRepository.save(profile)

        return ResponseEntity.ok().build()
    }
}