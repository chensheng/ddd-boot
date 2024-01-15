package io.github.chensheng.dddboot.excel.core;

import org.apache.poi.ss.usermodel.*;

import java.util.Objects;

public class CellStyleConfig {
    public static final CellStyleConfig EMPTY_STYLE = new CellStyleConfig();

    public static final CellStyleConfig DEFAULT_HEADER_CELL_STYLE;

    static {
        DEFAULT_HEADER_CELL_STYLE = createDefaultStyle();
    }

    private static CellStyleConfig createDefaultStyle() {
        CellStyleConfig cellStyleConfig = new CellStyleConfig();
        FontConfig headerCellFont = new FontConfig();
        headerCellFont.setFontName("宋体");
        headerCellFont.setFontHeightInPoints((short) 14);
        headerCellFont.setBold(true);
        cellStyleConfig.setFont(headerCellFont);
        cellStyleConfig.setWrapText(true);
        cellStyleConfig.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyleConfig.setAlignment(HorizontalAlignment.CENTER);
        cellStyleConfig.setLocked(true);
        cellStyleConfig.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyleConfig.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyleConfig.setBorderBottom(BorderStyle.THIN);
        cellStyleConfig.setBorderLeft(BorderStyle.THIN);
        return cellStyleConfig;
    }

    private FontConfig font;

    private Boolean wrapText;

    private VerticalAlignment verticalAlignment;

    private HorizontalAlignment alignment;

    private Boolean locked;

    private FillPatternType fillPattern;

    private Short fillForegroundColor;

    private BorderStyle borderBottom;

    private BorderStyle borderLeft;

    public FontConfig getFont() {
        return font;
    }

    public void setFont(FontConfig font) {
        this.font = font;
    }

    public Boolean getWrapText() {
        return wrapText;
    }

    public void setWrapText(Boolean wrapText) {
        this.wrapText = wrapText;
    }

    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public HorizontalAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(HorizontalAlignment alignment) {
        this.alignment = alignment;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public FillPatternType getFillPattern() {
        return fillPattern;
    }

    public void setFillPattern(FillPatternType fillPattern) {
        this.fillPattern = fillPattern;
    }

    public Short getFillForegroundColor() {
        return fillForegroundColor;
    }

    public void setFillForegroundColor(Short fillForegroundColor) {
        this.fillForegroundColor = fillForegroundColor;
    }

    public BorderStyle getBorderBottom() {
        return borderBottom;
    }

    public void setBorderBottom(BorderStyle borderBottom) {
        this.borderBottom = borderBottom;
    }

    public BorderStyle getBorderLeft() {
        return borderLeft;
    }

    public void setBorderLeft(BorderStyle borderLeft) {
        this.borderLeft = borderLeft;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CellStyleConfig that = (CellStyleConfig) o;
        return Objects.equals(font, that.font) &&
                Objects.equals(wrapText, that.wrapText) &&
                verticalAlignment == that.verticalAlignment &&
                alignment == that.alignment &&
                Objects.equals(locked, that.locked) &&
                fillPattern == that.fillPattern &&
                Objects.equals(fillForegroundColor, that.fillForegroundColor) &&
                borderBottom == that.borderBottom &&
                borderLeft == that.borderLeft;
    }

    @Override
    public int hashCode() {
        return Objects.hash(font, wrapText, verticalAlignment, alignment, locked, fillPattern, fillForegroundColor, borderBottom, borderLeft);
    }

    public static class FontConfig {
        private String fontName;

        private short fontHeightInPoints;

        private boolean bold;

        public String getFontName() {
            return fontName;
        }

        public void setFontName(String fontName) {
            this.fontName = fontName;
        }

        public short getFontHeightInPoints() {
            return fontHeightInPoints;
        }

        public void setFontHeightInPoints(short fontHeightInPoints) {
            this.fontHeightInPoints = fontHeightInPoints;
        }

        public boolean isBold() {
            return bold;
        }

        public void setBold(boolean bold) {
            this.bold = bold;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FontConfig that = (FontConfig) o;
            return fontHeightInPoints == that.fontHeightInPoints &&
                    bold == that.bold &&
                    Objects.equals(fontName, that.fontName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(fontName, fontHeightInPoints, bold);
        }
    }
}
