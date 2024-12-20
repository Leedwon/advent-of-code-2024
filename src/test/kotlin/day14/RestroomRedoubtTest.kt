package day14

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RestroomRedoubtTest {

    @Test
    fun `solves day 141`() {
        val result = solveDay141("/day14.txt", width = 11, height = 7)
        val expected = 12
        assertEquals(expected, result)
    }
}
