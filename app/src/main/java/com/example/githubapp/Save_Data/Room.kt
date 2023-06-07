package com.example.githubapp.Save_Data

package com.example.roomapp.Room

import androidx.room.*

@Entity
data class Star(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    @Embedded("userId")
    val userId: User,
    @Embedded("repositoryId")
    val repositoryId: Repository,
)

data class Repository(
    val id: Int,
    val name: String,
    @Embedded("ownerId")
    val ownerId: User,
)

data class User(
    val id: Int,
    val userName: String,
    val name: String,
    val avatarUrl: String
)


@Dao
interface EmployeeDao {
    @Query("SELECT * FROM star WHERE repositoryIdname = :repositoryIdname1")
    suspend fun selectStar(repositoryIdname1: String): List<Star>

    @Insert
    suspend fun insert(star: Star)


}

@Database(entities = [Star::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}