@file:Suppress("NOTHING_TO_INLINE")

package jit

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

inline fun MethodVisitor.aload(index: Int) = visitVarInsn(Opcodes.ALOAD, index)
inline fun MethodVisitor.iload(index: Int) = visitVarInsn(Opcodes.ILOAD, index)
inline fun MethodVisitor.istore(index: Int) = visitVarInsn(Opcodes.ISTORE, index)
inline fun MethodVisitor.astore(index: Int) = visitVarInsn(Opcodes.ASTORE, index)

inline fun <reified T, reified V> MethodVisitor.getField(f: KProperty1<T, V>) = visitFieldInsn(
    Opcodes.GETFIELD,
    getInternalName<T>(),
    f.name,
    V::class.getDescriptorString()
)

inline fun <reified T, reified V> MethodVisitor.getFieldFrom(index: Int, field: KProperty1<T, V>) {
    aload(index)
    getField(field)
}

inline fun <reified T> MethodVisitor.invokeConstructor() =
    visitMethodInsn(
        Opcodes.INVOKESPECIAL,
        getInternalName<T>(),
        "<init>",
        "()V",
        false
    )

inline fun MethodVisitor.invokeStatic(f: KFunction<*>) {
    val klazz = f.instanceParameter!!.type.jvmErasure.java

    visitMethodInsn(
        Opcodes.INVOKEVIRTUAL,
        Type.getInternalName(klazz),
        f.name,
        f.getDescriptor(),
        klazz.isInterface
    )
}

inline fun <reified T> MethodVisitor.getStatic(p: KProperty0<T>) {
    visitFieldInsn(
        Opcodes.GETSTATIC,
        Type.getInternalName(p.javaField!!.declaringClass),
        p.name,
        T::class.getDescriptorString()
    )
}

inline fun MethodVisitor.jmpIfNe(to: Label) = visitJumpInsn(Opcodes.IFNE, to)
inline fun MethodVisitor.jmpIfEq(to: Label) = visitJumpInsn(Opcodes.IFEQ, to)


inline fun MethodVisitor.sipush(n: Int) = visitIntInsn(Opcodes.SIPUSH, n)
inline fun MethodVisitor.baload() = visitInsn(Opcodes.BALOAD)
inline fun MethodVisitor.bastore() = visitInsn(Opcodes.BASTORE)

inline fun MethodVisitor.iinc(index: Int, n: Int) = visitIincInsn(index, n)
inline fun MethodVisitor.iadd() = visitInsn(Opcodes.IADD)
inline fun MethodVisitor.dup2() = visitInsn(Opcodes.DUP2)
inline fun MethodVisitor.Return() = visitInsn(Opcodes.RETURN)
