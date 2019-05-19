package internship

import kotlin.test.Test
import kotlin.test.assertEquals

val EXPECTED_TEXT_RANGE = TextRange(5, 26)

class MarkerFinderTest {
    @Test fun testFinding() {
        val actualTextRange = findMarkerIgnoringSpace("Text [1299bba / 0 0 0 0 1] from David", "[1299 bba/00001]")
        assertEquals(EXPECTED_TEXT_RANGE, actualTextRange)
    }
}
