package cut

import java.io.File
import java.io.IOException

class Cut (private val c : Boolean, private val w : Boolean, private val oFile : String,
           private val iFile : String, private val range : String) {

    @Throws(IOException::class)

    fun reader() {
        var entryList = mutableListOf<String>()
        if (iFile == "") {
            println("Enter 'EXIT' to stop typing")
            var currentLine = readLine() ?: ""
            while (currentLine != "EXIT") {
                entryList.add(currentLine)
                currentLine = readLine() ?: ""
            }
        } else {
            entryList = File(iFile).readLines().toMutableList()
        }
        cut(entryList)
    }

    private fun cut (entryList: List<String>) {
        val outList = mutableListOf<String>()
        if (w) {
            for (line in entryList) {
                val lineList = mutableListOf<String>()
                Regex("""[^ ]+""").findAll(line).forEach { lineList.add(it.value) }
                if (range.matches(Regex("""-\d+"""))) {
                    val k = range.drop(1).toInt()
                    if (k >= lineList.size) {
                        outList.add(lineList.joinToString(separator = " "))
                    } else {
                        outList.add(lineList.slice(0 until k).joinToString(separator = " "))
                    }

                } else if (range.matches(Regex("""\d+-"""))) {
                    val n = range.dropLast(1).toInt()
                    if (n >= lineList.size) {
                        outList.add("")
                    } else {
                        outList.add(lineList.slice(n - 1 until lineList.size).joinToString(separator = " "))
                    }

                } else {
                    val listOfStartEnd = mutableListOf<Int>()
                    Regex("""\d+""").findAll(range).forEach{ listOfStartEnd.add(it.value.toInt()) }
                    if (listOfStartEnd[1] >= lineList.size) {
                        outList.add(lineList.slice(
                            (listOfStartEnd[0] - 1) until lineList.size).joinToString(separator = " ")
                        )
                    } else outList.add(lineList.slice(
                        (listOfStartEnd[0] - 1) until listOfStartEnd[1]).joinToString(separator = " ")
                    )
                }
            }
        }

        if (c) {
            for (line in entryList) {
                if (range.matches(Regex("""-\d+"""))) {
                    val k = range.drop(1).toInt()
                    if (k >= line.length) {
                        outList.add(line)
                    } else outList.add(line.slice(0 until k))
                } else if (range.matches(Regex("""\d+-"""))) {
                    val n = range.dropLast(1).toInt()
                    if (n >= line.length) {
                        outList.add("")
                    } else outList.add(line.slice((n - 1) until line.length))
                } else {
                    val listOfStartEnd = mutableListOf<Int>()
                    Regex("""\d+""").findAll(range).forEach{listOfStartEnd.add(it.value.toInt())}
                    if (listOfStartEnd[1] >= line.length) {
                        outList.add(line.slice(listOfStartEnd[0] until line.length))
                    } else outList.add(line.slice((listOfStartEnd[0] - 1) until listOfStartEnd[1]))
                }
            }
        }

        if (oFile == "") {
            for (i in outList) {
                println(i)
            }
        } else {
            File(oFile).bufferedWriter().use {
                for (i in 0 until outList.size - 1) {
                    it.write(outList[i])
                    it.newLine()
                }
                it.write(outList.last())
            }
        }

    }


}