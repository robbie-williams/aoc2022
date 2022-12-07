package day3

import java.io.File

var rucksacks: ArrayList<Rucksack> = ArrayList()
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
    val bufferedReader = File("scratch_folder/day3-input.txt").bufferedReader()

    bufferedReader.useLines { lines ->
        lines.forEach { it ->
            if (it.isNotEmpty()) {
                rucksacks.add(Rucksack(arrayOf(
                    Compartment("Compartment A", HashMap()), Compartment("Compartment B", HashMap())) ,ArrayList()) )
                val rawCompartments = arrayOf(it.substring(0,Math.floorDiv(it.length,2)),
                    it.substring(Math.floorDiv(it.length,2),it.length) )

                for (i in 0..1)
                {
                    val compartment = rucksacks.last().compartments[i]
                    rawCompartments[i].forEach{ char->
                        if (compartment.items.containsKey(char))
                        {
                            compartment.items[char]?.count = compartment.items[char]?.count!! + 1
                        }
                        else
                        {
                            compartment.items[char] = CompartmentItem(1)
                        }
                        if (i == 1 &&
                            rucksacks.last().compartments[0].items.containsKey(char) &&
                            compartment.items[char]?.count!! == 1)
                        {
                            rucksacks.last().duplicates.add(char)
                        }
                    }
                }

                println("Rucksack duplicates: ${rucksacks.last().duplicates}")
            }
        }
    }
    var totalPriorities = 0
    for (rucksack in rucksacks) {
        rucksack.duplicates.forEach { char->
            totalPriorities += itemPriorities[char]!!
        }
    }
    println("Total Priorities: $totalPriorities")
}

