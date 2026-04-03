package edu.nd.pmcburne.hello.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.nd.pmcburne.hello.viewmodel.MapViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

// UVA brand colors
val UvaBlue = Color(0xFF232D4B)
val UvaOrange = Color(0xFFE57200)
val UvaLightBlue = Color(0xFF4B9CD3)
val BackgroundGray = Color(0xFFF5F5F5)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(viewModel: MapViewModel = viewModel()) {

    val allTags by viewModel.allTags.collectAsState()
    val selectedTag by viewModel.selectedTag.collectAsState()
    val filteredLocations by viewModel.filteredLocations.collectAsState()

    // UVA Grounds center coordinates
    val uvaCenter = LatLng(38.0336, -78.5080)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uvaCenter, 15f)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundGray)
    ) {
        // top header bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(UvaBlue)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column {
                Text(
                    text = "UVA Campus Map",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "University of Virginia · Charlottesville",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }

        // tag filter dropdown
        TagDropdown(
            tags = allTags,
            selectedTag = selectedTag,
            onTagSelected = { viewModel.selectTag(it) }
        )

        // map takes up rest of screen
        Box(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                filteredLocations.forEach { location ->
                    val position = LatLng(location.latitude, location.longitude)
                    Marker(
                        state = MarkerState(position = position),
                        title = location.name,
                        snippet = location.description
                    )
                }
            }

            // little chip showing current tag + count
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp)
                    .shadow(4.dp, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                color = UvaBlue
            ) {
                Text(
                    text = "\"$selectedTag\" — ${filteredLocations.size} location${if (filteredLocations.size != 1) "s" else ""}",
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun TagDropdown(
    tags: List<String>,
    selectedTag: String,
    onTagSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        // the dropdown trigger button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.5.dp, UvaBlue, RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Filter by Tag",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = selectedTag,
                    fontSize = 16.sp,
                    color = UvaBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "open dropdown",
                tint = UvaOrange
            )
        }

        // dropdown menu with scrollable tag list
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .heightIn(max = 300.dp) // makes it scrollable
                .background(Color.White)
        ) {
            tags.forEach { tag ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = tag,
                                fontSize = 15.sp,
                                color = if (tag == selectedTag) UvaOrange else UvaBlue,
                                fontWeight = if (tag == selectedTag) FontWeight.Bold else FontWeight.Normal
                            )
                            if (tag == selectedTag) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(UvaOrange, RoundedCornerShape(4.dp))
                                )
                            }
                        }
                    },
                    onClick = {
                        onTagSelected(tag)
                        expanded = false
                    },
                    modifier = Modifier
                        .background(
                            if (tag == selectedTag) UvaOrange.copy(alpha = 0.08f)
                            else Color.Transparent
                        )
                )
                if (tag != tags.last()) {
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.15f))
                }
            }
        }
    }
}