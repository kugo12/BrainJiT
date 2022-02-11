package jit

import VMState
import extensions.createClass
import extensions.createMethod
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor

interface Program {
    operator fun invoke(state: VMState)

    companion object {
        fun create(func: MethodVisitor.() -> Unit): Program =
            ClassWriter(ClassWriter.COMPUTE_FRAMES).run {
                createClass("pl/kvgx12/CompiledProgram", interfaces = arrayOf(getInternalName<Program>()))

                createMethod("<init>", "()V") {
                    aload(0)
                    invokeConstructor<Any>()
                    Return()
                    visitMaxs(0, 0)
                }

                createMethod(Program::invoke.name, Program::invoke.getDescriptor()) {
                    func()
                    Return()
                    visitMaxs(0, 0)
                }
                visitEnd()

                Loader.loadClass<Program>(toByteArray())
                    .getDeclaredConstructor()
                    .newInstance()
            }
    }
}

fun Program.run() = invoke(VMState())
