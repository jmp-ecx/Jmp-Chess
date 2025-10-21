import jmpngn.board.PieceType

object AppState {
    const val SQUARE_SIZE = 100
    const val WINDOW_SIZE = SQUARE_SIZE * 8

    var DisplayedBitboard = PieceType.None
        set (piece) {
            field = if (field == piece) PieceType.None else piece
        }
}
