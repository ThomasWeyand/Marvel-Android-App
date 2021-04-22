package br.com.thomas.weyandmarvel.data.model

import com.google.gson.annotations.SerializedName

data class ResultDataWrapperResponse<out resultType: ResultWrapper>(
    @SerializedName("code") val code: Int,
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: DataResponse<resultType>
)