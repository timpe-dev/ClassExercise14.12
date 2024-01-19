import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Set;
import java.util.Vector;

public class FileAccess {

    void StoreLogin(String userName, String encryptedPassword, String salt) throws Exception {
        UserInfo user = new UserInfo(encryptedPassword, userName, salt);
        JSONParser parser = new JSONParser();
        JSONObject item = new JSONObject();
        item.put("passwordHash", user.passwordHash);
        item.put("salt", user.salt);
        JSONObject currentData = new JSONObject();

        try {
            FileReader reader = new FileReader("users.json");
            currentData = (JSONObject) parser.parse(reader);

        } catch (Exception e) {
            ;
        }
        currentData.put(user.username, item);

        try {
            FileWriter file = new FileWriter("users.json");
            file.write(currentData.toJSONString());
            file.close();
        } catch (Exception e) {
            ;
        }
    }

    UserInfo FetchUser(String userName) throws IOException, ParseException {
        try {
            FileReader reader = new FileReader("users.json");
            JSONParser parser = new JSONParser();

            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONObject current = (JSONObject) jsonObject.get(userName);
            if (current == null) {
                return (null);
            }
            return(new UserInfo(current.get("passwordHash").toString(), userName, current.get("salt").toString()));
        } catch (Exception e) {
            return (null); //File doesnt exist, return null.
        }
    }
/*
    SchoolClass FetchClass(String ClassName) throws IOException, ParseException {
        FileReader reader = new FileReader("classes.json");
        JSONParser parser = new JSONParser();

        JSONObject jsonObject = (JSONObject) parser.parse(reader);

        JSONObject current = (JSONObject) jsonObject.get(ClassName);
        if (current == null) {
            return (null);
        }
        return(new UserInfo(current.get("passwordHash").toString(), ClassName, current.get("salt").toString()));
    } */
    AttendaceData FetchAttendance(String ClassName, String Date) {
        try {
            FileReader reader = new FileReader("attendance.json");
            JSONParser parser = new JSONParser();

            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONObject current = (JSONObject) jsonObject.get(Date);
            if (current == null) {
                return null;
            }
            JSONObject attendanceJson = (JSONObject) current.get(ClassName);
            if (attendanceJson == null) {
                return null;
            }
            int i = 0;
            AttendaceData attendance = new AttendaceData(ClassName, Date);
            JSONObject studentEntries = (JSONObject) attendanceJson.get("students");
            while (true) {
                try {
                    JSONObject attendanceEntryJson = (JSONObject) studentEntries.get(Integer.toString(i));
                    //System.out.println(studentJson);
                    //System.out.println(studentJson.get("firstname").toString());
                    //System.out.println(student.);
                    AttendanceEntry attendanceEntry = new AttendanceEntry(attendanceEntryJson.get("name").toString(), (boolean) attendanceEntryJson.get("isPresent"), attendanceEntryJson.get("time").toString(), attendanceEntryJson.get("notes").toString());
                    attendance.attendanceEntries.add(attendanceEntry);
                } catch (Exception e){
                    //System.out.println(e);
                    break;
                }
                i++;
            }
            return attendance;
        } catch (Exception e) {
            return null; //File doesnt exist, return null.
        }
    }
    void StoreAttendane(AttendaceData attendaceData) {
        System.out.println(attendaceData.attendanceEntries.get(0).name);
        JSONParser parser = new JSONParser();
        JSONObject attendanceJson = new JSONObject();
        attendanceJson.put("className", attendaceData.className);
        attendanceJson.put("date", attendaceData.date);
        JSONObject attendanceList = new JSONObject();
        for (int i=0; i<attendaceData.attendanceEntries.size(); i++) {
            //System.out.println(attendaceData.attendanceEntries.get(i).name);
            JSONObject attendanceEntry = new JSONObject();
            attendanceEntry.put("name", attendaceData.attendanceEntries.get(i).name);
            attendanceEntry.put("isPresent", attendaceData.attendanceEntries.get(i).isPresent);
            attendanceEntry.put("time", attendaceData.attendanceEntries.get(i).time);
            attendanceEntry.put("notes", attendaceData.attendanceEntries.get(i).notes);
            attendanceList.put(i, attendanceEntry);
        }
        attendanceJson.put("students", attendanceList);

        JSONObject currentData = new JSONObject();
        try {
            FileReader reader = new FileReader("attendance.json");
            currentData = (JSONObject) parser.parse(reader);

        } catch (Exception ignored) {
            ;
        }
        JSONObject attendancesForDate;
        attendancesForDate = (JSONObject) currentData.get(attendaceData.date); //fetch object with current date, remove object with current date, add object with currrent date where the new entry is added
        if (attendancesForDate == null) {
            attendancesForDate = new JSONObject();
        }
        attendancesForDate.put(attendaceData.className, attendanceJson);
        currentData.remove(attendaceData.date);
        currentData.put(attendaceData.date, attendancesForDate);
        try {
            FileWriter file = new FileWriter("attendance.json");
            file.write(currentData.toJSONString());
            file.close();
        } catch (Exception ignored) {
            ;
        }
    }

