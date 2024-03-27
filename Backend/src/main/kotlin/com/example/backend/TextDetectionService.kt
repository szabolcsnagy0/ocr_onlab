package com.example.backend

import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStream
import java.nio.charset.StandardCharsets

@Service
class TextDetectionService {
    fun runDetection(frontImagePath: String? = null, backImagePath: String? = null): String? {
        // Configure the command according to provided parameters
        val command = mutableListOf("python", SCRIPT_PATH).apply {
            addLast("--localization")
            addLast(LOCALIZATION_PATH)
            frontImagePath?.let {
                addLast("--front")
                addLast(frontImagePath)
            }
            backImagePath?.let {
                addLast("--back")
                addLast(backImagePath)
            }
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
        val errorReader = process.errorReader()
        var error: String?
        while (errorReader.readLine().also { error = it } != null) println(error)
        val exitCode = process.waitFor()
        println("Python script exited with code $exitCode")
        return result
    }

    companion object {
        const val SCRIPT_PATH = "D:\\ocr_onlab\\Backend\\ocr\\ocr.py"
        const val LOCALIZATION_PATH = "D:\\ocr_onlab\\Backend\\ocr\\text_localization.json"
    }
}