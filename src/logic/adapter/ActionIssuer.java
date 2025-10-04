package logic.adapter;

/**
 * This interface defines the set of actions an AI or player can issue.
 * It is implemented by BattleActionAdapter.
 */
public interface ActionIssuer {
    void doNothing();
    void move(int distance);
    void vulcan();
    void beamSaber();
    void defendEvade();
    void weaponDeployment();
    void ultimateSkill();
}