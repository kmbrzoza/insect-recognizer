package com.example.insectopedia.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "insects_table")
data class Insect(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "wiki_url") var wikiURL: String,
    @ColumnInfo(name = "photo_path") var photoPath: String,
    @ColumnInfo(name = "discovery_date") var discoveryDate: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}