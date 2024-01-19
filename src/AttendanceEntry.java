public class AttendanceEntry {
    String name;
    Boolean isPresent;
    String time;
    String notes;
    AttendanceEntry(String Name, Boolean IsPresent, String Time, String Notes) {
        name = Name;
        isPresent = IsPresent;
        time = Time;
        notes = Notes;
    }
}
