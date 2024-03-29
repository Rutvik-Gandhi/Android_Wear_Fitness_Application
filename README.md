# Android Wear Fitness Application

This Android application is designed for fitness tracking on wearable devices, specifically targeting Android Wear devices. 
It includes features for heart rate monitoring, sleep pattern analysis, and displaying previous data.

## Features
Heart Rate Monitoring: Allows users to monitor their heart rate using the device's sensors.
Sleep Pattern Analysis: Provides insights into the user's sleep patterns by tracking movement and sleep cycles.
Previous Data Display: Displays previously recorded heart rate and stress level data for reference.

## Components
HeartRateScanActivity.java
Manages heart rate scanning functionality.
Requests permission to access body sensors.
Retrieves and displays real-time heart rate data.
Saves heart rate and stress level data to SharedPreferences.

## MainActivity.java
Main activity of the application.
Allows users to start heart rate scanning, view previous data, and access sleep tracking functionality.

## PreviousDataActivity.java
Displays previously recorded heart rate and stress level data.
Retrieves data from SharedPreferences and displays it in a ListView.

## SleepPatternActivity.java
Tracks the user's sleep patterns using accelerometer data.
Calculates sleep quality based on sleep duration and awake count.
Provides real-time updates on sleep cycles and sleep quality.
Usage

## Heart Rate Monitoring:
Click the "Start Heart Rate Scan" button in the main activity to begin heart rate monitoring.
The heart rate is displayed in real-time, and stress level is calculated based on the heart rate.

## Sleep Pattern Analysis:
Click the "Start Sleep Pattern" button in the main activity to start tracking sleep patterns.
The application analyzes movement using the device's accelerometer to determine sleep cycles and quality.

## View Previous Data:
Click the "Show Previous Data" button in the main activity to view previously recorded heart rate and stress level data.
Technologies Used
Language: Java
IDE: Android Studio
Libraries: Android Sensor API
How to Run
Clone this repository to your local machine.
Open the project in Android Studio.
Build and run the application on an Android Wear device or emulator.

## License
This project is licensed under the MIT License.
