package com.example.backend

import jakarta.servlet.ServletContext
import org.apache.tomcat.util.http.fileupload.IOUtils
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.InputStream
import kotlin.random.Random


@RestController
@RequestMapping("/")
class ImageUploadController(
    val textDetectionService: TextDetectionService,
    val cornerDetectionService: CornerDetectionService,
    val cropService: CropService
) {

    @PostMapping("upload/text", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
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

    @PostMapping("upload/corners")
    fun detectCorners(
        @RequestParam("image") image: MultipartFile?,
    ): String? {
        if (image == null) throw RuntimeException("Image not found!")
        if (!File(UPLOAD_DIRECTORY).exists()) {
            File(UPLOAD_DIRECTORY).mkdir()
        }
        // Front image
        val imageFileNameAndPath: String?
        imageFileNameAndPath = UPLOAD_DIRECTORY + Random.nextInt(Int.MAX_VALUE) + image.originalFilename
        image.transferTo(File(imageFileNameAndPath).also { it.deleteOnExit() })
        return cornerDetectionService.runDetection(imagePath = imageFileNameAndPath)
    }

    @PostMapping("upload/crop")
    fun cropImage(
        @RequestParam("image") image: MultipartFile?,
        @RequestParam("corners") corners: String?
    ): ResponseEntity<String?> {
        if (image == null) throw RuntimeException("Image not found!")
        if (corners == null) throw RuntimeException("Corner coordinates not found!")

        val imageID: String = "" + Random.nextInt(Int.MAX_VALUE) + image.originalFilename
        val imageFileNameAndPath = UPLOAD_DIRECTORY + imageID
        image.transferTo(File(imageFileNameAndPath).also { it.deleteOnExit() })
        return if (cropService.runCropAlgorithm(imagePath = imageFileNameAndPath, corners = corners)) {
            ResponseEntity.ok(imageID)
        } else {
            ResponseEntity.internalServerError().body(null)
        }
    }

    @GetMapping("download/{id}")
    fun downloadCroppedImage(
        @PathVariable id: String
    ): ResponseEntity<ByteArray> {
        val image = File(UPLOAD_DIRECTORY + id)
        return if (image.exists()) {
            ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image.readBytes())
        } else {
            ResponseEntity.notFound().build()
        }

    }

    companion object {
        val UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images/"
    }
}