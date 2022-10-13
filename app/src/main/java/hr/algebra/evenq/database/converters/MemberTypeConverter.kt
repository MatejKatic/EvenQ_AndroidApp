package hr.algebra.evenq.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.algebra.evenq.network.model.Member

class MemberTypeConverter {

    @TypeConverter
    fun fromMember(value: Member): String {
        val gson = Gson()
        val type = object : TypeToken<Member>() {}.type
        return gson.toJson(value, type)
    }


    @TypeConverter
    fun toMember(value: String): Member {
        val gson = Gson()
        var type = object : TypeToken<Member>() {}.type
        return gson.fromJson(value, type)
    }

}