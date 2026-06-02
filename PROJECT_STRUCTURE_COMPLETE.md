# CollageAlert2 - Complete Project Structure Documentation

## 📁 **Project Overview**
CollageAlert2 is a comprehensive Android application for real-time college communication, built with Kotlin, Firebase, and modern Android architecture patterns.

---

## 🏗️ **Root Directory Structure**

```
CollageAlert2/
├── 📁 .git/                          # Git version control
├── 📁 .gradle/                       # Gradle build cache
├── 📁 .idea/                         # Android Studio IDE settings
├── 📁 .kotlin/                       # Kotlin compiler cache
├── 📁 .vscode/                       # VS Code settings
├── 📁 app/                           # Main application module
├── 📁 build/                         # Build output directory
├── 📁 gradle/                        # Gradle wrapper and dependencies
├── 📄 .getignore                     # Custom ignore file
├── 📄 .gitignore                     # Git ignore rules
├── 📄 build.gradle.kts               # Project-level build script
├── 📄 BUSINESS_CASE.md               # Business analysis document
├── 📄 CollageAlert2.code-workspace   # VS Code workspace
├── 📄 CONTRIBUTING.md                # Contribution guidelines
├── 📄 error.log                      # Error logs
├── 📄 gradle.properties              # Gradle configuration
├── 📄 gradlew                        # Gradle wrapper (Unix)
├── 📄 gradlew.bat                    # Gradle wrapper (Windows)
├── 📄 local.properties               # Local SDK paths
├── 📄 PRESENTATION_QUICK_REFERENCE.md # Presentation guide
├── 📄 presentation_scrollable.html   # Scrollable presentation
├── 📄 presentation_slides.html       # Slide-based presentation
├── 📄 PRESENTATION_SLIDES.md         # Presentation content
├── 📄 README.md                      # Project documentation
├── 📄 ROADMAP.md                     # Development roadmap
├── 📄 settings.gradle.kts            # Gradle settings
└── 📄 TECHNICAL_DEEP_DIVE.md         # Technical documentation
```

---

## 📱 **App Module Structure**

### **📁 app/ - Main Application Module**

```
app/
├── 📁 build/                         # Compiled output
├── 📁 release/                       # Release APK and metadata
├── 📁 src/                           # Source code
├── 📄 .gitignore                     # App-specific git ignore
├── 📄 build.gradle.kts               # App-level build script
├── 📄 google-services.json           # Firebase configuration
└── 📄 proguard-rules.pro             # Code obfuscation rules
```

### **📁 src/ - Source Code Directory**

```
src/
├── 📁 androidTest/                   # Instrumented tests
├── 📁 main/                          # Main source code
└── 📁 test/                          # Unit tests
```

---

## 💻 **Main Source Code (src/main/)**

### **📁 java/com/example/collagealert/ - Kotlin Source Files**

#### **🏛️ Architecture Components**

**Core Activities:**
- `MainActivity.kt` - Main app entry point with bottom navigation
- `LoginActivity.kt` - User authentication screen
- `SignUpActivity.kt` - User registration screen
- `AdminActivity.kt` - Admin-specific functionality

**ViewModels (MVVM Pattern):**
- `AuthViewModel.kt` - Authentication logic and state management
- `MainViewModel.kt` - Main app data and business logic
- `MainViewModelFactory.kt` - ViewModel factory for dependency injection

**Fragments (UI Components):**
- `AlertsFragment.kt` - Display and manage alerts
- `AdminAlertsFragment.kt` - Admin alert creation and management
- `NewsFragment.kt` - News feed with social features
- `NotificationFragment.kt` - Notification history
- `SettingsFragment.kt` - User settings and profile management

#### **📊 Data Layer**

**Database (Room):**
- `App Database.kt` - Room database configuration
- `Alert Entity.kt` - Alert data model for local storage
- `Alert Dao.kt` - Data access object for alerts
- `Alert Data.kt` - Alert data class

**Repository Pattern:**
- `AlertRepository.kt` - Data repository for alerts

**Models:**
- `User.kt` - User data model
- `Comment.kt` - Comment data model
- `AppNotification.kt` - Notification data model
- `CloudAlert.kt` - Firebase alert model
- `RealtimeAlert.kt` - Real-time alert model

