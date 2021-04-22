package br.com.thomas.weyandmarvel.data.model.character

import com.google.gson.annotations.SerializedName

data class ComicsResponse(
    @SerializedName("available") val available: Int,
    @SerializedName("collectionURI") val collectionURI: String,
    @SerializedName("items") val items: List<ItemResponse>
)

data class ItemResponse(
    @SerializedName("resourceURI") val resourceURI: String,
    @SerializedName("name") val name: String
)