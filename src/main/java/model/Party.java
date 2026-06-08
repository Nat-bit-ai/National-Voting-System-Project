package model;

public class Party extends BaseEntity {
    private String name;
    private String leaderName;
    private String symbolPath;

    public Party() {
    }

    public Party(String name, String leaderName, String symbolPath) {
        this.name = name;
        this.leaderName = leaderName;
        this.symbolPath = symbolPath;
    }

    @Override
    public String toString() {
        return name == null ? "" : name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getSymbolPath() {
        return symbolPath;
    }

    public void setSymbolPath(String symbolPath) {
        this.symbolPath = symbolPath;
    }
}
