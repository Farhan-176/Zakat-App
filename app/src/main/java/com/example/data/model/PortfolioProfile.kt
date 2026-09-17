package com.example.data.model

data class PortfolioProfile(
    val id: String,
    val name: String,
    val iconName: String = "Person",
    val description: String = ""
)

object DefaultProfiles {
    val defaults = listOf(
        PortfolioProfile("personal", "Personal", "Person", "My individual Zakat portfolio"),
        PortfolioProfile("spouse", "Spouse", "Favorite", "Spouse separate personal assets"),
        PortfolioProfile("family_trust", "Family Trust", "Groups", "Collective family emergency/trust fund"),
        PortfolioProfile("business", "Business Entity", "Store", "Sole proprietorship / Commercial assets")
    )
}
