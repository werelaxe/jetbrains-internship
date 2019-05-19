package internship

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import javax.mail.Part
import javax.mail.internet.ContentType
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart


fun recursiveExtractTextContent(element: Element): String {
    return if (element.children().isEmpty()) {
        element.text()
    } else {
        val childrenTexts = mutableListOf<String>()
        for (child in element.children()) {
            childrenTexts.add(recursiveExtractTextContent(child))
        }
        childrenTexts.joinToString(" ")
    }
}

fun extractContentFromHtml(html: String): String {
    val body= Jsoup.parse(html).body()
    return recursiveExtractTextContent(body)
}

fun recursiveExtractTextContent(part: Part): String {
    val baseType = ContentType(part.contentType).baseType
    return when {
        baseType == "text/plain" -> part.content as String
        baseType == "text/html" -> extractContentFromHtml(part.content as String)
        baseType.contains("multipart") -> {
            val contentParts = mutableListOf<String>()
            val body = part.content as MimeMultipart
            for (j in 0 until body.count) {
                val subPart = body.getBodyPart(j)
                contentParts.add(recursiveExtractTextContent(subPart))
            }
            contentParts.joinToString("\n")
        }
        else -> ""
    }
}

fun extractText(mimeMessage: MimeMessage): String {
    return recursiveExtractTextContent(mimeMessage)
}
