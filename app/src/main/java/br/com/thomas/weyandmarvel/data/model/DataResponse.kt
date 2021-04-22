package br.com.thomas.weyandmarvel.data.model

import com.google.gson.annotations.SerializedName

data class DataResponse<out resultType: ResultWrapper>(
    @SerializedName("offset") val offset: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("total") val total: Int,
    @SerializedName("count") val count: Int,
    @SerializedName("results") val results: List<resultType>
)