package jit

import extensions.createClass
import extensions.createMethod
import org.objectweb.asm.*

fun compile(program: String) = Program.create {
    localSetup()

    val stackPrevious = ArrayDeque<Label>(64)
    val stackNext = ArrayDeque<Label>(64)

    program.forEach { c ->
        when (c) {  
            '.' -> print()
            ',' -> inputChar()
            '+' -> addToCurrentCell(1)
            '-' -> addToCurrentCell(-1)
            '>' -> addToPointer(1)
            '<' -> addToPointer(-1)
            '[' -> {
                val previous = Label()
                val next = Label()

                jumpIfEq0(next, previous)

                stackNext.addLast(next)
                stackPrevious.addLast(previous)
            }
            ']' -> {
                jumpIfNe0(
                    stackPrevious.removeLast(),
                    stackNext.removeLast()
                )
            }
        }
    }

    assert(stackPrevious.size == 0 && stackNext.size == 0)
}
