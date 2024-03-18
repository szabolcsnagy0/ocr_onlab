package com.example.backend

import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.InputStreamReader

@Service
class CropService {
    fun runCropAlgorithm(imagePath: String, corners: String): Boolean {
        // Configure the command according to provided parameters
        val command = mutableListOf("python", SCRIPT_PATH)
        // Add required parameters
        command.addLast("--src")
        command.addLast(imagePath)

        command.addLast("--corners")
        for (coord in corners.split(" ")) {
            command.addLast(coord)
        }
        // Start the process
        val process = ProcessBuilder(command).start()
        // Read results
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            println(line)
        }
        val exitCode = process.waitFor()
        println("Python script exited with code $exitCode")
        return exitCode == 0
    }

    companion object {
        const val SCRIPT_PATH = "D:\\ocr_onlab\\Backend\\crop.py"
    }
}