package hu.piware.bricklog.feature.set.presentation.theme_list

import hu.piware.bricklog.feature.set.domain.model.SetThemeGroup

data class ThemeListState(
    val themeGroups: List<SetThemeGroup> = emptyList(),
)
