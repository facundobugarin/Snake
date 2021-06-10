import javax.swing.JFrame;

public class GameFrame extends JFrame{

	GameFrame(){
	
		this.add(new GamePanelll()); // lo mismo que instanciarlo y usar el this add dentro de la instancia
		this.setTitle("Snake");	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
