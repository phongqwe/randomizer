package com.x12q.randomizer.randomizer.class_randomizer

import com.x12q.randomizer.randomizer.RDClassData

/**
 * Create a [ClassRandomizer] from a factory function ([makeRandom])
 */
inline fun <reified T> randomizer(crossinline makeRandom: () -> T): ClassRandomizer<T> {
    val q = object : ClassRandomizer<T> {
        override val paramClassData: RDClassData = RDClassData.from<T>()
        override fun random(): T {
            return makeRandom()
        }
    }
    return q
}
fun basicRandomizers(): Collection<ClassRandomizer<*>> {
    TODO()
}
