import jit.KiB
import java.util.*

open class VMState {
    @JvmField
    val tape: ByteArray = ByteArray(64 * KiB)

    @JvmField
    var pointer = 0

    @JvmField
    var pc = 0

    @JvmField
    val input = Scanner(System.`in`)
}