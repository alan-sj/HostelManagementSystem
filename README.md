
# HostelManagementSystem

## Introduction

The Hostel Management System is a Java-based application developed with JavaFX, tailored for effective hostel administration. It is an admin-centered system that centralizes key functions like student and staff management, complaint tracking, room allocation, fee management and real-time data visualization. Core features include modules for storing and updating resident and staff details, a complaint management system that tracks issues from submission to resolution, the option to calculate and update the fee status of residents and allot rooms to students. The system's design emphasizes usability and future scalability, ensuring a user-friendly experience while supporting the evolving needs of hostel administration.

## Key Features

### - Students and Staff Management: 
The system enables organized storage, updates, and access to data.
### - Complaint Management:
Complaints reported by students can be viewed in the system, which ensures swift and necessary action to ensure their comfort.
### - Room Allocation:
Students can be allocated to rooms by choice.
### - Attendance:
The option to record each student's attendance is enabled, thereby providing proper statistical data for mess count and fee calculations.
### - Fee Management:
The fee, including mess and additional fees(if required), can be calculated based on students' attendance in the hostel. There is also an option to record and analyze each student's payment status.

## Installation
### Prerequisites
Ensure Java 8 or higher is installed.   

Make sure MySQL is set up and running for server-side operations.  

Ensure required libraries (e.g., JavaFX, MySQL JDBC Connector) are installed.

### Steps
Download and unzip the project file.  

Open the project in your preferred IDE (e.g., IntelliJ, Eclipse).  

Set up your MySQL database and update the database connection settings in the project files(requires manual creation of tables on system, at the moment).  

Run the Main.java file to start the application.  

## Usage
Once the application is running, log in as admin to access the features. The dashboard provides with side navigation bar to navigate to various features of the system.

## Future Enhancements

### Creation of Student Interface
It shall include a dedicated student side interface for students, with following feathres:  

#### - Option to apply for mess cut, if theres' an extended absence at hostel.  
#### - Submit complaints directly to the system.  
#### - View room allocation details.  
#### - Track attendance and fee status.  

## Authors

- Ahmed Zyed
- Alan Saji
- Farhaan Nizam
- Muhammed Zaamil
- Sreyas Warrier

## Tech Stack

**Admin:** JavaFx

**Server:** MySql

