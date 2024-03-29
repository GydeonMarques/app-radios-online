package br.com.gms.webradios.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.gms.webradios.data.model.local.RadioCategoryEntity
import br.com.gms.webradios.data.model.local.RadioEntity

@Database(
    version = 1, exportSchema = false, entities = [
        RadioEntity::class,
        RadioCategoryEntity::class
    ]
)
abstract class RadioStationsDatabase : RoomDatabase() {

    abstract val radioStationsFavoritesDAO: RadioStationsFavoritesDAO

    companion object {

        private val lock = Any()

        @Volatile
        private var INSTANCE: RadioStationsDatabase? = null

        @JvmStatic
        fun newInstance(context: Context): RadioStationsDatabase {
            synchronized(lock) {
                return INSTANCE ?: Room.databaseBuilder(
                    context,
                    RadioStationsDatabase::class.java,
                    "radios_online"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
