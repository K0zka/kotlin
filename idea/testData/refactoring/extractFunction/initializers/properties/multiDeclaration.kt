data class Pair<A, B>(val a: A, val b: B)
fun <A, B> A.to(b: B) = Pair(this, b)

// SIBLING:
fun foo() {
    val (a, b) = <selection>1 to 2</selection>
}