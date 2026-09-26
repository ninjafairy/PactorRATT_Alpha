package com.pactorratt.alpha.ui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.JViewport;

/**
 * FlowLayout that wraps components onto additional rows when the container is narrow.
 */
public final class WrapLayout extends FlowLayout {

    public WrapLayout() {
        this(LEFT, 4, 2);
    }

    public WrapLayout(int align, int hgap, int vgap) {
        super(align, hgap, vgap);
    }

    @Override
    public Dimension preferredLayoutSize(Container target) {
        return layoutSize(target, true);
    }

    @Override
    public Dimension minimumLayoutSize(Container target) {
        Dimension minimum = layoutSize(target, false);
        minimum.width -= (getHgap() + 1);
        return minimum;
    }

    @Override
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            Insets insets = target.getInsets();
            int maxWidth = availableWidth(target) - (insets.left + insets.right + getHgap() * 2);
            if (maxWidth <= 0) {
                maxWidth = Integer.MAX_VALUE;
            }
            int x = insets.left + getHgap();
            int y = insets.top + getVgap();
            int rowHeight = 0;
            int startX = x;

            int count = target.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component m = target.getComponent(i);
                if (!m.isVisible()) {
                    continue;
                }
                Dimension d = m.getPreferredSize();
                m.setSize(d.width, d.height);
                if (x - startX + d.width > maxWidth && x > startX) {
                    x = startX;
                    y += rowHeight + getVgap();
                    rowHeight = 0;
                }
                m.setLocation(x, y);
                x += d.width + getHgap();
                rowHeight = Math.max(rowHeight, d.height);
            }
        }
    }

    private Dimension layoutSize(Container target, boolean preferred) {
        Dimension dim;
        synchronized (target.getTreeLock()) {
            int targetWidth = availableWidth(target);

            int hgap = getHgap();
            int vgap = getVgap();
            Insets insets = target.getInsets();
            int maxWidth = targetWidth - (insets.left + insets.right + hgap * 2);

            dim = new Dimension(0, 0);
            int rowWidth = 0;
            int rowHeight = 0;

            int count = target.getComponentCount();
            for (int i = 0; i < count; i++) {
                Component m = target.getComponent(i);
                if (!m.isVisible()) {
                    continue;
                }
                Dimension d = preferred ? m.getPreferredSize() : m.getMinimumSize();
                if (rowWidth + d.width > maxWidth && rowWidth > 0) {
                    dim.width = Math.max(dim.width, rowWidth);
                    dim.height += rowHeight + vgap;
                    rowWidth = 0;
                    rowHeight = 0;
                }
                if (rowWidth != 0) {
                    rowWidth += hgap;
                }
                rowWidth += d.width;
                rowHeight = Math.max(rowHeight, d.height);
            }

            dim.width = Math.max(dim.width, rowWidth);
            dim.height += rowHeight;
            dim.width += insets.left + insets.right + hgap * 2;
            dim.height += insets.top + insets.bottom + vgap * 2;
            if (target.getParent() instanceof JViewport viewport && viewport.getWidth() > 0) {
                dim.width = viewport.getWidth();
            }
        }
        return dim;
    }

    /**
     * Width the buttons must fit. Inside a scroll pane this is the viewport, so
     * narrowing the window wraps instead of keeping the panel as wide as one row.
     */
    private static int availableWidth(Container target) {
        Container parent = target.getParent();
        if (parent instanceof JViewport viewport && viewport.getWidth() > 0) {
            return viewport.getWidth();
        }
        if (target.getWidth() > 0) {
            return target.getWidth();
        }
        return Integer.MAX_VALUE;
    }
}
