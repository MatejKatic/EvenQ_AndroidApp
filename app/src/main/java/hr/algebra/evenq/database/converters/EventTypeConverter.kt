package hr.algebra.evenq.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.algebra.evenq.network.model.Event
import hr.algebra.evenq.network.model.Member

class EventTypeConverter {

    @TypeConverter
    fun fromEvent(value: Event): String {
        val gson = Gson()
        val type = object : TypeToken<Event>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toEvent(value: String): Event {
        val gson = Gson()
        val type = object : TypeToken<Event>() {}.type
        return gson.fromJson(value, type)
    }

}