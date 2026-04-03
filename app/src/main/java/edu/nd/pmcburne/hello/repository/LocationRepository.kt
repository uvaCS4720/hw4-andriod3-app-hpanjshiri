package edu.nd.pmcburne.hello.repository

import edu.nd.pmcburne.hello.data.RetrofitInstance
import edu.nd.pmcburne.hello.database.AppDatabase
import edu.nd.pmcburne.hello.database.LocationEntity

class LocationRepository(private val db: AppDatabase) {

    // called once on startup - fetches from API and syncs to DB
    suspend fun syncFromApi() {
        val response = RetrofitInstance.api.getPlacemarks()

        val entities = response.mapNotNull { placemark ->
            // skip any that dont have coords
            val center = placemark.visualCenter ?: return@mapNotNull null
            LocationEntity(
                id = placemark.id,
                name = placemark.name,
                description = placemark.description ?: "",
                tags = placemark.tagList.joinToString(","),
                latitude = center.latitude,
                longitude = center.longitude
            )
        }

        // IGNORE conflict means duplicates just get skipped
        db.locationDao().insertAll(entities)
    }

    suspend fun getAllLocations(): List<LocationEntity> {
        return db.locationDao().getAllLocations()
    }
}