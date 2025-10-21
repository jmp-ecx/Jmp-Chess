package jmpngn.board

class Move(_move: Pair<Pair<Int, Int>, Pair<Int, Int>>) {
    // Format: 0bxxxxttttttssssss
    //  x - Flags
    //  t - Target idx
    //  s - Start idx
    private var move: UShort = 0u

    private val NullSpecial: ULong    = 0b0000u
    private val EnPassant: ULong      = 0b0001u
    private val KCastle: ULong        = 0b0010u
    private val QCastle: ULong        = 0b0011u

    constructor(moveString: String): this(StringParser.ParseMove(moveString)) {
    }

    init {
        val (start, target) = _move
        val (s_file, s_rank) = start
        val (t_file, t_rank) = target

        val s_idx = s_rank * BoardSize + s_file
        val t_idx = t_rank * BoardSize + t_file

        move = move or (NullSpecial shl 12).toUShort() // TODO - calculate special moves
        move = move or (t_idx.toULong() shl 6).toUShort()
        move = move or s_idx.toUShort()
    }

    fun GetStart():  Int = (move and 0b111111u).toInt()
    fun GetTarget(): Int = (move.toULong() and (0b111111.toULong() shl 6) shr 6).toInt()

    fun IsEnPassant():      Boolean = (move.toULong() and (0b1111.toULong() shl 12) shr 12) == 0b0100.toULong()
}