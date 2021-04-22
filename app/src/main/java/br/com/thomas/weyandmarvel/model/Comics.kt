package br.com.thomas.weyandmarvel.model

data class Comics(
    val available: Int,
    val collectionURI: String,
    val items: List<Item>
)

data class Item(
    val resourceURI: String,
    val name: String
)