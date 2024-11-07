package org.example;
import java.util.Scanner;
interface State {
    void attack(Character character); // defines how a character attacks
}
class NormalState implements State {
    public void attack(Character character) {
        System.out.println("💥 Character attacks with standard strength!");
        character.setStrength(character.getStrength() + 1); // increases strength a bit
    }
}

class PoweredUpState implements State {
    public void attack(Character character) {
        System.out.println("🔥 Character attacks with powerful strength!");
        character.setStrength(character.getStrength() + 5); // boosts strength more
    }
}

class DefeatedState implements State {
    public void attack(Character character) {
        System.out.println("💀 character is defeated and cannot attack.");
        character.setStrength(0); // set strength to 0 when defeated
    }
}

// --------------------- Strategy Pattern ---------------------

interface Strategy {
    void fight(Character character); // defines a fight action
}

class MeleeStrategy implements Strategy {
    public void fight(Character character) {
        System.out.println("🪓 character fights up close with melee attacks!");
        character.setHp(character.getHp() - 10); // reduces hp in melee fights
    }
}

class RangedStrategy implements Strategy {
    public void fight(Character character) {
        System.out.println("🏹 character fights from afar with ranged attacks!");
        character.setHp(character.getHp() - 5); // less hp loss in ranged attacks
    }
}

class MagicStrategy implements Strategy {
    public void fight(Character character) {
        System.out.println("✨ character casts magical spells!");
        character.setHp(character.getHp() - 15); // higher hp loss for magic
    }
}

// --------------------- Template Method Pattern ---------------------

abstract class GameAction {
    public final void executeAction(Character character) {
        start(); // show starting message
        performAction(character); // perform the main action
        end(); // show end message
    }

    protected void start() {
        System.out.println("🔸 preparing action...");
    }

    protected abstract void performAction(Character character);

    protected void end() {
        System.out.println("🔸 action completed.\n");
    }
}

class AttackAction extends GameAction {
    protected void performAction(Character character) {
        character.attack(); // character attacks
    }
}

class DefendAction extends GameAction {
    protected void performAction(Character character) {
        System.out.println("🛡️ character defends against attacks.");
    }
}

class HealAction extends GameAction {
    protected void performAction(Character character) {
        System.out.println("💖 character heals for some health.");
        character.setHp(character.getHp() + 20); // increases hp when healed
    }
}

// --------------------- Visitor Pattern ---------------------

interface EffectVisitor {
    void applyBoost(Character character);
    void applyDamage(Character character);
}

class BoostEffect implements EffectVisitor {
    public void applyBoost(Character character) {
        System.out.println("✨ character feels stronger!");
        character.setStrength(character.getStrength() + 10); // boosts strength
    }

    public void applyDamage(Character character) {
        // not applicable for boost effect
    }
}

class DamageEffect implements EffectVisitor {
    public void applyDamage(Character character) {
        System.out.println("💥 character takes heavy damage!");
        character.setHp(character.getHp() - 30); // reduces hp
    }

    public void applyBoost(Character character) {
        // not applicable for damage effect
    }
}

// --------------------- Character Class ---------------------

class Character {
    private State currentState;
    private Strategy currentStrategy;
    private int hp;                   // health points
    private int strength;
    private static final int MAX_HP = 100;
    private static final int MAX_STRENGTH = 100;

    public Character() {
        this.hp = 100;
        this.strength = 10; // can increase with actions
        this.currentState = new NormalState();
        this.currentStrategy = new MeleeStrategy();
    }

    public void setState(State state) {
        this.currentState = state; // update character’s state
        System.out.println("🌀 character state changed to: " + state.getClass().getSimpleName());
    }

    public void setStrategy(Strategy strategy) {
        this.currentStrategy = strategy; // update character’s fighting strategy
        System.out.println("⚔️ character strategy changed to: " + strategy.getClass().getSimpleName());
    }

    public int getHp() {
        return hp; // return current hp
    }

    public void setHp(int hp) {
        this.hp = Math.min(hp, MAX_HP);  // cap hp to max limit
    }

    public int getStrength() {
        return strength; // return current strength
    }

    public void setStrength(int strength) {
        this.strength = Math.min(strength, MAX_STRENGTH); // cap strength to max limit
    }

