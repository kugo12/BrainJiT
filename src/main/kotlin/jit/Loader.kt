package jit

object Loader: ClassLoader(getSystemClassLoader()) {
    fun <T> loadClass(bytecode: ByteArray): Class<T> =
        defineClass(null, bytecode, 0, bytecode.size) as Class<T>
}