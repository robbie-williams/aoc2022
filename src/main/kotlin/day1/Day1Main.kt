package day1

import kotlin.collections.ArrayList
import java.io.File

fun main() {
    val bufferedReader = File("scratch_folder/day1-input.txt").bufferedReader()

    val elvesAndBags: ArrayList<ArrayList<Long>> = ArrayList()
    elvesAndBags.add(ArrayList())
    var currentElf = elvesAndBags[elvesAndBags.size - 1]

    bufferedReader.useLines { lines -> lines.forEach {
        if (it.isNotBlank())
            currentElf.add(it.toLong())
        else
        {
            elvesAndBags.add(ArrayList())
            currentElf = elvesAndBags[elvesAndBags.size - 1]
        }
    } }
    var i = 0
    var biggestElfCal: Long = 0
    var bigThree: Long = 0

    elvesAndBags.iterator().forEach {
        val totalElfCalories = getElfCalories(it)
        if (totalElfCalories > biggestElfCal ) biggestElfCal= totalElfCalories
        println("$i: $totalElfCalories")
        i++
    }
    val calorieComparator = Comparator { elf1: ArrayList<Long>, elf2: ArrayList<Long> ->
        getElfCalories(elf2).compareTo(getElfCalories(elf1)) }

    elvesAndBags.sortedWith(calorieComparator).asSequence().take(3).forEach {
        bigThree += getElfCalories(it)
    }

    println("Biggest Elf Calories are: $biggestElfCal")
    println("Biggest Elf Calories are: $bigThree")
}

fun getElfCalories(elf: List<Long>): Long {
    var calories: Long = 0
    elf.iterator().forEach { calories += it }
    return calories
}