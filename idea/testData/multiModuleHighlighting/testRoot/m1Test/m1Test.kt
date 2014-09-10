package m1.test

import m1.*

fun main(args: Array<String>) {
    <error descr="[INVISIBLE_MEMBER] Cannot access 'privateInM1': it is 'private' in 'm1'">privateInM1</error>()
    internalInM1()
    publicInM1()
}