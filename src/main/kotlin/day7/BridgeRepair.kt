package day7

import util.readFileLines
import kotlin.math.pow

private const val fileName = "/day7.txt"

private data class Equation(
    val result: Long,
    val numbers: List<Long>
)

private enum class Operation {
    Add, Multiply, Concatenation
}

fun solveDay71(fileName: String): Long {
    val lines = readFileLines(fileName)
    val equations = lines.map { line ->
        val (result, numbers) = line.split(":")
        Equation(
            result = result.toLong(),
            numbers = numbers.split(" ").mapNotNull { it.toLongOrNull() }
        )
    }

    return equations
        .filter { it.canBeSolved(operationOptions = 2) }
        .sumOf { it.result }
}

// TODO optimize part two
fun solveDay72(fileName: String): Long {
    val lines = readFileLines(fileName)
    val equations = lines.map { line ->
        val (result, numbers) = line.split(":")
        Equation(
            result = result.toLong(),
            numbers = numbers.split(" ").mapNotNull { it.toLongOrNull() }
        )
    }

    return equations
        .filter { it.canBeSolved(operationOptions = 3) }
        .sumOf { it.result }
}

private fun Equation.canBeSolved(operationOptions: Int): Boolean {
    val operationsCombinations =
        generateOperationCombinations(operationsCount = numbers.size, operationOptions = operationOptions)

    return operationsCombinations.any { operations ->
        var operationIndex = 0
        val result = numbers.reduce { acc, l ->
            val operation = operations[operationIndex]
            operationIndex++
            when (operation) {
                Operation.Add -> acc + l
                Operation.Multiply -> acc * l
                Operation.Concatenation -> (acc.toString() + l.toString()).toLong()
            }
        }
        this.result == result
    }
}

/**
 * Generates all possible operations
 */
private fun generateOperationCombinations(operationsCount: Int, operationOptions: Int): List<List<Operation>> {
    val result = mutableListOf<List<Operation>>()
    repeat((operationOptions.toDouble().pow(operationsCount)).toInt()) {
        result.add(generateOperations(value = it, size = operationsCount, operationOptions = operationOptions))
    }
    return result
}

private fun generateOperations(value: Int, size: Int, operationOptions: Int): List<Operation> {
    val result = mutableListOf<Operation>()
    for (position in 0 until size) {
        result.add(value.toString(radix = operationOptions, minLength = size).getOperation(position))
    }
    return result
}

private fun String.getOperation(position: Int): Operation {
    return when (this[position]) {
        '0' -> Operation.Add
        '1' -> Operation.Multiply
        '2' -> Operation.Concatenation
        else -> error("Invalid operation")
    }
}

private fun Int.toString(radix: Int, minLength: Int): String {
    val converted = this.toString(radix = radix)
    val toBeFilled = minLength - converted.length
    return buildString {
        repeat(toBeFilled) {
            append("0")
        }
        append(converted)
    }
}

fun main() {
    println(solveDay71(fileName))
    println(solveDay72(fileName))
}