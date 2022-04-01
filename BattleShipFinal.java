package BattleShip;

import java.util.Scanner;


class BattleShipFinal {
    public static void main(String[] args) {
        //test();

        Scanner scanner = new Scanner(System.in);
        BattleField player1 = new BattleField();
        BattleField player2 = new BattleField();

        //Spēlētāji ievada kuģus
        System.out.println();
        System.out.println("Player 1, place your ships on the game field");
        System.out.println();
        player1.printBattleShip(true);
        player1.placeBattleShips();
        System.out.println();
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        // NOMAINĪJU UZ NEXT PLAYERMOVE

        System.out.println();
        System.out.println("Player 2, place your ships on the game field");
        System.out.println();
        player2.printBattleShip(true);
        player2.placeBattleShips();

        System.out.println();
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();

        boolean player1Move = true;
        boolean player1PrevMove = false;
        boolean isWinner = false;


        do {
            int res = -1;
            try {
                Boolean shipSank = false;
                Boolean lastShipSank = false;
                if (player1Move == true) {
                    //spēlē 1.spēlētājs
                    if (player1Move != player1PrevMove) {
                        player2.printBattleShip();
                        System.out.println("---------------------");
                        player1.printBattleShip(true);
                        System.out.println();
                        System.out.println("Player 1, it's your turn:");
                        System.out.println();
                    }

                    String coord = scanner.nextLine();
                    coord = coord.toUpperCase();
                    res = player2.makeShot(coord);
                    //if (res == 1) player1Move = false;
                    if (res == 0) shipSank = player2.checkSankShip(coord);
                    if (shipSank == true) lastShipSank = player2.allShipsSahk();
                } else {
                    //spēlē 2.spēlētājs

                    if (player1Move != player1PrevMove) {
                        player1.printBattleShip();
                        System.out.println("---------------------");
                        player2.printBattleShip(true);
                        System.out.println();
                        System.out.println("Player 2, it's your turn:");
                        System.out.println();
                    }

                    String coord = scanner.nextLine();
                    coord = coord.toUpperCase();
                    res = player1.makeShot(coord);

                    //if (res == 1) player1Move = true;
                    if (res == 0) shipSank = player1.checkSankShip(coord);
                    if (shipSank == true) lastShipSank = player1.allShipsSahk();
                }
                switch (res) {
                    case 0:
                        //trāpīts
                        if (lastShipSank == true) {
                            System.out.println();
                            System.out.println("You sank the last ship. You won. Congratulations!");
                            isWinner = true;
                        } else if (shipSank == true) {
                            System.out.println();
                            System.out.println("You sank a ship! Specify a new target:");
                        } else {
                            System.out.println();
                            System.out.println("You hit a ship! Try again:");
                        }
                        break;
                    case 1:
                        //garām
                        System.out.println();
                        System.out.println("You missed. Try again:");
                        System.out.println();
                        break;
                    case 2:
                        //nepareiza koordināte
                        System.out.println("Error! You entered the wrong coordinates! Try again: ");
                        System.out.println();
                        break;
                }

                //coord = scanner.nextLine();
//                System.out.println();
//                System.out.println("Press Enter and pass the move to another player");
//                scanner.nextLine();  // PIEVIENOJU šis trīs rindas
//                System.out.println();
            } catch (Exception e) {
                System.out.println(e.getMessage());

                System.out.println("Error! You entered the wrong coordinates! Try again: ");
                System.out.println();
            }

            if ((res == 0 || res == 1) && isWinner == false) {
                player1PrevMove = player1Move;
                player1Move = !player1Move;
                System.out.println("Press Enter and pass the move to another player");
                scanner.nextLine();
                System.out.println();
            } else {
                player1PrevMove = player1Move;
            }

        } while (player1.allShipsSahk() == false && player2.allShipsSahk() == false);

        /*if (player1.allShipsSahk() == true){
            System.out.println("Player 2 won");
        } else if (player2.allShipsSahk() == true){
            System.out.println("Player 1 won");
        }*/

    }

