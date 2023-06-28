package com.example.koltinflowex.data.model

import com.google.gson.annotations.SerializedName

data class MoviesListResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("results") val results: List<Result>? = null,
    @SerializedName("total_pages") val totalPages: Int? = null,
    @SerializedName("total_results") val totalResults: Int? = null
)

data class Result(
    @SerializedName("adult") val adult: Boolean? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
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
    @SerializedName("adult") val adult: Boolean? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    @SerializedName("belongs_to_collection") val belongsToCollection: Any? = null,
    @SerializedName("budget") val budget: Int? = null,
    @SerializedName("genres") val genres: List<Genre>? = null,
    @SerializedName("homepage") val homepage: String? = null,
    @SerializedName("id") val id: Int? = null,
    @SerializedName("imdb_id") val imdbId: String? = null,
    @SerializedName("original_language") val originalLanguage: String? = null,
    @SerializedName("original_title") val originalTitle: String? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("popularity") val popularity: Double? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("production_companies") val productionCompanies: List<ProductionCompany>? = null,
    @SerializedName("production_countries") val productionCountries: List<ProductionCountry>? = null,
    @SerializedName("release_date") val releaseDate: String? = null,
    @SerializedName("revenue") val revenue: Int? = null,
    @SerializedName("runtime") val runtime: Int? = null,
    @SerializedName("spoken_languages") val spokenLanguages: List<SpokenLanguage>? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("tagline") val tagline: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("video") val video: Boolean? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
    @SerializedName("vote_count") val voteCount: Int? = null
) {
    data class Genre(
        @SerializedName("id") val id: Int? = null, @SerializedName("name") val name: String? = null
    )

    data class ProductionCompany(
        @SerializedName("id") val id: Int? = null,
        @SerializedName("logo_path") val logoPath: String? = null,
        @SerializedName("name") val name: String? = null,
        @SerializedName("origin_country") val originCountry: String? = null// US
    )

    data class ProductionCountry(
        @SerializedName("iso_3166_1") val iso31661: String? = null,
        @SerializedName("name") val name: String? = null
    )

    data class SpokenLanguage(
        @SerializedName("english_name") val englishName: String? = null,
        @SerializedName("iso_639_1") val iso6391: String? = null,
        @SerializedName("name") val name: String? = null
    )
}
