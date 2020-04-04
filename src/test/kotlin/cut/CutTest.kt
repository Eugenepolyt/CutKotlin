package cut

import org.junit.Assert.*
import org.junit.Test
import  java.io.File

class CutTest {
    @Test
    fun wordPartTest() {
        val file = File("words.txt")
        file.bufferedWriter().use {
            it.write("There is some text")
            it.newLine()
            it.write("And some more text")
            it.newLine()
            it.write("word")
        }

        val fileAccept = File("accept.txt")
        fileAccept.bufferedWriter().use {
            it.write("There is")
            it.newLine()
            it.write("And some")
            it.newLine()
            it.write("word")
        }

        CutStart().start(arrayOf("-w", "-o", "text.txt", "words.txt", "-r", "-2"))
        assertEquals(
            File("accept.txt").readText(), File("text.txt").readText()
        )

        fileAccept.bufferedWriter().use {
            it.write("some text")
            it.newLine()
            it.write("more text")
            it.newLine()
        }

        CutStart().start(arrayOf("-w", "-o", "text.txt", "words.txt", "-r", "3-"))
        assertEquals(
            File("accept.txt").readText(), File("text.txt").readText()
        )

        fileAccept.bufferedWriter().use {
            it.write("There is some")
            it.newLine()
            it.write("And some more")
            it.newLine()
            it.write("word")
        }

        CutStart().start(arrayOf("-w", "-o", "text.txt", "words.txt", "-r", "1-3"))
        assertEquals(
            File("accept.txt").readText(), File("text.txt").readText()
        )

        File("words.txt").delete()
        File("text.txt").delete()
        File("accept.txt").delete()
    }

    @Test
    fun symbolPartTest() {
        val file = File("words.txt")
        file.bufferedWriter().use {
            it.write("---Symbols---")
            it.newLine()
            it.write("...TEST...")
            it.newLine()
            it.write("He--LLo")
        }

        val fileAccept = File("accept.txt")
        fileAccept.bufferedWriter().use {
            it.write("---Symbols")
            it.newLine()
            it.write("...TEST...")
            it.newLine()
            it.write("He--LLo")
        }

        CutStart().start(arrayOf("-c", "-o", "text.txt", "words.txt", "-r", "-10"))
        assertEquals(
            File("accept.txt").readText(), File("text.txt").readText()
        )

        fileAccept.bufferedWriter().use {
            it.write("-Symbols---")
            it.newLine()
            it.write(".TEST...")
            it.newLine()
            it.write("--LLo")
        }

        CutStart().start(arrayOf("-c", "-o", "text.txt", "words.txt", "-r", "3-"))
        assertEquals(
            File("accept.txt").readText(), File("text.txt").readText()
        )

        fileAccept.bufferedWriter().use {
            it.write("-S")
            it.newLine()
            it.write(".T")
            it.newLine()
            it.write("--")
        }
        CutStart().start(arrayOf("-c", "-o", "text.txt", "words.txt", "-r", "3-4"))
        assertEquals(
            File("accept.txt").readText(), File("text.txt").readText()
        )

        File("words.txt").delete()
        File("text.txt").delete()
        File("accept.txt").delete()
    }
}