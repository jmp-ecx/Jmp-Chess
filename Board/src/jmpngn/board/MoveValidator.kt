package jmpngn.board

import kotlin.math.abs

object MoveValidator {

    fun ValidatePawnMove(board: Board, move: Move): Boolean {
        val fwd_offset = if (board.WhiteToMove) 8 else -8
        val start_offset = if (board.WhiteToMove) 1 else 6

        val IsDoublePush = abs(move.GetTarget() - move.GetStart()) == 16
        if (IsDoublePush && move.GetStart() !in (start_offset*8)..(start_offset*8)+7)
            return false // Make sure that double push only happens if the pawn hasn't moved yet.

        // We check if the square is occupied twice in double pawn push, so that we ensure the pawn doesn't
        // jump a piece.
        if (board.GetSquare(move.GetStart() + fwd_offset) != null) return false
        if (IsDoublePush)
            if (board.GetSquare(move.GetStart() + fwd_offset*2) != null) return false

        // TODO - captures, en passant
        return (move.GetStart() + (fwd_offset * if (IsDoublePush) 2 else 1)) == move.GetTarget()
    }

    fun ValidateBishopMove(board: Board, move: Move): Boolean {
        return true
    }

    fun ValidateKnightMove(board: Board, move: Move): Boolean {
        return true
    }

    fun ValidateRookMove(board: Board, move: Move): Boolean {
        return true
    }

    fun ValidateQueenMove(board: Board, move: Move): Boolean {
        return true
    }

    fun ValidateKingMove(board: Board, move: Move): Boolean {
        return true
    }

}

object CaptureValidator {
    // TODO - Attack bitboards.

    /** As opposed to MoveValidator.ValidateMove(), this just checks if a move reveals a check.
     *
     * @param board The game board.
     * @param move  The move to check for revealed attacks.
     */
    fun ValidateMove(board: Board, move: Move) {
        // Note - Don't bother checking knight or pawn attacks - you can't reveal an attack from either.
        //      - Do make sure to check when they move though, just to be clear.
    }

    fun ValidatePawnCapture(board: Board, move: Move) {

    }

}
