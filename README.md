# PawPal – Android Mobile Application for Pet Care Management

## Overview

PawPal is an Android mobile application developed as part of the **Mobile App Development (CIS4034-N)** module for the **MSc Computer Science** programme at **Teesside University**.  
The application is designed to support responsible pet ownership by providing tools for managing pet profiles, scheduling care reminders, and discovering nearby veterinary services.

The project demonstrates the application of modern Android development practices, including Jetpack Compose, MVVM architecture, local data persistence, and optional cloud synchronisation.

---

## Key Features

- Pet profile management with optional image support  
- Care and reminder scheduling  
- Local-first data persistence using Room  
- Optional cloud synchronisation using Firebase Firestore  
- Location-based veterinary service discovery  
- Runtime permission handling aligned with Android best practices  
- Clean, declarative UI implemented with Jetpack Compose  

---

## Technical Stack

- **Language:** Kotlin  
- **UI Framework:** Jetpack Compose  
- **Architecture:** MVVM (Model–View–ViewModel)  
- **Local Persistence:** Room (SQLite abstraction)  
- **Reactive Data:** Kotlin Flow  
- **Cloud Services:** Firebase Firestore (optional sync)  
- **External APIs:** Google Maps / Places API  
- **Project Management:** Agile methodology with Trello  
- **Version Control:** Git & GitHub  

---

## Architecture Overview

The application follows a layered architecture to promote separation of concerns and maintainability:

- **UI Layer:** Jetpack Compose screens and navigation  
- **Presentation Layer:** ViewModels managing UI state and business logic  
- **Data Layer:** Repositories coordinating local and remote data sources  
- **Persistence Layer:** Room database as the primary source of truth  
- **Cloud Layer:** Firebase Firestore for optional data synchronisation  

An offline-first approach is adopted, ensuring the application remains functional without network connectivity.

---

## Data Persistence Strategy

Local storage is implemented using Room, which serves as the primary source of truth.  
All create, update, and delete operations are first committed locally, with reactive updates propagated to the UI using Kotlin Flow.

Cloud synchronisation is optional and user-controlled. When enabled, local data changes are mirrored to Firebase Firestore, maintaining schema consistency between local and cloud representations.

---

## Security, Privacy, and Ethics

- Data is stored within the app’s private sandbox on the device  
- Cloud synchronisation is optional and explicitly controlled by the user  
- Permissions are requested only when required and at the point of use  
- No sensitive personal data is collected  
- Medical diagnosis functionality is intentionally excluded for ethical reasons  

The application design aligns with GDPR principles such as data minimisation, transparency, and user consent.

---

## Agile Development Process

The project was developed using an agile, sprint-based approach inspired by Scrum.  
Development was organised into five sprints, each with defined goals, planning, and review documentation. Sprint plans and reviews were submitted via Blackboard as assessment evidence.

---

## Academic Context

- **Module:** Mobile App Development (CIS4034-N)  
- **Programme:** MSc Computer Science  
- **Institution:** Teesside University  
- **Assessment Type:** Individual Coursework Assignment (ICA)  

This repository is intended for academic evaluation and demonstration of professional mobile application development practices.

---

## Author

**Arkar Myo**  
MSc Computer Science  
Teesside University  

---

## License

This project is provided for academic and demonstration purposes only.  
Reuse or redistribution should be appropriately attributed.
