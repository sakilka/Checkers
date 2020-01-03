package org.sample.checkers.importer;


import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

/**
 * MayaGroup - A MayaGroup is equivalent to a Maya Transform Node
 * <p/>
 * If you are post-multiplying matrices, To transform a point p from object-space to world-space you would need to
 * post-multiply by the worldMatrix. (p' = p * wm) matrix = [SP-1][S][SH][SP][ST][RP-1][RA][R][RP][RT][T] where R =
 * [RX][RY][RZ]  (Note: order is determined by rotateOrder)
 * <p/>
 * If you are pre-multiplying matrices, to transform a point p from object-space to world-space you would need to
 * pre-multiply by the worldMatrix. (p' = wm * p) matrix = [T][RT][RP][R][RA][RP-1][ST][SP][SH][S][SP-1] where R =
 * [RZ][RY][RX]  (Note: order is determined by rotateOrder) Of these sub-matrices we can set [RT], [RA], [ST], and [SH]
 * to identity, so matrix = [T][RP][R][RP-1][SP][S][SP-1] matrix = [T][RP][RZ][RY][RX][RP-1][SP][S][SP-1]
 */
public class MayaGroup extends Group {
    Translate t = new Translate();
    Translate rpt = new Translate();  // rotate pivot translate
    Translate rp = new Translate();  // rotate pivot
    Translate rpi = new Translate();  // rotate pivot inverse
    Translate spt = new Translate();  // scale pivot translate
    Translate sp = new Translate();  // scale pivot
    Translate spi = new Translate();  // scale pivot inverse
    // should bind rpi = -rp, but doesn't currently work afaict

    Rotate rx = new Rotate(0, Rotate.X_AXIS);
    Rotate ry = new Rotate(0, Rotate.Y_AXIS);
    Rotate rz = new Rotate(0, Rotate.Z_AXIS);

    Scale s = new Scale();

    public MayaGroup() {
        initTransforms();
    }

    /**
     * Creates mayaGroup with the same set of transforms as given mayaGroup. Children are not copied.
     *
     * @param mayaGroup
     */
    public MayaGroup(MayaGroup mayaGroup) {
        t = mayaGroup.t.clone();
        rpt = mayaGroup.rpt.clone();
        rp = mayaGroup.rp.clone();
        rpi = mayaGroup.rpi.clone();
        sp = mayaGroup.sp.clone();
        spi = mayaGroup.spi.clone();

        rx = mayaGroup.rx.clone();
        ry = mayaGroup.ry.clone();
        rz = mayaGroup.rz.clone();

        s = mayaGroup.s.clone();

        setId(mayaGroup.getId());
        setDepthTest(mayaGroup.getDepthTest());
        setVisible(mayaGroup.isVisible());

        initTransforms();
    }

    private void initTransforms() {
        getTransforms().setAll(t, rpt, rp, rz, ry, rx, rpi, spt, sp, s, spi);
    }
}
