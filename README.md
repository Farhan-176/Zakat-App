# Zakat Companion

Zakat Companion is an Android app that helps Muslims organize, understand, and calculate their Zakat with a practical, privacy-conscious workflow. It combines a guided calculator, Islamic jurisprudence references, live Nisab market benchmarks, Hawl tracking, saved calculation history, and asset evidence organization in one place.

## Why This App

Zakat calculations can involve cash, gold, silver, investments, debts, and other assets. Zakat Companion is designed to make that process easier to review and repeat while keeping the user's records stored locally on the device.

> **Important:** This app is an educational and organizational aid, not a substitute for a qualified scholar or financial professional. Users should verify personal circumstances and fiqh questions with a trusted scholar.

## Features

- Guided Zakat calculation flow
- Support for gold and silver Nisab thresholds
- Fiqh guidance for Hanafi, Shafi'i, Maliki, and Hanbali perspectives
- Multiple currency options, including PKR, USD, GBP, EUR, SAR, AED, INR, CAD, AUD, TRY, MYR, and IDR
- Live foreign-exchange rates and grounded gold/silver market benchmarks
- Graceful fallback to FX-calibrated market benchmarks when the Gemini service is unavailable
- 354-day Hawl tracker with configurable milestones and continuity rulings
- Saved calculation history through a local Room database
- Custom profiles for separating household or personal calculations
- Asset groups for organizing verification images, receipts, precious metals, physical assets, and halal portfolios
- PDF report generation and sharing
- Light and dark themes
- Privacy information and settings screens

## Tech Stack

- Kotlin
- Jetpack Compose and Material 3
- AndroidX ViewModel and Lifecycle
- Room for local persistence
- Kotlin Coroutines
- OkHttp and Retrofit/Moshi networking components
- Gemini API integration for grounded Nisab market-rate research
- Firebase App Check and Firebase AI dependencies
- Robolectric, JUnit, Compose UI tests, and Roborazzi screenshot tests

## Requirements

- Android Studio with a compatible Android SDK
- JDK 11 or newer
- Android SDK Platform 36
- An Android device or emulator running Android 7.0 (API 24) or newer

## Getting Started

1. Clone or open the project in Android Studio.
2. Let Gradle sync and install any requested SDK components.
3. Configure the optional Gemini API key as described below.
4. Run the `app` configuration on an emulator or physical Android device.

From the project root, the usual Gradle commands are:

```bash
# Windows
.\gradlew.bat assembleDebug
.\gradlew.bat test

# macOS/Linux
./gradlew assembleDebug
./gradlew test
```

The debug APK is generated under `app/build/outputs/apk/debug/`.

## Configuration

The project uses the Google Secrets Gradle Plugin. Create a local `.env` file from `.env.example` when available and provide the Gemini key used for grounded Nisab lookups:

```text
GEMINI_API_KEY=your_api_key_here
```

The app remains usable without a configured Gemini key. In that case, it uses live foreign-exchange data with calibrated market benchmarks. Network access is required for live rate updates; previously available or fallback values allow the calculation flow to degrade gracefully.

Do not commit `.env`, API keys, signing keys, or other secrets to source control.

## Privacy and Data Handling

- Calculation history, profiles, Hawl settings, and asset-group metadata are stored locally through Room.
- Network access is used for live foreign-exchange and Nisab market-rate lookups.
- Images and evidence should only be added with the user's permission and awareness of Android storage and sharing behavior.
- Review the in-app Privacy information before using the app with sensitive financial records.

## Project Structure

```text
app/src/main/java/com/example/
├── data/
│   ├── local/       # Room database, DAOs, and converters
│   ├── model/       # Zakat, currency, fiqh, asset, and Hawl models
│   ├── repository/  # Data access abstractions
│   └── service/     # Grounded market-rate service
├── ui/
│   ├── components/  # Shared Compose components and dialogs
│   ├── screens/     # App screens and calculation flow
│   └── theme/       # Compose theme, colors, and typography
└── util/             # PDF export and portability helpers
```

## Project Team

### Farhan Afridi - Creator & Lead Developer

[LinkedIn](https://www.linkedin.com/in/farhanafrididev)

Farhan Afridi is the maker of Zakat Companion and contributed to the product concept, Android implementation, user experience, Zakat calculation flow, and overall project delivery.

This app was made possible through the team's shared ideas, feedback, testing, encouragement, and collaboration throughout the internship. Every team member played a valuable part in helping shape and complete the final project.

Special thanks to:

- **Samia Noor** - Team Lead, for coordinating the team and keeping the project moving forward
- **Aliza Ahmed** - Builder, for her collaboration and support during development
- **Laiba Ayaz** - Builder, for her collaboration and support during development
- **Muhammad Nihal** - Builder, for his collaboration and support during development
- **Palwasha Sajjad** - Builder, for her collaboration and support during development
- **Syeda Taqiya Noman** - Builder, for her collaboration and support during development

## Project Acknowledgement

This project was developed as part of the Alkhidmat internship final project submission.

- Mentors: [Hamas Malik](https://www.linkedin.com/in/hamas-malik/), [Talha Shahid](https://www.linkedin.com/in/talha-shahid-274941319/), and [Rao Umer](https://www.linkedin.com/in/rao-umer-7a585814a/)
- Internship drive leadership: **Sir Asad Ali**, Senior Manager & HOD, Volunteer Management Program, Alkhidmat Karachi

## License

No open-source license has been declared for this project yet. Contact the project team before reusing or distributing the code.
