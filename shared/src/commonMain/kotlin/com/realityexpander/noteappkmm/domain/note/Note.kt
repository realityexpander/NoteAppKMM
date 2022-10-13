package com.realityexpander.noteappkmm.domain.note

import com.realityexpander.noteappkmm.*
import com.realityexpander.noteappkmm.presentation.*
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.serializers.LocalDateTimeIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
@CommonParcelize
data class Note(
    val id: Long?,
    val title: String,
    val content: String,
    val colorHex: Long,

    @Serializable(with = LocalDateTimeIso8601Serializer::class)
    @CommonTypeParceler<LocalDateTime, LocalDateTimeParceler>()
    val created: LocalDateTime,
): CommonParcelable {

    companion object {
        private val colors = listOf(RedOrangeHex, RedPinkHex, LightGreenHex, BabyBlueHex, VioletHex)
        fun getRandomNoteColor() = colors.random()
    }
}
