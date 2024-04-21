package com.x12q.randomizer.randomizer.class_randomizer

import com.x12q.randomizer.randomizer.RDClassData
import com.x12q.randomizer.randomizer.TestObjects
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Assertions.*
import kotlin.test.*

class ClassRandomizerUtilsTest{
    @Test
    fun randomizer(){

        fun condition (dt:RDClassData) : Boolean{
            return dt.kClass == Int::class
        }

        fun makeRandomIfApplicable():Int{
            return 123
        }

        val rdm = randomizer<Int>(
            condition = ::condition,
            makeRandomIfApplicable = ::makeRandomIfApplicable
        )

        rdm.isApplicable(RDClassData.from<TestObjects.Class1>()) shouldBe condition(RDClassData.from<TestObjects.Class1>())
        rdm.isApplicable(RDClassData.from<Int>()) shouldBe condition(RDClassData.from<Int>())

        rdm.random() shouldBe makeRandomIfApplicable()

    }
}
