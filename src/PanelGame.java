import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import javax.swing.JPanel;

public class PanelGame extends JPanel implements Runnable {

	private static final boolean True = false;
	final int TILE_SIZE = 20;
	final int MAX_WIDTH = 30;
	final int MAX_HEIGHT = 30;
	final int STEP_COST = 10;
	final int CROSS_COST = 14;
	Tile[][] node = new Tile[30][30];
	Random random = new Random();
	int playerX, playerY, botX, botY, foodX, foodY, moveX, moveY, speed, time;
	boolean playerEat = false;

	PriorityQueue<Tile> queue = new PriorityQueue<>(new Comparator<Tile>() {
		@Override
		public int compare(Tile o1, Tile o2) {
			return o1.cost - o2.cost;
		}
	});

	public PanelGame() {
		Thread thread = new Thread(this);
		thread.start();
		initializeMap();
	}

	public void initializeMap() {
		String map = null;
		int x, y;

		for (int i = 0; i < MAX_WIDTH; i++) {
			for (int j = 0; j < MAX_HEIGHT; j++) {
				if (i == 0 || i == MAX_HEIGHT - 1 || j == 0 || j == MAX_WIDTH - 1) {
					map = "Tembok";
				} else {
					map = "Jalan";
				}
				node[i][j] = new Tile(i, j, null, Integer.MAX_VALUE, false, map);
			}
		}

		for (int i = 0; i < 100; i++) {
			do {
				x = random.nextInt(30);
				y = random.nextInt(30);
			} while (!cekTembok(x, y));
			node[x][y].map = "Tembok";
		}

		do {
			playerX = random.nextInt(30);
			playerY = random.nextInt(30);
		} while (!cekPlayer(playerX, playerY));
		node[playerX][playerY].map = "Player";

		do {
			botX = random.nextInt(30);
			botY = random.nextInt(30);
		} while (!cekBot(botX, botY));
		node[botX][botY].map = "Bot";

		generateFood();
	}

	public void generateFood() {
		do {
			foodX = random.nextInt(30);
			foodY = random.nextInt(30);
		} while (!cekFood(foodY, foodY));
		node[foodX][foodY].map = "Food";
	}

	public boolean cekTembok(int x, int y) {
		if (node[x][y].map == "Tembok")
			return false;
		if (node[x - 1][y].map == "Tembok")
			return false;
		if (node[x + 1][y].map == "Tembok")
			return false;
		if (node[x][y - 1].map == "Tembok")
			return false;
		if (node[x][y + 1].map == "Tembok")
			return false;
		return true;
	}

	public boolean cekPlayer(int x, int y) {
		if (node[x][y].map == "Tembok")
			return false;
		if (node[x][y].map == "Bot")
			return false;
		if (node[x][y].map == "Food")
			return false;
		return true;
	}

	public boolean cekBot(int x, int y) {
		if (node[x][y].map == "Tembok")
			return false;
		if (node[x][y].map == "Player")
			return false;
		if (node[x][y].map == "Food")
			return false;
		return true;
	}

	public boolean cekFood(int x, int y) {
		if (node[x][y].map == "Tembok")
			return false;
		if (node[x][y].map == "Player")
			return false;
		if (node[x][y].map == "Bot")
			return false;
		return true;
	}

