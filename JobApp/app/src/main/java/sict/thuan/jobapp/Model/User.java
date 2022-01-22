package sict.thuan.jobapp.Model;

public class User {

    private String id;
    private String username;
    private String fullname;
    private String imageurl;
    private String bio;
    private String state;
    private String birthday;
    private String sex;

    public String getWork() {
        return work;
    }

    private String work;
    private String skill;
    private String experience;
    private String email;

    public String getPhone() {
        return phone;
    }

    private String phone;

    public String getEmail() {
        return email;
    }


    public User(String id, String username, String fullname, String imageurl, String bio,
                String state, String birthday, String sex, String work, String skill, String experience,
                String salary, String phone, String email) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.imageurl = imageurl;
        this.bio = bio;
        this.state = state;
        this.birthday = birthday;
        this.sex = sex;
        this.work = work;
        this.skill = skill;
        this.experience = experience;
        this.salary = salary;
        this.phone = phone;
        this.email = email;
    }

    private String salary;



    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getBio() {
        return bio;
    }

    public String getState() {
        return state;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getSex() {
        return sex;
    }

    public String getSkill() {
        return skill;
    }

    public String getExperience() {
        return experience;
    }

    public String getSalary() {
        return salary;
    }
}


