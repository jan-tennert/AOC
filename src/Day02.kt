const val MAX_RED = 12
const val MAX_GREEN = 13
const val MAX_BLUE = 14

fun main() {
    fun part1(input: List<String>): Int {
        val games = Game.fromData(input)
        val filteredGames = games.filter {
            it.sets.all { set ->
                val groupedSets = set.groupColors()
                (groupedSets["red"] ?: 0) <= MAX_RED && (groupedSets["green"] ?: 0) <= MAX_GREEN && (groupedSets["blue"] ?: 0) <= MAX_BLUE
            }
        }
        return filteredGames.sumOf { it.id }
    }

    fun part2(input: List<String>): Int {
        val games = Game.fromData(input)
        val power = games.map { game ->
            var minRed = 0
            var minGreen = 0
            var minBlue = 0
            game.sets.forEach { set ->
                val groupedSets = set.groupColors()
                if ((groupedSets["red"] ?: 0) > minRed) {
                    minRed = groupedSets["red"]!!
                }
                if ((groupedSets["green"] ?: 0) > minGreen) {
                    minGreen = groupedSets["green"]!!
                }
                if ((groupedSets["blue"] ?: 0) > minBlue) {
                    minBlue = groupedSets["blue"]!!
                }
            }
            minRed * minGreen * minBlue
        }
        return power.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part2(testInput) == 2286)

    val input = readInput("day02")
    part1(input).println()
    part2(input).println()
}

private data class Game(val id: Int, val sets: List<GameSet>) {

    companion object {

        fun fromData(input: List<String>): List<Game> {
            return input.map {
                val parts = it.split(":")
                val id = parts.first().replace("Game ", "").toInt()
                val sets = parts[1].drop(1).split("; ").map { c->
                    val cubes = c.split(", ")
                    GameSet(cubes.map { cube ->
                        val cubeParts = cube.split(" ")
                        Cube(cubeParts[1], cubeParts[0].toInt())
                    })
                }
                Game(id, sets)
            }
        }

    }

}

private data class GameSet(val cubes: List<Cube>) {

    fun groupColors(): Map<String, Int> {
        return cubes.groupBy { cube -> cube.color }.map { (color, cubes) -> color to cubes.sumOf { cube -> cube.count } }.toMap()
    }

}

private data class Cube(val color: String, val count: Int)