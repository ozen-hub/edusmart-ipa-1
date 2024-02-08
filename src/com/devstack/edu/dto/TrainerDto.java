package com.devstack.edu.dto;

public class TrainerDto {
    private Long trainerId;
    private String trainerName;
    private String trainerEmail;
    private String nic;
    private String address;
    private boolean TrainerStatus;

    public TrainerDto() {
    }

    public TrainerDto(Long trainerId, String trainerName, String trainerEmail, String nic, String address, boolean trainerStatus) {
        this.trainerId = trainerId;
        this.trainerName = trainerName;
        this.trainerEmail = trainerEmail;
        this.nic = nic;
        this.address = address;
        TrainerStatus = trainerStatus;
    }

    public Long getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(Long trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public String getTrainerEmail() {
        return trainerEmail;
    }

    public void setTrainerEmail(String trainerEmail) {
        this.trainerEmail = trainerEmail;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isTrainerStatus() {
        return TrainerStatus;
    }

    public void setTrainerStatus(boolean trainerStatus) {
        TrainerStatus = trainerStatus;
    }

    @Override
    public String toString() {
        return "Trainer{" +
                "trainerId=" + trainerId +
                ", trainerName='" + trainerName + '\'' +
                ", trainerEmail='" + trainerEmail + '\'' +
                ", nic='" + nic + '\'' +
                ", address='" + address + '\'' +
                ", TrainerStatus=" + TrainerStatus +
                '}';
    }
}
