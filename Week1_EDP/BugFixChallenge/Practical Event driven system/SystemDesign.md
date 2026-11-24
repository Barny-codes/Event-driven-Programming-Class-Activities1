# Event-Driven System Design
## System Name: [Fitness Tracker]

# FitPulse Tracker - Event-Driven System Design

System Architecture and Prototype Specification for the **FitPulse Tracker** fitness monitoring system.

---

## Part 1: Event Sources

1. **Heart Rate Monitor / Wearable Sensor**  
   Generates continuous real-time biometric telemetry (`HeartRateSampled`, `AbnormalHeartRateDetected`).
2. **GPS / Accelerometer Sensor**  
   Generates movement and motion data (`StepCountUpdated`, `DistanceThresholdReached`).
3. **Workout Controls (UI Buttons)**  
   Generates manual session lifecycle events (`WorkoutStartRequested`, `WorkoutPauseRequested`, `WorkoutStopRequested`).
4. **Hydration / Nutrition Input Form**  
   Generates manual user entries (`WaterLogged`, `CaloriesLogged`).
5. **System Interval Timer**  
   Generates periodic background tick events (`PeriodicSyncTimerTick`, `InactivityAlertTimerExpired`).

---

## Part 2: Events

| Event Name | Source | Description |
| :--- | :--- | :--- |
| `WORKOUT_STARTED` | Workout Controls | Triggered when the user clicks the "Start Workout" button. |
| `STEP_INCREMENTED` | Accelerometer Sensor | Triggered every time the movement sensor detects a step threshold. |
| `HEART_RATE_UPDATED` | Heart Rate Monitor | Triggered continuously as new heart rate sensor readings arrive. |
| `GOAL_REACHED` | System Logic / Sensors | Triggered when steps, active calories, or duration hit target goals. |
| `WORKOUT_STOPPED` | Workout Controls | Triggered when the user clicks the "Stop Workout" button. |

---

## Part 3: Event Handlers

| Event | Handler Action |
| :--- | :--- |
| `WORKOUT_STARTED` | Initialize active session state, reset session metrics, start background timer thread, and update UI status to "Recording". |
| `STEP_INCREMENTED` | Increment step counter, recalculate estimated calories burned based on user profile, and update UI step label. |
| `HEART_RATE_UPDATED` | Update current heart rate UI display, re-evaluate target heart rate zone, and trigger warning toast if BPM exceeds max safety threshold. |
| `GOAL_REACHED` | Trigger victory notification dialog, play haptic/audio alert, and record milestone achievement in persistent log. |
| `WORKOUT_STOPPED` | Stop background timer thread, freeze session metrics, calculate final statistics, save workout log, and update UI status to "Completed". |

---

## Part 4: Event Flow Diagram

```text
+-------------------+      Generates Event      +----------------------+
|   Event Source    |  ---------------------->  |     Event Object     |
| (Sensors / UI)    |                           | (Action/Sensor Data) |
+-------------------+                           +----------------------+
                                                           |
                                                           v
+-------------------+       Dispatches to       +----------------------+
|  Event Handler    |  <----------------------  | Event Queue / Loop   |
| (Business Logic)  |                           |        (EDT)         |
+-------------------+                           +----------------------+
          |
          v Updates
+-------------------+
| GUI / System State|
+-------------------+
