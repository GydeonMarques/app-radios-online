package br.com.gms.radiosonline.presentation.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.encoding.dreams.radios.online.data.database.LocalRadioStationsDao
import br.com.encoding.dreams.radios.online.domain.model.RadioCategoryModel
import br.com.gms.radiosonline.domain.model.RadioModel

@Database(
    version = 1, exportSchema = false, entities = [
        RadioModel::class,
        RadioCategoryModel::class
    ]
)
abstract class LocalRadioStationsDatabase : RoomDatabase() {

    abstract val localRadioStationRepository: LocalRadioStationsDao

    companion object {

        private val lock = Any()

        @Volatile
        private var INSTANCE: LocalRadioStationsDatabase? = null

        @JvmStatic
        fun newInstance(context: Context): LocalRadioStationsDatabase {
            synchronized(lock) {
                return INSTANCE ?: Room.databaseBuilder(
                    context,
                    LocalRadioStationsDatabase::class.java,
                    "radios_online"
                ).fallbackToDestructiveMigration().build().also {
                    INSTANCE = it
                }
            }
        }
    }
}
