//
//  NoteItem.swift
//  iosApp

import shared
import SwiftUI

// struct is like a value class in Kotlin, it makes a new copy every time. Like how we use .copy() in kotlin.

struct NoteItem: View {
    var note: Note
    var onDeleteClick: () -> Void

    var body: some View {
        VStack(alignment: .leading) {
            HStack {
                Text(note.title)
                    .font(.title3)
                    .fontWeight(.semibold)
                Spacer()
                Button(action: onDeleteClick) {
                    Image(systemName: "xmark")
                        .foregroundColor(.black)
                }
            }.padding(.bottom, 3)

            Text(note.content)
                .fontWeight(.light)
                .padding(.bottom, 3)

            HStack {
                Spacer()
                Text(LocalDateTimeUtil().formatNoteDate(dateTime: note.created))
                    .font(.footnote)
                    .fontWeight(.light)
            }
        }
        .padding()
        .background(Color(hex: note.colorHex))
        .clipShape(RoundedRectangle(cornerRadius: 15.0)) // makes the rounded corners
        .shadow(radius: 2, x: 3, y: 3)
    }
}

// Add Identifiable to make it work with ForEach
struct NoteItem2: View, Identifiable {
    var id: ObjectIdentifier
    let note: Note
    let onDeleteClick: (Int) -> Void

    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(note.title)
                    .font(.headline)
                Text(note.content)
                    .font(.subheadline)
            }
            Spacer()
            Button(action: {
                onDeleteClick(Int(truncating: note.id ?? 0))
            }) {
                Image(systemName: "trash")
            }
        }
    }
}

struct NoteItem3: View {
    let note: Note
    let onDeleteClick: (Int) -> Void

    var body: some View {
        HStack {
            VStack(alignment: .leading) {
                Text(note.title)
                    .font(.headline)
                Text(note.content)
                    .font(.subheadline)
            }
            Spacer()
            Button(action: {
                onDeleteClick(Int(truncating: note.id ?? 0))
            }) {
                Image(systemName: "trash")
            }
        }
    }
}

struct NoteItem_Previews: PreviewProvider {
    static var previews: some View {
        NoteItem(
            note: Note(
                id: nil,
                title: "My note",
                content: "Note content",
                colorHex: 0x0FF341,
                created: LocalDateTimeUtil().now()
            ),
            onDeleteClick: { }
        )
    }
}

struct NoteItem_Previews2: PreviewProvider {
    static var previews: some View {
        NoteItem2(
            id: ObjectIdentifier(self),
            note: Note(
                id: nil,
                title: "My note",
                content: "Note content",
                colorHex: 0x0FF341,
                created: LocalDateTimeUtil().now()
            ),
            onDeleteClick: {_ in }
        )
    }
}

struct NoteItem_Previews3: PreviewProvider {
    static var previews: some View {
        NoteItem3(
            note: Note(
                id: nil,
                title: "My note",
                content: "Note content",
                colorHex: 0x0FF341,
                created: LocalDateTimeUtil().now()
            ),
            onDeleteClick: {_ in }
        )
    }
}
