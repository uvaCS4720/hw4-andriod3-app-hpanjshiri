# UVA Campus Maps
Android app that displays UVA locations on a Google Map.

## Features
- Fetches locations from the UVA placemarks API
- Stores data locally in SQLite via Room
- Filter locations by tag using dropdown
- Google Maps with markers and info windows

## Setup
Add your Google Maps API key to `local.properties`:
MAPS_API_KEY=_____

## Acknowledgements 
I built this app from scratch using Kotlin and Jetpack Compose. I designed 
the data layer using Room for local SQLite storage and Retrofit for API calls 
to the UVA placemarks endpoint. The app syncs data from the API on startup 
and stores it locally to avoid duplicate entries on subsequent launches. I 
implemented the ViewModel architecture to handle configuration changes like 
screen rotation without data loss. I used Claude as a learning resource to 
better understand concepts like OkHttp timeout configuration, Google Maps 
Compose integration and resolving Gradle/manifest issues with the Maps API 
key setup, similar to using Stack Overflow or documentation for debugging.
