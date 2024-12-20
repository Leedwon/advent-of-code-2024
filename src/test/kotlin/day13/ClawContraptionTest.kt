package day13

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ClawContraptionTest {

    @Test
    fun `solves day 131`() {
        val result = solveDay131("/day13.txt")
        val expected = 480L
        assertEquals(expected, result)
    }
}
