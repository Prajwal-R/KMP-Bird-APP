package di

import com.prajwal.app.BirdsViewModel
import domain.FetchBirdsDataUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.dsl.module
import service.FetchBirdsDataRepository

fun initKoin() =
    startKoin {
        modules(
            sharedViewModelModule +
            sharedUsecaseModule +
            sharedRepositoryModule
        )
    }

val sharedViewModelModule =
    module {
        factory {
            BirdsViewModel(get())
        }
    }
val sharedRepositoryModule =
    module {
        single { FetchBirdsDataRepository() }
    }

val sharedUsecaseModule =
    module {
        factory {
            FetchBirdsDataUseCase(get())
        }
    }

object ViewModelDependencies : KoinComponent {
    fun getBirdsViewModel() = getKoin().get<BirdsViewModel>()
}
