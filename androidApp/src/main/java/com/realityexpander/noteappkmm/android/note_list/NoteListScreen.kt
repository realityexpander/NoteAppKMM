package com.realityexpander.noteappkmm.android.note_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.realityexpander.noteappkmm.android.note_detail.NoteDetailState
import com.realityexpander.noteappkmm.domain.note.Note
import com.realityexpander.noteappkmm.domain.note.NoteDataSource
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListScreen(
    navController: NavController,
    viewModel: NoteListViewModel = hiltViewModel(),
    previewState: NoteListState? = null // For preview in IDE
) {
    val state = if (LocalInspectionMode.current) {
        // For preview in IDE
        previewState ?: viewModel.state.collectAsState().value
    } else {
        // For normal operation
        viewModel.state.collectAsState().value
    }
    //val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.loadNotes() // must load the initial notes manually, because we are not using a StateFlow
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("note_detail/-1L") // make a new note
                },
                backgroundColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add note",
                    tint = Color.White
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HideableSearchTextField(
                    text = state.searchText,
                    isSearchActive = state.isSearchActive,
                    onTextChange = viewModel::onSearchTextChange,
                    onSearchClick = viewModel::onToggleSearch,
                    onCloseClick = viewModel::onToggleSearch,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp)
                )
                this@Column.AnimatedVisibility(
                    visible = !state.isSearchActive,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(
                        text = "All notes",
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp
                    )
                }
            }
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(
                    items = state.notes,
                    key = { it.id!! }
                ) { note ->
                    NoteItem(
                        note = note,
                        backgroundColor = Color(note.colorHex),
                        onNoteClick = {
                            navController.navigate("note_detail/${note.id}")
                        },
                        onDeleteClick = {
                            viewModel.deleteNoteById(note.id!!)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .animateItemPlacement()
                    )
                }
            }
        }
    }
}

// Note does not use the ViewModel.
// But you can modify the savedStateHandle values to change the UI state.
@Preview
@Composable
fun NoteListScreenPreview() {
    val state = NoteListState(
        notes = listOf(
            Note(
                id = 1L,
                title = "Note 1",
                content = "This is a note",
                colorHex = 0xFFFF0000,
                created = LocalDateTime(2021, 1, 1, 0, 0),
            ),
            Note(
                id = 2L,
                title = "Note 2",
                content = "This is another note",
                colorHex = 0xFF00FF00,
                created = LocalDateTime(2021, 1, 1, 0, 0),
            ),
            Note(
                id = 3L,
                title = "Note 3",
                content = "This is yet another note",
                colorHex = 0xFF0000FF,
                created = LocalDateTime(2021, 1, 1, 0, 0),
            ),
            Note(
                id = 4L,
                title = "Note 4",
                content = "This is yet another note",
                colorHex = 0xFFFF00FF,
                created = LocalDateTime(2021, 1, 1, 0, 0),
            ),
            Note(
                id = 5L,
                title = "Note 5",
                content = "This is yet another note",
                colorHex = 0xFF00FFFF,
                created = LocalDateTime(2021, 1, 1, 0, 0),
            ),
            Note(
                id = 6L,
                title = "Note 6",
                content = "This is yet another note",
                colorHex = 0xFFFFEE00,
                created = LocalDateTime(2021, 1, 1, 0, 0),
            ),
        ),
        isSearchActive = true,
        searchText = "Search text"
    )

    NoteListScreen(
        navController = NavController(LocalContext.current),
        viewModel = NoteListViewModel(
            object : NoteDataSource {
                override suspend fun insertNote(note: Note) {
                    /* no-op */
                }
                override suspend fun getNoteById(id: Long): Note {
                    return Note(
                        id = 1,
                        title = "Title",
                        content = "Content",
                        colorHex = 0xFFE57373,
                        created = LocalDateTime.parse("2021-01-01T00:00:00")
                    )
                }
                override suspend fun getAllNotes(): List<Note> {
                    return emptyList() /* no op */
                }
                override suspend fun deleteNoteById(id: Long) {
                    /* no-op */
                }
            },
            savedStateHandle = SavedStateHandle().apply {
                set(PARAM_NOTES, emptyList<Note>())
                set(PARAM_SEARCH_QUERY, "")
                set(PARAM_IS_SEARCH_FOCUSED, false)
            }
        ),
        previewState = state
    )
}


// Run on device for preview of viewmodel state.
// Note: navigation is not working in preview.
@Preview
@Composable
fun NoteListScreenPreviewRunOnDevice() {
    NoteListScreen(
        navController = NavController(LocalContext.current),
        viewModel = NoteListViewModel(
            object : NoteDataSource {
                override suspend fun insertNote(note: Note) {
                    /* no-op */
                }

                override suspend fun getNoteById(id: Long): Note {
                    return Note(
                        id = 1,
                        title = "Title",
                        content = "Content",
                        colorHex = 0xFFE57373,
                        created = LocalDateTime.parse("2021-01-01T00:00:00")
                    )
                }

                override suspend fun getAllNotes(): List<Note> {
                    return listOf(
                        Note(
                            id = 1L,
                            title = "Note 1",
                            content = "Content 1",
                            colorHex = 0xFF485855,
                            created = LocalDateTime.parse("2021-01-01T00:00:00")
                        ),
                        Note(
                            id = 2L,
                            title = "Note 2",
                            content = "Content 2",
                            colorHex = 0xFFFF00EE,
                            created = LocalDateTime.parse("2021-01-01T00:00:00")
                        ),
                        Note(
                            id = 3L,
                            title = "Note 3",
                            content = "Content 3",
                            colorHex = 0xFF662243,
                            created = LocalDateTime.parse("2021-01-01T00:00:00")
                        ),
                        Note(
                            id = 4L,
                            title = "Note 4",
                            content = "Content 4",
                            colorHex = 0xFF33FF43,
                            created = LocalDateTime.parse("2021-01-01T00:00:00")
                        ),
                        Note(
                            id = 5L,
                            title = "Note 5",
                            content = "Content 5",
                            colorHex = 0xFF22FF43,
                            created = LocalDateTime.parse("2021-01-01T00:00:00")
                        ),
                        Note(
                            id = 6L,
                            title = "Note 6",
                            content = "Content 6",
                            colorHex = 0xFF331143,
                            created = LocalDateTime.parse("2021-01-01T00:00:00")
                        ),
                    )
                }

                override suspend fun deleteNoteById(id: Long) {
                    /* no-op */
                }
            },
            savedStateHandle = SavedStateHandle().apply {
                set(PARAM_NOTES, emptyList<Note>())
                set(PARAM_SEARCH_QUERY, "")
                set(PARAM_IS_SEARCH_FOCUSED, false)
            }
        ),
        previewState = null
    )
}







































