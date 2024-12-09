package day8

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ResonantCollinearityTest {

    @Test
    fun `solves day 81`() {
        val result = solveDay81("/day8.txt")
        val expected = 14
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 82`() {
        val result = solveDay82("/day8.txt")
        val expected = 34
        assertEquals(expected, result)
    }
}
