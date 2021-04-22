package br.com.thomas.weyandmarvel.domain

import br.com.thomas.weyandmarvel.data.model.ResultWrapper
import br.com.thomas.weyandmarvel.data.model.character.ComicsResponse
import br.com.thomas.weyandmarvel.data.model.character.ItemResponse
import br.com.thomas.weyandmarvel.extension.changeToHttps
import br.com.thomas.weyandmarvel.model.Comics
import br.com.thomas.weyandmarvel.model.Item
import br.com.thomas.weyandmarvel.model.ResultItem

fun ResultWrapper.CharacterResponse.mapFromResponse(isFavorite: Boolean) =
    ResultItem.Character(
        id = id,
        name = name,
        description = description,
        thumbnail = "${thumbnail.path}.${thumbnail.extension}".changeToHttps(),
        isFavorite = isFavorite
    )

fun ComicsResponse.mapFromResponse() =
    Comics(
        available = available,
        collectionURI = collectionURI,
        items = items.map { it.mapFromResponse() }
    )

fun ItemResponse.mapFromResponse() =
    Item(
        resourceURI = resourceURI,
        name = name
    )