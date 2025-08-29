# How to change Look & feel:
aka. Change Theme of Swing to make it look like intellij.

add to build.gradle:
```
implementation 'com.formdev:flatlaf-intellij-themes:3.2.1'
```
add to SwingMain.java:
```
import com.formdev.flatlaf.intellijthemes.FlatDarkFlatIJTheme;
```
and:
```
public static void main(String[] args) {
        try {
            FlatDarkFlatIJTheme.setup();
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf theme");
            e.printStackTrace();
        }
        ...
```
All Look-and-Feels can be selected here: https://github.com/JFormDesigner/FlatLaf
It should now look like this:
<img width="1370" height="770" alt="grafik" src="https://github.com/user-attachments/assets/d70f6a96-7751-494f-8c75-a81659f27529" />

Enjoy for our next project a better look & feel.