#### **🎨 UI Components**

**Adapters (RecyclerView):**
- `AlertAdapter.kt` - Alert list display
- `NoticeAdapter.kt` - Notice list display
- `NotificationAdapter.kt` - Notification list display
- `CommentAdapter.kt` - Comment list display

#### **🔧 Services & Utilities**

**Firebase Services:**
- `MyFirebaseMessagingService.kt` - FCM push notification handling
- `CollageAlertApplication.kt` - Application class

**Utilities:**
- `NotificationHelper.kt` - Local notification management

### **📁 kotlin/ - Additional Kotlin Files**

```
kotlin/
├── 📁 models/
│   ├── Post.kt                       # Post data model
│   └── User.kt                       # Extended user model
└── 📁 utils/
    └── Logger.kt                     # Logging utility
```

---

## 🎨 **Resources (src/main/res/)**

### **📁 drawable/ - Vector Graphics & Icons**

**Custom Icons:**
- `ic_launcher_background.xml` - App icon background
- `ic_launcher_foreground.xml` - App icon foreground
- `ic_alerts.xml` - Alert icon
- `ic_activity.xml` - Activity icon
- `ic_settings.xml` - Settings gear icon
- `ic_key.xml` - Password/key icon
- `ic_default_avatar.xml` - Default profile picture
- `ic_notification.xml` - Notification bell
- `ic_like.xml` / `ic_dislike.xml` - Social interaction icons
- `ic_comment.xml` - Comment icon
- `ic_share.xml` - Share icon
- `ic_logout.xml` - Logout icon

**UI Elements:**
- `gradient_background.xml` - Gradient backgrounds
- `circle_shape.xml` - Circular shapes
- `button_row_background.xml` - Button styling
- `profile_image_border.xml` - Profile image borders
- `status_badge_*.xml` - Status indicators

### **📁 layout/ - UI Layouts**

**Activities:**
- `activity_main.xml` - Main app layout with bottom navigation
- `activity_login.xml` - Login screen layout
- `activity_signup.xml` - Registration screen layout
- `activity_admin.xml` - Admin interface layout

**Fragments:**
- `fragment_alerts.xml` - Alert list layout
- `fragment_admin_alerts.xml` - Admin alert management
- `fragment_news.xml` - News feed layout
- `fragment_notifications.xml` - Notification list
- `fragment_settings.xml` - Settings screen with profile

**List Items:**
- `item_alert.xml` - Individual alert card
- `item_notification.xml` - Notification item
- `item_notica.xml` - Notice item
- `item_comment.xml` - Comment item

**Dialogs:**
- `dialog_add_news.xml` - Add news dialog
- `dialog_comments.xml` - Comments dialog

### **📁 values/ - App Resources**

**Configuration Files:**
- `strings.xml` - All app text strings
- `colors.xml` - Color palette
- `themes.xml` - App themes (light/dark)
- `test.xml` - Test configurations

**Night Mode:**
- `values-night/colors.xml` - Dark theme colors
- `values-night/themes.xml` - Dark theme styling

### **📁 menu/ - Navigation Menus**
- `bottom_nav_menu.xml` - Bottom navigation configuration

### **📁 mipmap-*/ - App Icons**
- Multiple density app icons (hdpi, mdpi, xhdpi, xxhdpi, xxxhdpi)
- Adaptive icon support

---

## ⚙️ **Configuration Files**

### **📄 Build Configuration**

**Project Level:**
- `build.gradle.kts` - Project dependencies and plugins
- `settings.gradle.kts` - Module configuration
- `gradle.properties` - Gradle settings
- `libs.versions.toml` - Dependency version catalog

**App Level:**
- `app/build.gradle.kts` - App dependencies, build types, signing

### **📄 Firebase Configuration**
- `google-services.json` - Firebase project configuration

### **📄 Android Configuration**
- `AndroidManifest.xml` - App permissions, activities, services
- `proguard-rules.pro` - Code obfuscation rules

---

## 📚 **Documentation Files**

