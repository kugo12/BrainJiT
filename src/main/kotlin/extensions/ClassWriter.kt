@file:Suppress("NOTHING_TO_INLINE")

package extensions

import jit.getInternalName
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

inline fun ClassWriter.createClass(
    name: String,
    version: Int = Opcodes.V17,
    access: Int = Opcodes.ACC_PUBLIC,
    signature: String? = null,
    superName: String? = getInternalName<Any>(),
    interfaces: Array<String>? = null
) {
    visit(version, access, name, signature, superName, interfaces)
}

inline fun ClassWriter.createMethod(
    name: String,
    descriptor: String,
    access: Int = Opcodes.ACC_PUBLIC,
    signature: String? = null,
    exceptions: Array<String>? = null,
    func: MethodVisitor.() -> Unit
) {
    visitMethod(access, name, descriptor, signature, exceptions).apply {
        visitCode()
        func()
        visitEnd()
    }
}