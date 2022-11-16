package battleship;

import java.io.IOException;
import java.util.*;

import static java.lang.Math.abs;

public class Main {

    //Ships
    public static class Ship {
        protected String name;
        protected int size;
        protected ArrayList<int[]> coord;
        protected int healthPoint;
        protected boolean destroyed;

        Ship (String name, int size, int healthPoint, boolean destroyed) {
            this.name = name;
            this.size = size;
            this.healthPoint = healthPoint;
            this.destroyed = destroyed;
            this.coord = new ArrayList<>();
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public void setCoord(ArrayList<int[]> coord) {
            this.coord = coord;
        }

        public ArrayList<int[]> getCoord() {
            return coord;
        }

        public void addCoord(int[] x) {
            coord.add(x);
        }

        public void setHealthPoint(int healthPoint) {
            this.healthPoint = healthPoint;
        }

        public int getHealthPoint() {
            return healthPoint;
        }
    }

    //Players
    public static class User {

        protected String name;
        protected List<Ship> ships = new ArrayList<>();
        protected String[][] gameMap;
        protected String[][] fogOfWar;
        protected int shipsLost;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setShips(List<Ship> ships) {
            this.ships = ships;
        }

        public List<Ship> getShips() {
            return ships;
        }

        public void setGameMap(String[][] gameMap) {
            this.gameMap = gameMap;
        }

        public String[][] getGameMap() {
            return gameMap;
        }

        public void setFogOfWar(String[][] fogOfWar) {
            this.fogOfWar = fogOfWar;
        }

        public String[][] getFogOfWar() {
            return fogOfWar;
        }
    }

    public static List<Ship> createNewShips() {
        List<Ship> shipList = new ArrayList<>();
        shipList.add(new Ship("Aircraft Carrier", 5, 5, false));
        shipList.add(new Ship("Battleship", 4, 4, false));
        shipList.add(new Ship("Submarine", 3, 3, false));
        shipList.add(new Ship("Cruiser", 3, 3, false));
        shipList.add(new Ship("Destroyer", 2, 2, false));

        return shipList;
    }

    public static void main(String[] args) {

        /**
        List<Ship> shipList = new ArrayList<>();
        shipList.add(new Ship("Aircraft Carrier", 5, 5, false));
        shipList.add(new Ship("Battleship", 4, 4, false));
        shipList.add(new Ship("Submarine", 3, 3, false));
        shipList.add(new Ship("Cruiser", 3, 3, false));
        shipList.add(new Ship("Destroyer", 2, 2, false));
         **/

        //Create player1 object
        User player1 = new User();

        player1.setName("Player 1");
        player1.setShips(createNewShips());
        player1.setGameMap(new String[10][10]);
        player1.setFogOfWar(new String[10][10]);
        player1.shipsLost = 0;

        //Create player2 object
        User player2 = new User();

        player2.setName("Player 2");
        player2.setShips(createNewShips());
        player2.setGameMap(new String[10][10]);
        player2.setFogOfWar(new String[10][10]);
        player2.shipsLost = 0;

        User[] users = { player1, player2 };


        // Initialize maps
        initializeMaps(users);

        // Print the map and place ships
        printAndPlaceShips(users);


        System.out.println("The game starts!");
        int a = 0, b = 1;
        while (users[a].shipsLost < 5 || users[b].shipsLost < 5) {

            gamePlay(users[a], users[b]);
            int temp = a;
            a = b;
            b = temp;

            if (users[a].shipsLost == 5 || users[b].shipsLost == 5) {
                System.out.println("You sank the last ship. You won. Congratulations!");
                return;
            }

            promptEnterKey();

        }
    }

    //Initialize the map
    private static void initializeMap(String[][] mapToInitialize) {

        for (String[] strings : mapToInitialize) {
            Arrays.fill(strings, "~");
        }
    }

    public static void initializeMaps(User[] users) {
        for (User user : users) {
            initializeMap(user.gameMap);
            initializeMap(user.fogOfWar);
        }
    }