### **📄 Technical Documentation**
- `TECHNICAL_DEEP_DIVE.md` - Comprehensive technical analysis
- `BUSINESS_CASE.md` - Market analysis and business model
- `README.md` - Project overview and setup instructions
- `ROADMAP.md` - Development roadmap and future plans
- `CONTRIBUTING.md` - Contribution guidelines

### **📄 Presentation Materials**
- `PRESENTATION_SLIDES.md` - Markdown presentation content
- `presentation_slides.html` - Interactive slide presentation
- `presentation_scrollable.html` - Scrollable web presentation
- `PRESENTATION_QUICK_REFERENCE.md` - Presentation day guide

---

## 🔧 **Key Technologies & Dependencies**

### **📱 Android Framework**
- **Language:** Kotlin
- **Min SDK:** 24 (Android 7.0)
- **Target SDK:** 34 (Android 14)
- **Architecture:** MVVM + Repository Pattern

### **🔥 Firebase Services**
- **Authentication:** User login/registration with email verification
- **Realtime Database:** Real-time data synchronization
- **Cloud Messaging:** Push notifications
- **Cloud Storage:** Profile image storage

### **🏗️ Architecture Components**
- **Room:** Local database for offline storage
- **ViewModel:** UI-related data management
- **LiveData:** Observable data holder
- **Navigation Component:** Fragment navigation

### **🎨 UI Libraries**
- **Material Design 3:** Modern UI components
- **Glide:** Image loading and caching
- **RecyclerView:** Efficient list display

---

## 📊 **Project Statistics**

### **📁 File Count**
- **Total Kotlin Files:** 29 main source files
- **Layout Files:** 15 XML layouts
- **Drawable Resources:** 25+ vector graphics
- **Configuration Files:** 10+ build/config files

### **📏 Code Metrics**
- **Architecture Pattern:** MVVM with Repository
- **Database:** Room (local) + Firebase (cloud)
- **Authentication:** Firebase Auth with email verification
- **Notifications:** FCM + Local notifications
- **UI Framework:** Material Design 3

### **🎯 Key Features Implemented**
- ✅ User authentication with email verification
- ✅ Role-based access (admin/student)
- ✅ Real-time push notifications
- ✅ Social news feed with likes/comments
- ✅ Profile management with image upload
- ✅ Dark/light theme support
- ✅ Offline data caching
- ✅ Admin alert broadcasting
- ✅ Notification history

---

## 🚀 **Build & Deployment**

### **📦 Build Outputs**
- `app/release/app-release.apk` - Signed release APK
- `app/build/outputs/` - All build artifacts

### **🔧 Development Tools**
- **IDE:** Android Studio
- **Build System:** Gradle with Kotlin DSL
- **Version Control:** Git
- **Code Editor:** VS Code (secondary)

---

This comprehensive structure shows a well-organized, production-ready Android application following modern development practices and architectural patterns. The project demonstrates clean code organization, proper separation of concerns, and scalable architecture suitable for a college communication platform.

---

## 🔄 **Application Flow - File to File Interactions**

### **📱 App Launch Flow**

```
1. CollageAlertApplication.kt (App Startup)
   ↓
2. LoginActivity.kt (Entry Point)
   ↓ (checks authentication)
   ├── AuthViewModel.kt (Authentication Logic)
   │   ↓ (Firebase Auth + Database)
   │   ├── User.kt (User Model)
   │   └── Firebase Auth Service
   ↓
3. MainActivity.kt OR AdminActivity.kt (Based on Role)
```

### **🔐 Authentication Flow**

```
LoginActivity.kt
├── AuthViewModel.kt
│   ├── Firebase Authentication
│   ├── Firebase Realtime Database (/users)
│   ├── User.kt (Data Model)
│   └── Email Verification Logic
├── SignUpActivity.kt (Registration)
│   └── AuthViewModel.kt (Shared ViewModel)
└── Navigation Decision:
    ├── MainActivity.kt (Student Role)
    └── AdminActivity.kt (Admin Role)
```

### **🏠 Main App Flow (MainActivity.kt)**

