package com.example.backend

import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStream

@Service
class TextDetectionService {
    fun runDetection(frontImagePath: String? = null, backImagePath: String? = null): String? {
        // Configure the command according to provided parameters
        val command = mutableListOf("python", SCRIPT_PATH)
        if(frontImagePath != null) {
            command.addLast("--front")
            command.addLast(frontImagePath)
        }
        if(backImagePath != null) {
            command.addLast("--back")
            command.addLast(backImagePath)
        }
        // Start the process
        val process = ProcessBuilder(command).start()
        // Read results
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        var line: String?
        var result: String? = null
        while (reader.readLine().also { line = it } != null) {
            println(line)
            // If the line starts and ends with curly braces, we got the result
            if(line?.contains("""\{.*?}""".toRegex()) == true) {
                result = line
            }
        }
        val exitCode = process.waitFor()
        println("Python script exited with code $exitCode")
        return result
    }

    companion object {
        const val SCRIPT_PATH = "D:\\ocr_onlab\\Backend\\ocr.py"
    }
}