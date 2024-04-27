package com.identity.backend.controllers

import com.identity.backend.repository.UserRepository
import com.identity.backend.services.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("image")
class ImageUploadController(
    val textDetectionService: TextDetectionService,
    val cornerDetectionService: CornerDetectionService,
    val cropService: CropService,
    val imageUploadService: ImageUploadService,
    val authenticationService: AuthenticationService
) {

    @Autowired
    private lateinit var userRepository: UserRepository

    @GetMapping("detection")
    fun detection(
        @RequestParam("front") front: String?,
        @RequestParam("back") back: String?
    ): ResponseEntity<String?> {
        try {
            if (front == null || back == null) throw RuntimeException("Missing parameters!")

            val userId = findUserId()
            // Front image
            val frontFile = imageUploadService.findImageFile(front, userId)
            // Back image
            val backFile = imageUploadService.findImageFile(back, userId)

            // Detection
            val result = textDetectionService.runDetection(
                frontImagePath = frontFile.path,
                backImagePath = backFile.path
            )

            return ResponseEntity.ok(result)
        } catch (ex: Exception) {
            print(ex.message)
            return ResponseEntity.badRequest().body(ex.message)
        }
    }

    @PostMapping("corners")
    fun detectCorners(
        @RequestParam("image") image: MultipartFile?,
    ): String? {
        if (image == null) throw RuntimeException("Image not found!")

        val imageFile =
            imageUploadService.saveToFile(image = image, userId = findUserId())
                ?: throw RuntimeException("File could not be created!")

        return cornerDetectionService.runDetection(imagePath = imageFile.path).also { imageFile.delete() }
    }

    @PostMapping("crop")
    fun cropImage(
        @RequestParam("image") image: MultipartFile?,
        @RequestParam("corners") corners: String?
    ): ResponseEntity<Any> {
        if (image == null) throw RuntimeException("Image not found!")
        if (corners == null) throw RuntimeException("Corner coordinates not found!")

        val imageFile = imageUploadService.saveToFile(image = image, userId = findUserId())
            ?: throw RuntimeException("File could not be created!")

        // Cropping the image
        return if (cropService.runCropAlgorithm(imagePath = imageFile.path, corners = corners)) {
            val imagePath = imageFile.name
            ResponseEntity.ok().body(imagePath)
        } else {
            imageFile.delete()
            ResponseEntity.internalServerError().build()
        }
    }

    @GetMapping("download/{id}")
    fun downloadCroppedImage(
        @PathVariable id: String
    ): ResponseEntity<ByteArray> {
        val image = imageUploadService.findImageFile(id, findUserId())
        return if (image.exists()) {
            ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image.readBytes())
        } else {
            ResponseEntity.notFound().build()
        }
    }

    private fun findUserId() = authenticationService.getEmail()?.let { email ->
        userRepository.findByEmail(email)?.id
    } ?: throw RuntimeException("User not found!")
}