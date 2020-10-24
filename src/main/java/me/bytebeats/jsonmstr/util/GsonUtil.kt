package me.bytebeats.jsonmstr.util

import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException

object GsonUtil {
    @Throws(JsonSyntaxException::class)
    fun toPrettyString(raw: String): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonElement = JsonParser().parse(raw)
//        JsonParser.parseString(raw)
        return gson.toJson(jsonElement)
    }

//    @Throws(Exception::class)
//    fun toCompactString(raw: String): String {
//        val gson = GsonBuilder().run {
//            setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//        }.create()
//        val jsonElement = JsonParser.parseString(raw)
//        return gson.toJson(jsonElement)
//    }
}