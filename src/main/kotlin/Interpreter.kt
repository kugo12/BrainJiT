import java.util.*


class InvalidProgramException : Throwable()

class Interpreter(
    program: String,
): VMState() {
    val program = program.filter { "-+<>,.[]".contains(it) }
    val link = ArrayDeque<Int>()

    fun run() {
        while (pc < program.length) step()
    }

    fun step() {
        when (program[pc]) {
            '>' -> ++pointer
            '<' -> --pointer
            '+' -> ++tape[pointer]
            '-' -> --tape[pointer]
            '.' -> print(tape[pointer].toInt().toChar())
            ',' -> tape[pointer] = input.nextByte()
            '[' -> if (tape[pointer] == 0.toByte()) {
                pc = findMatchingBracket()
            } else {
                link.addLast(pc)
            }
            ']' -> if (tape[pointer] != 0.toByte()) {
                pc = link.removeLast()
                return
            } else {
                link.removeLast()
            }
        }

        pc++
    }

    private fun findMatchingBracket(): Int {
        var at = pc + 1
        var nesting = 0

        while (at < program.length) {
            when (program[at]) {
                '[' -> nesting++
                ']' -> if (nesting == 0) return at else --nesting
            }
            at++
        }

        throw InvalidProgramException()
    }
}