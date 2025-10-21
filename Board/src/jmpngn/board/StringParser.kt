package jmpngn.board

object StringParser {
    fun ParseSinglePosition(position: String): Pair<Int, Int> {
        val file: Int = when (position[0]) {
            'a' -> 0
            'b' -> 1
            'c' -> 2
            'd' -> 3
            'e' -> 4
            'f' -> 5
            'g' -> 6
            'h' -> 7
            else -> 0
        }
        var rank = try {
            position[1].toString().toInt() - 1
        } catch (e: NumberFormatException) {
            0
        }
        if (rank == 8) rank = 0

        return Pair(file, rank)
    }

    fun ParseMove(position: String): Pair<Pair<Int, Int>, Pair<Int, Int>> {
        val pos = position.chunked(2)
        return Pair(ParseSinglePosition(pos[0]), ParseSinglePosition(pos[1]))
    }
}
