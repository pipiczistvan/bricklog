package hu.piware.bricklog.feature.set.data.database

import androidx.room.DatabaseView
import androidx.room.Embedded
import hu.piware.bricklog.feature.set.domain.model.FUTURE_DAYS_COUNT
import hu.piware.bricklog.feature.set.domain.model.SetStatus

const val FUTURE_MILLISECONDS = FUTURE_DAYS_COUNT * 24 * 60 * 60 * 1000L

@DatabaseView(
    viewName = "set_details_view",
    value = """
        SELECT 
            sets.*,
            CASE
                WHEN launchDate IS NOT NULL
                     AND CURRENT_TIMESTAMP < datetime(launchDate / 1000, 'unixepoch') 
                     AND CURRENT_TIMESTAMP > datetime((launchDate - $FUTURE_MILLISECONDS) / 1000, 'unixepoch') THEN 'ARRIVES_SOON'
                WHEN exitDate IS NOT NULL
                     AND CURRENT_TIMESTAMP <= datetime(exitDate / 1000, 'unixepoch') 
                     AND CURRENT_TIMESTAMP > datetime((exitDate - $FUTURE_MILLISECONDS) / 1000, 'unixepoch') THEN 'RETIRED_SOON'
                WHEN launchDate IS NOT NULL 
                     AND CURRENT_TIMESTAMP >= datetime(launchDate / 1000, 'unixepoch') 
                     AND (exitDate IS NULL OR CURRENT_TIMESTAMP <= datetime(exitDate / 1000, 'unixepoch') ) THEN 'ACTIVE'
                WHEN launchDate IS NOT NULL 
                     AND CURRENT_TIMESTAMP <= datetime((launchDate - $FUTURE_MILLISECONDS) / 1000, 'unixepoch') THEN 'FUTURE_RELEASE'
                WHEN exitDate IS NOT NULL 
                     AND CURRENT_TIMESTAMP > datetime(exitDate / 1000, 'unixepoch')  THEN 'RETIRED'
                ELSE 'UNKNOWN'
            END AS status
        FROM sets
        """,
)
data class SetDetailsView(
    @Embedded val legoSet: SetEntity,
    val status: SetStatus,
)
