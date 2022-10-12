package com.realityexpander.noteappkmm.android

import android.app.Application
import android.os.Parcel
import android.os.Parcelable
import com.realityexpander.noteappkmm.CommonParcelable
import com.realityexpander.noteappkmm.presentation.*
import dagger.hilt.android.HiltAndroidApp
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toLocalDateTime

@HiltAndroidApp
class NoteApp: Application()
