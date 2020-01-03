package org.sample.checkers.importer.types;

import org.sample.checkers.importer.MEnv;
import org.sample.checkers.importer.values.MData;
import org.sample.checkers.importer.values.impl.MArrayImpl;

public class MArrayType extends MDataType {

    MDataType elementType;

    public MArrayType(MEnv env, MDataType elementType) {
        super(env, elementType.getName() + "[]");
        this.elementType = elementType;
    }

    public MDataType getElementType() {
        return elementType;
    }

    public MData createData() {
        return new MArrayImpl(this);
    }

}
