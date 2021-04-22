package br.com.thomas.weyandmarvel.data.model

import br.com.thomas.weyandmarvel.data.model.character.ComicsResponse
import br.com.thomas.weyandmarvel.data.model.character.ThumbnailResponse
import com.google.gson.annotations.SerializedName

sealed class ResultWrapper {

    data class CharacterResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String,
        @SerializedName("description") val description: String,
        @SerializedName("thumbnail") val thumbnail: ThumbnailResponse,
        @SerializedName("comics") val comic: ComicsResponse
    ) : ResultWrapper()

}