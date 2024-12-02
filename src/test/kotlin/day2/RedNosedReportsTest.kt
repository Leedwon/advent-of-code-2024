package day2

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RedNosedReportsTest {

    @Test
    fun `solves day 21`() {
        val result = solveDay21("/day2.txt")
        val expected = 2
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 22`() {
        val result = solveDay22("/day2.txt")
        val expected = 5
        assertEquals(expected, result)
    }
}