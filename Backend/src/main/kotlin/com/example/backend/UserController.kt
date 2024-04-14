package com.example.backend

import com.example.backend.data.User
import com.example.backend.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/")
class UserController {

    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): User {
        return userRepository.findById(id)
            .orElseThrow {
                RuntimeException(
                    "User not found"
                )
            }
    }

    @GetMapping
    fun default(): ResponseEntity<String> {
        return ResponseEntity.ok("OK")
    }
}