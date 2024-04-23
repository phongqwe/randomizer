package com.x12q.randomizer.annotation_processor.clazz

import com.x12q.randomizer.randomizer.class_randomizer.ClassRandomizer
import kotlin.reflect.KClass

/**
 * Reasons for invalid class randomizer
 */
sealed class InvalidClassRandomizerReason {
    abstract val rmdClass: KClass<out ClassRandomizer<*>>

    /**
     * When the checked class is an abstract class
     */
    data class IsAbstract(
        override val rmdClass: KClass<out ClassRandomizer<*>>
    ) : InvalidClassRandomizerReason()

    /**
     * When the checked class is a [ClassRandomizer] but cannot generate random instance of [expectedType]
     */
    data class WrongTargetType(
        override val rmdClass: KClass<out ClassRandomizer<*>>,
        val actualTypes: KClass<*>?,
        val expectedType: KClass<*>,
    ) : InvalidClassRandomizerReason()

    /**
     * When the checked class is not of type [ClassRandomizer]
     */
    data class IllegalClass(
        override val rmdClass: KClass<out ClassRandomizer<*>>,
    ) : InvalidClassRandomizerReason()
}