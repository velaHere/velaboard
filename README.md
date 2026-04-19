<div align="center">
<a name="top"></a>

# 🎯 Scoreboard API

[![Maven Central](https://img.shields.io/maven-central/v/io.github.velahere/velaboard?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.velahere/velaboard)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://velahere.github.io/velaHere.mit-license/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.20-brightgreen)](https://www.minecraft.net/)
[![GitHub](https://img.shields.io/badge/GitHub-velaHere-blue?logo=github)](https://github.com/velaHere)
[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.java.com/)

**A powerful, lightweight, and easy-to-use Scoreboard & Team management API for Bukkit/Spigot servers.**

Build stunning scoreboards, manage teams, and control gameplay interactions with minimal code.

[Documentation](https://velas.mintlify.app/introduction) • [Discord Community](https://discord.gg/2C63RDgVAn)

</div>

---

## ✨ Features

- ⚡ **Simple & Clean API** — Minimal code, maximum control
- 👤 **Player Sidebars** — Individual scoreboards per player with live updates
- 👥 **Shared Sidebars** — One scoreboard displayed to multiple players
- 🧠 **Advanced Team System** — Prefix, suffix, colors, visibility, and collision rules
- 🎮 **Entity Support** — Add both players and entities to teams
- 🔥 **Flexible Damage Control** — Enable/disable friendly fire for:
  - Player ↔ Player damage
  - Player ↔ Entity damage
  - Entity ↔ Entity damage
- 📡 **Event-Driven Architecture** — Listen to team and scoreboard changes
- 🔄 **Automatic Player Handling** — Built-in join/quit event management
- 🎯 **Version Support** — Currently Compatible with Minecraft 1.20
- 📦 **Lightweight & Zero Dependencies** — Only Bukkit API (except JetBrains annotations)
- 🚀 **Production-Ready** — Tested and battle-hardened

---

## ⚠️ Important Requirements
**This API alone is NOT sufficient to run features in-game.**

To use this library, you MUST have the Velaboard Plugin installed on your Minecraft server.

✅ With plugin → API works perfectly
❌ Without plugin → Classes won't function at runtime
Download Velaboard Plugin: [**Coming Soon**]()

---

## 📥 Installation

### Maven

Add this dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.velahere</groupId>
    <artifactId>velaboard</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>

<repositories>
    <repository>
        <id>maven-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

---

## 📚 Documentation

| Document                 | Purpose                                   |
|--------------------------|-------------------------------------------|
| [**Getting Started**](https://velas.mintlify.app/introduction)        | Setup instructions and core concepts      |
| [**API Reference**](https://velas.mintlify.app/api/scoreboard-api)            | Complete method documentation             |
| [**Examples**](https://velas.mintlify.app/coordinates-sidebar-plugin)                 | Real-world usage examples                 |

---

## 🔗 Links

| Resource                    | Status        |
|-----------------------------|--------------|
| 📦 Maven Central Repository | SNAPSHOT(Release Coming Soon)     |
| 📚 Full Documentation       |  [**Link**](https://velas.mintlify.app/introduction)   |
| 🎨 Spigot Resource Page     | Coming Soon   |
| 💬 Discord Community        | [**Link**](https://discord.gg/2C63RDgVAn)   |

---

## 🚨 Important Notes

### 📌 Version Status
**This is currently a SNAPSHOT build:**
- ✅ Functional and tested  
- ⚠️ API subject to change before `v1.0.0`  
- 📦 Available on Maven Central Snapshots  

<details>
<summary>❓ Common Issues</summary>

**Q: I added the dependency but it doesn't work!**  
A: Make sure the Velaboard plugin is installed on your server.

**Q: NullPointerException when calling the API?**  
A: Verify the plugin is running using `/plugins`.

**Q: Scoreboards not displaying?**  
A: Ensure you called `sidebar.show()` after setup.

</details>


Need help? [Open an issue on GitHub](https://github.com/velaHere/velaboard/issues) 🎯
---

<div align="center">

[⬆ Back to Top](#top)

Made with ❤️ for Minecraft servers

If you find this helpful, please give it a ⭐!
</div>
