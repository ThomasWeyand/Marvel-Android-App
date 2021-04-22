package br.com.thomas.weyandmarvel.presentation.mapper

import br.com.thomas.weyandmarvel.model.CharacterDetailVO
import br.com.thomas.weyandmarvel.model.ResultItem

fun ResultItem.Character.mapToDetail() = CharacterDetailVO(
    id = id,
    isFavorite = isFavorite,
    name = name,
    description = description,
    thumbnailPath = thumbnail
)

fun CharacterDetailVO.mapFromDetail() = ResultItem.Character(
    id = id,
    isFavorite = isFavorite,
    name = name,
    description = description,
    thumbnail = thumbnailPath
)