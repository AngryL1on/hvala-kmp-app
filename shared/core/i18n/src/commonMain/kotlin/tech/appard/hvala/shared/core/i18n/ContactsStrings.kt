package tech.appard.hvala.shared.core.i18n
data class ContactsStrings(
    val screenTitle: String,
    val description: String,
    val emailLabel: String,
    val supportEmail: String,
    val infoEmail: String,
    val socialNetworks: String,
)

internal fun AppLanguage.contactsStrings(): ContactsStrings = when (this) {
    AppLanguage.RU -> ContactsStrings(
        screenTitle = "Контактная информация",
        description = "Если у вас есть вопросы, предложения или комментарии, мы всегда рады услышать от вас. Наша команда всегда готова помочь вам с любыми вопросами, связанными с нашим приложением. Не стесняйтесь обращаться к нам по электронной почте в любое время. Мы ценим ваше мнение и стремимся улучшить наше приложение, чтобы оно было максимально удобным и полезным для вас. Также вы можете связаться с нами через социальные сети - мы активны в Instagram и Facebook.",
        emailLabel = "Email: ",
        supportEmail = "support@hvala.app",
        infoEmail = "info@hvala.app",
        socialNetworks = "Социальные сети",
    )
    AppLanguage.EN -> ContactsStrings(
        screenTitle = "Contact Information",
        description = "If you have any questions, suggestions, or comments, we are always happy to hear from you. Our team is ready to assist you with any inquiries related to our application. Feel free to reach out to us via email at any time. We value your feedback and aim to enhance our application to make it as convenient and useful for you as possible. Additionally, you can contact us through social media; we are active on Instagram and Facebook.",
        emailLabel = "Email: ",
        supportEmail = "support@hvala.app",
        infoEmail = "info@hvala.app",
        socialNetworks = "Social networks",
    )
    AppLanguage.SR -> ContactsStrings(
        screenTitle = "Kontakt informacije",
        description = "Ako imate bilo kakva pitanja, predloge ili komentare, uvek smo srećni da čujemo od vas. Naš tim je spreman da vam pomogne sa bilo kakvim pitanjima vezanim za našu aplikaciju. Slobodno nas kontaktirajte putem e-pošte u bilo koje doba. Cenimo vaše mišljenje i trudimo se da unapredimo našu aplikaciju kako bismo je učinili što je moguće praktičnijom i korisnijom za vas. Takođe nas možete kontaktirati putem društvenih mreža - aktivni smo na Instagramu i Facebooku.",
        emailLabel = "Email: ",
        supportEmail = "support@hvala.app",
        infoEmail = "info@hvala.app",
        socialNetworks = "Društvene mreže",
    )
    AppLanguage.CNR -> ContactsStrings(
        screenTitle = "Kontakt informacije",
        description = "Ako imate bilo kakva pitanja, predloge ili komentare, uvek smo srećni da čujemo od vas. Naš tim je spreman da vam pomogne sa bilo kakvim pitanjima vezanim za našu aplikaciju. Slobodno nas kontaktirajte putem e-pošte u bilo koje doba. Cenimo vaše mišljenje i trudimo se da unapredimo našu aplikaciju kako bismo je učinili što je moguće praktičnijom i korisnijom za vas. Takođe nas možete kontaktirati putem društvenih mreža - aktivni smo na Instagramu i Facebooku.",
        emailLabel = "Email: ",
        supportEmail = "support@hvala.app",
        infoEmail = "info@hvala.app",
        socialNetworks = "Društvene mreže",
    )
    AppLanguage.BS -> ContactsStrings(
        screenTitle = "Kontakt informacije",
        description = "Ako imate bilo kakva pitanja, predloge ili komentare, uvek smo srećni da čujemo od vas. Naš tim je spreman da vam pomogne sa bilo kakvim pitanjima vezanim za našu aplikaciju. Slobodno nas kontaktirajte putem e-pošte u bilo koje doba. Cenimo vaše mišljenje i trudimo se da unapredimo našu aplikaciju kako bismo je učinili što je moguće praktičnijom i korisnijom za vas. Takođe nas možete kontaktirati putem društvenih mreža - aktivni smo na Instagramu i Facebooku.",
        emailLabel = "Email: ",
        supportEmail = "support@hvala.app",
        infoEmail = "info@hvala.app",
        socialNetworks = "Društvene mreže",
    )
    AppLanguage.HR -> ContactsStrings(
        screenTitle = "Kontakt informacije",
        description = "Ako imate bilo kakva pitanja, predloge ili komentare, uvek smo srećni da čujemo od vas. Naš tim je spreman da vam pomogne sa bilo kakvim pitanjima vezanim za našu aplikaciju. Slobodno nas kontaktirajte putem e-pošte u bilo koje doba. Cenimo vaše mišljenje i trudimo se da unapredimo našu aplikaciju kako bismo je učinili što je moguće praktičnijom i korisnijom za vas. Takođe nas možete kontaktirati putem društvenih mreža - aktivni smo na Instagramu i Facebooku.",
        emailLabel = "Email: ",
        supportEmail = "support@hvala.app",
        infoEmail = "info@hvala.app",
        socialNetworks = "Društvene mreže",
    )
}
