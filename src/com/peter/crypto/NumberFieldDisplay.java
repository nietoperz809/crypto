package com.peter.crypto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Class to draw a NumberField
 */
public class NumberFieldDisplay extends JPanel
{
    private static final long serialVersionUID = 1L;
    /**
     * Reference to field to be drawn
     */
    private NumberField m_field;
    /**
     * Image that is used for colorizing
     */
    private BufferedImage m_image;
    /**
     * Graphics object to draw into image
     */
    private Graphics2D m_imageGraphics;
    /**
     * Flag if image is colorized or grayscale
     */
    private boolean m_useGray = false;
    /**
     * Width of frame
     */
    private final int m_sizex = 400;   // Frame width
    /**
     * Height of frame
     */
    private final int m_sizey = 400;   // Frame height

    /**
     * Constructor
     * @param n Numberfield to display
     * @param show_gray TRUE if graphis is grayscale
     * @param posx x-position (use -1 to center)
     * @param posy y-position (use -1 to center)
     */
    public NumberFieldDisplay (NumberField n, boolean show_gray, int posx, int posy)
    {
        super();

        m_field = n;
        m_useGray = show_gray;
        addHierarchyListener(new HListener());

        JFrame f = new JFrame();
        f.setTitle(n.toString());
        f.add (this);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(m_sizex, m_sizey);

        // Set frame position
        if (posx == -1 || posy == -1)
        {
            // Centered
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screenSize.width - m_sizex) / 2;
            int y = (screenSize.height - m_sizey) / 2;
            f.setLocation(x, y);
        }
        else
        {
            // on given location
            f.setLocation (posx, posy);
        }
        f.setVisible(true);
    }

    /**
     * Constructor Gray or Colored and Centered
     * @param n Numberfield to display
     * @param show_gray TRUE if graphis is grayscale
     */
    public NumberFieldDisplay (NumberField n, boolean show_gray)
    {
        this (n, show_gray, -1, -1);
    }

    /**
     * Constructor ColorField Centered
     * @param n Field to be displayed
     */
    public NumberFieldDisplay (NumberField n)
    {
        this (n, false, -1, -1);
    }

    /**
     * Constructor ColorField, free positionable
     * @param n Numberfield to display
     * @param posx x-position (use -1 to center)
     * @param posy y-position (use -1 to center)
     */
    public NumberFieldDisplay (NumberField n, int posx, int posy)
    {
        this (n, false, posx, posy);
    }

    public void newField (NumberField n)
    {
        m_field = n;
        m_image = (BufferedImage) createImage(m_field.getWidth(), m_field.getHeight());
        m_imageGraphics = m_image.createGraphics();
        createImage();
        repaint();
    }

    /**
     * Creates an image for this Viewer
     *
     * 0: red up		r:x g:0 b:0   0...255
     * 1: green up		r:1 g:x b:0   256...511
     * 2: red down		r:x g:1 b:0   512...767
     * 3: blue up		r:0 g:1 b:x   768...1023
     * 4: green down	r:0 g:x b:1   1024...1279
     * 5: red up		r:x g:0 b:1   1280...1535
     * 6: geen up 		r:1 g:x b:1   1536...1791
     *
     */
    private void createImage()
    {
        final int h = m_field.getHeight();
        final int w = m_field.getWidth();
        final Map<Double, Double> map = m_field.calcStats();
        final Set<Double> set = map.keySet();
        final Double maxvalue = Collections.max(set);
        final Double minvalue = Collections.min(set);
        final double div = (maxvalue - minvalue) / 1791.0f;
        final double min = minvalue / div;
        final int zero = 120;
        final int full = 255;
        Color c = null;

        for (int a = 0; a < h; a++)
        {
            for (int b = 0; b < w; b++)
            {
                final int xx = (int) (m_field.values[a][b] / div - min);

                if (m_useGray)
                {
                    int col = xx/14 + 128;
                    c = new Color (col,col,col);
                }
                else
                {
                    final int idx = xx / 256;
                    final int offplus = 128 + (xx % 256) / 2;
                    final int offminus = 255 + 128 - offplus;

                    switch (idx)
                    {
                        case 0:
                            c = new Color(offplus, zero, zero);
                            break;

                        case 1:
                            c = new Color(full, offplus, zero);
                            break;

                        case 2:
                            c = new Color(offminus, full, zero);
                            break;

                        case 3:
                            c = new Color(zero, full, offplus);
                            break;

                        case 4:
                            c = new Color(zero, offminus, full);
                            break;

                        case 5:
                            c = new Color(offplus, zero, full);
                            break;

                        case 6:
                            c = new Color(full, offplus, full);
                            break;
                    }
                }
                m_imageGraphics.setColor(c);
                m_imageGraphics.drawRect(b, a, 1, 1);
            }
        }
    }

    /**
     * Helper class to show the image when frame becomes visible
     */
    private class HListener implements HierarchyListener
    {
        public void hierarchyChanged(HierarchyEvent e)
        {
            if ((e.getChangeFlags() & HierarchyEvent.DISPLAYABILITY_CHANGED) != 0 && NumberFieldDisplay.this.isDisplayable())
            {
                m_image = (BufferedImage) createImage(m_field.getWidth(), m_field.getHeight());
                m_imageGraphics = m_image.createGraphics();
                createImage();
            }
        }
    }

    /**
     * Overridden draw function
     * Simply draws the image
     * @param g Graphics object of the Frame
     */
    @Override
    public void paint(Graphics g)
    {
        g.drawImage(m_image, 0, 0, getWidth(), getHeight(), this);
    }
}
