# 📱 College Alert App

A real-time notification system for college students and administrators. Built with Kotlin and Firebase.

## ✨ Features

### For Students
- 📝 **Real-time Alerts**: Receive instant updates for exams, seminars, holidays, notices, and emergencies.
- 📰 **News Feed**: Engage with campus news through likes, dislikes, and comments.
- 🔔 **Push Notifications**: Get notified even when the app is closed.
- 📊 **Alert Tracking**: Keep track of read and unread alerts.
- 🌙 **Dark Mode**: Personalized UI with theme switching support.
- 🔐 **Secure Auth**: Firebase-powered authentication for data safety.

### For Administrators
- 📢 **Broadcast Alerts**: Create and send alerts to the entire student body.
- 🎯 **Categorization**: Organize alerts by type (Exam, Seminar, Holiday, Notice, Urgent).
- ⚡ **Priority Control**: Set priority levels (High, Medium, Low) for critical updates.
- 📋 **Management**: View, manage, and delete alert and news history.

### For Both

- Campus news feed loaded in real time from Firebase
- Post new news items (title and message)
- View all news posts with author name and timestamp
- Like and unlike posts
- Dislike and remove dislike on posts
- Comment on posts with live comment updates
- Share news via other apps (text share intent)
- Delete own posts (or admin can delete any post)
- In-app notifications to post authors on like, dislike, and comment
- Personal notification inbox (likes, comments, dislikes, and related activity)
- Notifications sorted by newest first
- Tap a notification to mark it as read
- Empty state when there are no notifications
- Read/unread status per notification
- View account name and email
- Default profile avatar display
- Change display username
- Change password (with re-login prompt if session is too old)
- Dark mode switch (saved across app restarts)
- View app version and build info
- Sign out with confirmation dialog

## 🛠️ Tech Stack

| Component | Technology |
|-----------|------------|
| Language | Kotlin |
| Local Database | Room (Alert Caching) |
| Backend | Firebase (Auth, Realtime DB, FCM) |
| UI | Material Design 3, ViewBinding |
| Preferences | SharedPreferences (Theme Settings) |
| Async | Coroutines & LiveData |

## 🚀 Key Functionalities

### 📰 Campus News & Social
- **Interactive Feed**: Post updates, share news, and see what's happening on campus.
- **Engagement**: Built-in system for Liking, Disliking, and Commenting on posts.
- **Sharing**: Easily share important news to external apps.
- **Smart Notifications**: Receive alerts when users interact with your posts.

### ⚙️ Account & Settings
- **Profile Customization**: Update your display name and view account details.
- **Security Management**: Update your password directly within the app.
- **UI Preferences**: Toggle between Dark and Light mode for better accessibility.

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17
- Firebase account

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/tsediworku123/mobile-app-project/.git
   ```

2. **Open in Android Studio**
    - File → Open → Select the project folder

3. **Set up Firebase**
    - Add `google-services.json` to the `app/` folder.
    - Enable **Email/Password** Authentication in Firebase Console.
    - Create a **Realtime Database**.

4. **Build and run**
    - Click the **Run** button or use `./gradlew installDebug`.

## 📱 How to Use

### Test Accounts
- **Student**: `kidkinfe7@gmail.com` | `11223344`
- **Admin**: `kidstekinfe21@gmail.com` | `12345678`

## 🎯 Future Enhancements
- 🏢 Department-specific alert channels.
- 📅 Calendar integration for exam schedules.
- 📎 Image and file attachments for alerts.
- 📈 Admin analytics dashboard for student engagement.

## 🤝 Contributing
Contributions are welcome! Feel free to open an issue or submit a pull request.

## 🙏 Acknowledgments
- Firebase Documentation
- Android Developers
- Material Design Team
