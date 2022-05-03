package io.parkmobile.parktunes.global.network

object NetConfig {
    const val GET: Int = 0
    const val POST: Int = 1
    const val PUT: Int = 2
    const val DELETE: Int = 3
    const val BASE_URL:String = "https://itunes.apple.com/"
    const val SEARCH_PARAM:String = "search?term="
    const val LIMIT_PARAM = "s&limit="
}