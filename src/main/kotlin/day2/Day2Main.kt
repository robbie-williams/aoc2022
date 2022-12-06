package day2

import java.io.File
import java.util.*

var codeMap: MutableMap<String, Selection> = HashMap()
var selectionValueMap: MutableMap<SelectionType, Int> = EnumMap(SelectionType::class.java)
var aBeatsB: MutableMap<SelectionType, SelectionType> = EnumMap(SelectionType::class.java)
var aIsBeatenByB: MutableMap<SelectionType, SelectionType> = EnumMap(SelectionType::class.java)

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

data class Selection (var selection: SelectionType,
                      var intent: SelectionType)

enum class SelectionType
{
    ROCK,
    PAPER,
    SCISSORS,
    LOSE,
    WIN,
    DRAW
}

fun main() {
    codeMap["A"] = Selection(SelectionType.ROCK,SelectionType.ROCK)
    codeMap["B"] = Selection(SelectionType.PAPER,SelectionType.PAPER)
    codeMap["C"] = Selection(SelectionType.SCISSORS, SelectionType.SCISSORS)
    codeMap["X"] = Selection(SelectionType.LOSE, SelectionType.LOSE)
    codeMap["Y"] = Selection(SelectionType.DRAW, SelectionType.DRAW)
    codeMap["Z"] = Selection(SelectionType.WIN, SelectionType.WIN)
    aBeatsB[SelectionType.SCISSORS] = SelectionType.PAPER
    aBeatsB[SelectionType.PAPER] = SelectionType.ROCK
    aBeatsB[SelectionType.ROCK] = SelectionType.SCISSORS
    aIsBeatenByB[SelectionType.SCISSORS] = SelectionType.ROCK
    aIsBeatenByB[SelectionType.PAPER] = SelectionType.SCISSORS
    aIsBeatenByB[SelectionType.ROCK] = SelectionType.PAPER
    selectionValueMap[SelectionType.ROCK] = 1
    selectionValueMap[SelectionType.PAPER] = 2
    selectionValueMap[SelectionType.SCISSORS] = 3


    val bufferedReader = File("scratch_folder/day2-input.txt").bufferedReader()
//    val bufferedReader = File("scratch_folder/day2-scratch-input.txt").bufferedReader()
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

            val round = Round(roundIndex++, codeMap[roundCodes[0]]!!.copy(),codeMap[roundCodes[1]]!!.copy())
            game.round.add( round )

            calculateRoundOutcomesPuzzle2(round)
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
            println("I intended to ${round.me.intent} and my score increased by ${round.outcome.meScore}")
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

fun calculateRoundOutcomesPuzzle1(round: Round) {
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
    round.outcome.meScore += selectionValueMap[round.me.selection]!!
    round.outcome.opponentScore += selectionValueMap[round.opponent.selection]!!
}

fun calculateRoundOutcomesPuzzle2(round: Round) {
    if(round.me.selection == SelectionType.DRAW)
    {
        round.outcome.meScore = DRAW
        round.outcome.opponentScore = DRAW
        round.me.selection = round.opponent.selection
    }
    else if (round.me.selection == SelectionType.WIN)
    {
        round.outcome.meScore = WIN
        round.me.selection = aIsBeatenByB[round.opponent.selection]!!
    }
    else
    {
        round.outcome.opponentScore = WIN
        round.me.selection = aBeatsB[round.opponent.selection]!!
    }
    round.outcome.meScore += selectionValueMap[round.me.selection]!!
    round.outcome.opponentScore += selectionValueMap[round.opponent.selection]!!
}

