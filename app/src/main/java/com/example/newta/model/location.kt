package com.example.newta.model

data class location(
    var id: String,
    var latitude: Double,
    var longitude: Double
){
    constructor(): this("",0.0,0.0){

    }
}
