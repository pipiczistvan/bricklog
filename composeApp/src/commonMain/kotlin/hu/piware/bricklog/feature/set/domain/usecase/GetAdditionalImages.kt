package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.repository.SetImageRepository

class GetAdditionalImages(
    private val setImageRepository: SetImageRepository
) {
    suspend operator fun invoke(setId: Int) = setImageRepository.getAdditionalImages(setId)
}
