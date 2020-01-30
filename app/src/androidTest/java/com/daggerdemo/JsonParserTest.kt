package com.daggerdemo

import androidx.test.filters.SmallTest
import org.junit.Test
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

/**
 * Created by Manish Patel on 1/30/2020.
 */
@SmallTest
class JsonParserTest {

    @Test
    fun parseTest() {
        val person = Person(name = "Manish Patel", height = 5.8f)
        println(Json.stringify(Person.serializer(), person))

        val personJson = "{\"id\":0,\"name\":\"Mdp3030\",\"height\":5.9}"
        println(Json.parse(Person.serializer(), personJson))

        val persons =
            listOf(Person(name = "Manish patel", height = 5.9f), Person(4, "Mdp3030", 5.7f))
        println(Json.stringify(Person.serializer().list, persons))

        val personsJson = "[{\"id\":0,\"name\":\"Manish patel\",\"height\":5.9},{\"id\":4,\"name\":\"Mdp3030\",\"height\":5.7}]"
        val persons2 = Json.parse(Person.serializer().list, personsJson)
        println(persons2)


    }


    @Serializable
    data class Person(
        val id: Int = 0,
        val name: String,
        val height: Float
    )
}