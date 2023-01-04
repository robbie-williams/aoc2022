package day8

import java.io.File

data class Tree(
    var coordinates: Pair<Int,Int>,
    var height: Int,
    var topVisible: Boolean = false,
    var bottomVisible: Boolean = false,
    var leftVisible: Boolean = false,
    var rightVisible: Boolean = false
)
{
    override fun toString(): String {
        return "Tree(${coordinates.second}, ${coordinates.first}) - Height: $height, " +
                "Top Visible: $topVisible, Bottom Visible: $bottomVisible, Left Visible: $leftVisible, " +
                "Right Visible: $rightVisible"
    }
}

fun main() {
    val reader = File("scratch_folder/day8-scratch-input.txt").reader()

    var x = -1
    var y = 0
    val trees = ArrayList<ArrayList<Tree>>()
    trees.add(ArrayList<Tree>())
    reader.use { inputStreamReader ->
        loop@ while (true) {
            val c = inputStreamReader.read()
            if (c == -1) break@loop
            if ('\n'.equals(c.toChar()))
            {
                y++
                x = -1
                trees.add(ArrayList<Tree>())
                continue@loop
            }
            x++
            trees.last().add(Tree(Pair(x,y), c.toChar().digitToInt()))
            println(trees.last().last())
        }
    }
    printTreeMap(trees)
}

    fun printTreeMap(trees: ArrayList<ArrayList<Tree>>) {
    trees.forEach { row ->
        row.forEach{tree ->
            print(tree.height)
        }
        println()
    }

    fun parseVisibility(trees: ArrayList<ArrayList<Tree>>) {
        trees.forEach { row ->
            row.forEach { tree ->
                print(tree.height)
            }
            println()
        }
    }
}
