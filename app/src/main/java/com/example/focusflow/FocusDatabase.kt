package com.example.focusflow

import android.content.Context
import androidx.room.*

// 1. 定义表结构 (Entity)
@Entity(tableName = "focus_records")
data class FocusRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,      // 专注日期 (如 "2023-12-25 10:00")
    val duration: String   // 专注时长 (如 "25分钟")
)

// 2. 定义操作接口 (DAO)
@Dao
interface FocusDao {
    @Insert
    fun insert(record: FocusRecord)

    @Query("SELECT * FROM focus_records ORDER BY id DESC")
    fun getAll(): List<FocusRecord>
}

// 3. 定义数据库入口 (Database)
@Database(entities = [FocusRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun focusDao(): FocusDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "focus_database"
                )
                    .allowMainThreadQueries() // 【偷懒关键点】允许在主线程读写数据库，防止报错
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}