package com.development.legally.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

@IgnoreExtraProperties
data class Event(
    @DocumentId
    val eventId: String = "",

    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String = "",

    @get:PropertyName("TituloEvento")
    @set:PropertyName("TituloEvento")
    var titulo: String = "",

    @get:PropertyName("Tipo")
    @set:PropertyName("Tipo")
    var tipo: String = "",

    @get:PropertyName("Estado")
    @set:PropertyName("Estado")
    var estado: String = "",

    @get:PropertyName("Fecha y hora")
    @set:PropertyName("Fecha y hora")
    var fechaHora: Timestamp? = null,

    @get:PropertyName("Duracion")
    @set:PropertyName("Duracion")
    var duracion: String = "",

    @get:PropertyName("Lugar")
    @set:PropertyName("Lugar")
    var lugar: String = "",

    @get:PropertyName("escripcion")
    @set:PropertyName("escripcion")
    var descripcion: String = "",

    @get:PropertyName("CasoId")
    @set:PropertyName("CasoId")
    var casoRelacionado: String = "",

    @get:PropertyName("Repetir")
    @set:PropertyName("Repetir")
    var repetir: String = "",

    @get:PropertyName("RecordarAntes")
    @set:PropertyName("RecordarAntes")
    var recordar: String = "",

    @get:PropertyName("Participante")
    @set:PropertyName("Participante")
    var participante: String = "",

    val createdAt: Timestamp? = null
)
