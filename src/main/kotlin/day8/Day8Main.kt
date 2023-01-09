package day8

import java.io.File

data class Tree(
//    var coordinates: Pair<Int,Int>,
    val x: Int,
    val y: Int,
    var height: Int,
    val visibility: MutableMap<VISIBILITY_PATH, Boolean> = mutableMapOf(
        VISIBILITY_PATH.FROM_TOP to false,
        VISIBILITY_PATH.FROM_BOTTOM to false,
        VISIBILITY_PATH.FROM_LEFT to false,
        VISIBILITY_PATH.FROM_RIGHT to false
    )
)
{
    override fun toString(): String {
        return "Tree($x, $y) - Height: $height,  Visibility: " +
                "${VISIBILITY_PATH.values().map { path ->  path.toString() + '=' + visibility[path]} }"
    }
}

enum class VISIBILITY_PATH
{
    FROM_TOP,
    FROM_BOTTOM,
    FROM_LEFT,
    FROM_RIGHT
}


fun main() {
    val reader = File("scratch_folder/day8-input.txt").reader()
    var y = -1
    var x = 0
    val trees = ArrayList<ArrayList<Tree>>()
    trees.add(ArrayList<Tree>())
    reader.use { inputStreamReader ->
        loop@ while (true) {
            val c = inputStreamReader.read()
            if (c == -1) break@loop
            if ('\n'.equals(c.toChar()))
            {
                x++
                y = -1
                trees.add(ArrayList<Tree>())
                continue@loop
            }
            y++
            trees.last().add(Tree(x,y, c.toChar().digitToInt()))
        }
    }
    parseVisibility(trees)
    printTreeMap(trees)

    println("-----\nPart1: ${part1(trees)}")
}

    fun printTreeMap(trees: ArrayList<ArrayList<Tree>>) {
        trees.forEach { row ->
            row.forEach { tree ->
                if (tree != row.first()) print(".")
                print("${tree.height}${if (isVisible(tree))"v" else "n"}")
            }
            println()
        }
    }

    fun parseVisibility(trees: ArrayList<ArrayList<Tree>>) {
        trees.forEach { row ->
            row.forEach { tree ->
                VISIBILITY_PATH.values().forEach{
                    tree.visibility[it] = true
                }
            }
        }

        //For each tree
        trees.forEach { row ->
            row.forEach { tree ->
                //compare against each tree in the 2d map
                trees.forEach { row ->
                    row.forEach { outerTree ->
                        if (tree.x == outerTree.x)
                            if (tree.y < outerTree.y){
                                if (tree.height <= outerTree.height) {
                                    tree.visibility[VISIBILITY_PATH.FROM_BOTTOM] = false
                                }
                            } else if (tree.y > outerTree.y) {
                                if (tree.height <= outerTree.height) {
                                    tree.visibility[VISIBILITY_PATH.FROM_TOP] = false
                                }
                            }
                        if (tree.y == outerTree.y)
                            if (tree.x < outerTree.x){
                                if (tree.height <= outerTree.height) {
                                    tree.visibility[VISIBILITY_PATH.FROM_RIGHT] = false
                                }
                            } else if (tree.x > outerTree.x) {
                                if (tree.height <= outerTree.height) {
                                    tree.visibility[VISIBILITY_PATH.FROM_LEFT] = false
                                }
                            }
                    }
                }
            }
        }
    }

fun isVisible(tree: Tree): Boolean {
    return tree.visibility.any{
        it.value
    }
}

fun part1(trees: ArrayList<ArrayList<Tree>>): Int{
    var visibleTreeCount = 0
    trees.forEach{ row ->
        visibleTreeCount +=
        row.count{ tree ->
            isVisible(tree)
        }
    }
    return visibleTreeCount
}