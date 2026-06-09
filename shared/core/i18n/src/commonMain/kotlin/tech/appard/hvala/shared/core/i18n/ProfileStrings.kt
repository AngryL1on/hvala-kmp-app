package tech.appard.hvala.shared.core.i18n

data class ProfileStrings(
    val activeTab: String,
    val archiveTab: String,
    val archiveEmpty: String,
    val listingsEmpty: String,
    val reviews: String,
    val activeListingsCount: (count: Int) -> String,
    val memberSince: (monthYear: String) -> String,
    val sellerNotFound: String,
    val noActiveListings: String,
)

internal fun AppLanguage.profileStrings(): ProfileStrings = when (this) {
    AppLanguage.RU -> ProfileStrings(
        activeTab = "Активные",
        archiveTab = "Архив",
        archiveEmpty = "Архив пуст",
        listingsEmpty = "Нет объявлений",
        reviews = "Отзывы",
        activeListingsCount = { count -> "$count активных объявления" },
        memberSince = { monthYear -> "На Hvala с $monthYear" },
        sellerNotFound = "Продавец не найден",
        noActiveListings = "Нет активных объявлений",
    )
    AppLanguage.EN -> ProfileStrings(
        activeTab = "Active",
        archiveTab = "Archive",
        archiveEmpty = "Archive is empty",
        listingsEmpty = "No listings",
        reviews = "Reviews",
        activeListingsCount = { count -> "$count active listings" },
        memberSince = { monthYear -> "On Hvala since $monthYear" },
        sellerNotFound = "Seller not found",
        noActiveListings = "No active listings",
    )
    AppLanguage.SR -> ProfileStrings(
        activeTab = "Aktivni",
        archiveTab = "Arhiva",
        archiveEmpty = "Arhiva je prazna",
        listingsEmpty = "Nema oglasa",
        reviews = "Recenzije",
        activeListingsCount = { count -> "$count aktivnih oglasa" },
        memberSince = { monthYear -> "Na Hvala od $monthYear" },
        sellerNotFound = "Prodavac nije pronađen",
        noActiveListings = "Nema aktivnih oglasa",
    )
    AppLanguage.CNR -> ProfileStrings(
        activeTab = "Aktivni",
        archiveTab = "Arhiva",
        archiveEmpty = "Arhiva je prazna",
        listingsEmpty = "Nema oglasa",
        reviews = "Recenzije",
        activeListingsCount = { count -> "$count aktivnih oglasa" },
        memberSince = { monthYear -> "Na Hvala od $monthYear" },
        sellerNotFound = "Prodavac nije pronađen",
        noActiveListings = "Nema aktivnih oglasa",
    )
    AppLanguage.HR -> ProfileStrings(
        activeTab = "Aktivni",
        archiveTab = "Arhiva",
        archiveEmpty = "Arhiva je prazna",
        listingsEmpty = "Nema oglasa",
        reviews = "Recenzije",
        activeListingsCount = { count -> "$count aktivnih oglasa" },
        memberSince = { monthYear -> "Na Hvala od $monthYear" },
        sellerNotFound = "Prodavač nije pronađen",
        noActiveListings = "Nema aktivnih oglasa",
    )
    AppLanguage.BS -> ProfileStrings(
        activeTab = "Aktivni",
        archiveTab = "Arhiva",
        archiveEmpty = "Arhiva je prazna",
        listingsEmpty = "Nema oglasa",
        reviews = "Recenzije",
        activeListingsCount = { count -> "$count aktivnih oglasa" },
        memberSince = { monthYear -> "Na Hvala od $monthYear" },
        sellerNotFound = "Prodavac nije pronađen",
        noActiveListings = "Nema aktivnih oglasa",
    )
}
