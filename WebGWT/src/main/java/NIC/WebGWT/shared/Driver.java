package NIC.WebGWT.shared;


import java.io.Serializable;

public class Driver implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String team;
    private int number;
    private int titles;

    public Driver() {
    }

    public Driver(String name, String team, int number, int titles) {
        this.name = name;
        this.team = team;
        this.number = number;
        this.titles = titles;
    }

    public String getName() { return name; }
    public String getTeam() { return team; }
    public int getNumber() { return number; }
    public int getTitles() { return titles; }

    public void setName(String name) { this.name = name; }
    public void setTeam(String team) { this.team = team; }
    public void setNumber(int number) { this.number = number; }
    public void setTitles(int titles) { this.titles = titles; }
}