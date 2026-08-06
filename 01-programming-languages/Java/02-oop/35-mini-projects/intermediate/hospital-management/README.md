# Hospital Management System

## Project Overview

A Hospital Management System that manages patients, doctors, appointments, and medical records. This project introduces the Composite pattern for department hierarchies, the Builder pattern for complex object construction, and the Command pattern for undoable operations. Students will design a system that handles complex relationships between healthcare entities.

## Learning Outcomes

- Implement the Builder pattern for complex object creation
- Use the Composite pattern for organizational hierarchies
- Apply the Command pattern for undoable operations
- Design complex many-to-many relationships
- Implement validation for sensitive data
- Use enums extensively for status management
- Practice proper encapsulation of medical data

## Requirements

### Functional Requirements

| ID | Requirement | Priority |
|----|-------------|----------|
| FR01 | Register patients with personal and medical information | Must |
| FR02 | Add doctors with specialties and schedules | Must |
| FR03 | Schedule appointments with conflict detection | Must |
| FR04 | Create and manage medical records | Must |
| FR05 | Manage hospital departments | Must |
| FR06 | View doctor availability | Must |
| FR07 | Cancel appointments with reason | Must |
| FR08 | View patient medical history | Should |
| FR09 | Department hierarchy (Ward, Unit, Room) | Should |
| FR10 | Prescription management | Could |

### Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR01 | HIPAA-like data privacy considerations |
| NFR02 | No double-booking of doctors |
| NFR03 | Appointment conflict detection |
| NFR04 | Immutable medical records |

## Architecture

```mermaid
graph TB
    subgraph Presentation Layer
        Main[Main.java]
        CLI[ConsoleUI.java]
    end
    
    subgraph Service Layer
        PatientService[Patient Service]
        DoctorService[Doctor Service]
        AppointmentService[Appointment Service]
        MedicalRecordService[Medical Record Service]
        DepartmentService[Department Service]
    end
    
    subgraph Core Components
        PatientManager[Patient Manager]
        AppointmentManager[Appointment Manager]
        DepartmentHierarchy[Department Hierarchy]
    end
    
    subgraph Patterns
        Builder[Builder Pattern]
        Composite[Composite Pattern]
        Command[Command Pattern]
    end
    
    subgraph Storage
        PatientDB[(Patient DB)]
        DoctorDB[(Doctor DB)]
        AppointmentDB[(Appointment DB)]
        MedicalRecordDB[(Medical Record DB)]
    end
    
    Main --> CLI
    CLI --> PatientService
    CLI --> DoctorService
    CLI --> AppointmentService
    PatientService --> PatientManager
    AppointmentService --> AppointmentManager
    DepartmentService --> DepartmentHierarchy
    PatientManager --> Builder
    DepartmentHierarchy --> Composite
    AppointmentService --> Command
```

## Package Structure

```
hospital-management/
├── README.md
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── academy/
│                   └── hospital/
│                       ├── Main.java
│                       ├── model/
│                       │   ├── Patient.java
│                       │   ├── Doctor.java
│                       │   ├── Appointment.java
│                       │   ├── MedicalRecord.java
│                       │   ├── Department.java
│                       │   ├── Ward.java
│                       │   ├── Room.java
│                       │   └── enums/
│                       │       ├── BloodType.java
│                       │       ├── Specialization.java
│                       │       ├── AppointmentStatus.java
│                       │       └── RecordType.java
│                       ├── builder/
│                       │   ├── PatientBuilder.java
│                       │   ├── DoctorBuilder.java
│                       │   └── AppointmentBuilder.java
│                       ├── composite/
│                       │   ├── DepartmentComponent.java
│                       │   ├── Department.java
│                       │   ├── Ward.java
│                       │   └── Room.java
│                       ├── command/
│                       │   ├── Command.java
│                       │   ├── ScheduleAppointmentCommand.java
│                       │   ├── CancelAppointmentCommand.java
│                       │   └── CommandManager.java
│                       ├── service/
│                       │   ├── PatientService.java
│                       │   ├── DoctorService.java
│                       │   ├── AppointmentService.java
│                       │   ├── MedicalRecordService.java
│                       │   └── DepartmentService.java
│                       └── exception/
│                           ├── AppointmentConflictException.java
│                           ├── PatientNotFoundException.java
│                           ├── DoctorNotFoundException.java
│                           └── ValidationException.java
└── src/
    └── test/
        └── java/
            └── com/
                └── academy/
                    └── hospital/
                        ├── AppointmentServiceTest.java
                        ├── PatientServiceTest.java
                        ├── DepartmentCompositeTest.java
                        └── BuilderTest.java
```

## Class Diagram

```mermaid
classDiagram
    class Patient {
        -String patientId
        -String firstName
        -String lastName
        -LocalDate dateOfBirth
        -BloodType bloodType
        -String phone
        -String email
        -List~MedicalRecord~ medicalRecords
        +Patient(PatientBuilder)
        +getPatientId() String
        +addMedicalRecord(MedicalRecord) void
        +getMedicalHistory() List~MedicalRecord~
    }
    
    class Doctor {
        -String doctorId
        -String firstName
        -String lastName
        -Specialization specialization
        -List~LocalDateTime~ availableSlots
        -double rating
        +Doctor(DoctorBuilder)
        +getDoctorId() String
        +isAvailable(LocalDateTime) boolean
        +getAvailableSlots() List~LocalDateTime~
    }
    
    class Appointment {
        -String appointmentId
        -Patient patient
        -Doctor doctor
        -LocalDateTime dateTime
        -AppointmentStatus status
        -String reason
        -String notes
        +Appointment(AppointmentBuilder)
        +getAppointmentId() String
        +getStatus() AppointmentStatus
        +cancel(String reason) void
    }
    
    class MedicalRecord {
        -String recordId
        -String patientId
        -String doctorId
        -RecordType type
        -LocalDateTime createdAt
        -String content
        -boolean isImmutable
        +MedicalRecord(id, patientId, doctorId, type, content)
        +getRecordId() String
        +getContent() String
    }
    
    class DepartmentComponent {
        <<interface>>
        +getName() String
        +getDescription() String
        +getCapacity() int
    }
    
    class Department {
        -String name
        -String description
        -List~DepartmentComponent~ components
        +Department(name, description)
        +add(DepartmentComponent) void
        +remove(DepartmentComponent) void
        +getCapacity() int
    }
    
    class Ward {
        -String wardId
        -String name
        -int bedCount
        +Ward(id, name, bedCount)
        +getCapacity() int
    }
    
    class Room {
        -String roomNumber
        -boolean isPrivate
        +Room(number, isPrivate)
        +getCapacity() int
    }
    
    class Command {
        <<interface>>
        +execute() void
        +undo() void
        +getDescription() String
    }
    
    class CommandManager {
        -Stack~Command~ commandHistory
        -Stack~Command~ undoHistory
        +executeCommand(Command) void
        +undo() void
        +redo() void
    }
    
    Patient --> MedicalRecord
    Appointment --> Patient
    Appointment --> Doctor
    Appointment --> AppointmentStatus
    Doctor --> Specialization
    MedicalRecord --> RecordType
    DepartmentComponent <|.. Department
    DepartmentComponent <|.. Ward
    DepartmentComponent <|.. Room
    Department o-- DepartmentComponent
    Command <|.. ScheduleAppointmentCommand
    Command <|.. CancelAppointmentCommand
```

---

**[Continue to Part 2: Implementation Guide →](README-part2.md)**