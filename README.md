# 🩸 Donor - blood donation management app<br>

## 📋 Description
Donor is an application developed for simplifying blood donation center operations. 
It offers specific features for different user roles, including donors, medical staff and administrators, allowing everyone
to work securely in one place. The system makes it easy to schedule appointments, record lab test results, and track blood inventory levels,
keeping everything organized and efficient throughout the entire donation process.<br><br>

## ⭐ Key Features
* **`Account Management (common to all users)`**: Secure registration, user authentication, and profile customization.
* **`Donors`**: Schedule and manage appointments, view personal donation history, and track eligibility status. Donors can also download their donation certificate as a PDF file directly from the application for their personal records.
* **`Medics`**: Validate appointments, process donation records, and monitor donor medical history to ensure safety.
* **`Biologists`**: Record laboratory test results and maintain accurate, real-time blood inventory levels.
* **`Administrator`**: User accounts management and system configuration.
* **`Automated Appointment Scheduler`**:
  - Auto-cleanup: Automatically detects and cancels missed appointments, ensuring the database remains accurate and up-to-date without manual intervention.
  - Activity logs: Every automatic action is recorded in the system logs (`donor_app.log`), so it's easy to check the history of what the system has done.<br><br>

## 🛠️ Tech Stack
| **Layer** | **Technologies** |
| :---: | :---: |
| Backend | Java, Spring Boot, Spring Data JPA, Hibernate |
| Frontend | JavaFX, FXML, CSS, Scene Builder |
| Database | MySQL |
| Build & Utilities | Maven, Lombok, Jakarta EE |
| Version Control | Git |

<br><br>

## 📂 Project Structure
```text
Donor-Blood-Transfusion-Center-App/
├── .mvn/                
├── src/
│   ├── main/
│   │   ├── java/org/alex/donor/
|   |   |   ├── config/
│   │   │   ├── controller/      
│   │   │   ├── model/           
│   │   │   ├── repository/      
│   │   │   ├── scheduler/
|   |   |   ├── service/      
│   │   └── resources/
│   │       ├── css/           
│   │       ├── fxml/
|   |       ├── images/           
│   │       └── application.properties
|   ├── test/java/org/alex/donor/
├── .gitattributes            
├── .gitignore
├── Donor_Certificate.pdf
├── README.md
├── cod_baza_de_date.sql
├── donor_app.log
├── mvnw
├── mvnw.cmd
└── pom.xml
```

<br><br>

## 🖼️ Screenshots
### 1. Login Window
<div align="center">
  <img alt="image" src="https://github.com/user-attachments/assets/84f9c1c1-0e7d-4994-a0cd-5f214d061e23" />
</div><br><br>

### 2. Admin dashboard
<div align="center">
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/fdf225c0-42a1-4ad1-9afd-19fcbc65ca1b" /><br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/e7f226a7-e4ad-4675-914c-34f095e9e7fa" />
</div><br><br>

### 3. Medic dashboard
<div align="center">
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/7aab06bb-4c5c-4ec1-bd8c-07b66f1ef5a7" /><br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/561a5f6c-b2d4-46ad-a747-1c20c6e91357" /><br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/4cf25902-9c56-41a6-86d0-f6bf6b3e8ab0" /><br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/31b40709-4d71-437a-9baa-08d37c59e85b" /><br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/d74275ad-5709-4633-8991-bc32c250c693" /><br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/f1deef86-f44f-4a57-af31-e1fd558720a6" /><br><br>
</div>

### 4. Biologist dashboard
<div align="center">
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/08d31864-6bb5-4d02-8ee4-2f22e9ce00ff" /><br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/03a7a16a-b19f-4f9b-9551-7f692b60dfe5" /><br><br>
  <img width="527" height="490" alt="image" src="https://github.com/user-attachments/assets/c2b6b9d9-5272-4161-acea-05c1561cd1ae" /><br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/bdb59010-4388-4779-b2e7-4936b847b467" /><br><br>
  <img width="500" height="397" alt="image" src="https://github.com/user-attachments/assets/79e9c9c3-bc1c-43f2-82f8-ee394041da4d" /><br><br>
</div>

### 5. Donor dashboard
<div align="center">
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/f4b17ae2-2acf-4616-a0e8-7580aadae925" /><br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/c0a4a888-9342-41a2-8287-bdf208e77256" /><br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/5f4c08b0-1b01-4ad4-adc9-339f6beeab8c" />
<br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/8adedb74-84b5-4247-9dae-42029b179d2f" />
<br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/9cc5ceaf-5e17-4784-846e-8684e600259d" />
<br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/5190de76-6ca7-4093-a549-2b69f047c4fd" />
<br><br>
  <img width="600" height="500" alt="image" src="https://github.com/user-attachments/assets/ee587fb1-d161-4429-8580-6d322e674fc2" />
<br><br>
  ⚠️ WARNING !! The data presented in this document is fictitious and does not represent real personal data !! <br><br>
  <img width="612" height="683" alt="image" src="https://github.com/user-attachments/assets/caae4c7d-48c7-4e69-b5cf-7c85eb328c37" />
</div>













