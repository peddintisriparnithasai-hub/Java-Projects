import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
public class ClinicManagementSystem {

    static Scanner sc = new Scanner(System.in);

    static final Path APPOINTMENT_FILE =
            Path.of("appointments.txt");

    static final Path PRESCRIPTION_FILE =
            Path.of("prescriptions.txt");

    static ArrayList<Appointment> appointments =
            new ArrayList<>();

    static ArrayList<Prescription> prescriptions =
            new ArrayList<>();

    static HashMap<String, Patient> patientsById =
            new HashMap<>();

    static HashSet<String> patientIds =
            new HashSet<>();

    public static void main(String[] args) {

        loadData();

        int choice;

        do {
            System.out.println(
                "\n==========================================");
            System.out.println(
                "        CLINIC MANAGEMENT SYSTEM");
            System.out.println(
                "==========================================");

            System.out.println("1. Appointment Manager");
            System.out.println("2. Prescription Manager");
            System.out.println("3. View All Appointments");
            System.out.println("4. Search Patient");
            System.out.println("5. Exit");

            choice = readInt("\nEnter your choice: ");

            switch (choice) {
                case 1:
                    appointmentManager();
                    break;

                case 2:
                    prescriptionManager();
                    break;

                case 3:
                    viewAppointments();
                    break;

                case 4:
                    searchPatient();
                    break;

                case 5:
                    saveData();
                    System.out.println(
                        "\nThank you for using the Clinic Management System!");
                    break;

                default:
                    System.out.println("\nInvalid choice!");
            }

        } while (choice != 5);

        sc.close();
    }

    static void appointmentManager() {

        int choice;

        do {
            System.out.println(
                "\n==========================================");
            System.out.println(
                "           APPOINTMENT MANAGER");
            System.out.println(
                "==========================================");

            System.out.println("1. Book New Appointment");
            System.out.println("2. View Appointments");
            System.out.println("3. Search Patient");
            System.out.println("4. Cancel Appointment");
            System.out.println("5. Back");

            choice = readInt("\nEnter your choice: ");

            switch (choice) {
                case 1:
                    bookAppointment();
                    break;

                case 2:
                    viewAppointments();
                    break;

                case 3:
                    searchPatient();
                    break;

                case 4:
                    cancelAppointment();
                    break;

                case 5:
                    break;

                default:
                    System.out.println("\nInvalid choice!");
            }

        } while (choice != 5);
    }