    //Print the map in console
    private static void printMap(String[][] mapToPrint) {
        int[] mapColons = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        char[] mapRows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

        System.out.print(" ");
        for (int mapColon : mapColons) {
            System.out.print(" " + mapColon);
        }
        System.out.print("\n");
        for (int i = 0; i < mapToPrint.length; i++) {
            System.out.print(mapRows[i]);
            for (int j = 0; j < mapToPrint[i].length; j++) {
                System.out.print(" " + mapToPrint[i][j]);
            }
            System.out.print("\n");
        }
    }

    //Pass the move to another player
    private static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    //Ships
    public enum Ship {
        Carrier("Aircraft Carrier", 5),
        Battleship("Battleship", 4),
        Submarine("Submarine", 3),
        Cruiser("Cruiser", 3),
        Destroyer("Destroyer", 2);

        public final String label;
        public final int size;

        private static final Map<String, Ship> BY_LABEL = new HashMap<>();
        private static final Map<Integer, Ship> BY_SIZE = new HashMap<>();

        static {
            for (Ship s : values()) {
                BY_LABEL.put(s.label, s);
                BY_SIZE.put(s.size, s);
            }
        }

        Ship (String label, int size) {
            this.label = label;
            this.size = size;
        }

        public static Ship valueOfLabel (String label) {
            return BY_LABEL.get(label);
        }
        public static Ship valueOfSize (int size) {
            return BY_SIZE.get(size);
        }
    }
     **/

    //Place all ships in order
    private static void placeShips(User player) {
        for (Ship playerShip : player.ships) {
            placeShip(playerShip, player.getGameMap());
        }
    }

    //Place the specified ship on the game map
    private static void placeShip(Ship playerShip, String[][] gameMap) {
        char[] splitCoordinates;

        System.out.printf("Enter the coordinates of the %s (%d cells):", playerShip.getName(), playerShip.getSize());
        splitCoordinates = parseShipCoordinates();

        if(splitCoordinates[0] < 'A' || splitCoordinates[2] < 'A' || splitCoordinates[0] > 'K' || splitCoordinates[2] > 'K') {
            System.out.println("Error! Out of map location! Try again:");
        } else if (splitCoordinates[1] < '0' || splitCoordinates[3] < '0' || splitCoordinates[1] > ':' || splitCoordinates[3] > ':') {
            System.out.println("Error! Out of map location! Try again:");
        } else {
            if (splitCoordinates[0] == splitCoordinates[2]) {
                if (abs(splitCoordinates[1] - splitCoordinates[3]) != playerShip.getSize() - 1) {
                    System.out.printf("Error! Wrong length of the %s! Try again:\n", playerShip.getName());
                    placeShip(playerShip, gameMap);
                } else if (splitCoordinates[1] > splitCoordinates[3]) {
                    if (!checkShipPerimeter(splitCoordinates, gameMap)) {
                        for (int j = splitCoordinates[3] - 49; j <= splitCoordinates[1] - 49; j++) {
                            gameMap[(splitCoordinates[0] - 65)][j] = "O";
                            playerShip.addCoord(new int[]{splitCoordinates[0] - 65, j});
                        }
                        printMap(gameMap);
                    }
                    else {
                        System.out.println("Error! You placed it too close to another one. Try again:\n");
                        placeShip(playerShip, gameMap);
                    }
                } else {
                    if (!checkShipPerimeter(splitCoordinates, gameMap)) {
                        for (int j = splitCoordinates[1] - 49; j <= splitCoordinates[3] - 49; j++) {
                            gameMap[(splitCoordinates[0] - 65)][j] = "O";
                            playerShip.addCoord(new int[]{splitCoordinates[0] - 65, j});
                        }
                        printMap(gameMap);
                    }
                    else {
                        System.out.println("Error! You placed it too close to another one. Try again:\n");
                        placeShip(playerShip, gameMap);
                    }
                }
            } else if (splitCoordinates[1] == splitCoordinates[3]) {
                if (abs(splitCoordinates[0] - splitCoordinates[2]) != playerShip.getSize() - 1) {
                    System.out.printf("Error! Wrong length of the %s! Try again:\n", playerShip.getName());
                    placeShip(playerShip, gameMap);
                } else if (splitCoordinates[0] > splitCoordinates[2]) {
                    if (!checkShipPerimeter(splitCoordinates, gameMap)) {
                        for (int i = (int) splitCoordinates[2] - 65; i <= (int) splitCoordinates[0] - 65; i++) {
                            gameMap[i][splitCoordinates[1] - 49] = "O";
                            playerShip.addCoord(new int[]{i, splitCoordinates[1] - 49});
                        }
                        printMap(gameMap);
                    }
                    else {
                        System.out.println("Error! You placed it too close to another one. Try again:\n");
                        placeShip(playerShip, gameMap);
                    }
                } else {
                    if (!checkShipPerimeter(splitCoordinates, gameMap)) {
                        for (int i = (int) splitCoordinates[0] - 65; i <= (int) splitCoordinates[2] - 65; i++) {
                            gameMap[i][splitCoordinates[1] - 49] = "O";
                            playerShip.addCoord(new int[]{i, splitCoordinates[1] - 49});
                        }
                        printMap(gameMap);
                    }
                    else {
                        System.out.println("Error! You placed it too close to another one. Try again:\n");
                        placeShip(playerShip, gameMap);
                    }
                }
            } else {
                System.out.println("Error! Wrong ship location! Try again:");
                placeShip(playerShip, gameMap);
            }
        }
    }

