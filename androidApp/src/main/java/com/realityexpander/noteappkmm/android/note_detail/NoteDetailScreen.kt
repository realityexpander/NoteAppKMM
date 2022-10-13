package com.realityexpander.noteappkmm.android.note_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import com.realityexpander.noteappkmm.domain.note.Note
import com.realityexpander.noteappkmm.domain.note.NoteDataSource
import kotlinx.datetime.LocalDateTime

@Composable
fun NoteDetailScreen(
    noteId: Long,
    navController: NavController,
    viewModel: NoteDetailViewModel = hiltViewModel(),
    previewState: NoteDetailState? = null // For preview in IDE
) {
    val state = if(LocalInspectionMode.current) {
        // For preview in IDE
        previewState ?: viewModel.state.collectAsState().value
    } else {
        // For normal operation
        viewModel.state.collectAsState().value
    }
    // val state by viewModel.state.collectAsState()

    val hasNoteBeenSaved by viewModel.hasNoteBeenSaved.collectAsState()

    LaunchedEffect(key1 = hasNoteBeenSaved) {
        if(hasNoteBeenSaved) {
            navController.popBackStack()
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::saveNote,
                backgroundColor = Color.Black
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save note",
                    tint = Color.White
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .background(Color(state.noteColor))
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            TransparentHintTextField(
                text = state.noteTitle,
                hint = "Enter a title...",
                isHintVisible = state.isNoteTitleHintVisible,
                onValueChanged = viewModel::onNoteTitleChanged,
                onFocusChanged = {
                    viewModel.onNoteTitleFocusChanged(it.isFocused)
                },
                singleLine = true,
                textStyle = TextStyle(fontSize = 20.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TransparentHintTextField(
                text = state.noteContent,
                hint = "Enter some content...",
                isHintVisible = state.isNoteContentHintVisible,
                onValueChanged = viewModel::onNoteContentChanged,
                onFocusChanged = {
                    viewModel.onNoteContentFocusChanged(it.isFocused)
                },
                singleLine = false,
                textStyle = TextStyle(fontSize = 20.sp),
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview(heightDp = 300)
@Composable
fun NoteDetailScreenPreview() {
    val note = Note(
        id = 1,
        title = "Note title",
        content = "Note content",
        colorHex = 0xFFE57373,
        created = LocalDateTime.parse("2021-08-01T00:00:00")
    )
    val state = NoteDetailState(
        noteTitle = note.title,
        noteContent = note.content,
        noteColor = note.colorHex.toLong(),
        isNoteTitleHintVisible = false,
        isNoteContentHintVisible = false
    )
    NoteDetailScreen(
        noteId = 1,
        navController = NavController(LocalContext.current),
        viewModel = NoteDetailViewModel(
            object: NoteDataSource {
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
                    /* no-op */
                    return emptyList()
                }
                override suspend fun deleteNoteById(id: Long) {
                    /* no-op */
                }
            },
            savedStateHandle = SavedStateHandle().apply {
                set("noteId", 1L)
            }
        ),
        previewState = state
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NoteDetailScreenPreviewDeviceInteractive() {

    val viewModel = NoteDetailViewModel(
        object: NoteDataSource {
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
                /* no-op */
                return emptyList()
            }
            override suspend fun deleteNoteById(id: Long) {
                /* no-op */
            }
        },
        savedStateHandle = SavedStateHandle().apply {
            set("noteId", 1L)
        }
    )

    val state by viewModel.state.collectAsState()

    NoteDetailScreen(
        noteId = 1L,
        navController = NavController(LocalContext.current),
        viewModel = viewModel,
        //previewState = state
    )
}

