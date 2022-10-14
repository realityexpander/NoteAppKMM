//
//  NoteListScreen.swift
//  iosApp

import shared
import SwiftUI

struct NoteListScreen: View {
    private var noteDataSource: NoteDataSource
    @StateObject var viewModel = NoteListViewModel(noteDataSource: nil) // @StateObject Makes only one instance of the viewModel (otherwise it would be re-created every time the NoteListScreen is redrawn)

    @State private var isNoteSelected = false       // @State is like mutableStateOf in Kotlin
    @State private var selectedNoteId: Int64?       // automatically set to nil be default

    init(noteDataSource: NoteDataSource) {
        self.noteDataSource = noteDataSource
    }

    var body: some View {
        VStack { // Like a Column in Compose
            ZStack { // Like a Box in Compose
                NavigationLink(
                    destination: NoteDetailScreen(
                        noteDataSource: self.noteDataSource,
                        noteId: selectedNoteId
                    ),
                    isActive: $isNoteSelected) {  // $ means use two-way binding
                        EmptyView()
                    }.hidden()  // make the link invisible (kinda hacky, but works)

                HideableSearchTextField<NoteDetailScreen>(
                    onSearchToggled: {
                        viewModel.toggleIsSearchActive()
                    },
                    destinationProvider: {
                        NoteDetailScreen(
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
                // `\.self` gets the hash of the item, ie: viewModel.filteredNotes[x].hashCode, and the `\.self.id` gets the id, ie: viewModel.filteredNotes[x].id
                ForEach(viewModel.filteredNotes, id: \.self.id) { note in
                    Button(action: {
                        isNoteSelected = true
                        selectedNoteId = note.id?.int64Value  // Must Convert Kotlin Long to int64Value
                    }) {
                        NoteItem(note: note, onDeleteClick: {
                            viewModel.deleteNoteById(id: note.id?.int64Value)  // convert kotlin Int to an int64Value
                        })
                    }
                }
            }
            .onAppear {
                viewModel.loadNotes()  // load the notes after the UI is built
            }
            .listStyle(.plain)   // remove the default boxed look
            .listRowSeparator(.hidden)  // hide the separators
        }
        .onAppear {
            viewModel.setNoteDataSource(noteDataSource: noteDataSource)
        }
        .navigationBarHidden(true)
        .hoverEffect(/*@START_MENU_TOKEN@*/ .highlight/*@END_MENU_TOKEN@*/)
    }
}

func fundonothing() {
    print("hello")
}

struct NoteListScreen_Previews: PreviewProvider {
    static var previews: some View {
        VStack {
            List {
                ForEach(0 ..< 10) { note in
                    Button(action: {}
                    ) {
                        NoteItem(
                            note: Note(
                                id: 1,
                                title: "hello " + String(note),
                                content: "goodbye",
                                colorHex: Int64(8454748548 * note),
                                created: Kotlinx_datetimeLocalDateTime(
                                    date: Kotlinx_datetimeLocalDate(
                                        year: 2022,
                                        monthNumber: 10,
                                        dayOfMonth: 9),
                                    time: Kotlinx_datetimeLocalTime(
                                        hour: 5,
                                        minute: 32,
                                        second: 10,
                                        nanosecond: 0)
                                )
                            ),
                            onDeleteClick: { fundonothing() }
                        )
                    }
                }
            }
            // .onAppear(perform: { // Hack to make the list pop to top of screen
            //    UITableView.appearance().contentInset.top = -35
            // })
        }
        .padding(.top, -35.0) // Hack to make the list pop to top of screen
    }
}
