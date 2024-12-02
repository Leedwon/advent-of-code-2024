package day2

import util.readFileLines
import kotlin.math.abs

private val fileName = "/day2.txt"

private const val minDistance = 1
private const val maxDistance = 3

fun solveDay21(fileName: String): Int {
    return readFileLines(fileName)
        .map { line -> line.split(" ").map { it.toInt() } }
        .count { report -> report.isSafe() }
}

fun solveDay22(fileName: String): Int {
    return readFileLines(fileName)
        .map { line -> line.split(" ").map { it.toInt() } }
        .count { report -> report.isSafeWithTolerance() }
}

private fun List<Int>.isSafe(): Boolean {
    check(size >= 2) { "Report is too short" }

    val decreasing = this[0] - this[1] > 0

    zipWithNext { previous, next ->
        if (decreasing && previous <= next) return false
        if (!decreasing && previous >= next) return false

        val distance = abs(previous - next)
        if (distance < minDistance || distance > maxDistance) return false
    }

    return true
}

private fun List<Int>.isSafeWithTolerance(): Boolean {
    val allLists = indices.map { index ->
        this.toMutableList().apply { removeAt(index) }
    }

    return allLists.any { it.isSafe() }
}

fun main() {
    println(solveDay21(fileName))
    println(solveDay22(fileName))
}
