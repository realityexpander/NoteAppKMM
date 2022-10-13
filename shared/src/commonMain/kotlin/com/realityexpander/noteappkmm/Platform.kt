package com.realityexpander.noteappkmm

import kotlinx.datetime.LocalDateTime

// For Android @Parcelize
@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
expect annotation class CommonParcelize()

// For Android Parcelable
expect interface CommonParcelable

// For Android @TypeParceler
@OptIn(ExperimentalMultiplatform::class)
@OptionalExpectation
@Retention(AnnotationRetention.SOURCE)
@Repeatable
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
expect annotation class CommonTypeParceler<T, P : CommonParceler<in T>>()

// For Android Parceler
expect interface CommonParceler<T>

// For Android @TypeParceler to convert LocalDateTimeParceler
expect object LocalDateTimeParceler: CommonParceler<LocalDateTime>
