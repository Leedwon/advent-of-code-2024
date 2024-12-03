package day3

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MullItOverTest {

    @Test
    fun `solves day 31`() {
        val result = solveDay31("/day3.txt")
        val expected = 161
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 32`() {
        val result = solveDay32("/day3_2.txt")
        val expected = 48
        assertEquals(expected, result)
    }
}