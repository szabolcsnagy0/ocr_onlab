package com.example.backend

import com.example.backend.data.Person
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("selected/")
class ExtensionController {
    val person = Person(
        name = "John Doe",
        can = "123456",
        sex = "M",
        dateOfBirth = "050505",
        documentNr = "12346IE"
    )

    @CrossOrigin
    @GetMapping("data")
    fun getName(): ResponseEntity<Person?> {
        Thread.sleep(2000)
        return ResponseEntity.ok().body(person)
    }

}