package com.stanger.listquest

import io.mockk.impl.annotations.MockK
import org.junit.Test

import org.junit.Assert.*

/**
 * stanger local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class stangerUnitTest {
    @MockK
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}
