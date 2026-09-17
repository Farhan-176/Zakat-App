package com.example.data.local

import androidx.room.TypeConverter
import org.json.JSONArray

class AssetGroupConverters {

    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        if (list == null) return "[]"
        val jsonArray = JSONArray()
        for (item in list) {
            jsonArray.put(item)
        }
        return jsonArray.toString()
    }

    @TypeConverter
    fun toStringList(json: String?): List<String> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            val jsonArray = JSONArray(json)
            val result = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.optString(i)
                if (!item.isNullOrBlank()) {
                    result.add(item)
                }
            }
            result
        } catch (e: Exception) {
            emptyList()
        }
    }
}
