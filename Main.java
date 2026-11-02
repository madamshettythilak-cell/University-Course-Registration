package UniversityCourseRegistration;




import java.io.*;
import java.util.Locale;
import java.time.LocalDateTime;

// ================= CUSTOM EXCEPTIONS =================

class InvalidCreditsException extends Exception {
    private int rejectedValue;

    public InvalidCreditsException(String message) {
        super(message);
    }

    public InvalidCreditsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCreditsException(String message, int value) {
        super(message);
        rejectedValue = value;
    }

    public int getRejectedValue() {
        return rejectedValue;
    }
}

class InvalidFeeException extends Exception {
    public InvalidFeeException(String message) {
        super(message);
    }

    public InvalidFeeException(String message, Throwable cause) {
        super(message, cause);
    }
}

class InvalidLabHoursException extends Exception {
    public InvalidLabHoursException(String message) {
        super(message);
    }

    public InvalidLabHoursException(String message, Throwable cause) {
        super(message, cause);
    }
}

class InvalidScholarshipPercentException extends RuntimeException {
    public InvalidScholarshipPercentException(String message) {
        super(message);
    }

    public InvalidScholarshipPercentException(String message, Throwable cause) {
        super(message, cause);
    }
}

class InvalidRecordException extends Exception {
    public InvalidRecordException(String message) {
        super(message);
    }

    public InvalidRecordException(String message, Throwable cause) {
        super(message, cause);
    }
}

class EmptyInputFileException extends Exception {
    public EmptyInputFileException(String message) {
        super(message);
    }

    public EmptyInputFileException(String message, Throwable cause) {
        super(message, cause);
    }
}

// ================= PARENT CLASS =================

class Course {

    private int courseId;
    private String courseName;
    private int credits;
    private double feePerSemester;

    public static String universityName = "University";
    public static double labFee = 2000;
    public static double creditCharge = 500;
    public static int courseCount = 0;

    // No-argument constructor
    public Course() {
        courseCount++;
    }

    // Constructor with ID and name
    public Course(int courseId, String courseName)
            throws InvalidCreditsException, InvalidFeeException {

        this();
        this.courseId = courseId;
        this.courseName = courseName;
        setCredits(1);
        setFeePerSemester(0);
    }

    // Parameterized constructor
    public Course(int courseId, String courseName,
                  int credits, double feePerSemester)
            throws InvalidCreditsException, InvalidFeeException {

        this();
        this.courseId = courseId;
        this.courseName = courseName;
        setCredits(credits);
        setFeePerSemester(feePerSemester);
    }

    // Getters
    public int getCourseId() {
        return courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public double getFeePerSemester() {
        return feePerSemester;
    }

    // Validated setters
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCredits(int credits)
            throws InvalidCreditsException {

        if (credits < 1 || credits > 6) {
            throw new InvalidCreditsException(
                "Credits must be between 1 and 6: " + credits,
                credits
            );
        }

        this.credits = credits;
    }

    public void setFeePerSemester(double fee)
            throws InvalidFeeException {

        if (fee < 0) {
            throw new InvalidFeeException(
                "Semester fee cannot be negative: " + fee
            );
        }

        this.feePerSemester = fee;
    }

    // Semester fee
    public double calculateSemesterFee() {
        return feePerSemester + credits * creditCharge;
    }

    // Overloaded method: scholarship calculation
    public double calculateSemesterFee(double scholarshipPercent) {

        if (scholarshipPercent < 0 || scholarshipPercent > 100) {
            throw new InvalidScholarshipPercentException(
                "Scholarship must be between 0 and 100: "
                + scholarshipPercent
            );
        }

        return calculateSemesterFee()
                * (1 - scholarshipPercent / 100);
    }

    // Compare credits
    public String compareCredits(Course other) {

        if (this.credits >= other.credits) {
            return this.courseName;
        }

        return other.courseName;
    }

    // Display course details
    public void display(BufferedWriter bw) throws IOException {

        bw.write("Course ID: " + courseId);
        bw.newLine();

        bw.write("Course Name: " + courseName);
        bw.newLine();

        bw.write("Credits: " + credits);
        bw.newLine();

        bw.write("Base Semester Fee: "
                + String.format(Locale.US, "%.2f", feePerSemester));
        bw.newLine();
    }

    // Static method: find maximum credits
    public static String findMaxCredits(Course[] courses) {

        Course max = null;

        for (Course c : courses) {

            if (c != null) {

                if (max == null || c.getCredits() > max.getCredits()) {
                    max = c;
                }
            }
        }

        return max == null ? "No courses available" : max.getCourseName();
    }
}

// ================= CHILD CLASS =================

class LabCourse extends Course {

    private int labHoursPerSemester;
    private double equipmentCost;

    public static int weeksPerSemester = 16;

    // No-argument constructor
    public LabCourse() {
        super();
    }

