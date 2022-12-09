package day4

import java.io.File

data class Elf (val assignments: IntRange)

data class ElfPair (val pair: Array<Elf>)

data class ElfPairs (val pairs: MutableList<ElfPair>)
fun main() {
    //Load up elves into a range-tree structure
    val bufferedReader = File("scratch_folder/day4-input.txt").bufferedReader()
    val elfPairs = ElfPairs(ArrayList())
    bufferedReader.useLines { lines ->
        lines.forEach { it ->
            if (it.isNotEmpty()) {
                val thisElfPair = ArrayList<Elf>()
                it.split(',').forEachIndexed { i, assignment ->
                    val elfBounds = ArrayList<Int>()
                    assignment.split('-').forEachIndexed { j, assignmentBound->
                        elfBounds.add(assignmentBound.toInt())
                    }
                    thisElfPair.add(Elf(IntRange(elfBounds.first(),elfBounds.last())))
                }
                elfPairs.pairs.add((ElfPair(arrayOf(thisElfPair.first(),thisElfPair.last()))))
            }
        }
    }

    var coverageDuplicates = 0
    elfPairs.pairs.forEach { elfPair ->
        if ( elfPair.pair.last().assignments.first in elfPair.pair.first().assignments &&
                elfPair.pair.last().assignments.last in elfPair.pair.first().assignments )
        {
            println("found ${elfPair.pair.last().assignments} exists wholly within ${elfPair.pair.first().assignments}" )
            coverageDuplicates++
        }

        else if ( elfPair.pair.first().assignments.first in elfPair.pair.last().assignments &&
            elfPair.pair.first().assignments.last in elfPair.pair.last().assignments )
        {
            println("found ${elfPair.pair.first().assignments} exists wholly within ${elfPair.pair.last().assignments}" )
            coverageDuplicates++
        }
    }

    println("------\nTotal duplicate coverage found: $coverageDuplicates")
}
