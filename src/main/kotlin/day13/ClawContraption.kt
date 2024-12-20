package day13

import util.readFileLines

private const val fileName = "/day13.txt"

private data class ClawMachine(
    val aButton: Button,
    val bButton: Button,
    val prize: Prize
)

private data class Button(
    val xShift: Long,
    val yShift: Long
)

private data class Prize(
    val x: Long,
    val y: Long
)

fun solveDay131(fileName: String): Long {
    val clawMachines = parse(readFileLines(fileName))

    return clawMachines.sumOf { it.tokensToWin() }
}

fun solveDay132(fileName: String): Long {
    val clawMachines = parse(lines = readFileLines(fileName), positionShift = 10_000_000_000_000L)

    return clawMachines.sumOf { it.tokensToWin() }
}

private fun parse(lines: List<String>, positionShift: Long = 0): List<ClawMachine> {
    return lines
        .filter { it.isNotBlank() }
        .chunked(3)
        .map {
            createClawMachine(
                aButton = it[0],
                bButton = it[1],
                prize = it[2],
                positionShift = positionShift
            )
        }
}

/**
 * @return tokens required to win or 0 if you can't win
 */
private fun ClawMachine.bruteForceTokensToWinWithPressLimit(): Long {
    val maxButtonPresses = 100
    val aPressCost = 3L
    val bPressCost = 1L

    val costs = mutableSetOf<Long>()

    for (aPressTimes in 0..maxButtonPresses) {
        for (bPressTime in 0..maxButtonPresses) {
            val x = aPressTimes * aButton.xShift + bPressTime * bButton.xShift
            val y = aPressTimes * aButton.yShift + bPressTime * bButton.yShift
            val hasWon = x == prize.x && y == prize.y
            if (hasWon) {
                val cost = aPressTimes * aPressCost + bPressTime * bPressCost
                costs.add(cost)
            }
        }
    }

    return costs.minOrNull() ?: 0
}

/**
 * @return tokens required to win or 0 if you can't win
 */
private fun ClawMachine.tokensToWin(): Long {
    val aPressCost = 3L
    val bPressCost = 1L

    val pressedBTimes =
        ((prize.y * aButton.xShift) - prize.x * aButton.yShift) / ((bButton.yShift * aButton.xShift) - (aButton.yShift * bButton.xShift))
    val pressedATimes = (prize.x - pressedBTimes * bButton.xShift) / aButton.xShift

    val x = pressedATimes * aButton.xShift + pressedBTimes * bButton.xShift
    val y = pressedATimes * aButton.yShift + pressedBTimes * bButton.yShift

    return if (x == prize.x && y == prize.y) {
        pressedATimes * aPressCost + pressedBTimes * bPressCost
    } else {
        0
    }
}

private fun createClawMachine(
    aButton: String,
    bButton: String,
    prize: String,
    positionShift: Long
): ClawMachine = ClawMachine(
    aButton = createButton(aButton),
    bButton = createButton(bButton),
    prize = createPrize(prize).let { it.copy(x = it.x + positionShift, y = it.y + positionShift) }
)

private fun createButton(input: String): Button {
    val (x, y) = input
        .split(":")
        .last()
        .trim()
        .split(",")
        .map {
            it
                .trim()
                .split("+")
                .last()
                .toLong()
        }

    return Button(
        xShift = x,
        yShift = y
    )
}

private fun createPrize(input: String): Prize {
    val (x, y) = input
        .split(":")
        .last()
        .trim()
        .split(",")
        .map {
            it
                .trim()
                .split("=")
                .last()
                .toLong()
        }

    return Prize(
        x = x,
        y = y
    )
}

fun main() {
    println(solveDay131(fileName))
    println(solveDay132(fileName))
}