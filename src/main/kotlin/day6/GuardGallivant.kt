package day6

import util.readFileLines

private const val fileName = "/day6.txt"

sealed interface Square {
    data object Empty : Square
    data object Blocked : Square
    data class Guard(var direction: Pair<Int, Int>) : Square
    data object Moved : Square

    fun prettyPrint() {
        val str = when (this) {
            Blocked -> '#'
            Empty -> '.'
            is Guard -> when (direction) {
                0 to -1 -> '^'
                1 to 0 -> '>'
                0 to 1 -> 'v'
                -1 to 0 -> '<'
                else -> error("Invalid direction")
            }

            Moved -> 'X'
        }
        print(str)
    }
}

enum class MoveResult {
    Moved, LeftMap, InLoop
}

data class VisitedObstacle(
    val position: Pair<Int, Int>,
    val approachedDirection: Pair<Int, Int>
)

data class LabMap(
    val rows: Int,
    val columns: Int,
    val squares: MutableMap<Pair<Int, Int>, Square>,
    val visitedObstacles: MutableList<VisitedObstacle>
) {
    fun prettyPrint() {
        for (row in 0 until rows) {
            for (col in 0 until columns) {
                squares[col to row]!!.prettyPrint()
            }
            print("\n")
        }
    }
}

private var printMoves = false

fun solveDay61(fileName: String): Int {
    val map = buildMap(readFileLines(fileName))

    map.runSimulation()
    return map.squares.values.count { it is Square.Moved || it is Square.Guard }
}

fun solveDay62(fileName: String): Int {
    val originalMap = buildMap(readFileLines(fileName))
    val initialGuardPosition = originalMap.squares.keys.first { originalMap.squares[it] is Square.Guard }

    val mapForMoves = labMapFrom(originalMap)

    mapForMoves.runSimulation()

    val potentialObstaclePositions = mapForMoves.squares.keys.filter {
        val square = mapForMoves.squares[it]
        square is Square.Moved || square is Square.Guard
    }

    var loops = 0

    // right now we play every simulation over and over - but they usually only differ by one square
    // we should be smarter and use some memory for it to skip moves
    // probably for starters even using only original map would be enough

    for (position in potentialObstaclePositions) {
        if (position == initialGuardPosition) continue
        val mapForSimulation = labMapFrom(originalMap)
        mapForSimulation.squares[initialGuardPosition] =
            Square.Guard(direction = 0 to -1) // deep copy is too much hassle since LabMap and Guard has a lot of mutable factors
        mapForSimulation.squares[position] = Square.Blocked
        if (mapForSimulation.runSimulation() == MoveResult.InLoop) {
            loops++
        }
    }

    return loops
}

fun solveDay62BruteForce(fileName: String): Int {
    val originalMap = buildMap(readFileLines(fileName))
    val initialGuardPosition = originalMap.squares.keys.first { originalMap.squares[it] is Square.Guard }

    val mapForMoves = labMapFrom(originalMap)

    mapForMoves.runSimulation()

    val potentialObstaclePositions = mapForMoves.squares.keys.filter {
        val square = mapForMoves.squares[it]
        square is Square.Moved || square is Square.Guard
    }

    var loops = 0

    for (position in potentialObstaclePositions) {
        if (position == initialGuardPosition) continue
        val mapForSimulation = labMapFrom(originalMap)
        mapForSimulation.squares[initialGuardPosition] =
            Square.Guard(direction = 0 to -1) // deep copy is too much hassle since LabMap and Guard has a lot of mutable factors
        mapForSimulation.squares[position] = Square.Blocked
        if (mapForSimulation.runSimulation() == MoveResult.InLoop) {
            loops++
        }
    }

    return loops
}

private fun LabMap.move(guardPosition: Pair<Int, Int>): MoveResult {
    val guard = squares[guardPosition] as Square.Guard

    var newGuardPosition = guardPosition + guard.direction

    var moved = false
    while (!moved) {
        val nextSquare = squares[newGuardPosition] ?: return MoveResult.LeftMap
        newGuardPosition = when (nextSquare) {
            Square.Blocked -> {
                val visitedObstacle = VisitedObstacle(
                    position = newGuardPosition,
                    approachedDirection = guard.direction
                )

                if (visitedObstacle in visitedObstacles) return MoveResult.InLoop
                visitedObstacles.add(visitedObstacle)
                guard.turn90()
                guardPosition + guard.direction
            }

            Square.Empty,
            Square.Moved -> newGuardPosition.also { moved = true }

            is Square.Guard -> error("Guard can't be in two places")
        }
    }

    squares[guardPosition] = Square.Moved
    squares[newGuardPosition] = guard

    return MoveResult.Moved
}

private fun LabMap.runSimulation(): MoveResult {
    var guardPosition = squares.keys.first { squares[it] is Square.Guard }
    val guard = squares[guardPosition] as Square.Guard

    var moved = move(guardPosition = guardPosition)
    guardPosition += guard.direction

    while (moved == MoveResult.Moved) {
        moved = move(guardPosition)
        guardPosition += guard.direction
        if (printMoves) {
            prettyPrint()
            println("------------------------------------------")
        }
    }

    return moved
}

private fun buildMap(lines: List<String>): LabMap {
    val rows = lines.size
    val columns = lines.first().length
    val squares = mutableMapOf<Pair<Int, Int>, Square>()

    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            val position = column to row

            squares[position] = when (char) {
                '.' -> Square.Empty
                '#' -> Square.Blocked
                '^' -> Square.Guard(direction = 0 to -1)
                '>' -> Square.Guard(direction = 1 to 0)
                'v' -> Square.Guard(direction = 0 to 1)
                '<' -> Square.Guard(direction = -1 to 0)
                'X' -> Square.Moved
                else -> error("Invalid map data")
            }
        }
    }

    return LabMap(
        rows = rows,
        columns = columns,
        squares = squares,
        visitedObstacles = mutableListOf()
    )
}

private fun Square.Guard.turn90() {
    direction = when (direction) {
        0 to -1 -> 1 to 0
        1 to 0 -> -0 to 1
        0 to 1 -> -1 to 0
        -1 to 0 -> 0 to -1
        else -> error("Invalid position")
    }
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return (first + other.first) to (second + other.second)
}

private fun labMapFrom(originalMap: LabMap) = originalMap.copy(
    rows = originalMap.rows,
    columns = originalMap.columns,
    squares = originalMap.squares.toMutableMap(),
    visitedObstacles = originalMap.visitedObstacles.toMutableList()
)

fun main() {
    println(solveDay61(fileName))
    println(solveDay62(fileName))
}