```
MainActivity.kt
├── Bottom Navigation Setup
│   ├── AlertsFragment.kt
│   ├── NewsFragment.kt
│   ├── NotificationFragment.kt
│   └── SettingsFragment.kt
├── Firebase Services
│   ├── MyFirebaseMessagingService.kt (FCM)
│   ├── NotificationHelper.kt (Local Notifications)
│   └── Firebase Realtime Database Listeners
└── Fragment Management
    └── Fragment Transaction Manager
```

### **📢 Alert System Flow**

```
AlertsFragment.kt
├── MainViewModel.kt (Business Logic)
│   └── MainViewModelFactory.kt (Dependency Injection)
├── AlertRepository.kt (Data Layer)
│   ├── AlertDao.kt (Room Database Access)
│   ├── AlertEntity.kt (Local Storage Model)
│   └── App Database.kt (Room Configuration)
├── AlertAdapter.kt (UI Display)
│   └── item_alert.xml (Layout)
├── Firebase Integration
│   ├── RealtimeAlert.kt (Firebase Model)
│   ├── CloudAlert.kt (Cloud Data)
│   └── Alert Data.kt (Unified Model)
└── UI Layout
    └── fragment_alerts.xml
```

### **👨‍💼 Admin Flow**

```
AdminActivity.kt
├── AdminAlertsFragment.kt
│   ├── Alert Creation Logic
│   ├── Firebase Database Write
│   └── Push Notification Trigger
├── User Management
└── Analytics Dashboard
```

### **📰 News & Social Flow**

```
NewsFragment.kt
├── News Display Logic
├── Social Features
│   ├── Comment.kt (Comment Model)
│   ├── CommentAdapter.kt (Comment Display)
│   └── item_comment.xml (Comment Layout)
├── Firebase Integration
│   └── News Database Operations
└── UI Components
    ├── fragment_news.xml
    ├── dialog_add_news.xml
    └── dialog_comments.xml
```

### **🔔 Notification Flow**

```
MyFirebaseMessagingService.kt (FCM Service)
├── Receives Push Notifications
├── NotificationHelper.kt (Local Display)
├── AppNotification.kt (Notification Model)
└── Updates UI Components
    ├── NotificationFragment.kt
    ├── NotificationAdapter.kt
    └── item_notification.xml
```

### **⚙️ Settings & Profile Flow**

```
SettingsFragment.kt
├── AuthViewModel.kt (Profile Updates)
├── Firebase Storage (Profile Images)
├── SharedPreferences (App Settings)
├── Theme Management
│   └── AppCompatDelegate (Dark/Light Mode)
└── UI Components
    └── fragment_settings.xml
```

---

## 🗂️ **Data Flow Architecture**

### **📊 MVVM Pattern Implementation**

```
View Layer (Activities/Fragments)
    ↕️ (Data Binding & Observers)
ViewModel Layer (Business Logic)
    ↕️ (Repository Pattern)
Repository Layer (Data Abstraction)
    ↕️ (Data Sources)
Data Layer (Firebase + Room)
```

### **🔄 Detailed Data Flow**

#### **1. User Authentication Flow**
```
LoginActivity.kt
    ↓ (User Input)
AuthViewModel.kt
    ↓ (Firebase Auth API)
Firebase Authentication Service
    ↓ (User Verification)
Firebase Realtime Database (/users)
    ↓ (Role Check)
Navigation Decision
    ├── MainActivity.kt (Student)
    └── AdminActivity.kt (Admin)
```

#### **2. Alert Creation & Distribution Flow**
```
AdminAlertsFragment.kt (Admin Creates Alert)
    ↓ (Alert Data)
Firebase Realtime Database (/notices)
    ↓ (Real-time Sync)
AlertsFragment.kt (Student Receives)
    ↓ (Local Storage)
AlertRepository.kt
    ↓ (Room Database)
AlertDao.kt → AlertEntity.kt
    ↓ (UI Update)
AlertAdapter.kt → RecyclerView Display
```

#### **3. Push Notification Flow**
```
Admin Creates Alert
    ↓ (Firebase Function Trigger)
Firebase Cloud Messaging
    ↓ (Push to Devices)
MyFirebaseMessagingService.kt
    ↓ (Local Processing)
NotificationHelper.kt
    ↓ (System Notification)
Android Notification System
    ↓ (User Interaction)
NotificationFragment.kt (History)
```

