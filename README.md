# Simple Discord Bot DMing

## About
**Simple Discord Bot DMing** is a Java-based Discord bot built using JDA (Java Discord API). It automatically sends direct messages (DMs) to all accessible users in a Discord server while avoiding staff members.

This tool is useful for:
- **Testing automated DMs**
- **Experimenting with bot-to-user messaging**
- **Verifying bot permissions and message delivery**

---

## Features
**Automated DM Sending** – No manual work, just run the bot and let it handle messaging.  
**Randomized Messages** – Each DM contains a different pre-set message.  
**File Attachment Support** – Sends a test file along with the message.  
**Intelligent Rate Limiting** – Adds randomized delays (10-19 sec) to prevent Discord rate limits.  
**Excludes Staff Members** – Avoids sending messages to users with mod/admin roles.  

---

## Installation
### **1. Requirements**
- **Java 17+** (Recommended)
- **Gradle/Maven** (Optional, for managing dependencies)
- **JDA (Java Discord API)** – Add this to your `build.gradle`

#### **Gradle**
```gradle
dependencies {
    implementation 'net.dv8tion:JDA:5.0.0-beta.13'
}
