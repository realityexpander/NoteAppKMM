//
//  NoteListScreen.swift
//  iosApp

import shared
import SwiftUI

struct NoteListScreen: View {

    private var noteDataSource: NoteDataSource
    @StateObject var viewModel = NoteListViewModel(noteDataSource: nil) // @StateObject Makes only one instance of the viewModel (otherwise it would be re-created every time the NoteListScreen is redrawn), this is similar to android MutableState

    @State private var isNoteSelected = false // @State is like mutableStateOf in Kotlin
    @State private var selectedNoteId: Int64? // automatically set to nil be default

    private var previewNotes: [Note]? // for in IDE Previews only

    init(noteDataSource: NoteDataSource, previewNotes: [Note]? = nil) {
        self.noteDataSource = noteDataSource
        self.previewNotes = previewNotes
    }

    var body: some View {
        VStack { // Like a Column in Compose
            ZStack { // Like a Box in Compose
                NavigationLink(
                    destination: NoteDetailScreen(
                        noteDataSource: self.noteDataSource,
                        noteId: selectedNoteId
                    ),
                    isActive: $isNoteSelected) { // $ means use two-way binding
                        EmptyView()
                    }.hidden() // make the link invisible (kinda hacky, but works)

                HideableSearchTextField<NoteDetailScreen>( // Must have type of screen that will nav to.
                    onSearchToggled: {
                        viewModel.toggleIsSearchActive()
                    },
                    destinationProvider: {
                        NoteDetailScreen( // must match type above, where we want to nav to.
                            noteDataSource: noteDataSource,
                            noteId: selectedNoteId
                        )
                    },
                    isSearchActive: viewModel.isSearchActive,
                    searchText: $viewModel.searchText
                )
                .frame(maxWidth: .infinity, minHeight: 40)
                .padding()

                if !viewModel.isSearchActive {
                    Text("All notes")
                        .font(.title2)
                }
            }

            List {
                // Only for preview (bypasses the viewmodel for getting notes data from DB)
                if previewNotes != nil {
                    ForEach(previewNotes ?? [], id: \.self.id) { note in
                        Button(action: {
                        }) {
                            NoteItem(note: note, onDeleteClick: {})
                        }
                    }
                } else {
                    // `\.self` gets the hash of the item, ie: viewModel.filteredNotes[x].hashCode, and the `\.self.id` gets the id, ie: viewModel.filteredNotes[x].id
                    ForEach(viewModel.filteredNotes, id: \.self.id) { note in
                        Button(action: {
                            isNoteSelected = true
                            selectedNoteId = note.id?.int64Value // Must Convert Kotlin Long to int64Value
                        }) {
                            NoteItem(note: note, onDeleteClick: {
                                viewModel.deleteNoteById(id: note.id?.int64Value) // convert kotlin Int to an int64Value
                            })
                        }
                    }
                }
            }
            .onAppear {
                viewModel.loadNotes() // load the notes after the UI is built
            }
            .listStyle(.plain) // remove the default boxed look
            .listRowSeparator(.hidden) // hide the separators
        }
        .onAppear {
            viewModel.setNoteDataSource(noteDataSource: noteDataSource)
        }
        .navigationBarHidden(true)
    }
}

struct NoteListScreen_Previews1: PreviewProvider {
    static var notes: [Note] = []
    static var initialized: Bool = false
    
    static var previews: some View {
        if !NoteListScreen_Previews1.initialized {
            for note in 1...10  {
                NoteListScreen_Previews1.notes.append(
                    Note(
                        id: KotlinLong(value: Int64(note)),
                        title: "Note title Preview #"+String(note) ,
                        content: "Note Content Preview #" + String(note),
                        colorHex: Int64(848548 * note),
                        created: Kotlinx_datetimeLocalDateTime(
                            date: Kotlinx_datetimeLocalDate(
                                year: 2022,
                                monthNumber: 10,
                                dayOfMonth: 9
                            ),
                            time: Kotlinx_datetimeLocalTime(
                                hour: 5,
                                minute: 32,
                                second: 10,
                                nanosecond: 0
                            )
                        )
                    )
                )
            }
            
            NoteListScreen_Previews1.initialized = true
        }
        
        return NavigationView {
            NoteListScreen(
                noteDataSource: DatabaseModule().noteDataSource,
                previewNotes: NoteListScreen_Previews1.notes
            )
        }
    }
}

struct NoteListScreen_Previews2: PreviewProvider {
    static var previews: some View {
        NoteItem(
            note: Note(
                id: KotlinLong(value: Int64(1)),
                title: "Title for note " + String(1),
                content: "Contents for note " + String(1),
                colorHex: Int64(8454748548 * 1),
                created: Kotlinx_datetimeLocalDateTime(
                    date: Kotlinx_datetimeLocalDate(
                        year: 2022,
                        monthNumber: 10,
                        dayOfMonth: 3
                    ),
                    time: Kotlinx_datetimeLocalTime(
                        hour: 5,
                        minute: 32,
                        second: 10,
                        nanosecond: 0
                    )
                )
            ),
            onDeleteClick: { doNothing() }
        )
    }
}
func doNothing() {
    print("hello")
}

// Another preview
