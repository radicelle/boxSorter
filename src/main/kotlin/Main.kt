fun main() {
    val chaine = "163841689525773"
    val longchaine = "1638416869846446345364863484364845643543243684864979878468345132486549525773"

    val resultEasy: String = chaine.simpleAlgo()
    val resultAdvanced: String = chaine.complexAlgo()
    println("Classic algo : $resultEasy -> performance = ${resultEasy.performance()}")
    println("Advanced algo : $resultAdvanced -> performance = ${resultAdvanced.performance()}")

    val result2Easy: String = longchaine.simpleAlgo()
    val result2Advanced: String = longchaine.complexAlgo()
    println("Classic algo : $result2Easy -> performance = ${result2Easy.performance()}")
    println("Advanced algo : $result2Advanced -> performance = ${result2Advanced.performance()}")
}


/**
 * The complex algorithm is using the core algorithm and takes the 2 by 2 algorithm output and keeps the boxes with the highest numbers
 * descending. Then repeating the core algorithm from 10, max size to 1 min size
 * It uses the simple algorithm at each iteration to group the remaining values
 */
private fun String.complexAlgo(): String {
    val finalList = mutableListOf<String>()
    val functionScopeList = complexAlgoCore().split("/").map { string -> string to string.asSequence().sumOf { it.digitToInt() } }
        .sortedByDescending { it.second }.toMutableList()
    for (boxValue in 10 downTo 1) {
        val indicesToRemove = mutableListOf<Int>()
        var index = 0
        while (index < functionScopeList.size && functionScopeList[index].second == boxValue) {
            finalList.add(functionScopeList[index].first)
            indicesToRemove.add(index)
            index++
        }
        val remainingItemsList = mutableListOf<Pair<String, Int>>()
        val iterator = functionScopeList.iterator()
        var removalIndex = 0
        while (iterator.hasNext()) {
            val nextPair = iterator.next()
            if (!indicesToRemove.contains(removalIndex)) {
                remainingItemsList.add(nextPair)
            }
            removalIndex++
        }
        if(remainingItemsList.isEmpty()) break
        functionScopeList.clear()
        functionScopeList.addAll(
            remainingItemsList.joinToString("") { it.first }.simpleAlgo().split("/")
            .map { string -> string to string.asSequence().sumOf { it.digitToInt() } }
            .sortedByDescending { it.second }.toList())
    }
    finalList.addAll(functionScopeList.map { it.first })
    return finalList.joinToString("/")
}

/**
 * The core of the algorithm is to sort values ascending and then place ends 2 by 2
 * to create a list of couples big and little
 */
private fun String.complexAlgoCore(): String {
    val ascSortedList = this.asSequence().map { it.digitToInt() }.sorted().toList()
    var indexUp = ascSortedList.size - 1
    var indexDown = 0
    val indexMiddle = ascSortedList.size / 2
    val swapList = mutableListOf<Int>()
    while (indexDown < indexMiddle || indexUp >= indexMiddle) {
        if (indexUp == indexMiddle) {
            swapList.add(ascSortedList[indexMiddle])
            break
        }
        val tmp = ascSortedList[indexUp]
        swapList.add(tmp)
        swapList.add(ascSortedList[indexDown])
        indexDown++
        indexUp--

    }
    return swapList.joinToString("").simpleAlgo()
}

private fun String.performance(): Int = this.split("/").size

private fun String.simpleAlgo(): String {
    val sequence = StringBuilder()
    val currentSequence = StringBuilder()
    var value = 0
    this.asSequence().forEach {
        val currentValue = it.digitToInt()
        value += currentValue
        if (value > 10) {
            sequence.append("$currentSequence/")
            currentSequence.clear()
            value = currentValue
        }
        currentSequence.append(it)
    }
    sequence.append(currentSequence)
    return sequence.toString()
}
