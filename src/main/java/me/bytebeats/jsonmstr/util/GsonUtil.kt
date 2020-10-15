package me.bytebeats.jsonmstr.util

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

object GsonUtil {
    @Throws(Exception::class)
    fun toPrettyString(raw: String): String {
        val gson = GsonBuilder().run {
            setPrettyPrinting()
        }.create()
        val jsonElement = JsonParser.parseString(raw)
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