//
//  NoteDetailViewModel.swift
//  iosApp
//
//

import Foundation
import shared

extension NoteDetailScreen {  // Defines what the View is associated with this ViewModel
    @MainActor class NoteDetailViewModel: ObservableObject {
        private var noteDataSource: NoteDataSource?

        private var noteId: Int64?
        @Published var noteTitle = ""      // two way binding with @Published and is also state (similar to MutableState in Compose)
        @Published var noteContent = ""
        @Published var noteColor = Note.companion.getRandomNoteColor()

        init(noteDataSource: NoteDataSource? = nil) {
            self.noteDataSource = noteDataSource
        }

        func loadNoteIfExists(id: Int64?) {
            if id != nil {
                noteId = id
                noteDataSource?.getNoteById(id: id!, completionHandler: { note, _ in
                    self.noteTitle = note?.title ?? ""
                    self.noteContent = note?.content ?? ""
                    self.noteColor = note?.colorHex ?? Note.companion.getRandomNoteColor()
                })
            }
        }

        func saveNote(onSaved: @escaping () -> Void) {  // `@escaping` allows the completion handler to return even if this caller is out of scope.
            noteDataSource?.insertNote(
                note: Note(
                    id: noteId == nil ? nil : KotlinLong(value: noteId!),  // Need to use a KotlinLong for the Domain model, not int64
                    title: noteTitle,
                    content: noteContent,
                    colorHex: noteColor,
                    created: LocalDateTimeUtil().now()),
                completionHandler: { _ in
                    onSaved()
                })
        }

        func setNoteDataSourceAndLoadNote(noteDataSource: NoteDataSource, noteId: Int64?) {
            self.noteDataSource = noteDataSource
            loadNoteIfExists(id: noteId)
        }
    }
}
