import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.Timer;


public class Render extends JPanel implements ActionListener, MouseMotionListener, MouseListener{
	private static final long serialVersionUID = 1L;
	Timer time = new Timer(10,this);
	static NavierStokesSolver solver = new NavierStokesSolver();
	
	//NEW
	static double visc = 0.0001, dt = 0.01, diff = 0.0001, limitVelocity = 100, gasDensity = 10;
	int oldMouseX = 0, oldMouseY = 0, mouseX = 0, mouseY = 0;
	static int drawSize = 6;
	static Color col = new Color(255,255,255), bgCol = new Color(0,0,0);
	boolean leftMouseDown = false, rightMouseDown = false;
	static boolean normalMode = true, fireMode = false, rainbowMode = false;
	static boolean drawVectorField = false, drawFluid = true, rightEnabled = false;
	
	public Render(){
		time.start();
		this.setBackground(bgCol);
		this.setBounds(0,0,drawSize*(NavierStokesSolver.N+2)+1,drawSize*(NavierStokesSolver.N+2)+1);
		this.addMouseMotionListener(this);
		this.addMouseListener(this);

	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawRect(0, 0, drawSize*(NavierStokesSolver.N+2), drawSize*(NavierStokesSolver.N+2));
		if(drawFluid){
			for(int y=0;y<NavierStokesSolver.N+2;y++){
				for(int x=0;x<NavierStokesSolver.N+2;x++){
					double density = (int)(255*solver.dense[boxNo(x,y)]);
					if(fireMode){
						double density2 = 0, density3 = 0;
						if(density >= 255){
							density2 = density-255;
							density = 255;
						}
						if(density2 >= 255){
							density3 = density2 - 255;
							density2 = 255;
						}
						if(density3 >= 255){
							density3 = 255;
						}
						g.setColor(new Color((int)density,(int)density2,(int)density3));
					}
					if(rainbowMode){
						double density2 = 0, density3 = 0;
						if(density >= 255){
							density2 = density-255;
							density = 255;
						}
						if(density2 >= 255){
							density = 255 - (density2-255);
							density2 = 255;
						}
						if(density < 0){
							density3 = -density;
							density = 0;
						}
						if(density3 >= 255){
							density2 = 255 - (density3-255);
							density3 = 255;
						}
						if(density2 < 0){
							density = -density2;
							density2 = 0;
							if(density >= 255){
								density3 = 255 - (density-255);
								density = 255;
							}
							if(density3 < 0){
								density3 = 0;
								density = 255;
							}
						}
						
						
						g.setColor(new Color((int)density,(int)density2,(int)density3));
					}
					if(normalMode){
						if(density >= 255){
							density = 255;
						}
						g.setColor(new Color(col.getRed(),col.getGreen(),col.getBlue(),(int)density));
					}
					
					
					g.fillRect(x*drawSize,y*drawSize,drawSize,drawSize);
				}
			}
		}
		
		if(drawVectorField){
			g.setColor(new Color(255,255,255));
			for(int y=0;y<NavierStokesSolver.N+2;y++){
				for(int x=0;x<NavierStokesSolver.N+2;x++){
					g.drawLine(x*drawSize, y*drawSize, (x*drawSize)+(int)(solver.u[boxNo(x,y)]*50), (y*drawSize)+(int)(solver.v[boxNo(x,y)]*50));
				}
			}
		}
		
	}
	public void actionPerformed(ActionEvent e){
		this.setBackground(bgCol);
		if(leftMouseDown){
			int cellX = (int) Math.floor(mouseX / drawSize);
			int cellY = (int) Math.floor(mouseY / drawSize);
			if(cellX >= 0 && cellX < NavierStokesSolver.N+2 && cellY >= 0 && cellY < NavierStokesSolver.N+2){
				solver.dense[boxNo(cellX,cellY)] += gasDensity;
			}
		}
		
		handleMouseMotion();
		solver.tick(dt, visc, diff);
        Toolkit.getDefaultToolkit().sync();
		repaint();
	}
	public int boxNo(int x, int y){
		return x + (NavierStokesSolver.N+2) * y;
	}
	
	private void handleMouseMotion() {
		double mouseDx = mouseX - oldMouseX;
		double mouseDy = mouseY - oldMouseY;
		int cellX = (int) Math.floor(mouseX / drawSize);
		int cellY = (int) Math.floor(mouseY / drawSize);
		if(cellX >= 0 && cellX < NavierStokesSolver.N+2 && cellY >= 0 && cellY < NavierStokesSolver.N+2){
			mouseDx = (Math.abs((float) mouseDx) > limitVelocity) ? Math.signum(mouseDx)
					* limitVelocity : mouseDx;
			mouseDy = (Math.abs((float) mouseDy) > limitVelocity) ? Math.signum(mouseDy)
					* limitVelocity : mouseDy;
			
			if(rightMouseDown || !rightEnabled){
				solver.applyForce(cellX, cellY, mouseDx, mouseDy);
			}

			oldMouseX = mouseX;
			oldMouseY = mouseY;
		}
	}
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1){
			leftMouseDown = true;
		}
		if(e.getButton() == 3){
			rightMouseDown = true;
		}
	}
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == 1){
			leftMouseDown = false;
		}
		if(e.getButton() == 3){
			rightMouseDown = false;
		}
	}
}
