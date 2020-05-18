package sample.Model;

public class Football {
    private int id;
    private String TeamName;
    private String Manager;
    private double Players;
    private String platform;
    private String Division;
    private int userId;


    public Football(int id, String TeamName, String Manager, double Players, String platform, String Division, int userId) {
        this.id = id;
        this.TeamName = TeamName;
        this.Manager = Manager;
        this.Players = Players;
        this.platform = platform;
        this.Division = Division;
        this.userId = userId;
    }

    public Football(String TeamName, String Manager, double Players, String platform, String Division, int userId) {
        this.TeamName = TeamName;
        this.Manager = Manager;
        this.Players = Players;
        this.platform = platform;
        this.Division = Division;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        this.TeamName = teamName;
    }

    public String getManager() {
        return Manager;
    }

    public void setManager(String manager) {
        this.Manager = manager;
    }

    public double getPlayers() {
        return Players;
    }

    public void setPlayers(double players) {
        this.Players = players;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDivision() {
        return Division;
    }

    public void setDivision(String division) {
        this.Division = division;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", Team Name='" + TeamName + '\'' +
                ", Manager='" + Manager + '\'' +
                ", Numbers of Players=" + Players +
                ", platform='" + platform + '\'' +
                ", Division='" + Division + '\'' +
                ", userId=" + userId +
                '}';
    }
}
