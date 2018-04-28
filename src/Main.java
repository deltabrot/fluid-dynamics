import javax.swing.*;


public class Main{
	public static void main(String args[]){
		JFrame frame = new JFrame("Navier");
		Render panel = new Render();
		int menuWidth = 387, menuHeight = 240;
		GUI gui;
		if((Render.drawSize*(NavierStokesSolver.N+2))+7 >= menuWidth){
			gui = new GUI((((Render.drawSize*(NavierStokesSolver.N+2))+7)/2)-(menuWidth/2),(Render.drawSize*(NavierStokesSolver.N+2))+7,menuWidth,menuHeight);
		}
		else{
			gui = new GUI(0,(Render.drawSize*(NavierStokesSolver.N+2))+7,menuWidth,menuHeight);
		}
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.add(panel);
		frame.add(gui);
		
		if((Render.drawSize*(NavierStokesSolver.N+2))+7 >= menuWidth){
			frame.setSize((Render.drawSize*(NavierStokesSolver.N+2))+7,(Render.drawSize*(NavierStokesSolver.N+2))+7 + menuHeight);
		}
		else{
			frame.setSize(menuWidth,(Render.drawSize*(NavierStokesSolver.N+2))+7 + menuHeight);
			panel.setBounds(((menuWidth-7)/2)-(((Render.drawSize*(NavierStokesSolver.N+2))+1)/2), 0, (Render.drawSize*(NavierStokesSolver.N+2))+1, (Render.drawSize*(NavierStokesSolver.N+2))+1);
		}
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}