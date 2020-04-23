package cut

import java.io.File
import java.io.IOException
import java.lang.IllegalArgumentException

class Cut (private val c : Boolean, private val w : Boolean, private val oFile : File,
           private val iFile : File, private var range : String) {

    private var parsedRange = -1 to -1

    @Throws(IOException::class)
    fun reader(){
        val text : Iterator<String> = iFile.bufferedReader().lineSequence().iterator()

        parseRange()

        var skipFirstSpace = true
        oFile.bufferedWriter().use {
            while (w && text.hasNext()) {
                if (skipFirstSpace) {
                    it.write(cutW(text.next()))
                    skipFirstSpace = false
                    continue
                }
                it.newLine()
                it.write(cutW(text.next()))
            }
            while (c && text.hasNext()) {
                if (skipFirstSpace) {
                    it.write(cutC(text.next()))
                    skipFirstSpace = false
                    continue
                }
                it.newLine()
                it.write(cutC(text.next()))
            }
        }


    }

    private fun cutW (entry: String): String {
        val lineList = mutableListOf<String>()
        Regex("""[^ ]+""").findAll(entry).forEach { lineList.add(it.value) }
        return when (range) {
            "1" -> if (parsedRange.first >= lineList.size) {
                        lineList.joinToString(separator = " ")
                    } else {
                        lineList.slice(0 until parsedRange.first).joinToString(separator = " ")
                    }

            "2" ->  if (parsedRange.first >= lineList.size) {
                        ""
                     } else {
                         lineList.slice(parsedRange.first - 1 until lineList.size).joinToString(separator = " ")
                     }

            "3" ->  if (parsedRange.second >= lineList.size) {
                        lineList.slice((parsedRange.first - 1) until lineList.size).joinToString(separator = " ")

                     } else lineList.slice((parsedRange.first - 1) until parsedRange.second)
                                                    .joinToString(separator = " ")
            else -> throw (IllegalArgumentException())
        }

    }

    private fun cutC (entry: String): String {
        return when (range) {
            "1" -> if (parsedRange.first >= entry.length) {
                        entry
                    } else entry.slice(0 until parsedRange.first)
            "2" ->  if (parsedRange.first >= entry.length) {
                         ""
                    } else entry.slice((parsedRange.first - 1) until entry.length)
            "3" -> if (parsedRange.second >= entry.length) {
                        entry.slice(parsedRange.first until entry.length)
                    } else entry.slice((parsedRange.first - 1) until parsedRange.second)

            else -> throw (IllegalArgumentException())
        }
    }

    private fun parseRange() {
        if (range.matches(Regex("""-\d+"""))) {
            parsedRange =  range.drop(1).toInt() to -1
            range = "1"
        } else if (range.matches(Regex("""\d+-"""))) {
            parsedRange = range.dropLast(1).toInt() to -1
            range = "2"

        } else {
            val listOfStartEnd = mutableListOf<Int>()
            Regex("""\d+""").findAll(range).forEach { listOfStartEnd.add(it.value.toInt()) }
            parsedRange = listOfStartEnd[0] to listOfStartEnd[1]
            range = "3"
        }

    }

}