    static void bookAppointment() {

        System.out.println(
            "\n==========================================");
        System.out.println("             BOOK APPOINTMENT");
        System.out.println(
            "==========================================");

        String name = readText("Enter patient full name: ");

        int age = readInt("Enter patient age: ");

        int weight = readInt("Enter patient weight (kg): ");

        if (age <= 0 || age > 120 ||
                weight <= 0 || weight > 500) {

            System.out.println(
                "Invalid age or weight. Please try again.");
            return;
        }

        String id = readText(
            "Enter patient identification number: ");

        if (!isValidPatientId(id)) {

            System.out.println("\nInvalid ID!");
            System.out.println(
                "Patient ID must contain exactly 12 digits.");
            return;
        }

        if (patientIds.contains(id)) {

            System.out.println(
                "\nThis Patient ID is already registered.");
            System.out.println(
                "Please use Search Patient to view the record.");
            return;
        }

        Patient patient = new Patient(
            name, age, weight, id);

        System.out.println(
            "\n==========================================");
        System.out.println("             PATIENT DETAILS");
        System.out.println(
            "==========================================");

        System.out.println(patient);

        boolean check = readBoolean(
            "\nAre these details correct? (true/false): ");

        if (!check) {

            System.out.println(
                "\nPlease enter the appointment again.");
            return;
        }

        String[] specialties = {
            "General Physician",
            "Pediatrics",
            "Gynecology",
            "Dermatology",
            "Orthopedics",
            "ENT",
            "Cardiology",
            "Gastroenterology",
            "Neurology",
            "Urology",
            "Endocrinology",
            "Pulmonologist"
        };

        System.out.println(
            "\n==========================================");
        System.out.println("               SPECIALTIES");
        System.out.println(
            "==========================================");

        System.out.println(
            "1. General Physician - General health problems");
        System.out.println(
            "2. Pediatrics - Children's health");
        System.out.println(
            "3. Gynecology - Women's health");
        System.out.println(
            "4. Dermatology - Skin, hair and nail problems");
        System.out.println(
            "5. Orthopedics - Bones, joints and muscles");
        System.out.println(
            "6. ENT - Ear, Nose and Throat problems");
        System.out.println(
            "7. Cardiology - Heart-related problems");
        System.out.println(
            "8. Gastroenterology - Digestive system problems");
        System.out.println(
            "9. Neurology - Brain and nervous system problems");
        System.out.println(
            "10. Urology - Urinary system problems");
        System.out.println(
            "11. Endocrinology - Hormones and glands");
        System.out.println(
            "12. Pulmonologist - Lungs and breathing problems");

        System.out.println(
            "\nIf unsure, choose General Physician.");
        System.out.println(
            "The staff can guide you if necessary.");

        int specialtyChoice =
            readInt("\nChoose specialty: ");

        if (specialtyChoice < 1 ||
                specialtyChoice > specialties.length) {

            System.out.println("\nInvalid specialty!");
            return;
        }

        String specialty =
            specialties[specialtyChoice - 1];

        System.out.println(
            "\n==========================================");
        System.out.println("                  SHIFTS");
        System.out.println(
            "==========================================");

        System.out.println("1. 8 AM - 4 PM");
        System.out.println("2. 4 PM - 12 AM");
        System.out.println("3. 12 AM - 8 AM");

        int shiftChoice = readInt("\nChoose shift: ");

        String shift;

        switch (shiftChoice) {
            case 1:
                shift = "8 AM - 4 PM";
                break;

            case 2:
                shift = "4 PM - 12 AM";
                break;

            case 3:
                shift = "12 AM - 8 AM";
                break;

            default:
                System.out.println("\nInvalid shift!");
                return;
        }

        ArrayList<String> doctors =
            getAvailableDoctors(specialty, shift);

        System.out.println(
            "\n==========================================");
        System.out.println("            AVAILABLE DOCTORS");
        System.out.println(
            "==========================================");

        System.out.println("Specialty : " + specialty);
        System.out.println("Shift     : " + shift);

        if (doctors.isEmpty()) {

            System.out.println(
                "No doctor is directly available.");

        } else {

            System.out.println("Available Doctors:");

            for (int i = 0; i < doctors.size(); i++) {

                System.out.println(
                    (i + 1) + ". " + doctors.get(i));
            }
        }

        System.out.println(
            "\n0. I am unsure - Let the staff choose");

        int doctorChoice = readInt("\nChoose doctor: ");

        String doctor;

        if (doctorChoice == 0) {

            doctor = "Assigned by Staff";

        } else if (doctorChoice >= 1 &&
                doctorChoice <= doctors.size()) {

            doctor = doctors.get(doctorChoice - 1);

        } else {

            System.out.println("\nInvalid doctor choice!");
            return;
        }

        String date = readText(
            "\nEnter appointment date: ");

        Appointment appointment = new Appointment(
            patient, specialty, shift, doctor, date);

        appointments.add(appointment);

        patientsById.put(id, patient);
        patientIds.add(id);

        saveData();

        System.out.println(
            "\n==========================================");
        System.out.println("          APPOINTMENT CONFIRMED");
        System.out.println(
            "==========================================");

        System.out.println("Patient    : " + name);
        System.out.println("Patient ID : " + id);
        System.out.println("Specialty  : " + specialty);
        System.out.println("Shift      : " + shift);
        System.out.println("Doctor     : " + doctor);
        System.out.println("Date       : " + date);

        System.out.println(
            "==========================================");
    }

