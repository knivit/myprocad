package com.tsoft.myprocad.swing;

import com.tsoft.myprocad.MyProCAD;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.dialog.AbstractDialogPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class BeamPanel extends AbstractDialogPanel implements Printable {
    private JLabel[] labels = new JLabel[8];
    private String[] labelsText = new String[] {
            L10.get(L10.CALCULATION_BEAM_COORDS_FIG),
            L10.get(L10.CALCULATION_BEAM_ABOUT_FIG),
            L10.get(L10.CALCULATION_BEAM_BEAM_FIG),
            L10.get(L10.CALCULATION_BEAM_SHEARING_FORCES_FIG),
            L10.get(L10.CALCULATION_BEAM_BENDING_MOMENTS_FIG),
            L10.get(L10.CALCULATION_BEAM_NAPR_FIG),
            L10.get(L10.CALCULATION_BEAM_ROTATIONS_FIG),
            L10.get(L10.CALCULATION_BEAM_MOVEMENTS_FIG),
    };
    private int imageIndex = 0;

    private JSplitPane splitPane;
    private JEditorPane textPane;

    public BeamPanel() {
        super(new BorderLayout());

        // center
        textPane = new JEditorPane();
        textPane.setContentType("text/html");
        JScrollPane textScrollPane = new JScrollPane(textPane);

        // right
        labels[imageIndex ++] = new JLabel(new ImageIcon(MyProCAD.class.getResource("resources/calculations/Beam1.gif")));
        labels[imageIndex ++] = new JLabel(new ImageIcon(MyProCAD.class.getResource("resources/calculations/Beam2.gif")));
        for (int i = 2; i < labels.length; i ++) {
            labels[i] = new JLabel();
            labels[i].setOpaque(false);
        }

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        panel.setBackground(Color.WHITE);

        for (int i = 0; i < labels.length; i ++) {
            JPanel figPanel = new JPanel();
            figPanel.setOpaque(false);
            figPanel.setLayout(new BoxLayout(figPanel, BoxLayout.LINE_AXIS));
            figPanel.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
            figPanel.add(Box.createHorizontalGlue());
            figPanel.add(labels[i]);
            figPanel.add(Box.createHorizontalGlue());
            panel.add(figPanel);

            JPanel labelPanel = new JPanel();
            labelPanel.setOpaque(false);
            labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.LINE_AXIS));
            labelPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
            JLabel label = new JLabel(labelsText[i]);
            labelPanel.add(Box.createHorizontalGlue());
            labelPanel.add(label);
            labelPanel.add(Box.createHorizontalGlue());

            panel.add(labelPanel);
        }

        JScrollPane picScrollPane = new JScrollPane(panel);
        picScrollPane.setOpaque(false);
        picScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, textScrollPane, picScrollPane);
        splitPane.setDividerLocation(350);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.3);
        splitPane.setBorder(null);
    }

    public void addImage(BufferedImage bufferedImage) {
        ImageIcon icon = new ImageIcon(bufferedImage);
        labels[imageIndex ++].setIcon(icon);
    }

    public void setText(String text) {
        String t1 = "<center><b>Полный расчёт балки на двух опорах</b></center>" +
                "<center>под действием произвольной системы изгибающих моментов, сосредоточенных сил и равномерно распределённых нагрузок, " +
                "расположенных в вертикальной плоскости.</center><br><br>" +
                "<center><b>Исходные данные</b></center>" +
                "Выберем систему координат так, как показано на рис.1. " +
                "Начало координат O поместим на левом краю (в жёсткой заделке), ось Oz направим вдоль оси балки, " +
                "а оси Ox и Oy − вдоль главных центральных осей инерции. Все силовые факторы считаем действующими в плоскости yOz.<br>" +
                "Будем использовать правило знаков <b>плюс-плюс-плюс-плюс</b>:<br>" +
                "1) Угол поворота сечения θ есть производная от вертикального перемещения w, взятая со знаком плюс;<br>" +
                "2) Изгибающий момент в сечении M есть производная от угла поворота θ, умноженного на EJx, взятая со знаком плюс;<br>" +
                "3) Перерезывающая сила в сечении Q есть производная от изгибающего момента M, взятая со знаком плюс;<br>" +
                "4) Распределённая нагрузка q есть производная от перерезывающей силы Q, взятая со знаком плюс:<br>" +
                "θ(z) = w'(z)<br>" +
                "M(z) = EJxθ'(z)<br>" +
                "Q(z) = M'(z)<br>" +
                "q(z) = Q'(z)<br><br>" +
                "Выберем положительное направление прогиба w(z) вверх, в сторону положительного направления оси Oy, а отрицательное − вниз. " +
                "Тогда положительные значения углов поворота θ(z) будут соответствовать возрастанию прогиба w(z), а отрицательные − убыванию. " +
                "Изгибающий момент − это вторая производная от прогиба (с точностью до положительного множителя) и первая производная от " +
                "угла поворота θ(z) (опять-таки с точностью до положительного множителя); поэтому положительное значение момента M(z) соответствует " +
                "увеличению угла поворота θ(z), т.е. изгибу балки выпуклостью вниз, а отрицательный M(z) − изгибу выпуклостью вверх.<br>" +
                "При построении эпюр будем разрезать балку в данном сечении z, отбрасывать левую часть и заменять её эквивалентной системой " +
                "сил и моментов. Положительное значение M(z) (выпуклостью вниз) при этом даст момент, направленный по <b>часовой стрелке</b>.<br><br>" +
                "При задании исходных данных будем считать:<br>" +
                "1) Распределённую нагрузку q положительной, если она направлена вверх;<br>" +
                "2) Сосредоточенную нагрузку F положительной, если она направлена вверх;<br>" +
                "3) Сосредоточенный момент M положительным, если он направлен по часовой стрелке.<br>" +
                "При построении эпюр<br>" +
                "1) Перемещение w положительным, если оно направлено вверх;<br>" +
                "2) Положительный угол поворота θ соответствует возрастанию w;<br>" +
                "3) Положительный изгибающий момент M соответствует возрастанию θ;<br>" +
                "4) Положительная перерезывающая сила Q соответствует возрастанию M;<br>" +
                "5) Положительная распределённая нагрузка q соответствует возрастанию Q.<br><br>" +
                "Длина балки L, места расположения опор a и b и нагрузка на неё: значения M, F, q и точки (интервалы) их приложения.<br>" +
                "Для подбора двутаврового сечения из условий прочности нужно также задать модуль упругости E и допускаемое напряжение [σ].<br><br>";

        textPane.setText("<html><body style='font-size: 22'>" + t1 + text + "</body></html>");
        textPane.setCaretPosition(0);
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return 0;
    }
}
