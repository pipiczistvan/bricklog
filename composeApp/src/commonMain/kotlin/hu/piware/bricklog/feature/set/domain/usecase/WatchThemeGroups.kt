package hu.piware.bricklog.feature.set.domain.usecase

import hu.piware.bricklog.feature.set.domain.model.SetTheme
import hu.piware.bricklog.feature.set.domain.model.SetThemeGroup
import hu.piware.bricklog.feature.set.domain.repository.SetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class WatchThemeGroups(
    private val setRepository: SetRepository,
) {
    operator fun invoke(): Flow<List<SetThemeGroup>> {
        return setRepository.watchThemeGroups()
            .fixEmptyNamedThemeGroup()
    }
}

/**
 * The set dataset may contain sets with empty theme group names.
 * This method sorts these sets to the correct name groups
 * and renames the empty named set group to 'Other'.
 */
private fun Flow<List<SetThemeGroup>>.fixEmptyNamedThemeGroup() = map { themeGroups ->
    val emptyNamedGroup = themeGroups.find { it.name.isEmpty() }?.toMutable()
    val namedGroups = themeGroups.filter { it.name.isNotEmpty() }
        .map { it.toMutable() }
        .toMutableList()

    namedGroups.forEach { namedGroup ->
        namedGroup.themes.forEach { namedGroupTheme ->
            val emptyNamedTheme = emptyNamedGroup?.themes?.find { it.name == namedGroupTheme.name }
            if (emptyNamedTheme != null) {
                namedGroupTheme.setCount += emptyNamedTheme.setCount
                emptyNamedGroup.themes.remove(emptyNamedTheme)
            }
        }
    }

    emptyNamedGroup?.let {
        it.name = "Other"
        namedGroups.add(it)
    }

    namedGroups.map { namedGroup ->
        namedGroup.toImmutable()
    }.sortedBy { it.name }
}

private fun SetThemeGroup.toMutable(): MutableSetThemeGroup {
    return MutableSetThemeGroup(
        name = name,
        themes = themes.map { it.toMutable() }.toMutableList()
    )
}

private fun MutableSetThemeGroup.toImmutable(): SetThemeGroup {
    return SetThemeGroup(
        name = name,
        themes = themes.map { it.toImmutable() }.toList()
    )
}

private fun SetTheme.toMutable(): MutableSetTheme {
    return MutableSetTheme(
        name = name,
        setCount = setCount
    )
}

private fun MutableSetTheme.toImmutable(): SetTheme {
    return SetTheme(
        name = name,
        setCount = setCount
    )
}

private data class MutableSetThemeGroup(
    var name: String,
    val themes: MutableList<MutableSetTheme>,
)

private data class MutableSetTheme(
    var name: String,
    var setCount: Int,
)
