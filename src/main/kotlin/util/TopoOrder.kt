package util

typealias Graph = Map<Int, List<Int>>

fun Graph.topologicalOrder(): List<Int> {
    val ordered = mutableListOf<Int>()

    val visited = mutableListOf<Int>()
    for (node in keys) {
        if (node !in visited) {
            val processed = mutableListOf<Int>()
            dfs(node = node, graph = this, visited = visited, processed = processed)
            for (processedElement in processed) {
                ordered.add(0, processedElement)
            }
        }
    }

    return ordered
}

fun dfs(node: Int, graph: Graph, visited: MutableList<Int>, processed: MutableList<Int>) {
    visited.add(node)

    val children = graph[node].orEmpty()
    for (childToVisit in children) {
        if (childToVisit !in visited) {
            dfs(
                node = childToVisit,
                graph = graph,
                visited = visited,
                processed = processed
            )
        }
    }
    processed.add(node)
}
