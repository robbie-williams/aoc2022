package day5

fun printDock(dock: Dock)
{
    var maxHeight = 0
    dock.loadingBays.forEachIndexed() { i, loadingBay ->
        if (maxHeight < loadingBay.crates.size)
        {
            maxHeight = loadingBay.crates.size
        }
    }

    println("\n-----Dock---")
    for (currentHeight in maxHeight downTo 1 ) {
        dock.loadingBays.forEach { loadingBay ->
            if (currentHeight <= loadingBay.crates.size) {
                print(loadingBay.crates[currentHeight-1].crateLabel)
            } else {
                print("...")
            }
            print(".")
        }
        println()
    }
    for (i in 1..dock.loadingBays.size)
        print(".${i}..")
    println("\n-----End of Dock---")
}