package utery_17_25_c04.controller;

import transforms.*;
import utery_17_25_c04.model.Element;
import utery_17_25_c04.model.ElementType;
import utery_17_25_c04.model.Vertex;
import utery_17_25_c04.rasterize.Raster;
import utery_17_25_c04.renderer.GPURenderer;
import utery_17_25_c04.renderer.RendererZBuffer;
import utery_17_25_c04.view.Panel;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller3D {

    private final GPURenderer renderer;
    private final Panel panel;
    private final Raster<Integer> raster;

    private final List<Element> elements;
    private final List<Vertex> vb; // vertex buffer
    private final List<Integer> ib; // index buffer

    private Mat4 model, projection;
    private Camera camera;

    public Controller3D(Panel panel) {
        this.panel = panel;
        this.raster = panel.getRaster();
        this.renderer = new RendererZBuffer(raster);

        elements = new ArrayList<>();
        vb = new ArrayList<>();
        ib = new ArrayList<>();

        initMatrices();
        initListeners(panel);
        initObjects();
    }

    private void initObjects() {
        vb.add(new Vertex(new Point3D(10, 10, 1), Color.RED)); // 0 // ten nejvíce vlevo
        vb.add(new Vertex(new Point3D(100, 300, 1), Color.RED)); // 1 // ten nejvíce dole
        vb.add(new Vertex(new Point3D(200, 50, 1), Color.RED)); // 2 // ten společný
        vb.add(new Vertex(new Point3D(1, 1, 1), Color.RED)); // 3 // ten nejvíce nahoře
        vb.add(new Vertex(new Point3D(1, 1, 1), Color.RED)); // 4 // ten nejvíce vpravo

        vb.add(new Vertex(new Point3D(1, 1, 1), Color.BLUE)); // 5 // ten nahoře
        vb.add(new Vertex(new Point3D(1, 1, 1), Color.BLUE)); // 6 // ten dole

        vb.add(new Vertex(new Point3D(1, 1, 1), Color.GREEN)); // 7 // vpravo
        vb.add(new Vertex(new Point3D(1, 1, 1), Color.GREEN)); // 8 // vlevo

        ib.add(0);
        ib.add(1);
        ib.add(2);
        // tyto 3 indexy tvoří jeden celý trojúhleník

        ib.add(2);
        ib.add(4);
        ib.add(3);
        // tyto 3 indexy tvoří jeden celý trojúhleník

        ib.add(5);
        ib.add(6);
        ib.add(7);
        ib.add(8);
        // tyto 4 indexy tvoří 2 celé úsečky


        // 2 trojúhelníky
        //  - každý má 3 vrcholy (vertexy)
        // 2 úsečky
        //  - kkaždá má 2 vrcholy (vertexy)

        elements.add(new Element(ElementType.TRIANGLE, 0, 6));
        // 0 -> nultý prvek v index bufferu
        // 6 -> použije se 6 indexů - 1 trojúhelník potřebuje 3 indexy, takže budou 2 trojúhelníky

        elements.add(new Element(ElementType.LINE, 6, 4));
        // 6 -> jako první vzít šestý prvek v index bufferu
        // 4 -> chci 2 úsečky a každá úsečka má 2 indexy

        renderer.draw(elements, ib, vb);
//        panel.repaint();
    }

//    private void initObjects2() {
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.RED)); // 0 // ten nejvíce vlevo
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.RED)); // 1 // ten nejvíce dole
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.RED)); // 2 // ten společný
//
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.BLUE)); // 3 // ten nahoře
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.BLUE)); // 4 // ten dole
//
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.GREEN)); // 5 // vpravo
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.GREEN)); // 6 // vlevo
//
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.RED)); // 7 // ten nejvíce nahoře
//        vb.add(new Vertex(new Point3D(1, 1, 1), Color.RED)); // 8 // ten nejvíce vpravo
//
//        ib.add(3);
//        ib.add(4);
//        // tyto 2 indexy tvoří 1 celou úsečku
//
//        ib.add(0);
//        ib.add(1);
//        ib.add(2);
//        // tyto 3 indexy tvoří 1 celý trojúhleník
//
//        ib.add(2);
//        ib.add(7);
//        ib.add(8);
//        // tyto 3 indexy tvoří 1 celý trojúhleník
//
//        ib.add(5);
//        ib.add(6);
//        // tyto 2 indexy tvoří 1 celou úsečku
//
//        elements.add(new Element(ElementType.LINE, 0, 2));
//        elements.add(new Element(ElementType.TRIANGLE, 2, 6));
//        elements.add(new Element(ElementType.LINE, 8, 2));
//    }

    private void initMatrices() {
        model = new Mat4Identity();

        var e = new Vec3D(0, -5, 2);
        camera = new Camera()
                .withPosition(e)
                .withAzimuth(Math.toRadians(90))
                .withZenith(Math.toRadians(-20));

        projection = new Mat4PerspRH(
                Math.PI / 3,
                raster.getHeight() / (float) raster.getWidth(),
                0.5,
                50
        );
    }

    private void initListeners(Panel panel) {
        // TODO
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.getX());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e.isControlDown());
            }
        });
        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("drag");
            }
        });
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode() == KeyEvent.VK_H);
            }
        });
//        MouseAdapter drag = new MouseAdapter() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                System.out.println("drag");
//            }
//        };
//
//        panel.addMouseMotionListener(drag);
//        panel.removeMouseMotionListener(drag);
    }

    private void display() {
        raster.clear();
        renderer.clear();

        renderer.setModel(model);
        renderer.setView(camera.getViewMatrix());
        renderer.setProjection(projection);

        // musíme nakonec říci, že panel má nový obsah zobrazit
        panel.repaint();
    }

}
