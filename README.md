Smart Pantry Manager

Smart Pantry Manager is an Android app that helps users reduce food waste by suggesting recipes based only on the ingredients they already have in their pantry. Users can add their available ingredients with expiry dates, and the app will only show recipes they can fully make.

Database
The app uses SQLite via the Room persistence library. I chose Room because it works fully on-device without needing internet or a server, it provides compile-time safety for SQL queries, and data persists even after the app is closed or the phone restarts.

How to Run
1. Clone the repository and open the project in Android Studio.
2. Let Gradle sync and download dependencies.
3. Run the app on an Android emulator or physical device running API 24 or higher.

Strict-Matching Rule
The recipe suggestion uses strict matching. If a recipe requires even one ingredient that the user does not have, that recipe is excluded from the results. Only recipes where all required ingredients are available will be shown.