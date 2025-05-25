package hu.piware.bricklog.feature.collection

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hu.piware.bricklog.feature.collection.domain.model.CollectionId
import hu.piware.bricklog.feature.collection.presentation.collection_edit.CollectionEditScreenRoot
import kotlinx.serialization.Serializable

sealed interface CollectionRoute {
    @Serializable
    data object Graph : CollectionRoute

    @Serializable
    data class CollectionEditScreen(val collectionId: CollectionId = 0) : CollectionRoute
}

fun NavGraphBuilder.collectionGraph(navController: NavHostController) {
    navigation<CollectionRoute.Graph>(
        startDestination = CollectionRoute.CollectionEditScreen(0)
    ) {
        composable<CollectionRoute.CollectionEditScreen> {
            CollectionEditScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}
