package jmpngn.toolbox

enum class Color(val code: String) {
    Reset("\u001b[0m"),
    Black("\u001b[0;30m"),
    Red("\u001b[0;31m"),
    Green("\u001b[0;32m"),
    Yellow("\u001b[0;33m"),
    Blue("\u001b[0;34m"),
    Magenta("\u001b[0;35m"),
    Cyan("\u001b[0;36m"),
    Grey("\u001b[0;37m");

    companion object {
        fun Format(str: String, col: Color): String = "${col.code}$str${Reset.code}"
    }
}