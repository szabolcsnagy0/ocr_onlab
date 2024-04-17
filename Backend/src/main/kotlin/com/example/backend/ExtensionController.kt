package com.example.backend

import com.example.backend.data.Profile
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("selected")
class ExtensionController {

    @CrossOrigin
    @GetMapping("data")
    fun getName(): ResponseEntity<Profile?> {
//        Thread.sleep(2000)
        return ResponseEntity.ok().body(null)
    }

    @GetMapping
    fun default() = ResponseEntity.ok().body("OK")
}