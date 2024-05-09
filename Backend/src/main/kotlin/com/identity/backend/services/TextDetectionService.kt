package com.identity.backend.services

import org.springframework.stereotype.Service

@Service
class TextDetectionService {
    fun runDetection(frontImagePath: String? = null, backImagePath: String? = null): String? {
        // Configure the command according to provided parameters
        val command = mutableListOf("python3", SCRIPT_PATH).apply {
            add("--localization")
            add(LOCALIZATION_PATH)
            frontImagePath?.let {
                add("--front")
                add(frontImagePath)
            }
            backImagePath?.let {
                add("--back")
                add(backImagePath)
            }
        }
        // Start the process
        val process = ProcessBuilder(command).start()
        // Read results
        val reader = process.inputReader()
        var line: String?
        var result: String? = null
        while (reader.readLine().also { line = it } != null) {
            println(line)
            // If the line starts and ends with curly braces, we got the result
            if (line?.contains("""\{.*?}""".toRegex()) == true) {
                result = line
            }
        }
        val errorReader = process.errorReader()
        var error: String?
        while (errorReader.readLine().also { error = it } != null) println(error)
        val exitCode = process.waitFor()
        println("Python script exited with code $exitCode")
        return result
    }

    companion object {
        val SCRIPT_PATH = System.getProperty("user.dir") + "/ocr/ocr.py"
        val LOCALIZATION_PATH = System.getProperty("user.dir") + "/ocr/text_localization.json"
    }
}