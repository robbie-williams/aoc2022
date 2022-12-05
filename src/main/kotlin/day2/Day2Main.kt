package day2

import java.io.File

var codeMap: MutableMap<String, Selection> = HashMap()

const val DRAW: Int = 3
const val WIN: Int = 6


data class Game(val gameIndex: Int,
                val round: MutableList<Round>)

data class Round( val roundIndex: Int,
                  val opponent: Selection,
                  val me: Selection,
                  val outcome: RoundOutcome = RoundOutcome())

data class RoundOutcome (var opponentScore: Int = 0,
                         var meScore: Int = 0)

data class Selection (val selection: SelectionType,
                      val selectionValue: Int = 0)

enum class SelectionType
{
    ROCK,
    PAPER,
    SCISSORS
}

fun main() {
    codeMap["A"] = Selection(SelectionType.ROCK, 1)
    codeMap["B"] = Selection(SelectionType.PAPER, 2)
    codeMap["C"] = Selection(SelectionType.SCISSORS, 3)
    codeMap["X"] = Selection(SelectionType.ROCK, 1)
    codeMap["Y"] = Selection(SelectionType.PAPER, 2)
    codeMap["Z"] = Selection(SelectionType.SCISSORS, 3)

    val bufferedReader = File("scratch_folder/day2-input.txt").bufferedReader()
    val games: MutableList<Game> = ArrayList()
    var gameIndex = 0
    var roundIndex = 0
    games.add(Game(gameIndex, ArrayList()))
    bufferedReader.useLines { lines ->
        lines.forEach {
            if (it.isEmpty())
            {
                roundIndex = 0
                games.add(Game(++gameIndex,ArrayList()))
            }
            val game = games.last()

            val roundCodes = it.split(" ")

            val round = Round(roundIndex++, codeMap[roundCodes[0]]!!,codeMap[roundCodes[1]]!!)
            game.round.add( round )

            calculateRoundOutcomes(round)
        }
    }

    var myTournamentScore = 0

    games.forEach { game ->
        var myGameScore = 0
        var opponentGameScore = 0
        println("---")
        println("Game #${game.gameIndex}")

        game.round.forEach { round ->
            println("Round #${round.roundIndex}")
            println ("I play ${round.me.selection.name} vs opponent's ${round.opponent.selection.name}")
            myGameScore += round.outcome.meScore
            opponentGameScore += round.outcome.opponentScore
        }

        myTournamentScore += myGameScore

        println("My Score: $myGameScore")
        println("Opponent Score: $opponentGameScore")

    }

    println("---\n---")
    println("My Total Tournament Score: $myTournamentScore")
}

fun calculateRoundOutcomes(round: Round) {
    if(round.me.selection == round.opponent.selection)
    {
        round.outcome.meScore = DRAW
        round.outcome.opponentScore = DRAW
    }
    else if ((SelectionType.ROCK == round.me.selection && SelectionType.SCISSORS == round.opponent.selection)||
        (SelectionType.SCISSORS == round.me.selection && SelectionType.PAPER == round.opponent.selection) ||
        (SelectionType.PAPER == round.me.selection && SelectionType.ROCK == round.opponent.selection))
    {
        round.outcome.meScore = WIN
    }
    else
    {
        round.outcome.opponentScore = WIN
    }
    round.outcome.meScore += round.me.selectionValue
    round.outcome.opponentScore += round.opponent.selectionValue
}
