package day3

import java.io.File

var rucksack: ArrayList<Array<Compartment>> = ArrayList()
val itemPriorities: MutableMap<Char, Int> = HashMap()

data class Compartment(val items: MutableMap<Char, CompartmentItem>)

data class CompartmentItem(var count: Int)

fun main() {
    val bufferedReader = File("scratch_folder/day3-scratch-input.txt").bufferedReader()

    bufferedReader.useLines { lines ->
        lines.forEach { it ->
            if (it.isEmpty()) {
                val comparmentA = Compartment(HashMap())
                    it.substring(0,Math.floorDiv(it.length,2)).forEach{char->
                        comparmentA.items[char]?.count = comparmentA.items[char]?.count!! + 1
                    }
                val comparmentB = Compartment(HashMap())
                it.substring(Math.floorDiv(it.length,2),it.length).forEach{char->
                    comparmentB.items[char]?.count = comparmentB.items[char]?.count!! + 1
                }

                rucksack.add(arrayOf(comparmentA, comparmentB) )
            }
        }
    }
}

