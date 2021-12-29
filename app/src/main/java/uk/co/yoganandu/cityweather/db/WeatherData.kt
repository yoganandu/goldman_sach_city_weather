package uk.co.yoganandu.cityweather.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Entity
data class CityDetails(
    @PrimaryKey
    @ColumnInfo(name = "city_id")
    val cityId: String,
    @ColumnInfo(name = "city_name") val cityName: String
)

@Dao
interface FavouritesDao {
    @Query("SELECT * FROM citydetails")
    fun getAll(): LiveData<List<CityDetails>>

    @Query("SELECT * FROM citydetails where city_id like :cityId")
    fun getCityById(cityId: String): LiveData<CityDetails>

    @Insert
    fun addCityToFavourite(cityDetail: CityDetails)

    @Delete
    fun removeCityFromFavourite(cityDetail: CityDetails)

}

@Database(entities = [CityDetails::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun favouriteDao(): FavouritesDao
}