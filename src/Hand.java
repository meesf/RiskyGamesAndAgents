public class Hand {
    private int infantry, cavalry, artillery;

    public int getInfantry() {
        return infantry;
    }

    public int getArtillery() {
        return artillery;
    }

    public int getCavalry() {
        return cavalry;
    }

    public void setInfantry(Integer infantry){
        this.infantry = infantry;
    }

    public void setArtillery(int artillery) {
        this.artillery = artillery;
    }

    public void setCavalry(int cavalry) {
        this.cavalry = cavalry;
    }

    public Hand(){
        infantry = 0;
        cavalry = 0;
        artillery = 0;
    }
}