    private static void test() {
        BattleField player1 = new BattleField();
        BattleField player2 = new BattleField();
        player1.testData();
        //player1.placeBattleShips();
        player1.printBattleShip(true);
        System.out.println("Player2");
        player2.printBattleShip(true);
    }

}


class BattleField {

    private Scanner scanner = new Scanner(System.in);

    private int[][] field = new int[10][10];
    private int[] shipLength = {5, 4, 3, 3, 2};
    private int shipsPlaced = 0;
    private String[] shipNames = {"Aircraft Carrier", "Battleship", "Submarine", "Cruiser", "Destroyer"};
    private int rows = 10;
    private String cols = "ABCDEFGHIJ";
    private char emptyField = '~'; //0 or 1 (if hidden)
    private char ship = 'O'; //1 if not hidden
    private char missedShot = 'M'; //2
    private char hitShip = 'X'; //3


    //printBattleShip dublikāts, nerāda savus nenogremdētos kuģus
    public void printBattleShip() {
        printBattleShip(false);
    }

    public void setField(int x, int y, int value) {
        if (value >= 0 && value <= 4) this.field[x][y] = value;
    }

    public int getField(int x, int y) {
        return this.field[x][y];
    }


    //laukuma satura izdruka
    //showAllShips: true - rāda spēlētāja nenogremdētos kuģus
    public void printBattleShip(boolean showAllShips) {
        for (int i = 0; i <= rows; i++) {
            for (int j = 0; j <= cols.length(); j++) {
                if (i == 0 && j == 0) {
                    System.out.print("  ");
                } else if (i == 0) {
                    System.out.print(j + " ");
                } else if (j == 0) {
                    System.out.print(cols.charAt(i - 1) + " ");
                } else {
                    switch (getField(i - 1, j - 1)) {
                        case 0:
                            System.out.print(emptyField + " ");
                            break;
                        case 1:
                            if (showAllShips == true)
                                System.out.print(ship + " ");
                            else {
                                System.out.print(emptyField + " ");
                            }
                            break;
                        case 2:
                            System.out.print(missedShot + " ");
                            break;
                        case 3:
                            System.out.print(hitShip + " ");
                            break;
                    }
                }
            }
            System.out.println(" ");
        }
    }

    //place all battle ships
    public void placeBattleShips() {
        boolean printCoordinates = false;
        System.out.println();
        System.out.println("Enter the coordinates of the " + shipNames[shipsPlaced] + " (" + shipLength[shipsPlaced] + " cells):");
        System.out.println();
        do {
            try {
//                printCoordinates = false;
                if (printCoordinates == true) {
                    System.out.println();
                    System.out.println("Enter the coordinates of the " + shipNames[shipsPlaced] + " (" + shipLength[shipsPlaced] + " cells):");
                    System.out.println();
                }
                String coordinates = scanner.nextLine();
                System.out.println();
                String[] coordinatesArray = coordinates.split(" ");
                String firstCoordinates = coordinatesArray[0].toUpperCase();
                String endCoordinates = coordinatesArray[1].toUpperCase();
                int result = placeBattleShip(firstCoordinates, endCoordinates);
                if (result == 1) {

                    System.out.println("Error! Wrong ship location! Try again:");
                    printCoordinates = false;
                    System.out.println();
                } else if (result == 2) {

                    System.out.println("Error! Wrong length of the " + shipNames[shipsPlaced] + "! Try again:");
                    printCoordinates = false;
                    System.out.println();
                } else if (result == 3) {

                    System.out.println("Error! You placed it too close to another one. Try again:");
                    printCoordinates = false;
                    System.out.println();
                } else {
                    printBattleShip(true);
                    printCoordinates = true;
                }
            } catch (Exception e) {

                System.out.println("Error! Wrong ship location! Try again:");
                printCoordinates = false;
                System.out.println();
            }
        } while (shipsPlaced < 5);
    }

