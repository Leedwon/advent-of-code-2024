package day5

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PrintQueueTest {

    @Test
    fun `solves day 51`() {
        val result = solveDay51("/day5.txt")
        val expected = 143
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 52`() {
        val result = solveDay52("/day5.txt")
        val expected = 123
        assertEquals(expected, result)
    }
}
