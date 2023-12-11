package Save_Data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update


@Entity("Star")
data class Star(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    
    val date: String,
    val userName: String,
    val repositoryName: String,
)
@Entity("Repository")
data class Repository(
    @PrimaryKey()
    val id: Int,

    val name: String,
    val ownerName: String,
    val favourite: Boolean,
)
@Entity("User")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val name: String,
    val avatarUrl: String,
)


@Dao
interface EmployeeDao {
    @Insert
    suspend fun insertRepo(repo: Repository)

    @Insert
    suspend fun insertStar(star: Star)

    @Delete
    suspend fun deleteRepo(repo: Repository)

    @Delete
    suspend fun deleteStar(star: Star)

    @Query("SELECT * FROM Repository")
    suspend fun allRepos(): List<Repository>

    @Query("SELECT * FROM Star")
    suspend fun allStar(): List<Star>

    @Update
    suspend fun updateRepository(repository: Repository)

    @Query("SELECT * FROM Repository WHERE name = :name")
    suspend fun selectRepo(name: String): List<Repository>

    @Query("SELECT * FROM Star WHERE userName = :starOwner")
    suspend fun selectStarWR(starOwner: String): List<Star>

    @Query("SELECT * FROM Star WHERE repositoryName = :repositoryName")
    suspend fun selectStarWO(repositoryName: String): List<Star>
}

@Database(entities = [Star::class, Repository::class, User::class], version = 1, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}

