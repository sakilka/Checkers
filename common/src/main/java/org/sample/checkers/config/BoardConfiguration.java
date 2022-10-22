package org.sample.checkers.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class BoardConfiguration {

    @Value("${width}")
    private float width;

    @Value("${height}")
    private float height;

    @Value("${right.panel.width}")
    private float rightPanelWidth;

    @Value("${angle.x}")
    private double angleX;

    @Value("${angle.y}")
    private double angleY;

    @Value("${delta.x}")
    private double deltaX;

    @Value("${delta.z}")
    private double deltaZ;

    @Value("${translate.z}")
    private double translateZ;

    @Value("${field.width}")
    private float fieldWidth;

    @Value("${field.height}")
    private float fieldHeight;

    @Value("${field.depth}")
    private float fieldDepth;

    @Value("${field.gap}")
    private float fieldGap;

    @Value("${field.gap.shift}")
    private float fieldGapShift;

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getRightPanelWidth() {
        return rightPanelWidth;
    }

    public void setRightPanelWidth(float rightPanelWidth) {
        this.rightPanelWidth = rightPanelWidth;
    }

    public double getAngleX() {
        return angleX;
    }

    public void setAngleX(double angleX) {
        this.angleX = angleX;
    }

    public double getAngleY() {
        return angleY;
    }

    public void setAngleY(double angleY) {
        this.angleY = angleY;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public void setDeltaZ(double deltaZ) {
        this.deltaZ = deltaZ;
    }

    public float getFieldWidth() {
        return fieldWidth;
    }

    public double getTranslateZ() {
        return translateZ;
    }

    public void setTranslateZ(double translateZ) {
        this.translateZ = translateZ;
    }

    public void setFieldWidth(float fieldWidth) {
        this.fieldWidth = fieldWidth;
    }

    public float getFieldHeight() {
        return fieldHeight;
    }

    public void setFieldHeight(float fieldHeight) {
        this.fieldHeight = fieldHeight;
    }

    public float getFieldDepth() {
        return fieldDepth;
    }

    public void setFieldDepth(float fieldDepth) {
        this.fieldDepth = fieldDepth;
    }

    public float getFieldGap() {
        return fieldGap;
    }

    public void setFieldGap(float fieldGap) {
        this.fieldGap = fieldGap;
    }

    public float getFieldGapShift() {
        return fieldGapShift;
    }

    public void setFieldGapShift(float fieldGapShift) {
        this.fieldGapShift = fieldGapShift;
    }
}
