import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanelll extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25; //tamaño de los cuadraditos de unidades donde moverse y spawnear manzana
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 90;
	final int x[] = new int[GAME_UNITS]; //array de todas las unidades horizontales
	final int y[] = new int[GAME_UNITS]; //array de los verticales
	int bodyParts = 6; //bodyParts es el largo de la serpiente
	int applesEaten;
	int appleX; //coord x manzana, hay que importar java.util.random para generar una ubicacion random
	int appleY; //coord y manzana, hay que importar java.util.random para generar una ubicacion random
	char direction = 'R';
	boolean running = false;
	static boolean gameOn = false; //estado del juego para poder pausarlo
	Timer timer;
	Random random;
	
	GamePanelll(){
		random = new Random(); //posicion inicial, para eso importo java.util.Random
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this); //no tengo idea de cómo funciona el action listener
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		if(running) {
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT); ////dibujo de las linas verticales de la grilla
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE); //dibujo de las linas horizontales de la grilla
			}
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			for(int i = 0; i< bodyParts;i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					g.setColor(new Color(45,180,0));
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}			
			}
			g.setColor(Color.red);
			g.setFont( new Font("Verdana",Font.BOLD, 30));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Barra para pausar. Puntaje: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Barra para pausar. Puntaje: "+applesEaten))/2, g.getFont().getSize());
		}
		else {
			gameOver(g);
		}
		
	}
	public void newApple(){ //metodo para generar coordenadas de manzana nueva para juego nuevo y para comer manzanas
		appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
	}
	public void move(){
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}
	public void checkCollisions() { //chequeo de choques
		//choque contra cuerpo
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		//choque contra bore izq
		if(x[0] < 0) {
			running = false;
		}
		//choque contra bore der
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//choque contra borde sup
		if(y[0] < 0) {
			running = false;
		}
		//choque contra borde inf
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont( new Font("Verdana",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Puntaje: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Puntaje: "+applesEaten))/2, g.getFont().getSize());
		//texto en pantalla
		g.setColor(Color.red);
		g.setFont( new Font("Verdana",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}
	
	public void pause() { //metodo para poner pausa
		GamePanelll.gameOn = true;
		timer.stop();
	}

	public void resume() { //metodo para poner play
		GamePanelll.gameOn = false;
		timer.start();
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
				
			case KeyEvent.VK_SPACE:
				if(GamePanelll.gameOn) {
					resume();
				} else {
					pause();
				}
				break;
			}
		}
	}
}