    //Get ship coordinates
    private static char[] parseShipCoordinates() {
        Scanner scanner = new Scanner(System.in);

        String coordinates = scanner.nextLine();

        String[] splitedString = coordinates.split("\\s+"); //(?!^)
        // String coordinatesColapsed = coordinates.replaceAll("\\s+","");

        String firstCoordinate = splitedString[0];
        String secondCoordinate = splitedString[1];

        int firstRow = Integer.parseInt(firstCoordinate.substring(1));
        int secondRow = Integer.parseInt(secondCoordinate.substring(1));

        String firstColumn = firstCoordinate.substring(0,1);
        String secondColumn = secondCoordinate.substring(0,1);

        char[] splited = new char[4];
        splited[0] = firstColumn.charAt(0);
        splited[1] = (char) (firstRow + 48);
        splited[2] = secondColumn.charAt(0);
        splited[3] = (char) (secondRow + 48);

        return splited;
    }

    //Check ship perimeter before placing it
    private static boolean checkShipPerimeter(char[] splitCoordinates, String[][] gameMap) {
        int top = 0, bottom = 0, left = 0, right = 0;

        if(splitCoordinates[0] == 'A' || splitCoordinates[2] == 'A')
            top = 1;
        if(splitCoordinates[2] == 'J' || splitCoordinates[0] == 'J')
            bottom = 1;
        if(splitCoordinates[1] == '1' || splitCoordinates[3] == '1')
            left = 1;
        if(splitCoordinates[3] == ':' || splitCoordinates[1] == ':')
            right = 1;

        if (splitCoordinates[0] == splitCoordinates[2]) {
            if (splitCoordinates[1] < splitCoordinates[3]) {
                for (int i = splitCoordinates[0] - 66 + top; i < splitCoordinates[2] - 63 - bottom; i++) {
                    for (int j = splitCoordinates[1] - 50 + left; j < splitCoordinates[3] - 47 - right; j++) {
                        if (gameMap[i][j].equals("O"))
                            return true;
                    }
                }
            }
            else {
                for (int i = splitCoordinates[0] - 66 + top; i < splitCoordinates[2] - 63 - bottom; i++) {
                    for (int j = splitCoordinates[3] - 50 + left; j < splitCoordinates[1] - 47 - right; j++) {
                        if (gameMap[i][j].equals("O"))
                            return true;
                    }
                }
            }
        }
        else if (splitCoordinates[1] == splitCoordinates[3]) {
            if (splitCoordinates[0] < splitCoordinates[2]) {
                for (int i = splitCoordinates[0] - 66 + top; i < splitCoordinates[2] - 63 - bottom; i++) {
                    for (int j = splitCoordinates[1] - 50 + left; j < splitCoordinates[3] - 47 - right; j++) {
                        if (gameMap[i][j].equals("O"))
                            return true;
                    }
                }
            }
            else {
                for (int i = splitCoordinates[2] - 66 + top; i < splitCoordinates[0] - 63 - bottom; i++) {
                    for (int j = splitCoordinates[1] - 50 + left; j < splitCoordinates[3] - 47 - right; j++) {
                        if (gameMap[i][j].equals("O"))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    public static void printAndPlaceShips(User[] users) {
        for (User user : users) {
            System.out.printf("%s, place your ships on the game field \n", user.getName());
            printMap(user.getGameMap());
            placeShips(user);
            promptEnterKey();
        }
    }

    //Get shot coordinates
    private static char[] parseShotCoordinates() {
        Scanner scanner = new Scanner(System.in);

        String coordinates = scanner.nextLine();

        String column = coordinates.substring(0,1);
        int row = Integer.parseInt(coordinates.substring(1));

        char[] splited = new char[2];
        splited[0] = column.charAt(0);
        splited[1] = (char) (row + 48);

        return splited;
    }

    //Check shot perimeter to see if ship still alive
    private static boolean checkShotPerimeter(char[] splitCoordinates, User player) {
        int top = 0, bottom = 0, left = 0, right = 0;

        if(splitCoordinates[0] == 'A')
            top = 1;
        if(splitCoordinates[0] == 'J')
            bottom = 1;
        if(splitCoordinates[1] == '1')
            left = 1;
        if(splitCoordinates[1] == ':')
            right = 1;

        for (int i = splitCoordinates[0] - 66 + top; i < splitCoordinates[0] - 63 - bottom; i++) {
            for (int j = splitCoordinates[1] - 50 + left; j < splitCoordinates[1] - 47 - right; j++) {
                if (player.gameMap[i][j].equals("O"))
                    return true;
            }
        }

        return false;
    }

    //Player A shot Player B
    private static void shot(User playerA, User playerB) {
        char[] splitCoordinates;

        splitCoordinates = parseShotCoordinates();

        if(splitCoordinates[0] < 'A' || splitCoordinates[0] > 'K') {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            shot(playerA, playerB);
        } else if (splitCoordinates[1] < '0' || splitCoordinates[1] > ':') {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            shot(playerA, playerB);
        } else {
            if (Objects.equals(playerB.gameMap[(splitCoordinates[0] - 65)][(splitCoordinates[1] - 49)], "X")) {
                System.out.println("You hit a ship!");
            } else if (Objects.equals(playerB.gameMap[(splitCoordinates[0] - 65)][(splitCoordinates[1] - 49)], "O")) {
                playerB.gameMap[(splitCoordinates[0] - 65)][(splitCoordinates[1] - 49)] = "X";
                playerA.fogOfWar[(splitCoordinates[0] - 65)][(splitCoordinates[1] - 49)] = "X";

                int checkedShip = checkShipsParts(playerB);

                if (checkedShip == 2) {
                    System.out.println("\n");
                } else if (checkedShip == 1) {
                    System.out.println("You sank a ship!");
                } else {
                    System.out.println("You hit a ship!");
                }
            } else {
                playerB.gameMap[(splitCoordinates[0] - 65)][(splitCoordinates[1] - 49)] = "M";
                playerA.fogOfWar[(splitCoordinates[0] - 65)][(splitCoordinates[1] - 49)] = "M";
                System.out.println("You missed!");
            }
        }
    }

    private static int checkShipsParts(User player) {

        for (Ship ship : player.getShips()) {
            int counter = 0;

            System.out.println(ship.getName() + ": " + Arrays.deepToString(ship.getCoord().toArray()));
            for (int[] shipPart : ship.getCoord()) {
                if (player.gameMap[shipPart[0]][shipPart[1]].equals("X")) {
                    counter++;
                }

                if (!ship.destroyed && counter >= ship.getSize()) {
                    System.out.println("Ship destroyed");
                    ship.destroyed = true;
                    player.shipsLost++;
                    System.out.println(player.shipsLost);
                    if (player.shipsLost >= 5)
                        return 2; //Last ship destroyed
                    else
                        return 1; //New ship destroyed
                }
            }
        }

        return 0; //No new ship destroyed
    }

    //Gameplay
    private static void gamePlay(User playerA, User playerB) {
        printMap(playerA.fogOfWar);
        System.out.println("---------------------");
        printMap(playerA.gameMap);
        System.out.println("");
        System.out.printf("%s, it's your turn: \n", playerA.name);
        shot(playerA, playerB);
    }

    //Verifica de ce se apleaza a doua oara metoda 'checkShipsParts'
}