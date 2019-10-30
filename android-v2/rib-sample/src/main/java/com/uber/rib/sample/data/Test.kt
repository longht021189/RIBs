package com.uber.rib.sample.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import library.annotation.gson.FromMap

@FromMap
class Test(
    @Expose
    @SerializedName("test")
    val abc: Int
)