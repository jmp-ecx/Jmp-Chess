package jmpngn.board

import kotlin.math.abs
import kotlin.math.floor

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
        val start_rank  = floor(move.GetStart() / 8.0).toInt() * 8
        val target_rank = floor(move.GetTarget() / 8.0).toInt() * 8

        val vertical_movement = move.GetTarget() % 8 == move.GetStart() % 8
        val horizontal_movement = start_rank == target_rank

        if (!vertical_movement && !horizontal_movement) return false

        var is_legal = true
        var i = move.GetStart()
        if (vertical_movement) {
            if (move.GetStart() > move.GetTarget()) {
                while (i > move.GetTarget()+8) {
                    i -= 8
                    if (board.GetSquare(i) != null) {
                        is_legal = false
                        break
                    }
                }
            } else {
                while (i < move.GetTarget()-8) {
                    i += 8
                    if (board.GetSquare(i) != null) {
                        is_legal = false
                        break
                    }
                }
            }
        } else {
            if (move.GetStart() > move.GetTarget()) {
                while (i > move.GetTarget()+1) {
                    i -= 1
                    if (board.GetSquare(i) != null) {
                        is_legal = false
                        break
                    }
                }
            } else {
                while (i < move.GetTarget()-1) {
                    i += 1
                    if (board.GetSquare(i) != null) {
                        is_legal = false
                        break
                    }
                }
            }
        }

        val p = board.GetSquare(move.GetTarget())
        if (p != null && p.white == board.GetSquare(move.GetStart())!!.white)
            is_legal = false

        return is_legal
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
