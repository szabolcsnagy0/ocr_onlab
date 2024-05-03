package com.identity.backend.services

import org.springframework.stereotype.Service

@Service
class CornerDetectionService {
    fun runDetection(imagePath: String): String? {
        println("CORNER DETECTION: imagePath: $imagePath")
        // Configure the command according to provided parameters
        val command = mutableListOf("python", SCRIPT_PATH)
        command.add(imagePath)
        println("COMMAND: $command")
        // Start the process
        val process = ProcessBuilder(command).start()
        // Read results
        val reader = process.inputReader()
        var line: String?
        var result: String? = null
        while (reader.readLine().also { line = it } != null) {
            println(line)
            if (line?.contains("""\[[^\[\]]*?]""".toRegex()) == true) {
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
        val SCRIPT_PATH = System.getProperty("user.dir") + "/ocr/corners.py"
    }
}