#### **4. Social Features Flow**
```
NewsFragment.kt (User Interaction)
    ↓ (Like/Comment Action)
Firebase Realtime Database (/news)
    ↓ (Real-time Update)
CommentAdapter.kt (UI Update)
    ↓ (Display Comments)
Comment.kt (Data Model)
```

---

## 🔧 **Service & Utility Integration**

### **🔥 Firebase Services Integration**

```
CollageAlertApplication.kt (App Init)
├── Firebase Initialization
├── google-services.json (Configuration)
└── Firebase Services:
    ├── FirebaseAuth (Authentication)
    ├── FirebaseDatabase (Real-time Data)
    ├── FirebaseStorage (File Storage)
    └── FirebaseMessaging (Push Notifications)
```

### **💾 Local Storage Integration**

```
App Database.kt (Room Configuration)
├── AlertDao.kt (Alert Operations)
├── AlertEntity.kt (Local Model)
└── Database Migrations
    └── Version Management
```

### **🎨 UI Resource Flow**

```
Activity/Fragment
    ↓ (Layout Inflation)
Layout XML Files
    ↓ (Resource References)
Drawable Resources
    ├── Custom Icons (ic_*.xml)
    ├── Backgrounds (gradient_*.xml)
    └── Shapes (circle_*.xml)
    ↓ (Theme Application)
Theme Resources
    ├── colors.xml
    ├── themes.xml
    └── values-night/ (Dark Theme)
```

---

## 🔄 **Real-time Synchronization Flow**

### **📡 Firebase Real-time Listeners**

```
MainActivity.kt (Listener Setup)
├── Global Notifications Listener
│   └── /global_notifications
├── User Notifications Listener
│   └── /users/{uid}/notifications
└── News Updates Listener
    └── /news

AlertsFragment.kt (Alert Listener)
├── /notices (Cloud Alerts)
└── Local Database Sync

NewsFragment.kt (Social Listener)
├── /news (Posts)
├── /news/{id}/likes (Reactions)
└── /news/{id}/comments (Comments)
```

### **🔄 Data Synchronization Strategy**

```
Cloud Data (Firebase)
    ↕️ (Real-time Sync)
Local Cache (Room)
    ↕️ (Repository Pattern)
ViewModel (Business Logic)
    ↕️ (LiveData Observers)
UI Components (Views)
```

---

## 🎯 **Navigation Flow**

### **📱 Screen Navigation Map**

```
App Launch
    ↓
LoginActivity.kt
    ├── SignUpActivity.kt (Registration)
    └── Authentication Success
        ├── MainActivity.kt (Student)
        │   ├── AlertsFragment.kt
        │   ├── NewsFragment.kt
        │   ├── NotificationFragment.kt
        │   └── SettingsFragment.kt
        └── AdminActivity.kt (Admin)
            └── AdminAlertsFragment.kt
```

### **🔄 Fragment Lifecycle in MainActivity**

```
MainActivity.onCreate()
├── setupNavigation() (Bottom Navigation)
├── Fragment Manager Setup
└── Default Fragment (AlertsFragment)

Bottom Navigation Click
    ↓ (Fragment Transaction)
Fragment Replace
    ├── onCreateView() (Layout Inflation)
    ├── onViewCreated() (Setup Logic)
    └── Observer Setup (LiveData)
```

---

## 📊 **Error Handling & Logging Flow**

### **🚨 Error Propagation**

```
Data Layer (Firebase/Room)
    ↓ (Exception Handling)
Repository Layer
    ↓ (Error Transformation)
ViewModel Layer
    ↓ (LiveData Error States)
UI Layer
    └── Toast/Dialog Display
```

### **📝 Logging Integration**

```
Logger.kt (Utility)
    ↓ (Used Throughout App)
AuthViewModel.kt (Auth Logs)
MainActivity.kt (Navigation Logs)
AlertsFragment.kt (Alert Logs)
```

---

This comprehensive flow diagram shows how every file in your CollageAlert2 project interacts with others, creating a cohesive, well-architected Android application. The MVVM pattern ensures clean separation of concerns, while Firebase provides real-time synchronization and Room handles offline storage.