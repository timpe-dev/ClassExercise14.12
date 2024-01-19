import java.util.Vector;

public class AttendaceData {
    String className;
    String date;
    Vector<AttendanceEntry> attendanceEntries;
    AttendaceData(String ClassName, String Date) {
        className = ClassName;
        date = Date;
        attendanceEntries = new Vector<>();
    }
}
