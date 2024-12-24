package components;

import java.awt.*;

import javax.swing.JButton;

public class Button extends JButton {

    private boolean isHovering;
    private Color defaultColor;
    private Color hoverColor;
    private Color colorClick;
    private Color borderColor;
    private int radius = 0;


    public Button(Rectangle bounds, String buttonName) {

        this.setContentAreaFilled(false);


        this.setBounds(bounds);
        this.setText(buttonName);
        this.setFocusable(false);
        this.setLayout(null);
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getColorClick() {
        return colorClick;
    }

    public void setColorClick(Color colorClick) {
        this.colorClick = colorClick;
    }

    public Color getDefaultColor() {
        return defaultColor;
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    public boolean isHovering() {
        return isHovering;
    }

    public void setHovering(boolean hovering) {
        isHovering = hovering;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }
}