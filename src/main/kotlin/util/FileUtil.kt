package util

import java.net.URL

private fun getResource(fileName: String): URL = {}::class.java.getResource(fileName)!!

fun readFileLines(fileName: String): List<String> = getResource(fileName).readText().split("\n")
