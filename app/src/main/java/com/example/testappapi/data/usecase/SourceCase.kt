package com.example.testappapi.data.usecase

import com.example.testappapi.data.model.sources.Source
import com.example.testappapi.data.repository.SourcesRepository
import io.reactivex.Single
import javax.inject.Inject

class SourceCase @Inject constructor(
    private val sourcesRepository: SourcesRepository
) {

    fun getSource() : Single<ArrayList<Source>> = sourcesRepository.getSources()
}