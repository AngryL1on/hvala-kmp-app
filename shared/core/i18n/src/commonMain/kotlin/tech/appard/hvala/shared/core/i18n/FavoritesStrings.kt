package tech.appard.hvala.shared.core.i18n

data class FavoritesStrings(
    val empty: String,
    val noFilterResults: String,
    val sort: String,
    val filters: String,
)

internal fun AppLanguage.favoritesStrings(): FavoritesStrings = when (this) {
    AppLanguage.RU -> FavoritesStrings(
        empty = "В избранном пока ничего нет",
        noFilterResults = "Ничего не найдено по фильтрам",
        sort = "Сортировка",
        filters = "Фильтры",
    )
    AppLanguage.EN -> FavoritesStrings(
        empty = "No favorites yet",
        noFilterResults = "Nothing matches your filters",
        sort = "Sort",
        filters = "Filters",
    )
    AppLanguage.SR -> FavoritesStrings(
        empty = "Još nema omiljenih oglasa",
        noFilterResults = "Nema rezultata za filtere",
        sort = "Sortiranje",
        filters = "Filteri",
    )
    AppLanguage.CNR -> FavoritesStrings(
        empty = "Još nema omiljenih oglasa",
        noFilterResults = "Nema rezultata za filtere",
        sort = "Sortiranje",
        filters = "Filteri",
    )
    AppLanguage.HR -> FavoritesStrings(
        empty = "Još nema favorita",
        noFilterResults = "Nema rezultata za filtere",
        sort = "Sortiranje",
        filters = "Filteri",
    )
    AppLanguage.BS -> FavoritesStrings(
        empty = "Još nema omiljenih oglasa",
        noFilterResults = "Nema rezultata za filtere",
        sort = "Sortiranje",
        filters = "Filteri",
    )
}
