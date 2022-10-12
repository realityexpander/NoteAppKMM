package com.realityexpander.noteappkmm

import com.realityexpander.noteappkmm.presentation.*
import kotlinx.datetime.LocalDateTime

// Note: no need to define CommonParcelize here (its optional)
actual interface CommonParcelable

// -----------------------------------

actual annotation class CommonTypeParceler<T, P : CommonParceler<in T>>()
actual interface CommonParceler<T>
actual object LocalDateTimeParceler : CommonParceler<LocalDateTime>
