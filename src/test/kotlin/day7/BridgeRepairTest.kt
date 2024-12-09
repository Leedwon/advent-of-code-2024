package day7

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BridgeRepairTest {

    @Test
    fun `solves day 71`() {
        val result = solveDay71("/day7.txt")
        val expected = 3749L
        assertEquals(expected, result)
    }

    @Test
    fun `solves day 72`() {
        val result = solveDay72("/day7.txt")
        val expected = 11387L
        assertEquals(expected, result)
    }
}