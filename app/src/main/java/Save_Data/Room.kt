package Save_Data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase


@Entity("Star")
data class Star(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val favourite: Boolean,
    @Embedded("userId")
    val userId: User,
    @Embedded("repositoryId")
    val repositoryId: Repository,
)
@Entity("Repository")
data class Repository(
    val id: Int,
    val name: String,
    @Embedded("ownerId")
    val ownerId: User,
)
@Entity("User")
data class User(
    val id: Int,
    val name: String,
    val avatarUrl: String,
)


@Dao
interface EmployeeDao {
    @Query("SELECT * FROM Repository WHERE ownerIdname = :userIdname")
    suspend fun selectNameRepos(userIdname: String): List<Repository>

    @Query("SELECT * FROM Star WHERE repositoryIdname = :repositoryIdname")
    suspend fun selectStarOfRepo(repositoryIdname: String): List<Star>

    @Insert
    suspend fun insertOfRepo(repo: Repository)
}

@Database(entities = [Star::class, Repository::class, User::class], version = 1, exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}

