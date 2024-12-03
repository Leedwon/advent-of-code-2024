package day3

import util.readFileLines

private const val fileName = "/day3.txt"

fun solveDay31(fileName: String): Int {
    val memory = readFileLines(fileName).joinToString("")
    val regex = "mul\\(\\d*,\\d*\\)".toRegex()
    return regex.findAll(memory).sumOf { match ->
        match.value
            .drop(4)
            .dropLast(1)
            .split(",")
            .map { it.toInt() }
            .let { it[0] * it[1] }
    }
}

fun solveDay32(fileName: String): Int {
    val memory = readFileLines(fileName).joinToString("")
    val instructions = memory.readInstructions()

    var sum = 0
    var isOn = true
    for (instruction in instructions) {
        when (instruction) {
            Instruction.Do -> isOn = true
            Instruction.Dont -> isOn = false
            is Instruction.Multiply -> if (isOn) {
                sum += instruction.a * instruction.b
            } else {
                // no-op
            }
        }
    }

    return sum
}

private fun String.readInstructions(): List<Instruction> {
    val instructions = mutableListOf<Instruction>()

    var runningDo = ""
    var runningDont = ""
    var runningMul = ""

    forEach { char ->
        val newDo = runningDo + char

        runningDo = when {
            newDo == "do()" -> "".also { instructions.add(Instruction.Do) }
            "do()".contains(newDo) -> newDo
            else -> char.toString()
        }

        val newDont = runningDont + char
        runningDont = when {
            newDont == "don't()" -> "".also { instructions.add(Instruction.Dont) }
            "don't()".contains(newDont) -> newDont
            else -> char.toString()
        }

        val newMul = runningMul + char
        val isMulValid = isMulValid(currentMul = runningMul, newMul = newMul)
        runningMul = when {
            isMulValid && char == ')' -> "".also { instructions.add(newMul.toMultiply()) }
            isMulValid -> newMul
            else -> char.toString()
        }
    }

    return instructions
}

private fun isMulValid(currentMul: String, newMul: String): Boolean {
    val lastChar = currentMul.lastOrNull()
    val newChar = newMul.last()

    return when {
        currentMul == "" -> newChar == 'm'
        currentMul == "m" -> newChar == 'u'
        currentMul == "mu" -> newChar == 'l'
        currentMul == "mul" -> newChar == '('
        currentMul == "mul(" -> newChar.isDigit()
        currentMul.contains("mul(") -> when {
            lastChar == ',' -> newChar.isDigit()
            !currentMul.contains(",") -> newChar.isDigit() || newChar == ','
            currentMul.contains(",") && lastChar!!.isDigit() -> newChar.isDigit() || newChar == ')'
            else -> false
        }

        else -> false
    }
}

private fun String.toMultiply(): Instruction.Multiply {
    return this
        .drop(4)
        .dropLast(1)
        .split(",")
        .map { it.toInt() }
        .let {
            Instruction.Multiply(
                a = it[0],
                b = it[1]
            )
        }
}

sealed interface Instruction {
    data class Multiply(val a: Int, val b: Int) : Instruction
    data object Do : Instruction
    data object Dont : Instruction
}

fun main() {
    println(solveDay31(fileName))
    println(solveDay32(fileName))
}
