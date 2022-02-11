import jit.compile
import jit.run
import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val program = File(args[0]).readText()

    val interpreter = Interpreter(program)
    val x = measureTimeMillis { interpreter.run() }
    println("\nInterpreted took $x ms")

    val compiledProgram = compile(program)
    val y = measureTimeMillis { compiledProgram.run() }
    println("\nCompiled took $y ms")
}