package ui

import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Toolkit
import java.awt.event.*
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.Timer

class Window(private val Width: Int, private val Height: Int): JFrame("Chess") {
    private val Surface = Surface(Width, Height)

    init {
        add(Surface)
        addMouseListener(Surface)
        defaultCloseOperation = EXIT_ON_CLOSE

        isResizable = false
        pack()
        setLocationRelativeTo(null)
    }

    fun AddDrawer(drawer: (Graphics) -> Unit) {
        Surface.Drawers.add(drawer)
    }

    fun AddClickHandler(handler: (MouseEvent) -> Unit) {
        Surface.Clicks.add(handler)
    }
}

class Surface(width: Int, height: Int): JPanel(), ActionListener, MouseListener {
    private val Timer: Timer

    val Drawers: MutableList<(Graphics) -> Unit> = mutableListOf()
    val Clicks: MutableList<(MouseEvent) -> Unit> = mutableListOf()

    init {
        preferredSize = Dimension(width, height)
        background = Color(130, 130, 130)

        Timer = Timer(0, this)
        Timer.delay = 1
        Timer.start()
    }

    override fun actionPerformed(e: ActionEvent?) {
        repaint()
    }

    override fun paintComponent(g: Graphics?) {
        super.paintComponent(g)
        if (g == null) return

        for (d in Drawers) {
            d(g)
        }

        Toolkit.getDefaultToolkit().sync()
    }

    override fun mouseClicked(e: MouseEvent?) {
        if (e == null) return
        for (c in Clicks) {
            c(e)
        }
    }

    override fun mousePressed(e: MouseEvent?) { }
    override fun mouseReleased(e: MouseEvent?) { }
    override fun mouseEntered(e: MouseEvent?) { }
    override fun mouseExited(e: MouseEvent?) { }

}
