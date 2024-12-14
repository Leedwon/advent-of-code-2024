package day11

import util.readFileLines

private const val fileName = "/day11.txt"

fun solveDay111(fileName: String): Long {
    val stones = readFileLines(fileName)
        .first()
        .split(" ")
        .map { it.toLong() }

    var runningStones = stones.associateWith { 1L }

    repeat(25) {
        runningStones = runSimulation(stones = runningStones)
    }

    return runningStones.values.sum()
}

fun solveDay112(fileName: String): Long {
    val stones = readFileLines(fileName)
        .first()
        .split(" ")
        .map { it.toLong() }

    var runningStones = stones.associateWith { 1L }

    repeat(75) {
        runningStones = runSimulation(stones = runningStones)
    }

    return runningStones.values.sum()
}

private fun runSimulation(stones: List<Long>, round: Int, stopRound: Int): List<Long> {
    if (round == stopRound) return stones

    return runSimulation(
        stones = stones.flatMap { it.nextStones() },
        round = round + 1,
        stopRound = stopRound
    )
}

private fun runSimulation(stones: List<Long>): List<Long> {
    val newStones = mutableListOf<Long>()

    stones.forEach { stone ->
        newStones.addAll(stone.nextStones())
    }

    return newStones
}

private fun runSimulation(stones: Map<Long, Long>): Map<Long, Long> {
    val newStones = mutableMapOf<Long, Long>()

    stones.forEach { (stone, count) ->
        val nextStones = stone.nextStones()

        nextStones.forEach { nextStone ->
            newStones[nextStone] = newStones.getOrElse(nextStone) { 0 } + count
        }
    }

    return newStones
}

private fun Long.nextStones(): List<Long> {
    return when {
        this == 0L -> listOf(1L)
        this.toString().length % 2 == 0 -> {
            val strStone = this.toString()
            val halfIndex = strStone.length / 2
            val firstHalf = strStone.substring(0, halfIndex).toLong()
            val secondHalf = strStone.substring(halfIndex, strStone.length).toLong()
            listOf(firstHalf, secondHalf)
        }

        else -> listOf(this * 2024)
    }
}

fun main() {
    println(solveDay111(fileName))
    println(solveDay112(fileName))
}
