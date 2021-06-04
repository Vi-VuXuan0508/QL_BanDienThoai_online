package com.example.project1.Model;

public class ProductInformation {
    private String idproduct,idproductInformation,screen,camera,
            cameraselfie,ram,rom,cpu,gpu,pin,sim,system,origin,yearofmanufacture;



    @Override
    public String toString() {
        return "Màn hình: "+screen+", Camera: "+camera+"and "+cameraselfie+",RAM: "+ram+", CPU: "+cpu;
    }


    public ProductInformation(String idproduct, String screen, String camera, String cameraselfie, String ram, String rom, String cpu, String gpu, String pin, String sim, String system, String origin, String yearofmanufacture) {
        this.idproduct = idproduct;
        this.screen = screen;
        this.camera = camera;
        this.cameraselfie = cameraselfie;
        this.ram = ram;
        this.rom = rom;
        this.cpu = cpu;
        this.gpu = gpu;
        this.pin = pin;
        this.sim = sim;
        this.system = system;
        this.origin = origin;
        this.yearofmanufacture = yearofmanufacture;
    }

    public ProductInformation() {
    }

    public String getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
    }

    public String getIdproductInformation() {
        return idproductInformation;
    }

    public void setIdproductInformation(String idproductInformation) {
        this.idproductInformation = idproductInformation;
    }

    public ProductInformation(String idproduct, String idproductInformation, String screen, String camera, String cameraselfie, String ram, String rom, String cpu, String gpu, String pin, String sim, String system, String origin, String yearofmanufacture) {
        this.idproduct = idproduct;
        this.idproductInformation = idproductInformation;
        this.screen = screen;
        this.camera = camera;
        this.cameraselfie = cameraselfie;
        this.ram = ram;
        this.rom = rom;
        this.cpu = cpu;
        this.gpu = gpu;
        this.pin = pin;
        this.sim = sim;
        this.system = system;
        this.origin = origin;
        this.yearofmanufacture = yearofmanufacture;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getCameraselfie() {
        return cameraselfie;
    }

    public void setCameraselfie(String cameraselfie) {
        this.cameraselfie = cameraselfie;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getGpu() {
        return gpu;
    }

    public void setGpu(String gpu) {
        this.gpu = gpu;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getSim() {
        return sim;
    }

    public void setSim(String sim) {
        this.sim = sim;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getYearofmanufacture() {
        return yearofmanufacture;
    }

    public void setYearofmanufacture(String yearofmanufacture) {
        this.yearofmanufacture = yearofmanufacture;
    }
}
