package day1

import util.readFileLines
import kotlin.math.abs

private const val inputFile = "/day1.txt"

fun solveDay11(inputFile: String): Int {
    val (firstList, secondList) = parseLists(readFileLines(inputFile)).let { it.first.sorted() to it.second.sorted() }

    return firstList
        .mapIndexed { index, value -> abs(value - secondList[index]) }
        .sum()
}

fun solveDay12(inputFile: String): Int {
    val (firstList, secondList) = parseLists(readFileLines(inputFile))

    val buckets = mutableMapOf<Int, Int>()
    secondList.forEach {
        buckets[it] = (buckets[it] ?: 0) + 1
    }

    return firstList.sumOf { value ->
        value * (buckets[value] ?: 0)
    }
}

private fun parseLists(lines: List<String>): Pair<List<Int>, List<Int>> {
    val firstList = mutableListOf<Int>()
    val secondList = mutableListOf<Int>()

    lines.map { line ->
        line
            .split(" ")
            .filter { it.isNotEmpty() }
            .map { it.toInt() }
            .let { it[0] to it[1] }
    }.forEach {
        firstList.add(it.first)
        secondList.add(it.second)
    }
    return firstList to secondList
}

fun main() {
    println(solveDay11(inputFile))
    println(solveDay12(inputFile))
}