# 🧰 GitHub User Activity CLI

A simple and interactive Java-based command-line tool that fetches and displays the recent public GitHub activity of **any user** — directly in your terminal.

---

## 📌 Features

- 🔍 View recent activity of **any GitHub user**
- ✅ No external libraries (pure Java)
- 🌍 Shows your **public IP address**
- ⏳ Displays loading animations and success ✅ status
- 🧠 Parses and shows common event types:
    - Pushed code
    - Opened issues
    - Starred repositories
    - Created repositories
    - Forked projects, etc.

---

## 🖥️ Example

```bash
Enter a GitHub User Name:
torvalds

🌐 Your IP Address: 83.97.142.118
⏳ Fetching activity for user: torvalds...

✅ Done! Showing recent GitHub activity:

🔁 Pushed code to torvalds/linux
   └ Commit: Merge branch 'fix-bug-1290'

⭐ Starred Microsoft/vscode

💬 Opened an issue in kernel/linux
   └ Issue: Compilation error on arch64