    public void displayStatus() {
        System.out.println("\n✨ Character status ✨");
        System.out.println("State: " + currentState.getClass().getSimpleName());
        System.out.println("Strategy: " + currentStrategy.getClass().getSimpleName());
        System.out.println("Hp: " + hp + "/" + MAX_HP);
        System.out.println("Strength: " + strength + "/" + MAX_STRENGTH);
        System.out.println("---------------------------");
    }

    public void attack() {
        currentState.attack(this); // perform an attack based on state
    }

    public void fight() {
        currentStrategy.fight(this); // perform a fight based on strategy
    }
}

// --------------------- Main Game Class ---------------------

class Game {
    private static Character character = new Character();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("🎮 welcome to the adventure game!");

        boolean exit = false;
        while (!exit) {
            character.displayStatus(); // show character status
            showMainMenu(); // display main menu

            int choice = getInput("choose an option (1-5): ");

            switch (choice) {
                case 1 -> changeState(); // change character state
                case 2 -> selectStrategy(); // select fighting strategy
                case 3 -> performAction(); // perform a game action
                case 4 -> applyEffect(); // apply an effect on character
                case 5 -> exit = true; // exit game
                default -> System.out.println("invalid option. please try again.");
            }
        }
        System.out.println("thank you for playing! see you on your next adventure!");
    }

    private static void showMainMenu() {
        System.out.println("\n🌟 game menu 🌟");
        System.out.println("1. change character state");
        System.out.println("2. select fighting strategy");
        System.out.println("3. perform game action");
        System.out.println("4. apply effect to character");
        System.out.println("5. exit");
        System.out.println("────────────────────────────────");
    }

    // method to change character state based on user input
    private static void changeState() {
        System.out.println("\n🔄 choose a new state:");
        System.out.println("1. normal");
        System.out.println("2. powered up");
        System.out.println("3. defeated");
        System.out.println("4. go back");

        int choice = getInput("select state (1-4): ");

        switch (choice) {
            case 1 -> character.setState(new NormalState());     // set to normal state
            case 2 -> character.setState(new PoweredUpState());  // set to powered up state
            case 3 -> character.setState(new DefeatedState());   // set to defeated state
            case 4 -> System.out.println("returning to main menu..."); // go back
            default -> System.out.println("invalid option. please try again.");
        }
    }

    // method to select a fighting strategy
    private static void selectStrategy() {
        System.out.println("\n⚔️ choose a new fighting strategy:");
        System.out.println("1. melee");
        System.out.println("2. ranged");
        System.out.println("3. magic");
        System.out.println("4. go back");

        int choice = getInput("select strategy (1-4): ");

        switch (choice) {
            case 1 -> character.setStrategy(new MeleeStrategy());   // set to melee strategy
            case 2 -> character.setStrategy(new RangedStrategy());  // set to ranged strategy
            case 3 -> character.setStrategy(new MagicStrategy());   // set to magic strategy
            case 4 -> System.out.println("returning to main menu..."); // go back
            default -> System.out.println("invalid option. please try again.");
        }
    }

    // method to perform different actions
    private static void performAction() {
        System.out.println("\n🎬 choose an action:");
        System.out.println("1. attack");
        System.out.println("2. defend");
        System.out.println("3. heal");
        System.out.println("4. go back");

        int choice = getInput("select action (1-4): ");
        GameAction action;

        switch (choice) {
            case 1 -> action = new AttackAction(); // create attack action
            case 2 -> action = new DefendAction(); // create defend action
            case 3 -> action = new HealAction();   // create heal action
            case 4 -> {
                System.out.println("returning to main menu...");
                return; // go back
            }
            default -> {
                System.out.println("invalid option. please try again.");
                return;
            }
        }
        action.executeAction(character); // execute the selected action
    }

    // method to apply effects on character
    private static void applyEffect() {
        System.out.println("\n💥 choose an effect:");
        System.out.println("1. boost");
        System.out.println("2. damage");
        System.out.println("3. go back");

        int choice = getInput("select effect (1-3): ");
        EffectVisitor effect;

        switch (choice) {
            case 1 -> effect = new BoostEffect(); // create boost effect
            case 2 -> effect = new DamageEffect(); // create damage effect
            case 3 -> {
                System.out.println("returning to main menu...");
                return; // go back
            }
            default -> {
                System.out.println("invalid option. please try again.");
                return;
            }
        }

        // apply selected effect
        effect.applyBoost(character);
        effect.applyDamage(character);
    }

    // method to get user input
    private static int getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextInt(); // read user input
    }
}
