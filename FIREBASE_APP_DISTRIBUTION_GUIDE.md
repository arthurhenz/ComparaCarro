# Firebase App Distribution Setup Guide

## ✅ Setup Complete!

Firebase App Distribution has been successfully configured in your ComparaCarro Android project.

## 📋 What Was Configured

1. **Firebase App Distribution Gradle Plugin (v5.2.0)** added to your project
2. **Distribution settings** configured for both `release` and `debug` build variants
3. **Ready to distribute** APKs to your testers

## 🔧 Configuration Files Modified

- `gradle/libs.versions.toml` - Added plugin version
- `build.gradle.kts` (root) - Added plugin declaration
- `app/build.gradle.kts` - Applied plugin and configured distribution settings

## 🚀 How to Use

### Step 1: Authenticate with Firebase

You need to authenticate before distributing your app. Choose one of these methods:

#### Option A: Using Firebase CLI (Recommended)

```bash
# Install Firebase CLI if not already installed
npm install -g firebase-tools

# Login to Firebase
firebase login

# The Gradle plugin will automatically use these credentials
```

#### Option B: Using Service Account (For CI/CD)

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Select your project → Settings → Service Accounts
3. Click "Generate New Private Key"
4. Save the JSON file securely
5. Add to your `app/build.gradle.kts`:

```kotlin
firebaseAppDistribution {
    serviceCredentialsFile = "/path/to/service-account-key.json"
    // ... other settings
}
```

### Step 2: Update Tester Emails

Edit `app/build.gradle.kts` and replace `"your-tester@example.com"` with actual tester emails:

```kotlin
buildTypes {
    release {
        firebaseAppDistribution {
            artifactType = "APK"
            releaseNotes = "New release version for testing"
            testers = "tester1@example.com, tester2@example.com"
            // Or use tester groups:
            // groups = "qa-team, android-testers"
        }
    }
}
```

### Step 3: Distribute Your App

To build and distribute your app to testers:

#### For Release Build:
```bash
# Windows (PowerShell/CMD)
.\gradlew assembleRelease appDistributionUploadRelease

# Linux/Mac
./gradlew assembleRelease appDistributionUploadRelease
```

#### For Debug Build:
```bash
# Windows
.\gradlew assembleDebug appDistributionUploadDebug

# Linux/Mac
./gradlew assembleDebug appDistributionUploadDebug
```

## 📧 Managing Testers

### Add Testers to Your Firebase Project

```bash
.\gradlew appDistributionAddTesters --projectNumber=<your_project_number> --emails="new-tester@example.com"
```

### Remove Testers from Your Firebase Project

```bash
.\gradlew appDistributionRemoveTesters --projectNumber=<your_project_number> --emails="old-tester@example.com"
```

To find your project number:
1. Go to [Firebase Console](https://console.firebase.google.com)
2. Select your project
3. Go to Settings → General
4. Project number is displayed there

## 🎯 Advanced Configuration Options

You can customize your distribution by modifying the `firebaseAppDistribution` block in `app/build.gradle.kts`:

### Using a Release Notes File

```kotlin
firebaseAppDistribution {
    artifactType = "APK"
    releaseNotesFile = "/path/to/release-notes.txt"
    testers = "tester@example.com"
}
```

### Using Tester Groups

```kotlin
firebaseAppDistribution {
    artifactType = "APK"
    releaseNotes = "New features and bug fixes"
    groups = "qa-team, beta-testers"
}
```

### Using a Testers File

```kotlin
firebaseAppDistribution {
    artifactType = "APK"
    releaseNotes = "Testing build"
    testersFile = "/path/to/testers.txt"
}
```

Format of `testers.txt`:
```
tester1@example.com, tester2@example.com, tester3@example.com
```

### Distributing AAB Files

```kotlin
firebaseAppDistribution {
    artifactType = "AAB"
    releaseNotes = "AAB distribution"
    testers = "tester@example.com"
}
```

Then build with:
```bash
.\gradlew bundleRelease appDistributionUploadRelease
```

### Custom APK/AAB Path

```kotlin
firebaseAppDistribution {
    artifactType = "APK"
    artifactPath = "path/to/your/custom.apk"
    releaseNotes = "Custom build"
    testers = "tester@example.com"
}
```

## 🔍 After Distribution

Once your build is uploaded, you'll receive links for:

1. **Firebase Console URI** - View the release in Firebase Console
2. **Testing URI** - Share this with testers to download the app
3. **Binary Download URI** - Direct download link (expires in 1 hour)

## 📱 Tester Experience

1. Testers receive an email invitation with download instructions
2. They can download the app from:
   - Email link
   - Firebase App Distribution Android app
   - Direct download link (if shared)
3. Testers need to accept the invitation within 30 days

## ⚠️ Important Notes

1. **Build Expiration**: Distributed builds expire after 150 days (5 months)
2. **Invitation Expiration**: Tester invitations expire after 30 days
3. **Package Name**: Your app's package name is `com.comparacarro` - this cannot be changed after Firebase registration
4. **Firebase App ID**: Found in your `app/google-services.json` file

## 🐛 Troubleshooting

### Authentication Issues

If you get authentication errors:
```bash
# Re-login to Firebase
firebase logout
firebase login

# Or set Firebase token as environment variable
$env:FIREBASE_TOKEN="your-token-here"  # PowerShell
# or
set FIREBASE_TOKEN=your-token-here     # CMD
```

### Behind a Corporate Proxy

Add to your `gradle.properties`:
```properties
systemProp.javax.net.ssl.trustStore=/path/to/truststore
systemProp.javax.net.ssl.trustStorePassword=password
```

### Debug Mode

For detailed error information:
```bash
.\gradlew assembleRelease appDistributionUploadRelease --stacktrace
```

## 📚 Additional Resources

- [Firebase App Distribution Documentation](https://firebase.google.com/docs/app-distribution)
- [Firebase Console](https://console.firebase.google.com)
- [Gradle Plugin Reference](https://firebase.google.com/docs/app-distribution/android/distribute-gradle)

## 🎉 Quick Start Commands

```bash
# 1. Login to Firebase (if not already)
firebase login

# 2. Update tester emails in app/build.gradle.kts

# 3. Build and distribute
.\gradlew assembleRelease appDistributionUploadRelease

# Done! Your testers will receive email invitations.
```

---

**Need Help?** Check the [Firebase App Distribution documentation](https://firebase.google.com/docs/app-distribution) or reach out to your team.

