package com.identity.backend.services

import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader

@Service
class CornerDetectionService {
    fun runDetection(imagePath: String): String? {
        // Configure the command according to provided parameters
        val command = mutableListOf("python", SCRIPT_PATH)
        command.add(imagePath)
        // Start the process
        val process = ProcessBuilder(command).start()
        // Read results
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        var line: String?
        var result: String? = null
        while (reader.readLine().also { line = it } != null) {
            println(line)
            // If the line starts and ends with curly braces, we got the result
            if (line?.contains("""\[[^\[\]]*?]""".toRegex()) == true) {
                result = line
            }
        }
        val exitCode = process.waitFor()
        println("Python script exited with code $exitCode")
        return result
    }

    companion object {
        val SCRIPT_PATH = System.getProperty("user.dir") + "/ocr/corners.py"
    }
}