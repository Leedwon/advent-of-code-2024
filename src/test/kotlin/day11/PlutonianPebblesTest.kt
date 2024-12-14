package day11

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PlutonianPebblesTest {

    @Test
    fun `solves day 111`() {
        val result = solveDay111("/day11.txt")
        val expected = 55312L
        assertEquals(expected, result)
    }
}
