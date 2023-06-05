package org.like.a.wordtrie
/*
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import org.ardverk.collection.Trie
import java.io.*
import java.nio.file.Paths

val path = Paths.get("src/LikeAFly/wordtrie/src/main/java/org/like/a/wordtrie/").toAbsolutePath().toString()


path




val file: File = File(path+"/" +
        "key_dict.csv")
file
val rows: List<List<String>> = csvReader().readAll(file)
var m = TrieMap<String, String>()

var i:Int =0
for (row in rows) {
    val uk = row[0]
    val en = row[1]
    println("$uk   $en")


    m[row[0]] = row[1]
    m[row[1]] = row[0]




    if (i >10) {
        break
    }
}


val expected = m.readOnlySnapshot()

val bos: ByteArrayOutputStream = ByteArrayOutputStream()
val oos: ObjectOutputStream = ObjectOutputStream(bos)
oos.writeObject(expected);
oos.close();

val bytes = bos.toByteArray()
val bis: ByteArrayInputStream = ByteArrayInputStream(bytes)
val ois: ObjectInputStream = ObjectInputStream(bis)
val actual = ois.readObject() as TrieMap<String, String>

ois.close();
*/