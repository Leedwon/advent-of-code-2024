package day5

import util.readFileLines

private const val fileName = "/day5.txt"

private data class Rule(
    val before: Int,
    val after: Int
)

private infix fun Int.before(other: Int): Rule = Rule(before = this, after = other)

private typealias Update = List<Int>

private data class Data(
    val rules: List<Rule>,
    val updates: List<Update>
)

fun solveDay51(fileName: String): Int {
    val lines = readFileLines(fileName)
    val data = parseData(lines)

    return data.updates
        .filter { it.isValid(rules = data.rules) }
        .sumOf {
            val index = it.size / 2
            it[index]
        }
}

fun solveDay52(fileName: String): Int {
    val lines = readFileLines(fileName)
    val data = parseData(lines)

    return data.updates
        .filter { !it.isValid(rules = data.rules) }
        .map { it.fix(rules = data.rules) }
        .sumOf {
            val index = it.size / 2
            it[index]
        }
}

private fun parseData(lines: List<String>): Data {
    val separatorIndex = lines.indexOf("")
    val rules = lines.subList(0, separatorIndex)
    val updates = lines.subList(separatorIndex + 1, lines.size)

    return Data(
        rules = parseRules(rules),
        updates = parseUpdates(updates)
    )
}

private fun parseRules(input: List<String>): List<Rule> {
    return input.map { line ->
        val (before, after) = line.split("|")
        before.toInt() before after.toInt()
    }
}

private fun parseUpdates(input: List<String>): List<Update> {
    return input.map { line ->
        line.split(",").map { it.toInt() }
    }
}

private fun Update.isValid(rules: List<Rule>): Boolean {
    for (i in 0 until lastIndex) {
        for (j in i + 1..lastIndex) {
            val brokenRule = Rule(before = this[j], after = this[i])
            if (brokenRule in rules) return false
        }
    }
    return true
}

private fun Update.fix(rules: List<Rule>): Update {
    return sortedWith { o1, o2 ->
        val rule = Rule(before = o1, after = o2)
        if (rule in rules) -1 else 1
    }
}

fun main() {
    println(solveDay51(fileName))
    println(solveDay52(fileName))
}