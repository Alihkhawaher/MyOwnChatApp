# MyOwnChatApp

A simple Android chat application built with Kotlin and Jetpack Compose that integrates with OpenAI's API.

## Features

- Clean, modern Material Design 3 UI
- OpenAI API integration
- Secure API key management
- Real-time chat interface
- Message history with context
- Loading indicators and error handling

## Tech Stack

- Kotlin
- Jetpack Compose
- Material Design 3
- Navigation Compose
- ViewModel
- OkHttp
- Coroutines

## Getting Started

1. Clone the repository
2. Open in Android Studio
3. Build and run the project
4. Enter your OpenAI API key when prompted

## Project Structure

- `app/src/main/java/com/example/myownchat/`
  - `data/` - Data models and utility functions
  - `navigation/` - Navigation setup and routing
  - `screens/` - UI screens (ApiKey, Chat)
  - `viewmodel/` - Business logic and state management
  - `ui/` - Theme and styling

## Build Configuration

- compileSdk: 34
- minSdk: 24
- targetSdk: 34
- Kotlin Compiler Extension: 1.5.1
