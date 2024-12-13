package day10

import util.readFileLines

private const val fileName = "/day10.txt"

private data class Point(
    val x: Int,
    val y: Int
)

private infix fun Int.pointsTo(y: Int) = Point(x = this, y = y)
private operator fun Point.plus(other: Point) = Point(x = x + other.x, y = y + other.y)

fun solveDay101(fileName: String): Int {
    val lines = readFileLines(fileName)

    val map = mutableMapOf<Point, Int>()

    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            val point = column pointsTo row
            map[point] = char.digitToInt()
        }
    }

    val startingPoints = map.filter { it.value == 0 }.keys
    return startingPoints.sumOf {
        summits(
            startingPoint = it,
            map = map
        )
            .distinct()
            .size
    }
}

fun solveDay102(fileName: String): Int {
    val lines = readFileLines(fileName)

    val map = mutableMapOf<Point, Int>()

    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            val point = column pointsTo row
            map[point] = char.digitToInt()
        }
    }

    val startingPoints = map.filter { it.value == 0 }.keys
    return startingPoints.sumOf {
        summits(
            startingPoint = it,
            map = map
        ).size
    }
}

private fun summits(startingPoint: Point, map: Map<Point, Int>): List<Point> {
    val summits = mutableListOf<Point>()
    val nextPoints = ArrayDeque(listOf(startingPoint))

    while (nextPoints.isNotEmpty()) {
        val directions = listOf(
            0 pointsTo 1,
            1 pointsTo 0,
            0 pointsTo -1,
            -1 pointsTo 0
        )

        val point = nextPoints.removeFirst()
        val pointValue = map[point]!!

        if (pointValue == 9) {
            summits.add(point)
        } else {
            for (direction in directions) {
                val nextPoint = point + direction
                val nextValue = map.getOrElse(nextPoint) { 0 }
                if (nextValue == pointValue + 1) {
                    nextPoints.add(nextPoint)
                }
            }
        }
    }

    return summits
}

fun main() {
    println(solveDay101(fileName))
    println(solveDay102(fileName))
}
