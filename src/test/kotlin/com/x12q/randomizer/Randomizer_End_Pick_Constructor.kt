package com.x12q.randomizer

import com.x12q.randomizer.randomizer.ClassRandomizer
import com.x12q.randomizer.randomizer.RDClassData
import com.x12q.randomizer.test_util.TestSamples
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.jvm.internal.Ref.BooleanRef
import kotlin.reflect.KCallable
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaConstructor
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.reflect
import kotlin.test.BeforeTest
import kotlin.test.Test


class Randomizer_End_Pick_Constructor {

    lateinit var rdm: RandomizerEnd

    @BeforeTest
    fun bt() {
        rdm = TestSamples.comp.randomizer()
    }

    class A(val i: Int, val str: String, val l: List<Float>) {
        @Randomizable
        constructor(i: Int) : this(i, "str${i}", emptyList())
        constructor(str: String) : this(0, str, listOf(str.length.toFloat()))
    }


    @Test
    fun `pickConstructor on annotated constructor`() {
        val f = rdm.pickConstructor2(A::class)
        f.shouldNotBeNull()
        f.constructor shouldBe A::class.constructors.toList()[0]
    }

    class B(val i: Int, val str: String, val l: List<Float>) {
        constructor(i: Int) : this(i, "str${i}", emptyList())
        constructor(str: String) : this(0, str, listOf(str.length.toFloat()))
    }

    @Test
    fun `pickConstructor on not-annotated class`() {
        rdm.pickConstructor2(B::class)?.constructor shouldBe B::class.primaryConstructor
    }


    class C(val i: Int, val str: String, val b: Boolean) {
        @Randomizable
        constructor(i:Int):this(i,"str1",false)
        constructor(str:String):this(100,str,true)
    }

    @Test
    fun `pickConstructor with annotated constructor`() {
        rdm.pickConstructor2(C::class).also {
            it?.constructor shouldBe C::class.constructors.toList()[0]
            it?.randomizer shouldBe null
        }
    }

    class C2 @Randomizable constructor(val i: Int, val str: String, val b: Boolean) {
        constructor(i:Int):this(i,"str1",false)
        constructor(str:String):this(100,str,true)
    }

    @Test
    fun `pickConstructor with annotated primary constructor`() {
        rdm.pickConstructor2(C2::class).also {
            it?.constructor shouldBe C2::class.primaryConstructor
            it?.randomizer shouldBe null
        }
    }

    class C3 @Randomizable(randomizer = R1::class) constructor(val i: Int, val str: String, val b: Boolean) {
        constructor(i:Int):this(i,"str1",false)
        constructor(str:String):this(100,str,true)
    }

    @Test
    fun `pickConstructor with wrong randomizer on primary constructor`() {
        shouldThrow<Throwable> {
            rdm.pickConstructor2(C3::class)
        }
    }

    class C4 constructor(val i: Int, val str: String, val b: Boolean) {
        constructor(i:Int):this(i,"str1",false)
        @Randomizable(randomizer = R1::class)
        constructor(str:String):this(100,str,true)
    }

    @Test
    fun `pickConstructor with wrong randomizer on secondary constructor`() {
        shouldThrow<Throwable> {
            rdm.pickConstructor2(C4::class)
        }
    }

    abstract class R0: ClassRandomizer<Int> {
        override val returnedInstanceData: RDClassData
            get() = TODO("Not yet implemented")

        override fun isApplicableTo(classData: RDClassData): Boolean {
            TODO("Not yet implemented")
        }

        override fun random(): Int {
            TODO("Not yet implemented")
        }
    }

    class R1: R0()


}
