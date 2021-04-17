package com.sandorln.champion.model

import com.google.gson.annotations.SerializedName

class CharacterSkin {
    @SerializedName("id")
    var skId : String = ""
    @SerializedName("num")
    var skNum : String = ""
    @SerializedName("name")
    var skName : String = ""
    @SerializedName("chromas")
    var skChromas : Boolean = false
}