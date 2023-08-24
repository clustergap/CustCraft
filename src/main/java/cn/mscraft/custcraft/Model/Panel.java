package cn.mscraft.custcraft.Model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Panel {
    private String id;

    private Boolean debug;

    private int row;

    private String title;

    private Boolean isButton;

    private List<Integer> buttonSlots;

    private List<Integer> matrixSlots;

    private List<Integer> resultSlots;

    public Panel(String id, Boolean debug, int row, String title, Boolean isButton, List<Integer> buttonSlots, List<Integer> matrixSlots, List<Integer> resultSlots) {
        this.id = id;
        this.debug = debug;
        this.row = row;
        this.title = title;
        this.isButton = isButton;
        this.buttonSlots = buttonSlots;
        this.matrixSlots = matrixSlots;
        this.resultSlots = resultSlots;
    }

    public String getId() {
        return this.id;
    }

    public Boolean getDebug() {
        return this.debug;
    }

    public int getRow() {
        return this.row;
    }

    public String getTitle() {
        return this.title;
    }

    public Boolean getIsButton() {
        return this.isButton;
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

    public void setResultsSlots(List<Integer> slots) {
        this.resultSlots = slots;
    }

    public List<Integer> getResultsSlots() {
        return this.resultSlots;
    }
}
