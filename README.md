# 📱 College Alert App

A real-time notification system for college students and administrators. Built with Kotlin and Firebase, featuring local caching with Room.

## ✨ Features

### 🔐 Authentication & Accounts
- **Secure Login/Sign-up**: Email and password authentication via Firebase.
- **Session Management**: Automatic session restore on app launch.
- **Role-Based Access**: Automatic routing based on user role (Student vs. Administrator).
- **Profile Management**: Update display name and change passwords securely within the app.

### 📢 Alert System (Student)
- **Real-time Sync**: Alerts are synced instantly from Firebase Realtime Database.
- **Categorization**: Filter alerts by **Exam, Seminar, Holiday, Notice,** and **Urgent**.
- **Priority Levels**: Visual indicators for **High, Medium,** and **Low** priority tasks.
- **Local Persistence**: Room database caching for offline viewing of previous alerts.
- **Read/Unread Tracking**: Keep track of what you've seen with unread counts and status indicators.

### 📰 News Feed & Social (Student)
- **Interactive Feed**: View and post campus news updates.
- **Engagement**: Like, Dislike, and Comment on news posts in real-time.
- **Sharing**: Share important news via external apps using the system share sheet.
- **Social Notifications**: Get notified when someone likes or comments on your post.

### 🛠️ Administrator Panel
- **Broadcast System**: Create and send alerts to the entire student body instantly.
- **Notice Management**: View sent history and delete past notices.
- **Role Control**: Secure access restricted to authorized administrator accounts.

### ⚙️ Settings & UI
- **Dark Mode**: Support for system-wide dark and light themes with manual toggle.
- **Profile Customization**: Easily update your campus handle/name.
- **Notification Controls**: Manage in-app and push notification permissions.

---

## 🛠️ Tech Stack
- **Language**: Kotlin
- **Local Database**: Room (Alert Caching)
- **Backend**: Firebase (Auth, Realtime DB, Cloud Messaging)
- **Architecture**: MVVM with LiveData & Coroutines
- **UI**: Material Design 3, ViewBinding

---

## 🚀 Getting Started

### Prerequisites
- Android Studio (Hedgehog or newer recommended)
- JDK 17
- `google-services.json` from your Firebase project

### Installation
1. Clone the repository.
2. Place your `google-services.json` in the `app/` directory.
3. Open the project in Android Studio and sync Gradle.

---

## 🔧 Troubleshooting

### Fixing the `jlink` Executable Error
If you encounter an error like `Cause: jlink executable ... does not exist`, it's because Gradle is pointing to a JRE instead of a full JDK (often caused by VS Code Java extensions).

**To fix in Android Studio:**
1. Go to **File > Settings** (or `Ctrl+Alt+S`).
2. Navigate to **Build, Execution, Deployment > Build Tools > Gradle**.
3. Under **Gradle JDK**, change the selection to the **Embedded JDK** (e.g., `jbr-17` or `jbr-21`).
4. Click **Apply** and **Sync Project**.

**To fix via environment:**
Ensure your `JAVA_HOME` environment variable points to a full JDK installation, not a VS Code extension folder.
