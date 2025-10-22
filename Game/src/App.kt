import AppState.SQUARE_SIZE
import AppState.WINDOW_SIZE
import jmpngn.board.Board
import jmpngn.board.Move
import jmpngn.board.Piece
import jmpngn.board.PieceType
import jmpngn.toolbox.*
import ui.Button
import ui.PieceTexture
import ui.Window
import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities
import kotlin.math.floor


fun main() {
    // Disable DPI-Aware scaling because it messes up everything on laptops.
    System.setProperty("sun.java2d.uiScale", "1")

    Logger.AddStream(StdoutStream())
    Logger.SetLevel(LogLevel.INFO)

    val board = Board()
    board.LoadFEN("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b")

    val win = Window(WINDOW_SIZE + SQUARE_SIZE, WINDOW_SIZE)
    val tex = PieceTexture("./Game/resources/chess.png")

    SwingUtilities.invokeLater { win.isVisible = true }

    val ui_buttons = listOf(
        Button(WINDOW_SIZE+10, 10, SQUARE_SIZE-20, 40, Color(255, 0, 0), "P") {
            AppState.DisplayedBitboard = PieceType.Pawn
        },
        Button(WINDOW_SIZE+10, 60, SQUARE_SIZE-20, 40, Color(0, 255, 0), "B") {
            AppState.DisplayedBitboard = PieceType.Bishop
        },
        Button(WINDOW_SIZE+10, 110, SQUARE_SIZE-20, 40, Color(0, 0, 255), "N") {
            AppState.DisplayedBitboard = PieceType.Knight
        },
        Button(WINDOW_SIZE+10, 160, SQUARE_SIZE-20, 40, Color(255, 255, 0), "R") {
            AppState.DisplayedBitboard = PieceType.Rook
        },
        Button(WINDOW_SIZE+10, 210, SQUARE_SIZE-20, 40, Color(0, 255, 255), "Q") {
            AppState.DisplayedBitboard = PieceType.Queen
        },
        Button(WINDOW_SIZE+10, 260, SQUARE_SIZE-20, 40, Color(255, 0, 255), "K") {
            AppState.DisplayedBitboard = PieceType.King
        },
        Button(WINDOW_SIZE+10, WINDOW_SIZE-50, SQUARE_SIZE-20, 40, Color(50, 50, 120), "Undo") {
            board.UndoMove()
        },
    )

    var selected: Pair<Piece?, Pair<Int, Int>>? = null

    win.AddClickHandler { e: MouseEvent ->
        if (e.x < WINDOW_SIZE) {
            val square_x = 7 - floor(e.x / SQUARE_SIZE.toFloat()).toInt()
            val square_y = 7 - floor((e.y - 30) / SQUARE_SIZE.toFloat()).toInt()

            if (selected == null) {
                selected = Pair(board.GetSquare(square_x, square_y), Pair(square_x, square_y))
            } else {
                val move = Move(Pair(selected?.second ?: Pair(0, 0), Pair(square_x, square_y)))
                board.Move(move)
                selected = null
            }
        } else {
            for (btn in ui_buttons) {
                btn.Clicked(e.x, e.y - 30)
            }
        }
    }

    win.AddDrawer { g: Graphics ->
        g.font = Font("Consolas", Font.PLAIN, 25)

        for (square_y in 7 downTo 0) {
            for (square_x in 7 downTo 0) {
                val light = (square_x+square_y) % 2 == 0

                val board_x = 7-square_x
                val board_y = 7-square_y

                g.color = if (selected != null && selected?.second == Pair(board_x, board_y)
                           && selected?.first != null
                           && selected?.first?.type != PieceType.None) {
                    Color(220, 72, 88)
                } else if (AppState.DisplayedBitboard != PieceType.None
                        && board.IsPiece(board_x, board_y, AppState.DisplayedBitboard)) {
                    Color(72, 88, 220)
                } else {
                    Color(
                        if (light) 243 else 183,
                        if (light) 226 else 125,
                        if (light) 207 else 107
                    )
                }

                g.fillRect(
                    square_x * SQUARE_SIZE,
                    square_y * SQUARE_SIZE,
                    SQUARE_SIZE,
                    SQUARE_SIZE
                )

                val p = board.GetSquare(board_x, board_y)
                if (p != null && p.type != PieceType.None) {
                    g.drawImage(tex.GetPiece(p), square_x * SQUARE_SIZE, square_y * SQUARE_SIZE, null)
                }

            }
        }

        for (btn in ui_buttons) {
            btn.Draw(g)
        }

    }
}
