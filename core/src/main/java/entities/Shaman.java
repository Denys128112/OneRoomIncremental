package entities;

import java.util.List;

public class Shaman extends Archer{
    public Shaman(float x, float y, Player player, List<Projectile> projectileList){
        super(x,y,player,60f,140,1,0.5f,0.5f,projectileList);
    }
}
