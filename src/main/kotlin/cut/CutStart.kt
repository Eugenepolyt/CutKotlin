package cut

import org.kohsuke.args4j.Argument
import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import java.io.IOException

class CutStart {

    @Option(name = "-c", metaVar = "CutSymbols", forbids = ["-w"],  required = false, usage = "Cut number of symbols")
    private var c: Boolean = false

    @Option(name = "-w", metaVar = "CutWord", forbids = ["-c"], required = false, usage = "Cut number of words")
    private var w: Boolean = false

    @Option(name = "-o", metaVar = "OutputFile",  required = false, usage = "OutputFile")
    private var oFile: String = ""

    @Argument(metaVar = "InputFile", required = false, usage = "InputFile", index = 0)
    private var iFile: String = ""

    @Option(name = "-r", metaVar = "OutputFile",  required = true, usage = "OutputFile")
    private var range: String = ""

    fun start(args : Array<String>){
        val parser = CmdLineParser(this)
        try {
            parser.parseArgument(*args)
        } catch (e: CmdLineException) {
            println(e.message)
            println("cut [-c|-w] [-o ofile] [file] range")
            parser.printUsage(System.out)
            return
        }
        if (!c && !w) throw IllegalArgumentException()

        try {
            Cut(c, w, oFile, iFile, range).reader()
        } catch (e: IOException) {
            println(e.message)
            return
        }
    }
}

fun main(args : Array<String>){
    CutStart().start(args)
}