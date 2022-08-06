package com.mongo.demo.dto;

import com.mongodb.BasicDBObject;

public class PersonInfo extends BasicDBObject {
    static final long serialVersionUID = 2105061907470199595L;
    private String id;
    private String name;
    private String age;

    public PersonInfo(String id, String name, String age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    @Override
    public String toString() {
        return "PersonInfo{" +
                ", identifier='" + id + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
