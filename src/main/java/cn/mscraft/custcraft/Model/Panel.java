package cn.mscraft.custcraft.Model;

import java.util.List;

public class Panel {
    private String id;

    private List<Integer> buttonSlots;

    private List<Integer> closeSlots;

    private List<Integer> matrixSlots;

    private List<Integer> resultSlots;

    public Panel(String id, List<Integer> buttonSlots, List<Integer> closeSlots, List<Integer> matrixSlots, List<Integer> resultSlots) {
        this.id = id;
        this.buttonSlots = buttonSlots;
        this.closeSlots = closeSlots;
        this.matrixSlots = matrixSlots;
        this.resultSlots = resultSlots;
    }

    public String getId() {
        return this.id;
    }

    public void setButtonSlots(List<Integer> slots) {
        this.buttonSlots = slots;
    }

    public List<Integer> getButtonSlots() {
        return this.buttonSlots;
    }

    public void setMatrix(List<Integer> matrix) {
        this.matrixSlots = matrix;
    }

    public List<Integer> getMatrixSlots() {
        return this.matrixSlots;
    }

    public void setCloseSlots(List<Integer> slots) {
        this.closeSlots = slots;
    }

    public List<Integer> getCloseSlot() {
        return this.closeSlots;
    }

    public void setResultsSlots(List<Integer> slots) {
        this.resultSlots = slots;
    }

    public List<Integer> getResultsSlots() {
        return this.resultSlots;
    }
}