    void StoreClass(SchoolClass schoolClass) {

        JSONParser parser = new JSONParser();
        JSONObject classJson = new JSONObject();
        classJson.put("classname", schoolClass.className);
        JSONObject studentList = new JSONObject();
        for (int i=0; i<schoolClass.students.size(); i++) {
            //System.out.println(attendaceData.attendanceEntries.get(i).name);
            JSONObject studentEntry = new JSONObject();
            studentEntry.put("firstname", schoolClass.students.get(i).firstName);
            studentEntry.put("lastname", schoolClass.students.get(i).surName);
            studentList.put(Integer.toString(i), studentEntry);
        }
        classJson.put("students", studentList);

        JSONObject currentData = new JSONObject();
        try {
            FileReader reader = new FileReader("classes.json");
            currentData = (JSONObject) parser.parse(reader);

        } catch (Exception ignored) {
            ;
        }
        //currentData.put(schoolClass.className, classJson);
        currentData.remove(schoolClass.className);
        currentData.put(schoolClass.className, classJson);
        //System.out.println(currentData);
        //System.out.println(currentData);
        try {
            FileWriter file = new FileWriter("classes.json");
            file.write(currentData.toJSONString());
            file.close();
        } catch (Exception ignored) {
            ;
        }
    }
    Vector<String> FetchClassNames() {
        Vector<String> classNames = new Vector<>();
        try {
            FileReader reader = new FileReader("classes.json");
            JSONParser parser = new JSONParser();

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            Set<String> keys = jsonObject.keySet();
            for (String key : keys) {
                classNames.add(key);
            }
        } catch (Exception e) {
            ;
        }
        return classNames;
    }
    SchoolClass FetchClass(String ClassName) {
        try {
            FileReader reader = new FileReader("classes.json");
            JSONParser parser = new JSONParser();

            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONObject current = (JSONObject) jsonObject.get(ClassName);
            if (current == null) {
                return (null);
            }
            SchoolClass schoolClass = new SchoolClass(ClassName);
            JSONObject studentsJson = (JSONObject) current.get("students");
            int i = 0;
            while (true) {
                try {
                    JSONObject studentJson = (JSONObject) studentsJson.get(Integer.toString(i));
                    //System.out.println(studentJson);
                    //System.out.println(studentJson.get("firstname").toString());
                    Student student = new Student(studentJson.get("firstname").toString(), studentJson.get("lastname").toString());
                    //System.out.println(student.);
                    schoolClass.students.add(student);
                } catch (Exception e){
                    //System.out.println(e);
                    break;
                }
                i++;
            }
            return schoolClass;
        } catch (Exception e) {
            return (null); //File doesnt exist, return null.
        }
    }

}
