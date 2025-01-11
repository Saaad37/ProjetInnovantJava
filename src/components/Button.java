package components;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class Button extends JButton {

    private boolean isHovering;
    private Color defaultColor;
    private Color hoverColor;
    private Color colorClick;
    private Color borderColor;
    private int radius;


    public Button(Rectangle bounds, String buttonName, Color defaultColor, Color hoverColor, int radius) {

        // Init colors;
        setRadius(radius);
        setDefaultColor(defaultColor);
        setHoverColor(hoverColor);
        setBackground(getDefaultColor());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(getHoverColor());
                setHovering(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(getDefaultColor());
                setHovering(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(getColorClick());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(isHovering()){
                    setBackground(getHoverColor());
                }else{
                    setBackground(getDefaultColor());
                }
            }
        });

//        this.setContentAreaFilled(false);
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
        setBackground(defaultColor);
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