package day8

import util.readFileLines

private const val fileName = "/day8.txt"

fun solveDay81(fileName: String): Int {
    val lines = readFileLines(fileName)

    val map = buildAntennasMap(lines)

    val antinodes = mutableSetOf<Pair<Int, Int>>()

    map.keys.forEach { antenna ->
        val points = map[antenna]!!

        for (i in 0 until points.lastIndex) {
            for (j in 1 + i..points.lastIndex) {
                val firstAntenna = points[i]
                val secondAntenna = points[j]

                val xDistance = firstAntenna.first - secondAntenna.first
                val yDistance = firstAntenna.second - secondAntenna.second

                val d = xDistance to yDistance

                antinodes.add(firstAntenna + d)
                antinodes.add(secondAntenna - d)
            }
        }
    }

    val rows = lines.size
    val columns = lines.first().length

    return antinodes.count { it.first in 0 until columns && it.second in 0 until rows }
}

fun solveDay82(fileName: String): Int {
    val lines = readFileLines(fileName)

    val map = buildAntennasMap(lines)

    val antinodes = mutableSetOf<Pair<Int, Int>>()

    val maxY = lines.lastIndex
    val maxX = lines.first().lastIndex

    map.keys.forEach { antenna ->
        val points = map[antenna]!!

        for (i in 0 until points.lastIndex) {
            for (j in 1 + i..points.lastIndex) {
                val firstAntenna = points[i]
                val secondAntenna = points[j]

                val xDistance = firstAntenna.first - secondAntenna.first
                val yDistance = firstAntenna.second - secondAntenna.second

                val d = xDistance to yDistance

                // move up left
                var nextPoint = firstAntenna
                while (nextPoint.first in 0..maxX && nextPoint.second in 0..maxY) {
                    antinodes.add(nextPoint)
                    nextPoint += d
                }

                // move down right
                nextPoint = secondAntenna
                while (nextPoint.first in 0..maxX && nextPoint.second in 0..maxY) {
                    antinodes.add(nextPoint)
                    nextPoint -= d
                }
            }
        }
    }

    debugPrint(
        lines = lines,
        antinodes = antinodes.toList()
    )

    return antinodes.size
}

private fun buildAntennasMap(lines: List<String>): Map<Char, List<Pair<Int, Int>>> {
    val map = mutableMapOf<Char, List<Pair<Int, Int>>>()

    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            if (char != '.') {
                val point = column to row
                val current = map[char].orEmpty()

                map[char] = current + point
            }
        }
    }

    return map
}

private fun debugPrint(
    lines: List<String>,
    antinodes: List<Pair<Int, Int>>
) {
    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            val point = column to row
            if (antinodes.contains(point)) {
                print("#")
            } else {
                print(char)
            }
        }
        print("\n")
    }
}

private operator fun Pair<Int, Int>.minus(other: Pair<Int, Int>): Pair<Int, Int> {
    return first - other.first to second - other.second
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return first + other.first to second + other.second
}

fun main() {
    println(solveDay81(fileName))
    println(solveDay82(fileName))
}
