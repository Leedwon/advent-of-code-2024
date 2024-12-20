package day14

import util.readFileLines

private const val fileName = "/day14.txt"

private data class Robot(
    val startingPosition: Point,
    val velocity: Point
)

private data class Point(
    val x: Int,
    val y: Int
)

private data class Quadrant(
    val xRange: IntRange,
    val yRange: IntRange
)

private operator fun Point.plus(other: Point) = Point(x = x + other.x, y = y + other.y)

fun solveDay141(fileName: String, width: Int = 101, height: Int = 103): Int {
    val robots = parse(readFileLines(fileName))

    val finalPositions = robots
        .map {
            it.move(
                seconds = 100,
                width = width,
                height = height
            )
        }

    val halfWidth = width / 2
    val halfHeight = height / 2

    val topLeft = Quadrant(
        xRange = 0..<halfWidth,
        yRange = 0..<halfHeight
    )
    val topRight = Quadrant(
        xRange = halfWidth + 1..<width,
        yRange = 0..<halfHeight
    )
    val bottomLeft = Quadrant(
        xRange = 0..<halfWidth,
        yRange = halfHeight + 1..<height
    )
    val bottomRight = Quadrant(
        xRange = halfWidth + 1..<width,
        yRange = halfHeight + 1..<height
    )

    val quadrants = listOf(
        topLeft,
        topRight,
        bottomLeft,
        bottomRight
    )

    return quadrants.fold(1) { acc, quadrant -> acc * quadrant.calculateRobots(positions = finalPositions) }
}

fun solveDay142(fileName: String, width: Int = 101, height: Int = 103) {
    val robots = parse(readFileLines(fileName))

    repeat(10_000) { second ->
        val finalPositions = robots
            .map {
                it.move(
                    seconds = second,
                    width = width,
                    height = height
                )
            }

        if (hasPossibleChristmasTree(width = width, height = height, finalPositions.toSet())) {
            debugPrint(
                second = second,
                width = width,
                height = height,
                finalPositions
            )
        }
    }
}

private fun Robot.move(seconds: Int, width: Int, height: Int): Point {
    var finalX = (startingPosition.x + seconds * velocity.x) % width
    var finalY = (startingPosition.y + seconds * velocity.y) % height

    if (finalX < 0) {
        finalX += width
    }

    if (finalY < 0) {
        finalY += height
    }

    return Point(
        x = finalX,
        y = finalY
    )
}

private fun hasPossibleChristmasTree(
    width: Int,
    height: Int,
    positions: Set<Point>
): Boolean {
    val threshold = 10

    for (row in 0..<height) {
        var neighbours = 0
        for (column in 0..<width) {
            val robotAtCurrentPosition = positions.contains(Point(x = column, y = row))
            if (robotAtCurrentPosition) {
                neighbours++
            } else {
                neighbours = 0
            }
            if (neighbours == threshold) {
                return true
            }
        }
    }

    return false
}

private fun debugPrint(second: Int, width: Int, height: Int, positions: List<Point>) {
    println("-----------$second second-----------")
    for (row in 0..<height) {
        for (column in 0..<width) {
            val count = positions.count { it.x == column && it.y == row }
            if (count > 0) {
                print("X")
            } else {
                print(".")
            }
        }
        print("\n")
    }
}

private fun Quadrant.calculateRobots(positions: List<Point>): Int {
    return positions.count { it.x in xRange && it.y in yRange }
}

private fun parse(lines: List<String>): List<Robot> {
    return lines.map { it.toRobot() }
}

private fun String.toRobot(): Robot {
    val (position, velocity) = split(" ")
    return Robot(
        startingPosition = position.toPoint(),
        velocity = velocity.toPoint()
    )
}

private fun String.toPoint(): Point {
    val (x, y) = this
        .split("=")
        .last()
        .split(",")
        .map { it.toInt() }

    return Point(
        x = x,
        y = y
    )
}

fun main() {
    println(solveDay141(fileName))
    println(solveDay142(fileName))
}