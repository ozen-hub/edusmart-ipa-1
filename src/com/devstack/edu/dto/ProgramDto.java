package com.devstack.edu.dto;

import java.util.List;

public class ProgramDto {
    private long programId;
    private int hours;
    private String programName;
    private double amount;
    private String userEmail;
    private long trainerId;
    private List<String> contents;

    public ProgramDto() {
    }

    public ProgramDto(long programId, int hours, String programName, double amount, String userEmail, long trainerId, List<String> contents) {
        this.programId = programId;
        this.hours = hours;
        this.programName = programName;
        this.amount = amount;
        this.userEmail = userEmail;
        this.trainerId = trainerId;
        this.contents = contents;
    }

    public long getProgramId() {
        return programId;
    }

    public void setProgramId(long programId) {
        this.programId = programId;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(long trainerId) {
        this.trainerId = trainerId;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    @Override
    public String toString() {
        return "Program{" +
                "programId=" + programId +
                ", hours=" + hours +
                ", programName='" + programName + '\'' +
                ", amount=" + amount +
                ", userEmail='" + userEmail + '\'' +
                ", trainerId=" + trainerId +
                ", contents=" + contents +
                '}';
    }
}
