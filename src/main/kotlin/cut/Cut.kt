package cut

import java.io.File
import java.io.IOException

class Cut (private val c : Boolean, private val w : Boolean, private val oFile : String,
           private val iFile : String, private val range : String) {

    @Throws(IOException::class)


    fun reader(){
        val text : Iterator<String> = if (iFile == ""){
            println("Enter text : ")
            System.`in`.bufferedReader().lineSequence().iterator()
        } else{
            File(iFile).bufferedReader().lineSequence().iterator()
        }


        if (oFile == "") {

            while (w && text.hasNext()) {
                println(cutW(text.next()))
            }

            while (c && text.hasNext()) {
                println(cutC(text.next()))
            }
        } else {
            var skipFirstSpace = true
            File(oFile).bufferedWriter().use {
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


    }

    private fun cutW (entry: String): String {
        val lineList = mutableListOf<String>()
        Regex("""[^ ]+""").findAll(entry).forEach { lineList.add(it.value) }
        if (range.matches(Regex("""-\d+"""))) {
            val k = range.drop(1).toInt()
            return if (k >= lineList.size) {
                lineList.joinToString(separator = " ")
            } else {
                lineList.slice(0 until k).joinToString(separator = " ")
            }

        } else if (range.matches(Regex("""\d+-"""))) {
            val n = range.dropLast(1).toInt()
            return if (n >= lineList.size) {
                ""
            } else {
                lineList.slice(n - 1 until lineList.size).joinToString(separator = " ")
            }

        } else {
            val listOfStartEnd = mutableListOf<Int>()
            Regex("""\d+""").findAll(range).forEach { listOfStartEnd.add(it.value.toInt()) }
            return if (listOfStartEnd[1] >= lineList.size) {
                lineList.slice((listOfStartEnd[0] - 1) until lineList.size).joinToString(separator = " ")

            } else lineList.slice(
                (listOfStartEnd[0] - 1) until listOfStartEnd[1]
            ).joinToString(separator = " ")

        }

    }

    private fun cutC (entry: String): String {
        if (range.matches(Regex("""-\d+"""))) {
            val k = range.drop(1).toInt()
            return if (k >= entry.length) {
                entry
            } else entry.slice(0 until k)
        } else if (range.matches(Regex("""\d+-"""))) {
            val n = range.dropLast(1).toInt()
            return if (n >= entry.length) {
                ""
            } else entry.slice((n - 1) until entry.length)
        } else {
            val listOfStartEnd = mutableListOf<Int>()
            Regex("""\d+""").findAll(range).forEach { listOfStartEnd.add(it.value.toInt()) }
            return if (listOfStartEnd[1] >= entry.length) {
                entry.slice(listOfStartEnd[0] until entry.length)
            } else entry.slice((listOfStartEnd[0] - 1) until listOfStartEnd[1])
        }
    }

}