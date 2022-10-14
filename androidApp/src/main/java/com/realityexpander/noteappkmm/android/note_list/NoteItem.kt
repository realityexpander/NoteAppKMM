package com.realityexpander.noteappkmm.android.note_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.realityexpander.noteappkmm.domain.note.Note
import com.realityexpander.noteappkmm.domain.time.LocalDateTimeUtil

@Composable
fun NoteItem(
    note: Note,
    backgroundColor: Color,
    onNoteClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedDate = remember(note.created) {
        LocalDateTimeUtil.formatNoteDate(note.created)
    }
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(backgroundColor)
            .clickable { onNoteClick() }
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = note.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Delete note",
                modifier = Modifier
                    .clickable(MutableInteractionSource(), null) {
                        onDeleteClick()
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = note.content, fontWeight = FontWeight.Light)
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = formattedDate,
            color = Color.DarkGray,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Preview(widthDp = 300, backgroundColor = 0x00000000, showBackground = true)
@Composable
fun NoteItemPreview() {
    NoteItem(
        note = Note(
            id = 0,
            title = "Note title",
            content = "Note content",
            colorHex = 0xFFE57373,
            created = LocalDateTimeUtil.now()
        ),
        backgroundColor = Color(0xFFE57373),
        onNoteClick = {},
        onDeleteClick = {}
    )
}

@Preview(heightDp = 200, backgroundColor = 0x00000000, showBackground = true)
@Composable
fun NoteItemPreview2() {
    NoteItem(
        note = Note(
            id = 0,
            title = "Different title",
            content = "Here is some really long content " +
                    "that will wrap to the next line, and then more wrapping" +
                    "that will wrap to the next line, and then more wrapping",
            colorHex = 0xFF737355,
            created = LocalDateTimeUtil.now()
        ),
        backgroundColor = Color(0xFF737355),
        onNoteClick = {},
        onDeleteClick = {}
    )
}