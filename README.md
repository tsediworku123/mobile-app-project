# 📱 College Alert

A real-time notification and alert system for college campuses. This app helps students stay informed about important updates, schedules, and emergencies.

## 🚀 Current Progress

This repository represents the initial development phase of the College Alert system. The core framework, authentication, and real-time data sync are now operational.

### ✅ Completed Features
- **Secure Authentication**: User registration and login using Firebase Authentication.
- **Real-time Alerts**: Integration with Firebase Realtime Database to receive instant notifications.
- **Alert History**: Local storage of alerts using Room Database for offline access.
- **User Profiles**: Basic student profile management and role-based access.

### 🛠️ Tech Stack
- **Language**: Kotlin
- **UI Architecture**: XML with ViewBinding / Material Design 3
- **Local Database**: Room
- **Backend**: Firebase (Auth, Realtime Database)
- **Asynchronous Work**: Kotlin Coroutines & Flow

## 📦 Installation & Setup

1. **Clone the repository**:
   ```bash
   git clone https://github.com/tsediworku123/mobile-app-project.git
   ```
2. **Open in Android Studio**: File → Open → Select the project folder.
3. **Firebase Setup**:
   - Ensure `google-services.json` is present in the `app/` directory.
   - Build and run on an emulator or physical device.

## 🚧 Upcoming Features
- **Admin Dashboard**: A dedicated interface for administrators to broadcast alerts.
- **Push Notifications**: Full integration with Firebase Cloud Messaging (FCM).
- **Advanced Filtering**: Categorizing alerts by department and priority.
- **Attachments**: Support for adding images or documents to alerts.

---
*Developed as part of the Mobile App Development Project.*
