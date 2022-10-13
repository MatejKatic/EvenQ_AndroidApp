package hr.algebra.evenq.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import hr.algebra.evenq.database.converters.EventTypeConverter
import hr.algebra.evenq.database.converters.MemberTypeConverter
import hr.algebra.evenq.database.model.AdminTicketDb
import hr.algebra.evenq.database.model.TicketDb


@Database(entities = [TicketDb::class, AdminTicketDb::class], version = 1)
@TypeConverters(MemberTypeConverter::class, EventTypeConverter::class)
abstract class TicketDatabase: RoomDatabase() {
    abstract fun ticketDao(): TicketDao

    companion object{
        private var instance: TicketDatabase? = null

        fun getDatabase(context: Context): TicketDatabase?{
            if (instance == null){
                instance = buildDatabase(context)
            }
            return instance
        }

        private fun buildDatabase(context: Context): TicketDatabase =
            Room.databaseBuilder(context, TicketDatabase::class.java, "TicketsDatabase")
                .allowMainThreadQueries().build()
    }

}