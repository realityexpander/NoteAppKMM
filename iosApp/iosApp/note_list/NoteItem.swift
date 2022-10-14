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
            onDeleteClick: {}
        )
    }
}