    // Parameterized constructor
    public LabCourse(int courseId, String courseName,
                     int credits, double fee,
                     int labHours, double equipmentCost)
            throws InvalidCreditsException,
                   InvalidFeeException,
                   InvalidLabHoursException {

        super(courseId, courseName, credits, fee);

        setLabHoursPerSemester(labHours);
        setEquipmentCost(equipmentCost);
    }

    // Getters
    public int getLabHoursPerSemester() {
        return labHoursPerSemester;
    }

    public double getEquipmentCost() {
        return equipmentCost;
    }

    // Validated setters
    public void setLabHoursPerSemester(int hours)
            throws InvalidLabHoursException {

        if (hours < 0) {
            throw new InvalidLabHoursException(
                "Lab hours cannot be negative: " + hours
            );
        }

        this.labHoursPerSemester = hours;
    }

    public void setEquipmentCost(double cost)
            throws InvalidFeeException {

        if (cost < 0) {
            throw new InvalidFeeException(
                "Equipment cost cannot be negative: " + cost
            );
        }

        this.equipmentCost = cost;
    }

    // Overridden semester fee
    @Override
    public double calculateSemesterFee() {

        return super.calculateSemesterFee()
                + labFee + equipmentCost;
    }

    // Calculate weekly lab hours
    public double calculateLabHoursPerWeek() {

        return (double) labHoursPerSemester / weeksPerSemester;
    }

    // Overridden display method
    @Override
    public void display(BufferedWriter bw) throws IOException {

        super.display(bw);

        bw.write("Lab Hours Per Semester: " + labHoursPerSemester);
        bw.newLine();

        bw.write("Equipment Cost: "
                + String.format(Locale.US, "%.2f", equipmentCost));
        bw.newLine();
    }
}

// ================= MAIN CLASS =================

public class Main
{

    static int recordsRead = 0;
    static int accepted = 0;
    static int rejected = 0;

    // Write rejected record to errors.txt
    static void logError(BufferedWriter errorWriter,
                         int lineNumber,
                         String record,
                         Exception e) throws IOException {

        errorWriter.write(
            "Line " + lineNumber
            + " | Record: " + record
            + " | " + e.getClass().getSimpleName()
            + " | " + e.getMessage()
        );

        errorWriter.newLine();
        rejected++;
    }

    // Convert parsing errors into InvalidRecordException
    static InvalidRecordException invalidRecord(
            String message, Exception cause) {

        return new InvalidRecordException(message, cause);
    }

