private data class Index(val index1: Int, val index2: Int)

private data class Ratio(val count: Int, val ratio: Int)

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        val grid = input.map { it.split("").filter { s -> s.isNotBlank() }.map { s -> s.first() } }
        for ((index1, row) in grid.withIndex()) {
            val ignoreIndices = mutableListOf<Int>()
            for ((index2, cell) in row.withIndex()) {
                if (!cell.isDigit() || ignoreIndices.contains(index2)) {
                    continue
                }
                var number = cell.toString()
                var numIndex = index2
                while(row.size > (numIndex + 1) && row[numIndex + 1].isDigit()) {
                    number += row[numIndex + 1]
                    ignoreIndices.add(numIndex + 1)
                    numIndex++
                }
                val indexRange = index2..numIndex
                if(checkIfAdjacent(grid, index1, indexRange)) {
                    sum += number.toInt()
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.split("").filter { s -> s.isNotBlank() }.map { s -> s.first() } }
        val ratios = mutableMapOf<Index, Ratio>()
        for ((index1, row) in grid.withIndex()) {
            val ignoreIndices = mutableListOf<Int>()
            for ((index2, cell) in row.withIndex()) {
                if (!cell.isDigit() || ignoreIndices.contains(index2)) {
                    continue
                }
                var number = cell.toString()
                var numIndex = index2
                while(row.size > (numIndex + 1) && row[numIndex + 1].isDigit()) {
                    number += row[numIndex + 1]
                    ignoreIndices.add(numIndex + 1)
                    numIndex++
                }
                val indexRange = index2..numIndex
                checkIfAdjacentRatio(grid, number.toInt(), index1, indexRange, ratios)
            }
        }
        return ratios.toList().filter { it.second.count > 1 }.sumOf { it.second.ratio }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part2(testInput) == 467835)

    val input = readInput("day03")
    part1(input).println()
    part2(input).println()
}

private fun checkIfAdjacent(
    grid: List<List<Char>>,
    col: Int,
    range: IntRange,
): Boolean {
    for(index in range) {
        for(num1 in col - 1..col + 1) {
            for(num2 in index - 1..index+1) {
                if(grid.safeCheck(num1, num2)) {
                    return true
                }
            }
        }
    }
    return false
}

private fun checkIfAdjacentRatio(
    grid: List<List<Char>>,
    num: Int,
    col: Int,
    range: IntRange,
    ratios: MutableMap<Index, Ratio>
) {
    for(index in range) {
        for(num1 in col - 1..col + 1) {
            for(num2 in index - 1..index+1) {
                if(grid.safeCheck(num1, num2)) {
                    val isStar = grid[num1][num2] == '*'
                    if(isStar) {
                        val oldVal = ratios[Index(num1, num2)] ?: Ratio(0, 1)
                        ratios[Index(num1, num2)] = Ratio(oldVal.count + 1, oldVal.ratio * num)
                        return
                    }
                }
            }
        }
    }
}

private fun Char.isNotDigitOrDot() = !this.isDigit() && this != '.'

private fun List<List<Char>>.safeCheck(index1: Int, index2: Int) = if(
    index1 > 0 && index2 > 0 && size > index1 && get(index1).size > index2
) get(index1).get(index2).isNotDigitOrDot() else false