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


    public Button(Rectangle bounds, String buttonName, Color defaultColor, Color hoverColor) {

        this.setContentAreaFilled(false);

        // Init colors;
        setDefaultColor(defaultColor);
        setHoverColor(hoverColor);


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

    @Override
    public void paintComponents(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Painting borders;
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        // Setting Borders to 2 pixels of width;
        g2.setColor(getBackground());
        g2.fillRoundRect(2,2,getWidth() - 4, getHeight() - 4, radius, radius);

        super.paintComponents(g);

    }

}