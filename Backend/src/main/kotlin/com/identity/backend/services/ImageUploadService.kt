package com.identity.backend.services

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import kotlin.random.Random

@Service
class ImageUploadService {
    fun saveToFile(image: MultipartFile?, userId: Int): File? = image?.let { img ->
        println("SAVING FILE: userId:${userId}")
        if (!File(UPLOAD_DIRECTORY).exists()) {
            println("MAKING DIRECTORY: $UPLOAD_DIRECTORY")
            File(UPLOAD_DIRECTORY).mkdir()
        }
        println("MAKING USER DIRECTORY")
        if (!File(userId.generateDirectoryFromId()).exists()) {
            println("CREATING DIRECTORY: ${userId.generateDirectoryFromId()}")
            File(userId.generateDirectoryFromId()).mkdir()
        }
        File(img.generateFilePath(userId)).let {
            img.transferTo(it)
            if (!it.exists()) {
                println("ERROR: FILE NOT EXISTING: ${it.name}")
                null
            }
            else {
                println("FILE CREATED: ${it.name}")
                it
            }
        }
    }

    fun findImageFile(imageId: String, userId: Int): File =
        File(userId.generateDirectoryFromId() + imageId)

    fun deleteUserFolder(userId: Int) {
        File(userId.generateDirectoryFromId()).let {
            if (it.exists()) {
                it.deleteRecursively()
            }
        }
    }

    private fun Int.generateDirectoryFromId(): String =
        "$UPLOAD_DIRECTORY$this/"

    private fun MultipartFile.generateFilePath(userId: Int): String =
        userId.generateDirectoryFromId() + Random.nextInt(Int.MAX_VALUE) + "." + this.originalFilename?.substringAfterLast(
            '.'
        )

    companion object {
        val UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/images/"
    }
}