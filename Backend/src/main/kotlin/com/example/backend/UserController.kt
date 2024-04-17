package com.example.backend

import com.example.backend.data.NationalId
import com.example.backend.data.Profile
import com.example.backend.data.User
import com.example.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("users")
class UserController {

    @Autowired
    private lateinit var userRepository: UserRepository

    @CrossOrigin
    @GetMapping("{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        return userRepository.findById(id).let { user ->
            if (user.isPresent) {
                ResponseEntity.ok(user.get())
            } else ResponseEntity.notFound().build()
        }
    }

    @CrossOrigin
    @GetMapping("{id}/profiles")
    fun getUserProfiles(@PathVariable id: Long): ResponseEntity<List<Profile>> {
        return userRepository.findById(id).let { user ->
            if (user.isPresent) {
                ResponseEntity.ok(user.get().profiles)
            } else ResponseEntity.notFound().build()
        }
    }

    @CrossOrigin
    @GetMapping("{id}/profiles/{profile_id}")
    fun getNationalIdOfProfile(
        @PathVariable id: Long,
        @PathVariable("profile_id") profileId: Long
    ): ResponseEntity<NationalId> {
        val user = userRepository.findById(id)
        if (user.isPresent) {
            val foundProfile = user.get().profiles.find { it.id == profileId }
            if (foundProfile?.nationalId != null) {
                return ResponseEntity.ok(foundProfile.nationalId)
            }
        }
        return ResponseEntity.notFound().build()
    }
}