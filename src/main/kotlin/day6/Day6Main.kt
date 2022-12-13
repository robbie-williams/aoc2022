package day6

import java.io.File
import java.util.*

val part1WindowSize = 4
val part2WindowSize = 14
fun main() {
    val reader = File("scratch_folder/day6-input.txt").reader()
    val queue: Queue<Char> = LinkedList<Char>()

    var stringIndex = 0
    reader.use { inputStreamReader ->
        loop@ while (true) {

            val c = inputStreamReader.read()
            stringIndex++

            if (c == -1 || '\n'.code == c || queue.size >= part2WindowSize) {
                println("Signal starts after character ${stringIndex-1}")
                stringIndex = 0
                queue.clear()
                do { //read to EOL/EOF
                    val nextC = inputStreamReader.read()
                    if (nextC == -1)
                        break@loop
                    if (nextC == '\n'.code)
                        continue@loop
                } while (true)
            }
            while (queue.contains(Char(c)))
            {
                queue.remove()
            }
            queue.add(Char(c))
        }
    }
}
