package practice.pojo;

/**
 * Sample POJO for oauth.json
 * Nested Json requires a separate POJO Class to store key value pairs e.g. Courses key has a nested JSON as value, so we created Courses Class
 * GetCourse.java is a parent for Courses.java class which is parent for WebAutomation, Api and Mobile classes
 * These child lasses have been constructed to parse complex nested json using POJO classes
 */
public class GetCourse {
    private String services;
    private String expertise;
    private Courses courses;
    private String instructor;
    private String url;

    private String linkedIn;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }


}
