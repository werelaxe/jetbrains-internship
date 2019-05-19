package internship

data class TextRange(val start: Int, val endExclusive: Int)

class Node(
        val next: MutableMap<Char, Node>,
        var substring: String?,
        val parent: Node?,
        val parentChar: Char,
        var link: Node?,
        val go: MutableMap<Char, Node>
)

class Trie(
    val callback: (TextRange) -> Unit
) {
    private val root: Node = Node(HashMap(), null, null, '0', null, HashMap())

    private fun getLink(node: Node): Node {
        if (node.link == null) {
            if (node == root || node.parent == root) {
                node.link = root
            } else {
                node.link = go(getLink(node.parent!!), node.parentChar)
            }
        }
        return node.link!!
    }

    private fun go(node: Node, c: Char): Node {
        if (node.go[c] == null) {
            if (node.next[c] != null) {
                node.go[c] = node.next[c]!!
            } else {
                node.go[c] = if (node == root) root else go(getLink(node), c)
            }
        }
        return node.go[c]!!
    }

    fun addString(source: String) {
        var node = root
        for (i in 0 until source.length) {
            val c = source[i]
            if (c.isWhitespace()) {
                continue
            }
            if (node.next[c] == null) {
                val newNode = Node(HashMap(), null, node, c, null, HashMap())
                node.next[c] = newNode
            }
            node = node.next[c]!!
        }
        node.substring = source
    }

    fun feed(source: String) {
        var currentNode = root
        for (charIndex in 0 until source.length) {
            val c = source[charIndex]
            if (c.isWhitespace()) {
                continue
            }
            currentNode = go(currentNode, c)
            if (currentNode.substring != null) {
                val substring = currentNode.substring!!
                val end = charIndex + 1
                val nonWhitespaceChars = substring.filterNot { it.isWhitespace() }.length
                var start = end - 1
                var skippedChars = 0
                while (skippedChars != nonWhitespaceChars) {
                    if (!source[start].isWhitespace()) {
                        ++skippedChars
                    }
                    --start
                }
                callback(TextRange(start + 1, end))
            }
        }
    }
}

fun findMarkerIgnoringSpace(text: String, marker: String): TextRange? {
    val result = mutableListOf<TextRange>()
    val trie = Trie { result.add(it) }
    trie.addString(marker)
    trie.feed(text)
    return result.getOrNull(0)
}
