package com.example.newta.model

data class LatLngModel(
    var user_id: String,
    var latitude: Double,
    var longitude: Double,
    var name: String,
    var capacity: Int
){
    constructor(): this("",0.0,0.0, "", 0){

    }
}
