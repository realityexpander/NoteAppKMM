//
//  NoteListViewModel.swift
//  iosApp

import Foundation
import shared    // imports cocoapods stuff (not need as many imports as android)

extension NoteListScreen {
    @MainActor class NoteListViewModel: ObservableObject {  // @MainActor means main thread, ObservableObject means reactive
        private var noteDataSource: NoteDataSource? = nil
        
        private let searchNotes = SearchNotes()  // let is a constant value
        
        private var notes = [Note]()  // Brackets indicate the type for notes
        @Published private(set) var filteredNotes = [Note]()  // @Published makes `filteredNotes` a "reactive state"
        @Published var searchText = "" {
            didSet { // executes this block after the `searchText` is updated
                self.filteredNotes = searchNotes.execute(notes: self.notes, query: searchText)
            }
        }
        @Published private(set) var isSearchActive = false  // is the search UI element showing?
        
        init(noteDataSource: NoteDataSource? = nil) {
            self.noteDataSource = noteDataSource
        }
        
        func loadNotes() {
            noteDataSource?.getAllNotes(completionHandler: { notes, error in  // in is same as -> in kotlin
                self.notes = notes ?? []  // ?? == ?: in kotlin
                self.filteredNotes = self.notes
            })
        }
        
        func deleteNoteById(id: Int64?) {
            if id != nil {  // no parens for if statements in swift
                noteDataSource?.deleteNoteById(id: id!, completionHandler: { error in  // null assertion in swift is one exclamation mark (!)
                    self.loadNotes()
                })
            }
        }
        
        func toggleIsSearchActive() {
            isSearchActive = !isSearchActive
            if !isSearchActive {
                searchText = ""
            }
        }
        
        func setNoteDataSource(noteDataSource: NoteDataSource) {
            self.noteDataSource = noteDataSource
        }
    }
}
