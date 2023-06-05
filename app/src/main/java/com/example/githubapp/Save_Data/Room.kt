package com.example.githubapp.Save_Data

import androidx.room.*


@Entity
data class All(
    @PrimaryKey var id: Int = 0,
    val name: String = "Artem Shakirov",
    val age: Int = 13,
)

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employee")
    suspend fun all(): List<All>

    @Query("SELECT * FROM employee WHERE id = :id")
    suspend fun getById(id: Long): All

    @Insert(entity = All::class)
    suspend fun insert(employee: All)

    @Update
    suspend fun update(employee: All)

    @Delete
    suspend fun delete(employee: All)

}

@Database(entities = [All::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}