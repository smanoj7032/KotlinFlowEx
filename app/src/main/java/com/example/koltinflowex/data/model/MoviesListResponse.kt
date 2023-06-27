package com.example.koltinflowex.data.model

import com.google.gson.annotations.SerializedName

data class MoviesListResponse(
    @SerializedName("page") val page: Int? = null, // 1
    @SerializedName("results") val results: List<Result>? = null,
    @SerializedName("total_pages") val totalPages: Int? = null, // 34218
    @SerializedName("total_results") val totalResults: Int? = null // 684341
)
    data class Result(
        @SerializedName("adult") val adult: Boolean? = null, // false
        @SerializedName("backdrop_path") val backdropPath: String? = null, // /wcKFYIiVDvRURrzglV9kGu7fpfY.jpg
        @SerializedName("genre_ids") val genreIds: List<Int>? = null,
        @SerializedName("id") val id: Int? = null, // 453395
        @SerializedName("original_language") val originalLanguage: String? = null, // en
        @SerializedName("original_title") val originalTitle: String? = null, // Doctor Strange in the Multiverse of Madness
        @SerializedName("overview") val overview: String? = null, // Doctor Strange, with the help of mystical allies both old and new, traverses the mind-bending and dangerous alternate realities of the Multiverse to confront a mysterious new adversary.
        @SerializedName("popularity") val popularity: Double? = null, // 7931.499
        @SerializedName("poster_path") val posterPath: String? = null, // /9Gtg2DzBhmYamXBS1hKAhiwbBKS.jpg
        @SerializedName("release_date") val releaseDate: String? = null, // 2022-05-04
        @SerializedName("title") val title: String? = null, // Doctor Strange in the Multiverse of Madness
        @SerializedName("video") val video: Boolean? = null, // false
        @SerializedName("vote_average") val voteAverage: Double? = null, // 7.5
        @SerializedName("vote_count") val voteCount: Int? = null // 3987
    )



data class MovieDetailsResponse(
    @SerializedName("adult") val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String,
    @SerializedName("belongs_to_collection") val belongsToCollection: Any,
    @SerializedName("budget") val budget: Int,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("homepage") val homepage: String,
    @SerializedName("id") val id: Int,
    @SerializedName("imdb_id") val imdbId: String,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("popularity") val popularity: Double,
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompany>,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountry>,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("revenue") val revenue: Int,
    @SerializedName("runtime") val runtime: Int,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>,
    @SerializedName("status") val status: String,
    @SerializedName("tagline") val tagline: String,
    @SerializedName("title") val title: String,
    @SerializedName("video") val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
) {
    data class Genre(
        @SerializedName("id") val id: Int, @SerializedName("name") val name: String
    )

    data class ProductionCompany(
        @SerializedName("id") val id: Int,
        @SerializedName("logo_path") val logoPath: String,
        @SerializedName("name") val name: String,
        @SerializedName("origin_country") val originCountry: String // US
    )

    data class ProductionCountry(
        @SerializedName("iso_3166_1") val iso31661: String, @SerializedName("name") val name: String
    )

    data class SpokenLanguage(
        @SerializedName("english_name") val englishName: String,
        @SerializedName("iso_639_1") val iso6391: String,
        @SerializedName("name") val name: String
    )
}
