package com.example.courselistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.courselistapp.ui.theme.CourseListAppTheme

// Chocolate-Themed Colors 🍫
val DeepCocoaText = Color(0xFF563D2D)
val DarkChocolate = Color(0xFF3E2723)
val LightCocoaText = Color(0xFFD7CCC8)
 // Soft cocoa for readable text in dark mode


// Global Course List
val sampleCourses = listOf(
    Course("Mobile App Development", "CS301", 5, "Learn to build mobile apps using Jetpack Compose.", "Basic web dev knowledge"),
    Course("Graphics Design", "CS302", 3, "Introduction to digital design principles and creative tools.", "No prerequisites"),
    Course("Cyber Security", "CS303", 3, "Understand security threats and protection techniques.", "Networking Basics"),
    Course("Fundamentals of AI", "CS304", 5, "Explore machine learning concepts and neural networks.", "Python"),
    // New Courses
    Course("Software Engineering", "CS305", 4, "Explore software design and development methodologies.", "Programming Basics"),
    Course("Game Development", "CS307", 5, "Develop games using Unity and Unreal Engine.", "Basic programming knowledge"),
    Course("UI/UX Design", "CS308", 3, "Master user experience principles for mobile & web apps.", "No prerequisites"),
    Course("Data Science", "CS309", 5, "Learn data analysis, visualization, and machine learning.", "Python & Statistics"),
    Course("Blockchain Technology", "CS310", 4, "Understand decentralized applications and smart contracts.", "Basic cryptography knowledge"),
    Course("Embedded Systems", "CS311", 4, "Work with IoT and microcontrollers for smart devices.", "Electronics & C Programming")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CourseListAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun SearchBar(searchQuery: String, onSearchQueryChanged: (String) -> Unit) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchQueryChanged,
        label = { Text("Search Course") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        singleLine = true
    )
}

@Composable
fun AppContent() {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var isDarkMode by rememberSaveable { mutableStateOf(false) }

    CourseListAppTheme(darkTheme = isDarkMode) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                // Dark Mode Toggle Button
                Button(
                    onClick = { isDarkMode = !isDarkMode },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode")
                }

                SearchBar(searchQuery) { newQuery -> searchQuery = newQuery } // Search input
                AnimatedCourseList(searchQuery) // Animated course list replaces standard one
            }
        }
    }
}

@Composable
fun AnimatedCourseList(searchQuery: String) {
    val filteredCourses = sampleCourses.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.code.contains(searchQuery, ignoreCase = true)
    }

    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(filteredCourses, key = { it.code }) { course ->
            AnimatedVisibility(
                visible = true,
                enter = slideInVertically(initialOffsetY = { it * 10 }) + fadeIn()
            ) {
                CourseCard(
                    courseTitle = course.title,
                    courseCode = course.code,
                    creditHours = course.creditHours,
                    description = course.description,
                    prerequisites = course.prerequisites
                )
            }
        }
    }
}

@Composable
fun CourseCard(courseTitle: String, courseCode: String, creditHours: Int, description: String, prerequisites: String) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    val scale by animateFloatAsState(if (isExpanded) 1.05f else 1f)
    val alpha by animateFloatAsState(if (isExpanded) 1f else 0.8f)

    // Dynamic text color for dark mode support
    val textColor = if (MaterialTheme.colorScheme.background == DarkChocolate) LightCocoaText else DeepCocoaText

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { isExpanded = !isExpanded }
            .graphicsLayer(scaleX = scale, scaleY = scale, alpha = alpha),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.elevatedCardElevation(10.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = courseTitle,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary // Ensures title stands out
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row {
                Text(text = "Code: $courseCode", style = MaterialTheme.typography.bodyMedium, color = textColor)
                Spacer(modifier = Modifier.width(20.dp))
                Text(text = "Credits: $creditHours", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Description:", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text(text = description, style = MaterialTheme.typography.bodyMedium, color = textColor)

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Prerequisites:", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                Text(text = prerequisites, style = MaterialTheme.typography.bodyMedium, color = textColor)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CourseCardPreview() {
    CourseListAppTheme {
        CourseCard(
            courseTitle = "Mobile App Development",
            courseCode = "CS301",
            creditHours = 5,
            description = "Learn to build modern mobile applications using Kotlin and Jetpack Compose.",
            prerequisites = "Basic web dev knowledge"
        )
    }
}
