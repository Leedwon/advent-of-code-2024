package day9

import util.readFileLines

private const val fileName = "/day9.txt"

data class File(
    val length: Int,
    var remainingLength: Int,
    val id: Int
)

fun solveDay91(fileName: String): Long {
    val diskMap = readFileLines(fileName).first().map { it.toString().toInt() }
    val files = diskMap
        .filterIndexed { index, _ -> index % 2 == 0 }
        .mapIndexed { index, i ->
            File(
                length = i,
                remainingLength = i,
                id = index
            )
        }
    val freeSpaces = diskMap.filterIndexed { index, _ -> index % 2 == 1 }

    val rearranged = mutableListOf<Int>()
    val filesQueue = ArrayDeque(files)
    val freeSpacesQueue = ArrayDeque(freeSpaces)

    var fillingFile = filesQueue.removeLast()

    while (filesQueue.isNotEmpty()) {
        val file = filesQueue.removeFirst()
        rearranged.addAll(List(file.length) { file.id })
        file.remainingLength = 0

        var gapLength = freeSpacesQueue.removeFirst()
        while (gapLength != 0) {
            if (fillingFile.remainingLength == 0) {
                fillingFile = filesQueue.removeLast()
            }

            if (gapLength >= fillingFile.remainingLength) {
                rearranged.addAll(List(fillingFile.remainingLength) { fillingFile.id })
                gapLength -= fillingFile.remainingLength
                fillingFile.remainingLength = 0
            } else {
                rearranged.addAll(List(gapLength) { fillingFile.id })
                fillingFile.remainingLength -= gapLength
                gapLength = 0
            }
        }
    }

    rearranged.addAll(List(fillingFile.remainingLength) { fillingFile.id })

    val filesTotalLength = files.sumOf { it.length }
    val actualLength = rearranged.size

    println(filesTotalLength)
    println(actualLength)

    check(filesTotalLength == actualLength)

    return rearranged.mapIndexed { index, value -> value.toLong() * index }.sum()
}

fun solveDay92(fileName: String): Long {
    val diskMap = readFileLines(fileName).first().map { it.toString().toInt() }
    val files = diskMap
        .filterIndexed { index, _ -> index % 2 == 0 }
        .mapIndexed { index, i ->
            File(
                length = i,
                remainingLength = i,
                id = index
            )
        }
    val gaps = diskMap.filterIndexed { index, _ -> index % 2 == 1 }.toMutableList()

    val rearranged = mutableListOf<Int>()

    val movedFiles = mutableMapOf<Int, List<File>>()

    for (fileIndex in files.lastIndex downTo 0) {
        val file = files[fileIndex]

        // don't check all gaps, just the ones before file otherwise we could move the file to the right which we don't want
        for (gapIndex in 0 until fileIndex) {
            val gap = gaps[gapIndex]
            if (file.length <= gap) {
                gaps[gapIndex] -= file.length
                println("\uD83D\uDEB6 moving file ${file.id} to gap at $gapIndex length left = ${gaps[gapIndex]}")
                movedFiles[gapIndex] = movedFiles[gapIndex].orEmpty() + file
                break
            }
        }
    }

    val processedFiles = mutableListOf<File>()
    var index = 0
    while (processedFiles.size < files.size) {
        val fileBeforeGap = files[index]

        val wasMoved = fileBeforeGap in processedFiles
        if (wasMoved) {
            rearranged.addAll(List(fileBeforeGap.length) { -1 })
        }
        val filesAtGap = movedFiles[index].orEmpty()
        val allFiles = if (wasMoved) filesAtGap else listOf(fileBeforeGap) + filesAtGap
        for (file in allFiles) {
            rearranged.addAll(List(file.length) { file.id })
            processedFiles.add(file)
        }
        val gap = gaps[index]

        rearranged.addAll(List(gap) { -1 })

        index++
    }

    println(rearranged)
    println("gaps length = ${gaps.size}")

    return rearranged
        .mapIndexed { index, value -> if (value == -1) 0 else value.toLong() * index }
        .sum()
}

fun main() {
    println(solveDay91(fileName))
    println(solveDay92(fileName))
}
