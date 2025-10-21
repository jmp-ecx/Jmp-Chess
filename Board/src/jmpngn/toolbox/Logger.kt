package jmpngn.toolbox

import java.io.PrintStream

enum class LogLevel {
    INFO,
    DEBUG,
    TODO,
    WARNING,
    ERROR,
}

open class Stream(private val stream: PrintStream) {
    open fun Write(txt: String) { stream.println(txt) }
}

class StdoutStream(): Stream(System.out) {}

object Logger {
    private val streams = mutableListOf<Stream>()

    private const val info    = "INFO"
    private const val debug   = "DEBUG"
    private const val todo    = "TODO"
    private const val warning = "WARNING"
    private const val error   = "ERROR"

    private var level: LogLevel = LogLevel.INFO

    private var fmt = "" // TODO - Format logger messages.

    fun AddStream(stream: Stream) {
        streams.add(stream)
    }

    fun SetLevel(lvl: LogLevel) {
        level = lvl
    }

    private fun write(msg: String) {
        streams.forEachIndexed { _, stream ->
            stream.Write(msg)
        }
    }

    private fun getCallerFunction(): String = Thread.currentThread().stackTrace[3].toString()

    fun Info(msg: String)    { if (level <= LogLevel.INFO)    { write("[${getCallerFunction()}] ${Color.Format(info, Color.Cyan)} :: $msg")    }}
    fun Debug(msg: String)   { if (level <= LogLevel.DEBUG)   { write("[${getCallerFunction()}] ${Color.Format(debug, Color.Green)} :: $msg")   }}
    fun TODO(msg: String)    { if (level <= LogLevel.TODO)    { write("[${getCallerFunction()}] ${Color.Format(todo, Color.Magenta)} :: $msg")    }}
    fun Warning(msg: String) { if (level <= LogLevel.WARNING) { write("[${getCallerFunction()}] ${Color.Format(warning, Color.Yellow)} :: $msg") }}
    fun Error(msg: String)   { if (level <= LogLevel.ERROR)   { write("[${getCallerFunction()}] ${Color.Format(error, Color.Red)} :: $msg")   }}

}

