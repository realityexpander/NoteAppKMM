package com.realityexpander.noteappkmm.android.note_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realityexpander.noteappkmm.domain.note.Note
import com.realityexpander.noteappkmm.domain.note.NoteDataSource
import com.realityexpander.noteappkmm.domain.note.SearchNotes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

const val PARAM_NOTES = "notes"
const val PARAM_SEARCH_QUERY = "searchQuery"
const val PARAM_IS_SEARCH_FOCUSED = "isSearchFocused"

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteDataSource: NoteDataSource,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val searchNotes = SearchNotes()

    private val notes = savedStateHandle.getStateFlow(PARAM_NOTES, emptyList<Note>())
    private val searchQuery = savedStateHandle.getStateFlow(PARAM_SEARCH_QUERY, "")
    private val isSearchFocused = savedStateHandle.getStateFlow(PARAM_IS_SEARCH_FOCUSED, false)

    val state = combine(notes, searchQuery, isSearchFocused) { notes, searchText, isSearchActive ->
        NoteListState(
            notes = searchNotes.execute(notes, searchText),
            searchText = searchText,
            isSearchActive = isSearchActive
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteListState())

    fun loadNotes() {
        viewModelScope.launch {
            savedStateHandle[PARAM_NOTES] = noteDataSource.getAllNotes()
        }
    }

    fun onSearchTextChange(text: String) {
        savedStateHandle[PARAM_SEARCH_QUERY] = text
    }

    fun onToggleSearch() {
        savedStateHandle[PARAM_IS_SEARCH_FOCUSED] = !isSearchFocused.value
        if(!isSearchFocused.value) {
            savedStateHandle[PARAM_SEARCH_QUERY] = ""
        }
    }

    fun deleteNoteById(id: Long) {
        viewModelScope.launch {
            noteDataSource.deleteNoteById(id)
            loadNotes() // since we are not using flows on the database, we need to reload the notes manually.
        }
    }
}