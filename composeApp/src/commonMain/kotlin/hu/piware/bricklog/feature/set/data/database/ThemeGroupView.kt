package hu.piware.bricklog.feature.set.data.database

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "theme_group_view",
    value = """
        SELECT
            COALESCE(themeGroup, '') as themeGroup,
            COALESCE(theme, '') as theme,
            count(*) as setCount
        FROM sets
        GROUP BY themeGroup, theme
        ORDER BY themeGroup ASC, theme ASC
    """
)
data class ThemeGroupView(
    val themeGroup: String,
    val theme: String,
    val setCount: Int,
)
