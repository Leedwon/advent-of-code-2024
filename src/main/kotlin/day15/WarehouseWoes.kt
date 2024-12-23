package day15

import util.readFileLines

private const val fileName = "/day15.txt"

data class Data(
    val map: Map<Pair<Int, Int>, Char>,
    val moves: List<Direction>
)

enum class Direction(val value: Pair<Int, Int>) {
    North(0 to -1),
    South(0 to 1),
    West(-1 to 0),
    East(1 to 0)
}

private operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>): Pair<Int, Int> {
    return (first + other.first) to (second + other.second)
}

fun solveDay151(fileName: String): Int {
    val data = parseData(readFileLines(fileName))

    val map = data.map.toMutableMap()
    val moves = data.moves

    var robotPosition = map.keys.first { map[it] == '@' }
    for (move in moves) {
        val positionsToMove = mutableListOf<Pair<Int, Int>>()

        if (
            canMove(
                position = robotPosition,
                move = move,
                map = map,
                positionsToMove = positionsToMove
            )
        ) {
            robotPosition += move.value
            positionsToMove.reversed().forEach { moveTo ->
                val current = map[moveTo]!!
                map[moveTo + move.value] = current
            }
            map[positionsToMove[0]] = '.'
        }
    }

    var sum = 0
    map.forEach { (position, value) ->
        if (value == 'O') {
            sum += 100 * position.second + position.first
        }
    }
    return sum
}

fun solveDay152(fileName: String): Int {
    val data = parseData(lines = readFileLines(fileName), wide = true)

    val map = data.map.toMutableMap()
    val moves = data.moves

    var robotPosition = map.keys.first { map[it] == '@' }
    for (move in moves) {
        val positionsToMove = mutableSetOf<Pair<Int, Int>>()

        if (
            canMoveWide(
                touchPositions = listOf(robotPosition),
                move = move,
                map = map,
                positionsToMove = positionsToMove
            )
        ) {
            robotPosition += move.value

            val sorted = when (move) {
                Direction.North -> positionsToMove.sortedBy { it.second }
                Direction.South -> positionsToMove.sortedByDescending { it.second }
                Direction.West -> positionsToMove.sortedBy { it.first }
                Direction.East -> positionsToMove.sortedByDescending { it.first }
            }

            sorted.forEach { moveTo ->
                val current = map[moveTo]!!
                map[moveTo + move.value] = current
                map[moveTo] = '.'
            }
        }
    }

    var sum = 0
    map.forEach { (position, value) ->
        if (value == '[') {
            sum += 100 * position.second + position.first
        }
    }
    return sum
}

private fun canMove(
    position: Pair<Int, Int>,
    move: Direction,
    map: Map<Pair<Int, Int>, Char>,
    positionsToMove: MutableList<Pair<Int, Int>>
): Boolean {
    val nextPosition = position + move.value
    val next = map[nextPosition]

    return when (next) {
        '.' -> true.also { positionsToMove.add(position) }
        'O' -> canMove(
            position = nextPosition,
            move = move,
            map = map,
            positionsToMove = positionsToMove.apply { add(position) }
        )

        else -> false
    }
}

private fun canMoveWide(
    touchPositions: List<Pair<Int, Int>>,
    move: Direction,
    map: Map<Pair<Int, Int>, Char>,
    positionsToMove: MutableSet<Pair<Int, Int>>
): Boolean {
    return when (move) {
        Direction.North,
        Direction.South -> {
            touchPositions.all { position ->
                val nextPosition = position + move.value
                val next = map[nextPosition]

                when (next) {
                    '.' -> true.also { positionsToMove.add(position) }
                    ']' -> canMoveWide(
                        touchPositions = listOf(nextPosition, nextPosition + Direction.West.value),
                        move = move,
                        map = map,
                        positionsToMove = positionsToMove.apply { add(position) }
                    )

                    '[' -> canMoveWide(
                        touchPositions = listOf(nextPosition, nextPosition + Direction.East.value),
                        move = move,
                        map = map,
                        positionsToMove = positionsToMove.apply { add(position) }
                    )

                    else -> false
                }
            }
        }

        Direction.West,
        Direction.East -> {
            val position = touchPositions.first()
            val nextPosition = position + move.value
            val next = map[nextPosition]

            when (next) {
                '.' -> true.also { positionsToMove.add(position) }
                ']', '[' -> canMoveWide(
                    touchPositions = listOf(nextPosition),
                    move = move,
                    map = map,
                    positionsToMove = positionsToMove.apply { add(position) }
                )

                else -> false
            }
        }
    }
}

private fun parseData(lines: List<String>, wide: Boolean = false): Data {
    val splitAt = lines.indexOfFirst { it.isEmpty() }

    val map = if (wide) buildWideMap(lines.subList(0, splitAt)) else buildMap(lines.subList(0, splitAt))
    val moves = parseMoves(lines.subList(splitAt + 1, lines.size))

    return Data(
        map = map,
        moves = moves
    )
}

private fun buildMap(lines: List<String>): Map<Pair<Int, Int>, Char> {
    val map = mutableMapOf<Pair<Int, Int>, Char>()

    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            map[column to row] = char
        }
    }

    return map
}

private fun buildWideMap(lines: List<String>): Map<Pair<Int, Int>, Char> {
    val map = mutableMapOf<Pair<Int, Int>, Char>()

    lines.forEachIndexed { row, line ->
        line.forEachIndexed { column, char ->
            val x = column * 2

            when (char) {
                '#' -> {
                    map[x to row] = '#'
                    map[x + 1 to row] = '#'
                }

                'O' -> {
                    map[x to row] = '['
                    map[x + 1 to row] = ']'
                }

                '.' -> {
                    map[x to row] = '.'
                    map[x + 1 to row] = '.'
                }

                '@' -> {
                    map[x to row] = '@'
                    map[x + 1 to row] = '.'
                }

                else -> error("Invalid char $char")
            }
        }
    }

    return map
}

private fun debugPrint(
    map: Map<Pair<Int, Int>, Char>,
    rows: Int = 8,
    columns: Int = 8
) {
    for (row in 0 until rows) {
        for (column in 0 until columns) {
            print(map[column to row])
        }
        print("\n")
    }
    repeat(columns) {
        print("-")
    }
    print("\n")
}

private fun parseMoves(lines: List<String>): List<Direction> =
    lines
        .map { line ->
            line.map {
                when (it) {
                    '^' -> Direction.North
                    '>' -> Direction.East
                    '<' -> Direction.West
                    'v' -> Direction.South
                    else -> error("Invalid direction")
                }
            }
        }
        .flatten()

fun main() {
    println(solveDay151(fileName))
    println(solveDay152(fileName))
}
