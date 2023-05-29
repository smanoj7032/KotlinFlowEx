package com.example.koltinflowex.presentation

fun main() {
    val name =
        "ParagraphParagraphs are the group of sentences combined together, about a certain topic. It is a very important form of writing as we write almost everything in paragraphs, be it an answer, essay, story, emails, etc. We can say that a well-structured paragraph is the essence of good writing. The purposes of the paragraph are to give information, to explain something, to tell a story, and to convince someone that our idea is right".replace(
            " ", ""
        ).toLowerCase()
    same(name)

}

fun same(name: String) {
    val char = name.toCharArray()
    var list = char.toList()
    for (i in list) {
        var count = 0
        for (j in list) {
            if (i == j) {
                list = list.minus(i)
                count++
            }
        }
        if (count > 1) println("$i=$count")
    }
}

fun checkDuplicates(name: String) {
    val map = HashMap<String, Int>()
    for (i in name.indices) {
        if (map.contains(name[i].toString())) {
            map[name[i].toString()] = map[name[i].toString()]!!.toInt() + 1
        } else {
            map[name[i].toString()] = 1
        }
    }
    for (i in 0 until map.size) {
        if (map[name[i].toString()]!!.toInt() > 1) {

            println("${map.keys.toList()[i]} == ${map.getOrDefault(name[i].toString(), -1)}")
        }
    }
// println(map.entries)
}


