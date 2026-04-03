package edu.nd.pmcburne.hello.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hello.database.AppDatabase
import edu.nd.pmcburne.hello.database.LocationEntity
import edu.nd.pmcburne.hello.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LocationRepository(AppDatabase.getDatabase(application))

    private val _allLocations = MutableStateFlow<List<LocationEntity>>(emptyList())

    private val _allTags = MutableStateFlow<List<String>>(emptyList())
    val allTags: StateFlow<List<String>> = _allTags

    private val _selectedTag = MutableStateFlow("core")
    val selectedTag: StateFlow<String> = _selectedTag

    private val _filteredLocations = MutableStateFlow<List<LocationEntity>>(emptyList())
    val filteredLocations: StateFlow<List<LocationEntity>> = _filteredLocations

    init {
        viewModelScope.launch {
            // sync from API
            try {
                repository.syncFromApi()
            } catch (e: Exception) {
                // if network fails, try catch load from db
                e.printStackTrace()
            }

            // load from DB
            val locations = repository.getAllLocations()
            _allLocations.value = locations

            val tags = locations
                .flatMap { it.tags.split(",") }
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .distinct()
                .sorted()
            _allTags.value = tags

            // apply initial filter
            applyFilter()
        }
    }

    fun selectTag(tag: String) {
        _selectedTag.value = tag
        applyFilter()
    }

    private fun applyFilter() {
        val tag = _selectedTag.value
        _filteredLocations.value = _allLocations.value.filter { location ->
            location.tags.split(",").map { it.trim() }.contains(tag)
        }
    }
}