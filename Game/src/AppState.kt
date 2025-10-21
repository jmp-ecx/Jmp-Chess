import jmpngn.board.PieceType

object AppState {
    const val SQUARE_SIZE = 100
    const val WINDOW_SIZE = SQUARE_SIZE * 8

    var DisplayedBitboard = PieceType.None

    fun SetDisplayedBitboard(piece: PieceType) {
        DisplayedBitboard = if (DisplayedBitboard == piece) PieceType.None else piece
    }
}
