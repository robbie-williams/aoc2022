package day3

import java.io.File

var rucksack: ArrayList<Array<Compartment>> = ArrayList()
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
    println(priorityMap)
    return priorityMap
}

data class Compartment(val items: MutableMap<Char, CompartmentItem>)

data class CompartmentItem(var count: Int)

fun main() {
    val bufferedReader = File("scratch_folder/day3-scratch-input.txt").bufferedReader()

    bufferedReader.useLines { lines ->
        lines.forEach { it ->
            if (it.isNotEmpty()) {
                val compartmentA = Compartment(HashMap())
                println("\n------------");
                print("Compartment A: ")
                it.substring(0,Math.floorDiv(it.length,2)).forEach{char->
                    compartmentA.items[char]?.count = compartmentA.items[char]?.count!! + 1
                    print("[$char]")
                }
                print(" || Compartment B: ")
                val compartmentB = Compartment(HashMap())
                it.substring(Math.floorDiv(it.length,2),it.length).forEach{char->
                    compartmentB.items[char]?.count = compartmentB.items[char]?.count!! + 1
                    print("[$char]")
                }

                rucksack.add(arrayOf(compartmentA, compartmentB) )
            }
        }
    }
}

