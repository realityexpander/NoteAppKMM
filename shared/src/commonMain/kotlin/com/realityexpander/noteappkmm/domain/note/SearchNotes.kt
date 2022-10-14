package com.realityexpander.noteappkmm.domain.note

import com.realityexpander.noteappkmm.domain.time.LocalDateTimeUtil

class SearchNotes {

    fun execute(notes: List<Note>, query: String): List<Note> {
        if(query.isBlank()) {
            return notes
        }
        return notes.filter {
            it.title.trim().lowercase().contains(query.lowercase()) ||
                    it.content.trim().lowercase().contains(query.lowercase())
        }.sortedBy {
            LocalDateTimeUtil.toEpochMillis(it.created)
        }
    }
}