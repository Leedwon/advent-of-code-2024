package day6

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GuardGallivantTest {

    @Test
    fun `solves day 61`() {
        val result = solveDay61("/day6.txt")
        val expected = 41
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 62`() {
        val result = solveDay62("/day6.txt")
        val expected = 6
        assertEquals(expected, result)
    }
}
