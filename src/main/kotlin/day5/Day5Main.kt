package day5

import java.io.File
import java.util.*

data class Dock( val instructions: Queue<Instruction>,
                 val loadingBays: ArrayList<LoadingBay>)

data class LoadingBay( val crates: Stack<Crate>)

data class Crate ( val crateLabel: String)

data class Instruction( var instructionMap: Map<InstructionLoadingIndicator, Pair<Int, InstructionLoadingIndicator>> =
                            mapOf( InstructionLoadingIndicator.AMOUNT to Pair(0, InstructionLoadingIndicator.FROM),
                                InstructionLoadingIndicator.FROM to Pair(0, InstructionLoadingIndicator.TO),
                                InstructionLoadingIndicator.TO to Pair(0, InstructionLoadingIndicator.AMOUNT)),
                        var instructionLoadingIndicator: InstructionLoadingIndicator = InstructionLoadingIndicator.AMOUNT)


enum class InstructionLoadingIndicator
{
    AMOUNT,
    FROM,
    TO
}
enum class LineType{
    CRATE,
    BAY,
    INSTRUCTION,
    UNMATCHED
}

val myLineExpressions = mapOf<LineType,Pair<String, String>>(
LineType.CRATE to Pair("^(([ \\[][ A-Z][ \\]]) {0,1})+?\$","( {0,1}   | {0,1}\\[[a-zA-Z]\\])"),
LineType.BAY to Pair("^(?> [0-9]  {0,1})+\$","(\\d)+"),
LineType.INSTRUCTION to Pair("^move ([0-9]+) from ([0-9]) to ([0-9])\$","([0-9]+)"),
LineType.UNMATCHED to Pair("","")
)

fun main() {
    //Load up elves into a range-tree structure
    val bufferedReader = File("scratch_folder/day5-input.txt").bufferedReader()
    val dock = Dock(LinkedList(), ArrayList())
    var currentInstruction = Instruction()
    bufferedReader.useLines { lines ->
        lines.forEachIndexed { lineNumber, line ->
            if (line.isNotEmpty()) {
                var lineType: LineType
                LineType.values().forEach {
                    if (line.matches(Regex((myLineExpressions.get(it)!!.first)))) {
                        lineType = it
                        print("\n------Line #$lineNumber: $line : $lineType: ")
                        myLineExpressions.get(it)!!.second.toRegex().findAll(line).forEachIndexed() { groupId, group ->
                            if (lineType == LineType.CRATE || lineType == LineType.BAY) {
                                if (dock.loadingBays.size < groupId + 1)
                                    dock.loadingBays.add(LoadingBay(Stack()))
                                if (lineType == LineType.CRATE && group.groupValues.last().trim().isNotEmpty())
                                    dock.loadingBays[groupId].crates.push(Crate(group.groupValues.last()))
                            } else if (lineType == LineType.INSTRUCTION) {
//                                currentInstruction.instructionMap[currentInstruction.instructionLoadingIndicator]!!.first = group.groupValues.last().trim().toInt()
                                //Execute Instruction when TO is loaded
                            }
                            print("Group #$groupId: ${group.groupValues.last()} |")
                        }
                    }
                }
            }
        }
    }
    println("\nDock: $dock")
}