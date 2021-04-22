package br.com.thomas.weyandmarvel.model

data class DataResult<out result: ResultItem>(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<result>
)