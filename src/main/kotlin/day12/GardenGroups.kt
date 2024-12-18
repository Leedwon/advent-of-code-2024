package day12

import util.readFileLines

private const val fileName = "/day12.txt"

private data class Point(
    val x: Int,
    val y: Int,
) {
    override fun toString(): String {
        return when (this) {
            north -> "north"
            south -> "south"
            east -> "east"
            west -> "west"
            else -> ("($x, $y)")
        }
    }
}

private val north = 0 pointsTo -1
private val south = 0 pointsTo 1
private val west = -1 pointsTo 0
private val east = 1 pointsTo 0

private infix fun Int.pointsTo(y: Int) = Point(x = this, y = y)
private operator fun Point.plus(other: Point) = Point(x = x + other.x, y = y + other.y)

private data class Region(
    val pointsOnBorder: MutableSet<Point>,
    val char: Char,
    var area: Int,
    var perimeter: Int
)

fun solveDay121(fileName: String): Int {
    val lines = readFileLines(fileName)

    val map = mutableMapOf<Point, Char>()

    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            val point = column pointsTo row
            map[point] = char
        }
    }

    val regions = buildRegions(map)

    return regions.sumOf { it.area * it.perimeter }
}

fun solveDay122(fileName: String): Int {
    val lines = readFileLines(fileName)

    val map = mutableMapOf<Point, Char>()

    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            val point = column pointsTo row
            map[point] = char
        }
    }

    val regions = buildRegions(map)

    return regions.sumOf { it.calculateBorders(map) * it.area }
}

private fun buildRegions(map: Map<Point, Char>): List<Region> {

    val regions = mutableListOf<Region>()

    val points = ArrayDeque(map.keys)

    while (points.isNotEmpty()) {
        val point = points.first()
        val visited = mutableListOf<Point>()

        val region = Region(
            pointsOnBorder = mutableSetOf(),
            char = map[point]!!,
            area = 0,
            perimeter = 0
        )

        visit(
            point = point,
            map = map,
            visited = visited,
            region = region
        )

        points.removeAll(visited)
        regions.add(region)
    }

    return regions
}

private fun visit(point: Point, map: Map<Point, Char>, visited: MutableList<Point>, region: Region) {
    visited.add(point)

    val value = map[point]!!

    region.area += 1

    val neighbours = listOf(
        1 pointsTo 0,
        -1 pointsTo 0,
        0 pointsTo 1,
        0 pointsTo -1
    )

    for (neighbour in neighbours) {
        val neighbourPoint = point + neighbour
        val neighbourValue = map[neighbourPoint]
        if (neighbourValue == null || neighbourValue != value) {
            region.pointsOnBorder.add(point)
            region.perimeter += 1
        } else if (neighbourPoint !in visited) {
            visit(
                point = neighbourPoint,
                map = map,
                visited = visited,
                region = region
            )
        }
    }
}

private fun Region.calculateBorders(map: Map<Point, Char>): Int {
    val directions = listOf(north, south, west, east)

    return directions.sumOf { direction ->
        var borderCount = 0
        val visitedPoints = mutableSetOf<Point>()
        for (point in pointsOnBorder) {
            if (point in visitedPoints) continue
            val isBorderForDirection = map[(point + direction)] != char
            if (isBorderForDirection) {
                borderCount++
                val borderDirection = when (direction) {
                    north, south -> listOf(west, east)
                    west, east -> listOf(north, south)
                    else -> error("Invalid direction $direction")
                }

                borderDirection.forEach { sideDirection ->
                    var current = point
                    while (map[current] == char && map[current + direction] != char) {
                        visitedPoints += current
                        current += sideDirection
                    }
                }
            }
        }
        borderCount
    }
}

fun main() {
    println(solveDay121(fileName))
    println(solveDay122(fileName))
}