package com.x12q.randomizer.randomizer

object TestObjects {
    data class Class1(val lst: List<Float>, val tm12: String)
    data class Class2(val a: Class1, val t: String)
    data class ClassWithGeneric<T>(val t: T)
}
