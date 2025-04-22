package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.repository.SetInstructionRepository

class GetInstructions(
    private val setInstructionRepository: SetInstructionRepository
) {
    suspend operator fun invoke(setId: Int) = setInstructionRepository.getInstructions(setId)
}
