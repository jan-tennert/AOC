fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input.map { line ->
            val newList = line.filter { it.isDigit() }
            (newList.first().toString() + newList.last().toString()).toInt()
        }
        return numbers.sum()
    }

    fun part2(input: List<String>): Int {
        val numbers = input.map { line ->
            val newList = line.filterIsDigitOrSpelledNumber()
            (newList.first().toString() + newList.last().toString()).toInt()
        }
        return numbers.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part2(testInput) == 281)

    val input = readInput("day01")
    part1(input).println()
    part2(input).println()
}

private val NUMBERS = listOf(
    "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
)

private fun String.filterIsDigitOrSpelledNumber(): List<Int> {
    var tempS = ""
    return buildList {
        for (char in this@filterIsDigitOrSpelledNumber) {
            if(char.isDigit()) {
                add(char.digitToInt())
                continue
            }
            if(NUMBERS.any { n -> n.contains(char) }) {
                tempS += char
            } else {
                tempS = ""
            }
            NUMBERS.forEachIndexed { index, s ->
                if(s == tempS) {
                    add(index + 1)
                    tempS = ""
                }
            }
            while(NUMBERS.none { it.contains(tempS) }) {
                tempS = tempS.drop(1)
            }
        }
    }
}