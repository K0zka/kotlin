package foo

// NOTE THIS FILE IS AUTO-GENERATED by the generateTestDataForReservedWords.kt. DO NOT EDIT!

enum class Foo {
    BAR;
    fun `try`() { `try`() }

    fun test() {
        testNotRenamed("try", { `try`() })
    }
}

fun box(): String {
    Foo.BAR.test()

    return "OK"
}