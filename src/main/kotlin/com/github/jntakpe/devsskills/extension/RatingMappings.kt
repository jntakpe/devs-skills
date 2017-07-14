package com.github.jntakpe.devsskills.extension

import com.github.jntakpe.devsskills.dto.RatingDTO
import com.github.jntakpe.devsskills.model.Rating

fun RatingDTO.toEntity() = Rating(level, accessor)

fun Rating.toDTO() = RatingDTO(level, accessor)