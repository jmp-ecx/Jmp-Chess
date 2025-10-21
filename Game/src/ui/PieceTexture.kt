package ui

import AppState.SQUARE_SIZE
import jmpngn.board.Piece
import jmpngn.board.PieceType
import java.awt.Image
import java.awt.Image.*
import java.io.File
import javax.imageio.ImageIO

const val PIECE_WIDTH = 333

class PieceTexture(path: String) {
    private val Image = ImageIO.read(File(path))

    fun GetPiece(piece: Piece): Image {
        val x = when (piece.type) {
            PieceType.None   -> 0
            PieceType.Pawn   -> 5
            PieceType.Bishop -> 2
            PieceType.Knight -> 3
            PieceType.Rook   -> 4
            PieceType.Queen  -> 1
            PieceType.King   -> 0
        }
        val y = if (piece.white) 0 else 1

        val img = Image.getSubimage(x * PIECE_WIDTH, y * PIECE_WIDTH, PIECE_WIDTH, PIECE_WIDTH)
        return img.getScaledInstance(SQUARE_SIZE, SQUARE_SIZE, SCALE_AREA_AVERAGING)
    }

}
