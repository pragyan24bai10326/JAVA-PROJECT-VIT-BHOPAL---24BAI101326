# Campus Course & Records Manager (CCRM)

A comprehensive Java SE console application for managing students, courses, enrollments, grading, and file operations for educational institutions.

## Table of Contents

- [Overview](#overview)
- [Java Evolution Timeline](#java-evolution-timeline)
- [Java Platform Comparison](#java-platform-comparison)
- [Architecture Overview](#architecture-overview)
- [Installation Guide](#installation-guide)
- [Eclipse Setup](#eclipse-setup)
- [Syllabus-by-File Mapping](#syllabus-by-file-mapping)
- [Features](#features)
- [Design Patterns Used](#design-patterns-used)
- [OOP Principles Implemented](#oop-principles-implemented)
- [Getting Started](#getting-started)
- [Usage Examples](#usage-examples)
- [File Structure](#file-structure)
- [Contributing](#contributing)
- [License](#license)

## Overview

The Campus Course & Records Manager (CCRM) is a sophisticated console application built with Java SE that demonstrates modern Java programming concepts, design patterns, and best practices. It provides a complete solution for managing academic records in educational institutions.

### Key Features

- **Student Management**: Create, update, search, and manage student profiles
- **Course Management**: Handle course creation, prerequisites, and scheduling
- **Enrollment System**: Manage student enrollments with credit limits and grade tracking
- **Grading System**: Record grades, calculate GPAs, and generate transcripts
- **Data Persistence**: Import/export data via CSV files with NIO.2
- **Backup & Restore**: Automated backup system with versioning
- **Reports**: Generate comprehensive reports and transcripts
- **Menu-Driven CLI**: User-friendly console interface

## Java Evolution Timeline

### Major Java Versions and Features

• **Java 1.0 (1996)**: Initial release with basic OOP features
• **Java 1.1 (1997)**: Inner classes, JDBC, RMI, Reflection
• **Java 1.2 (1998)**: Collections Framework, Swing, JIT compiler
• **Java 1.3 (2000)**: HotSpot JVM, JNDI, JavaSound
• **Java 1.4 (2002)**: Regular expressions, NIO, XML processing
• **Java 5.0 (2004)**: Generics, Enums, Autoboxing, Enhanced for-loop, Varargs
• **Java 6 (2006)**: Scripting support, JDBC 4.0, Compiler API
• **Java 7 (2011)**: Try-with-resources, Diamond operator, String switch, NIO.2
• **Java 8 (2014)**: Lambda expressions, Streams API, Optional, Date/Time API
• **Java 9 (2017)**: Module system (Project Jigsaw), JShell, HTTP/2 client
• **Java 10 (2018)**: Local variable type inference (var)
• **Java 11 (2018)**: LTS version, HTTP client, String methods
• **Java 12 (2019)**: Switch expressions, Text blocks preview
• **Java 13 (2019)**: Text blocks, Dynamic CDS archives
• **Java 14 (2020)**: Pattern matching, Records preview
• **Java 15 (2020)**: Text blocks, Sealed classes preview
• **Java 16 (2021)**: Records, Pattern matching, Sealed classes
• **Java 17 (2021)**: LTS version, Sealed classes, Pattern matching
• **Java 18 (2022)**: UTF-8 by default, Simple Web Server
• **Java 19 (2022)**: Virtual threads preview, Pattern matching
• **Java 20 (2023)**: Scoped values, Structured concurrency
• **Java 21 (2023)**: LTS version, Virtual threads, Pattern matching, String templates

## Java Platform Comparison

| Feature                    | Java ME          | Java SE           | Java EE                 |
| -------------------------- | ---------------- | ----------------- | ----------------------- |
| **Target Platform**  | Mobile/Embedded  | Desktop/Server    | Enterprise/Web          |
| **API Size**         | Minimal          | Standard          | Extended                |
| **Memory Footprint** | Very Small       | Moderate          | Large                   |
| **Deployment**       | Mobile devices   | Standalone apps   | Application servers     |
| **Core Libraries**   | Limited          | Full JDK          | Enterprise extensions   |
| **Networking**       | Basic            | Full              | Advanced                |
| **Database**         | Limited          | JDBC              | JPA, EJB                |
| **Web Services**     | None             | Basic             | JAX-WS, JAX-RS          |
| **Security**         | Basic            | Standard          | Advanced                |
| **Use Cases**        | IoT, Mobile apps | Desktop, CLI apps | Web apps, Microservices |

## Architecture Overview

### JDK/JRE/JVM Relationship

```
┌─────────────────────────────────────────────────────────┐
│                    Application Layer                    │
│              (CCRM Console Application)                │
├─────────────────────────────────────────────────────────┤
│                      JDK (Java Development Kit)        │
│  ┌─────────────────┬─────────────────┬─────────────────┐ │
│  │   Compiler      │     Tools       │   Libraries     │ │
│  │   (javac)       │   (jar, javadoc)│   (java.*,      │ │
│  │                 │                 │    javax.*)     │ │
│  └─────────────────┴─────────────────┴─────────────────┘ │
├─────────────────────────────────────────────────────────┤
│                    JRE (Java Runtime Environment)       │
│  ┌─────────────────┬─────────────────┬─────────────────┐ │
│  │   Libraries     │   JVM           │   Deployment    │ │
│  │   (java.*,      │   (HotSpot)     │   Technology    │ │
│  │    javax.*)     │                 │   (Java Web     │ │
│  │                 │                 │    Start)       │ │
│  └─────────────────┴─────────────────┴─────────────────┘ │
├─────────────────────────────────────────────────────────┤
│                    JVM (Java Virtual Machine)           │
│  ┌─────────────────┬─────────────────┬─────────────────┐ │
│  │   Class Loader  │   Execution     │   Memory        │ │
│  │   System        │   Engine        │   Management    │ │
│  └─────────────────┴─────────────────┴─────────────────┘ │
└─────────────────────────────────────────────────────────┘
```

### System Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                   │
│                  (CLI Menu System)                     │
├─────────────────────────────────────────────────────────┤
│                    Service Layer                        │
│  ┌─────────────┬─────────────┬─────────────┬──────────┐ │
│  │   Student   │   Course    │ Enrollment  │   File   │ │
│  │   Service   │   Service   │   Service   │ Manager  │ │
│  └─────────────┴─────────────┴─────────────┴──────────┘ │
├─────────────────────────────────────────────────────────┤
│                    Domain Layer                         │
│  ┌─────────────┬─────────────┬─────────────┬──────────┐ │
│  │   Student   │   Course    │ Enrollment  │  Enums   │ │
│  │   Entity    │   Entity    │   Entity    │          │ │
│  └─────────────┴─────────────┴─────────────┴──────────┘ │
├─────────────────────────────────────────────────────────┤
│                    Data Access Layer                    │
│  ┌─────────────┬─────────────┬─────────────┬──────────┐ │
│  │   CSV       │   Text      │   Backup    │   NIO.2  │ │
│  │   Import    │   Export    │   System    │   Files  │ │
│  └─────────────┴─────────────┴─────────────┴──────────┘ │
└─────────────────────────────────────────────────────────┘
```

## Installation Guide

### Windows Installation Steps

#### Step 1: Download Java JDK

1. Visit [Oracle JDK Download](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
2. Download JDK 17 or later for Windows x64
3. Run the installer and follow the setup wizard

#### Step 2: Set Environment Variables

1. Open System Properties → Advanced → Environment Variables
2. Add `JAVA_HOME` variable pointing to your JDK installation directory
3. Add `%JAVA_HOME%\bin` to your `PATH` variable
4. Verify installation by opening Command Prompt and typing: `java -version`

#### Step 3: Verify Installation

```cmd
C:\> java -version
java version "17.0.2" 2022-01-18 LTS
Java(TM) SE Runtime Environment (build 17.0.2+8-LTS-86)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.2+8-LTS-86, mixed mode, sharing)
```

#### Step 4: Compile and Run CCRM

```cmd
C:\> cd /path/to/javaCode
C:\javaCode> javac -d . src/main/java/edu/campus/ccrm/*.java
C:\javaCode> java edu.campus.ccrm.CampusRecordsManager
```

## Eclipse Setup

### 1. Install Eclipse IDE

1. Download Eclipse IDE for Java Developers from [eclipse.org](https://www.eclipse.org/downloads/)
2. Install Eclipse with Java 17+ support

### 2. Import Project

1. Open Eclipse IDE
2. File → Import → General → Existing Projects into Workspace
3. Select the javaCode directory
4. Click Finish

### 3. Configure Project Settings

1. Right-click project → Properties
2. Java Build Path → Libraries → Add Library → JRE System Library
3. Select Java 17 or later
4. Apply and Close

### 4. Run Configuration

1. Right-click `CampusRecordsManager.java`
2. Run As → Java Application
3. The console application will start

## Syllabus-by-File Mapping

| Concept                     | Files                                                           | Description                                           |
| --------------------------- | --------------------------------------------------------------- | ----------------------------------------------------- |
| **OOP Principles**    |                                                                 |                                                       |
| Encapsulation               | `Student.java`, `Course.java`, `Enrollment.java`          | Private fields with public getters, immutable objects |
| Inheritance                 | `AbstractEntity.java`                                         | Base class for common entity functionality            |
| Polymorphism                | `Displayable.java`, `Validatable.java`                      | Interface implementations and method overriding       |
| Abstraction                 | `AbstractEntity.java`, Interfaces                             | Abstract classes and interface definitions            |
| **Design Patterns**   |                                                                 |                                                       |
| Singleton                   | `CampusRecordsManager.java`                                   | Application instance management                       |
| Builder                     | `Student.Builder`, `Course.Builder`, `Enrollment.Builder` | Object construction patterns                          |
| **Java 8+ Features**  |                                                                 |                                                       |
| Lambda Expressions          | `StudentService.java`, `CourseService.java`                 | Functional programming with streams                   |
| Stream API                  | All Service classes                                             | Collection processing and filtering                   |
| Optional                    | Throughout codebase                                             | Null-safe programming                                 |
| Date/Time API               | `Student.java`, `Enrollment.java`                           | Modern date handling                                  |
| **Advanced Features** |                                                                 |                                                       |
| NIO.2                       | `FileDataManager.java`                                        | Modern file I/O operations                            |
| Functional Programming      | `ReportGenerator.java`                                        | Higher-order functions and streams                    |
| Exception Handling          | All classes in `exception` package                            | Custom exceptions and error handling                  |
| Generics                    | `ReportGenerator.java`                                        | Type-safe generic programming                         |
| Collections                 | All domain classes                                              | Modern collection usage                               |
| **File Operations**   |                                                                 |                                                       |
| CSV Processing              | `FileDataManager.java`                                        | Data import/export functionality                      |
| Backup System               | `FileDataManager.java`                                        | Automated backup with NIO.2                           |

## Features

### Core Functionality

#### Student Management

- Create, read, update, and deactivate students
- Search students by name, status, or GPA range
- Generate student profiles and transcripts
- Track enrollment history and academic progress

#### Course Management

- Manage course catalog with prerequisites
- Track course availability and scheduling
- Handle instructor assignments and department organization
- Support for different course levels (undergraduate/graduate)

#### Enrollment System

- Enroll students in courses with validation
- Enforce credit limits per semester (max 21 credits)
- Track enrollment status and grade recording
- Handle withdrawals and incomplete grades

#### Grading System

- Record grades with comprehensive grade types
- Calculate semester and cumulative GPAs
- Generate official transcripts
- Support for pass/fail and letter grades

#### Data Management

- Import/export data via CSV files
- Automated backup and restore functionality
- Data validation and error handling
- Support for bulk operations

### Advanced Features

#### Reporting System

- Generate comprehensive reports using functional programming
- Support for filtered and grouped reports
- Statistical analysis and trend reporting
- Comparative analysis capabilities

#### Backup & Recovery

- Automated daily backups with timestamps
- Versioned backup management
- One-click restore functionality
- Backup manifest and validation

## Design Patterns Used

### 1. Singleton Pattern

- **Implementation**: `CampusRecordsManager`
- **Purpose**: Ensure single application instance
- **Benefits**: Resource management, global access point

### 2. Builder Pattern

- **Implementation**: `Student.Builder`, `Course.Builder`, `Enrollment.Builder`
- **Purpose**: Construct complex objects step by step
- **Benefits**: Flexible object creation, validation during construction

### 3. Strategy Pattern

- **Implementation**: Report generation methods in `ReportGenerator`
- **Purpose**: Interchangeable algorithms for different report types
- **Benefits**: Extensibility, maintainability

### 4. Template Method Pattern

- **Implementation**: Abstract entity validation and display methods
- **Purpose**: Define algorithm structure with customizable steps
- **Benefits**: Code reuse, consistent behavior

## OOP Principles Implemented

### 1. Encapsulation

- **Private fields** with controlled access through getters
- **Immutable objects** using final fields and builder pattern
- **Data hiding** with proper access modifiers

### 2. Inheritance

- **AbstractEntity** base class for common functionality
- **Interface implementations** for consistent behavior
- **Method overriding** for polymorphic behavior

### 3. Polymorphism

- **Interface polymorphism** through `Displayable` and `Validatable`
- **Method overriding** in entity classes
- **Generic programming** in utility classes

### 4. Abstraction

- **Abstract classes** for common entity behavior
- **Interfaces** for defining contracts
- **Implementation hiding** through service layer

## Getting Started

### Prerequisites

- Java JDK 17 or later
- Any text editor or IDE (Eclipse, IntelliJ IDEA, VS Code)

### Quick Start

1. Clone or download the project
2. Navigate to the project directory
3. Compile the application:
   ```bash
   javac -d . src/main/java/edu/campus/ccrm/*.java src/main/java/edu/campus/ccrm/**/*.java
   ```
4. Run the application:
   ```bash
   java edu.campus.ccrm.CampusRecordsManager
   ```

### First Steps

1. Start the application
2. Navigate to "Student Management" to add students
3. Use "Course Management" to create courses
4. Enroll students in courses via "Enrollment Management"
5. Record grades and generate reports

## Usage Examples

### Adding a Student

```
--- Add New Student ---
Enter Student ID: STU001
Enter First Name: John
Enter Last Name: Doe
Enter Email: john.doe@university.edu
Enter Phone Number: (555) 123-4567
Enter Address: 123 Main St, City, State
Enter Date of Birth (yyyy-MM-dd): 2000-01-15
Enter Enrollment Date (yyyy-MM-dd): 2024-01-15
```

### Creating a Course

```
--- Add New Course ---
Enter Course Code: CS101
Enter Course Name: Introduction to Programming
Enter Description: Basic programming concepts and Java fundamentals
Enter Credits (1-6): 3
Enter Department: Computer Science
Enter Instructor: Dr. Smith
Enter Prerequisites (comma-separated, or press Enter for none): 
```

### Generating a Transcript

```
--- Generate Student Transcript ---
Enter Student ID: STU001

============================================================
OFFICIAL TRANSCRIPT
============================================================
Student: John Doe
ID: STU001
Enrollment Date: 2024-01-15
============================================================

SPRING 2024
----------------------------------------
Semester GPA: 3.50

CS101    Introduction to Programming        3   A
MATH101  Calculus I                         4   B+

============================================================
Overall GPA: 3.50
Total Credits: 7
============================================================
```

## File Structure

```
javaCode/
├── src/main/java/edu/campus/ccrm/
│   ├── CampusRecordsManager.java          # Main application class
│   ├── cli/
│   │   └── CLIMenu.java                   # Command-line interface
│   ├── domain/
│   │   ├── Student.java                   # Student entity
│   │   ├── Course.java                    # Course entity
│   │   ├── Enrollment.java                # Enrollment entity
│   │   ├── abstract/
│   │   │   └── AbstractEntity.java        # Base entity class
│   │   ├── enums/
│   │   │   ├── Semester.java              # Semester enumeration
│   │   │   ├── Grade.java                 # Grade enumeration
│   │   │   ├── StudentStatus.java         # Student status
│   │   │   ├── CourseStatus.java          # Course status
│   │   │   ├── EnrollmentStatus.java      # Enrollment status
│   │   │   └── CourseLevel.java           # Course level
│   │   └── interfaces/
│   │       ├── Displayable.java           # Display interface
│   │       └── Validatable.java           # Validation interface
│   ├── service/
│   │   ├── StudentService.java            # Student business logic
│   │   ├── CourseService.java             # Course business logic
│   │   └── EnrollmentService.java         # Enrollment business logic
│   ├── io/
│   │   └── FileDataManager.java           # File I/O operations
│   ├── util/
│   │   └── ReportGenerator.java           # Report generation utilities
│   └── exception/
│       ├── StudentNotFoundException.java  # Student exceptions
│       ├── CourseNotFoundException.java   # Course exceptions
│       ├── EnrollmentNotFoundException.java # Enrollment exceptions
│       ├── InvalidEnrollmentException.java # Validation exceptions
│       ├── InvalidCourseException.java    # Course validation
│       ├── InvalidGradeException.java     # Grade validation
│       ├── DataImportException.java       # Import errors
│       └── DataExportException.java       # Export errors
├── data/                                  # Data storage directory
├── backups/                               # Backup storage directory
└── README.md                              # This documentation
```

## Contributing

We welcome contributions to improve the CCRM system! Please follow these guidelines:

### Development Guidelines

1. **Code Style**: Follow Java naming conventions and best practices
2. **Documentation**: Add Javadoc comments for all public methods
3. **Testing**: Include unit tests for new functionality
4. **Error Handling**: Implement proper exception handling
5. **Performance**: Consider performance implications of changes

### Pull Request Process

1. Fork the repository
2. Create a feature branch
3. Make your changes with proper documentation
4. Test your changes thoroughly
5. Submit a pull request with a clear description

### Code Review Checklist

- [ ] Code follows Java conventions
- [ ] Proper exception handling
- [ ] Javadoc documentation included
- [ ] No hardcoded values
- [ ] Proper use of design patterns
- [ ] Performance considerations addressed

## Acknowledgments

- Java Platform documentation for architectural guidance
- Oracle's Java tutorials for best practices
- Modern Java development community for design pattern examples

---

**Note**: This application demonstrates comprehensive Java SE programming concepts and is suitable for educational purposes, portfolio projects, and as a foundation for larger academic management systems.

For technical support or questions, please refer to the code documentation or create an issue in the project repository.
