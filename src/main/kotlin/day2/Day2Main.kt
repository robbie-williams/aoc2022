package day2

import java.io.File

val CODE_MAP = """
    {"opponent":
        { "rock": {"code": "A","value": "1"},
        "paper": {"code": "B","value": "2"},
        "scissors": {"code": "C","value": "3"}},
    "me":
        { "rock": {"code": "X","value": "1"},
        "paper": {"code": "Y","value": "2"},
        "scissors": {"code": "Z","value": "3"}},
    "outcome":
        { "loss": "0",
          "draw": "3",
          "win" : 6 }
    }
""".trimIndent()

fun main() {
    val bufferedReader = File("scratch_folder/day2-scratch-input.txt").bufferedReader()
    bufferedReader.useLines { lines ->
        lines.forEach {
            println(it)
        }
    }

    println("---")
    println(CODE_MAP)
}