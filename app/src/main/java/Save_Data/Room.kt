package Save_Data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase


@Entity
data class Star(
    @PrimaryKey
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
    val name: String,
    val avatarUrl: String
)


@Dao
interface EmployeeDao {
    @Query("SELECT * FROM star WHERE userIdname = :userIdname")
    suspend fun selectNameRepos(userIdname: String): List<Star>

    @Query("SELECT * FROM star WHERE repositoryIdname = :repositoryIdname")
    suspend fun selectStarOfRepo(repositoryIdname: String): List<Star>

    @Insert
    suspend fun insertOfStars(star: Star)

    @Query("SELECT * FROM star")
    suspend fun allStar(): List<Star>

}

@Database(entities = [Star::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}