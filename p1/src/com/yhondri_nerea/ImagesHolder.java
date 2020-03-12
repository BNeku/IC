package com.yhondri_nerea;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;

public class ImagesHolder {
    final public ImageIcon barroImage = new ImageIcon(ClassLoader.getSystemResource("barro.png"));
    final public ImageIcon castilloImage = new ImageIcon(ClassLoader.getSystemResource("Castillo.png"));
    final public ImageIcon exploradoImage = new ImageIcon(ClassLoader.getSystemResource("explorado.png"));
    final public ImageIcon inicioImage = new ImageIcon(ClassLoader.getSystemResource("Inicio.png"));
    final public ImageIcon metaImage = new ImageIcon(ClassLoader.getSystemResource("Meta.png"));
    final public ImageIcon paredImage = new ImageIcon(ClassLoader.getSystemResource("Pared.png"));
    final public ImageIcon pointImage = new ImageIcon(ClassLoader.getSystemResource("Point.png"));
    final public ImageIcon stepsImage = new ImageIcon(ClassLoader.getSystemResource("Steps.png"));

    public ImagesHolder() throws IOException {
    }
}
