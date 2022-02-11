package jit

import VMState
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import java.io.PrintStream
import java.util.*
import kotlin.reflect.KFunction


private const val STATE = 1
private const val PC = 2
private const val PTR = 3
private const val TAPE = 4
private const val OUT = 5
private const val IN = 6

fun <T : Function<*>> forceFnType(fn: T) = fn as KFunction<*>

private val printChar = forceFnType<PrintStream.(Char) -> Unit>(PrintStream::print)
private val nextByte = forceFnType<Scanner.() -> Byte>(Scanner::nextByte)

private fun MethodVisitor.loadCell() {
    aload(TAPE)
    iload(PTR)
    baload()
}

fun MethodVisitor.localSetup() {
    getFieldFrom(STATE, VMState::tape)
    astore(TAPE)

    getFieldFrom(STATE, VMState::pc)
    istore(PC)

    getFieldFrom(STATE, VMState::pointer)
    istore(PTR)

    getFieldFrom(STATE, VMState::input)
    astore(IN)

    getStatic(System::out)
    astore(OUT)
}

fun MethodVisitor.print() {
    aload(OUT)

    loadCell()
    invokeStatic(printChar)
}

fun MethodVisitor.addToPointer(n: Int) {
    iinc(PTR, n)
}

fun MethodVisitor.addToCurrentCell(n: Int) {
    aload(TAPE)
    iload(PTR)
    dup2()

    baload()
    sipush(n)

    iadd()
    bastore()
}

fun MethodVisitor.inputChar() {
    aload(TAPE)
    aload(PTR)

    aload(IN)
    invokeStatic(nextByte)

    bastore()
}

fun MethodVisitor.jumpIfEq0(to: Label, after: Label) {
    loadCell()
    jmpIfEq(to)
    visitLabel(after)
}

fun MethodVisitor.jumpIfNe0(to: Label, after: Label) {
    loadCell()
    jmpIfNe(to)
    visitLabel(after)
}