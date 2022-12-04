package day2

import kotlinx.serialization.Serializable
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

@Serializable
data class CodeMap(val opponent: GameChoice,
                    val me: GameChoice,
                    val outcome: Outcome)

@Serializable
data class GameChoice(val rock: Choice ,
                    val paper: Choice,
                    val scissors: Choice)

@Serializable
data class Choice (val code: String,
                   val value: Int)

@Serializable
data class Outcome(val Loss: Int,
                   val Draw: Int,
                   val Win: Int)




fun main() {
    val bufferedReader = File("scratch_folder/day2-scratch-input.txt").bufferedReader()
    bufferedReader.useLines { lines ->
        lines.forEach {
            println(it)
        }
    }
    println("---")
    println(CODE_MAP)

//    val obj = Json.decodeFromString<CodeMap>(CODE_MAP)

}