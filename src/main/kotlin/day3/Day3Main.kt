package day3

import java.io.File

val itemPriorities: MutableMap<Char, Int> = buildPriorities()

fun buildPriorities(): MutableMap<Char, Int> {
    val priorityMap: MutableMap<Char, Int> = HashMap()
    //A-Z (65-90), priorities 27-52
    for (i in 65..90)
    {
        priorityMap[Char(i)]=i-38
    }
    //a-z (97-122), priorities 1-26
    for (i in 97..122)
    {
        priorityMap[Char(i)]=i-96
    }
    return priorityMap
}

data class Compartment(val label: String,
                       val items: MutableMap<Char, CompartmentItem>)

data class Rucksack(val compartments: Array<Compartment>,
val duplicates: MutableList<Char>)

data class CompartmentItem(var count: Int)

fun main() {
    val rucksacks: ArrayList<Rucksack> = ArrayList()
    val bufferedReader = File("scratch_folder/day3-input.txt").bufferedReader()
    var lineCount = 0
    val badgeList: MutableList<Char> = ArrayList()
    bufferedReader.useLines { lines ->
        lines.forEach { it ->
            if (it.isNotEmpty()) {
                lineCount++
                rucksacks.add(Rucksack(arrayOf(
                    Compartment("Compartment A", HashMap()), Compartment("Compartment B", HashMap())) ,ArrayList()) )
                val rawCompartments = arrayOf(it.substring(0,Math.floorDiv(it.length,2)),
                    it.substring(Math.floorDiv(it.length,2),it.length) )

                for (i in 0..1)
                {
                    val compartment = rucksacks.last().compartments[i]
                    rawCompartments[i].forEach{ char->
                        if (compartment.items.containsKey(char))
                            compartment.items[char]?.count = compartment.items[char]?.count!! + 1
                        else
                            compartment.items[char] = CompartmentItem(1)
                        if (i == 1 &&
                            rucksacks.last().compartments[0].items.containsKey(char) &&
                            compartment.items[char]?.count!! == 1)
                            rucksacks.last().duplicates.add(char)
                    }
                }
                println("Rucksack duplicates: ${rucksacks.last().duplicates}")

                if(lineCount % 3 == 0)
                {
                    badgeList.add(identifyBadge(rucksacks))
                    println("Badge identified: ${badgeList.last()}")
                }

            }
        }
    }
    var totalRucksackPriorities = 0
    for (rucksack in rucksacks) {
        rucksack.duplicates.forEach { char->
            totalRucksackPriorities += itemPriorities[char]!!
        }
    }
    println("Total Rucksack Priorities: $totalRucksackPriorities")

    var totalBadgePriorities = 0
    badgeList.forEach { char ->
        totalBadgePriorities += itemPriorities[char]!!
    }

    println("Total Badge Priorities: $totalBadgePriorities")
}

fun identifyBadge(rucksacks: ArrayList<Rucksack>): Char {
    for (i in 0..1) {
        for (keyA in rucksacks.last().compartments[i].items.keys) {
            for (j in 0..1) {
                for (keyB in rucksacks[rucksacks.size - 2].compartments[j].items.keys) {
                    if (keyA == keyB) {
                        for (k in 0..1) {
                            for (keyC in rucksacks[rucksacks.size - 3].compartments[k].items.keys) {
                                if (keyC == keyB) {
                                    return keyA
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    return '-'
}