package com.nik7.upgcraft.redstone;


public class RedstoneSimpleLogicElement extends RedstoneLogicElement {


    protected RedstoneSimpleLogicElement(ExpressionType type, int tickToComplete, boolean defaultOutput) {
        this(type, tickToComplete);
        this.output = defaultOutput;
    }

    protected RedstoneSimpleLogicElement(ExpressionType type, int tickToComplete) {
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

    public static final class AND {
        public static RedstoneSimpleLogicElement getNewComponent() {
            return new RedstoneSimpleLogicElement(ExpressionType.AND, 1);
        }
    }

    public static final class OR {
        public static RedstoneSimpleLogicElement getNewComponent() {
            return new RedstoneSimpleLogicElement(ExpressionType.OR, 1);
        }
    }

    public static final class NOT {
        public static RedstoneSimpleLogicElement getNewComponent() {
            return new RedstoneSimpleLogicElement(ExpressionType.NOT, 1, true);
        }
    }

}
