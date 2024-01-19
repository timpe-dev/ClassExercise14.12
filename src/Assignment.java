import java.util.Vector;

public class Assignment {
    String className;
    String name;
    String date;
    String description;
    Vector<AssignmentEntry> assignmentEntries;
    Assignment(String className, String Name, String Date, String Description) {
        name = Name;
        date = Date;
        description = Description;
        assignmentEntries = new Vector<>();
    }
}
