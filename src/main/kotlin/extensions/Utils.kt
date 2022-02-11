@file:Suppress("NOTHING_TO_INLINE")

package jit

import org.objectweb.asm.Type
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.jvmErasure

const val KiB = 1024

inline fun <reified T> getInternalName(): String = Type.getInternalName(T::class.java)

inline val KClass<*>.primitiveOrObject get() = javaPrimitiveType ?: javaObjectType
inline fun KClass<*>.getDescriptorString(): String = if (isInstance(Unit)) "V" else primitiveOrObject.descriptorString()

inline fun KFunction<*>.getDescriptor(): String {
    val params = valueParameters
        .joinToString("") { it.type.jvmErasure.getDescriptorString() }
    val ret = returnType.jvmErasure.getDescriptorString()

    return "($params)$ret"
}

