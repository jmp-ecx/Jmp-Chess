package ui

import java.awt.Color
import java.awt.Graphics

class Button(private val x: Int, private val y: Int,
             private val w: Int, private val h: Int,
             private val color: Color,
             private val text: String,
             private val action: () -> Unit) {

    fun Clicked(m_x: Int, m_y: Int) {
        if (m_x in x..x+w && m_y in y..y+h) action()
    }

    fun Draw(g: Graphics) {
        g.color = color
        g.fillRect(x, y, w, h)
        g.color = Color(0, 0, 0)
        g.drawString(text, x+10, y+30)
    }

}