    /* Ievieto kuģi uz laukuma
     * atgriež kļūdas kodus
     *   0 - No error
     *   1 - Wrong ship location!
     *   2 - Wrong length of the ship
     *   3 - You placed it too close to another one
     */
    private int placeBattleShip(String start_coordinate, String end_coordinate) {
        CoordinateToArr start = new CoordinateToArr(start_coordinate);
        CoordinateToArr end = new CoordinateToArr(end_coordinate);
        //System.out.println(Integer.toString(shipLength[shipsPlaced]));
        if (start.valid == false || end.valid == false) {
            //wrong location
            return 1;
        }
        if (start.x != end.x && start.y != end.y) {
            //wrong location
            return 1;
        }
        if (start.x > end.x || start.y > end.y) {
            CoordinateToArr temp = start;
            start = end;
            end = temp;
        }
        int shipLen = shipLength[shipsPlaced];
        int lenX = end.x - start.x;
        int lenY = end.y - start.y;
        if ((lenX > 4 || lenY > 4) || (lenX > 0 && lenY > 0) ||
                (lenX == 0 && lenY != shipLen - 1) ||
                (lenY == 0 && lenX != shipLen - 1)) {
            //wrong length
            return 2;
        }
        if (isFieldEmpty(start, end) == false) {
            //field not empty or to close
            return 3;
        }
        for (int x = start.x; x <= end.x; x++) {
            for (int y = start.y; y <= end.y; y++) {
                setField(x, y, 1);
                //field[x][y] = 1;

            }
        }
        //System.out.println(Integer.toString(lenX) + " " + Integer.toString(lenY) + " " + Integer.toString(shipLen));

        shipsPlaced++;
        return 0;
    }

    public boolean isFieldEmpty(CoordinateToArr start, CoordinateToArr end) {
        for (int x = start.x - 1; x <= end.x + 1; x++) {
            if (x < 0 || x > 9) continue;
            for (int y = start.y - 1; y <= end.y + 1; y++) {
                if (y < 0 || y > 9) continue;
                if (getField(x, y) != 0) return false;
            }
        }

        return true;
    }

    //dublikāts, lai kā koordināti var padot arī string
    //0 - hit
    //1 - missed
    //2 - wrong coordinate
    public int makeShot(String c) {
        CoordinateToArr cord = new CoordinateToArr(c);
        if (cord.valid == false) {
            return 2;
        } else {
            return makeShot(cord);
        }
    }

    //veikt šāvienu
    //1 - missed
    //0 - hit
    public int makeShot(CoordinateToArr c) {
        int fieldValue = getField(c.x, c.y);
        if (fieldValue == 1 || fieldValue == 3) {
            //field[c.x][c.y] = 3; //hit
            setField(c.x, c.y, 3);
            return 0;
        } else if (fieldValue == 0) {
            //field[c.x][c.y] = 2; //missed shot
            setField(c.x, c.y, 2);
            return 1;
        }
        return 0;
    }

    //dublikāts
    public boolean checkSankShip(String c) {
        CoordinateToArr cord = new CoordinateToArr(c);
        return checkSankShip(cord);
    }

    //pārbaudīt vai kuģis ir nogrimis
    //true - nogrimis, false - nav nogrimis
    public boolean checkSankShip(CoordinateToArr c) {
        boolean dir1 = true;
        boolean dir2 = true;
        boolean dir3 = true;
        boolean dir4 = true;
        //if (field[c.x][c.y] == 0 || field[c.x][c.y] == 2) return false;
        if (getField(c.x, c.y) == 0 || getField(c.x, c.y) == 2) return false;


        for (int i = 1; i < 5; i++) {
            if (dir1 == true && c.x - i >= 0) {
                if (getField(c.x - i, c.y) == 1) {
                    return false;
                } else if (getField(c.x - i, c.y) == 0 ||
                        getField(c.x - i, c.y) == 2) {
                    dir1 = false;
                }
            }
            if (dir2 == true && c.x + i <= 9) {
                if (getField(c.x + i, c.y) == 1) {
                    return false;
                } else if (getField(c.x + i, c.y) == 0 ||
                        getField(c.x + i, c.y) == 2) {
                    dir2 = false;
                }
            }

            if (dir3 == true && c.y - i >= 0) {
                if (getField(c.x, c.y - i) == 1) {
                    return false;
                } else if (getField(c.x, c.y - i) == 0 ||
                        getField(c.x, c.y - i) == 2) {
                    dir3 = false;
                }
            }

            if (dir4 == true && c.y + i <= 9) {
                if (getField(c.x, c.y + i) == 1) {
                    return false;
                } else if (getField(c.x, c.y + i) == 0 ||
                        getField(c.x, c.y + i) == 2) {
                    dir3 = false;
                }
            }

        }
        return true;
    }

