package com.example.newta.model

data class StoryModel (
    var adminId: String,
    var weekly: String,
    var totalCapacity: Int,
    var date: String = "",
    var nopol: String,
    var distance: Int,
    var time: Int,
    var bahan: Int,
    var price: Int
){
    constructor():this("", "", 0, "","",0,0,0,0)
}