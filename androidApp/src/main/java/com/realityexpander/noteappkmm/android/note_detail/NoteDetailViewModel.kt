package com.realityexpander.noteappkmm.android.note_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realityexpander.noteappkmm.domain.note.Note
import com.realityexpander.noteappkmm.domain.note.NoteDataSource
import com.realityexpander.noteappkmm.domain.time.DateTimeUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

const val NOTE_TITLE = "noteTitle"
const val IS_NOTE_TITLE_FOCUSED = "isNoteTitleFocused"
const val NOTE_CONTENT = "noteContent"
const val IS_NOTE_CONTENT_FOCUSED = "isNoteContentFocused"
const val NOTE_COLOR = "noteColor"

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val noteTitle =
        savedStateHandle.getStateFlow(NOTE_TITLE, "")
    private val isNoteTitleFocused =
        savedStateHandle.getStateFlow(IS_NOTE_TITLE_FOCUSED, false)
    private val noteContent =
        savedStateHandle.getStateFlow(NOTE_CONTENT, "")
    private val isNoteContentFocused =
        savedStateHandle.getStateFlow(IS_NOTE_CONTENT_FOCUSED, false)
    private val noteColor =
        savedStateHandle.getStateFlow(NOTE_COLOR, Note.generateRandomColor())

    val state = combine(
        noteTitle,
        isNoteTitleFocused,
        noteContent,
        isNoteContentFocused,
        noteColor
    ) { title, isTitleFocused, content, isContentFocused, color ->
        NoteDetailState(
            noteTitle = title,
            isNoteTitleHintVisible = title.isEmpty() && !isTitleFocused,
            noteContent = content,
            isNoteContentHintVisible = content.isEmpty() && !isContentFocused,
            noteColor = color
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteDetailState())

    private val _hasNoteBeenSaved = MutableStateFlow(false)
    val hasNoteBeenSaved = _hasNoteBeenSaved.asStateFlow()

    private var existingNoteId: Long? = null

    init {
        savedStateHandle.get<Long>("noteId")?.let { existingNoteId ->
            // navigation doesn't support nullable types, so we have to use a -1L to indicate a new note
            if(existingNoteId == -1L) {
                return@let
            }

            this.existingNoteId = existingNoteId
            viewModelScope.launch {
                noteDataSource.getNoteById(existingNoteId)?.let { note ->
                    savedStateHandle[NOTE_TITLE] = note.title
                    savedStateHandle[NOTE_CONTENT] = note.content
                    savedStateHandle[NOTE_COLOR] = note.colorHex
                }
            }
        }
    }

    fun onNoteTitleChanged(text: String) {
        savedStateHandle[NOTE_TITLE] = text
    }

    fun onNoteContentChanged(text: String) {
        savedStateHandle[NOTE_CONTENT] = text
    }

    fun onNoteTitleFocusChanged(isFocused: Boolean) {
        savedStateHandle[IS_NOTE_TITLE_FOCUSED] = isFocused
    }

    fun onNoteContentFocusChanged(isFocused: Boolean) {
        savedStateHandle[IS_NOTE_CONTENT_FOCUSED] = isFocused
    }

    fun saveNote() {
        viewModelScope.launch {
            noteDataSource.insertNote(
                Note(
                    id = existingNoteId,
                    title = noteTitle.value,
                    content = noteContent.value,
                    colorHex = noteColor.value,
                    created = DateTimeUtil.now()
                )
            )
            _hasNoteBeenSaved.value = true
        }
    }
}