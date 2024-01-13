package di

import com.prajwal.app.BirdsViewModel
import domain.FetchBirdsDataUseCase
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import service.FetchBirdsDataRepository

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
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
            BirdsViewModel()
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

fun initKoin() = initKoin {}
