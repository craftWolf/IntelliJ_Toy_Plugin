# IntelliJ_Toy_Plugin

**IntelliJ_Toy_Plugin** is a plugin built for [IntelliJ IDEA](https://www.jetbrains.com/idea/) that collects statistics -- and presents a summary report -- on methods defined in the currently opened file.

It is a student project which was implemented on the request of a JetBrains representative so that we would show what we are capable of. It was done in order to get a chance to be approved for a project in the TU Delft's course *CSE2000*, Software Project. It was developed by 3 students in less than a week.

## Table of Contents

- [Getting Started](#getting-started)
  - [Installation](#installation)
  - [Run with installation](#run-with-installation)
  - [Run without installation](#run-without-installation)
  - [Generated method metrics](#generated-method-metrics)

- [Built With](#built-with)

- [Developers](#developers)


## Getting Started

### Installation

1) Clone this repository using SSH or HTTPS.
2) Open the project (make sure `Use auto-import` is checked)
3) Inside IntelliJ IDEA terminal invoke `gradle buildPlugin` to create the plugin distribution.
The resulting JAR/ZIP is located in `build/distributions` (from the root of the project).
4) [Install](https://www.jetbrains.com/help/idea/managing-plugins.html#installing-plugins-from-disk) the plugin manually.


### Run with installation

1) Download and install the plugin
2) Make sure the java file you want is opened and you are inside the editor
3) Go to Tools -> Statistic Report;
    - Alternatively, you can press `Ctrl+Alt+M, S`
4) Your report will be generated inside the `/statisticReport` folder (from the root of your project)

### Run without installation

1) Clone this repository using SSH or HTTPS.
2) Open the project (make sure `Use auto-import` is checked)
3) Create a new configuration;
    - Make sure the gradle project is selected
    - In the *Tasks* field add `:runIde` and press *Ok*
4) Run the plugin (Shift+F10)
5) A new IDE window will appear, select your project
6) Go to Tools -> Statistic Report;
    - Alternatively, you can press `Ctrl+Alt+M, S`
7) Your report will be generated inside the `/statisticReport` folder (from the root of your project)


### Generated method metrics

  - Number of method lines
  - Number of statements
  - Number of comments
  - Lines of Code
  - Cyclomatic complexity
  - Does it have JavaDoc comment
  - Number of calls in the whole project
  - Does it override any other method
  - Name of the class with the method it overrides


## Built With
- [IntelliJ IDEA](https://www.jetbrains.com/idea/) - IDE
- [Gradle](http://gradle.org) - Dependency Management


## Developers

* Mihai Plotean
* Mihail Chirosca
* Alex Simonov