    public static void main(String[] args) {

        File inputFile = new File("input/courses.txt");
        File outputFolder = new File("output");

        Course[] courses = null;
        double[] scholarships = null;

        int n = 0;

        try {

            // Create output folder
            if (!outputFolder.exists()) {
                outputFolder.mkdirs();
            }

            // Check input file
            if (!inputFile.exists() ||
                !inputFile.isFile() ||
                !inputFile.canRead()) {

                throw new FileNotFoundException(
                    "Input file is missing or cannot be read."
                );
            }

            if (inputFile.length() == 0) {
                throw new EmptyInputFileException(
                    "Input file is empty."
                );
            }

            try (
                BufferedReader br = new BufferedReader(
                    new FileReader(inputFile)
                );

                BufferedWriter errorWriter = new BufferedWriter(
                    new FileWriter("output/errors.txt", true)
                )
            ) {

                // Error log header
                errorWriter.write(
                    "===== Run: " + LocalDateTime.now() + " ====="
                );
                errorWriter.newLine();

                // Read number of records
                String firstLine = br.readLine();

                if (firstLine == null || firstLine.trim().isEmpty()) {
                    throw new EmptyInputFileException(
                        "Number of records is missing."
                    );
                }

                try {
                    n = Integer.parseInt(firstLine.trim());
                } catch (NumberFormatException e) {
                    throw invalidRecord(
                        "Invalid number of records.", e
                    );
                }

                if (n <= 0) {
                    throw new EmptyInputFileException(
                        "Number of records must be greater than zero."
                    );
                }

                courses = new Course[n];
                scholarships = new double[n];

                int processed = 0;

                // Read every record
                try {

                    for (int i = 0; i < n; i++) {

                        String line = br.readLine();

                        if (line == null) {
                            break;
                        }

                        processed++;
                        int lineNumber = i + 2;

                        try {

                            String[] data = line.split(",", -1);
                            String type = data[0].trim();

                            Course course;
                            double scholarship;

                            if (type.equals("T")) {

                                if (data.length != 6) {
                                    throw new InvalidRecordException(
                                        "Theory record must contain 6 fields."
                                    );
                                }

                                int id = Integer.parseInt(data[1].trim());
                                String name = data[2].trim();
                                int credits = Integer.parseInt(data[3].trim());
                                double fee = Double.parseDouble(data[4].trim());

                                scholarship =
                                    Double.parseDouble(data[5].trim());

                                course = new Course(
                                    id, name, credits, fee
                                );

                            } else if (type.equals("L")) {

                                if (data.length != 8) {
                                    throw new InvalidRecordException(
                                        "Lab record must contain 8 fields."
                                    );
                                }

                                int id = Integer.parseInt(data[1].trim());
                                String name = data[2].trim();
                                int credits = Integer.parseInt(data[3].trim());
                                double fee = Double.parseDouble(data[4].trim());
                                int hours = Integer.parseInt(data[5].trim());
                                double equipment =
                                    Double.parseDouble(data[6].trim());

                                scholarship =
                                    Double.parseDouble(data[7].trim());

                                course = new LabCourse(
                                    id, name, credits, fee,
                                    hours, equipment
                                );

                            } else {

                                throw new InvalidRecordException(
                                    "Unknown course type: " + type
                                );
                            }

                            // Validate scholarship and calculate
                            // the fee using the correct overridden method.
                            double finalFee =
                                course.calculateSemesterFee(scholarship);

                            // Store only accepted records
                            courses[accepted] = course;
                            scholarships[accepted] = scholarship;
                            accepted++;

                        } catch (InvalidCreditsException e) {
                            logError(errorWriter, lineNumber, line, e);

                        } catch (InvalidFeeException e) {
                            logError(errorWriter, lineNumber, line, e);

                        } catch (InvalidLabHoursException e) {
                            logError(errorWriter, lineNumber, line, e);

                        } catch (InvalidScholarshipPercentException e) {
                            logError(errorWriter, lineNumber, line, e);

                        } catch (InvalidRecordException e) {
                            logError(errorWriter, lineNumber, line, e);

                        } catch (NumberFormatException |
                                 ArrayIndexOutOfBoundsException e) {

                            InvalidRecordException wrapped =
                                invalidRecord(
                                    "Invalid record format or numeric value.",
                                    e
                                );

                            logError(errorWriter, lineNumber, line, wrapped);
                        }
                    }

                } finally {
                    recordsRead = processed;
                }

                errorWriter.flush();
            }

            // Write fee statement
            try (
                BufferedWriter bw = new BufferedWriter(
                    new FileWriter("output/fee_statement.txt")
                )
            ) {

                bw.write("UNIVERSITY COURSE REGISTRATION SYSTEM");
                bw.newLine();

                bw.write("University: " + Course.universityName);
                bw.newLine();
                bw.newLine();

                for (int i = 0; i < accepted; i++) {

                    Course c = courses[i];

                    bw.write("------------------------------");
                    bw.newLine();

                    c.display(bw);

                    double beforeScholarship =
                        c.calculateSemesterFee();

                    double afterScholarship =
                        c.calculateSemesterFee(scholarships[i]);

                    bw.write("Semester Fee Before Scholarship: "
                        + String.format(
                            Locale.US, "%.2f", beforeScholarship
                        ));
                    bw.newLine();

                    bw.write("Scholarship: "
                        + String.format(
                            Locale.US, "%.2f", scholarships[i]
                        ) + "%");
                    bw.newLine();

                    bw.write("Semester Fee After Scholarship: "
                        + String.format(
                            Locale.US, "%.2f", afterScholarship
                        ));
                    bw.newLine();

                    if (c instanceof LabCourse) {

                        LabCourse lab = (LabCourse) c;

                        bw.write("Weekly Lab Hours: "
                            + String.format(
                                Locale.US, "%.2f",
                                lab.calculateLabHoursPerWeek()
                            ));
                        bw.newLine();
                    }

                    bw.newLine();
                }

                // Compare first two accepted courses
                bw.write("Course with more credits (first two): ");

                if (accepted >= 2) {
                    bw.write(
                        courses[0].compareCredits(courses[1])
                    );
                } else {
                    bw.write("Not enough accepted courses");
                }

                bw.newLine();

                // Maximum credits
                bw.write("Course with Maximum Credits: "
                    + Course.findMaxCredits(courses));
                bw.newLine();

                // Course count
                bw.write("Course Count: " + Course.courseCount);
                bw.newLine();

                // Summary
                bw.write(
                    "Records read: " + recordsRead
                    + ", accepted: " + accepted
                    + ", rejected: " + rejected
                );

                bw.newLine();
                bw.flush();
            }

            System.out.println(
                "Processing complete. See output folder."
            );

        } catch (EmptyInputFileException e) {

            System.out.println("Processing failed: " + e.getMessage());

        } catch (InvalidRecordException e) {

            System.out.println("Processing failed: " + e.getMessage());

        } catch (FileNotFoundException e) {

            System.out.println("Processing failed: " + e.getMessage());

        } catch (IOException e) {

            System.out.println("Processing failed: " + e.getMessage());
        }
    }
}


