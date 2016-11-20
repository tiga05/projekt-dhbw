package stateMachine;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import messages.KafkaMessage;

import java.util.Vector;

/**
 * Created by nicob on 15.11.2016.
 * class for the finite machine
 */

public class FiniteMachine {
    /**
     * enumeration of the possible states
     */
    private enum State {
        L1_ENTRY, L1_EXIT, L2_ENTRY, L2_EXIT, L3_ENTRY, L3_EXIT,
        L4_ENTRY, L4_EXIT, L5_ENTRY, L5_EXIT, MILLING, DRILING,
        MILLING_DONE, DRILLING_DONE,
    }

    /**
     * enumeration of the triggers
     */
    private enum Trigger {
        L1_CLOSED, L1_OPEN, L2_CLOSED, L2_OPEN, L3_CLOSED, L3_OPEN,
        L4_CLOSED, L4_OPEN, L5_CLOSED, L5_OPEN, MILLING_START, MILLING_STOP,
        DRILLING_START, DRILLING_STOP
    }

    //list of all finite machines
    private static Vector<FiniteMachine> allFiniteMachines = new Vector<>();

    //order number of the produced part and the state machine object
    private String orderNumber;
    private StateMachine<State, Trigger> stateMachine;

    private FiniteMachine(String orderNumber) {
        this.orderNumber = orderNumber;
        configStateMachine();
    }

    /**
     *
     * @param orderNumber: number to identify the machine
     */
    public static void createFiniteMachine(String orderNumber) {
        FiniteMachine machine = new FiniteMachine(orderNumber);
        allFiniteMachines.add(machine);
    }

    /**
     *
     * @param kafkaMessage: message that affects a machine
     */
    public static void receiveMessage(KafkaMessage kafkaMessage) {
        for (FiniteMachine finiteMachine : allFiniteMachines) {
            finiteMachine.triggerEvent(kafkaMessage);
        }
    }

    @Override
    public String toString() {
        return "Finite Machine for order " + orderNumber;
    }

    private void triggerEvent(KafkaMessage kafkaMessage) {
        switch (kafkaMessage.getItemName()) {
            case "L1":
                if (!kafkaMessage.isBooleanValue()) {
                    if (stateMachine.canFire(Trigger.L1_CLOSED)) {
                        stateMachine.fire(Trigger.L1_CLOSED);
                    }
                } 
                else {
                    if (stateMachine.canFire(Trigger.L1_OPEN)){
                        stateMachine.fire(Trigger.L1_OPEN);
                    }
                }
                break;
            case "L2":
                if (!kafkaMessage.isBooleanValue()) {
                    if (stateMachine.canFire(Trigger.L2_CLOSED)) {
                        stateMachine.fire(Trigger.L2_CLOSED);
                    }
                }
                else {
                    if (stateMachine.canFire(Trigger.L2_OPEN)){
                        stateMachine.fire(Trigger.L2_OPEN);
                    }
                }
                break;
            case "L3":
                if (!kafkaMessage.isBooleanValue()) {
                    if (stateMachine.canFire(Trigger.L3_CLOSED)) {
                        stateMachine.fire(Trigger.L3_CLOSED);
                    }
                }
                else {
                    if (stateMachine.canFire(Trigger.L3_OPEN)){
                        stateMachine.fire(Trigger.L3_OPEN);
                    }
                }
                break;
            case "L4":
                if (!kafkaMessage.isBooleanValue()) {
                    if (stateMachine.canFire(Trigger.L4_CLOSED)) {
                        stateMachine.fire(Trigger.L4_CLOSED);
                    }
                }
                else {
                    if (stateMachine.canFire(Trigger.L4_OPEN)){
                        stateMachine.fire(Trigger.L4_OPEN);
                    }
                }
                break;
            case "L5":
                if (!kafkaMessage.isBooleanValue()) {
                    if (stateMachine.canFire(Trigger.L5_CLOSED)) {
                        stateMachine.fire(Trigger.L5_CLOSED);
                    }
                }
                else {
                    if (stateMachine.canFire(Trigger.L5_OPEN)){
                        stateMachine.fire(Trigger.L5_OPEN);
                        removeFromAllVector(this);
                    }
                }
                break;
            case "MILLING":
                if (kafkaMessage.isBooleanValue()) {
                    if (stateMachine.canFire(Trigger.MILLING_START)) {
                        stateMachine.fire(Trigger.MILLING_START);
                    }
                }
                else {
                    if (stateMachine.canFire(Trigger.MILLING_STOP)){
                        stateMachine.fire(Trigger.MILLING_STOP);
                    }
                }
                break;
            case "DRILLING":
                if (kafkaMessage.isBooleanValue()) {
                    if (stateMachine.canFire(Trigger.DRILLING_START)) {
                        stateMachine.fire(Trigger.DRILLING_START);
                    }
                }
                else {
                    if (stateMachine.canFire(Trigger.DRILLING_STOP)){
                        stateMachine.fire(Trigger.DRILLING_STOP);
                    }
                }
                break;
        }

        System.out.println(stateMachine.getState());
    }

    //configure state machine
    private void configStateMachine() {
        StateMachineConfig<State, Trigger> machineConfig = new StateMachineConfig<>();

        machineConfig.configure(State.L1_ENTRY)
                .permit(Trigger.L1_OPEN, State.L1_EXIT)
                .permitReentry(Trigger.L1_CLOSED);

        machineConfig.configure(State.L1_EXIT)
                .permit(Trigger.L2_CLOSED, State.L2_ENTRY);

        machineConfig.configure(State.L2_ENTRY)
                .permit(Trigger.L2_OPEN, State.L2_EXIT);

        machineConfig.configure(State.L2_EXIT)
                .permit(Trigger.L3_CLOSED, State.L3_ENTRY);

        machineConfig.configure(State.L3_ENTRY)
                .permit(Trigger.MILLING_START, State.MILLING);

        machineConfig.configure(State.MILLING)
                .permit(Trigger.MILLING_STOP, State.MILLING_DONE);

        machineConfig.configure(State.MILLING_DONE)
                .permit(Trigger.L3_OPEN, State.L3_EXIT);

        machineConfig.configure(State.L3_EXIT)
                .permit(Trigger.L4_CLOSED, State.L4_ENTRY);

        machineConfig.configure(State.L4_ENTRY)
                .permit(Trigger.DRILLING_START, State.DRILING);

        machineConfig.configure(State.DRILING)
                .permit(Trigger.DRILLING_STOP, State.DRILLING_DONE);

        machineConfig.configure(State.DRILLING_DONE)
                .permit(Trigger.L4_OPEN, State.L4_EXIT);

        machineConfig.configure(State.L4_EXIT)
                .permit(Trigger.L5_CLOSED, State.L5_ENTRY);

        machineConfig.configure(State.L5_ENTRY)
                .permit(Trigger.L5_OPEN, State.L5_EXIT);

        //create state machine
        stateMachine = new StateMachine<>(State.L1_ENTRY, machineConfig);
    }

    private void removeFromAllVector(FiniteMachine finiteMachine) {
        System.out.println(finiteMachine.toString() + " is done producing!");
        allFiniteMachines.remove(finiteMachine);
    }
}