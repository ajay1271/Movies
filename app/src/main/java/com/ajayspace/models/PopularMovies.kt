package com.ajayspace.models

data class PopularMovies(

    val results: List<MovieResult>
)


data class MovieResult(
    val id: Int, val overview: String,
    var poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int,
    var backdrop_path :String
)
////
/////*
// Result(id=1930,
// overview=Peter Parker is an outcast high schooler abandoned by his parents as a boy, leaving him to be raised by his Uncle Ben and Aunt May. Like most teenagers, Peter is trying to figure out who he is and how he got to be the person he is today. As Peter discovers a mysterious briefcase that belonged to his father, he begins a quest to understand his parents' disappearance – leading him directly to Oscorp and the lab of Dr. Curt Connors, his father's former partner. As Spider-Man is set on a collision course with Connors' alter ego, The Lizard, Peter will make life-altering choices to use his powers and shape his destiny to become a hero.,
//  poster_path=/fSbqPbqXa7ePo8bcnZYN9AHv6zA.jpg,
//  release_date=2012-06-23,
//  title=The Amazing Spider-Man,
//  vote_average=6.6,
//  vote_count=14140,
//  backdrop_path=/sLWUtbrpiLp23a0XDSiUiltdFPJ.jpg)
//
// */