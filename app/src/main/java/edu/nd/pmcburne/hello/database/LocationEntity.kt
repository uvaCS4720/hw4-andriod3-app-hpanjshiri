package edu.nd.pmcburne.hello.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val description: String,
    val tags: String,       // stored as comma-separated string
    val latitude: Double,
    val longitude: Double
)