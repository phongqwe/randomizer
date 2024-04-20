package com.x12q.randomizer.randomizer.class_randomizer

import com.x12q.randomizer.randomizer.RDClassData
import com.x12q.randomizer.randomizer.WithRDClassData

/**
 * Can generate a random instance of some type [T].
 */
interface ClassRandomizer<T>: WithRDClassData {
    override val paramClassData: RDClassData
    fun random(): T
}
