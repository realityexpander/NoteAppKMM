package com.realityexpander.noteappkmm.domain.note

import com.realityexpander.noteappkmm.*
import com.realityexpander.noteappkmm.presentation.*
import kotlinx.datetime.LocalDateTime

@CommonParcelize
@CommonTypeParceler<LocalDateTime, LocalDateTimeParceler>()
class Note(
    val id: Long?,
    val title: String,
    val content: String,
    val colorHex: Long,
    val created: LocalDateTime,
): CommonParcelable {
    companion object {
        private val colors = listOf(RedOrangeHex, RedPinkHex, LightGreenHex, BabyBlueHex, VioletHex)

        fun generateRandomColor() = colors.random()
    }
}
