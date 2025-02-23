package com.alexchern.cryptonewsbot.domain.service

import com.alexchern.cryptonewsbot.domain.model.Filter
import com.alexchern.cryptonewsbot.domain.repository.FilterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FilterService(private val filterRepository: FilterRepository) {

    @Transactional(readOnly = true)
    fun getFilters(): List<Filter> {
        return filterRepository.findAll()
    }

    fun deleteAll() {
        filterRepository.deleteAll()
    }

    fun save(filter: Filter): Filter {
        return filterRepository.save(filter)
    }
}