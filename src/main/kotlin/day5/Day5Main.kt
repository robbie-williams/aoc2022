package day5

import java.io.File
import java.util.*

data class Dock( val instructions: Queue<Instruction>,
                 val loadingBays: ArrayList<LoadingBay>)

data class LoadingBay( val crates: Stack<Crate>)

data class Crate ( val crateLabel: String)

data class Instruction(var instructionMap: MutableMap<InstructionLoadingIndicator, Pair<Int, InstructionLoadingIndicator>> =
                            mutableMapOf( InstructionLoadingIndicator.AMOUNT to Pair(0, InstructionLoadingIndicator.FROM),
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

val myLineExpressions = mapOf(
LineType.CRATE to Pair("^(([ \\[][ A-Z][ \\]]) {0,1})+?\$","( {0,1}   | {0,1}\\[[a-zA-Z]\\])"),
LineType.BAY to Pair("^(?> [0-9]  {0,1})+\$","(\\d)+"),
LineType.INSTRUCTION to Pair("^move ([0-9]+) from ([0-9]) to ([0-9])\$","([0-9]+)"),
LineType.UNMATCHED to Pair("","")
)

fun main() {
    val bufferedReader = File("scratch_folder/day5-input.txt").bufferedReader()
    val dock = Dock(LinkedList(), ArrayList())
    val currentInstruction = Instruction()
    bufferedReader.useLines { lines ->
        lines.forEachIndexed { lineNumber, line ->
            if (line.isNotEmpty()) {
                var lineType: LineType
                LineType.values().forEach {
                    if (line.matches(Regex((myLineExpressions.get(it)!!.first)))) {
                        lineType = it
                        myLineExpressions.get(it)!!.second.toRegex().findAll(line).forEachIndexed() { groupId, group ->
                            if (lineType == LineType.CRATE || lineType == LineType.BAY) {
                                if (dock.loadingBays.size < groupId + 1)
                                    dock.loadingBays.add(LoadingBay(Stack()))
                                if (lineType == LineType.CRATE && group.groupValues.last().trim().isNotEmpty())
                                    dock.loadingBays[groupId].crates.push(Crate(group.groupValues.last().trim()))
                                if (lineType == LineType.BAY)
                                    dock.loadingBays.forEach {loadingBay ->
                                        loadingBay.crates.reverse()
                                }
                            } else if (lineType == LineType.INSTRUCTION) {
                                currentInstruction.instructionMap[currentInstruction.instructionLoadingIndicator] =
                                    Pair(group.groupValues.last().trim().toInt(),
                                        currentInstruction.instructionMap[currentInstruction.instructionLoadingIndicator]!!.second)
                                currentInstruction.instructionLoadingIndicator = currentInstruction.instructionMap[currentInstruction.instructionLoadingIndicator]!!.second
                                //Execute Instruction when TO is loaded
                                if(currentInstruction.instructionLoadingIndicator == InstructionLoadingIndicator.AMOUNT) {
                                    printDock(dock)
                                    executeInstructionCrateMover9001(currentInstruction,dock)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    printDock(dock)
    println("\nTop Crates: ")
    dock.loadingBays.forEach { bay ->
        print(bay.crates.peek().crateLabel)
    }

}

//Part 1
fun executeInstructionCrateMover9000(currentInstruction: Instruction, dock: Dock) {
    print(
        "\nExecuting instruction to move " +
                "${currentInstruction.instructionMap[InstructionLoadingIndicator.AMOUNT]!!.first} units from " +
                "Bay ${currentInstruction.instructionMap[InstructionLoadingIndicator.FROM]!!.first } units to " +
                "Bay ${currentInstruction.instructionMap[InstructionLoadingIndicator.TO]!!.first }")

    for (i in 1..currentInstruction.instructionMap[InstructionLoadingIndicator.AMOUNT]!!.first) {
        val crate = dock.loadingBays[currentInstruction.instructionMap[InstructionLoadingIndicator.FROM]!!.first-1].crates.pop()
        print("\nMoving crate: ${crate.crateLabel}")
        dock.loadingBays[currentInstruction.instructionMap[InstructionLoadingIndicator.TO]!!.first-1].crates.push(crate)
    }
}

//Part 2
fun executeInstructionCrateMover9001(currentInstruction: Instruction, dock: Dock) {
    print(
        "\nExecuting instruction to move " +
                "${currentInstruction.instructionMap[InstructionLoadingIndicator.AMOUNT]!!.first} units from " +
                "Bay ${currentInstruction.instructionMap[InstructionLoadingIndicator.FROM]!!.first } units to " +
                "Bay ${currentInstruction.instructionMap[InstructionLoadingIndicator.TO]!!.first }")

    val tempStack = Stack<Crate>()
    for (i in 1..currentInstruction.instructionMap[InstructionLoadingIndicator.AMOUNT]!!.first) {
        tempStack.push(dock.loadingBays[currentInstruction.instructionMap[InstructionLoadingIndicator.FROM]!!.first-1].crates.pop())
    }

    while( tempStack.isNotEmpty())
    {
        val crate = tempStack.pop()
        print("\nMoving crate: ${crate.crateLabel}")
        dock.loadingBays[currentInstruction.instructionMap[InstructionLoadingIndicator.TO]!!.first-1].crates.push(crate)
    }
}