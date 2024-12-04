package day4

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class CeresSearchTest {

    @Test
    fun `solves day 41`() {
        val result = solveDay41("/day4.txt")
        val expected = 18
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 42`() {
        val result = solveDay42("/day4.txt")
        val expected = 9
        assertEquals(expected, result)
    }
}
