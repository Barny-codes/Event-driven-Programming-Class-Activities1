# Week 5: Advanced Swing Components

This week's self-paced activities practice Swing lists, tables, trees, tabs, menus,
toolbars, dialogs, and a combined file manager. The detailed student handbook is
[Week 5 Self paced Activity.pdf](./Week%205%20Self%20paced%20Activity.pdf).

## Activities

| Activity | Source file | What to try |
| --- | --- | --- |
| 1. JList and custom renderers | `week05-advanced-swing/activity1-jlist-renderer/ContactListDemo.java` | Select contacts and see their status dots and selection feedback. |
| 2. JTable student database | `week05-advanced-swing/activity2-jtable/StudentTableDemo.java` | Edit names/grades, add students, delete selected rows, clear the table, and inspect the GPA colors. |
| 3. JTree and JTabbedPane | `week05-advanced-swing/activity3-jtree-tabs/FileExplorerDemo.java` | Select tree nodes and explore the General, Appearance, and About tabs. |
| 4. Menus, toolbars, and dialogs | `week05-advanced-swing/activity4-menus-dialogs/MiniEditor.java` | Create, open, and save text files; use Edit actions; watch the word count; and test unsaved-change prompts. |
| 5. Capstone file manager | `week05-advanced-swing/activity5-capstone-file-manager/FileManager.java` | Browse folders with the tree, table, Home/Up/Refresh controls, and double-click actions. |

## Requirements

- JDK 8 or newer installed and available as `javac` and `java` in PowerShell.
- Run each activity from its own directory. The activities are standalone Java
  files in the default package and require no third-party libraries.

## Compile and run

From the project root, run the commands for the activity you want. For example,
Activity 1:

```powershell
Set-Location '.\Week5\week05-advanced-swing\activity1-jlist-renderer'
javac ContactListDemo.java
java ContactListDemo
```

Use the corresponding directory and main class for each activity:

| Activity | PowerShell directory | Compile and run |
| --- | --- | --- |
| 1 | `.\Week5\week05-advanced-swing\activity1-jlist-renderer` | `javac ContactListDemo.java`; `java ContactListDemo` |
| 2 | `.\Week5\week05-advanced-swing\activity2-jtable` | `javac StudentTableDemo.java`; `java StudentTableDemo` |
| 3 | `.\Week5\week05-advanced-swing\activity3-jtree-tabs` | `javac FileExplorerDemo.java`; `java FileExplorerDemo` |
| 4 | `.\Week5\week05-advanced-swing\activity4-menus-dialogs` | `javac MiniEditor.java`; `java MiniEditor` |
| 5 | `.\Week5\week05-advanced-swing\activity5-capstone-file-manager` | `javac FileManager.java`; `java FileManager` |

For example, to run Activity 5 from the project root:

```powershell
Set-Location '.\Week5\week05-advanced-swing\activity5-capstone-file-manager'
javac FileManager.java
java FileManager
```

If PowerShell cannot find Java, install a JDK or invoke the executables using
their full paths, for example:

```powershell
& 'C:\Program Files\Java\jdk-21\bin\javac.exe' FileManager.java
& 'C:\Program Files\Java\jdk-21\bin\java.exe' FileManager
```

Replace `jdk-21` with the JDK version installed on your computer.
