package com.example.backend

import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDate
import kotlin.random.Random


@RestController
@RequestMapping("")
class ImageUploadController(
    val textDetectionService: TextDetectionService,
    val cornerDetectionService: CornerDetectionService
) {

    @PostMapping("/upload/text", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(
        @RequestParam("front") front: MultipartFile?,
        @RequestParam("back") back: MultipartFile?
    ): String? {
        if (front == null && back == null) throw RuntimeException("Image not found!")
        if (!File(UPLOAD_DIRECTORY).exists()) {
            File(UPLOAD_DIRECTORY).mkdir()
        }
        // Front image
        var frontFileNameAndPath: String? = null
        if (front != null) {
            frontFileNameAndPath = UPLOAD_DIRECTORY + Random.nextInt(Int.MAX_VALUE) + front.originalFilename
            front.transferTo(File(frontFileNameAndPath).also { it.deleteOnExit() })
        }
        // Back image
        var backFileNameAndPath: String? = null
        if (back != null) {
            backFileNameAndPath = UPLOAD_DIRECTORY + Random.nextInt(Int.MAX_VALUE) + back.originalFilename
            back.transferTo(File(backFileNameAndPath).also { it.deleteOnExit() })
        }
        return textDetectionService.runDetection(
            frontImagePath = frontFileNameAndPath,
            backImagePath = backFileNameAndPath
        )
    }

    @PostMapping("/upload/corners")
    fun detectCorners(
        @RequestParam("image") image: MultipartFile?,
    ): String? {
        if (image == null) throw RuntimeException("Image not found!")
        if (!File(UPLOAD_DIRECTORY).exists()) {
            File(UPLOAD_DIRECTORY).mkdir()
        }
        // Front image
        var imageFileNameAndPath: String? = null
        imageFileNameAndPath = UPLOAD_DIRECTORY + Random.nextInt(Int.MAX_VALUE) + image.originalFilename
        image.transferTo(File(imageFileNameAndPath).also { it.deleteOnExit() })
        return cornerDetectionService.runDetection(imagePath = imageFileNameAndPath)
    }

    @GetMapping("/download", produces = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun download(): MultipartFile? {
        return null
    }

    companion object {
        val UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images/"
    }
}