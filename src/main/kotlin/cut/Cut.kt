package cut

import java.io.File
import java.io.IOException

class Cut (private val c : Boolean, private val w : Boolean, private val oFile : String,
           private val iFile : String, private val range : String) {

    private val outList = mutableListOf<String>()

    @Throws(IOException::class)


    fun start(){
        val text : Iterator<String> = if (iFile == ""){
            println("Enter text : ")
            System.`in`.bufferedReader().lineSequence().iterator()
        } else{
            File(iFile).bufferedReader().lineSequence().iterator()
        }


        while (w && text.hasNext()) {
            cutW(text.next())
        }

        while (c && text.hasNext()) {
            cutC(text.next())
        }

        writer(outList)

    }

    private fun cutW (entry: String) {
        val lineList = mutableListOf<String>()
        Regex("""[^ ]+""").findAll(entry).forEach { lineList.add(it.value) }
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
            Regex("""\d+""").findAll(range).forEach { listOfStartEnd.add(it.value.toInt()) }
            if (listOfStartEnd[1] >= lineList.size) {
                outList.add(
                    lineList.slice(
                        (listOfStartEnd[0] - 1) until lineList.size
                    ).joinToString(separator = " ")
                )
            } else outList.add(
                lineList.slice(
                    (listOfStartEnd[0] - 1) until listOfStartEnd[1]
                ).joinToString(separator = " ")
            )
        }

    }

    private fun cutC (entry: String) {
        if (range.matches(Regex("""-\d+"""))) {
            val k = range.drop(1).toInt()
            if (k >= entry.length) {
                outList.add(entry)
            } else outList.add(entry.slice(0 until k))
        } else if (range.matches(Regex("""\d+-"""))) {
            val n = range.dropLast(1).toInt()
            if (n >= entry.length) {
                outList.add("")
            } else outList.add(entry.slice((n - 1) until entry.length))
        } else {
            val listOfStartEnd = mutableListOf<Int>()
            Regex("""\d+""").findAll(range).forEach { listOfStartEnd.add(it.value.toInt()) }
            if (listOfStartEnd[1] >= entry.length) {
                outList.add(entry.slice(listOfStartEnd[0] until entry.length))
            } else outList.add(entry.slice((listOfStartEnd[0] - 1) until listOfStartEnd[1]))
        }
    }

private fun writer (outList: List<String>) {
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