    static ArrayList<String> getAvailableDoctors(
            String specialty, String shift) {

        ArrayList<String> doctors = new ArrayList<>();

        if (specialty.equals("General Physician")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Ravi Prasad");
                doctors.add("Dr. Madhumita");
                doctors.add("Dr. Anil Kumar");

            } else if (shift.equals("4 PM - 12 AM")) {
                doctors.add("Dr. Ravi Prasad");
                doctors.add("Dr. Suresh Rao");
                doctors.add("Dr. Madhumita");

            } else {
                doctors.add("Dr. Kiran Kumar");
                doctors.add("Dr. Madhumita");
            }

        } else if (specialty.equals("Pediatrics")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Priya Sharma");
                doctors.add("Dr. Neha Reddy");

            } else if (shift.equals("4 PM - 12 AM")) {
                doctors.add("Dr. Neha Reddy");
                doctors.add("Dr. Anjali Rao");

            } else {
                doctors.add("Dr. Anjali Rao");
            }

        } else if (specialty.equals("Gynecology")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Meena Sharma");
                doctors.add("Dr. Kavya Reddy");

            } else if (shift.equals("4 PM - 12 AM")) {
                doctors.add("Dr. Kavya Reddy");
                doctors.add("Dr. Divya Rao");

            } else {
                doctors.add("Dr. Divya Rao");
            }

        } else if (specialty.equals("Dermatology")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Sneha Rao");
                doctors.add("Dr. Aditi Sharma");

            } else if (shift.equals("4 PM - 12 AM")) {
                doctors.add("Dr. Aditi Sharma");
                doctors.add("Dr. Riya Kumar");

            } else {
                doctors.add("Dr. Riya Kumar");
            }

        } else if (specialty.equals("Orthopedics")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Arjun Rao");
                doctors.add("Dr. Vikram Kumar");

            } else if (shift.equals("4 PM - 12 AM")) {
                doctors.add("Dr. Vikram Kumar");
                doctors.add("Dr. Rohit Sharma");

            } else {
                doctors.add("Dr. Rohit Sharma");
            }

        } else if (specialty.equals("ENT")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Meena Rao");
                doctors.add("Dr. Arjun Sharma");

            } else if (shift.equals("4 PM - 12 AM")) {
                doctors.add("Dr. Arjun Sharma");
                doctors.add("Dr. Kiran Rao");

            } else {
                doctors.add("Dr. Kiran Rao");
            }

        } else if (specialty.equals("Cardiology")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Rajesh Kumar");
            } else {
                doctors.add("Dr. Staff Available");
            }

        } else if (specialty.equals("Gastroenterology")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Sanjay Rao");
            } else {
                doctors.add("Dr. Staff Available");
            }

        } else if (specialty.equals("Neurology")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Naveen Kumar");
            } else {
                doctors.add("Dr. Staff Available");
            }

        } else if (specialty.equals("Urology")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Ajay Sharma");
            } else {
                doctors.add("Dr. Staff Available");
            }

        } else if (specialty.equals("Endocrinology")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Varun Rao");
            } else {
                doctors.add("Dr. Staff Available");
            }

        } else if (specialty.equals("Pulmonologist")) {

            if (shift.equals("8 AM - 4 PM")) {
                doctors.add("Dr. Harish Kumar");
            } else {
                doctors.add("Dr. Staff Available");
            }
        }

        return doctors;
    }

    static void viewAppointments() {

        System.out.println(
            "\n==========================================");
        System.out.println("             ALL APPOINTMENTS");
        System.out.println(
            "==========================================");

        if (appointments.isEmpty()) {
            System.out.println("No appointments available.");
            return;
        }

        appointments.stream()
            .sorted(Comparator.comparing(
                a -> a.getPatient().getName(),
                String.CASE_INSENSITIVE_ORDER))
            .forEach(a -> System.out.println(
                "\nAppointment\n" + a));
    }


    static void searchPatient() {

        System.out.println(
            "\n==========================================");
        System.out.println("              SEARCH PATIENT");
        System.out.println(
            "==========================================");

        String id = readText("Enter Patient ID: ");

        Patient patient = patientsById.get(id);

        if (patient == null) {
            System.out.println("\nPatient not found.");
            return;
        }

        System.out.println("\nPatient Found!");
        System.out.println(patient);

        Predicate<Appointment> matchPatient =
            a -> a.getPatient().getPatientID().equals(id);

        appointments.stream()
            .filter(matchPatient)
            .forEach(a -> System.out.println(
                a.appointmentDetails()));
    }

    static void cancelAppointment() {

        System.out.println(
            "\n==========================================");
        System.out.println("           CANCEL APPOINTMENT");
        System.out.println(
            "==========================================");

        String id = readText("Enter Patient ID: ");

        List<Appointment> matches = appointments.stream()
            .filter(a -> a.getPatient()
                .getPatientID().equals(id))
            .collect(Collectors.toList());

        if (matches.isEmpty()) {
            System.out.println("\nPatient not found.");
            return;
        }

        System.out.println(
            "\nAppointment found for: " +
            matches.get(0).getPatient().getName());

        boolean confirm = readBoolean(
            "Are you sure you want to cancel? (true/false): ");

        if (!confirm) {
            System.out.println("\nCancellation stopped.");
            return;
        }

        appointments.remove(matches.get(0));

        boolean stillHasAppointment = appointments.stream()
            .anyMatch(a -> a.getPatient()
                .getPatientID().equals(id));

        if (!stillHasAppointment) {

            patientsById.remove(id);
            patientIds.remove(id);

            prescriptions.removeIf(
                p -> p.getPatientID().equals(id));
        }

        saveData();

        System.out.println(
            "\nAppointment cancelled successfully.");
    }

    static void prescriptionManager() {

        int choice;

        do {
            System.out.println(
                "\n==========================================");
            System.out.println("          PRESCRIPTION MANAGER");
            System.out.println(
                "==========================================");

            System.out.println("1. Create Prescription");
            System.out.println("2. View All Prescriptions");
            System.out.println("3. Search Prescription");
            System.out.println("4. Delete Prescription");
            System.out.println("5. Back");

            choice = readInt("\nEnter your choice: ");

            switch (choice) {
                case 1:
                    createPrescription();
                    break;

                case 2:
                    viewPrescriptions();
                    break;

                case 3:
                    searchPrescription();
                    break;

                case 4:
                    deletePrescription();
                    break;

                case 5:
                    break;

                default:
                    System.out.println("\nInvalid choice!");
            }

        } while (choice != 5);
    }

    static void createPrescription() {

        System.out.println(
            "\n==========================================");
        System.out.println("           CREATE PRESCRIPTION");
        System.out.println(
            "==========================================");

        String id = readText("Enter Patient ID: ");

        Patient patient = patientsById.get(id);

        if (patient == null) {
            System.out.println("\nPatient not found.");
            System.out.println(
                "Please register/book an appointment first.");
            return;
        }

        System.out.println("\nPatient Found!");
        System.out.println(
            "Patient Name : " + patient.getName());

        appointments.stream()
            .filter(a -> a.getPatient()
                .getPatientID().equals(id))
            .findFirst()
            .ifPresent(a -> {
                System.out.println(
                    "Appointment Doctor : " + a.getDoctorName());
                System.out.println(
                    "Specialty : " + a.getSpecialty());
            });

        String doctor =
            readText("\nEnter prescribing doctor name: ");

        String diagnosis =
            readText("Enter diagnosis/reason: ");

        String medicine =
            readText("Enter medicine: ");

        String dosage =
            readText("Enter dosage: ");

        int days = readInt("Enter number of days: ");

        if (days <= 0) {
            System.out.println(
                "Number of days must be positive.");
            return;
        }

        String instructions =
            readText("Enter instructions: ");

        Prescription prescription = new Prescription(
            id,
            patient.getName(),
            doctor,
            diagnosis,
            medicine,
            dosage,
            days,
            instructions
        );

        prescriptions.add(prescription);

        saveData();

        System.out.println(
            "\n==========================================");
        System.out.println("        PRESCRIPTION CREATED");
        System.out.println(
            "==========================================");

        System.out.println(
            "Patient  : " + patient.getName());
        System.out.println("Doctor   : " + doctor);
        System.out.println("Medicine : " + medicine);
        System.out.println("Dosage   : " + dosage);
        System.out.println("Days     : " + days);

        System.out.println(
            "==========================================");
    }


    static void viewPrescriptions() {

        System.out.println(
            "\n==========================================");
        System.out.println("           ALL PRESCRIPTIONS");
        System.out.println(
            "==========================================");

        if (prescriptions.isEmpty()) {
            System.out.println(
                "No prescriptions available.");
            return;
        }
        prescriptions.forEach(System.out::println);
    }


    static void searchPrescription() {

        System.out.println(
            "\n==========================================");
        System.out.println("          SEARCH PRESCRIPTION");
        System.out.println(
            "==========================================");

        String id = readText("Enter Patient ID: ");

        List<Prescription> results = prescriptions.stream()
            .filter(p -> p.getPatientID().equals(id))
            .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("\nNo prescription found.");
        } else {
            results.forEach(p ->
                System.out.println(
                    "\nPrescription Found!\n" + p));
        }
    }

    static void deletePrescription() {

        System.out.println(
            "\n==========================================");
        System.out.println("           DELETE PRESCRIPTION");
        System.out.println(
            "==========================================");

        String id = readText("Enter Patient ID: ");

        for (int i = 0; i < prescriptions.size(); i++) {

            if (prescriptions.get(i)
                    .getPatientID().equals(id)) {

                prescriptions.remove(i);

                saveData();

                System.out.println(
                    "\nPrescription deleted successfully.");
                return;
            }
        }

        System.out.println("\nPrescription not found.");
    }


    static boolean isValidPatientId(String id) {

        if (id.length() != 12) {
            return false;
        }

        for (int i = 0; i < id.length(); i++) {

            if (!Character.isDigit(id.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    static int readInt(String prompt) {

        while (true) {

            System.out.print(prompt);

            try {
                return Integer.parseInt(
                    sc.nextLine().trim());

            } catch (NumberFormatException e) {

                System.out.println(
                    "Please enter a valid whole number.");
            }
        }
    }

    static boolean readBoolean(String prompt) {

        while (true) {

            System.out.print(prompt);

            String value = sc.nextLine().trim();

            if (value.equalsIgnoreCase("true")) {
                return true;
            }

            if (value.equalsIgnoreCase("false")) {
                return false;
            }

            System.out.println(
                "Please enter true or false.");
        }
    }

    static String readText(String prompt) {

        System.out.print(prompt);

        return sc.nextLine().trim();
    }

    static String encode(String value) {

        return Base64.getEncoder().encodeToString(
            value.getBytes(StandardCharsets.UTF_8));
    }

    static String decode(String value) {

        return new String(
            Base64.getDecoder().decode(value),
            StandardCharsets.UTF_8);
    }

    static void saveData() {

        try {
            List<String> appointmentLines =
                appointments.stream().map(a -> {

                    Patient p = a.getPatient();

                    return String.join("|",
                        encode(p.getName()),
                        String.valueOf(p.getAge()),
                        String.valueOf(p.getWeight()),
                        p.getPatientID(),
                        encode(a.getSpecialty()),
                        encode(a.getShift()),
                        encode(a.getDoctorName()),
                        encode(a.getDate())
                    );

                }).collect(Collectors.toList());

            Files.write(
                APPOINTMENT_FILE,
                appointmentLines,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );

            List<String> prescriptionLines =
                prescriptions.stream().map(p ->
                    String.join("|",
                        p.getPatientID(),
                        encode(p.getPatientName()),
                        encode(p.getDoctor()),
                        encode(p.getDiagnosis()),
                        encode(p.getMedicine()),
                        encode(p.getDosage()),
                        String.valueOf(p.getDays()),
                        encode(p.getInstructions())
                    )
                ).collect(Collectors.toList());

            Files.write(
                PRESCRIPTION_FILE,
                prescriptionLines,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {

            System.out.println(
                "Could not save clinic records: " +
                e.getMessage());
        }
    }


    static void loadData() {

        try {
            if (Files.exists(APPOINTMENT_FILE)) {

                // MODULE 5: try-with-resources
                try (var reader =
                        Files.newBufferedReader(
                            APPOINTMENT_FILE)) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        try {
                            String[] data =
                                line.split("\\|", -1);

                            Patient patient = new Patient(
                                decode(data[0]),
                                Integer.parseInt(data[1]),
                                Integer.parseInt(data[2]),
                                data[3]
                            );

                            Appointment appointment =
                                new Appointment(
                                    patient,
                                    decode(data[4]),
                                    decode(data[5]),
                                    decode(data[6]),
                                    decode(data[7])
                                );

                            appointments.add(appointment);

                            patientsById.put(
                                patient.getPatientID(), patient);

                            patientIds.add(
                                patient.getPatientID());

                        } catch (RuntimeException e) {

                            System.out.println(
                                "Skipped an invalid appointment record.");
                        }
                    }
                }
            }

            if (Files.exists(PRESCRIPTION_FILE)) {

                try (var reader =
                        Files.newBufferedReader(
                            PRESCRIPTION_FILE)) {

                    String line;

                    while ((line = reader.readLine()) != null) {

                        try {
                            String[] data =
                                line.split("\\|", -1);

                            Prescription prescription =
                                new Prescription(
                                    data[0],
                                    decode(data[1]),
                                    decode(data[2]),
                                    decode(data[3]),
                                    decode(data[4]),
                                    decode(data[5]),
                                    Integer.parseInt(data[6]),
                                    decode(data[7])
                                );

                            prescriptions.add(prescription);

                        } catch (RuntimeException e) {

                            System.out.println(
                                "Skipped an invalid prescription record.");
                        }
                    }
                }
            }

        } catch (IOException e) {

            System.out.println(
                "Could not load saved records: " +
                e.getMessage());
        }
    }
}


class Patient {

    private String name;
    private int age;
    private int weight;
    private final String patientID;

    public Patient(
            String name,
            int age,
            int weight,
            String patientID) {

        this.name = name;
        this.age = age;
        this.weight = weight;
        this.patientID = patientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getPatientID() {
        return patientID;
    }

    @Override
    public String toString() {

        return "Name   : " + name +
               "\nAge    : " + age +
               "\nWeight : " + weight + " kg" +
               "\nID     : " + patientID;
    }
}

class Appointment {

    private final Patient patient;
    private final String specialty;
    private final String shift;
    private final String doctorName;
    private final String date;

    public Appointment(
            Patient patient,
            String specialty,
            String shift,
            String doctorName,
            String date) {

        this.patient = patient;
        this.specialty = specialty;
        this.shift = shift;
        this.doctorName = doctorName;
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getShift() {
        return shift;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDate() {
        return date;
    }

    public String appointmentDetails() {

        return "Specialty  : " + specialty +
               "\nShift      : " + shift +
               "\nDoctor     : " + doctorName +
               "\nDate       : " + date;
    }

    @Override
    public String toString() {

        return "Patient    : " + patient.getName() +
               "\nAge        : " + patient.getAge() +
               "\nWeight     : " + patient.getWeight() + " kg" +
               "\nPatient ID : " + patient.getPatientID() +
               "\n" + appointmentDetails() +
               "\n------------------------------------------";
    }
}

class Prescription {

    private final String patientID;
    private final String patientName;
    private final String doctor;
    private final String diagnosis;
    private final String medicine;
    private final String dosage;
    private final String instructions;
    private final int days;

    public Prescription(
            String patientID,
            String patientName,
            String doctor,
            String diagnosis,
            String medicine,
            String dosage,
            int days,
            String instructions) {

        this.patientID = patientID;
        this.patientName = patientName;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
        this.medicine = medicine;
        this.dosage = dosage;
        this.days = days;
        this.instructions = instructions;
    }

    public String getPatientID() {
        return patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getMedicine() {
        return medicine;
    }

    public String getDosage() {
        return dosage;
    }

    public int getDays() {
        return days;
    }

    public String getInstructions() {
        return instructions;
    }

    @Override
    public String toString() {

        return "Patient ID  : " + patientID +
               "\nPatient     : " + patientName +
               "\nDoctor      : " + doctor +
               "\nDiagnosis   : " + diagnosis +
               "\nMedicine    : " + medicine +
               "\nDosage      : " + dosage +
               "\nDays        : " + days +
               "\nInstructions: " + instructions +
               "\n------------------------------------------";
    }
}