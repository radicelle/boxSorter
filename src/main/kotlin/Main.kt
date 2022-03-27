fun main() {
    val shortString = "163841689525773"
    val longString = "1638416869846446345364863484364845643543243684864979878468345132486549525773"
    val veryLongString = (1..100000).map { kotlin.random.Random.nextInt(1, 9) }.joinToString("")

    val resultEasy: String = shortString.simpleAlgo()
    val resultSwapping: String = shortString.swapping()
    println("Classic algo : $resultEasy -> performance = ${resultEasy.performance()}")
    println("Advanced algo : $resultSwapping -> performance = ${resultSwapping.performance()}")
    println("Advanced algo : $resultSwapping -> performance = ${resultSwapping.performance()}")

    val result2Easy: String = longString.simpleAlgo()
    val result2Swapping: String = longString.swapping()
    println("Classic algo : $result2Easy -> performance = ${result2Easy.performance()}")
    println("Advanced algo : $result2Swapping -> performance = ${result2Swapping.performance()}")

    val result3Easy: String = veryLongString.simpleAlgo()
    val result3Swapping: String = veryLongString.swapping()

    val perfEasy : Double = result3Easy.performance().toDouble()
    val perfComplex : Double = result3Swapping.performance().toDouble()
    println("performance easy = $perfEasy")
    println("performance complex = $perfComplex")
    println("Gain = ${(perfEasy/perfComplex*100)-100}")
}

/**
 * "163/55/336 -> 16355336.length"
 */
private fun String.numberItems(): Int {
    return this.split("/").joinToString("").length
}


/**
 * The complex algorithm is using the core algorithm and takes the 2 by 2 algorithm output and keeps the boxes with the highest numbers
 * descending. Then repeating the core algorithm from 10, max size to 1 min size
 * It uses the simple algorithm at each iteration to group the remaining values
 */
private fun String.swapping(): String {
    val finalList = mutableListOf<String>()
    val functionScopeList =
        complexAlgoCore().split("/").map { string -> string to string.asSequence().sumOf { it.digitToInt() } }
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
        if (remainingItemsList.isEmpty()) break
        functionScopeList.clear()
        functionScopeList.addAll(
            remainingItemsList.joinToString("") { it.first }.simpleAlgo().split("/")
                .map { string -> string to string.asSequence().sumOf { it.digitToInt() } }
                .sortedByDescending { it.second }.toList()
        )
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
