

public class PlayerShip {

	private int CurrLife, MaxLife;
	private int DmgLevel;
	private double MS;
	enum WeaponType{Base,Spray,Laser};
	boolean isinvul;
	private double SpeedX, SpeedY;
	//Drawable Sprite;
	
	
	public int getCurrLife() {
		return CurrLife;
	}
	public void setCurrLife(int currLife) {
		CurrLife = currLife;
	}
	public int getMaxLife() {
		return MaxLife;
	}
	public void setMaxLife(int maxLife) {
		MaxLife = maxLife;
	}
	public int getDmgLevel() {
		return DmgLevel;
	}
	public void setDmgLevel(int dmgLevel) {
		DmgLevel = dmgLevel;
	}
	public double getMS() {
		return MS;
	}
	public void setMS(double mS) {
		MS = mS;
	}
	public double getSpeedX() {
		return SpeedX;
	}
	public void setSpeedX(double speedX) {
		SpeedX = speedX;
	}
	public double getSpeedY() {
		return SpeedY;
	}
	public void setSpeedY(double speedY) {
		SpeedY = speedY;
	}
	
	
	
	
	
	
	
}
