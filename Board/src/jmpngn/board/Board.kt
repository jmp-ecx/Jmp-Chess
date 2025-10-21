package jmpngn.board

const val BoardSize = 8

enum class PieceType(val char: String) {
    None(" "),
    Pawn("P"),
    Bishop("B"),
    Knight("N"),
    Rook("R"),
    Queen("Q"),
    King("K"),
}

data class Piece(val type: PieceType, val white: Boolean)

// TODO - Move legality checker

class Board {

    companion object {
        private const val PawnOffset   = 0
        private const val BishopOffset = 1
        private const val KnightOffset = 2
        private const val RookOffset   = 3
        private const val QueenOffset  = 4
        private const val KingOffset   = 5
    }

    private var State = mutableListOf( // TODO - FEN Notation loading
        0b00000000_00000000_00000000_00000000_00000000_00000000_11111111_00000000u, // White Pawn
        0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00100100u, // White Bishop
        0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_01000010u, // White Knight
        0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_10000001u, // White Rook
        0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00010000u, // White Queen
        0b00000000_00000000_00000000_00000000_00000000_00000000_00000000_00001000u, // White King
        0b00000000_11111111_00000000_00000000_00000000_00000000_00000000_00000000u, // Black Pawn
        0b00100100_00000000_00000000_00000000_00000000_00000000_00000000_00000000u, // Black Bishop
        0b01000010_00000000_00000000_00000000_00000000_00000000_00000000_00000000u, // Black Knight
        0b10000001_00000000_00000000_00000000_00000000_00000000_00000000_00000000u, // Black Rook
        0b00010000_00000000_00000000_00000000_00000000_00000000_00000000_00000000u, // Black Queen
        0b00001000_00000000_00000000_00000000_00000000_00000000_00000000_00000000u, // Black King
    )
    private var PrevState = State

    var WhiteToMove = true
        private set

    fun LoadFEN(fen: String) {
        val data = fen.split(' ')
        val ranks = data[0].split('/')
        var offset = 63

        for (i in 0..<State.size) { State[i] = 0u }

        for (rank in ranks) {
            for (char in rank) {
                if (char.isDigit()) {
                    offset -= char.toString().toInt()
                    continue
                }

                val col_offset = if (char.isUpperCase()) 0 else 6

                when (char.lowercaseChar()) {
                    'p' -> State[col_offset + PawnOffset]   = State[col_offset + PawnOffset].SetBit(offset)
                    'b' -> State[col_offset + BishopOffset] = State[col_offset + BishopOffset].SetBit(offset)
                    'n' -> State[col_offset + KnightOffset] = State[col_offset + KnightOffset].SetBit(offset)
                    'r' -> State[col_offset + RookOffset]   = State[col_offset + RookOffset].SetBit(offset)
                    'q' -> State[col_offset + QueenOffset]  = State[col_offset + QueenOffset].SetBit(offset)
                    'k' -> State[col_offset + KingOffset]   = State[col_offset + KingOffset].SetBit(offset)
                }

                offset -= 1
            }
        }

        WhiteToMove = data[1] == "w"
    }

    fun GetSquare(idx: Int): Piece? {
        if (!FullBoard().GetBit(idx)) return null

        val col_offset = if (WhiteBoard().GetBit(idx)) 0 else 6

        if (State[col_offset + PawnOffset].GetBit(idx))   return Piece(PieceType.Pawn,   col_offset == 0)
        if (State[col_offset + BishopOffset].GetBit(idx)) return Piece(PieceType.Bishop, col_offset == 0)
        if (State[col_offset + KnightOffset].GetBit(idx)) return Piece(PieceType.Knight, col_offset == 0)
        if (State[col_offset + RookOffset].GetBit(idx))   return Piece(PieceType.Rook,   col_offset == 0)
        if (State[col_offset + QueenOffset].GetBit(idx))  return Piece(PieceType.Queen,  col_offset == 0)
        if (State[col_offset + KingOffset].GetBit(idx))   return Piece(PieceType.King,   col_offset == 0)

        return null
    }

    fun GetSquare(file: Int, rank: Int): Piece? {
        val idx = rank * BoardSize + file

        return GetSquare(idx)
    }

    fun GetSquare(pos: String): Piece? {
        val (file, rank) = StringParser.ParseSinglePosition(pos)
        return GetSquare(file, rank)
    }

    fun IsPiece(idx: Int, typ: PieceType): Boolean {
        return when (typ) {
            PieceType.None   -> false
            PieceType.Pawn   -> State[PawnOffset].GetBit(idx)   || State[6 + PawnOffset].GetBit(idx)
            PieceType.Bishop -> State[BishopOffset].GetBit(idx) || State[6 + BishopOffset].GetBit(idx)
            PieceType.Knight -> State[KnightOffset].GetBit(idx) || State[6 + KnightOffset].GetBit(idx)
            PieceType.Rook   -> State[RookOffset].GetBit(idx)   || State[6 + RookOffset].GetBit(idx)
            PieceType.Queen  -> State[QueenOffset].GetBit(idx)  || State[6 + QueenOffset].GetBit(idx)
            PieceType.King   -> State[KingOffset].GetBit(idx)   || State[6 + KingOffset].GetBit(idx)
        }
    }

