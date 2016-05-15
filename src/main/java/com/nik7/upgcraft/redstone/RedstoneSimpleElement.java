package com.nik7.upgcraft.redstone;


public class RedstoneSimpleElement extends RedstoneElement {


    public RedstoneSimpleElement(ExpressionType type, int tickToComplete, boolean defaultOutput) {
        this(type, tickToComplete);
        this.output = defaultOutput;
    }

    public RedstoneSimpleElement(ExpressionType type, int tickToComplete) {
        super(type, tickToComplete);
    }

    @Override
    protected void myExec() {

        if (getCurrentTik() == 0 && type.isBooleanExpression()) {
            boolean[] inputs = new boolean[getInputsPort().length];
            short ports[] = getInputsPort();

            for (short p : ports) {

                inputs[p] = connections[p].getOutputValue(this, p);

            }

            output = RedstoneUpgC.evaluateBooleanExpression(inputs[0], inputs[1], inputs[2], type);
        }

    }

}