    //pārbaudīt vai visi kuģi ir nogrimuši
    public boolean allShipsSahk() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols.length(); j++) {
                if (getField(i, j) == 1) {
                    return false;
                }
            }
        }
        return true;
    }


    //testa dati
    public void testData() {
        String coordin = "A10";
        CoordinateToArr c = new CoordinateToArr(coordin);
        System.out.println(c.orig + " " + Integer.toString(c.x) + " " + Integer.toString(c.y) + " " + String.valueOf(c.valid));
        System.out.println("--- Place ships ---");
        System.out.println(placeBattleShip("F3", "F7") == 0);
        System.out.println(placeBattleShip("A1", "D1") == 0);
        System.out.println(placeBattleShip("J7", "J10") == 2);
        System.out.println(placeBattleShip("J10", "J8") == 0);
        System.out.println(placeBattleShip("B9", "D8") == 1);
        System.out.println(placeBattleShip("B9", "D9") == 0);
        System.out.println(placeBattleShip("E6", "D6") == 3);
        System.out.println(placeBattleShip("I2", "J2") == 0);
        System.out.println("-- shooting --");
        System.out.println(makeShot(new CoordinateToArr("F3")) == 0);
        System.out.println(makeShot(new CoordinateToArr("I7")) == 1);
        System.out.println(checkSankShip(new CoordinateToArr("F3")) == false);
        System.out.println(makeShot(new CoordinateToArr("F2")) == 1);
        System.out.println(makeShot(new CoordinateToArr("F4")) == 0);
        System.out.println(makeShot(new CoordinateToArr("F5")) == 0);
        System.out.println(makeShot(new CoordinateToArr("F7")) == 0);
        System.out.println(checkSankShip(new CoordinateToArr("F7")) == false);
        System.out.println(makeShot(new CoordinateToArr("F6")) == 0);
        System.out.println(checkSankShip(new CoordinateToArr("F6")) == true);
        System.out.println(makeShot(new CoordinateToArr("A1")) == 0);
        System.out.println(makeShot(new CoordinateToArr("B1")) == 0);
        System.out.println(makeShot(new CoordinateToArr("C1")) == 0);
        System.out.println(makeShot(new CoordinateToArr("D1")) == 0);
        System.out.println(allShipsSahk() == false);
        System.out.println(makeShot(new CoordinateToArr("B9")) == 0);
        System.out.println(makeShot(new CoordinateToArr("C9")) == 0);
        System.out.println(makeShot(new CoordinateToArr("D9")) == 0);
        System.out.println(makeShot(new CoordinateToArr("I2")) == 0);
        System.out.println(makeShot(new CoordinateToArr("J2")) == 0);
        System.out.println(makeShot(new CoordinateToArr("J8")) == 0);
        System.out.println(makeShot(new CoordinateToArr("J9")) == 0);
        System.out.println(makeShot(new CoordinateToArr("J10")) == 0);
        System.out.println(allShipsSahk() == true);


    }


    //kooridināte uz masīva vērtībām
    class CoordinateToArr {
        public int x = 0;
        public int y = 0;
        public String orig = "";
        public boolean valid = false;
        private String cols = "ABCDEFGHIJ";

        public CoordinateToArr(String coordinate) {
            try {
                orig = coordinate;
                char letter = coordinate.charAt(0);
                x = cols.indexOf(letter);
                y = Integer.parseInt(coordinate.substring(1)) - 1;
                if (y >= 0 && y <= 9 && x >= 0 && x <= 9) valid = true;
            } catch (Exception e) {
                valid = false;
            }
        }
    }


}