	public void paintTile(Graphics g, int i, int j) {
		if (node[i][j].map == "Tembok") {
			g.setColor(Color.BLACK);
			g.fillRect(node[i][j].x * TILE_SIZE, node[i][j].y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
		} else if (node[i][j].map == "Jalan") {
			g.setColor(Color.WHITE);
			g.fillRect(node[i][j].x * TILE_SIZE, node[i][j].y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
		} else if (node[i][j].map == "Player") {
			g.setColor(new Color(255, 0, 255, 60));
			g.fillRect(node[i][j].x * TILE_SIZE, node[i][j].y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			g.setColor(new Color(0, 255, 0));
			int triangleX[] = { node[i][j].x * TILE_SIZE, node[i][j].x * TILE_SIZE + 10,
					node[i][j].x * TILE_SIZE + TILE_SIZE };
			int triangleY[] = { node[i][j].y * TILE_SIZE + TILE_SIZE, node[i][j].y * TILE_SIZE,
					node[i][j].y * TILE_SIZE + TILE_SIZE };
			Polygon triangle = new Polygon(triangleX, triangleY, 3);
			g.fillPolygon(triangle);
		} else if (node[i][j].map == "Bot") {
			g.setColor(new Color(255, 0, 255, 60));
			g.fillRect(node[i][j].x * TILE_SIZE, node[i][j].y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
			g.setColor(new Color(0, 255, 255));
			int triangleX[] = { node[i][j].x * TILE_SIZE, node[i][j].x * TILE_SIZE + 10,
					node[i][j].x * TILE_SIZE + TILE_SIZE };
			int triangleY[] = { node[i][j].y * TILE_SIZE + TILE_SIZE, node[i][j].y * TILE_SIZE,
					node[i][j].y * TILE_SIZE + TILE_SIZE };
			Polygon triangle = new Polygon(triangleX, triangleY, 3);
			g.fillPolygon(triangle);
		} else if (node[i][j].map == "Food") {
			g.setColor(new Color(255, 0, 0));
			g.fillRect(node[i][j].x * TILE_SIZE, node[i][j].y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
		}
	}

	void paintInformation(Graphics g) {
		g.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 30));
		g.drawString("Menu", 700, 100);
		g.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 20));
		g.drawString("Level : " + GameInformation.level, 685, 150);
		g.setFont(new Font("Franklin Gothic Medium", Font.PLAIN, 15));
		g.drawString("Score", 700, 200);
		g.drawString("Player : " + GameInformation.scorePlayer, 700, 225);
		g.drawString("Bot : " + GameInformation.scoreBot, 700, 250);
		if (GameInformation.time % 60 == 0)
			time = GameInformation.time / 60;
		g.drawString("Time : " + time, 705, 350);
		if (GameInformation.result != null) {
			g.drawString(GameInformation.result, 695, 450);
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (int i = 0; i < MAX_WIDTH; i++) {
			for (int j = 0; j < MAX_HEIGHT; j++) {
				g.setColor(Color.WHITE);
				paintTile(g, i, j);
				g.setColor(Color.BLACK);
				g.drawLine(node[i][j].x * TILE_SIZE, node[i][j].y * TILE_SIZE, node[i][j].x * TILE_SIZE + TILE_SIZE,
						node[i][j].y * TILE_SIZE);
				g.drawLine(node[i][j].x * TILE_SIZE, node[i][j].y * TILE_SIZE, node[i][j].x * TILE_SIZE,
						node[i][j].y * TILE_SIZE + TILE_SIZE);
			}
		}
		paintInformation(g);
	}

	public void playerControl(String direction) {
		if (direction == "Left") {
			playerMovement(node[playerX - 1][playerY].map, playerX, playerY, playerX - 1, playerY);
		}
		if (direction == "Up") {
			playerMovement(node[playerX][playerY - 1].map, playerX, playerY, playerX, playerY - 1);
		}
		if (direction == "Down") {
			playerMovement(node[playerX][playerY + 1].map, playerX, playerY, playerX, playerY + 1);
		}
		if (direction == "Right") {
			playerMovement(node[playerX + 1][playerY].map, playerX, playerY, playerX + 1, playerY);
		}
		repaint();
	}

	public void playerMovement(String now, int oldX, int oldY, int newX, int newY) {
		if (now == "Jalan") {
			node[oldX][oldY].map = "Jalan";
			node[newX][newY].map = "Player";
			playerX = newX;
			playerY = newY;
		}
		if (now == "Food") {
			node[oldX][oldY].map = "Jalan";
			node[newX][newY].map = "Player";
			playerX = newX;
			playerY = newY;
			playerEat = true;
			GameInformation.scorePlayer += 1;
			generateFood();
		}
		if (now == "Bot") {
			node[oldX][oldY].map = "Jalan";
			node[newX][newY].map = "Bot";
			playerX = newX;
			playerY = newY;
		}
	}

	public boolean walkable(String map) {
		if (map == "Tembok")
			return false;
		return true;
	}

	public void initializeWeight() {
		for (int i = 0; i < MAX_WIDTH; i++) {
			for (int j = 0; j < MAX_HEIGHT; j++) {
				node[i][j].isVisited = false;
				node[i][j].parent = null;
				node[i][j].cost = Integer.MAX_VALUE;
			}
		}
	}

	public void dijkstraEasyMedium() {
		queue.clear();
		node[botX][botY].cost = 0;
		queue.add(node[foodX][foodY]);

		while (!queue.isEmpty() && !node[botX][botY].isVisited) {
			Tile now = queue.peek();
			int x = now.x;
			int y = now.y;

			if (now.isVisited) {
				queue.remove(now);
				continue;
			}

			now.isVisited = true;

			if (walkable(node[x][y - 1].map) && node[x][y - 1].cost > now.cost + STEP_COST) {
				node[x][y - 1].cost = now.cost + STEP_COST;
				node[x][y - 1].parent = node[x][y];
				queue.add(node[x][y - 1]);
			}

			if (walkable(node[x][y + 1].map) && node[x][y + 1].cost > now.cost + STEP_COST) {
				node[x][y + 1].cost = now.cost + STEP_COST;
				node[x][y + 1].parent = node[x][y];
				queue.add(node[x][y + 1]);
			}

			if (walkable(node[x - 1][y].map) && node[x - 1][y].cost > now.cost + STEP_COST) {
				node[x - 1][y].cost = now.cost + STEP_COST;
				node[x - 1][y].parent = node[x][y];
				queue.add(node[x - 1][y]);
			}

			if (walkable(node[x + 1][y].map) && node[x + 1][y].cost > now.cost + STEP_COST) {
				node[x + 1][y].cost = now.cost + STEP_COST;
				node[x + 1][y].parent = node[x][y];
				queue.add(node[x + 1][y]);
			}

			queue.remove(now);
		}
	}

	public void dijkstraHard() {
		queue.clear();
		node[botX][botY].cost = 0;
		queue.add(node[foodX][foodY]);

		while (!queue.isEmpty() && !node[botX][botY].isVisited) {
			Tile now = queue.peek();
			int x = now.x;
			int y = now.y;

			if (now.isVisited) {
				queue.remove(now);
				continue;
			}

			now.isVisited = true;

			if (walkable(node[x][y - 1].map) && node[x][y - 1].cost > now.cost + STEP_COST) {
				node[x][y - 1].cost = now.cost + STEP_COST;
				node[x][y - 1].parent = node[x][y];
				queue.add(node[x][y - 1]);
			}

			if (walkable(node[x][y + 1].map) && node[x][y + 1].cost > now.cost + STEP_COST) {
				node[x][y + 1].cost = now.cost + STEP_COST;
				node[x][y + 1].parent = node[x][y];
				queue.add(node[x][y + 1]);
			}

			if (walkable(node[x - 1][y].map) && node[x - 1][y].cost > now.cost + STEP_COST) {
				node[x - 1][y].cost = now.cost + STEP_COST;
				node[x - 1][y].parent = node[x][y];
				queue.add(node[x - 1][y]);
			}

			if (walkable(node[x + 1][y].map) && node[x + 1][y].cost > now.cost + STEP_COST) {
				node[x + 1][y].cost = now.cost + STEP_COST;
				node[x + 1][y].parent = node[x][y];
				queue.add(node[x + 1][y]);
			}

			if (walkable(node[x - 1][y - 1].map) && node[x - 1][y - 1].cost > now.cost + CROSS_COST) {
				node[x - 1][y - 1].cost = now.cost + CROSS_COST;
				node[x - 1][y - 1].parent = node[x][y];
				queue.add(node[x - 1][y - 1]);
			}

			if (walkable(node[x + 1][y - 1].map) && node[x + 1][y - 1].cost > now.cost + CROSS_COST) {
				node[x + 1][y - 1].cost = now.cost + CROSS_COST;
				node[x + 1][y - 1].parent = node[x][y];
				queue.add(node[x + 1][y - 1]);
			}

			if (walkable(node[x + 1][y + 1].map) && node[x + 1][y + 1].cost > now.cost + CROSS_COST) {
				node[x + 1][y + 1].cost = now.cost + CROSS_COST;
				node[x + 1][y + 1].parent = node[x][y];
				queue.add(node[x + 1][y + 1]);
			}

			if (walkable(node[x - 1][y + 1].map) && node[x - 1][y + 1].cost > now.cost + CROSS_COST) {
				node[x - 1][y + 1].cost = now.cost + CROSS_COST;
				node[x - 1][y + 1].parent = node[x][y];
				queue.add(node[x - 1][y + 1]);
			}

			queue.remove(now);
		}
	}

	public void move() {
		if (GameInformation.onPlay) {
			int tempX, tempY;
			tempX = botX;
			tempY = botY;
			node[botX][botY].map = "Jalan";
			botX = node[tempX][tempY].parent.x;
			botY = node[tempX][tempY].parent.y;
			node[botX][botY].map = "Bot";
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();
		}
	}

	public void playGameEasy() {
		speed = 400;
		initializeWeight();
		dijkstraEasyMedium();
		while (botX != foodX || botY != foodY) {
			if (playerEat) {
				break;
			}
			move();
		}
		if (playerEat) {
			playerEat = false;
			playGameEasy();
			return;
		} else {
			GameInformation.scoreBot += 1;
			generateFood();
			playGameEasy();
			return;
		}
	}

	public void playGameMedium() {
		speed = 350;
		initializeWeight();
		dijkstraEasyMedium();
		while (botX != foodX || botY != foodY) {
			if (playerEat) {
				break;
			}
			move();
		}
		if (playerEat) {
			playerEat = false;
			playGameMedium();
			return;
		} else {
			GameInformation.scoreBot += 1;
			generateFood();
			playGameMedium();
			return;
		}
	}

	public void playGameHard() {
		speed = 300;
		initializeWeight();
		dijkstraHard();
		while (botX != foodX || botY != foodY) {
			if (playerEat) {
				break;
			}
			move();
		}
		if (playerEat) {
			playerEat = false;
			playGameHard();
			return;
		} else {
			GameInformation.scoreBot += 1;
			generateFood();
			playGameHard();
			return;
		}
	}

	@Override
	public void run() {
		while (true && GameInformation.onPlay) {
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			GameInformation.time += 1;
			if (GameInformation.time == 900) {
				if (GameInformation.scorePlayer > GameInformation.scoreBot)
					GameInformation.result = "Player Win!";
				else
					GameInformation.result = "Player Lose!";
				GameInformation.onPlay = false;
				repaint();
				break;
			}
			repaint();
		}
	}
}
