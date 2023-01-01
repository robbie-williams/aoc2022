package day7

import org.jetbrains.annotations.Nullable
import java.io.File

const val PART1_FOLDER_SIZE_LIMIT = 100000L
const val PART2_TOTAL_DRIVE_SIZE = 70000000L
const val PART2_PATCH_SIZE = 30000000L

enum class LineType{
    CHANGE_DIRECTORY,
    LIST_DIRECTORY,
    DIRECTORY_LINE,
    FILE_LINE,
    UNMATCHED
}

val myLineExpressions: Map<LineType, String> = mapOf(
    LineType.CHANGE_DIRECTORY to "^\\\$ +cd (.*)\$",
    LineType.LIST_DIRECTORY to "^\\\$ +ls *\$",
    LineType.DIRECTORY_LINE to "^dir (.*)\$",
    LineType.FILE_LINE to "^([0-9]*) (.*)\$",
    LineType.UNMATCHED to ""
)

enum class PointType{
    FILE,
    FOLDER
}

data class Point(
    @Nullable var parent: Point?,
    val points: MutableMap<String, Point>,
    var name: String,
    var size: Long,
    val type: PointType
)
{
    override fun toString(): String {
        return "${name}  ${if (type == PointType.FOLDER)  "(Total ${size})" else size}"
    }
}

val root = Point(null, HashMap(), "/", 0, PointType.FOLDER)

fun main() {
    val bufferedReader = File("scratch_folder/day7-input.txt").bufferedReader()
    var cd = root
    bufferedReader.useLines { lines ->
        lines.forEach { line ->
            if (line.isNotEmpty()) {
                LineType.values().forEach {
                    if (line.matches(Regex(myLineExpressions[it]!!))) {

                        if (it == LineType.FILE_LINE)
                        {
                            cd.points.putIfAbsent(myLineExpressions[it]!!.toRegex().matchEntire(line)!!.groupValues[2],
                            Point(cd, HashMap(), myLineExpressions[it]!!.toRegex().matchEntire(line)!!.groupValues[2],
                                myLineExpressions[it]!!.toRegex().matchEntire(line)!!.groupValues[1].toLong(),
                                PointType.FILE))

                            val d: Point = cd
                            cd.size += myLineExpressions[it]!!.toRegex().matchEntire(line)!!.groupValues[1].toLong()
                            while (cd.parent != null)
                            {
                                cd = cd.parent!!
                                cd.size += myLineExpressions[it]!!.toRegex().matchEntire(line)!!.groupValues[1].toLong()
                            }
                            cd = d
                        }
                        else if (it == LineType.DIRECTORY_LINE)
                        {
                            cd.points.putIfAbsent(myLineExpressions[it]!!.toRegex().matchEntire(line)!!.groupValues[1],
                                Point(cd, HashMap(), myLineExpressions[it]!!.toRegex().matchEntire(line)!!.groupValues[1], 0, PointType.FOLDER))
                        }
                        else if(it == LineType.CHANGE_DIRECTORY)
                        {
                            val to = myLineExpressions[it]!!.toRegex().matchEntire(line)!!.groupValues[1]
                            if (".." == to && cd.parent != null) cd = cd.parent!!
                            else if ("/" == to ) cd = root
                            else if (cd.points.containsKey(myLineExpressions[it]!!.toRegex().matchEntire(line)!!.groupValues[1]))
                            {
                                cd = cd.points[myLineExpressions[it]!!.toRegex().matchEntire(line)!!.groupValues[1]]!!
                            }
                            else println("directory does not exist: ${myLineExpressions[it]!!.toRegex().matchEntire(line)!!.groupValues[1]}")
                        }
                    }
                }
            }
        }
    }
    println( "- ${root.name} (Total ${root.size})")
    printPointMap(root,"    ")

    println("Part 1: ${part1(root, PART1_FOLDER_SIZE_LIMIT)}")
    val p = part2(root)
    println("-----")
    println("Part 2: Free space ${PART2_TOTAL_DRIVE_SIZE - root.size}")
    println("Part 2: Needed space ${PART2_PATCH_SIZE - (PART2_TOTAL_DRIVE_SIZE - root.size)}")
    println("Part 2: Identified directory $p")
}
fun printPointMap(point: Point, indent: String)
{
    point.points.forEach{
        println( "$indent - ${it.value.name}  ${if (it.value.type == PointType.FOLDER)  "(Total ${it.value.size})" else it.value.size}")
        if (it.value.type == PointType.FOLDER)
        {
            printPointMap(it.value, "$indent    ")
        }
    }
}

fun part1(point: Point, limit: Long): Long
{
    var total = 0L
    point.points.forEach{
        if (it.value.type == PointType.FOLDER)
        {
            if (it.value.size < PART1_FOLDER_SIZE_LIMIT)
            {
                total += it.value.size
            }
            total += part1(it.value, limit)
        }
    }
    return total
}

fun part2(point: Point): Point
{
    var smallestDir = point
    point.points.forEach{
        if (it.value.type == PointType.FOLDER)
        {
            if (PART2_PATCH_SIZE - (PART2_TOTAL_DRIVE_SIZE - root.size) - it.value.size <= 0)
            {
                val p = part2(it.value)
                smallestDir = if (p.size < smallestDir.size) p else smallestDir
            }
        }
    }
    return smallestDir
}