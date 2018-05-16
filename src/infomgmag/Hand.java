package infomgmag;
public class Hand {
    private int infantry, cavalry, artillery, wildcards;

    public int getInfantry() {
        return infantry;
    }

    public int getArtillery() {
        return artillery;
    }

    public int getCavalry() {
        return cavalry;
    }

    public int getWildcards(){return wildcards;}

    public void setInfantry(Integer infantry){
        this.infantry = infantry;
    }

    public void setArtillery(int artillery) {
        this.artillery = artillery;
    }

    public void setCavalry(int cavalry) {
        this.cavalry = cavalry;
    }

    public void setWildCards(int wildcard){ this.wildcards = wildcards;}

    public Hand(){
        infantry = 0;
        cavalry = 0;
        artillery = 0;
        wildcards = 0;
    }
}
