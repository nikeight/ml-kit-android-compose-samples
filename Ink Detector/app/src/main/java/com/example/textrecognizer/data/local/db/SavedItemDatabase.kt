package com.example.textrecognizer.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.textrecognizer.data.model.SavedItemEntity
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Date
import java.util.UUID

@Database(
    entities = [
        SavedItemEntity::class
    ],
    version = 1
)
@TypeConverters(DateConverters::class, ListConverters::class, UUIDToStringConverter::class)
abstract class SavedItemDatabase() : RoomDatabase() {
    abstract fun provideSavedItemDao(): SavedItemDao
}


// Helps to Store Data Object
class DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

class ListConverters {
    @TypeConverter
    fun fromList(value: List<String>?): String? {
        return value?.let { safeList ->
            Json.encodeToString(safeList)
        }
    }

    @TypeConverter
    fun toList(value: String?) = value?.let { Json.decodeFromString<List<String>>(it) }
}

class UUIDToStringConverter {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return if (uuid == null) null else UUID.fromString(uuid)
    }
}