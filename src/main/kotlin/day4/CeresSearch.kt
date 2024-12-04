package day4

import util.readFileLines
import kotlin.math.pow
import kotlin.math.sqrt

private const val fileName = "/day4.txt"

typealias CharsGrid = List<List<Char>>
typealias Point = Pair<Int, Int>

fun solveDay41(fileName: String): Int {
    val charsGrid = readFileLines(fileName).toCharsGrid()

    val rows = charsGrid.first().size
    val columns = charsGrid.size

    var count = 0

    for (row in 0 until rows) {
        for (column in 0 until columns) {
            count += charsGrid.lookupAllXmas(
                row = row,
                column = column
            )
        }
    }

    return count
}

fun solveDay42(fileName: String): Int {
    val charsGrid = readFileLines(fileName).toCharsGrid()

    val rows = charsGrid.first().size
    val columns = charsGrid.size

    val masPaths = mutableListOf<List<Point>>()

    for (row in 0 until rows) {
        for (column in 0 until columns) {
            masPaths.addAll(
                charsGrid.lookupAllMas(
                    row = row,
                    column = column
                )
            )
        }
    }

    return countXMasses(masPaths)
}

private fun List<String>.toCharsGrid(): CharsGrid = map { line -> line.map { it } }

/**
 * @return amount of found XMAS
 */
private fun CharsGrid.lookupAllXmas(row: Int, column: Int): Int {
    val directions = listOf(
        0 to 1,
        1 to 1,
        1 to 0,
        1 to -1,
        0 to -1,
        -1 to -1,
        -1 to 0,
        -1 to 1
    )

    return directions.count { direction ->
        lookupXmas(
            row = row,
            column = column,
            direction = direction,
            current = this[row][column].toString()
        )
    }
}

private fun CharsGrid.lookupXmas(
    row: Int,
    column: Int,
    direction: Pair<Int, Int>,
    current: String,
): Boolean {
    if (!"XMAS".startsWith(current)) return false

    val (x, y) = direction
    val newRow = row + x
    val newColumn = column + y
    val next = this.getOrNull(newRow)?.getOrNull(newColumn) ?: return false
    val str = current + next

    return when {
        str == "XMAS" -> true
        "XMAS".startsWith(str) -> lookupXmas(
            row = newRow,
            column = newColumn,
            direction = direction,
            current = str
        )

        else -> false
    }
}

/**
 * @return Indexes of all MAS appearances i.e [(0,0), (1,0), (2,0)]
 */
fun CharsGrid.lookupAllMas(
    row: Int,
    column: Int
): List<List<Point>> {
    val directions = listOf(
        1 to 1,
        1 to -1,
        -1 to 1,
        -1 to -1
    )

    return directions.mapNotNull { direction ->
        lookupMas(
            row = row,
            column = column,
            direction = direction,
            current = this[row][column].toString(),
            runningPath = listOf(row to column)
        )
    }
}

private fun countXMasses(masPaths: List<List<Point>>): Int {
    val countedPaths = mutableListOf<Pair<List<Point>, List<Point>>>()

    for (masPath in masPaths) {
        for (otherMasPath in masPaths) {
            if (masPath == otherMasPath) continue

            val pair = masPath to otherMasPath
            val possibleAlreadyCountedPair = otherMasPath to masPath

            if (masPath.isXmas(otherMasPath) && possibleAlreadyCountedPair !in countedPaths) {
                countedPaths.add(pair)
            }
        }
    }

    return countedPaths.size
}

private fun List<Point>.isXmas(other: List<Point>): Boolean {
    val (firstPoint, middlePoint, lastPoint) = this
    val (otherFirstPoint, otherMiddlePoint, otherLastPoint) = other

    return firstPoint.distance(otherFirstPoint) == 2 &&
            middlePoint == otherMiddlePoint &&
            lastPoint.distance(otherLastPoint) == 2
}

private fun Point.distance(other: Point): Int {
    val xDist = (first - other.first).toFloat()
    val yDist = (second - other.second).toFloat()
    return sqrt(xDist.pow(2) + yDist.pow(2)).toInt()
}

/**
 * @return Indexes of MAS if found null otherwise
 */
fun CharsGrid.lookupMas(
    row: Int,
    column: Int,
    direction: Point,
    current: String,
    runningPath: List<Point>
): List<Point>? {
    if (!"MAS".startsWith(current)) return null

    val (x, y) = direction
    val newRow = row + x
    val newColumn = column + y
    val next = this.getOrNull(newRow)?.getOrNull(newColumn) ?: return null
    val str = current + next
    val path = runningPath + (newRow to newColumn)

    return when {
        str == "MAS" -> path
        "MAS".startsWith(str) -> lookupMas(
            row = newRow,
            column = newColumn,
            direction = direction,
            current = str,
            runningPath = path
        )

        else -> null
    }
}

fun main() {
    println(solveDay41(fileName))
    println(solveDay42(fileName))
}