    fun IsPiece(file: Int, rank: Int, piece: PieceType): Boolean {
        val idx = rank * BoardSize + file
        return IsPiece(idx, piece)
    }

    fun Move(move: Move): Boolean { // TODO - capturing
        // Get the piece, target, and their indices in the bitboards.
        val piece  = GetSquare(move.GetStart()) ?: return false
        val target = GetSquare(move.GetTarget())

        val startIdx = move.GetStart()
        val endIdx = move.GetTarget()

        if (piece.white != WhiteToMove) return false // Make sure it's the correct player's turn
        if (target != null && target.white == piece.white) return false // Make sure the target and piece are different colors

        PrevState = State.toMutableList()

        // Calculate bitboard indices for moving and target pieces
        val p_col_offset = if (piece.white) 0 else 6
        val t_col_offset = if (target == null || target.white) 0 else 6

        val t_type_offset = when (target?.type) {
            PieceType.None   -> 0
            PieceType.Pawn   -> PawnOffset
            PieceType.Bishop -> BishopOffset
            PieceType.Knight -> KnightOffset
            PieceType.Rook   -> RookOffset
            PieceType.Queen  -> QueenOffset
            PieceType.King   -> KingOffset
            null             -> 0
        }

        // Set / Unset pieces on the bitboards
        when (piece.type) {
            PieceType.None -> return false
            PieceType.Pawn -> {
                if (!MoveValidator.ValidatePawnMove(this, move)) return false
                State[p_col_offset + PawnOffset] = State[p_col_offset + PawnOffset].ZeroBit(startIdx)
                State[p_col_offset + PawnOffset] = State[p_col_offset + PawnOffset].SetBit(endIdx)

                if (t_col_offset != p_col_offset)
                    State[t_col_offset + t_type_offset] = State[t_col_offset + t_type_offset].ZeroBit(endIdx)
            }
            PieceType.Bishop -> {
                State[p_col_offset + BishopOffset] = State[p_col_offset + BishopOffset].ZeroBit(startIdx)
                State[p_col_offset + BishopOffset] = State[p_col_offset + BishopOffset].SetBit(endIdx)

                if (t_col_offset != p_col_offset)
                    State[t_col_offset + t_type_offset] = State[t_col_offset + t_type_offset].ZeroBit(endIdx)
            }
            PieceType.Knight -> {
                State[p_col_offset + KnightOffset] = State[p_col_offset + KnightOffset].ZeroBit(startIdx)
                State[p_col_offset + KnightOffset] = State[p_col_offset + KnightOffset].SetBit(endIdx)

                if (t_col_offset != p_col_offset)
                    State[t_col_offset + t_type_offset] = State[t_col_offset + t_type_offset].ZeroBit(endIdx)
            }
            PieceType.Rook -> {
                State[p_col_offset + RookOffset] = State[p_col_offset + RookOffset].ZeroBit(startIdx)
                State[p_col_offset + RookOffset] = State[p_col_offset + RookOffset].SetBit(endIdx)

                if (t_col_offset != p_col_offset)
                    State[t_col_offset + t_type_offset] = State[t_col_offset + t_type_offset].ZeroBit(endIdx)
            }
            PieceType.Queen -> {
                State[p_col_offset + QueenOffset] = State[p_col_offset + QueenOffset].ZeroBit(startIdx)
                State[p_col_offset + QueenOffset] = State[p_col_offset + QueenOffset].SetBit(endIdx)

                if (t_col_offset != p_col_offset)
                    State[t_col_offset + t_type_offset] = State[t_col_offset + t_type_offset].ZeroBit(endIdx)
            }
            PieceType.King -> {
                State[p_col_offset + KingOffset] = State[p_col_offset + KingOffset].ZeroBit(startIdx)
                State[p_col_offset + KingOffset] = State[p_col_offset + KingOffset].SetBit(endIdx)

                if (t_col_offset != p_col_offset)
                    State[t_col_offset + t_type_offset] = State[t_col_offset + t_type_offset].ZeroBit(endIdx)
            }
        }

        WhiteToMove = !WhiteToMove
        return true
    }

    fun UndoMove() { State = PrevState.toMutableList() }
    private fun FullBoard(): ULong  = State.reduce { acc, value -> acc or value }
    private fun WhiteBoard(): ULong = State.subList(0, 6).reduce { acc, value -> acc or value}
    private fun BlackBoard(): ULong = State.subList(6, 12).reduce { acc, value -> acc or value}
}

private fun ULong.GetBit(idx: Int):  Boolean = ((this shr idx) and 0x1u) != 0.toULong()
private fun ULong.ZeroBit(idx: Int): ULong   = this and (1.toULong() shl idx).inv()
private fun ULong.SetBit(idx: Int):  ULong   = this or  (1.toULong() shl idx)

